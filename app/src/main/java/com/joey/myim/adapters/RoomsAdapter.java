package com.joey.myim.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.joey.myim.R;
import com.joey.myim.pages.ChatActivity;
import com.joey.myim.pages.RoomChatActivity;

import java.util.ArrayList;
import java.util.List;

public class RoomsAdapter extends RecyclerView.Adapter {
    public static List<String> list = new ArrayList<>();
    Context mContext;

    public RoomsAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RoomsAdapter.ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_room_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((RoomsAdapter.ItemViewHolder)holder).mTvRoomName.setText(list.get(position));
        ((RoomsAdapter.ItemViewHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, RoomChatActivity.class);
                intent.putExtra("roomName",((RoomsAdapter.ItemViewHolder) holder).mTvRoomName.getText());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView mTvRoomName;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvRoomName = itemView.findViewById(R.id.tv_room_name);
        }
    }
}
