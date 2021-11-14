package com.joey.myim.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.joey.myim.R;
import com.joey.myim.adapters.RoomsAdapter;
import com.joey.myim.service.RoomChat;
import com.joey.myim.widget.AddFriendDialog;

public class RoomsFragment extends Fragment {
    private String mUserName;
    RecyclerView mRcvRoom;
    Button mBtnAddRoom;
    RoomsAdapter roomsAdapter = null;

    public RoomsFragment() {
        // Required empty public constructor
    }

    public static RoomsFragment newInstance(String param1) {
        RoomsFragment fragment = new RoomsFragment();
        Bundle args = new Bundle();
        args.putString("username", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserName = getArguments().getString("username");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBtnAddRoom = getActivity().findViewById(R.id.btn_add_friend);
        mRcvRoom = view.findViewById(R.id.rcv_all_friends);
        roomsAdapter = new RoomsAdapter(getContext());

        //配置RecyclerView
        mRcvRoom.setLayoutManager(new LinearLayoutManager(getContext()));
       //获得所有群组传入Adapter
        RoomChat.getHostedRoom();
        mRcvRoom.setAdapter(roomsAdapter);
        //添加聊天室按钮单击事件
        mBtnAddRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFriendDialog addRoomDialog = new AddFriendDialog(getContext());
                addRoomDialog.setTitle("创建聊天室");
                addRoomDialog.setCanceledOnTouchOutside(false);
                addRoomDialog.setConfirm("创建", new AddFriendDialog.OnConfirmListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onConfirm(AddFriendDialog customDialog) {
                        String roomName = addRoomDialog.getEtAdd().getText().toString().trim();
                        if(!TextUtils.isEmpty(roomName)){
                            if(RoomChat.joinMultiUserChat(roomName,mUserName)!=null){
                                //获得所有群组
                                RoomChat.getHostedRoom();
                                roomsAdapter.notifyDataSetChanged();
                                addRoomDialog.dismiss();
                                Toast.makeText(getContext(),"创建成功",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getContext(),"创建失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                addRoomDialog.setCancel("取消", new AddFriendDialog.OnCancelListener() {
                    @Override
                    public void onCancel(AddFriendDialog customDialog) {
                        addRoomDialog.dismiss();
                    }
                });
                addRoomDialog.show();
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
        if(roomsAdapter!=null) {
            roomsAdapter.notifyDataSetChanged();
        }
    }
}