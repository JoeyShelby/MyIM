package com.joey.myim.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.joey.myim.R;
import com.joey.myim.adapters.FriendsAdapter;
import com.joey.myim.pages.MainActivity;
import com.joey.myim.service.FriendsManage;
import com.joey.myim.widget.AddFriendDialog;

public class FriendsFragment extends Fragment {
    RecyclerView mRcvFriends;
    Button mBtnAddFriend;
    FriendsAdapter friendsAdapter = null;

    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBtnAddFriend = getActivity().findViewById(R.id.btn_add_friend);
        mRcvFriends = view.findViewById(R.id.rcv_all_friends);
        friendsAdapter = new FriendsAdapter(getContext());

        //配置RecyclerView
        mRcvFriends.setLayoutManager(new LinearLayoutManager(getContext()));
        FriendsManage.getAllFriends();
        mRcvFriends.setAdapter(friendsAdapter);

        //添加好友按钮单击事件
        mBtnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFriendDialog addFriendDialog = new AddFriendDialog(getContext());
                addFriendDialog.setTitle("添加好友");
                addFriendDialog.setCanceledOnTouchOutside(false);
                addFriendDialog.setConfirm("添加", new AddFriendDialog.OnConfirmListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onConfirm(AddFriendDialog customDialog) {
                        String username = addFriendDialog.getEtAdd().getText().toString().trim();
                        if(!TextUtils.isEmpty(username)){
                            if(FriendsManage.addFriend(username,username,null)){
                                FriendsManage.getAllFriends();
                                friendsAdapter.notifyDataSetChanged();
                                addFriendDialog.dismiss();
                                Toast.makeText(getContext(),"添加成功",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getContext(),"添加失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                addFriendDialog.setCancel("取消", new AddFriendDialog.OnCancelListener() {
                    @Override
                    public void onCancel(AddFriendDialog customDialog) {
                        addFriendDialog.dismiss();
                    }
                });
                addFriendDialog.show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friends, container, false);

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResume() {
        super.onResume();
        if(friendsAdapter!=null) {
            friendsAdapter.notifyDataSetChanged();
        }
    }
}