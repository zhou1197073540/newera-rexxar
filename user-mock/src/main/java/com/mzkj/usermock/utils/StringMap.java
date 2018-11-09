package com.mzkj.usermock.utils;

import java.util.HashMap;

public class StringMap extends HashMap<String,String>{

    public StringMap() {}

    public StringMap add(String key,String value){
        this.put(key,value);
        return this;
    }

    public static StringMap New(){
        return new StringMap();
    }
}
