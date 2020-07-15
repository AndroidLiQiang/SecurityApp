package com.example.mobiletest.base;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import com.gyf.immersionbar.ImmersionBar;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import me.jessyan.autosize.AutoSizeCompat;

/**
 * <pre>
 *     author : liqiang
 *     e-mail : liqiang02082@kayak.com.cn
 *     time   : 2020/07/09
 *     desc   : BaseActivity
 * </pre>
 */
public class BaseActivity<T extends ViewBinding> extends AppCompatActivity {
    protected T binding;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initImmersionBar();
        Type superclass = getClass().getGenericSuperclass();
        Class<?> aClass = (Class<?>) ((ParameterizedType) superclass).getActualTypeArguments()[0];
        try {
            Method method = aClass.getDeclaredMethod("inflate", LayoutInflater.class);
            binding = (T) method.invoke(null, getLayoutInflater());
            setContentView(binding.getRoot());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
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
