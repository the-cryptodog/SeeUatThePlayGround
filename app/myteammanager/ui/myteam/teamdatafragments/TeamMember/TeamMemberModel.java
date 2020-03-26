package com.app.myteammanager.ui.myteam.teamdatafragments.TeamMember;



import android.os.Parcel;
import android.os.Parcelable;

import com.app.myteammanager.recycleview.BaseModel;

public class TeamMemberModel implements BaseModel ,Parcelable {

    public static final String TYPE = "TeamMemberModel";


    int userId;
    int uniformNumber;
    String tmpGameNumber;



    String tmpGameNumberString;

    String userName;
    String role;
    String userPhoto;

    Boolean isChosen;
    Boolean readyForGame ;
    Boolean readyForChosen ;
    Boolean isStartingLineUp ;


    public TeamMemberModel(int userId, int uniformNumber, String userName, String role){
        this.userId = userId;
        this.uniformNumber=uniformNumber;
        this.tmpGameNumber = uniformNumber+"";
        this.userName=userName;
        this.role =role;
        this.isChosen =false;
        this.readyForChosen = false;
        this.readyForGame = false;
        this.isStartingLineUp=false;
    }


    protected TeamMemberModel(Parcel in) {
        userId = in.readInt();
        uniformNumber = in.readInt();
        tmpGameNumber = in.readString();
        userName = in.readString();
        role = in.readString();
        userPhoto = in.readString();
        byte tmpIsChosen = in.readByte();
        isChosen = tmpIsChosen == 0 ? null : tmpIsChosen == 1;
        byte tmpReadyForGame = in.readByte();
        readyForGame = tmpReadyForGame == 0 ? null : tmpReadyForGame == 1;
        byte tmpReadyForChosen = in.readByte();
        readyForChosen = tmpReadyForChosen == 0 ? null : tmpReadyForChosen == 1;
        byte tmpIsStartingLineUp = in.readByte();
        isStartingLineUp = tmpIsStartingLineUp == 0 ? null : tmpIsStartingLineUp == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeInt(uniformNumber);
        dest.writeString(tmpGameNumber);
        dest.writeString(userName);
        dest.writeString(role);
        dest.writeString(userPhoto);
        dest.writeByte((byte) (isChosen == null ? 0 : isChosen ? 1 : 2));
        dest.writeByte((byte) (readyForGame == null ? 0 : readyForGame ? 1 : 2));
        dest.writeByte((byte) (readyForChosen == null ? 0 : readyForChosen ? 1 : 2));
        dest.writeByte((byte) (isStartingLineUp == null ? 0 : isStartingLineUp ? 1 : 2));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TeamMemberModel> CREATOR = new Creator<TeamMemberModel>() {
        @Override
        public TeamMemberModel createFromParcel(Parcel in) {
            return new TeamMemberModel(in);
        }

        @Override
        public TeamMemberModel[] newArray(int size) {
            return new TeamMemberModel[size];
        }
    };

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getTmpGameNumber() {
        return tmpGameNumber;
    }

    public void setTmpGameNumber(String tmpGameNumber) {
        this.tmpGameNumber = tmpGameNumber;
    }

    public Boolean getReadyForChosen() {
        return readyForChosen;
    }

    public void setReadyForChosen(Boolean readyForChosen) {
        this.readyForChosen = readyForChosen;
    }

    public Boolean getIsChosen() {
        return isChosen;
    }

    public void setIsChosen(Boolean choosen) {
        isChosen = choosen;
    }

    public Boolean getReadyForGame() {
        return readyForGame;
    }

    public void setReadyForGame(Boolean readyForGame) {
        this.readyForGame = readyForGame;
    }

    public Boolean getStartingLineUp() {
        return isStartingLineUp;
    }

    public void setStartingLineUp(Boolean startingLineUp) {
        isStartingLineUp = startingLineUp;
    }


    @Override
    public String getType() {
        return TYPE;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUniformNumber() {
        return uniformNumber;
    }

    public void setUniformNumber(int uniformNumber) {
        this.uniformNumber = uniformNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }



}
