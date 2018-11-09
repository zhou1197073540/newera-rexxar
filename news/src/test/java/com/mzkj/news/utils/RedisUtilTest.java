package com.mzkj.news.utils;

import com.mzkj.news.bean.MsgPushNewsVO;
import com.mzkj.news.mapper.MsgMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class RedisUtilTest {
    @Autowired
    MsgMapper msgMapper;

    @Test
    public void testBBB(){
        MsgPushNewsVO vo=msgMapper.selectPushNewsCode("600000");
        System.out.println(vo.toString());
    }

    @Test
    public void testRedis(){
        Jedis jedis=RedisUtil.getJedis();
        String str=jedis.get("test");
        System.out.println(str);
    }

}