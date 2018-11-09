package com.mzkj.news.utils;

import java.util.*;
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
                    list.add(StringUtil.formatStockCode(c));
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

    public static String list2SingleMarkStr(String[] strs) {
        if(isEmpty(strs)) return null;
        List<String> strs_=Arrays.asList(strs);
        return  "'"+strs_.stream().filter(x->!isEmpty(x)).reduce((x,y)->x+"','"+y).get()+"'";
    }
    public static String list2SingleMarkStr(List<String> strs) {
        if(strs==null||strs.isEmpty()) return null;
        return  "'"+strs.stream().filter(x->!isEmpty(x)).reduce((x,y)->x+"','"+y).get()+"'";
    }

    public static void main(String[] args) {
        String[] strs=new String[]{"1","2","3","1 3"};
        List<String> s=new ArrayList<>();
        s.add("11");
        s.add("22");
        s.add("33");
        s.add("");
        System.out.println(list2SingleMarkStr(s));
    }


}
