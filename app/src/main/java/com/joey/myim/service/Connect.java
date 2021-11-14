package com.joey.myim.service;

import android.util.Log;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.parts.Localpart;

import java.io.IOException;
import java.net.InetAddress;

/**
 * 即时通讯相关工具类，获得连接，断开连接，用户注册，用户登录，修改密码，删除账户
 * 2021年11月6日13点37分：要注意一个点，很多操作，比如添加好友，删除账号，这些操作需要的connection都应该是已经登陆的connection
 */
public class Connect {
    public static final String HOST = "118.178.88.199";//服务器公网IP
    public static final int PORT = 5222;//端口
    public static final String DOMAIN = "i-bp11sex44qvkoiqip7w5";//服务器主机名
    public static final String SOURCE = "";//源，可忽略，传输文件时可能需要用到
    private static  XMPPTCPConnection connection = null;

    /**
     * 获得服务器端的openfire连接
     * @return connection：与openfire服务器的正常连接
     */
    public static XMPPTCPConnection getConnect(){
        if(connection == null){
            try {
                XMPPTCPConnectionConfiguration configuration = XMPPTCPConnectionConfiguration.builder()
                        .setHostAddress(InetAddress.getByName(HOST))//IP
                        .setPort(PORT)//端口
                        .setXmppDomain(DOMAIN)//服务器名称
                        .setSendPresence(false)//以离线方式登录,以便获取离线消息
                        .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                        .build();

                connection = new XMPPTCPConnection(configuration);

                connection.connect();
                Log.d("connect","Chat服务连接成功：[" + connection.getHost() + ":" + connection.getPort()+"]");
            } catch (Exception e) {
                Log.d("connect","Chat服务连接失败");
                e.printStackTrace();
            }
        }
        return connection;
    }

    /**
     * 断开连接，退出登录
     */
    public static void disConnect(){
        if(connection!=null){
            connection.disconnect();
            connection = null;
        }
    }

    /**
     * 新用户注册
     * @param username
     * @param password
     * @return true/false: 注册成功，注册失败
     */
    public static boolean register(String username, String password){
        try {
            AccountManager accountManager = AccountManager.getInstance(getConnect());
            accountManager.sensitiveOperationOverInsecureConnection(true);
            accountManager.createAccount(Localpart.fromOrThrowUnchecked(username),password);
            Log.d("register","["+username+"]注册成功");
            return true;
        }catch (XMPPException | SmackException | InterruptedException e) {
            Log.d("register","["+username+"]注册失败");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 用户通过账号密码登录
     * @param username
     * @param password
     * @return
     */
    public static boolean login(String username, String password){
        try {
            getConnect().login(username,password);
            Presence presence = new Presence(Presence.Type.available);
            getConnect().sendStanza(presence);

            Log.d("login","["+username+"]登录成功");
            return true;
        } catch (XMPPException | SmackException | IOException | InterruptedException e) {
            e.printStackTrace();
            disConnect();
            Log.d("login","["+username+"]登录失败");
            return false;
        }
    }

    /**
     * 修改密码
     * @param password
     * @return
     */
    public static boolean changePassword(String password){
        AccountManager accountManager = null;
        try {
            accountManager = AccountManager.getInstance(getConnect());
            accountManager.changePassword(password);
            Log.d("changePassword",connection.getUser().toString()+"改密码成功");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("changePassword",connection.getUser().toString()+"改密码失败");
            return false;
        }

    }

    /**
     * 删除账户
     * @return
     */
    public static boolean deleteAccount(){
        try {
            AccountManager.getInstance(getConnect()).deleteAccount();
            Log.d("deleteAccount",connection.getUser().toString()+"账户删除成功");

            return true;
        } catch (SmackException.NoResponseException | XMPPException.XMPPErrorException | SmackException.NotConnectedException | InterruptedException e) {
            e.printStackTrace();
            Log.d("deleteAccount",connection.getUser().toString()+"账户删除失败");

            return false;
        }
    }

}

