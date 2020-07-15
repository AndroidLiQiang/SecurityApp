package com.example.mobiletest.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.mobiletest.BR;
import com.example.mobiletest.base.BaseActivity;
import com.example.mobiletest.bean.Demo;
import com.example.mobiletest.databinding.ActivityMainBinding;
import com.example.mobiletest.net.MyObserver;
import com.example.mobiletest.net.RequestUtils;
import com.example.mobiletest.ui.test5g.Test5GMsgActivity;

/**
 * author : liqiang
 * e-mail : qiang_li1@asdc.com.cn
 * date   : 2020/7/9
 * desc   :
 */
public class MainActivity extends BaseActivity<ActivityMainBinding> {
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setVariable(BR.main, this);
    }

    public void goLogin() {
        doThings();
//        startActivity(new Intent(this, LoginResultActivity.class));
    }

    public void goTest5GMsg() {
        startActivity(new Intent(this, Test5GMsgActivity.class));
    }

    private void doThings(){
        RequestUtils.getDemoList2(this, new MyObserver<Demo>(this, true) {
            @Override
            public void onSuccess(Demo result) {
                Log.e(TAG, "onSuccess: ~~~~~~~~~~~" );

            }

            @Override
            public void onFailure(Throwable e, String errorMsg) {
                Log.e(TAG, "onSuccess: ~~~~~~~~~~~"+errorMsg );

            }
        });
    }
}