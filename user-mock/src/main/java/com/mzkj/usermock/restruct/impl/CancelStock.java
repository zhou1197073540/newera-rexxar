package com.mzkj.usermock.restruct.impl;

import com.mzkj.usermock.bean.StockOperation;
import com.mzkj.usermock.bean_rmi.StockOrderRMI;
import com.mzkj.usermock.constant.Const;
import com.mzkj.usermock.enums.CodeEnum;
import com.mzkj.usermock.exception.runtime.RequestException;
import com.mzkj.usermock.exception.runtime.SettlementException;
import com.mzkj.usermock.mapper.MockMapper;
import com.mzkj.usermock.restruct.AbstrackStock;
import com.mzkj.usermock.service.AAccountService;
import com.mzkj.usermock.service.MockService;
import com.mzkj.usermock.utils.MathUtil;
import com.mzkj.usermock.utils.RedisUtil;
import com.mzkj.usermock.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("cancelStock")
public class CancelStock extends AbstrackStock {
    private static Logger logger= LoggerFactory.getLogger(SellStock.class);
    @Autowired
    MockService mockService;
    @Autowired
    AAccountService aAccountService;
    @Autowired
    MockMapper mockMapper;
    @Override
    public boolean checkMoney(StockOperation stock) throws Exception {
        return false;
    }

    @Override
    @Transactional
    public String dealOperateion(StockOperation stock) throws RequestException{
        try {
            mockService.saveStockOperation(stock);
            if(!mockService.stockRMIs(stock)){
                throw new RequestException(CodeEnum.REQUEST_RMI_ERROR);
            }
            return "委托已提交";
        }catch (RequestException e1){
            throw e1;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            throw new RequestException(CodeEnum.ERROR);
        }
    }

    @Override
    @Transactional
    public boolean settlementOperation(StockOrderRMI stock) throws SettlementException {
        String key_lock="";
        try {
            StockOrderRMI res = mockMapper.selectResultByOrderNum(stock.getOid());
            if (null == res) {
                throw new SettlementException(CodeEnum.ORDER_NUM_NOT_EXIST);
            }
            if(!checkSettle(stock.getOid())){
                throw new SettlementException(CodeEnum.ORDER_SETTLE_REPEAT);
            }
            key_lock="settle_"+res.getUserAccount();
            if(!getSettleLock(key_lock)){
                throw new SettlementException(CodeEnum.FREQUENT_SETTLEMENT,stock);
            }
            if (res.getOperation().equals(Const.BUY)) {
                //取消买股票结算的时候，需要对账户加钱操作
                String calAvaiAssets = MathUtil.mul2str(res.getPrice(), res.getAmount());
                boolean dealmoney = mockService.addSubMoney(res.getUserAccount(), calAvaiAssets, Const.ADD_MONEY);
                if (!dealmoney) {
                    throw new SettlementException(CodeEnum.MONEY_SETTLEMENT_ERROR);
                }
                mockService.updateTradingStatusFinish(stock.getOid(), Const.TRADING_STATUS_CANCEL);
                logger.info("cancel买股票结算结果,账户:{},流水单号:{},操作类型:{},价钱:{}",
                        res.getUserAccount(), res.getOid(), res.getOperation(), res.getPrice());
                return true;
            } else if (res.getOperation().equals(Const.SALE)) {
                StockOperation ops = mockMapper.selectCostByAccount(res);
                //取消卖股票结算的时候，需要对账户加可用持仓操作
                String calAvaiPostion = MathUtil.add(res.getAmount(), ops.getPosition_available()).toString();
                ops.setPosition_available(calAvaiPostion);
                int num = mockMapper.updateAvaiPosition(ops);
                if (num <= 0) {
                    throw new SettlementException(CodeEnum.AVI_POSITION_ADD_ERROR);
                }
                mockMapper.updateTradingStatusFinish(stock.getOid(), Const.TRADING_STATUS_CANCEL);

                logger.info("cancel卖股票结算结果,账户:{},流水单号:{},操作类型:{}",
                        res.getUserAccount(), res.getOid(), res.getOperation());
                return true;
            }
            return false;
        } catch (SettlementException e1) {
            throw e1;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new SettlementException(CodeEnum.ERROR);
        }finally {
            if(!StringUtil.isEmpty(key_lock)){
                RedisUtil.releaseDistributedLock(key_lock);
            }
        }
    }
}
