package com.example.mobiletest.ui.test5g;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.example.mobiletest.BR;
import com.example.mobiletest.R;
import com.example.mobiletest.adapter.NineGridAdapter;
import com.example.mobiletest.base.BaseActivity;
import com.example.mobiletest.bean.ResultBean;
import com.example.mobiletest.databinding.ActivityImageEncryptOrDecryptBinding;
import com.example.mobiletest.net.Constants;
import com.example.mobiletest.util.SPUtil;
import com.example.teesimmanager.TeeSimManager;
import com.hitomi.tilibrary.style.index.CircleIndexIndicator;
import com.hitomi.tilibrary.style.progress.ProgressBarIndicator;
import com.hitomi.tilibrary.transfer.TransferConfig;
import com.hitomi.tilibrary.transfer.Transferee;
import com.vansz.glideimageloader.GlideImageLoader;
import com.wildma.pictureselector.PictureBean;
import com.wildma.pictureselector.PictureSelector;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

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
 * date   : 2020/9/29
 * desc   : 图片加解密
 */
public class ImageEncryptOrDecryptActivity extends BaseActivity<ActivityImageEncryptOrDecryptBinding> implements TeeSimManager.IDecryptCallback {

    public static final String TAG = "PictureSelector";
    private int location = -1;
    private List<ResultBean> resultData = new ArrayList<>();
    private NineGridAdapter resultAdapter;
    private List<String> testData = new ArrayList<>();
    private TransferConfig config;
    private Transferee transfer;
    private GridLayoutManager gridLayManager = new GridLayoutManager(this, 2);
    private TeeSimManager teeSimManager = new TeeSimManager(this);

    @Override
    protected int getLayoutId() {
        return R.layout.activity_image_encrypt_or_decrypt;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setVariable(BR.ied, this);
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
        teeSimManager.initSEService(this, () -> {
        });
    }

    private void initData() {
        if (SPUtil.getList(Constants.DATA_IMAGE_LIST) != null) {
            resultData = SPUtil.getList(Constants.DATA_IMAGE_LIST);
            for (int i = 0; i < resultData.size(); i++) {
                testData.add(resultData.get(i).getContent());
            }
        }
    }

    @SuppressLint("WrongConstant")
    private void initRecyclerView() {
        binding.recyclerView.setLayoutManager(gridLayManager);
        resultAdapter = new NineGridAdapter(this, resultData);
        binding.recyclerView.setAdapter(resultAdapter);
//        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        //设置增加或删除条目的动画
        ((SimpleItemAnimator) Objects.requireNonNull(binding.recyclerView.getItemAnimator())).setSupportsChangeAnimations(false);
        transfer = Transferee.getDefault(this);
        config = TransferConfig.build()
                .setSourceUrlList(testData)
                .setImageLoader(GlideImageLoader.with(getApplicationContext()))
                .enableScrollingWithPageChange(true)
                .bindRecyclerView(binding.recyclerView, R.id.imageResult);
        resultAdapter.setImageClickListener((itemView, position) -> this.location = position);
        resultAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder viewHolder, int position) {
                if ("1".equals(resultAdapter.getData().get(position).getLock())) {
                    ResultBean resultBean = resultAdapter.getData().get(location);
                    byte[] message = resultBean.getByteContent();
                    if (message != null) {
                        byte[] decrypt = teeSimManager.decrypt(message);
                        byte[] decrypt2 = new byte[]{0x6f, 0x01};
                        if (Arrays.equals(decrypt, decrypt2)) {
                            teeSimManager.decrypt(ImageEncryptOrDecryptActivity.this, message, bytes -> {
                                if (bytes != null) {
                                    config.setNowThumbnailIndex(position);
                                    transfer.apply(config).show();
                                }
                            });
                        } else {
                            config.setNowThumbnailIndex(position);
                            transfer.apply(config).show();
                        }
                    } else {
                        showToast("解密失败");
                    }
                } else {
                    config.setNowThumbnailIndex(position);
                    transfer.apply(config).show();
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                return false;
            }
        });
    }

    /**
     * 图片储存
     */
    public void save5GImage() {
        SPUtil.putList(Constants.DATA_IMAGE_LIST, resultAdapter.getData());
    }

    /**
     * 选择图片
     */
    public void selectPhoto() {
        PictureSelector
                .create(ImageEncryptOrDecryptActivity.this, PictureSelector.SELECT_REQUEST_CODE)
                .selectPicture(false);
    }

    /**
     * 加密
     */
    public void encrypt() {
        if (location != -1) {
            ResultBean resultBean = resultAdapter.getData().get(location);
            if ("2".equals(resultBean.getLock())) {
                StringBuilder message = new StringBuilder(Objects.requireNonNull(resultBean.getContent()));
                String fillStr = "^";
                int length = message.toString().getBytes(StandardCharsets.UTF_8).length;
                int fill = 16 - length % 16;
                for (int i = 0; i < fill; i++) {
                    message.append(fillStr);
                }
                byte[] encrypt = teeSimManager.encrypt(message.toString().getBytes());
                if (encrypt != null) {
                    showToast("加密成功");
                    resultBean.setLock("1");
                    resultBean.setByteContent(encrypt);
                    resultAdapter.notifyItemChanged(location);
                    save5GImage();
                } else {
                    showToast("加密失败");
                }

            } else {
                showToast("图片已加密,不用再次加密");
            }
        } else {
            showToast("请选择图片");
        }

    }

    /**
     * 解密
     */
    public void decrypt() {
        if (location != -1) {
            ResultBean resultBean = resultAdapter.getData().get(location);
            if ("1".equals(resultBean.getLock())) {
                byte[] message = resultBean.getByteContent();
                if (message != null) {
                    byte[] decrypt = teeSimManager.decrypt(message);
                    byte[] decrypt2 = new byte[]{0x6f, 0x01};
                    if (Arrays.equals(decrypt, decrypt2)) {
                        teeSimManager.decrypt(this, message, bytes -> {
                            if (bytes != null) {
                                String decryptMsg;
                                decryptMsg = new String(bytes, StandardCharsets.UTF_8).replaceAll("\\^", "");
                                //解密展示
                                showToast("解密成功");
                                resultBean.setLock("2");
                                resultBean.setContent(decryptMsg);
                                resultAdapter.notifyItemChanged(location);
                                save5GImage();

                            } else {
                                showToast("解密失败");
                            }

                        });
                    } else {
                        String decryptMsg;
                        decryptMsg = new String(decrypt, StandardCharsets.UTF_8).replaceAll("\\^", "");
                        //解密展示
                        showToast("解密成功");
                        resultBean.setLock("2");
                        resultBean.setContent(decryptMsg);
                        resultAdapter.notifyItemChanged(location);
                        save5GImage();

                    }
                } else {
                    showToast("解密失败");
                }
            } else {
                showToast("图片未加密");
            }
        } else {
            showToast("请选择图片");
        }
    }

    /**
     * 删除
     */
    public void delete() {
        resultAdapter.removedItem(location);
        testData.remove(location);
        if (testData.size() == 0) {
            location = -1;
        }
        SPUtil.putList(Constants.DATA_IMAGE_LIST, resultAdapter.getData());
    }

    public void judgeSimState() {
        TelephonyManager manager = (TelephonyManager) getApplicationContext().
                getSystemService(Context.TELEPHONY_SERVICE);
        int simState = manager.getSimState();
        switch (simState) {
            case TelephonyManager.SIM_STATE_UNKNOWN: //0未知状态
            case TelephonyManager.SIM_STATE_ABSENT:  //1没有SIM卡
                //回调暂且为接口解密
                teeSimManager.decrypt(ImageEncryptOrDecryptActivity.this,
                        resultAdapter.getData().get(location).getContent().getBytes(),
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

    @Override
    public void onDecryptCallback(byte[] bytes) {
        if (bytes != null) {
            String message = new String(bytes);
            if (TextUtils.isEmpty(message)) {
                showToast("解密失败");
                teeSimManager.decrypt(this, bytes, this);
            }
        } else {
            showToast("解密数据为空");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*结果回调*/
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                PictureBean pictureBean = data.getParcelableExtra(PictureSelector.PICTURE_RESULT);
                if (pictureBean != null) {
                    resultAdapter.addItem(new ResultBean(pictureBean.getUri().toString(), getTime(), "2"));
                    testData.add(0, pictureBean.getUri().toString());
                    config = TransferConfig.build()
                            .setSourceUrlList(testData)
                            .setProgressIndicator(new ProgressBarIndicator())
                            .setIndexIndicator(new CircleIndexIndicator())
                            .setImageLoader(GlideImageLoader.with(getApplicationContext()))
                            .enableScrollingWithPageChange(true)
                            .bindRecyclerView(binding.recyclerView, R.id.result);
                    transfer.apply(config);
                    resultAdapter.notifyDataSetChanged();
                    save5GImage();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        transfer.destroy();
    }
}