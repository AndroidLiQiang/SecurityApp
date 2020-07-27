package com.example.mobiletest.net;

import android.util.Log;

import java.util.Objects;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * author : liqiang
 * e-mail : qiang_li1@asdc.com.cn
 * date   : 2020/7/13
 * desc   : 数据返回统一处理
 */
public abstract class BaseObserver<T> implements Observer<BaseResponse<T>> {
    private static final String TAG = "BaseObserver";

    @Override
    public void onNext(BaseResponse<T> response) {
        //在这边对 基础数据 进行统一处理  举个例子：
        if ("200".equals(response.getCode())) {
            onSuccess(response);
        } else {
            try {
                onFailure(null, response.getErr_msg());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(Throwable e) {//服务器错误信息处理
        onFailure(e, RxExceptionUtil.exceptionHandler(e));
        Log.e("TAG", Objects.requireNonNull(e.getMessage()));
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onSubscribe(Disposable d) {
    }

    public abstract void onSuccess(BaseResponse<T> result);

    public abstract void onFailure(Throwable e, String errorMsg);

}
