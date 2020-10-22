package com.example.mobiletest.ui.test5g;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.example.mobiletest.BR;
import com.example.mobiletest.R;
import com.example.mobiletest.base.BaseActivity;
import com.example.mobiletest.databinding.ActivityPayMsgDetailsBinding;

/**
 * author : liqiang
 * e-mail : qiang_li1@asdc.com.cn
 * date   : 2020/10/20
 * desc   : 支付消息列表
 */
public class PayMsgDetailsActivity extends BaseActivity<ActivityPayMsgDetailsBinding> {
    private String titleEntry;
    private String contentEntry;
    private String timeEntry;
    private String moneyEntry;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_pay_msg_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setVariable(BR.entry, this);
        initData();
    }

    @SuppressLint("SetTextI18n")
    private void initData() {
        titleEntry = getIntent().getStringExtra("titleEntry");
        contentEntry = getIntent().getStringExtra("contentEntry");
        timeEntry = getIntent().getStringExtra("timeEntry");
        moneyEntry = getIntent().getStringExtra("moneyEntry");
        binding.titleEntry.setText(titleEntry);
        binding.content.setText(contentEntry);
        binding.time.setText(timeEntry);
        binding.money.setText("¥" + moneyEntry);
    }

    /**
     * 去支付
     */
    public void goPay() {
        Intent intent = new Intent(this, PayActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putString("title", getResources().getString(R.string.online_pay_test));
        mBundle.putString("titleEntry", titleEntry);
        mBundle.putString("contentEntry", contentEntry);
        mBundle.putString("timeEntry", timeEntry);
        mBundle.putString("moneyEntry", moneyEntry);
        intent.putExtras(mBundle);
        startActivity(intent);
    }


}