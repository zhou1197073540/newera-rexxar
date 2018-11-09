package com.mzkj.xiaoxin.controller;

import com.mzkj.xiaoxin.classifier.RootClassifier;
import com.mzkj.xiaoxin.controller.VO.QandAVO;
import com.mzkj.xiaoxin.dto.RespDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * create by zhouzhenyang on 2018/9/18
 */
@Controller
@RequestMapping("/xiaoxin/")
public class QAController {


    @Autowired
    RootClassifier rootClassifier;

    @RequestMapping(value = "/qa/", method = RequestMethod.POST)
    @ResponseBody
    public RespDto<QandAVO<?>> QuestionAndAnswer(@RequestBody String msg) throws Exception {
        return new RespDto<>(true, "成功", rootClassifier.process(msg));
    }
}
