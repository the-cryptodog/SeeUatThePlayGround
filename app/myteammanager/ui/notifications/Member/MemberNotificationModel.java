package com.app.myteammanager.ui.notifications.Member;

import com.app.myteammanager.recycleview.BaseModel;


public class MemberNotificationModel implements BaseModel {

    public static final String TYPE = "ManagerNotificationModel";
    private int notifyId ;
    private String groupName;
    private String acceptedOrNot ;


    public int getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(int notifyId) {
        this.notifyId = notifyId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getAcceptedOrNot() {
        return acceptedOrNot;
    }

    public void setAcceptedOrNot(String acceptedOrNot) {
        this.acceptedOrNot = acceptedOrNot;
    }

    public MemberNotificationModel(int notifyId, String groupName,String acceptedOrNot){
        this.notifyId = notifyId;
        this.groupName = groupName;
        this.acceptedOrNot = acceptedOrNot;

    }

    @Override
    public String getType() {
        return TYPE;
    }
}
