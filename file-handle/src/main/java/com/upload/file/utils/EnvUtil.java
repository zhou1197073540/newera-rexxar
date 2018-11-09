package com.upload.file.utils;

public class EnvUtil {
    public static String getSystemEnv(){
        String os = System.getProperty("os.name");
//        System.out.println("当前系统环境:"+os);
        if(os.startsWith("win")||os.startsWith("Win")){
            return "win";
        }else{
            return "linux";
        }
    }

    public static void main(String[] args) {
        System.out.println(getSystemEnv());
    }
}
