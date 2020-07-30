package com.example.mobiletest.ui.test5g;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.mobiletest.BR;
import com.example.mobiletest.R;
import com.example.mobiletest.base.BaseActivity;
import com.example.mobiletest.databinding.ActivityEncryptOrDecryptBinding;
import com.example.teesimmanager.TeeSimManager;

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
     * 加密
     */
    public byte[] encrypt() {
        String mseeage = binding.message.getText().toString();

        if (mseeage != null && !TextUtils.isEmpty(mseeage)) {
            byte[] encrypt = TeeSimManager.getInstance().encrypt(mseeage.getBytes());
            Toast.makeText(this, "加密" + encrypt, Toast.LENGTH_SHORT).show();
            binding.result.setText(encrypt + "");
            return encrypt;
        } else {
            Toast.makeText(this, "请输入要加密的消息", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    /**
     * 解密
     */
    public void decrypt() {

        /****
         * 正常解密
         */
        byte[] encrypt = encrypt();
        if (encrypt != null) {
            byte[] decrypt = TeeSimManager.getInstance().decrypt(encrypt);
            String message = new String(decrypt);
            Toast.makeText(this, "解密后的消息" + message, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "解密失败，请指纹解密", Toast.LENGTH_SHORT).show();
            /**
             * 正常解密失败   指纹验证解密
             */
            TeeSimManager.getInstance().decrypt(this, encrypt, this);
        }
    }

    @Override
    public void onDecryptCallback(byte[] bytes) {
        if (bytes != null) {
            String message = new String(bytes);
            if (message != null && !TextUtils.isEmpty(message)) {
                Toast.makeText(this, "解密后的消息为" + message, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "解密失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "为空", Toast.LENGTH_SHORT).show();
        }

    }
}