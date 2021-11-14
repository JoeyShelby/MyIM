package com.joey.myim.service;

import android.util.Log;

import com.joey.myim.adapters.ChatAdapter;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

/**
 * 发送消息工具类，创建聊天会话，发送消息
 */
public class MessageManage {
    public static  ChatManager chatManager = ChatManager.getInstanceFor(Connect.getConnect());

    /**
     * 创建聊天会话
     * @param
     * @return
     */
    public static Chat createChat(String user) {

        Chat curChat = null;
        try {
            curChat = chatManager.chatWith(JidCreate.entityBareFrom(user+"@i-bp11sex44qvkoiqip7w5"));
            Log.d("createChat", "创建聊天成功");

        } catch (XmppStringprepException e) {
            e.printStackTrace();
            Log.d("createChat", "创建聊天失败");
        }

        //接受消息监听器
        chatManager.addIncomingListener(new IncomingChatMessageListener() {
            @Override
            public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
                ChatAdapter.list.add(user+":"+message.getBody());

                Log.d("newIncomingMessage", message.toString()+":"+message.getBody());
            }
        });
        return curChat;
    }



    /**
     * 发送消息
     * @param chat
     * @param message
     */
    public static void sendMessage(Chat chat, Message message) {
        try {
            chat.send(message);
            Log.d("sendMessage", "发送消息成功");
        } catch (SmackException.NotConnectedException | InterruptedException e) {
            e.printStackTrace();
            Log.d("sendMessage", "发送消息失败");
        }
    }
}
