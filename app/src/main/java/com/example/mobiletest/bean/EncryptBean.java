package com.example.mobiletest.bean;


/***
 * 数据加密实体类
 */
public class EncryptBean {

    public String encryptOrDecrypt;

    public EncryptBean(){}

    public String getEncryptOrDecrypt() {
        return encryptOrDecrypt;
    }

    public void setEncryptOrDecrypt(String message) {
        this.encryptOrDecrypt = message;
    }
}
