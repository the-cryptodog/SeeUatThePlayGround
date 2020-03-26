package com.app.myteammanager.utils;

import com.app.myteammanager.ui.login.data.model.LoggedInUser;

import java.util.ArrayList;
import java.util.LinkedList;

public class Global {

    public static final boolean DEBUG =true;
    public static int NUMBER_SWITCH = 0 ;
    public static int checkCount = 0 ;
    public static int checkStartCount = 0 ;

    public static String CURRENTUSER_TOKEN = null;
    public static int CURRENTUSER_ID = 0;
    public static String CURRENT_GROUP_ID = null;

    public static final String API_LOGIN = "http://34.80.133.188/api/Login";
    public static final String API_REGISTER = "http://34.80.133.188/api/Register";


    public static final String API_GET_PROFILE = "http://34.80.133.188/api/ShowPersonal";



    public static final String API_SHOW_GROUP = "http://34.80.133.188/api/Group/all_i_have";
    public static final String API_ADD_GROUP = "http://34.80.133.188/api/Group/new_build_image";
    public static final String API_SEARCH_CLOUD_GROUP = "http://34.80.133.188/api/Member/search_group";
    public static final String API_APPLY_GROUP = "http://34.80.133.188/api/Member/send_application";
    public static final String API_GETINVITEDCODE = "http://34.80.133.188/api/Group/invited_code";

    public static final String API_NOTIFICATION_MANAGER = "http://34.80.133.188/api/Manager/email";
    public static final String API_NOTIFICATION_MEMBER = "http://34.80.133.188/api/User/email";

    public static final String API_CHECK_NOTIFICATION_MANAGER = "http://34.80.133.188/api/Manager/check_email";
    public static final String API_CHECK_NOTIFICATION_MEMBER = "http://34.80.133.188/api/User/check_email";

    //隊伍成員頁面 http://34.80.133.188/api/Member/{groupId}
    public static final String API_GET_MEMBER = "http://34.80.133.188/api/Member/";
    public static final String API_SET_MANAGER ="http://34.80.133.188/api/Member/";
    public static final String API_DELETE_MEMBER = "http://34.80.133.188/api/Member/";
    public static final String API_UPLOAD_PHOTO = "http://34.80.133.188/api/Member/api/update_user_photo";

    //隊伍財務頁面
    public static final String API_GET_FINANCE_LIST ="http://34.80.133.188/api/Group/wallet/";
    public static final String API_DELETE_FINANCE_RECORD ="http://34.80.133.188/api/Group/wallet/";
    public static final String API_EDIT_FINANCE_RECORD ="http://34.80.133.188/api/Group/wallet/";

    public static final String API_UPLOAD_GAME_RECORD ="http://34.80.133.188/api/CompetitionRecord";
    public static final String API_SHOW_GAMELIST ="http://34.80.133.188/api/ShowCompetitionRecord";
    public static final String API_SHOW_GAME_RECORD_DETAIL =" http://34.80.133.188/api/ShowCompetitionRecord";
    public static final String API_SHOW_GAME_QUARTER_SCORE = "http://34.80.133.188/api/ShowCompetitionRecord";


}
