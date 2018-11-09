package com.mzkj.news.controller;

import com.alibaba.fastjson.JSONObject;
import com.mzkj.news.bean.MsgPush;
import com.mzkj.news.dto.RespResult;
import com.mzkj.news.service.MsgService;
import com.mzkj.news.utils.JSONUtil;
import com.mzkj.news.utils.StringUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class MsgController {
    @Autowired
    MsgService msgService;
    /**
     * 安卓推送消息
     */
    @ResponseBody
    @PostMapping("/mc/msg-push")
    public RespResult messagePush(@RequestBody JSONObject json){
        MsgPush msg= JSONUtil.JSON2Object(json.toJSONString(),MsgPush.class);
        String user_uid=msgService.getUidByToken(msg.getAccount());
        if(StringUtil.isEmpty(user_uid)) return RespResult.genErrorResult("token失效或已过期！");
        msg.setAccount(user_uid);
        int num=msgService.saveOrUpdatePushMsg(msg);
        if(num<=0) return RespResult.genErrorResult("插入失败");
        return RespResult.genSuccessResult("更新成功");
    }

}
