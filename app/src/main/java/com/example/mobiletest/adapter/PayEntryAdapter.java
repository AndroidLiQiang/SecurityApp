package com.example.mobiletest.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobiletest.R;
import com.example.mobiletest.bean.PayEntryBean;

import java.util.List;

/**
 * author: liqiang
 * e-mail: qiang_li1@asdc.com.cn
 * date  : 2020/8/17
 * desc  :
 */
public class PayEntryAdapter extends RecyclerView.Adapter<PayEntryAdapter.EntryViewHolder> {
    private Context context;
    private List<PayEntryBean> data;
    private OnItemClickListener onItemClickListener;

    public PayEntryAdapter(Context context, List<PayEntryBean> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public EntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_entry_recycler, parent, false);
        return new EntryViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull EntryViewHolder holder, int position) {
        holder.title.setText(data.get(position).getTitle());
        holder.content.setText(data.get(position).getContent());
        holder.time.setText(data.get(position).getTime());
        holder.img.setImageDrawable(data.get(position).getImgUrl());
        holder.itemView.setOnClickListener(view -> onItemClickListener.onItemClick(holder.itemView, position));
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setEntryItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class EntryViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView content;
        TextView time;
        ImageView img;

        public EntryViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            time = itemView.findViewById(R.id.time);
            img = itemView.findViewById(R.id.img);
        }
    }
}