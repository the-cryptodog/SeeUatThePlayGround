package com.app.myteammanager.ui.notifications.Manager;

import com.app.myteammanager.recycleview.BaseModel;


public class ManagerNotificationModel implements BaseModel {

    public static final String TYPE = "ManagerNotificationModel";
    private int notifyId ;
    private int requestId ;
    private String requestName;
    private String groupName ;

    public int getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(int notifyId) {
        this.notifyId = notifyId;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getRequestName() {
        return requestName;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public ManagerNotificationModel(int notifyId, int requestId, String requestName, String groupName){
        this.notifyId = notifyId;
        this.requestId=requestId;
        this.requestName = requestName;
        this.groupName = groupName;

    }

    @Override
    public String getType() {
        return TYPE;
    }
}
