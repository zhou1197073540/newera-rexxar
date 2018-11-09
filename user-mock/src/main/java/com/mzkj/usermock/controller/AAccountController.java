package com.mzkj.usermock.controller;

import com.alibaba.fastjson.JSONObject;
import com.mzkj.usermock.dto.RespResult;
import com.mzkj.usermock.service.MockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AAccountController {


    @ResponseBody
    @GetMapping("/test")
    public RespResult test() throws Exception {
        return RespResult.genSuccessResult("测试接口");
    }
}
