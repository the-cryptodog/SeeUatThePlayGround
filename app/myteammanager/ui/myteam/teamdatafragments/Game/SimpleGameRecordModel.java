package com.app.myteammanager.ui.myteam.teamdatafragments.Game;

import android.os.Parcel;
import android.os.Parcelable;

import com.app.myteammanager.recycleview.BaseModel;

public class SimpleGameRecordModel implements BaseModel, Parcelable {

    public static final String TYPE = "SimpleGameRecordModel";

    int competitionId;
    int ourScore;
    int opponentScore;
    String selfName;
    String competitionDate;
    String competitionName;
    String competitionLocation;
    String opponentName;

    protected SimpleGameRecordModel(Parcel in) {
        competitionId = in.readInt();
        ourScore = in.readInt();
        opponentScore = in.readInt();
        selfName = in.readString();
        competitionDate = in.readString();
        competitionName = in.readString();
        competitionLocation = in.readString();
        opponentName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(competitionId);
        dest.writeInt(ourScore);
        dest.writeInt(opponentScore);
        dest.writeString(selfName);
        dest.writeString(competitionDate);
        dest.writeString(competitionName);
        dest.writeString(competitionLocation);
        dest.writeString(opponentName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SimpleGameRecordModel> CREATOR = new Creator<SimpleGameRecordModel>() {
        @Override
        public SimpleGameRecordModel createFromParcel(Parcel in) {
            return new SimpleGameRecordModel(in);
        }

        @Override
        public SimpleGameRecordModel[] newArray(int size) {
            return new SimpleGameRecordModel[size];
        }
    };

    public String getSelfName() {
        return selfName;
    }

    public void setSelfName(String selfName) {
        this.selfName = selfName;
    }

    public SimpleGameRecordModel(int competitionId, int ourScore,
                                 int opponentScore, String selfName, String competitionDate,
                                 String competitionName, String competitionLocation, String opponentName) {
            this.competitionId = competitionId;
            this.ourScore = ourScore;
            this.opponentScore = opponentScore;
            this.selfName = selfName;
            this.competitionDate = competitionDate;
            this.competitionName = competitionName;
            this.competitionLocation = competitionLocation;
            this.opponentName = opponentName;
        }



        public int getCompetitionId() {
            return competitionId;
        }

        public void setCompetitionId(int competitionId) {
            this.competitionId = competitionId;
        }

        public int getOurScore() {
            return ourScore;
        }

        public void setOurScore(int ourScore) {
            this.ourScore = ourScore;
        }

        public int getOpponentScore() {
            return opponentScore;
        }

        public void setOpponentScore(int opponentScore) {
            this.opponentScore = opponentScore;
        }

        public String getCompetitionDate() {
            return competitionDate;
        }

        public void setCompetitionDate(String competitionDate) {
            this.competitionDate = competitionDate;
    }

    public String getCompetitionName() {
        return competitionName;
    }

    public void setCompetitionName(String competitionName) {
        this.competitionName = competitionName;
    }

    public String getCompetitionLocation() {
        return competitionLocation;
    }

    public void setCompetitionLocation(String competitionLocation) {
        this.competitionLocation = competitionLocation;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }


    @Override
    public String getType() {
        return TYPE;
    }
}
