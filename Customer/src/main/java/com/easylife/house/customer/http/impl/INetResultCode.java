package com.easylife.house.customer.http.impl;

/**
 * Created by Mars on 2017/2/25 09:43.
 * 描述：接口回调错误码以及错误文本提示
 */

public interface INetResultCode {
    /**
     * 成功
     */
    public static String CODE_NET_RESULT_SUCC = "1000";
    public static String CODE_NET_RESULT_SUCC_1 = "200";// 通用接口成功码
    public static String CODE_STRING_RESULT_SUCC = "成功";
    /**
     * 失败
     */
    public static String CODE_NET_RESULT_FAIL = "E100";
    public static String CODE_STRING_RESULT_FAIL = "失败";
    /**
     * 服务器异常
     */
    public static String CODE_NET_RESULT_SERVER_EXCEPTION = "E505";
    public static String CODE_STRING_RESULT_SERVER_EXCEPTION = "服务器异常";
    /**
     * 用户已存在
     */
    public static String CODE_NET_RESULT_USER_EXIST = "E0001";
    public static String CODE_STRING_RESULT_USER_EXIST = "用户已存在";
    /**
     * 用户名不存在
     */
    public static String CODE_NET_RESULT_USER_EXIST_NO = "E0005";
    public static String CODE_STRING_RESULT_USER_EXIST_NO = "用户名不存在";
    /**
     * 两次密码不一致，基本本地已判断。
     */
    public static String CODE_NET_RESULT_PASS_FIT = "E0002";
    public static String CODE_STRING_RESULT_PASS_FIT = "两次密码不一致";
    /**
     * 用户名或密码不正确
     */
    public static String CODE_NET_RESULT_PASS_WRONG = "E0003";
    public static String CODE_STRING_RESULT_PASS_WRONG = "用户名或密码不正确";
    /**
     * 数据为空
     */
    public static String CODE_NET_RESULT_DATA_NULL = "E0004";
    public static String CODE_STRING_RESULT_DATA_NULL = "数据为空";
    /**
     * 参数错误
     */
    public static String CODE_NET_RESULT_PARAMS_ERROR = "E400";
    public static String CODE_STRING_RESULT_PARAMS_ERROR = "参数错误";

    /**
     * 登录失效
     */
    public static String CODE_NET_RESULT_LOGIN_INVALID = "9003";
    public static String CODE_NET_RESULT_DATA_EMPTY = "9001";


    public static String CODE_STRING_RESULT_VERIFYCODE_ERROR_1 = "isv.BUSINESS_LIMIT_CONTROL";
    public static String CODE_STRING_RESULT_VERIFYCODE_ERROR_2 = "isv.MOBILE_NUMBER_ILLEGAL";


}
