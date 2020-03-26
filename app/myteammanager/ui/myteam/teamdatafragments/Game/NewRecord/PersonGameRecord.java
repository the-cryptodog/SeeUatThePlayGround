package com.app.myteammanager.ui.myteam.teamdatafragments.Game.NewRecord;

import android.os.Parcel;
import android.os.Parcelable;

import com.app.myteammanager.recycleview.BaseModel;


public class PersonGameRecord implements BaseModel, Parcelable {

    public static final String TYPE = "PersonGameRecord";

    protected PersonGameRecord(Parcel in) {
        score = in.readInt();
        or = in.readInt();
        dr = in.readInt();
        ast = in.readInt();
        blk = in.readInt();
        stl = in.readInt();
        pf = in.readInt();
        fga = in.readInt();
        fgm = in.readInt();
        thrpa = in.readInt();
        thrpm = in.readInt();
        fta = in.readInt();
        ftm = in.readInt();
        to = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(score);
        dest.writeInt(or);
        dest.writeInt(dr);
        dest.writeInt(ast);
        dest.writeInt(blk);
        dest.writeInt(stl);
        dest.writeInt(pf);
        dest.writeInt(fga);
        dest.writeInt(fgm);
        dest.writeInt(thrpa);
        dest.writeInt(thrpm);
        dest.writeInt(fta);
        dest.writeInt(ftm);
        dest.writeInt(to);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PersonGameRecord> CREATOR = new Creator<PersonGameRecord>() {
        @Override
        public PersonGameRecord createFromParcel(Parcel in) {
            return new PersonGameRecord(in);
        }

        @Override
        public PersonGameRecord[] newArray(int size) {
            return new PersonGameRecord[size];
        }
    };

    public int getScore() {
        return fga * 2 + fta * 1 + thrpa * 3;
    }
    public int getSetScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    private boolean isStart;
    private String userName;
    private int score;
    private int or;
    private int dr;
    private int ast;
    private int blk;
    private int stl;
    private int pf;
    private int fga;
    private int fgm;
    private int thrpa;
    private int thrpm;
    private int fta;
    private int ftm;
    private int to;

    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getOr() {
        return or;
    }

    public void setOr(int or) {
        this.or = or;
    }

    public int getDr() {
        return dr;
    }

    public void setDr(int dr) {
        this.dr = dr;
    }

    public int getAst() {
        return ast;
    }

    public void setAst(int ast) {
        this.ast = ast;
    }

    public int getBlk() {
        return blk;
    }

    public void setBlk(int blk) {
        this.blk = blk;
    }

    public int getStl() {
        return stl;
    }

    public void setStl(int stl) {
        this.stl = stl;
    }

    public int getPf() {
        return pf;
    }

    public void setPf(int pf) {
        this.pf = pf;
    }

    public int getFga() {
        return fga;
    }

    public void setFga(int fga) {
        this.fga = fga;
    }

    public int getFgm() {
        return fgm;
    }

    public void setFgm(int fgm) {
        this.fgm = fgm;
    }

    public int getThrpa() {
        return thrpa;
    }

    public void setThrpa(int thrpa) {
        this.thrpa = thrpa;
    }

    public int getThrpm() {
        return thrpm;
    }

    public void setThrpm(int thrpm) {
        this.thrpm = thrpm;
    }

    public int getFta() {
        return fta;
    }

    public void setFta(int fta) {
        this.fta = fta;
    }

    public int getFtm() {
        return ftm;
    }

    public void setFtm(int ftm) {
        this.ftm = ftm;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }


    public PersonGameRecord() {
        this.or = 0;
        this.dr = 0;
        this.ast = 0;
        this.blk = 0;
        this.stl = 0;
        this.pf = 0;
        this.fga = 0;
        this.fgm = 0;
        this.thrpa = 0;
        this.thrpm = 0;
        this.fta = 0;
        this.ftm = 0;
        this.to = 0;
    }

    public PersonGameRecord(String userName, boolean isStart
            , int score
            , int or
            , int dr
            , int ast
            , int blk
            , int stl
            , int pf
            , int fga
            , int fgm
            , int thrpa
            , int thrpm
            , int fta
            , int ftm
            , int to) {
        this.userName=userName;
        this.isStart=isStart;
        this.score=score;
        this.or = or;
        this.dr = dr;
        this.ast = ast;
        this.blk = blk;
        this.stl = stl;
        this.pf = pf;
        this.fga = fga;
        this.fgm = fgm;
        this.thrpa = thrpa;
        this.thrpm = thrpm;
        this.fta = fta;
        this.ftm = ftm;
        this.to = to;
    }


    @Override
    public String getType() {
        return TYPE;
    }
}
