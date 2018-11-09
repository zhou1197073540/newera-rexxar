package com.upload.file.mapper;

import com.upload.file.bean.Upload;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("aliyun")
public class FileMapperTest {
    @Autowired
    FileMapper fileMapper;

    @Value("${web.upload-path}")
    String path;
    @Test
    public void testDBInsert(){
        Upload upload=new Upload();
        upload.setImg_url("ssss");
        int num=fileMapper.saveImageUrl(upload);
        System.out.println(num);
    }

    @Test
    public void testDB(){
        int num=fileMapper.selectCountUserInfo();
        System.out.println(num);
        System.out.println(path);
    }
}