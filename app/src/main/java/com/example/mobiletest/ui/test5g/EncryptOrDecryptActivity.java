package com.example.mobiletest.ui.test5g;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mobiletest.BR;
import com.example.mobiletest.R;
import com.example.mobiletest.adapter.ResultAdapter;
import com.example.mobiletest.base.BaseActivity;
import com.example.mobiletest.bean.ResultBean;
import com.example.mobiletest.databinding.ActivityEncryptOrDecryptBinding;
import com.example.mobiletest.net.Constants;
import com.example.mobiletest.util.SPUtil;
import com.example.teesimmanager.TeeSimManager;
import com.example.teesimmanager.TeeSimManager.IDecryptCallback;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * author : liqiang
 * e-mail : qiang_li1@asdc.com.cn
 * date   : 2020/7/9
 * desc   : 5g消息加解密
 */
public class EncryptOrDecryptActivity extends BaseActivity<ActivityEncryptOrDecryptBinding> implements IDecryptCallback {

    private int position;
    private List<ResultBean> data = new ArrayList<>();
    private ResultAdapter resultAdapter;
    private int index = 0;
    private List<String> testData = new ArrayList<>();
    private TeeSimManager teeSimManager = new TeeSimManager(this);

    @Override
    protected int getLayoutId() {
        return R.layout.activity_encrypt_or_decrypt;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setVariable(BR.ed, this);
        initTeeOrSe();
        initData();
        initRecyclerView();
    }

    private void initTeeOrSe() {
        teeSimManager.initTeeService(this, new TeeSimManager.ITeeServiceCallback() {
            @Override
            public void onServiceConnected() {

            }

            @Override
            public void onServiceDisconnected() {

            }
        });
        teeSimManager.initSEService(this, new TeeSimManager.ISEServiceConnectedCallback() {
            @Override
            public void onConnected() {
            }
        });
    }

    private void initData() {
        if (SPUtil.getList(Constants.DATA_5G_LIST) != null) {
            data = SPUtil.getList(Constants.DATA_5G_LIST);
        }
        testData.add("12345678912");
        testData.add(getResources().getString(R.string.test_msg1));
        testData.add(getResources().getString(R.string.test_msg2));
        testData.add(getResources().getString(R.string.test_msg3));
        testData.add(getResources().getString(R.string.test_msg4));
        testData.add(getResources().getString(R.string.test_msg5));
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
        binding.time.setText(getTime());
        if (index == 4) {
            index = 0;
        }
        binding.message.setText(testData.get(index));
        index++;
        /*String encrypt = AESCBCUtil.encrypt(messagetest);
        if (encrypt != null && !TextUtils.isEmpty(encrypt)) {
            SPUtil.putString("encrypt", encrypt);
        }*/
    }

    /**
     * 5G消息本地储存
     */
    public void save5GMsg() {
        SPUtil.putList(Constants.DATA_5G_LIST, resultAdapter.getData());
    }

    /**
     * 加密
     */
    public void encrypt() {
        String message = Objects.requireNonNull(binding.message.getText()).toString();
        byte[] bytes = {1, 2, 3, 4, 5, 6, 7, 8, 9, 1, 1, 2, 3, 4, 5, 6};
        if (message != null) {
            try {
                String fillStr = "^";
                int length = message.getBytes("UTF-8").length;
                int fill = 16 - length % 16;
                for (int i = 0; i < fill; i++) {
                    message = message + fillStr;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            byte[] encrypt = teeSimManager.encrypt(message.getBytes());
//            showToast("加密成功");
            resultAdapter.addItem(new ResultBean(encrypt, getTime(), "1"));
            binding.recyclerView.smoothScrollToPosition(0);
            save5GMsg();
        } else {
            showToast(getString(R.string.please_input_encrypt_msg));
        }
    }

    /**
     * 解密
     */
    public void decrypt() {
        if (!resultAdapter.getItemIsSelect()) {
            showToast(getString(R.string.select_item));
        } else {
            byte[] message = resultAdapter.getData().get(position).getByteContent();
            if (message != null) {
                byte[] decrypt = teeSimManager.decrypt(message);
                byte[] decrypt2 = new byte[]{0x6f, 0x01};
                if (Arrays.equals(decrypt, decrypt2)) {
                    teeSimManager.decrypt(this, message, bytes -> {
                        if (bytes != null) {
                            String decryptMsg = null;
                            decryptMsg = new String(bytes, StandardCharsets.UTF_8).replaceAll("\\^", "");
                            binding.message.setText(decryptMsg);
                            showToast("解密成功");
                        }

                    });
                } else {
                    showToast("解密成功");
                    binding.message.setText(new String(decrypt));
                }

            } else {
                showToast("解密数据为空");
            }
        }
    }

    /**
     * 清除
     */
    public void clear() {
        binding.message.setText("");
        binding.time.setText("");
    }

    /**
     * 删除
     */
    public void delete() {
        resultAdapter.removedItem(position);
        SPUtil.putList(Constants.DATA_5G_LIST, resultAdapter.getData());
    }

    public void judgeSimState() {
        TelephonyManager manager = (TelephonyManager) getApplicationContext().
                getSystemService(Context.TELEPHONY_SERVICE);
        int simState = manager.getSimState();
        switch (simState) {
            case TelephonyManager.SIM_STATE_UNKNOWN: //0未知状态
            case TelephonyManager.SIM_STATE_ABSENT:  //1没有SIM卡
                //解密
                teeSimManager.decrypt(EncryptOrDecryptActivity.this,
                        resultAdapter.getData().get(position).getContent().getBytes(),
                        bytes -> decrypt());
                break;
            case TelephonyManager.SIM_STATE_READY: //5良好
                decrypt();
                break;
            case TelephonyManager.SIM_STATE_CARD_IO_ERROR:
            case TelephonyManager.SIM_STATE_CARD_RESTRICTED:
            case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
            case TelephonyManager.SIM_STATE_NOT_READY:
            case TelephonyManager.SIM_STATE_PERM_DISABLED:
            case TelephonyManager.SIM_STATE_PIN_REQUIRED:
            case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                break;
        }
    }

    /**
     * 获取时间
     */
    public String getTime() {
        Date date = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        return dateFormat.format(date);
    }

    private void teeSimManagerDecrypt(String message) {
        teeSimManager.decrypt(EncryptOrDecryptActivity.this, message.getBytes(), this);
        /*teeSimManager.initTeeService(this, new TeeSimManager.ITeeServiceCallback() {
            @Override
            public void onServiceConnected() {
            }

            @Override
            public void onServiceDisconnected() {

            }
        });*/
    }

    @Override
    public void onDecryptCallback(byte[] bytes) {
        if (bytes != null) {
            String message = new String(bytes);
            if (!TextUtils.isEmpty(message)) {
//                showToast("解密后的消息为" + message);
            } else {
                showToast("解密失败");
                teeSimManager.decrypt(this, bytes, this);
                /*teeSimManager.initTeeService(this, new TeeSimManager.ITeeServiceCallback() {
                    @Override
                    public void onServiceConnected() {
                        teeSimManager.decrypt(this, bytes, this);
                    }

                    @Override
                    public void onServiceDisconnected() {

                    }
                });*/
            }
        } else {
            showToast("解密数据为空");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        teeSimManager.releaseResource();
    }
}