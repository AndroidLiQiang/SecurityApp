package com.example.mobiletest.bean;

import java.io.Serializable;

/**
 * author: liqiang
 * e-mail: qiang_li1@asdc.com.cn
 * date  : 2020/8/18
 * desc  :
 */
public class ResultBean implements Serializable {
    private String content;
    private byte[] byteContent;
    private String time;
    private String lock;//1有锁 2无锁

    public ResultBean(String content, String time) {
        this.content = content;
        this.time = time;
    }

    public ResultBean(String content, String time, String lock) {
        this.content = content;
        this.time = time;
        this.lock = lock;
    }

    public ResultBean(byte[] byteContent, String time, String lock) {
        this.byteContent = byteContent;
        this.time = time;
        this.lock = lock;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }

    public byte[] getByteContent() {
        return byteContent;
    }

    public void setByteContent(byte[] byteContent) {
        this.byteContent = byteContent;
    }
}
