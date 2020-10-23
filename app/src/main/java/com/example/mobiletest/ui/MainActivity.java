package com.example.mobiletest.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import com.example.mobiletest.BR;
import com.example.mobiletest.R;
import com.example.mobiletest.base.BaseActivity;
import com.example.mobiletest.bean.MacBean;
import com.example.mobiletest.bean.RandomBean;
import com.example.mobiletest.databinding.ActivityMainBinding;
import com.example.mobiletest.net.BaseResponse;
import com.example.mobiletest.net.MyObserver;
import com.example.mobiletest.net.RequestUtils;
import com.example.mobiletest.ui.test5g.Test5GMsgActivity;
import com.example.teesimmanager.TeeSimManager;
import com.tbruyelle.rxpermissions3.RxPermissions;

import java.util.HashMap;

/**
 * author : liqiang
 * e-mail : qiang_li1@asdc.com.cn
 * date   : 2020/7/9
 * desc   :
 */
public class MainActivity extends BaseActivity<ActivityMainBinding> {
    private String TAG = "MainActivity";
    final RxPermissions rxPermissions = new RxPermissions(this);
    private TeeSimManager teeSimManager = new TeeSimManager(this);

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTeeOrSe();
        binding.setVariable(BR.main, this);
        getPermissions();
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

    private void getPermissions() {
        rxPermissions
                .requestEach(Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA,
                        Manifest.permission.MODIFY_AUDIO_SETTINGS, Manifest.permission.SYSTEM_ALERT_WINDOW)
                .subscribe(permission -> { // will emit 2 Permission objects
                    if (permission.granted) {// 同意后调用

                    } else if (permission.shouldShowRequestPermissionRationale) {// 禁止，但没有选择“以后不再询问”，以后申请权限，会继续弹出提示

                    } else {// 禁止，但选择“以后不再询问”，以后申请权限，不会继续弹出提示
                    }
                });
    }

    public void goLogin() {
        getRandom();
    }

    public void goTest5GMsg() {
        startActivity(new Intent(this, Test5GMsgActivity.class));
    }

    /**
     * 获取随机数
     */
    private void getRandom() {
        HashMap<String, String> map = new HashMap<>();
        map.put("type", "login");
        RequestUtils.getRandom(this, map, new MyObserver<RandomBean>(this, false) {
            @Override
            public void onSuccess(BaseResponse<RandomBean> result) {
//                showToast(result.getMessage());
                //TODO 获取mac
                byte[] mac = teeSimManager.authenticate(result.getResult().getRandom().getBytes());
                //无卡返回null；
                //  有卡返回1字节状态+16字节mac，其中状态字节1表示在线，0表示不在线
                if (mac == null) {
                    showToast("无SIM卡,登录失败");
                } else if (mac[0] == 1) {//1在线
                    showToast("有SIM卡,登录成功");
                    verifyMac(mac);
                } else if (mac[0] == 0) {//0表示不在线
                    showToast("有SIM卡,登录失败(SIM卡不在线)");
                }
            }

            @Override
            public void onFailure(Throwable e, String errorMsg) {
                showToast(errorMsg);
            }
        });
    }

    /**
     * 验证mac
     */
    private void verifyMac(byte[] mac) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", "mac");
        map.put("mac", mac);
        RequestUtils.verifyMac(this, map, new MyObserver<MacBean>(this, true) {
            @Override
            public void onSuccess(BaseResponse<MacBean> result) {
//                showToast(result.getResult().getMac());
                showToast("登录成功");
                startActivity(new Intent(MainActivity.this, Test5GMsgActivity.class));
            }

            @Override
            public void onFailure(Throwable e, String errorMsg) {
                showToast(errorMsg);
            }
        });
    }
}