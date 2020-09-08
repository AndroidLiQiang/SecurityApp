package com.example.mobiletest.net;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.mobiletest.R;
import com.example.mobiletest.util.NetUtil;
import com.example.mobiletest.view.LoadingDialog;

import java.util.Objects;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * author : liqiang
 * e-mail : qiang_li1@asdc.com.cn
 * date   : 2020/7/13
 * desc   :
 */
public abstract class MyObserver<T> implements Observer<BaseResponse<T>> {
    private boolean mShowDialog;
    private LoadingDialog dialog;
    private Context mContext;
    private Disposable d;

    public MyObserver(Context context, Boolean showDialog) {
        mContext = context;
        mShowDialog = showDialog;
    }

    @Override
    public void onNext(BaseResponse<T> response) {
        //在这边对 基础数据 进行统一处理  举个例子：
        if (!NetUtil.isConnected(mContext)) {
            Toast.makeText(mContext, "未连接网络", Toast.LENGTH_SHORT).show();
            if (d.isDisposed()) {
                d.dispose();
            }
        } else if ("200".equals(response.getCode())) {
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
    public void onSubscribe(Disposable d) {
        this.d = d;
        if (NetUtil.isConnected(mContext)) {
            if (dialog == null && mShowDialog) {
                dialog = new LoadingDialog(mContext, R.style.LoadingDialog);
//                dialog.setMessage("正在加载中");
            }
            if (mShowDialog && !dialog.isShowing()) {
                dialog.show();
            }
        }
    }

    @Override
    public void onError(Throwable e) {
        if (d.isDisposed()) {
            d.dispose();
        }
        hidDialog();
        onFailure(e, RxExceptionUtil.exceptionHandler(e));
        Log.e("TAG", Objects.requireNonNull(e.getMessage()));
    }

    @Override
    public void onComplete() {
        if (d.isDisposed()) {
            d.dispose();
        }
        hidDialog();
    }

    public void hidDialog() {
        if (dialog != null && mShowDialog)
            dialog.dismiss();
        dialog = null;
    }

    public abstract void onSuccess(BaseResponse<T> result);

    public abstract void onFailure(Throwable e, String errorMsg);

    /**
     * 取消订阅
     */
    public void cancelRequest() {
        if (d != null && d.isDisposed()) {
            d.dispose();
            hidDialog();
        }
    }
}

