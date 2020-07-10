package com.example.mobiletest.ui.test5g;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.mobiletest.BR;
import com.example.mobiletest.R;
import com.example.mobiletest.base.BaseActivity;
import com.example.mobiletest.databinding.ActivityNfcPayBinding;

public class NFCPayActivity extends BaseActivity<ActivityNfcPayBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setVariable(BR.nfc, this);
    }
}