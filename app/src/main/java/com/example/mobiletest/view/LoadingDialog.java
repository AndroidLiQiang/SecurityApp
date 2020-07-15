package com.example.mobiletest.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleObserver;

import com.airbnb.lottie.LottieAnimationView;
import com.example.mobiletest.R;

/**
 * author: liqiang
 * e-mail: qiang_li1@asdc.com.cn
 * date: 2020/7/14
 * desc:
 */
public class LoadingDialog extends Dialog implements LifecycleObserver {

    private LottieAnimationView animationView;

    public LoadingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        createDialog(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createDialog(this.getContext());
    }

    public void createDialog(Context context) {
        setCanceledOnTouchOutside(false);
        getWindow().setDimAmount(0f);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        animationView = view.findViewById(R.id.lottieView);
        addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                animationView.cancelAnimation();
                animationView.clearAnimation();
            }
        });
    }

    /*//TODO
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy(){
        dismiss();
        if (animationView != null) {
            animationView.clearAnimation();
        }
    }*/
}
