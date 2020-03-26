package com.app.myteammanager.ui.myteam.teamdatafragments.Game.NewRecord;

import android.os.Parcel;
import android.os.Parcelable;

import com.app.myteammanager.recycleview.BaseModel;


public class InGamePlayersModel implements BaseModel, Parcelable {

    public static final String TYPE = "InGamePlayersModel";
    int userId;
    int uniformNumber;
    String tmpGameNumber;
    String userName;
    String userIntro;
    String userPhoto;
    Boolean isChosen;
    Boolean readyForGame ;
    Boolean readyForChosen ;
    Boolean isStartingLineUp ;
    Boolean isStarter ;
    PersonGameRecord pgr;

    protected InGamePlayersModel(Parcel in) {
        userId = in.readInt();
        uniformNumber = in.readInt();
        tmpGameNumber = in.readString();
        userName = in.readString();
        userIntro = in.readString();
        userPhoto = in.readString();
        byte tmpIsChosen = in.readByte();
        isChosen = tmpIsChosen == 0 ? null : tmpIsChosen == 1;
        byte tmpReadyForGame = in.readByte();
        readyForGame = tmpReadyForGame == 0 ? null : tmpReadyForGame == 1;
        byte tmpReadyForChosen = in.readByte();
        readyForChosen = tmpReadyForChosen == 0 ? null : tmpReadyForChosen == 1;
        byte tmpIsStartingLineUp = in.readByte();
        isStartingLineUp = tmpIsStartingLineUp == 0 ? null : tmpIsStartingLineUp == 1;
        byte tmpIsStarter = in.readByte();
        isStarter = tmpIsStarter == 0 ? null : tmpIsStarter == 1;
        pgr = in.readParcelable(PersonGameRecord.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeInt(uniformNumber);
        dest.writeString(tmpGameNumber);
        dest.writeString(userName);
        dest.writeString(userIntro);
        dest.writeString(userPhoto);
        dest.writeByte((byte) (isChosen == null ? 0 : isChosen ? 1 : 2));
        dest.writeByte((byte) (readyForGame == null ? 0 : readyForGame ? 1 : 2));
        dest.writeByte((byte) (readyForChosen == null ? 0 : readyForChosen ? 1 : 2));
        dest.writeByte((byte) (isStartingLineUp == null ? 0 : isStartingLineUp ? 1 : 2));
        dest.writeByte((byte) (isStarter == null ? 0 : isStarter ? 1 : 2));
        dest.writeParcelable(pgr, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<InGamePlayersModel> CREATOR = new Creator<InGamePlayersModel>() {
        @Override
        public InGamePlayersModel createFromParcel(Parcel in) {
            return new InGamePlayersModel(in);
        }

        @Override
        public InGamePlayersModel[] newArray(int size) {
            return new InGamePlayersModel[size];
        }
    };

    public Boolean getStarter() {
        return isStarter;
    }

    public void setStarter(Boolean starter) {
        isStarter = starter;
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

    public String getTmpGameNumber() {
        return tmpGameNumber;
    }

    public void setTmpGameNumber(String tmpGameNumber) {
        this.tmpGameNumber = tmpGameNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIntro() {
        return userIntro;
    }

    public void setUserIntro(String userIntro) {
        this.userIntro = userIntro;
    }

    public Boolean getChosen() {
        return isChosen;
    }

    public void setChosen(Boolean chosen) {
        isChosen = chosen;
    }

    public Boolean getReadyForGame() {
        return readyForGame;
    }

    public void setReadyForGame(Boolean readyForGame) {
        this.readyForGame = readyForGame;
    }

    public Boolean getReadyForChosen() {
        return readyForChosen;
    }

    public void setReadyForChosen(Boolean readyForChosen) {
        this.readyForChosen = readyForChosen;
    }

    public Boolean getStartingLineUp() {
        return isStartingLineUp;
    }

    public void setStartingLineUp(Boolean startingLineUp) {
        isStartingLineUp = startingLineUp;
    }

    public PersonGameRecord getPgr() {
        return pgr;
    }

    public void setPgr(PersonGameRecord pgr) {
        this.pgr = pgr;
    }

    public InGamePlayersModel(int userId, int uniformNumber, String tmpGameNumber, String userName, String userIntro) {
        this.userId = userId;
        this.uniformNumber = uniformNumber;
        this.tmpGameNumber = tmpGameNumber;
        this.userName = userName;
        this.userIntro = userIntro;
        this.pgr = new PersonGameRecord();
        this.isStarter = false;
    }

    @Override
    public String getType() {
        return TYPE;
    }




    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }


}
