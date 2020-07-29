package com.example.mobiletest.ui.test5g;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.mobiletest.BR;
import com.example.mobiletest.R;
import com.example.mobiletest.base.BaseActivity;
import com.example.mobiletest.bean.PayBean;
import com.example.mobiletest.databinding.ActivityPayBinding;
import com.example.mobiletest.net.BaseResponse;
import com.example.mobiletest.net.MyObserver;
import com.example.mobiletest.net.RequestUtils;
import com.example.teesimmanager.TeeSimManager;

import java.util.HashMap;

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
        if (getTarget()) {
            //线上交易认证，需要用户指纹比对
            TeeSimManager.getInstance().authenticateOnline(this, "test".getBytes(), this::verifySign);
        } else {
            //TODO NFC
            Toast.makeText(PayActivity.this, "nfc支付", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 验证签名
     */
    private void verifySign(byte[] sign) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", "sign");
        map.put("sign", sign);
        RequestUtils.verifySign(this, map, new MyObserver<PayBean>(this, true) {
            @Override
            public void onSuccess(BaseResponse<PayBean> result) {
                Toast.makeText(PayActivity.this, "" + result.getResult().getSign(), Toast.LENGTH_SHORT).show();
                jumpPage();
            }

            @Override
            public void onFailure(Throwable e, String errorMsg) {
            }
        });
    }

    /**
     * 根据文字判断操作
     */
    private boolean getTarget() {
        return getResources().getString(R.string.online_pay_test).equals(titleName);
    }

    /**
     * 跳转页面
     */
    private void jumpPage() {
        Intent intent = new Intent(PayActivity.this, PayResultActivity.class);
        if (getTarget()) {
            intent.putExtra("type", "online");
        } else {
            intent.putExtra("type", "nfc");
        }
        startActivity(intent);
        finish();
    }
}