package com.mzkj.usermock.utils;

import com.alibaba.fastjson.JSONObject;
import com.mzkj.usermock.constant.Const;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    public static boolean isEmpty(String str){
        if(str==null||str.trim().isEmpty()) return true;
        return false;
    }
    public static boolean isEmpty(String... strs){
        boolean isEmpty=false;
        for(String str:strs){
            if(isEmpty(str)){
                isEmpty=true;
                break;
            }
        }
        return isEmpty;
    }

    /**
     * 去除字符串首尾的[]
     */
    public static String removeMark(String str){
        if(isEmpty(str)) return str;
        String txt=null;
        if(!"[]".equals(str)){
            if(str.startsWith("[")&&str.endsWith("]")){
                txt=str.substring(1,str.length()-1);
            }
        }
        if(str.contains("[\"")&& str.contains("\"]")){
            txt=str.replace("[\"","").replace("\"]","");
        }
        return txt;
    }

    /**
     * 获取前100个字符串
     */
    public static String gainFirst100Words(String str){
        if(isEmpty(str)||str.length()<100) return str;
        return str.substring(0,100)+"...";
    }

    public static Set<String> formatCondition(String cons) {
        Set<String> list=new HashSet<>();
        if(cons.contains(",")){
            String[] cs=cons.split(",");
            for(String c:cs){
                if(!isEmpty(c)) list.add(c);
            }
        }else{
            if(!isEmpty(cons)) list.add(cons);
        }
        return list;
    }
    public static Set<String> formatCodes(String codes) {
        Pattern p=Pattern.compile("\\d{6}");
        Set<String> list=new HashSet<>();
        if(StringUtil.isEmpty(codes)) return list;
        if(codes.contains(",")){
            String[] cs=codes.split(",");
            for(String c:cs){
                Matcher m=p.matcher(c);
                if(m.find()){
                    list.add(c);
                }
            }
        }
        return list;
    }
    public static String formatStockCode(String code){
        if(isEmpty(code)) return null;
        if(code.startsWith("sz")||code.startsWith("sh")) return code;
        String code_type="unknown";
        if(code.startsWith("0")||code.startsWith("3")){
            code_type="sz"+code;
        }else if(code.startsWith("6")){
            code_type="sh"+code;
        }
        return code_type;
    }
    public static String formatStockCode(String code,String stockname){
        if(isEmpty(code)) return null;
        if(code.startsWith("sz")||code.startsWith("sh")) return code;
        String code_type="unknown";
        if(code.startsWith("0")||code.startsWith("3")){
            if(stockname.equals("上证指数")||code.equals("000300")||stockname.equals("中证500")||
                    stockname.equals("中证800")||stockname.equals("上证50")){
                code_type="sh"+code;
            }else{
                code_type="sz"+code;
            }
        }else if(code.startsWith("6")){
            code_type="sh"+code;
        }
        return code_type;
    }


    public static String removeStockCode(String code){
        if(isEmpty(code)) return null;
        if(code.startsWith("sz")||code.startsWith("sh")){
            return code.substring(2,code.length());
        }
        return null;
    }
    public static boolean hasIndex(String code_type) {
        return code_type.equals("sh000001") || code_type.equals("sz399005") ||
                code_type.equals("sz399006") || code_type.equals("sh000016") ||
                code_type.equals("sz399001") || code_type.equals("sh000906") ||
                code_type.equals("sh000905") || code_type.equals("sh000300");
    }
    public static boolean checkEmpty(JSONObject obj) {
        try {
            String a_account = obj.getString("a_account");
            String code_type=obj.getString("code_type");
            String operation=obj.getString("operation");
            String price=obj.getString("price");
            String num=obj.getString("num");
            if(isEmpty(a_account,code_type,operation,price,num)) return false;
            if(!Const.SALE.equals(operation)&&!Const.BUY.equals(operation)) return false;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        System.out.println(formatStockCode("sh600000",""));
    }



}
