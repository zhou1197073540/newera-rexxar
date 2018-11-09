package com.mouzhiapp.stock_market.annotation;

import java.lang.annotation.*;

/**
 * create by zhouzhenyang on 2018/5/31
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SelfSelected {
}
