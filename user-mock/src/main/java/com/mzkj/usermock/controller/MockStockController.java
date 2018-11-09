package com.mzkj.usermock.controller;

import com.alibaba.fastjson.JSONObject;
import com.mzkj.usermock.bean.StockOperation;
import com.mzkj.usermock.bean_rmi.StockOrderRMI;
import com.mzkj.usermock.bean_vo.AccountInfoVO;
import com.mzkj.usermock.bean_vo.PositionVO;
import com.mzkj.usermock.bean_vo.UserAssets;
import com.mzkj.usermock.constant.Const;
import com.mzkj.usermock.dto.RespResult;
import com.mzkj.usermock.dto.RespResults;
import com.mzkj.usermock.enums.CodeEnum;
import com.mzkj.usermock.exception.runtime.CheckEmptyException;
import com.mzkj.usermock.exception.runtime.RequestException;
import com.mzkj.usermock.restruct.AbstrackStock;
import com.mzkj.usermock.service.AAccountService;
import com.mzkj.usermock.service.MockService;
import com.mzkj.usermock.utils.*;
import com.sun.javafx.tk.Toolkit;
import com.sun.jmx.snmp.tasks.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class MockStockController {
    @Autowired
    AAccountService aAccountService;
    @Autowired
    MockService mockService;

    /**
     * 模拟炒股账户接口和股票盈亏接口
     */
    @ResponseBody
    @GetMapping("/um/stock-account-info")
    public RespResult stockAccountInfo(@RequestParam("account") String account) {
        Map vo = mockService.stockAccountInfo(account);
        if(vo==null) return RespResult.genErrorResult("账号不存在");
        return RespResult.genSuccessResult(vo);
    }

    /**
     * 模拟炒股的买入，卖出 接口
     */
    @ResponseBody
    @PostMapping("/um/stock-operation")
    public RespResult stockOperation(@RequestBody JSONObject obj) throws Exception {
        LocalTime time = LocalDateTime.now().toLocalTime();
        if (!StringUtil.checkEmpty(obj))
            return RespResult.genErrorResult("参数不合法或者参数不能为空");
        if (time.isAfter(LocalTime.of(15, 0)) && time.isBefore(LocalTime.of(18, 0)))
            return RespResult.genErrorResult("系统正在清算中，请稍后重试!");
        if (StringUtil.hasIndex(obj.getString("code_type")))
            return RespResult.genErrorResult("不支持指数类型交易");
        System.out.println("============老版买卖单中心=========");
        StockOperation stock = JSONUtil.JSON2Object(obj.toString(), StockOperation.class);
        String key_lock="operation_"+stock.getA_account();
        if (RedisUtil.tryGetDistributedLock(key_lock)){
            try {
                AbstrackStock ab = StockFactory.getStock(stock.getOperation());
                String res = ab.dealOperateion(stock);
                return RespResult.genSuccessResult(res);
            } finally {
                RedisUtil.releaseDistributedLock(key_lock);
            }
        }
        return RespResult.genErrorResult("操作过于频繁，请稍后再试");
    }


    /**
     * 撤单接口
     */
    @ResponseBody
    @GetMapping("/um/stock-operation-cancel")
    public RespResult stockCancel(@RequestParam("order_num") String order_num) throws Exception {
        StockOperation opera = new StockOperation();
        opera.setOrder_num(order_num);
        opera.setOperation(Const.CANCLE);
        if (mockService.stockRMIs(opera)) return RespResult.genSuccessResult("撤单委托已提交");
        return RespResult.genErrorResult("可能是结算中心挂了，赶紧联系后台人员吧");
    }

    /**
     * 显示所有未成交的下单，撤单股票
     */
    @ResponseBody
    @GetMapping("/um/stock-operation-records")
    public RespResult stockRecordsRecords(@RequestParam("account") String a_account) throws Exception {
        List<StockOrderRMI> res = mockService.selectResultByAccount(a_account);
        return RespResult.genSuccessResult(res);
    }


    /**
     * 结算接口
     */
    @ResponseBody
    @PostMapping("/um/stock-operation-settlement")
    public RespResult stockSettlements(@RequestBody List<StockOrderRMI> stocks) throws Exception {
        System.out.println("↓↓↓↓↓↓↓----结算中心--↓↓↓↓↓↓↓");
        List<String> errors=new ArrayList<>();
        boolean isAllSuccess=true;
        for (StockOrderRMI order : stocks) {
            AbstrackStock ab = StockFactory.getStock(order.getOperation());
            if(!ab.settlementOperation(order)) {
                isAllSuccess=false;
                errors.add(order.getOid());
            }
        }
        if(isAllSuccess) return RespResult.genSuccessResult(CodeEnum.SUCCESS.getMsg());
        else return RespResult.genErrorResult(CodeEnum.SETTLEMENT_ERROR.getMsg());
    }



//========================对外开放接口============================================

    /**
     * 显示所有未成交的下单，撤单股票
     */
    @ResponseBody
    @GetMapping("/um/stock-operation-results/{account}")
    public RespResults stockRecordsResults(@PathVariable("account") String a_accounts) throws Exception {
        String a_account=null;
        try {
            a_account= DESUtil.decryption(a_accounts,DESUtil.SECRET_KEY);
            if(StringUtil.isEmpty(a_account)){
                throw new RequestException(CodeEnum.ACCOUNT_FAULT);
            }
        }catch (Exception e){
            System.out.println("用户股票账号非法id："+a_accounts);
            throw new RequestException(CodeEnum.ACCOUNT_FAULT);
        }
        List<StockOrderRMI> res = mockService.selectResultByAccount(a_account);
        return RespResults.genSuccessResult(res);
    }

    /**
     * 下单撤单
     * @param stock
     * @param bindRes
     * @return
     * @throws Exception
     */
    @ResponseBody
    @PostMapping("/um/stock-operation-order")
    public RespResults stockOperations(@Valid StockOperation stock,BindingResult bindRes) throws RequestException,CheckEmptyException {
        if(bindRes.hasErrors()){
            return RespResults.genErrorResult(CodeEnum.TRADING_PARAMS_EMPTY_ERROR,bindRes.getFieldError().getDefaultMessage());
        }
        System.out.println("============对外买卖单中心=========");
        try {
            String account= DESUtil.decryption(stock.getA_account(),DESUtil.SECRET_KEY);
            if(StringUtil.isEmpty(account)){
                throw new RequestException(CodeEnum.ACCOUNT_FAULT);
            }
            stock.setA_account(account);
        }catch (Exception e){
            System.out.println("用户股票账号非法id："+stock.getA_account());
            throw new RequestException(CodeEnum.ACCOUNT_FAULT);
        }
        String key_lock="operation_"+stock.getA_account();
        if (RedisUtil.tryGetDistributedLock(key_lock)) {
            try {
                AbstrackStock ab = StockFactory.getStock(stock.getOperation());
                String res = ab.dealOperateion(stock);
                return RespResults.genSuccessResult(res);
            } finally {
                RedisUtil.releaseDistributedLock(key_lock);
            }
        }
        return RespResults.genErrorResult(CodeEnum.FREQUENT_OPERATION);
    }

    /**
     * 查询当前持仓组合
     */
    @ResponseBody
    @GetMapping("/um/user-stock-position/{account}")
    public RespResults userStockPosition(@PathVariable("account") String accounts) throws Exception {
        String account=null;
        try {
            account= DESUtil.decryption(accounts,DESUtil.SECRET_KEY);
             if(StringUtil.isEmpty(accounts)){
                 throw new RequestException(CodeEnum.ACCOUNT_FAULT);
             }
        }catch (Exception e){
            System.out.println("用户股票账号非法id："+accounts);
            throw new RequestException(CodeEnum.ACCOUNT_FAULT);
        }
        List<PositionVO> res = mockService.selectListUserPositionStock(account);
        return RespResults.genSuccessResult(res);
    }

    /**
     * 查询当前用户资金情况
     */
    @ResponseBody
    @GetMapping("/um/user-assets-info/{account}")
    public RespResults userAssets(@PathVariable("account") String accounts) throws RequestException {
        String account=null;
        try {
            account= DESUtil.decryption(accounts,DESUtil.SECRET_KEY);
            if(StringUtil.isEmpty(accounts)){
                throw new RequestException(CodeEnum.ACCOUNT_FAULT);
            }
        }catch (Exception e){
            System.out.println("用户股票账号非法id："+accounts);
            throw new RequestException(CodeEnum.ACCOUNT_FAULT);
        }
        Map vo = mockService.stockAccountInfo(account);
        if(vo==null) throw new RequestException(CodeEnum.ACCOUNT_EMPTY);
        AccountInfoVO aInfo = (AccountInfoVO) vo.get("accountInfo");
        return RespResults.genSuccessResult(
                new UserAssets(aInfo.getTotal_assets(),aInfo.getAssets_available(),(String) vo.get("lockingAssets")));
    }
}
