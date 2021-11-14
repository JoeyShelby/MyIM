package com.joey.myim.service;

import android.util.Log;

import com.joey.myim.adapters.FriendsAdapter;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntries;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterGroup;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.util.JidUtil;
import org.jxmpp.stringprep.XmppStringprepException;

import java.util.Collection;

/**
 * 好友管理工具类，获取所有好友，添加好友， 删除好友，改变好友分组，更新分组名称，添加分组
 */
public class FriendsManage{
    static Roster roster = Roster.getInstanceFor(Connect.getConnect());

    //设置订阅处理模式
    public static void setSubscriptionMode(){
        roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);
    }

    /**
     * 获取所有花名册
     */
    public static void getAllFriends() {
        if (!roster.isLoaded()) {
            try {
                roster.reloadAndWait();
            } catch (SmackException.NotLoggedInException |
                    SmackException.NotConnectedException |
                    InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 这里获取好友后会回调到 rosterEntires 方法
        roster.getEntriesAndAddListener(new MyRosterListener(), new MyRosterEntries());
    }

    /**
     *  删除好友
     */
    public static boolean deleteFriend(String username) {
        try {
            RosterEntry entry = roster.getEntry(JidCreate.bareFrom(username));
            if (entry != null) {
                roster.removeEntry(entry);
                return true;
            }
        } catch (SmackException.NotLoggedInException | SmackException.NoResponseException | XMPPException.XMPPErrorException | SmackException.NotConnectedException | InterruptedException | XmppStringprepException e) {
                e.printStackTrace();
                return false;
        }
        return false;
    }

    /**
     *添加好友
     */
    public static boolean addFriend(String user, String name, String[] groupName) {
        if (Connect.getConnect().isAuthenticated()) {
            try {
                roster.createEntry(JidCreate.bareFrom(user), name, groupName);
                Log.d("addFriend", "添加好友成功");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("addFriend", "添加好友失败");
                return false;

            }
        }
        return false;
    }


    /**
     * 改变好友分组
     */
    public static void updateFriendsByGroup(String user, String curGroup, String mvGroup) {
        try {
            RosterGroup cGroup = roster.getGroup(curGroup);
            RosterGroup mGroup = roster.getGroup(mvGroup);
            RosterEntry entry = null;
            if (cGroup != null) {
                entry = cGroup.getEntry(JidCreate.bareFrom(user));
                if (entry != null) {
                    try {
                        if (mGroup == null) {
                            // 不存在该分组 即创建
                            mGroup = roster.createGroup(mvGroup);
                        }
                        cGroup.removeEntry(entry);
                        mGroup.addEntry(entry);
                    } catch (SmackException.NoResponseException | SmackException.NotConnectedException | XMPPException.XMPPErrorException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新分组名称
     * @param groupName
     * @param modifyName
     */
    public static void setGroupName(String groupName, String modifyName) {
        RosterGroup group = roster.getGroup(groupName);
        if (group != null) {
            try {
                group.setName(modifyName);
            } catch (SmackException.NotConnectedException | SmackException.NoResponseException | XMPPException.XMPPErrorException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 添加分组
     * @param groupName
     */
    public static void addGroup(String groupName) {
        roster.createGroup(groupName);
    }


    static class MyRosterListener implements RosterListener {
        @Override
        public void entriesAdded(Collection<Jid> addresses) {

        }

        @Override
        public void entriesUpdated(Collection<Jid> addresses) {

        }

        @Override
        public void entriesDeleted(Collection<Jid> addresses) {

        }

        @Override
        public void presenceChanged(Presence presence) {

        }
    }

    static class MyRosterEntries implements RosterEntries {
        @Override
        public void rosterEntries(Collection<RosterEntry> rosterEntries) {
            FriendsAdapter.rosterEntries.clear();
            for (RosterEntry entry : rosterEntries) {
                FriendsAdapter.rosterEntries.add(entry);
                Log.d("entry", entry.getJid().toString());
            }
        }
    }
}
