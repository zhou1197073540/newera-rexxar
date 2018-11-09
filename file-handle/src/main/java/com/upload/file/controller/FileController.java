package com.upload.file.controller;

import com.upload.file.bean.Upload;
import com.upload.file.service.FileService;
import com.upload.file.utils.EnvUtil;
import com.upload.file.utils.FileUtil;
import com.upload.file.utils.ScpClient;
import com.upload.file.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@Controller
public class FileController {
    @Autowired
    FileService fileService;

    @Value("${web.upload-win-path}")
    String path_win;

    @Value("${web.upload-test-path}")
    String path_test_linux;

    @Value("${web.upload-aliyun-path}")
    String path_aliyun_linux;

    @RequestMapping("/upload")
    public String html(){
        return "upload";
    }

    /**
     * @param file 要上传的文件
     * @return
     */
    @RequestMapping(value = "/fileUpload",method = RequestMethod.POST)
    public String upload(@RequestParam("photos") MultipartFile file, Upload upload,Model model){
        String msg="error";
        String path="";
        if(EnvUtil.getSystemEnv().equals("win")){
            path=path_win;
        }else {
            path=path_test_linux;
        }
        String dest_filename=TimeUtil.getCurDates()+"_"+file.getOriginalFilename();
        String rootPath=path+"/"+ TimeUtil.getCurDate()+"/"+dest_filename;
        String destImgUrl="http://image.mzkj88.com/upload/";
        if (FileUtil.upload(file, rootPath)){
            upload.setImg_path(rootPath);
            upload.setImg_url(destImgUrl+dest_filename);
            int num=fileService.saveImageUrl(upload);
            if(num>0) msg= "success";
            ScpClient scp = ScpClient.getInstance();
            scp.putFile(rootPath,dest_filename,path_aliyun_linux, "0666");
            System.out.println("上传的文件名："+dest_filename);
        }
        model.addAttribute("imgUrl",destImgUrl+dest_filename);
        return msg;
    }
}
