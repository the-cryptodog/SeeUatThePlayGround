package com.app.myteammanager.ui.myteam.teamdatafragments.Game.NewRecord;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.myteammanager.R;
import com.app.myteammanager.recycleview.BaseModel;
import com.app.myteammanager.recycleview.BaseViewHolder;
import com.app.myteammanager.recycleview.CommonAdapter;
import com.app.myteammanager.ui.myteam.teamdatafragments.TeamMember.TeamMemberModel;
import com.app.myteammanager.ui.myteam.teamlist.TeamModel;
import com.app.myteammanager.utils.Global;

import java.util.ArrayList;
import java.util.Arrays;

public class StartingLineUpActivity extends AppCompatActivity {

    public final static String TAG = "StartingLineUpActivity";
    private CommonAdapter commonAdapter;
    private static ArrayList<BaseModel> inGamePlayersList;
    private static ArrayList<TeamMemberModel> tmpDataList;
    private Button nextStep;
    private int groupId;
    private TeamModel teamModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_line_up);


        inGamePlayersList = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        tmpDataList = bundle.getParcelableArrayList("ChosenList");
        teamModel = bundle.getParcelable("teamModel");
        groupId = bundle.getInt("GroupId");

        for(int i= 0 ; i <tmpDataList.size(); i++ ){
            inGamePlayersList.add(new InGamePlayersModel(
                    tmpDataList.get(i).getUserId(),
                    tmpDataList.get(i).getUniformNumber(),
                    tmpDataList.get(i).getTmpGameNumber(),
                    tmpDataList.get(i).getUserName(),
                    tmpDataList.get(i).getRole()));
            ((InGamePlayersModel)inGamePlayersList.get(i)).setUserPhoto(tmpDataList.get(i).getUserPhoto());
            System.out.println(((InGamePlayersModel)inGamePlayersList.get(i)).getUserPhoto());
        }

        setResult(RESULT_CANCELED);

//       System.out.println("!!!!!!!!"+((TeamMemberModel)dataList.get(0)).getReadyForChosen());


        for(int i = 0; i <inGamePlayersList.size(); i++) {
//            ((TeamMemberModel)dataList.get(i)).setReadyForChosen(true);
            ((InGamePlayersModel)inGamePlayersList.get(i)).setReadyForGame(true);
            ((InGamePlayersModel)inGamePlayersList.get(i)).setChosen(false);
        }

        System.out.println("StartingLineUpActivity!!!");

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.rv_startingList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.smoothScrollToPosition(0);



        commonAdapter = new CommonAdapter(
                Arrays.asList(new InGamePlayerViewHolder.Factory(new BaseViewHolder.ClickFuncBuilder()
                        .add(R.id.checkBox_startupline, new BaseViewHolder.ClickHandler() {
                            @Override
                            public void onClick(int position) {
                                InGamePlayersModel tmm = ((InGamePlayersModel)inGamePlayersList.get(position));
                                if(tmm.getChosen()) {
                                    tmm.setChosen(false);

                                }else{
                                    tmm.setChosen(true);

                                }
                            }

                            @Override
                            public void reTurnData(int position, int integerData) {

                            }
                        })
                )));

        System.out.println("dataList =" + inGamePlayersList.size());
        commonAdapter.bindDataSource(inGamePlayersList);
        recyclerView.setAdapter(commonAdapter);
        init();
    }

    private void init(){

        nextStep = findViewById(R.id.tv_starting_nextStep);
        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmAlertDialog( );

            }
        });

    }

    private void confirmAlertDialog( ) {
        final AlertDialog.Builder altDlgBuilder = new AlertDialog.Builder(this);
        altDlgBuilder.setTitle(R.string.Warning);
        altDlgBuilder.setMessage(R.string.Are_you_sure_to_start_game);
        altDlgBuilder.setIcon(R.drawable.ic_error_outline_black_24dp);
        altDlgBuilder.setCancelable(true);
        altDlgBuilder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for(int i= 0 ; i<inGamePlayersList.size(); i ++){
                    if(((InGamePlayersModel)inGamePlayersList.get(i)).getChosen()){ //如果沒有人被選，則不進行轉跳
                        ((InGamePlayersModel)inGamePlayersList.get(i)).setStartingLineUp(true);
                        ((InGamePlayersModel)inGamePlayersList.get(i)).setStarter(true);
                    }
                }
                jumpToGamePanelActivity(inGamePlayersList);
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


    private void jumpToGamePanelActivity(ArrayList<BaseModel> inGamePlayersList) {
        if(Global.checkCount <=4){
            Toast.makeText(StartingLineUpActivity.this,"先發要五位啦",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, GamePanelActivity.class);
        //製作附加資料
        Bundle bundle = new Bundle();
        for(int i = 0 ; i <inGamePlayersList.size() ; i++){
            InGamePlayersModel tmpData = ((InGamePlayersModel)inGamePlayersList.get(i));
            bundle.putParcelable("player"+i, tmpData);
        }
        bundle.putInt("totalPlayers",inGamePlayersList.size());
        bundle.putInt("GroupId",groupId);
        bundle.putParcelable("teamModel",teamModel);
        //附加到intent上
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}
