package com.app.myteammanager.ui.myteam.teamlist;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import com.app.myteammanager.recycleview.BaseModel;


public class TeamModel implements Parcelable , BaseModel {

    public static final String TYPE = "TeamModel";


    int groupId;
    String groupName;
    String nextEvent;
    String groupIntro;
    String inviteCode;
    String imageURL;


    protected TeamModel(Parcel in) {
        groupId = in.readInt();
        groupName = in.readString();
        nextEvent = in.readString();
        groupIntro = in.readString();
        inviteCode = in.readString();
        imageURL = in.readString();

    }

    public static final Creator<TeamModel> CREATOR = new Creator<TeamModel>() {
        @Override
        public TeamModel createFromParcel(Parcel in) {
            return new TeamModel(in);
        }

        @Override
        public TeamModel[] newArray(int size) {
            return new TeamModel[size];
        }
    };

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public TeamModel(int groupId, String groupName,
                     String nextEvent, String groupIntro, String inviteCode, String imageURL){
        this.groupId = groupId;
        this.groupName = groupName;
        this.nextEvent = nextEvent;
        this.groupIntro = groupIntro;
        this.inviteCode = inviteCode;
        this.imageURL= imageURL;
    }


    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
    public String getInviteCode(){
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }


    public String getNextEvent() {
        return nextEvent;
    }

    public void setNextEvent(String nextEvent) {
        this.nextEvent = nextEvent;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(groupId);
        dest.writeString(groupName);
        dest.writeString(nextEvent);
        dest.writeString(groupIntro);
        dest.writeString(inviteCode);
        dest.writeString(imageURL);
    }
}
