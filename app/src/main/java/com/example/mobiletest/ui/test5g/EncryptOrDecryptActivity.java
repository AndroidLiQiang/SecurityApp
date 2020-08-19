package com.example.mobiletest.ui.test5g;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mobiletest.BR;
import com.example.mobiletest.R;
import com.example.mobiletest.adapter.ResultAdapter;
import com.example.mobiletest.base.BaseActivity;
import com.example.mobiletest.bean.EncryptBean;
import com.example.mobiletest.bean.ResultBean;
import com.example.mobiletest.databinding.ActivityEncryptOrDecryptBinding;
import com.example.mobiletest.net.BaseResponse;
import com.example.mobiletest.net.Constants;
import com.example.mobiletest.net.MyObserver;
import com.example.mobiletest.net.RequestUtils;
import com.example.mobiletest.util.SPUtil;
import com.example.mobiletest.util.StringUtil;
import com.example.teesimmanager.TeeSimManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * author : liqiang
 * e-mail : qiang_li1@asdc.com.cn
 * date   : 2020/7/9
 * desc   : 5g消息加解密
 */
public class EncryptOrDecryptActivity extends BaseActivity<ActivityEncryptOrDecryptBinding> implements TeeSimManager.IDecryptCallback {

    private int position;
    private List<ResultBean> data = new ArrayList<>();
    private ResultAdapter resultAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_encrypt_or_decrypt;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setVariable(BR.ed, this);
        initData();
        initRecyclerView();
    }

    private void initData() {
        if (SPUtil.getList(Constants.DATA_LIST) != null) {
            data = SPUtil.getList(Constants.DATA_LIST);
        }
    }

    @SuppressLint("WrongConstant")
    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(layoutManager);
        resultAdapter = new ResultAdapter(this, data);
        binding.recyclerView.setAdapter(resultAdapter);
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        //设置增加或删除条目的动画
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        resultAdapter.setOnItemClickListener((itemView, position) -> this.position = position);
    }

    /**
     * 接收5g消息
     */
    @SuppressLint("SetTextI18n")
    public void get5GMsg() {
        binding.message.setText(getTime() + " 5G消息 " + StringUtil.getRandomString(6));
        /*String encrypt = AESCBCUtil.encrypt(messagetest);
        if (encrypt != null && !TextUtils.isEmpty(encrypt)) {
            SPUtil.putString("encrypt", encrypt);
        }*/
    }

    /**
     * 5G消息本地储存
     */
    public void save5GMsg() {
        SPUtil.putList(Constants.DATA_LIST, resultAdapter.getData());
    }

    /**
     * 加密
     */
    public void encrypt() {
        String message = Objects.requireNonNull(binding.message.getText()).toString();
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
                            resultAdapter.addItem(new ResultBean(message1, getTime()));
                            binding.recyclerView.smoothScrollToPosition(0);
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
        if (!resultAdapter.getItemIsSelect()) {
            Toast.makeText(this, "请选择条目", Toast.LENGTH_SHORT).show();
        } else {
            String message = resultAdapter.getData().get(position).getContent();
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
                                binding.message.setText(message2);
                            } else {
                                Toast.makeText(EncryptOrDecryptActivity.this, message1 + "解密失败，请指纹解密", Toast.LENGTH_SHORT).show();
                                teeSimManagerDecrypt(message);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {
                        Toast.makeText(EncryptOrDecryptActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                        teeSimManagerDecrypt(message);
                    }
                });
            } else {
                Toast.makeText(this, "请先进行加密", Toast.LENGTH_SHORT).show();
            }
        /*//正常解密
        byte[] encrypt;
        if (encrypt != null) {
            byte[] decrypt = TeeSimManager.getInstance().decrypt(encrypt);
            String message = new String(decrypt);
            Toast.makeText(this, "解密后的消息" + message, Toast.LENGTH_SHORT).show();

            if (decrypt != null && false) {//假设解密失败

                Toast.makeText(this, "解密失败，请指纹解密", Toast.LENGTH_SHORT).show();
                //正常解密失败   指纹验证解密
                TeeSimManager.getInstance().decrypt(this, encrypt, this);
            }
        } else {

            Toast.makeText(this, "解密数据为空", Toast.LENGTH_SHORT).show();

        }*/
        }
    }

    /**
     * 清除
     */
    public void clear() {
        binding.message.setText("");
    }

    /**
     * 删除
     */
    public void delete() {
        resultAdapter.removedItem(position);
        SPUtil.putList(Constants.DATA_LIST, resultAdapter.getData());
    }

    /**
     * 获取时间
     *
     * @return
     */
    public String getTime() {
        Date date = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        return dateFormat.format(date);
    }

    private void teeSimManagerDecrypt(String message) {
        TeeSimManager.getInstance().decrypt(EncryptOrDecryptActivity.this, message.getBytes(), this);
    }

    @Override
    public void onDecryptCallback(byte[] bytes) {
        if (bytes != null) {
            String message = new String(bytes);
            if (!TextUtils.isEmpty(message)) {

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