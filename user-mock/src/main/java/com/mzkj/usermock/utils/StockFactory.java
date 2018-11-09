package com.mzkj.usermock.utils;

import com.mzkj.usermock.constant.Const;
import com.mzkj.usermock.enums.CodeEnum;
import com.mzkj.usermock.exception.runtime.CheckEmptyException;
import com.mzkj.usermock.restruct.AbstrackStock;
import com.mzkj.usermock.restruct.impl.BuyStock;
import com.mzkj.usermock.restruct.impl.CancelStock;
import com.mzkj.usermock.restruct.impl.SellStock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class StockFactory  {

    public static AbstrackStock getStock(String operation) throws CheckEmptyException {
        if(StringUtil.isEmpty(operation)) throw new CheckEmptyException(CodeEnum.REQUEST_PARAMETER_ERROR);
        switch (operation){
            case Const.BUY:
                return (BuyStock) ApplicationContextFactory.getContext().getBean("buyStock");
            case Const.SALE:
                return (SellStock) ApplicationContextFactory.getContext().getBean("sellStock");
            case Const.CANCLE:
                return (CancelStock) ApplicationContextFactory.getContext().getBean("cancelStock");
            default:
                throw new CheckEmptyException(CodeEnum.ERROR);
        }
    }
}
