package com.mzkj.usermock.service;

import com.mzkj.usermock.bean.StockAccountInfo;
import com.mzkj.usermock.bean.StockOperation;
import com.mzkj.usermock.constant.Const;
import com.mzkj.usermock.mapper.AAccountMapper;
import com.mzkj.usermock.utils.MathUtil;
import com.mzkj.usermock.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AAccountService {
    @Autowired
    AAccountMapper aAccountMapper;

    public boolean addSubMoney(String account,String totalcost, String type){
        if(StringUtil.isEmpty(account,totalcost,type)) return false;
        StockAccountInfo stockAcc=aAccountMapper.getAccountInfo(account);
        if(null==stockAcc){
            //没注册，给该用户添加20万
            aAccountMapper.saveStockAccount(new StockAccountInfo(account,"200000"));
            stockAcc=new StockAccountInfo(account,"200000");
        }
        if(Const.ADD_MONEY.equals(type)){
            String avamoney=MathUtil.add(stockAcc.getAssets_available(),totalcost).toString();
            stockAcc.setAssets_available(avamoney);
            int num=updateAvaliableMoney(stockAcc);
            return num>0;
        }else if(Const.SUB_MONEY.equals(type)){
            float avamoney=MathUtil.sub(stockAcc.getAssets_available(),totalcost).floatValue();
            if(avamoney>=0){
                stockAcc.setAssets_available(String.valueOf(avamoney));
                int num=updateAvaliableMoney(stockAcc);
                return num>0;
            }
        }
        return false;
    }

    public boolean buyStock(String account,String totalcost){
        if(StringUtil.isEmpty(account,totalcost)) return false;
        checkAccount(account);
        StockAccountInfo stockAcc=aAccountMapper.getAccountInfo(account);
        String avamoney=MathUtil.add(stockAcc.getAssets_available(),totalcost).toString();
        stockAcc.setAssets_available(avamoney);
        updateAvaliableMoney(stockAcc);
        return true;
    }
    public boolean saleStock(String account,String totalcost){
        if(StringUtil.isEmpty(account,totalcost)) return false;
        checkAccount(account);
        StockAccountInfo stockAcc=aAccountMapper.getAccountInfo(account);
        float avamoney=MathUtil.sub(stockAcc.getAssets_available(),totalcost).floatValue();
        if(avamoney>0){
            stockAcc.setAssets_available(String.valueOf(avamoney));
            updateAvaliableMoney(stockAcc);
            return true;
        }
        return false;
    }

    public void checkAccount(String account) {
        StockAccountInfo stock=aAccountMapper.getAccountInfo(account);
        if(null==stock){
            aAccountMapper.saveStockAccount(new StockAccountInfo(account,"200000"));
        }
    }

    public String getAvaliableMoney(String account){
        checkAccount(account);
        return aAccountMapper.getAccountInfo(account).getAssets_available();
    }
    public int updateAvaliableMoney(StockAccountInfo stock){
        return aAccountMapper.updateAvaliableMoney(stock);
    }

    public void updateTradingStatus(StockOperation stock) {
        aAccountMapper.updateTradingStatus(stock);
    }

}
