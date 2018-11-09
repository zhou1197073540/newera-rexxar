package com.upload.file.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class FileUtil {

    public static boolean upload(MultipartFile file,String rootPath){
        File dest=new File(rootPath);
        //判断文件是否存在
        if(!dest.getParentFile().exists()){
            dest.getParentFile().mkdirs();
        }
        try {
            if(file.isEmpty()) return false;
            file.transferTo(dest);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
