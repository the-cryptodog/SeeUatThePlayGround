package com.app.myteammanager.ui.myteam.teamdatafragments.Game.NewRecord;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.myteammanager.R;
import com.app.myteammanager.recycleview.BaseModel;
import com.app.myteammanager.recycleview.BaseViewHolder;
import com.app.myteammanager.recycleview.CommonAdapter;
import com.app.myteammanager.ui.myteam.teamdatafragments.Game.NewRecord.StartingLineUpActivity;
import com.app.myteammanager.ui.myteam.teamdatafragments.TeamMember.TeamMemberModel;
import com.app.myteammanager.ui.myteam.teamdatafragments.TeamMember.TeamMemberViewHolder;
import com.app.myteammanager.ui.myteam.teamlist.TeamModel;
import com.app.myteammanager.utils.Global;
import com.app.myteammanager.utils.Logger;
import com.app.myteammanager.utils.NetworkController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class ChoosePlayerActivity extends AppCompatActivity {

    public final static String TAG = "ChoosePlayerActivity";
    private CommonAdapter commonAdapter;
    private static ArrayList<BaseModel> dataList;
    private TeamModel teamModel;
    private Button nextStep;
    private ArrayList<TeamMemberModel> chosenList;
    private int[] checkedList;
    private int checkedCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_player);

        Bundle bundle = getIntent().getExtras();
        teamModel = bundle.getParcelable("Team");
        dataList = new ArrayList<>();

        System.out.println("onCreateView!!!!!!");
        System.out.println("隊伍ID=" + teamModel.getGroupId());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.rv_chosenMemberList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.smoothScrollToPosition(0);


        commonAdapter = new CommonAdapter(
                Arrays.asList(new TeamMemberViewHolder.Factory(new BaseViewHolder.ClickFuncBuilder()
                        .add(R.id.player_checkbox, new BaseViewHolder.ClickHandler() {
                            @Override
                            public void onClick(int position) {
                                TeamMemberModel tmm = ((TeamMemberModel)dataList.get(position));
                                if(tmm.getIsChosen()) {
                                    tmm.setIsChosen(false);
                                    System.out.println(tmm.getUserName()+"被取消選擇");
                                }else{
                                    tmm.setIsChosen(true);
                                    System.out.println(tmm.getUserName()+"被選擇");

                                }

                            }

                            @Override
                            public void reTurnData(int position, int integerData) {
                                TeamMemberModel tmm = ((TeamMemberModel)dataList.get(position));
                                if(tmm.getIsChosen()) {
                                   tmm.setTmpGameNumber(""+integerData);
                                }else{

                                }
                            }
                        }).add(R.id.tv_chooseplayer_number, new BaseViewHolder.ClickHandler() {
                            @Override
                            public void onClick(int position) {

                            }

                            @Override
                            public void reTurnData(int position, int integerData) {
                                ((TeamMemberModel)dataList.get(position)).setTmpGameNumber(integerData+"");
                            }
                        }
                ))));

        System.out.println("dataList =" + dataList.size());
        commonAdapter.bindDataSource(dataList);
        recyclerView.setAdapter(commonAdapter);

        getMemberListFromCloud();
        init();

    }



    private void init(){
        chosenList = new ArrayList<>();
        nextStep = findViewById(R.id.tv_chosen_nextStep);

        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Global.checkCount <=4){
                    Toast.makeText(ChoosePlayerActivity.this,"正規出賽起碼要五位喔",Toast.LENGTH_SHORT).show();
                    return;
                }
                for(int i= 0 ; i<dataList.size(); i ++){
                    if(((TeamMemberModel)dataList.get(i)).getIsChosen()){
                        chosenList.add((TeamMemberModel)(dataList.get(i)));
                    }
                }
                jumpToStartingLineUpActivity(chosenList);
            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == START_LINEUP && resultCode==RESULT_CANCELED) {
//            chosenList.clear();
//            System.out.println("選單頁面重新讀取成員資料");
//            getMemberListFromCloud();
//        }
//
//    }

    private void getMemberListFromCloud(){

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

                        System.out.println("可出賽球員列表長度="+listLength);
                        //印出資料
                        for (int i = 0; i < listLength; i++) {
                            System.out.println("i="+ i);
                            Logger.d(TAG, "抓取可出賽球員資料:" + memberList.getJSONObject(i));
                        }
                        for (int i = 0; i < listLength ; i++) {

                            JSONObject jsonObject = memberList.getJSONObject(i);
                            int userId = jsonObject.getInt("userId");
                            int uniformNumber = jsonObject.getInt("uniformNumber");
                            String userName = jsonObject.getString("userName");
                            String role = jsonObject.getString("role");
                            String userPhoto = jsonObject.getString("userPhoto");

                            dataList.add(new TeamMemberModel(userId
                                    , uniformNumber, userName, role));

                            ((TeamMemberModel) dataList.get(i)).setReadyForChosen(true);
                            ((TeamMemberModel) dataList.get(i)).setUserPhoto(userPhoto);


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

    private void jumpToStartingLineUpActivity(ArrayList<TeamMemberModel> gameMember) {

        Intent intent = new Intent(this, StartingLineUpActivity.class);
        //製作附加資料
        Bundle bundle = new Bundle();
        System.out.println("gameMember" + gameMember);
        bundle.putParcelableArrayList("ChosenList",gameMember);
        bundle.putParcelable("teamModel",teamModel);
        bundle.putInt("GroupId",teamModel.getGroupId());
        Global.checkCount = 0;
        //附加到intent上
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}
