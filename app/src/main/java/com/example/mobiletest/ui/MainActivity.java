package com.example.mobiletest.ui;

import android.content.Intent;
import android.os.Bundle;

import com.example.mobiletest.BR;
import com.example.mobiletest.base.BaseActivity;
import com.example.mobiletest.databinding.ActivityMainBinding;
import com.example.mobiletest.ui.login.LoginResultActivity;
import com.example.mobiletest.ui.test5g.Test5GMsgActivity;

/**
 * author : liqiang
 * e-mail : qiang_li1@asdc.com.cn
 * date   : 2020/7/9
 * desc   :
 */
public class MainActivity extends BaseActivity<ActivityMainBinding> {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setVariable(BR.main, this);
    }

    public void goLogin() {
        startActivity(new Intent(this, LoginResultActivity.class));
    }

    public void goTest5GMsg() {
        startActivity(new Intent(this, Test5GMsgActivity.class));
    }
}