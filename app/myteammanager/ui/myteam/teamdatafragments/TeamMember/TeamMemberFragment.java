package com.app.myteammanager.ui.myteam.teamdatafragments.TeamMember;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.myteammanager.AddTeamActivity;
import com.app.myteammanager.R;
import com.app.myteammanager.recycleview.BaseModel;
import com.app.myteammanager.recycleview.BaseViewHolder;
import com.app.myteammanager.recycleview.CommonAdapter;
import com.app.myteammanager.ui.myteam.teamdatafragments.Finance.FinanceRecordModel;
import com.app.myteammanager.ui.myteam.teamdatafragments.Game.NewRecord.InGamePlayersModel;
import com.app.myteammanager.ui.myteam.teamlist.TeamListViewHolder;
import com.app.myteammanager.ui.myteam.teamlist.TeamModel;
import com.app.myteammanager.utils.Global;
import com.app.myteammanager.utils.ImageUploadController;
import com.app.myteammanager.utils.Logger;
import com.app.myteammanager.utils.NetworkController;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;


public class TeamMemberFragment extends Fragment {

    public final static String TAG = "TeamMemberFragment";
    private CommonAdapter commonAdapter;
    private static ArrayList<BaseModel> dataList;
    private TeamModel teamModel;
    TextView groupName, groupIntro,  inviteCode;
    CircleImageView teamPhoto;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        dataList = new ArrayList<>();
        System.out.println("onCreateView!!!!!!");

        //---------------------------------這裡把view吹出來
        View fragmentView = inflater.inflate(R.layout.myteam_teamboard_fragment_member, container, false);
        //---------------------------------設定ToolBar

        teamModel = (TeamModel) getArguments().get("Team");

        System.out.println("隊伍ID=" + teamModel.getGroupId());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView recyclerView = fragmentView.findViewById(R.id.rv_teammemberlist);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.smoothScrollToPosition(0);


        commonAdapter = new CommonAdapter(

                Arrays.asList(new MemberToShowViewHolder.Factory(new BaseViewHolder.ClickFuncBuilder()
                        .add(R.id.btn_setManager, new BaseViewHolder.ClickHandler() {
                            @Override
                            public void onClick(int position) {
                                int userId = ((MemberToShowModel)dataList.get(position)).getUserId();
                               if(((MemberToShowModel)dataList.get(position)).getRole().equals("is manager")){
                                   setRole(true,userId);
                               }else{
                                   setRole(false,userId);
                               }

                            }
                            @Override
                            public void reTurnData(int position, int integerData) {

                            }
                        })

                        .add(R.id.btn_kickMember, new BaseViewHolder.ClickHandler() {
                            @Override
                            public void onClick(int position) {

                                int userId = ((MemberToShowModel)dataList.get(position)).getUserId();
                                confirmAlertDialog( userId);

//                                teamModel = ((TeamModel)dataList.get(position));
                            }

                            @Override
                            public void reTurnData(int position, int integerData) {

                            }
                        })
                )));

        System.out.println("dataList =" + dataList.size());
        commonAdapter.bindDataSource(dataList);
        recyclerView.setAdapter(commonAdapter);
        getMemberListFromCloud();
        init(fragmentView);
        return fragmentView;

    }

    private void init(View fragmentView){
        groupName = fragmentView.findViewById(R.id.tv_team_name);
        groupIntro= fragmentView.findViewById(R.id.tv_team_intro);
        teamPhoto= fragmentView.findViewById(R.id.team_iv_photo);
        inviteCode= fragmentView.findViewById(R.id.tv_invitedCode);

        Picasso.get().load(teamModel.getImageURL()).into(teamPhoto);
        groupName.setText(teamModel.getGroupName());
        groupIntro.setText(teamModel.getGroupIntro());
        inviteCode.setText(teamModel.getInviteCode());
    }

    private void getMemberListFromCloud() {

        dataList.clear();

        int groupId = teamModel.getGroupId();

        NetworkController.getInstance().get(Global.API_GET_MEMBER, String.valueOf(groupId)
                , new NetworkController.CCallback() {
                    @Override
                    public void onFailure(String errorMsg) {

                        System.out.println("抓取資料失敗 = "+ errorMsg);
                    }

                    @Override
                    public void onResponse(JSONObject data) throws JSONException {

                        JSONArray memberList = data.getJSONArray("memberList");

                        int listLength = memberList.length();

                        System.out.println("總務隊員檔案長度="+listLength);
                        //印出資料
                        for (int i = 0; i < listLength; i++) {
                            System.out.println("i="+ i);
                            Logger.d(TAG, "抓取隊員資料:" + memberList.getJSONObject(i));
                        }
                        for (int i = 0; i < listLength ; i++) {
                            JSONObject jsonObject = memberList.getJSONObject(i);
                            int userId = jsonObject.getInt("userId");
                            int uniformNumber = jsonObject.getInt("uniformNumber");
                            String userName = jsonObject.getString("userName");
                            String role = jsonObject.getString("role");
                            String photoUrl = jsonObject.getString("userPhoto");

                            dataList.add(new MemberToShowModel(userId
                                    , uniformNumber, userName, role));
                            ((MemberToShowModel)dataList.get(i)).setUserPhoto(photoUrl);

                        }

                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                commonAdapter.notifyDataSetChanged();
                            }
                        });
                    }

                    @Override
                    public void onCompleted() {

                    }
                });


    }

    private void setRole(boolean isManager, int userId){

        int groupId = teamModel.getGroupId();
        String customAPI = Global.API_SET_MANAGER+groupId+"/"+"change_"+userId;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", Global.CURRENTUSER_TOKEN)
                    .put("ChangeEvent",
                            (isManager)?"change to normal player":"change to manager");
        } catch (JSONException e) {
            e.printStackTrace();

        }

        NetworkController.getInstance().post(customAPI, jsonObject.toString()
                , new NetworkController.CCallback() {
                    @Override
                    public void onFailure(String errorMsg) {
                        if(errorMsg.equals("you are not manager")){
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(),"你不是管理員",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        System.out.println("抓取資料失敗 = "+ errorMsg);
                    }

                    @Override
                    public void onResponse(JSONObject data) throws JSONException {

                        getMemberListFromCloud();

                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                commonAdapter.notifyDataSetChanged();
                            }
                        });
                    }

                    @Override
                    public void onCompleted() {

                    }
                });

    }

    private void kickMember(int userId){

        int groupId = teamModel.getGroupId();
        String customAPI = Global.API_DELETE_MEMBER+groupId+"/"+"delete_"+userId;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", Global.CURRENTUSER_TOKEN);

        } catch (JSONException e) {
            e.printStackTrace();

        }
        System.out.println("API"+ customAPI);
        System.out.println("TOKEN"+ Global.CURRENTUSER_TOKEN);

        NetworkController.getInstance().post(customAPI, jsonObject.toString()
                , new NetworkController.CCallback() {
                    @Override
                    public void onFailure(String errorMsg) {
                        Toast.makeText(getActivity(),"你非管理員無刪除權限",Toast.LENGTH_SHORT).show();
                        System.out.println("抓取資料失敗 = "+ errorMsg);
                    }
                    @Override
                    public void onResponse(JSONObject data) throws JSONException {

                        getMemberListFromCloud();
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                commonAdapter.notifyDataSetChanged();
                            }
                        });
                    }

                    @Override
                    public void onCompleted() {

                    }
                });
    }


    private void confirmAlertDialog(final int  userId) {
        final AlertDialog.Builder altDlgBuilder = new AlertDialog.Builder(getActivity());
        altDlgBuilder.setTitle(R.string.Warning);
        altDlgBuilder.setMessage(R.string.Are_you_sure);
        altDlgBuilder.setIcon(R.drawable.ic_error_outline_black_24dp);
        altDlgBuilder.setCancelable(true);
        altDlgBuilder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                kickMember(userId);
                dialog.dismiss();

            }
        });
        altDlgBuilder.setNegativeButton("等等", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();


    }



}