package com.mzkj.news.service;

import com.mzkj.news.bean.AppVersion;
import com.mzkj.news.bean.MessageCenter;
import com.mzkj.news.bean.User;
import com.mzkj.news.mapper.UserMapper;
import com.mzkj.news.utils.HashUtil;
import com.mzkj.news.utils.TimeUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("aliyun")
public class UserServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    UserMapper userMapper;

    @Test
    public void checkVersionss() throws Exception {
        User user=new User();
        user.setPhone_num("132465789");
        user.setPassword("abcd1234");
        user.setUid("46546546");
        user.setType("app");
        user.setUsername("wz_"+"132465789");
        user.setToken("asfdawge");
        userMapper.saveUser(user);
    }
    @Test
    public void checkVersion() throws Exception {
        AppVersion aa=userService.checkVersion("0.8.0");
    }

    @Test
    public void addThumbsUp() throws Exception {
        userService.delThumbsUp("asdfasf","asdfasfsa");
    }

    @Test
    public void selectedStockByUid() throws Exception {
        userService.saveOrUpdateSelfSelectStock("2222222","sh600001");
    }

    @Test
    public void selectedStockByUids() throws Exception {
        userService.selectedStockByUid("11111");
    }

    @Test
    public void test() throws Exception {
        userService.saveOrUpdateUserPhone("123456789");
    }

}