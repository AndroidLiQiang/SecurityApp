package com.example.mobiletest.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;

/**
 * author: liqiang
 * e-mail: qiang_li1@asdc.com.cn
 * date  : 2020/8/26
 * desc  :
 */
public class FingerManager {
    private static FingerManager fingerManager;

    public enum SupportResult {
        DEVICE_UNSUPPORTED,//不支持指纹识别
        SUPPORT_WITHOUT_DATA,//支持指纹识别但没有指纹数据
        SUPPORT,//支持且有指纹数据
    }

    private static FingerManager getInstance() {
        if (fingerManager == null) {
            synchronized (FingerManager.class) {
                if (fingerManager == null) {
                    fingerManager = new FingerManager();
                }
            }
        }
        return fingerManager;
    }

    /**
     * 检查指纹
     */
    @SuppressLint("MissingPermission")
    public static SupportResult checkSupport(Context context) {
        FingerprintManager fingerprintManager = context.getSystemService(FingerprintManager.class);
        if (fingerprintManager.isHardwareDetected()) {
            if (fingerprintManager.hasEnrolledFingerprints()) {
                return SupportResult.SUPPORT;
            } else {
                return SupportResult.SUPPORT_WITHOUT_DATA;
            }
        } else {
            return SupportResult.DEVICE_UNSUPPORTED;
        }
    }
}
