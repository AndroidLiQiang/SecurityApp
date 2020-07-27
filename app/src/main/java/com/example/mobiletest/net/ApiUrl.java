package com.example.mobiletest.net;

import com.example.mobiletest.bean.MacBean;
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

    @GET("json")
    Observable<BaseResponse<RandomBean>> getRandom(@QueryMap HashMap<String, String> info);

    @GET("mac")
    Observable<BaseResponse<MacBean>> verifyMac(@QueryMap HashMap<String, Object> info);

    @Headers("Accept:application/json")
    @POST("json")
    @FormUrlEncoded
    Observable<BaseResponse<RandomBean>> postRandom(@FieldMap Map<String, String> map);

}
