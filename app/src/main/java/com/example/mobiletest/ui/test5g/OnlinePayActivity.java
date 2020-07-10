package com.example.mobiletest.ui.test5g;

import android.os.Bundle;

import com.example.mobiletest.BR;
import com.example.mobiletest.base.BaseActivity;
import com.example.mobiletest.databinding.ActivityOnlinePayBinding;

public class OnlinePayActivity extends BaseActivity<ActivityOnlinePayBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setVariable(BR.online, this);
    }
}