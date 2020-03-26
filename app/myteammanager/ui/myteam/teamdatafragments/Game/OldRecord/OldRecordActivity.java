package com.app.myteammanager.ui.myteam.teamdatafragments.Game.OldRecord;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.app.myteammanager.R;
import com.app.myteammanager.recycleview.BaseModel;
import com.app.myteammanager.recycleview.BaseViewHolder;
import com.app.myteammanager.recycleview.CommonAdapter;
import com.app.myteammanager.ui.myteam.teamdatafragments.Game.NewRecord.PersonGameRecord;
import com.app.myteammanager.ui.myteam.teamdatafragments.Game.SimpleGameRecordModel;
import com.app.myteammanager.ui.myteam.teamdatafragments.Game.SimpleGameRecordViewHolder;
import com.app.myteammanager.ui.myteam.teamlist.TeamModel;
import com.app.myteammanager.utils.Global;
import com.app.myteammanager.utils.Logger;
import com.app.myteammanager.utils.NetworkController;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class OldRecordActivity extends AppCompatActivity {


    public final static String TAG = "OldRecordActivity";
    private CommonAdapter commonAdapter;
    private ArrayList<BaseModel> dataList;
    private TeamModel teamModel;
    private int competitionId;
    int[] quarterSelfScore;
    int[] quarterRivalScore;
    TextView q1s, q2s, q3s, q4s, q1r, q2r, q3r, q4r
            , competitionLocation, opponentName, competitionName
            , competitionDate, gameScore, gameResult, selfName;
    SimpleGameRecordModel sgrm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_record);
        Bundle bundle = getIntent().getExtras(); //攜帶隊伍資料
        teamModel = bundle.getParcelable("Team");
        competitionId = bundle.getInt("competitionId");
        sgrm = bundle.getParcelable("SimpleGameRecordModel");
        System.out.println("隊伍ID=" + teamModel.getGroupId());

        quarterSelfScore = new int[4];
        quarterRivalScore = new int[4];

        dataList = new ArrayList<>();
        dataList.add(new RecordItemModel());

        System.out.println("隊伍ID=" + teamModel.getGroupId());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.rv_detailrecord);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.smoothScrollToPosition(0);

        commonAdapter = new CommonAdapter(
                Arrays.asList(new RecordItemViewHolder.Factory(new BaseViewHolder.ClickFuncBuilder().add(R.id.lin_lay_oldRecord, new BaseViewHolder.ClickHandler() {
                            @Override
                            public void onClick(int position) {

                            }

                            @Override
                            public void reTurnData(int position, int integerData) {

                            }
                        })), new PersonalRecordViewHolder.Factory(new BaseViewHolder.ClickFuncBuilder()
                                .add(R.id.lin_lay_simpleGameRecord, new BaseViewHolder.ClickHandler() {
                                    @Override
                                    public void onClick(int position) {

                                    }

                                    @Override
                                    public void reTurnData(int position, int integerData) {

                                    }
                                })
                        )
                )
        );

        System.out.println("dataList =" + dataList.size());
        commonAdapter.bindDataSource(dataList);
        recyclerView.setAdapter(commonAdapter);
        init();setGamePanel();
        initQuarterScore();
        getGameDetailFromCloud();


    }
    private void init(){
        selfName=findViewById(R.id.tv_self_name);
        competitionLocation=findViewById(R.id.tv_game_location);
        competitionName= findViewById(R.id.tv_game_name);
        opponentName= findViewById(R.id.tv_rival_name);
        competitionDate= findViewById(R.id.tv_game_date);
        gameScore= findViewById(R.id.tv_game_score);
        gameResult= findViewById(R.id.tv_game_result);
    }

    private void getGameDetailFromCloud() {

        int groupId = teamModel.getGroupId();
        System.out.println("呼叫記錄細節API" + Global.API_SHOW_GAME_RECORD_DETAIL + "/" + groupId + "/"
                + competitionId + "/player");

        NetworkController.getInstance().get(Global.API_SHOW_GAME_RECORD_DETAIL + "/" + groupId + "/"
                , competitionId + "/player"
                , new NetworkController.CCallback() {
                    @Override
                    public void onFailure(String errorMsg) {
                        System.out.println("抓取比賽細節失敗" + errorMsg);
                    }

                    @Override
                    public void onResponse(JSONObject data) throws JSONException {

                        JSONArray recordDetail = data.getJSONArray("cRecordList");
                        int dataLength = recordDetail.length();

                        System.out.println("紀錄長度=" + dataLength);
                        //印出資料
                        for (int i = 0; i < dataLength; i++) {
                            System.out.println("i=" + i);
                            Logger.d(TAG, "紀錄資料:" + recordDetail.getJSONObject(i));
                        }

                        for (int i = 0; i < dataLength; i++) {
                            JSONObject jsonObject = recordDetail.getJSONObject(i);

                            boolean isStart = jsonObject.getBoolean("isStart");
                            String userName = jsonObject.getString("userName");
                            int personScore = jsonObject.getInt("personScore");
                            int orb = jsonObject.getInt("orb");
                            int drb = jsonObject.getInt("drb");
                            int assists = jsonObject.getInt("assists");
                            int steals = jsonObject.getInt("steals");
                            int blocks = jsonObject.getInt("blocks");
                            int fga = jsonObject.getInt("fga");
                            int fgm = jsonObject.getInt("fgm");
                            int fG3 = jsonObject.getInt("fG3");
                            int fgM3 = jsonObject.getInt("fgM3");
                            int fta = jsonObject.getInt("fta");
                            int ftm = jsonObject.getInt("ftm");
                            int turnovers = jsonObject.getInt("turnovers");
                            int pf = jsonObject.getInt("pf");

                            dataList.add(new PersonGameRecord(userName, isStart,
                                    personScore, orb, drb, assists, blocks, steals, pf, fga, fgm, fG3, fgM3, fta, ftm, turnovers));

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

        System.out.println("呼叫記錄細節API" + Global.API_SHOW_GAME_QUARTER_SCORE + "/" + groupId + "/"
                + competitionId + "/quarter");
        NetworkController.getInstance().get(Global.API_SHOW_GAME_QUARTER_SCORE + "/" + groupId + "/"
                , competitionId + "/quarter"
                , new NetworkController.CCallback() {
                    @Override
                    public void onFailure(String errorMsg) {
                        System.out.println("抓取節分數失敗" + errorMsg);
                    }

                    @Override
                    public void onResponse(JSONObject data) throws JSONException {

                        JSONArray recordDetail = data.getJSONArray("cRecordList");
                        int dataLength = recordDetail.length();

                        System.out.println("紀錄長度=" + dataLength);
                        //印出資料
                        for (int i = 0; i < dataLength; i++) {
                            System.out.println("i=" + i);
                            Logger.d(TAG, "紀錄資料:" + recordDetail.getJSONObject(i));
                        }


                        for (int i = 0; i < dataLength; i++) {
                            JSONObject jsonObject = recordDetail.getJSONObject(i);

                            int selfScore = jsonObject.getInt("quarterScore");
                            int rivalScore = jsonObject.getInt("opponentScore");

                            quarterSelfScore[i] = selfScore;
                            quarterRivalScore[i] = rivalScore;

                        }

                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                updateQuarterData();
                            }
                        });

                    }

                    @Override
                    public void onCompleted() {

                    }
                });


    }

    private void setGamePanel() {
        competitionLocation.setText(sgrm.getCompetitionLocation());
        competitionName.setText(sgrm.getCompetitionName());
        selfName.setText(sgrm.getSelfName());
        opponentName.setText(sgrm.getOpponentName());
        competitionDate.setText(sgrm.getCompetitionDate());
        String score = sgrm.getOurScore()+" : "+ sgrm.getOpponentScore();
        gameScore.setText(score);
        gameResult.setText(((sgrm.getOurScore()-sgrm.getOpponentScore())>0)?"勝":"敗");
    }

    private void initQuarterScore() {

        q1s = findViewById(R.id.o_tv_quarter_1_selfScore);
        q2s = findViewById(R.id.o_tv_quarter_2_selfScore);
        q3s = findViewById(R.id.o_tv_quarter_3_selfScore);
        q4s = findViewById(R.id.o_tv_quarter_4_selfScore);
        q1r = findViewById(R.id.o_tv_quarter_1_rivalScore);
        q2r = findViewById(R.id.o_tv_quarter_2_rivalScore);
        q3r = findViewById(R.id.o_tv_quarter_3_rivalScore);
        q4r = findViewById(R.id.o_tv_quarter_4_rivalScore);

    }

    private void updateQuarterData() {

        q1s.setText("" + quarterSelfScore[0]);
        q2s.setText("" + quarterSelfScore[1]);
        q3s.setText("" + quarterSelfScore[2]);
        q4s.setText("" + quarterSelfScore[3]);
        q1r.setText("" + quarterRivalScore[0]);
        q2r.setText("" + quarterRivalScore[1]);
        q3r.setText("" + quarterRivalScore[2]);
        q4r.setText("" + quarterRivalScore[3]);


    }

}
