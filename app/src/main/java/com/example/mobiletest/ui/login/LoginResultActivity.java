package com.example.mobiletest.ui.login;

import android.os.Bundle;

import com.example.mobiletest.BR;
import com.example.mobiletest.base.BaseActivity;
import com.example.mobiletest.databinding.ActivityLoginResultBinding;

/**
 * author : liqiang
 * e-mail : qiang_li1@asdc.com.cn
 * date   : 2020/7/9
 * desc   :
 */
public class LoginResultActivity extends BaseActivity<ActivityLoginResultBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setVariable(BR.login, this);
    }
}