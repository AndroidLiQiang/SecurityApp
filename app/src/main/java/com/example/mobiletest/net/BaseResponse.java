package com.example.mobiletest.net;

/**
 * author : liqiang
 * e-mail : qiang_li1@asdc.com.cn
 * date   : 2020/7/13
 * desc   : 统一响应
 */
public class BaseResponse<T> {
    private String code;
    private String message;
    private String err_msg;
    private T result;

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }


    public String getErr_msg() {
        return err_msg;
    }


    public T getResult() {
        return result;
    }

}
