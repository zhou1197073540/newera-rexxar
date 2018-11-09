package com.mzkj.usermock.mapper;

import com.mzkj.usermock.bean.StockAccountInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class AAccountMapperTest {

    @Autowired
    AAccountMapper aAccountMapper;

    @Test
    public void test(){
        StockAccountInfo a=aAccountMapper.getAccountInfo("aaa");
        System.out.println(a.getA_account()+"="+a.getAssets_available());
    }

}