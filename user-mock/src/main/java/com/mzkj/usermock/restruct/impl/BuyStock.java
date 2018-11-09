package com.mzkj.usermock.restruct.impl;

import com.mzkj.usermock.bean.StockOperation;
import com.mzkj.usermock.bean_rmi.StockOrderRMI;
import com.mzkj.usermock.constant.Const;
import com.mzkj.usermock.dto.RespResult;
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
import com.mzkj.usermock.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("buyStock")
public class BuyStock extends AbstrackStock {
    private static Logger logger= LoggerFactory.getLogger(BuyStock.class);
    @Autowired
    AAccountService aAccountService;
    @Autowired
    MockService mockService;
    @Autowired
    MockMapper mockMapper;

    @Override
    public boolean checkMoney(StockOperation stock) {
        String calMoney = MathUtil.mul2str(stock.getNum(), stock.getPrice());
        return mockService.addSubMoney(stock.getA_account(), calMoney, Const.SUB_MONEY);
    }

    @Override
    @Transactional
    public String dealOperateion(StockOperation stock) throws RequestException {
        try {
            String calMoney = MathUtil.mul2str(stock.getNum(), stock.getPrice());
            boolean res =mockService.addSubMoney(stock.getA_account(), calMoney, Const.SUB_MONEY);
            if(!res){
                throw new RequestException(CodeEnum.WALLET_NOT_ENOUGHT_ERROR);
            }
            mockService.saveStockOperation(stock);
            boolean resRmi=mockService.stockRMIs(stock);
            if(!resRmi){
                throw new RequestException(CodeEnum.REQUEST_RMI_ERROR);
            }
        }catch (RequestException e1){
            throw e1;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            throw new RequestException(CodeEnum.ERROR);
        }
        return "委托已提交";
    }

    @Override
    @Transactional
    public boolean settlementOperation(StockOrderRMI stock) {
        String key_lock="";
        try {
            key_lock="settle_"+stock.getUserAccount();
            if(!getSettleLock(key_lock)){
                throw new SettlementException(CodeEnum.FREQUENT_SETTLEMENT,stock);
            }
            if(!checkSettle(stock.getOid())){
                throw new SettlementException(CodeEnum.ORDER_SETTLE_REPEAT,stock);
            }
            StockOperation op = mockMapper.selectCostByAccount(stock);
            if (op == null) {
                stock.setDatetime(TimeUtil.getDateTime());
                mockMapper.saveSettlementCost(stock);
                mockMapper.updateTradingStatusFinish(stock.getOid(), Const.TRADING_STATUS_FINIST);
                logger.info("首次结算。账户:{},流水单号:{},操作类型:{},总持仓:{},可用持仓:{},成本:{}",
                        stock.getUserAccount(), stock.getOid(), stock.getOperation(), stock.getAmount(), "0", stock.getPrice());
                return true;
            }
            //买结算的时候，只需要计算成本和持仓
            String calCost = MathUtil.calBuyCost(stock.getTradePrice(), stock.getAmount(), op.getCost(), op.getPosition()).toString();
            String totalPosition = MathUtil.add(op.getPosition(), stock.getAmount()).toString();
            op.setPosition(totalPosition);
            op.setSettlement_time(TimeUtil.getDateTime());
            op.setCost(calCost);
            int num=mockMapper.updateSettleCost(op);
            if(num<=0){
                throw new SettlementException(CodeEnum.SETTLEMENT_ERROR);
            }
            int res=mockMapper.updateTradingStatusFinish(stock.getOid(), Const.TRADING_STATUS_FINIST);

            logger.info("buy结算结果,账户:{},流水单号:{},操作类型:{},价格:{},数量:{},新总持仓:{},新可用持仓:{},新成本:{}",
                    stock.getUserAccount(), stock.getOid(), stock.getOperation(), stock.getPrice(), stock.getAmount(), op.getPosition(), op.getPosition_available(), op.getCost());
            return res>0;
        }catch (SettlementException e1){
            throw e1;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            throw new SettlementException(CodeEnum.ERROR);
        }finally {
            if(!StringUtil.isEmpty(key_lock)){
                RedisUtil.releaseDistributedLock(key_lock);
            }
        }
    }
}
