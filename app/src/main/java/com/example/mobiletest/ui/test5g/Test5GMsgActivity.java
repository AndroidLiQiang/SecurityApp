package com.example.mobiletest.ui.test5g;

import android.content.Intent;
import android.os.Bundle;

import com.example.mobiletest.BR;
import com.example.mobiletest.R;
import com.example.mobiletest.base.BaseActivity;
import com.example.mobiletest.databinding.ActivityTest5gMsgBinding;

/**
 * author : liqiang
 * e-mail : qiang_li1@asdc.com.cn
 * date   : 2020/7/9
 * desc   : 5G消息测试入口
 */
public class Test5GMsgActivity extends BaseActivity<ActivityTest5gMsgBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test_5g_msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setVariable(BR.test, this);
    }

    /**
    * 5g消息加解密
    * */
    public void goEncryptOrDecrypt() {
        startActivity(new Intent(this, EncryptOrDecryptActivity.class));
    }

    /**
     * 在线支付
     */
    public void goOnlinePay() {
        Intent intent = new Intent(this, PayActivity.class);
        intent.putExtra("title", getResources().getString(R.string.online_pay_test));
        startActivity(intent);
    }

    /**
     * NFC支付
     */
    public void goNFCPay() {
        Intent intent = new Intent(this, RemindActivity.class);
        startActivity(intent);
    }
}