package com.example.mobiletest.base;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewbinding.ViewBinding;

import com.gyf.immersionbar.ImmersionBar;

import me.jessyan.autosize.AutoSizeCompat;

/**
 * <pre>
 *     author : liqiang
 *     e-mail : liqiang02082@kayak.com.cn
 *     time   : 2020/07/09
 *     desc   : BaseActivity
 * </pre>
 */
public abstract class BaseActivity<T extends ViewBinding> extends AppCompatActivity {
    protected T binding;

    protected abstract int getLayoutId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initImmersionBar();
        binding = (T) DataBindingUtil.setContentView(this, getLayoutId());
    }

    public void back() {
        finish();
    }

    private void initImmersionBar() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f)
                .navigationBarColor("#FFFFFF")
                .navigationBarDarkIcon(true)
                .keyboardEnable(false)
                .fitsSystemWindows(true)
                .statusBarColor("#FAFAFA")
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
