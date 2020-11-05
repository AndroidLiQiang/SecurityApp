package com.example.mobiletest.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mobiletest.R;
import com.example.mobiletest.bean.ResultBean;
import com.example.mobiletest.util.Base64AndPic;
import com.example.mobiletest.util.FileUtil;
import com.example.mobiletest.util.GlideBlurTransformer;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.File;
import java.util.List;

/**
 * author: liqiang
 * e-mail: qiang_li1@asdc.com.cn
 * date  : 2020/9/30
 * desc  :
 */
public class NineGridAdapter extends CommonAdapter<ResultBean> {
    private static final String TAG = "NineGridAdapter";
    private Context context;
    private List<ResultBean> data;
    private OnImageClickListener onImageClickListener;
    private boolean isRemove = false;

    public NineGridAdapter(Context context, List<ResultBean> data) {
        super(context, R.layout.img_item_recycler, data);
        this.context = context;
        this.data = data;
    }

    @Override
    protected void convert(ViewHolder holder, ResultBean item, final int position) {
        ImageView result = holder.getView(R.id.imageResult);
        ImageView lock = holder.getView(R.id.lock);
        TextView time = holder.getView(R.id.time);
        time.setText(data.get(position).getTime());
        boolean isLock = "1".equals(data.get(position).getLock());
        lock.setVisibility(isLock ? View.VISIBLE : View.INVISIBLE);
        if (isLock) {
            Bitmap bitmap = Base64AndPic.base64ToImage(item.getBitmapStr());
            Log.d(TAG, "convert: " + bitmap);
            Glide.with(result)
                    .load(bitmap)
                    .apply(RequestOptions.bitmapTransform(new GlideBlurTransformer(25)))
                    .into(result);
        } else {
            Log.d(TAG, "convert: 解密图片地址" + item.getContent());
            Glide.with(result)
                    .load(Uri.parse(item.getContent()))
                    .into(result);
        }

        holder.itemView.setOnFocusChangeListener((view, b) -> {
            if (b) {
                onImageClickListener.onImageClick(holder.itemView, holder.getLayoutPosition());
                isRemove = true;
            } else {
                isRemove = false;
            }
        });
    }

    public boolean removedItem(int position) {
        if (!isRemove) {
            Toast.makeText(context, "请选择条目", Toast.LENGTH_SHORT).show();
        } else if (data.size() > 0) {
            //隐藏图片
            File hiddenFile = new File(Environment.getExternalStorageDirectory().getPath() + "/DCIM/mobilePhoto/.nomedia/" + data.get(position).getPhotoNam());
            FileUtil.deleteFolder(hiddenFile);

            data.remove(position);
            notifyDataSetChanged();
            return true;
        } else {
            Toast.makeText(context, "没有数据可以删除", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void addItem(ResultBean item) {
        data.add(0, item);
    }

    public List<ResultBean> getData() {
        return data;
    }

    public void setData(List<ResultBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public boolean getItemIsSelect() {
        return isRemove;
    }

    public interface OnImageClickListener {
        void onImageClick(View itemView, int position);
    }

    public void setImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }
}
