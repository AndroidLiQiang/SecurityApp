package com.example.mobiletest.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mobiletest.R;
import com.example.mobiletest.bean.ResultBean;
import com.example.mobiletest.util.GlideBlurTransformer;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * author: liqiang
 * e-mail: qiang_li1@asdc.com.cn
 * date  : 2020/9/30
 * desc  :
 */
public class NineGridAdapter extends CommonAdapter<ResultBean> {
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
            Glide.with(result)
                    .load(Uri.parse(item.getContent()))
//                    .placeholder(R.mipmap.ic_launcher)
                    .apply(RequestOptions.bitmapTransform(new GlideBlurTransformer(25)))
                    .into(result);
        } else {
            Glide.with(result)
                    .load(Uri.parse(item.getContent()))
//                    .placeholder(R.mipmap.ic_launcher)
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

    public void removedItem(int position) {
        if (!isRemove) {
            Toast.makeText(context, "请选择条目", Toast.LENGTH_SHORT).show();
        } else if (data.size() > 0) {
            data.remove(position);
            notifyDataSetChanged();
        } else {
            Toast.makeText(context, "没有数据可以删除", Toast.LENGTH_SHORT).show();
        }
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
