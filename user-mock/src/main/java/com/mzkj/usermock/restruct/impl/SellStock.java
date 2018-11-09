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
import com.mzkj.usermock.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("sellStock")
public class SellStock extends AbstrackStock {
    private static Logger logger= LoggerFactory.getLogger(SellStock.class);
    @Autowired
    MockService mockService;
    @Autowired
    AAccountService aAccountService;
    @Autowired
    MockMapper mockMapper;

    @Override
    public boolean checkMoney(StockOperation stock) {
        String calMoney = MathUtil.mul2str(stock.getNum(), stock.getPrice());
        return mockService.addSubMoney(stock.getA_account(), calMoney, Const.ADD_MONEY);
    }

    @Override
    @Transactional
    public String dealOperateion(StockOperation stock) throws RequestException {
        try {
            if (!mockService.addSubAvaiPosition(stock, Const.SUB_AVAI_POSITION)) {
                throw new RequestException(CodeEnum.AVI_POSITION_SUB_ERROR);
            }
            mockService.saveStockOperation(stock);
            if (!mockService.stockRMIs(stock)) {
                throw new RequestException(CodeEnum.REQUEST_RMI_ERROR);
            }
            return "委托已提交";
        }catch (RequestException e1){
            throw e1;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            throw new RequestException(CodeEnum.ERROR);
        }
        //卖股票出异常了需要把持仓还给人家
//                mockService.addSubAvaiPosition(stock,Const.ADD_AVAI_POSITION);
//                stock.setTrading_status(Const.TRADING_STATUS_EXCEPTION);
//                aAccountService.updateTradingStatus(stock);
//                return "交易中心挂了，请及时检查";
//            }
//        }else return "可用持仓不足";
//        return "委托已提交";
    }

    @Override
    @Transactional
    public boolean settlementOperation(StockOrderRMI stock) throws SettlementException {
        String key_lock="";
        try {
            key_lock="settle_"+stock.getUserAccount();
            if(!getSettleLock(key_lock)){
                throw new SettlementException(CodeEnum.FREQUENT_SETTLEMENT,stock);
            }
            if(!checkSettle(stock.getOid())){
                throw new SettlementException(CodeEnum.ORDER_SETTLE_REPEAT);
            }
            System.out.println("卖股票结算:" + stock);
            StockOperation op = mockMapper.selectCostByAccount(stock);
            if (op == null) {
                stock.setDatetime(TimeUtil.getDateTime());
                mockMapper.saveSettlementCost(stock);
                mockMapper.updateTradingStatusFinish(stock.getOid(), Const.TRADING_STATUS_FINIST);
                logger.info("首次结算。账户:{},流水单号:{},操作类型:{},总持仓:{},可用持仓:{},成本:{}",
                        stock.getUserAccount(), stock.getOid(), stock.getOperation(), stock.getAmount(), "0", stock.getPrice());
                return true;
            }
            //卖结算的时候，只需要计算成本和减总持仓和加钱操作
            op.setNum(stock.getAmount());
            op.setPrice(stock.getTradePrice());
            op.setOperation(stock.getOperation());
            if (!checkMoney(op)) {
                throw new SettlementException(CodeEnum.MONEY_SETTLEMENT_ERROR);
            }
            String calCost = MathUtil.calSaleCost(op.getCost(), op.getPosition(), stock.getTradePrice(), stock.getAmount());
            if (calCost == null) {
                //全卖光了
                op.setPosition("0");
                op.setPosition_available("0");
                op.setCost("0");
            } else {
                String totalPosition = MathUtil.sub(op.getPosition(), stock.getAmount()).toString();
                op.setPosition(totalPosition);
                op.setCost(calCost);
            }
            int num = mockMapper.updateSettleCost(op);
            if (num <= 0) {
                throw new SettlementException(CodeEnum.POSITION_SUB_ERROR);
            }
            int res=mockMapper.updateTradingStatusFinish(stock.getOid(), Const.TRADING_STATUS_FINIST);
            logger.info("sale结算结果,账户:{},流水单号:{},操作类型:{},价格:{},数量:{},当前总持仓:{},当前可用持仓:{},新成本:{}",
                    stock.getUserAccount(), stock.getOid(), stock.getOperation(), stock.getPrice(), stock.getAmount(), op.getPosition(), op.getPosition_available(), op.getCost());
            return res>0;
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
