package com.joey.myim.pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.joey.myim.adapters.FriendsAdapter;
import com.joey.myim.service.Connect;
import com.joey.myim.service.FriendsManage;
import com.joey.myim.service.MessageManage;
import com.joey.myim.R;
import com.joey.myim.adapters.ChatAdapter;

import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.packet.Message;
import org.jxmpp.stringprep.XmppStringprepException;

/**
 * 聊天页
 */
public class ChatActivity extends AppCompatActivity {
    RecyclerView mRcvChat;
    EditText mEtChat;
    TextView mTvChatUsername;
    Button mBtnChatSend, mBtnChatBack, mBtnChatDelete;
    ChatAdapter chatAdapter = new ChatAdapter(ChatActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mRcvChat = findViewById(R.id.rcv_chat);
        mEtChat = findViewById(R.id.et_chat);
        mBtnChatSend = findViewById(R.id.btn_chat_send);
        mBtnChatBack = findViewById(R.id.btn_chat_back);
        mBtnChatDelete = findViewById(R.id.btn_chat_delete);
        mTvChatUsername = findViewById(R.id.tv_chat_username);
        String username = getIntent().getStringExtra("username").trim();
        Chat chat = MessageManage.createChat(username);

        //上方好友昵称
        mTvChatUsername.setText(username);

        //返回主页面
        mBtnChatBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //删除好友
        mBtnChatDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                builder.setTitle("删除好友")
                        .setMessage("是否删除"+username)
                        .setCancelable(true)
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(FriendsManage.deleteFriend(username)){
                                    FriendsManage.getAllFriends();
                                    finish();
                                }else{
                                    Toast.makeText(ChatActivity.this,"删除好友失败",Toast.LENGTH_SHORT).show();
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
        mRcvChat.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        mRcvChat.setAdapter(chatAdapter);

        //以1s的间隔实时刷新聊天页面
        handler.sendEmptyMessage(0);
        //为发送消息按钮绑定单击事件
        mBtnChatSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sendMessage = mEtChat.getText().toString();
                try {
                    MessageManage.sendMessage(chat,new Message(username+"@i-bp11sex44qvkoiqip7w5",sendMessage));
                    ChatAdapter.list.add("me: " + sendMessage);
                    mEtChat.setText("");
                } catch (XmppStringprepException e) {
                    Toast.makeText(ChatActivity.this,"发送失败", Toast.LENGTH_SHORT).show();
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
                chatAdapter.notifyDataSetChanged();
                sendEmptyMessage(0);
            },1000);
        }
    };
}