package com.example.mobiletest.bean;

import java.io.Serializable;

/**
 * author: liqiang
 * e-mail: qiang_li1@asdc.com.cn
 * date  : 2020/7/27
 * desc  :
 */
public class PayBean implements Serializable {
    private String sign;

    public String getSign() {
        return sign;
    }

    public void setSign(String mac) {
        this.sign = mac;
    }
}
