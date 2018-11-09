package com.mzkj.usermock.enums;

public enum CodeEnum {
    SUCCESS(10000,"成功"),

    RESPONSE_ORDER_SETTLEMENT(11000,"系统正在清算中，请稍后重试!"),


    ERROR(9900,"未知错误"),
    REQUEST_TYPE_ERROR(9901,"post,get 请求类型错误"),//(多指post,get用法不对)
    REQUEST_CONTENT_TYPE_ERROR(9902,"参数内容编码不正确"),//一般都是指定参数的contentType类型指定错误
    REQUEST_PARAMETER_ERROR(9903,"可能是部分请求参数为空"),
    TRADING_PARAMS_EMPTY_ERROR(9904,"可能部分参数为空"),
    REQUEST_RMI_ERROR(9905,"结算中心挂了，暂时不能股票交易"),
    REQUEST_RMI_RES_ERROR(9906,"结算中心挂了，暂时不能股票交易"),
    WALLET_NOT_ENOUGHT_ERROR(9907,"可用现金不足"),
    ACCOUNT_EMPTY(9908,"账号不存在"),
    SETTLEMENT_ERROR(9909,"股票结算异常"),
    POSITION_ADD_ERROR(9910,"总持仓添加异常"),
    POSITION_SUB_ERROR(9911,"总持仓不足"),
    AVI_POSITION_SUB_ERROR(9912,"可用持仓不足"),
    AVI_POSITION_ADD_ERROR(9913,"可用持仓添加异常"),
    AVI_POSITION_ERROR(9914,"可用持仓异常"),
    MONEY_SETTLEMENT_ERROR(9915,"现金结算异常"),
    ORDER_NUM_NOT_EXIST(9916,"订单不存在"),
    ACCOUNT_FAULT(9917,"非法账户"),
    ORDER_SETTLE_REPEAT(9918,"订单重复结算"),
    FREQUENT_OPERATION(9919,"操作过于频繁"),
    FREQUENT_SETTLEMENT(9920,"结算相同账户过于频繁"),
    ;
    private Integer status;
    private String msg;

    CodeEnum(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public static CodeEnum msgOf(int status){
        for(CodeEnum sta:values()){
            if(sta.getStatus()==status){
                return sta;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        CodeEnum s=CodeEnum.SUCCESS;
        System.out.println(s.getStatus());
        System.out.println(s.getMsg());
    }

}
