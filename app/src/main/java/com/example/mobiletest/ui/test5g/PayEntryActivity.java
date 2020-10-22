package com.example.mobiletest.ui.test5g;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mobiletest.BR;
import com.example.mobiletest.R;
import com.example.mobiletest.adapter.PayEntryAdapter;
import com.example.mobiletest.base.BaseActivity;
import com.example.mobiletest.bean.PayEntryBean;
import com.example.mobiletest.databinding.ActivityPayEntryBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * author : liqiang
 * e-mail : qiang_li1@asdc.com.cn
 * date   : 2020/10/20
 * desc   : 支付消息列表
 */
public class PayEntryActivity extends BaseActivity<ActivityPayEntryBinding> {
    private List<PayEntryBean> data;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pay_entry;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setVariable(BR.entry, this);
        initData();
    }

    private void initData() {
        data = new ArrayList<>();
        data.add(new PayEntryBean("海底捞", "您有一笔金额未支付", "10-08", getResources().getDrawable(R.mipmap.ic_logo1), "333"));
        data.add(new PayEntryBean("星巴克", "您有一笔金额未支付", "10-04", getResources().getDrawable(R.mipmap.ic_logo4), "88"));
        data.add(new PayEntryBean("会员卡充值", "您有一笔金额未支付", "09-31", getResources().getDrawable(R.mipmap.ic_logo3), "500"));
        data.add(new PayEntryBean("话费充值", "您有一笔金额未支付", "09-22", getResources().getDrawable(R.mipmap.ic_logo2), "100"));
        PayEntryAdapter payEntryAdapter = new PayEntryAdapter(this, data);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.entryRV.setLayoutManager(layoutManager);
        binding.entryRV.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        binding.entryRV.setAdapter(payEntryAdapter);
        payEntryAdapter.setEntryItemClickListener((itemView, position) -> {
            Intent intent = new Intent(PayEntryActivity.this, PayMsgDetailsActivity.class);
            Bundle mBundle = new Bundle();
            mBundle.putString("titleEntry", data.get(position).getTitle());
            mBundle.putString("contentEntry", data.get(position).getContent());
            mBundle.putString("timeEntry", data.get(position).getTime());
            mBundle.putString("moneyEntry", data.get(position).getMoney());
            intent.putExtras(mBundle);
            startActivity(intent);
        });
    }


}