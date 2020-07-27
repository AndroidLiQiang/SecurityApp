package com.example.mobiletest.net;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.widget.Toast;

import com.example.mobiletest.R;
import com.example.mobiletest.view.LoadingDialog;

import io.reactivex.disposables.Disposable;

/**
 * author : liqiang
 * e-mail : qiang_li1@asdc.com.cn
 * date   : 2020/7/13
 * desc   :
 */
public abstract class MyObserver<T> extends BaseObserver<T> {
    private boolean mShowDialog;
    private LoadingDialog dialog;
    private Context mContext;
    private Disposable d;

    public MyObserver(Context context, Boolean showDialog) {
        mContext = context;
        mShowDialog = showDialog;
    }

    public MyObserver(Context context) {
        this(context, true);
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.d = d;
        if (!isConnected(mContext)) {
            Toast.makeText(mContext, "未连接网络", Toast.LENGTH_SHORT).show();
            if (d.isDisposed()) {
                d.dispose();
            }
        } else {
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
        super.onError(e);
    }

    @Override
    public void onComplete() {
        if (d.isDisposed()) {
            d.dispose();
        }
        hidDialog();
        super.onComplete();
    }

    public void hidDialog() {
        if (dialog != null && mShowDialog)
            dialog.dismiss();
        dialog = null;
    }

    /**
     * 是否有网络连接，不管是wifi还是数据流量
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkCapabilities networkCapabilities = manager.getNetworkCapabilities(manager.getActiveNetwork());
            if (networkCapabilities != null) {
                return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
            }
        }
        return false;
    }

    /**
     * 取消订阅
     */
    public void cancleRequest() {
        if (d != null && d.isDisposed()) {
            d.dispose();
            hidDialog();
        }
    }
}

