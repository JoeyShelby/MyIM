package com.joey.myim.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.joey.myim.R;
import com.joey.myim.pages.ChatActivity;

import org.jivesoftware.smack.roster.RosterEntry;

import java.util.LinkedList;
import java.util.List;

/**
 * 显示可聊天好友页面Adapter
 */
public class FriendsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static List<RosterEntry> rosterEntries = new LinkedList<>();
    Context mContext;
    public FriendsAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_friend_item,parent,false ));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder)holder).mTvName.setText(rosterEntries.get(position).getName().toString());
        ((ItemViewHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,ChatActivity.class);
                intent.putExtra("username",((ItemViewHolder) holder).mTvName.getText());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return rosterEntries.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView mTvName;
        ImageView mImage;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.image_photo);
            mTvName = itemView.findViewById(R.id.tv_friend_name);
        }
    }
}
