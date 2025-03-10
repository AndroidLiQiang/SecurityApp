package com.example.mobiletest.ui.test5g;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.example.mobiletest.BR;
import com.example.mobiletest.R;
import com.example.mobiletest.base.BaseActivity;
import com.example.mobiletest.databinding.ActivityPayResultBinding;

public class PayResultActivity extends BaseActivity<ActivityPayResultBinding> {

    private String type;
    private String money;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pay_result;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getStringExtra("type");
        money = getIntent().getStringExtra("money");
        binding.setVariable(BR.payResult, this);
        binding.money.setText("-" + money + "元");
    }

    public void goMain() {
        startActivity(new Intent(this, Test5GMsgActivity.class));
        finish();
    }
}