package com.example.mobiletest.ui.test5g;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.mobiletest.BR;
import com.example.mobiletest.R;
import com.example.mobiletest.base.BaseActivity;
import com.example.mobiletest.databinding.ActivityEncryptOrDecryptBinding;

/**
 * author : liqiang
 * e-mail : qiang_li1@asdc.com.cn
 * date   : 2020/7/9
 * desc   : 5g消息加解密
 */
public class EncryptOrDecryptActivity extends BaseActivity<ActivityEncryptOrDecryptBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setVariable(BR.ed, this);
    }

    /**
     * 加密
     */
    public void encrypt() {
        Toast.makeText(this, "加密", Toast.LENGTH_SHORT).show();
    }

    /**
     * 解密
     */
    public void decrypt() {
        Toast.makeText(this, "解密", Toast.LENGTH_SHORT).show();
    }

}