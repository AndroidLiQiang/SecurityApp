package com.example.mobiletest.net;

import com.example.mobiletest.bean.EncryptBean;
import com.example.mobiletest.bean.MacBean;
import com.example.mobiletest.bean.PayBean;
import com.example.mobiletest.bean.RandomBean;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface ApiUrl {
    /**
     * TODO Get请求
     */
    @GET
    Observable<RandomBean> getUser(@Url String url);

    /**
     * 获取随机数
     */
    @GET("json")
    Observable<BaseResponse<RandomBean>> getRandom(@QueryMap HashMap<String, String> info);

    /**
     * 验证mac
     */
    @GET("mac")
    Observable<BaseResponse<MacBean>> verifyMac(@QueryMap HashMap<String, Object> info);

    /**
     * 验证签名
     */
    @GET("sign")
    Observable<BaseResponse<PayBean>> verifySign(@QueryMap HashMap<String, Object> info);

    /**
     * 消息加密
     */
    @GET("encrypt")
    Observable<BaseResponse<EncryptBean>> encryptdata(@QueryMap HashMap<String, String> map);


    /***
     *  消息解密
     */

    @GET("decrypt")
    Observable<BaseResponse<EncryptBean>> decryptdata(@QueryMap HashMap<String, String> map);

    @Headers("Accept:application/json")
    @POST("json")
    @FormUrlEncoded
    Observable<BaseResponse<RandomBean>> postRandom(@FieldMap Map<String, String> map);

}
