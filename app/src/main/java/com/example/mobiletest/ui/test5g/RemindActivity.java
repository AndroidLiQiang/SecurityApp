package com.example.mobiletest.ui.test5g;

import android.content.Intent;
import android.os.Bundle;

import com.example.mobiletest.BR;
import com.example.mobiletest.R;
import com.example.mobiletest.base.BaseActivity;
import com.example.mobiletest.databinding.ActivityRemindBinding;

/**
 * author: liqiang
 * e-mail: qiang_li1@asdc.com.cn
 * date  : 2020/9/11
 * desc  :
 */
public class RemindActivity extends BaseActivity<ActivityRemindBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_remind;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setVariable(BR.remind, this);
    }

    public void goMain() {
        startActivity(new Intent(this, Test5GMsgActivity.class));
        finish();
    }
}