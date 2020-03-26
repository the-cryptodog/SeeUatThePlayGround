package com.app.myteammanager.ui.myteam.teamdatafragments.Game.NewRecord;

public class GameRecord {
    int GroupId;
    int totalScore;
    String competitionName;
    String competitionDate;
    String competitionDescribe;
    String opponent;

    public String getCompetitionLocation() {
        return competitionLocation;
    }

    public void setCompetitionLocation(String competitionLocation) {
        this.competitionLocation = competitionLocation;
    }

    String competitionLocation;
    int selfQuarterScore[];
    int rivalQuarterScore[];
    int selfTotalScore;
    int rivalTotalScore;
    int gameResult;

    public int getGameResult() {
        return (selfTotalScore>rivalTotalScore)?1:0;
    }

    public void setGameResult(int gameResult) {
        this.gameResult = gameResult;
    }

    public int getSelfTotalScore() {
        return selfQuarterScore[0]+selfQuarterScore[1]+selfQuarterScore[2]+selfQuarterScore[3];
    }

    public void setSelfTotalScore(int selfTotalScore) {
        this.selfTotalScore = selfTotalScore;
    }

    public int getRivalTotalScore() {
        return rivalQuarterScore[0]+rivalQuarterScore[1]+rivalQuarterScore[2]+rivalQuarterScore[3];
    }

    public void setRivalTotalScore(int rivalTotalScore) {
        this.rivalTotalScore = rivalTotalScore;
    }

    public GameRecord(){

    }

    public int getGroupId() {
        return GroupId;
    }

    public void setGroupId(int groupId) {
        GroupId = groupId;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public String getCompetitionName() {
        return competitionName;
    }

    public void setCompetitionName(String competitionName) {
        this.competitionName = competitionName;
    }

    public String getCompetitionDate() {
        return competitionDate;
    }

    public void setCompetitionDate(String competitionDate) {
        this.competitionDate = competitionDate;
    }

    public String getCompetitionDescribe() {
        return competitionDescribe;
    }

    public void setCompetitionDescribe(String competitionDescribe) {
        this.competitionDescribe = competitionDescribe;
    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public int[] getSelfQuarterScore() {
        return selfQuarterScore;
    }

    public void setSelfQuarterScore(int[] selfQuarterScore) {
        this.selfQuarterScore = selfQuarterScore;
    }

    public int[] getRivalQuarterScore() {
        return rivalQuarterScore;
    }

    public void setRivalQuarterScore(int[] rivalQuarterScore) {
        this.rivalQuarterScore = rivalQuarterScore;
    }


}
