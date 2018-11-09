package com.mouzhiapp.stock_market.service;

import com.mouzhiapp.stock_market.util.TokenUtil;
import jdk.nashorn.internal.parser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * create by zhouzhenyang on 2018/8/17
 */
@Service
public class UserUtilService {

    @Autowired
    TokenUtil tokenUtil;

    public String getUidByToken(String token) {
        return tokenUtil.getProperty(token, "uid");
    }
}
