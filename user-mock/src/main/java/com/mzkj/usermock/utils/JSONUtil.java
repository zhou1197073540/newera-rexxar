package com.mzkj.usermock.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JSONUtil {
    public static <T> T JSON2Object(String jsonStr,Class<T> obj) {
        T t=null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            //允许使用未带引号的字段名
            objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            //允许使用单引号
            objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            t=objectMapper.readValue(jsonStr,obj);
        }catch (Exception e){
            e.printStackTrace();
        }
        return t;
    }

    public static void main(String[] args) {
    }
}
