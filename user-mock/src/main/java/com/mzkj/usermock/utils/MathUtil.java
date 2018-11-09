package com.mzkj.usermock.utils;

import java.math.BigDecimal;
import java.math.BigInteger;

public class MathUtil {

    public static BigDecimal add(String arg0,String arg1){
        if(StringUtil.isEmpty(arg0)) arg0="0";
        if(StringUtil.isEmpty(arg1)) arg1="0";
        return new BigDecimal(arg0).add(new BigDecimal(arg1));
    }
    public static BigDecimal add(String arg0,BigDecimal arg1){
        if(StringUtil.isEmpty(arg0)) arg0="0";
        if(arg1==null) arg1=new BigDecimal(0);
        return new BigDecimal(arg0).add(arg1);
    }
    /**
     * 成本计算
     */
    public static BigDecimal add(String ... args){
        BigDecimal total=new BigDecimal(0);
        for(String arg:args){
            if(StringUtil.isEmpty(arg)) arg="0";
            total=total.add(new BigDecimal(arg));
        }
        return total;
    }

    public static BigDecimal sub(String arg0,String arg1){
        if(StringUtil.isEmpty(arg0)) arg0="0";
        if(StringUtil.isEmpty(arg1)) arg1="0";
        return new BigDecimal(arg0).subtract(new BigDecimal(arg1));
    }
    public static String sub2str(String arg0,String arg1){
        if(StringUtil.isEmpty(arg0)) arg0="0";
        if(StringUtil.isEmpty(arg1)) arg1="0";
        return new BigDecimal(arg0).subtract(new BigDecimal(arg1)).toString();
    }

    public static BigDecimal mul(String arg0,String arg1){
        if(StringUtil.isEmpty(arg0,arg1)) return new BigDecimal(0);
        return new BigDecimal(arg0).multiply(new BigDecimal(arg1)).setScale(5,BigDecimal.ROUND_HALF_UP);
    }
    public static String mul2str(String arg0,String arg1){
        if(StringUtil.isEmpty(arg0,arg1)) return new BigDecimal(0).toString();
        return new BigDecimal(arg0).multiply(new BigDecimal(arg1)).setScale(5,BigDecimal.ROUND_HALF_UP).toString();
    }

    public static BigDecimal calBuyCost(String arg0,String num0,String arg1,String num1){
        if(StringUtil.isEmpty(arg0)) arg0="0";
        if(StringUtil.isEmpty(num0)) num0="0";
        if(StringUtil.isEmpty(arg1)) arg1="0";
        if(StringUtil.isEmpty(num1)) num1="0";
        BigDecimal p0=mul(arg0,num0);
        BigDecimal p1=mul(arg1,num1);
        BigDecimal totalMoney=p0.add(p1);
        BigDecimal totalNum=add(num0,num1);
        return totalMoney.divide(totalNum,2,BigDecimal.ROUND_HALF_UP);
    }

    /**
     *
     * @param cost 成本
     * @param position  持仓
     * @param price  卖出价格
     * @param amount 卖出数量
     * @return
     */
    public static String calSaleCost(String cost, String position,String price, String amount) {
        if(StringUtil.isEmpty(price)) price="0";
        if(StringUtil.isEmpty(amount)) amount="0";
        if(StringUtil.isEmpty(cost)) cost="0";
        if(StringUtil.isEmpty(position)) position="0";
        long num= sub(position,amount).longValue();
        if(num<=0){
            return null;
        }
        BigDecimal totalCost=mul(position,cost);
        BigDecimal curCost=sub(totalCost.toString(),mul(price,amount).toString());
        return divPoint2(curCost.toString(),String.valueOf(num)).toString();
    }

    public static BigDecimal divPoint2(String arg0, String arg1) {
        if(StringUtil.isEmpty(arg0)) return new BigDecimal(0);
        if(StringUtil.isEmpty(arg1)) return new BigDecimal(0);
        return new BigDecimal(arg0).divide(new BigDecimal(arg1),2,BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal div(String arg0, String arg1) {
        if(StringUtil.isEmpty(arg0)) return new BigDecimal(0);
        if(StringUtil.isEmpty(arg1)) return new BigDecimal(0);
        return new BigDecimal(arg0).divide(new BigDecimal(arg1),5,BigDecimal.ROUND_HALF_UP);
    }

    public static void main(String[] args) {
        System.out.println(calSaleCost("10","100","5","50"));
    }


}
