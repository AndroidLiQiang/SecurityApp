package com.example.mobiletest.bean;


import java.io.Serializable;

/***
 * 数据加密实体类
 */
public class EncryptBean implements Serializable {

    public String encryptOrDecrypt;

    public EncryptBean(){}

    public String getEncryptOrDecrypt() {
        return encryptOrDecrypt;
    }

    public void setEncryptOrDecrypt(String message) {
        this.encryptOrDecrypt = message;
    }
}
