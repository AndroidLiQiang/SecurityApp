package com.example.mobiletest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobiletest.R;

import java.util.List;

/**
 * author: liqiang
 * e-mail: qiang_li1@asdc.com.cn
 * date  : 2020/8/17
 * desc  :
 */
public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.MyViewHolder> {
    private Context context;
    private List<String> data;
    private OnItemClickListener onItemClickListener;
    private boolean isRemove = false;

    public ResultAdapter(Context context, List<String> data) {
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

    public void addItem(String item) {
        data.add(0, item);
        notifyItemInserted(0);
    }

    public List<String> getData() {
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

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.result.setText(data.get(position));
        holder.result.setOnFocusChangeListener((view, b) -> {
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

        public MyViewHolder(View itemView) {
            super(itemView);
            result = itemView.findViewById(R.id.result);
        }
    }
}