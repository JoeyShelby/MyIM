package com.joey.myim.pages;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.joey.myim.R;
import com.joey.myim.adapters.ChatAdapter;
import com.joey.myim.adapters.RoomChatAdapter;
import com.joey.myim.service.FriendsManage;
import com.joey.myim.service.MessageManage;
import com.joey.myim.service.RoomChat;

import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.packet.Message;
import org.jxmpp.stringprep.XmppStringprepException;

/**
 * 聊天页
 */
public class RoomChatActivity extends AppCompatActivity {
    RecyclerView mRcvChat;
    EditText mEtChat;
    TextView mTvChatRoomName;
    Button mBtnChatSend, mBtnChatBack, mBtnChatDelete;
    RoomChatAdapter roomChatAdapter  = new RoomChatAdapter(RoomChatActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_chat);

        mRcvChat = findViewById(R.id.rcv_chat);
        mEtChat = findViewById(R.id.et_chat);
        mBtnChatSend = findViewById(R.id.btn_chat_send);
        mBtnChatBack = findViewById(R.id.btn_chat_back);
        mBtnChatDelete = findViewById(R.id.btn_chat_delete);
        mTvChatRoomName = findViewById(R.id.tv_chat_room_name);
        String roomName = getIntent().getStringExtra("roomName").trim();

        RoomChat.joinMultiUserChat(roomName,MainActivity.myUserName);
        RoomChat.initListener(roomName);
        //上方群名
        mTvChatRoomName.setText(roomName);

        //返回主页面
        mBtnChatBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //退出聊天室
        mBtnChatDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RoomChatActivity.this);
                builder.setTitle("离开聊天室")
                        .setMessage("是否离开"+roomName)
                        .setCancelable(true)
                        .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(RoomChat.quitRoom(roomName)){
                                    RoomChat.getHostedRoom();
                                    finish();
                                }else{
                                    Toast.makeText(RoomChatActivity.this,"离开聊天室失败",Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                builder.show();
            }
        });

        //绑定Adapter
        mRcvChat.setLayoutManager(new LinearLayoutManager(RoomChatActivity.this));
        mRcvChat.setAdapter(roomChatAdapter);

        //以1s的间隔实时刷新聊天页面
        handler.sendEmptyMessage(0);
        //为发送消息按钮绑定单击事件
        mBtnChatSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sendMessage = mEtChat.getText().toString();
                try {
                    RoomChat.sendChatGroupMessage(roomName,sendMessage);
                    mEtChat.setText("");
                } catch (Exception e) {
                    Toast.makeText(RoomChatActivity.this,"发送失败", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    //以1hz的频率刷新聊天界面
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull android.os.Message msg) {
            super.handleMessage(msg);
            this.postDelayed(() -> {
                roomChatAdapter.notifyDataSetChanged();
                sendEmptyMessage(0);
            },1000);
        }
    };
}