package com.mzkj.news.utils;

import java.math.BigDecimal;

public class MathUtil {

    public static BigDecimal add(String arg0,String arg1){
        if(StringUtil.isEmpty(arg0)) arg0="0";
        if(StringUtil.isEmpty(arg1)) arg1="0";
        return new BigDecimal(arg0).add(new BigDecimal(arg1));
    }
    public static String add2Str(String arg0,String arg1){
        if(StringUtil.isEmpty(arg0)) arg0="0";
        if(StringUtil.isEmpty(arg1)) arg1="0";
        return new BigDecimal(arg0).add(new BigDecimal(arg1)).toString();
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

    public static BigDecimal mul(String arg0,String arg1){
        if(StringUtil.isEmpty(arg0,arg1)) new BigDecimal(0);
        return new BigDecimal(arg0).multiply(new BigDecimal(arg1));
    }


    public static BigDecimal costCalculationByPriceNum(String arg0,String num0,String arg1,String num1){
        if(StringUtil.isEmpty(arg0)) arg0="0";
        if(StringUtil.isEmpty(num0)) num0="0";
        if(StringUtil.isEmpty(arg1)) arg1="0";
        if(StringUtil.isEmpty(num1)) num1="0";
        BigDecimal p0=new BigDecimal(arg0).multiply(new BigDecimal(num0));
        BigDecimal p1=new BigDecimal(arg1).multiply(new BigDecimal(num1));
        BigDecimal total=add(num0,num1);
        return null;
    }

    public static String div(String arg0, String arg1) {
        if(StringUtil.isEmpty(arg0)) return "0";
        if(StringUtil.isEmpty(arg1)) return "0";
        return new BigDecimal(arg0).divide(new BigDecimal(arg1),4,BigDecimal.ROUND_HALF_UP).toString();
    }
    public static String divFloor(String arg0, String arg1) {
        if(StringUtil.isEmpty(arg0)) return "0";
        if(StringUtil.isEmpty(arg1)) return "0";
        return new BigDecimal(arg0).divide(new BigDecimal(arg1),0,BigDecimal.ROUND_FLOOR).toString();
    }


    public static void main(String[] args) {
        System.out.println(divFloor("4","3"));
    }


}
