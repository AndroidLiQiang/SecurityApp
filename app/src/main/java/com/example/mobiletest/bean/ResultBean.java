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
    private String bitmapStr;
    private byte[] byteContent;
    private String time;
    private String photoNam;
    private String lock;//1有锁 2无锁

    public ResultBean(String content, String time) {
        this.content = content;
        this.time = time;
    }

    public ResultBean(String content, String time, String lock, String photoNam) {
        this.content = content;
        this.time = time;
        this.lock = lock;
        this.photoNam = photoNam;
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

    public String getBitmapStr() {
        return bitmapStr;
    }

    public void setBitmapStr(String bitmapStr) {
        this.bitmapStr = bitmapStr;
    }

    public String getPhotoNam() {
        return photoNam;
    }

    public void setPhotoNam(String photoNam) {
        this.photoNam = photoNam;
    }
}
