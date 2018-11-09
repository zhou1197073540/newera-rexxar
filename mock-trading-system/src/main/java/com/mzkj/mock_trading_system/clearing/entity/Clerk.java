package com.mzkj.mock_trading_system.clearing.entity;

import com.mzkj.mock_trading_system.clearing.POJO.MailValue;
import com.mzkj.mock_trading_system.common.consts.Consts;
import com.mzkj.mock_trading_system.common.util.MailUtil;
import com.mzkj.mock_trading_system.remote.ImprovedRemoteUserService;
import com.mzkj.mock_trading_system.trade.entity.UserOrder;
import com.mzkj.mock_trading_system.trade.repo.cache.OrderQueue;
import com.mzkj.mock_trading_system.trade.repo.mapper.ow.StockOrderMapper;
import com.mzkj.mock_trading_system.trade.service.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * create by zhouzhenyang on 2018/8/9
 */
@Service
public class Clerk {

    @Autowired
    UtilService utilService;
    @Autowired
    OrderQueue orderQueue;
    @Autowired
    ImprovedRemoteUserService remoteUserService;
    @Autowired
    StockOrderMapper stockOrderMapper;


    /**
     * 清算未成交订单->退款,退股
     * 清算已成交异常订单->重新结算
     *
     * @throws Exception
     */

    @Scheduled(cron = "0 20 15 * * *")
    public void clearing() {
        if (utilService.isTradingDate(LocalDateTime.now())) {
            MailValue mailValue = new MailValue();
            mailValue.setStartDateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            clearWaitingOrders(mailValue);
            clearErrorOrders(mailValue);
            mailValue.setEndDateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            MailUtil.sendMail(mailValue);
            if (mailValue.getErrorCount().equals(mailValue.getClearErrorCount())) {
                cleanUserErrorOrder();
                cleanUserOrder();
            }
        }
    }

    /**
     * 状态为wait的order
     */
    @Transactional
    public void cleanUserOrder() {
        final List<UserOrder> waitOrders = getWaitOrders();
        stockOrderMapper.doUpdateTradeOrderByOids(waitOrders, Consts.OPERATION_CANCEL, null);
//        final LocalDateTime v1500 = LocalDateTime.of(LocalDate.now(), LocalTime.of(15, 0));
//        stockOrderMapper.doDeleteLatestUserOrdersByTimeAndStatus(v1500, Consts.WAIT);
    }

    /**
     * 回调异常order
     */
    @Transactional
    void cleanUserErrorOrder() {
        stockOrderMapper.doDeleteErrorOrders();
        orderQueue.getAll();
    }


    /**
     * 清算,但通知mock-user模块出错的订单
     */
    private void clearErrorOrders(MailValue mailValue) {
        final List<UserOrder> errorOrders = getErrorOrders();
        int totalErrorSize = (errorOrders == null) ? 0 : errorOrders.size();
        mailValue.setErrorCount(totalErrorSize);
        if (totalErrorSize > 0) {
            int finalSize = 0;
            finalSize = remoteUserService.sendOrderResultSegmented(errorOrders);
            mailValue.setClearErrorCount(finalSize);
        }
    }

    private List<UserOrder> getWaitOrders() {
        return stockOrderMapper.doGetOrdersByStatus(Consts.WAIT);
    }

    /**
     * 在orders中已经标注位finish但是
     *
     * @return
     */
    private List<UserOrder> getErrorOrders() {
        return stockOrderMapper.doGetErrorOrders(LocalDateTime.of(LocalDate.now(), LocalTime.of(15, 20)));
    }

    @Transactional
    public void clearWaitingOrders(MailValue mailValue) {
        final List<UserOrder> waitOrders = getWaitOrders();
        final int waitCount = (waitOrders == null) ? 0 : waitOrders.size();
        mailValue.setWaitCount(waitCount);
        if (waitCount > 0) {
            waitOrders.forEach(userOrder -> userOrder.setOperation(Consts.OPERATION_CANCEL));
            int finalSize = 0;
            finalSize = remoteUserService.sendOrderResultSegmented(waitOrders);
            mailValue.setClearWaitCount(finalSize);
        }
    }
}
