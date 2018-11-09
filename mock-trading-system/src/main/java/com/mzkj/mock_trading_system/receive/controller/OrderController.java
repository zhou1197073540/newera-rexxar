package com.mzkj.mock_trading_system.receive.controller;

import com.mzkj.mock_trading_system.clearing.entity.Clerk;
import com.mzkj.mock_trading_system.common.dto.RespDto;
import com.mzkj.mock_trading_system.receive.service.OrderOperationHandler;
import com.mzkj.mock_trading_system.receive.service.OrderOperationHandlerFactory;
import com.mzkj.mock_trading_system.receive.service.OrderService;
import com.mzkj.mock_trading_system.receive.valobj.OrderContext;
import com.mzkj.mock_trading_system.trade.entity.Trader;
import com.mzkj.mock_trading_system.trade.entity.UserOrder;
import com.mzkj.mock_trading_system.trade.service.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

import static com.mzkj.mock_trading_system.common.consts.Message.UN_SUPPORTED_OPERATION;

/**
 * create by zhouzhenyang on 2018/7/29
 */
@Controller
public class OrderController {

    @Autowired
    UtilService utilService;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderOperationHandlerFactory factory;

    @Autowired
    Trader trader;
    @Autowired
    Clerk clerk;

//    @GetMapping("/trade")
//    @ResponseBody
//    public void hehe() throws Exception {
//        trader.doTrade();
//    }
//
//
//    @GetMapping("/clearing")
//    @ResponseBody
//    public void clear() throws Exception {
//        clerk.clearing();
//    }

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    @ResponseBody
    public RespDto resolveOrder(@RequestBody UserOrder order) throws Exception {
        System.out.println(order);
//        if (validateDate()) {
        OrderContext orderContext = buildContext(order);
        if (orderContext.getOperation() == null) {
            return new RespDto<>(UN_SUPPORTED_OPERATION.statusCode, UN_SUPPORTED_OPERATION.message);
        }
        OrderOperationHandler handler = factory.getHandler(orderContext.getOperation());
        if (handler == null) {
            return new RespDto<>(UN_SUPPORTED_OPERATION.statusCode, UN_SUPPORTED_OPERATION.message);
        }
        handler.process(orderContext);

        return new RespDto<>(orderContext.getStatusCode(), orderContext.getMsg());
//        }
//        return new RespDto<>(UN_TRADING_TIME.statusCode, UN_TRADING_TIME.message);
    }

    private OrderContext buildContext(UserOrder order) {
        return new OrderContext(order);
    }

    private boolean validateDate() {
        return utilService.isAvailable(LocalDateTime.now());
    }

}
