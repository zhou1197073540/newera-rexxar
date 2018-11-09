package com.mzkj.usermock.service;

import com.mzkj.usermock.bean_vo.PositionVO;
import com.mzkj.usermock.mapper.MockMapper;
import com.mzkj.usermock.restruct.AbstrackStock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("aliyun")
public class MockServiceTest {

    @Autowired
    MockService mockService;
    private static Logger logger= LoggerFactory.getLogger(MockServiceTest.class);

    @Test
    public void checkSettle(){
        System.out.println(mockService.checkSettle("20180831102150159272830164713"));
    }

    @Test
    public void selectListUserPositionStock() throws Exception {
        List<PositionVO> positionVOS = mockService.selectListUserPositionStock("061755717279");
        positionVOS.forEach(a->System.out.print(a.toString()));
    }

    @Test
    public void stockAccountInfo() throws Exception {
        LocalTime time =  LocalDateTime.now().toLocalTime();
        System.out.println(time.toString());
        System.out.println(time.isAfter(LocalTime.of(13,43)));
    }

}