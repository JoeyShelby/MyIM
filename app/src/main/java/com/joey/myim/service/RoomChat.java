package com.joey.myim.service;

import android.text.TextUtils;
import android.util.Log;

import com.joey.myim.adapters.RoomChatAdapter;
import com.joey.myim.adapters.RoomsAdapter;
import com.joey.myim.pages.MainActivity;

import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MucEnterConfiguration;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatException;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.xdata.Form;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Resourcepart;
import org.jxmpp.stringprep.XmppStringprepException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 群聊
 */
public class RoomChat {
    //room管理对象
    static MultiUserChatManager multiUserChatManager = MultiUserChatManager.getInstanceFor(Connect.getConnect());

   // 创建群聊
    public static MultiUserChat createRoom(String roomName, String nickName, String username) {
        MultiUserChat multiUserChat = null;
        try {
            EntityBareJid roomJid = JidCreate.entityBareFrom(roomName + "@conference." + Connect.getConnect().getXMPPServiceDomain());

            //创建聊天室
            multiUserChat = multiUserChatManager.getMultiUserChat(roomJid);
            multiUserChat.createOrJoin(Resourcepart.fromOrNull(nickName));
            //获得配置表单
            Form form = multiUserChat.getConfigurationForm();
            //根据原始表单创建一个要提交的新表单
            Form answerForm = form.createAnswerForm();
            // 设置聊天室的新拥有者
            List<String> owners = new ArrayList<>();
            owners.add(username+ "@" + Connect.getConnect().getXMPPServiceDomain());

            answerForm.setAnswer("muc#roomconfig_roomowners", owners);
            //设置为公共房间
            answerForm.setAnswer("muc#roomconfig_publicroom", true);
            // 设置聊天室是持久聊天室，即将要被保存下来
            answerForm.setAnswer("muc#roomconfig_persistentroom", true);
            // 房间仅对成员开放
            answerForm.setAnswer("muc#roomconfig_membersonly", false);
            // 允许占有者邀请其他人
            answerForm.setAnswer("muc#roomconfig_allowinvites", true);
            //进入不需要密码
            answerForm.setAnswer("muc#roomconfig_passwordprotectedroom",  false);
            // 能够发现占有者真实 JID 的角色
            // submitForm.setAnswer("muc#roomconfig_whois", "anyone");
            // 登录房间对话
            answerForm.setAnswer("muc#roomconfig_enablelogging", true);
            // 仅允许注册的昵称登录
            answerForm.setAnswer("x-muc#roomconfig_reservednick", false);
            // 允许使用者修改昵称
            answerForm.setAnswer("x-muc#roomconfig_canchangenick", true);
            // 允许用户注册房间
            answerForm.setAnswer("x-muc#roomconfig_registration", false);
            // 发送已完成的表单（有默认值）到服务器来配置聊天室
            multiUserChat.sendConfigurationForm(answerForm);
            //添加群消息监听
            multiUserChat.addMessageListener(new MessageListener() {
                @Override
                public void processMessage(Message message) {
                    Log.d("message", message.getBody());
                }
            });

            Log.d("createRoom", "聊天室创建成功");
            return multiUserChat;
        } catch (XmppStringprepException | MultiUserChatException.MucAlreadyJoinedException | SmackException.NotConnectedException | MultiUserChatException.NotAMucServiceException | XMPPException.XMPPErrorException | SmackException.NoResponseException | InterruptedException e) {
            e.printStackTrace();
            Log.d("createRoom", "聊天室创建失败");
            return null;
        }
    }

    /**
     * 加入群聊会议室
     * @param groupName
     * @param nickName
     * @return
     */
    public static MultiUserChat joinMultiUserChat(String groupName, String nickName){
        try {
            //群jid
            String jid = groupName + "@conference." + Connect.getConnect().getXMPPServiceDomain();
            //jid实体创建
            EntityBareJid groupJid = JidCreate.entityBareFrom(jid);

            //获取群管理对象
            MultiUserChatManager multiUserChatManager = MultiUserChatManager.getInstanceFor(Connect.getConnect());
            //通过群管理对象获取该群房间对象
            MultiUserChat multiUserChat = multiUserChatManager.getMultiUserChat(groupJid);

            MucEnterConfiguration.Builder builder = multiUserChat.getEnterConfigurationBuilder(Resourcepart.from(nickName));
            //只获取最后99条历史记录
            builder.requestMaxCharsHistory(99);
            MucEnterConfiguration mucEnterConfiguration = builder.build();
            //加入群
            multiUserChat.join(mucEnterConfiguration);

            Log.d("joinMultiUserChat", "加入群聊成功");
            return multiUserChat;
        } catch (XmppStringprepException | XMPPException.XMPPErrorException | SmackException.NotConnectedException | SmackException.NoResponseException | MultiUserChatException.NotAMucServiceException | InterruptedException e) {
            e.printStackTrace();
            Log.d("joinMultiUserChat", "加入群聊失败");
            return null;
        }
    }

    /**
     * 发送群聊普通消息
     * @param groupName
     * @param body
     */
    public static boolean sendChatGroupMessage(String groupName, String body){
        try {
            //拼凑jid
            String jid = groupName + "@conference." + Connect.getConnect().getXMPPServiceDomain();
            //创建jid实体
            EntityBareJid groupJid = JidCreate.entityBareFrom(jid);
            //群管理对象
            MultiUserChatManager multiUserChatManager = MultiUserChatManager.getInstanceFor(Connect.getConnect());
            MultiUserChat multiUserChat = multiUserChatManager.getMultiUserChat(groupJid);
            //发送信息
            multiUserChat.sendMessage(body);
            Log.d("sendChatGroupMessage", "群消息发送成功");
            return true;
        } catch (XmppStringprepException | SmackException.NotConnectedException | InterruptedException e) {
            e.printStackTrace();
            Log.d("sendChatGroupMessage", "群消息发送失败");
            return false;
        }
    }

    /**
     * 退出群聊
     * @param groupName
     * @throws XmppStringprepException
     */
    public static boolean quitRoom(String groupName){

        try {
            String jid = groupName + "@conference." + Connect.getConnect().getXMPPServiceDomain();
            EntityBareJid groupJid = JidCreate.entityBareFrom(jid);

            MultiUserChatManager multiUserChatManager = MultiUserChatManager.getInstanceFor(Connect.getConnect());
            MultiUserChat multiUserChat = multiUserChatManager.getMultiUserChat(groupJid);

            //退出群
            multiUserChat.leave();
            Log.d("quitRoom", "退出群聊成功");
            return true;
        } catch (XmppStringprepException | SmackException.NotConnectedException | InterruptedException e) {
            e.printStackTrace();
            Log.d("quitRoom", "退出群聊失败");
            return false;
        }
    }

    /**
     * 获取服务器上的所有群组
     * @return
     */
    public static Map<EntityBareJid, HostedRoom> getHostedRoom() {
        MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(Connect.getConnect());
        try {
            Map<EntityBareJid, HostedRoom> roomsHostedBy = manager.getRoomsHostedBy(manager.getMucServiceDomains().get(0));
            RoomsAdapter.list.clear();
            for (Map.Entry<EntityBareJid, HostedRoom> entry : roomsHostedBy.entrySet()) {
                String s = entry.getKey().getLocalpart().toString();
                RoomsAdapter.list.add(s);
                Log.d("getHostedRoom",s);
            }
            return roomsHostedBy;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 绑定收到消息监听器
     */
    public static boolean initListener(String roomName) {
        String jid = roomName + "@conference." + Connect.getConnect().getXMPPServiceDomain();
        try {
            EntityBareJid groupJid = JidCreate.entityBareFrom(jid);
            MultiUserChat multiUserChat = MultiUserChatManager.getInstanceFor(Connect.getConnect()).getMultiUserChat(groupJid);
            multiUserChat.addMessageListener(new MessageListener() {
                @Override
                public void processMessage(final Message message) {
                    //当消息返回为空的时候，表示用户正在聊天窗口编辑信息并未发出消息
                    if (!TextUtils.isEmpty(message.getBody())) {
                        String from = message.getFrom().getResourceOrEmpty().toString();
                        RoomChatAdapter.list.add(from+": "+message.getBody());
                        Log.d("message", message.toString());
                    }
                }
            });
            Log.d("initListener", "聊天室绑定接收消息监听器成功");
            return true;
        } catch (XmppStringprepException e) {
            e.printStackTrace();
            Log.d("initListener", "聊天室绑定接收消息监听器失败");
            return false;
        }
    }

}
