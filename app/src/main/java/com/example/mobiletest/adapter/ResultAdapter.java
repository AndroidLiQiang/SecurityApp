package com.example.mobiletest.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobiletest.R;
import com.example.mobiletest.bean.ResultBean;

import java.util.List;

/**
 * author: liqiang
 * e-mail: qiang_li1@asdc.com.cn
 * date  : 2020/8/17
 * desc  :
 */
public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.MyViewHolder> {
    private Context context;
    private List<ResultBean> data;
    private OnItemClickListener onItemClickListener;
    private boolean isRemove = false;

    public ResultAdapter(Context context, List<ResultBean> data) {
        this.context = context;
        this.data = data;
    }

    public void removedItem(int position) {
        if (!isRemove) {
            Toast.makeText(context, "请选择条目", Toast.LENGTH_SHORT).show();
        } else if (data.size() > 0) {
            data.remove(position);
            notifyItemRemoved(position);
        } else {
            Toast.makeText(context, "没有数据可以删除", Toast.LENGTH_SHORT).show();
        }
    }

    public void addItem(ResultBean item) {
        data.add(0, item);
        notifyItemInserted(0);
    }

    public List<ResultBean> getData() {
        return data;
    }

    public boolean getItemIsSelect() {
        return isRemove;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_recycler, parent, false);
        return new MyViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.time.setText(data.get(position).getTime());
        holder.result.setText(data.get(position).getContent());
        holder.item.setOnFocusChangeListener((view, b) -> {
            if (b) {
                onItemClickListener.onItemClick(holder.itemView, holder.getLayoutPosition());
                isRemove = true;
            } else {
                isRemove = false;
            }
        });
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView result;
        TextView time;
        LinearLayout item;

        public MyViewHolder(View itemView) {
            super(itemView);
            result = itemView.findViewById(R.id.result);
            time = itemView.findViewById(R.id.time);
            item = itemView.findViewById(R.id.item);
        }
    }
}