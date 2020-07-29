package com.example.mobiletest.ui.test5g;

import android.content.Intent;
import android.os.Bundle;

import com.example.mobiletest.BR;
import com.example.mobiletest.R;
import com.example.mobiletest.base.BaseActivity;
import com.example.mobiletest.databinding.ActivityPayResultBinding;

public class PayResultActivity extends BaseActivity<ActivityPayResultBinding> {

    private String type;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pay_result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getStringExtra("type");
        binding.setVariable(BR.payResult, this);
        if ("online".equals(type)) {
            binding.payResult.setText(R.string.online_pay_success);
        } else {
            binding.payResult.setText(R.string.nfc_pay_success);
        }
    }

    public void goMain(){
        startActivity(new Intent(this, Test5GMsgActivity.class));
        finish();
    }
}