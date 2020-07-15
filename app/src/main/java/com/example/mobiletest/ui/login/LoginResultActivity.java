package com.example.mobiletest.ui.login;

import android.content.Intent;
import android.os.Bundle;

import com.example.mobiletest.BR;
import com.example.mobiletest.R;
import com.example.mobiletest.base.BaseActivity;
import com.example.mobiletest.databinding.ActivityLoginResultBinding;
import com.example.mobiletest.ui.MainActivity;

/**
 * author : liqiang
 * e-mail : qiang_li1@asdc.com.cn
 * date   : 2020/7/9
 * desc   :
 */
public class LoginResultActivity extends BaseActivity<ActivityLoginResultBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login_result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setVariable(BR.login, this);
    }

    public void goMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}