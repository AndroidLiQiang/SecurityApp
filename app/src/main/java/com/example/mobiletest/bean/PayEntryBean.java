package com.example.mobiletest.bean;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * author: liqiang
 * e-mail: qiang_li1@asdc.com.cn
 * date  : 2020/8/18
 * desc  :
 */
public class PayEntryBean implements Parcelable {
    private String title;
    private String content;
    private String time;
    private Drawable imgUrl;
    private String money;

    public PayEntryBean(String title, String content, String time, Drawable imgUrl, String money) {
        this.title = title;
        this.content = content;
        this.time = time;
        this.imgUrl = imgUrl;
        this.money = money;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String lock) {
        this.title = lock;
    }

    public Drawable getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(Drawable imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.time);
        dest.writeParcelable((Parcelable) this.imgUrl, flags);
        dest.writeString(this.money);
    }

    protected PayEntryBean(Parcel in) {
        this.title = in.readString();
        this.content = in.readString();
        this.time = in.readString();
        this.imgUrl = in.readParcelable(Drawable.class.getClassLoader());
        this.money = in.readString();
    }

    public static final Creator<PayEntryBean> CREATOR = new Creator<PayEntryBean>() {
        @Override
        public PayEntryBean createFromParcel(Parcel source) {
            return new PayEntryBean(source);
        }

        @Override
        public PayEntryBean[] newArray(int size) {
            return new PayEntryBean[size];
        }
    };
}
