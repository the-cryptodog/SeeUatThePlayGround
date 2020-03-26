package com.app.myteammanager.ui.myteam.search;

import com.app.myteammanager.recycleview.BaseModel;

public class SearchResultModel implements BaseModel {

    public static final String TYPE = "SearchResultModel";

    String groupId;
    String groupName;
    String imageURL;
    String status;
    String groupIntro;

    public SearchResultModel(String groupId, String groupName , String imageURL , String groupIntro, String status){
        this.groupId = groupId;
        this.groupName= groupName;
        this.imageURL=imageURL;
        this.groupIntro=groupIntro;
        this.status = status;

    }
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGroupIntro() {
        return groupIntro;
    }

    public void setGroupIntro(String groupIntro) {
        this.groupIntro = groupIntro;
    }




    @Override
    public String getType() {
        return TYPE;
    }
}
