package com.app.myteammanager.ui.myteam.teamdatafragments.Game;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.myteammanager.R;
import com.app.myteammanager.recycleview.BaseModel;
import com.app.myteammanager.recycleview.BaseViewHolder;
import com.app.myteammanager.recycleview.CommonAdapter;
import com.app.myteammanager.ui.myteam.teamdatafragments.Game.NewRecord.ChoosePlayerActivity;
import com.app.myteammanager.ui.myteam.teamdatafragments.Game.OldRecord.OldRecordActivity;
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

public class GameFragment extends Fragment {


    public final static String TAG = "GameFragment";
    private CommonAdapter commonAdapter;
    private ArrayList<BaseModel> dataList;
    private TeamModel teamModel;
    private FloatingActionButton fab;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.myteam_teamboard_fragment_game,container,false);
        teamModel = (TeamModel) getArguments().get("Team");
        System.out.println("隊伍ID=" + teamModel.getGroupId());
        System.out.println("比賽清單頁啟動");
        init(fragmentView);

        dataList = new ArrayList<>();

        teamModel = (TeamModel) getArguments().get("Team");

        System.out.println("隊伍ID=" + teamModel.getGroupId());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView recyclerView = fragmentView.findViewById(R.id.rv_gameList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.smoothScrollToPosition(0);

        commonAdapter = new CommonAdapter(
                Arrays.asList(new SimpleGameRecordViewHolder.Factory(new BaseViewHolder.ClickFuncBuilder()
                                .add(R.id.lin_lay_simpleGameRecord, new BaseViewHolder.ClickHandler() {
                                    @Override
                                    public void onClick(int position) {
//                                        Toast.makeText(getActivity(),"點擊"+position,Toast.LENGTH_SHORT).show();
                                        int competitionId  = ((SimpleGameRecordModel)dataList.get(position)).getCompetitionId();
                                        jumpToOldRecordActivity((SimpleGameRecordModel)dataList.get(position),teamModel,competitionId);
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
        init(fragmentView);

        getGameListFromCloud();
        commonAdapter.notifyDataSetChanged();
        return fragmentView;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        System.out.println("RESUME!!!!!!!!!!!!");
//        getGameListFromCloud();
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == getActivity().RESULT_OK) {
//            System.out.println("重新抓取比賽紀錄頁面!!!!!!!!!!!!");
//            getGameListFromCloud();
//        }
//    }

    private void init(View fragmentView) {

        fab = fragmentView.findViewById(R.id.fab_add_game);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToAddGame(teamModel);
            }
        });
    }

    private void getGameListFromCloud() {

        dataList.clear();
        int groupId = teamModel.getGroupId();
        NetworkController.getInstance().get(Global.API_SHOW_GAMELIST+"/", String.valueOf(groupId)
                , new NetworkController.CCallback() {
                    @Override
                    public void onFailure(String errorMsg) {
                        System.out.println("抓取比賽失敗"+errorMsg);
                    }

                    @Override
                    public void onResponse(JSONObject data) throws JSONException {

                        JSONArray groupDetail = data.getJSONArray("cRecordList");
                        int dataLength = groupDetail.length();

                        System.out.println("總務檔案長度=" + dataLength);
                        //印出資料
                        for (int i = 0; i < dataLength; i++) {
                            System.out.println("i=" + i);
                            Logger.d(TAG, "抓取總務資料:" + groupDetail.getJSONObject(i));
                        }
                        for (int i = 0; i< dataLength-1 ; i++) {
                            JSONObject jsonObject = groupDetail.getJSONObject(i);
                            int competitionId = jsonObject.getInt("competitionId");
                            int ourScore = jsonObject.getInt("ourScore");
                            int opponentScore = jsonObject.getInt("opponentScore");
                            String competitionDate = jsonObject.getString("competitionDate");
                            String competitionName = jsonObject.getString("competitionName");
                            String competitionLocation = jsonObject.getString("competitionLocation");
                            String opponentName = jsonObject.getString("opponentName");
                            String selfName = teamModel.getGroupName();

                            dataList.add(new SimpleGameRecordModel(competitionId, ourScore,
                             opponentScore, selfName, competitionDate,
                                    competitionName, competitionLocation, opponentName));

                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    commonAdapter.notifyDataSetChanged();
                                }
                            });

                        }

                    }

                    @Override
                    public void onCompleted() {

                    }
                });


    }

    private void jumpToAddGame(TeamModel teamModel) {
        Intent intent = new Intent(getActivity(), ChoosePlayerActivity.class);
        //製作附加資料
        Bundle bundle = new Bundle();
        bundle.putParcelable("Team", teamModel);
        //附加到intent上
        intent.putExtras(bundle);
        startActivity(intent);

    }

    private void jumpToOldRecordActivity(SimpleGameRecordModel sgrm, TeamModel teamModel,int competitionId) {
        Intent intent = new Intent(getActivity(), OldRecordActivity.class);
        //製作附加資料
        Bundle bundle = new Bundle();
        bundle.putParcelable("Team", teamModel);
        bundle.putParcelable("SimpleGameRecordModel", sgrm);
        bundle.putInt("competitionId", competitionId);
        //附加到intent上
        intent.putExtras(bundle);
        startActivityForResult(intent, 2);

    }
}
