package com.example.mobiletest.ui.test5g;

import android.content.Intent;
import android.os.Bundle;

import com.example.mobiletest.BR;
import com.example.mobiletest.R;
import com.example.mobiletest.base.BaseActivity;
import com.example.mobiletest.databinding.ActivityPayBinding;

/**
 * author : liqiang
 * e-mail : qiang_li1@asdc.com.cn
 * date   : 2020/7/13
 * desc   : 支付金额
 */
public class PayActivity extends BaseActivity<ActivityPayBinding> {

    public String titleName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pay;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titleName = getIntent().getStringExtra("title");
        binding.setVariable(BR.pay, this);
    }

    public void goPay() {
        Intent intent = new Intent(this, PayResultActivity.class);
        if (getResources().getString(R.string.online_pay_test).equals(titleName)) {
            intent.putExtra("type", "online");
        } else {
            intent.putExtra("type", "nfc");
        }
        startActivity(intent);
        finish();
    }

}