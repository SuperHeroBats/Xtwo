package com.xtwo.android;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MemoryAdapter extends RecyclerView.Adapter<MemoryAdapter.ViewHolder> {

    private Context mContext;
    private List<Memory> mMemoryList;

    public MemoryAdapter(List<Memory> memoryList) {
        mMemoryList = memoryList;
    }

    @NonNull
    @Override
    public MemoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.memory_item, parent, false);

        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Memory memory = mMemoryList.get(position);
                Intent intent = new Intent(mContext, MemoryActivity.class);
                intent.putExtra(MemoryActivity.MEMORY_NAME, memory.getName());
                intent.putExtra(MemoryActivity.MEMORY_IMAGE_ID, memory.getImgageId());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MemoryAdapter.ViewHolder holder, int position) {
        //获取位置 设置值
        Memory memory = mMemoryList.get(position);
        holder.memoryName.setText(memory.getName());
        Glide.with(mContext).load(memory.getImgageId()).into(holder.memoryImage);
    }

    @Override
    public int getItemCount() {
        return mMemoryList.size();
    }

    //实例化控件
    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView memoryImage;
        TextView memoryName;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            memoryImage = view.findViewById(R.id.memory_image);
            memoryName = view.findViewById(R.id.memory_name);
        }
    }
}
