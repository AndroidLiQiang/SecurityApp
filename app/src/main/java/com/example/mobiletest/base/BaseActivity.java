package com.example.mobiletest.base;

import android.content.res.Resources;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.gyf.immersionbar.ImmersionBar;

import me.jessyan.autosize.AutoSizeCompat;

/**
 * author : liqiang
 * e-mail : qiang_li1@asdc.com.cn
 * time   : 2020/07/09
 * desc   : BaseActivity
 */
public abstract class BaseActivity<T extends ViewDataBinding> extends AppCompatActivity {

    protected T binding;

    protected abstract int getLayoutId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initImmersionBar();
        binding = DataBindingUtil.setContentView(this, getLayoutId());
    }

    public void back() {
        finish();
    }

    public void showToast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    private void initImmersionBar() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f)
                .navigationBarColor("#FFFFFF")
                .navigationBarDarkIcon(true)
                .keyboardEnable(false)
                .fitsSystemWindows(true)
                .statusBarColor("#FFFFFF")
                .init();
    }

    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        AutoSizeCompat.autoConvertDensityOfGlobal(resources);
        return resources;
    }

    @Override
    protected void onResume() {
        super.onResume();
        AutoSizeCompat.autoConvertDensityOfGlobal(super.getResources());
    }
}