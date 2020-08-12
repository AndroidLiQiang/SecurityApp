package com.example.mobiletest.ui.test5g;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.mobiletest.BR;
import com.example.mobiletest.R;
import com.example.mobiletest.base.BaseActivity;
import com.example.mobiletest.bean.EncryptBean;
import com.example.mobiletest.databinding.ActivityEncryptOrDecryptBinding;
import com.example.mobiletest.net.BaseResponse;
import com.example.mobiletest.net.MyObserver;
import com.example.mobiletest.net.RequestUtils;
import com.example.teesimmanager.TeeSimManager;

import java.util.HashMap;

/**
 * author : liqiang
 * e-mail : qiang_li1@asdc.com.cn
 * date   : 2020/7/9
 * desc   : 5g消息加解密
 */
public class EncryptOrDecryptActivity extends BaseActivity<ActivityEncryptOrDecryptBinding> implements TeeSimManager.IDecryptCallback {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_encrypt_or_decrypt;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setVariable(BR.ed, this);
    }

    /**
     * 接收5g消息
     */
    public void get5GMsg() {}

    /**
     * 保存5g消息
     */
    public void save5GMsg() {}

    /**
     * 加密
     */
    public void encrypt() {
        String message = binding.message.getText().toString();
        if (!TextUtils.isEmpty(message)) {
            HashMap<String, String> map = new HashMap<>();
            map.put("data", message);
            RequestUtils.encryptData(this, map, new MyObserver<EncryptBean>(this, false) {
                @Override
                public void onSuccess(BaseResponse<EncryptBean> result) {
                    if (result != null) {
                        String code = result.getCode();
                        int integerCode = Integer.parseInt(code);
                        String message = result.getMessage();
                        if (integerCode == 200) {
                            String message1 = result.getResult().getEncryptOrDecrypt();
                            Toast.makeText(EncryptOrDecryptActivity.this, message, Toast.LENGTH_SHORT).show();
                            binding.result.setText(message1);
                        } else {
                            Toast.makeText(EncryptOrDecryptActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Throwable e, String errorMsg) {
                    Toast.makeText(EncryptOrDecryptActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "请输入要加密的消息", Toast.LENGTH_SHORT).show();
        }
        /*if (mseeage != null && !TextUtils.isEmpty(mseeage)) {
            byte[] encrypt = TeeSimManager.getInstance().encrypt(mseeage.getBytes());
            Toast.makeText(this, "加密" + encrypt, Toast.LENGTH_SHORT).show();
            binding.result.setText(encrypt + "");
            return encrypt;
        } else {
            Toast.makeText(this, "请输入要加密的消息", Toast.LENGTH_SHORT).show();
        }
        return null;*/
    }

    /**
     * 解密
     */
    public void decrypt() {
        String message = binding.result.getText().toString();
        if (!TextUtils.isEmpty(message)) {
            HashMap<String, String> map = new HashMap<>();
            map.put("data", message);
            RequestUtils.decryptData(this, map, new MyObserver<EncryptBean>(this, false) {
                @Override
                public void onSuccess(BaseResponse<EncryptBean> result) {
                    if (result != null) {
                        String code = result.getCode();
                        int integerCode = Integer.parseInt(code);
                        String message1 = result.getMessage();
                        if (integerCode == 200) {
                            String message2 = result.getResult().getEncryptOrDecrypt();
                            Toast.makeText(EncryptOrDecryptActivity.this, message1, Toast.LENGTH_SHORT).show();
                            binding.result.setText(message2);
                        } else {
                            Toast.makeText(EncryptOrDecryptActivity.this, message1 + "解密失败，请指纹解密", Toast.LENGTH_SHORT).show();
                            TeeSimManagerdecrypt(message);
                        }
                    }

                }

                @Override
                public void onFailure(Throwable e, String errorMsg) {
                    Toast.makeText(EncryptOrDecryptActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    TeeSimManagerdecrypt(message);
                }
            });
        }
        /* *//****
         * 正常解密
         *//*
        byte[] encrypt;
        if (encrypt != null) {
            byte[] decrypt = TeeSimManager.getInstance().decrypt(encrypt);
            String message = new String(decrypt);
            Toast.makeText(this, "解密后的消息" + message, Toast.LENGTH_SHORT).show();

            if (decrypt != null && false) {//假设解密失败

                Toast.makeText(this, "解密失败，请指纹解密", Toast.LENGTH_SHORT).show();
                *//**
         * 正常解密失败   指纹验证解密
         *//*
                TeeSimManager.getInstance().decrypt(this, encrypt, this);
            }
        } else {

            Toast.makeText(this, "解密数据为空", Toast.LENGTH_SHORT).show();

        }*/
    }

    /**
     * 清除
     */
    public void clear() {}

    private void TeeSimManagerdecrypt(String message) {
        TeeSimManager.getInstance().decrypt(EncryptOrDecryptActivity.this, message.getBytes(), this);
    }

    @Override
    public void onDecryptCallback(byte[] bytes) {
        if (bytes != null) {
            String message = new String(bytes);
            if (message != null && !TextUtils.isEmpty(message)) {

                Toast.makeText(this, "解密后的消息为" + message, Toast.LENGTH_SHORT).show();

            } else {

                Toast.makeText(this, "解密失败", Toast.LENGTH_SHORT).show();

                TeeSimManager.getInstance().decrypt(this, bytes, this);
            }
        } else {
            Toast.makeText(this, "解密数据为空", Toast.LENGTH_SHORT).show();
        }

    }
}