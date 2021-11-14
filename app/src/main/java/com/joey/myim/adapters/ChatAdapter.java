package com.joey.myim.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.joey.myim.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天界面Adapter
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static List<String> list = new ArrayList<>();
    Context mContext;

    public ChatAdapter() {
    }

    public ChatAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_chat_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder)holder).mTvChat.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView mTvChat;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvChat = itemView.findViewById(R.id.tv_chat);
        }
    }
}
