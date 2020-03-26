package com.app.myteammanager.ui.myteam.teamdatafragments.TeamMember;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.app.myteammanager.recycleview.BaseModel;

import java.io.File;
import java.io.IOException;

public class MemberToShowModel extends TeamMemberModel implements BaseModel {

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    File file;

    public static final String TYPE = "MemberToShowModel";

    //    int userId;
//    int uniformNumber;
//    String userName;
//    String role;
//    String userPhoto;
//
    public MemberToShowModel(int userId, int uniformNumber, String userName, String role) {
        super(userId, uniformNumber, userName, role);
    }
    @Override
    public String getType(){
        return TYPE;
    }



}
