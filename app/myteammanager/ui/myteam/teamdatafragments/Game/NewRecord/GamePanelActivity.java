package com.app.myteammanager.ui.myteam.teamdatafragments.Game.NewRecord;

import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.app.myteammanager.AddTeamActivity;
import com.app.myteammanager.R;
import com.app.myteammanager.TeamActivity;
import com.app.myteammanager.ui.myteam.teamdatafragments.Game.OldRecord.OldRecordActivity;
import com.app.myteammanager.ui.myteam.teamdatafragments.Game.SimpleGameRecordModel;
import com.app.myteammanager.ui.myteam.teamlist.TeamModel;
import com.app.myteammanager.utils.CustomSpinner;
import com.app.myteammanager.utils.Global;
import com.app.myteammanager.utils.Logger;
import com.app.myteammanager.utils.NetworkController;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

import de.hdodenhof.circleimageview.CircleImageView;

public class GamePanelActivity extends AppCompatActivity {

    public final static String TAG = "GamePanelActivity";
    public final static int SPINNER_DEFUALT = 0;
    public final static int TOTAL_QUARTER = 4;
    public final static int FIVE_PLAYER = 3;

    private int groupId;
    private LinkedList<InGamePlayersModel> inGamePlayersList;
    private CircleImageView civ1, civ2, civ3, civ4, civ5;
    private TextView player_intro_1, player_intro_2, player_intro_3, player_intro_4, player_intro_5;
    private CustomSpinner spinner1, spinner2, spinner3, spinner4, spinner5;
    private LinkedList<InGamePlayersModel> playingList;
    private LinkedList<InGamePlayersModel> benchList;
    private ArrayList<String> spinnerSet;
    private ArrayAdapter<String> arrayAdapter;
    private InGamePlayersModel currentPlayer;
    private Boolean spinner1IsFirst, spinner2IsFirst, spinner3IsFirst, spinner4IsFirst, spinner5IsFirst;
    private ImageButton btn_add_rival, btn_minus_rival, btn_minus_self, btn_add_self;
    private TextView tv_currentQuarter, tv_quarter_1_selfScore, tv_quarter_2_selfScore, tv_quarter_3_selfScore, tv_quarter_4_selfScore,
            tv_quarter_1_rivalScore, tv_quarter_2_rivalScore, tv_quarter_3_rivalScore, tv_quarter_4_rivalScore,
            tv_selfTotalFault, tv_rivalTotalFault, tv_selfScore, tv_rivalScore,
            tv_OR, tv_DR, tv_AST, tv_BLK, tv_STL, tv_TO,
            tv_FGA, tv_3PA, tv_FGM, tv_3PM, tv_FTM, tv_FTA, tv_PF, tv_APF;
    private ImageButton a_OR, m_OR, a_DR, m_DR, a_AST, m_AST, a_BLK, m_BLK, a_STL, m_STL, a_TO, m_TO,
            a_FGA, m_FGA, a_FGM, m_FGM, a_3PA, m_3PA, a_3PM, m_3PM, a_FTM, m_FTM, a_FTA, m_FTA, a_PF, m_PF, a_APF, m_APF;
    private Button btn_nextQuarter;
    private int currentQuarter;
    private int quartersSelfScores[];
    private int quartersRivalScores[];
    private int quartersSelfTotalFualt[];
    private GameRecord gameRecord;
    private TeamModel teamModel;
    private int currentSpinnerSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_panel);



        gameRecord = new GameRecord();
        spinnerSet = new ArrayList<>();
        inGamePlayersList = new LinkedList<>();
        playingList = new LinkedList<>();
        benchList = new LinkedList<>();
        System.out.println("開啟GAMEPANEL");

        Bundle bundle = getIntent().getExtras();
        groupId = bundle.getInt("GroupId");
        teamModel = bundle.getParcelable("teamModel");
        //初始化板凳球員與先發球員列
        for (int i = 0; i < bundle.getInt("totalPlayers"); i++) {
            inGamePlayersList.add((InGamePlayersModel) bundle.getParcelable("player" + i));
            System.out.println("參賽球員-背號" + i + "-" + inGamePlayersList.get(i).getUserName());
            if (inGamePlayersList.get(i).getChosen()) { //在上一個頁面有被選取的腳色列為先發
                System.out.println("(先發)");
                System.out.println(inGamePlayersList.get(i).getStartingLineUp());
                playingList.add(inGamePlayersList.get(i));
            } else {
                benchList.add(inGamePlayersList.get(i));
                System.out.println("(板凳)");
            }
        }
        //初始化下拉選單板凳球員選列
        spinner1 = findViewById(R.id.starting_spining_1);
        for (int i = 0; i < benchList.size(); i++) {
            spinnerSet.add(benchList.get(i).getUserName());
        }
        arrayAdapter = new ArrayAdapter<>(GamePanelActivity.this,
                android.R.layout.simple_spinner_dropdown_item, spinnerSet);

        init();
        //初始化先發行列
        //初始化按鈕
        initSelectedListener();
        //初始化清單選擇事件
        playerSelected();//初始化目前球員事件
    }

    private void init() {

        quartersSelfTotalFualt = new int[4];

        spinner1IsFirst = true;
        spinner2IsFirst = true;
        spinner3IsFirst = true;
        spinner4IsFirst = true;
        spinner5IsFirst = true;

        civ1 = findViewById(R.id.starting_iv_photo_1);
        civ2 = findViewById(R.id.starting_iv_photo_2);
        civ3 = findViewById(R.id.starting_iv_photo_3);
        civ4 = findViewById(R.id.starting_iv_photo_4);
        civ5 = findViewById(R.id.starting_iv_photo_5);

        player_intro_1 = findViewById(R.id.starting_iv_intro_1);
        player_intro_2 = findViewById(R.id.starting_iv_intro_2);
        player_intro_3 = findViewById(R.id.starting_iv_intro_3);
        player_intro_4 = findViewById(R.id.starting_iv_intro_4);
        player_intro_5 = findViewById(R.id.starting_iv_intro_5);

        updatePlayerInfo(); //這個將更新所有場上球員的資料

        spinner1 = findViewById(R.id.starting_spining_1);
        spinner2 = findViewById(R.id.starting_spining_2);
        spinner3 = findViewById(R.id.starting_spining_3);
        spinner4 = findViewById(R.id.starting_spining_4);
        spinner5 = findViewById(R.id.starting_spining_5);


        spinner1.setAdapter(arrayAdapter);
        spinner1.setSelected(spinner1IsFirst);

        spinner2.setAdapter(arrayAdapter);
        spinner2.setSelected(spinner2IsFirst);

        spinner3.setAdapter(arrayAdapter);
        spinner3.setSelected(spinner3IsFirst);

        spinner4.setAdapter(arrayAdapter);
        spinner4.setSelected(spinner4IsFirst);

        spinner5.setAdapter(arrayAdapter);
        spinner5.setSelected(spinner5IsFirst);


        tv_quarter_1_selfScore = findViewById(R.id.tv_quarter_1_selfScore);
        tv_quarter_2_selfScore = findViewById(R.id.tv_quarter_2_selfScore);
        tv_quarter_3_selfScore = findViewById(R.id.tv_quarter_3_selfScore);
        tv_quarter_4_selfScore = findViewById(R.id.tv_quarter_4_selfScore);

        tv_quarter_1_rivalScore = findViewById(R.id.tv_quarter_1_rivalScore);
        tv_quarter_2_rivalScore = findViewById(R.id.tv_quarter_2_rivalScore);
        tv_quarter_3_rivalScore = findViewById(R.id.tv_quarter_3_rivalScore);
        tv_quarter_4_rivalScore = findViewById(R.id.tv_quarter_4_rivalScore);

        tv_currentQuarter = findViewById(R.id.tv_current_quarter);

        btn_nextQuarter = findViewById(R.id.btn_nextQuarter);

        tv_selfScore = findViewById(R.id.tv_self_score);
        tv_rivalScore = findViewById(R.id.tv_rival_score);


        currentPlayer = playingList.get(0);
        initPanelButton();
        setCurrentPlayerData();
        playerSelectedEffect(civ1);
        removeSelectedEffect(civ2, civ3, civ4, civ5);

    }

    private void initSelectedListener() {

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner1IsFirst) {
                    spinner1IsFirst = false;
                    System.out.println("spinner初始化成功");
                    return;
                }

                swapPlayer(0, position,0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(GamePanelActivity.this, "啥都沒", Toast.LENGTH_SHORT).show();
            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner2IsFirst) {
                    spinner2IsFirst = false;
                    System.out.println("spinner初始化成功");
                    return;
                }
                swapPlayer(1, position,1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(GamePanelActivity.this, "啥都沒", Toast.LENGTH_SHORT).show();
            }
        });
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner3IsFirst) {
                    spinner3IsFirst = false;
                    System.out.println("spinner初始化成功");
                    return;
                }
                swapPlayer(2, position,2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(GamePanelActivity.this, "啥都沒", Toast.LENGTH_SHORT).show();
            }
        });
        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner4IsFirst) {
                    spinner4IsFirst = false;
                    System.out.println("spinner初始化成功");
                    return;
                }
                swapPlayer(3, position,3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(GamePanelActivity.this, "啥都沒", Toast.LENGTH_SHORT).show();
            }
        });
        spinner5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner5IsFirst) {
                    spinner5IsFirst = false;
                    System.out.println("spinner初始化成功");
                    return;
                }
                swapPlayer(4, position,4);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(GamePanelActivity.this, "啥都沒", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePlayerInfo() {

        player_intro_1.setText(playingList.get(0).getUserName()+" "+playingList.get(0).getTmpGameNumber()+"");
        player_intro_2.setText(playingList.get(1).getUserName()+" "+playingList.get(1).getTmpGameNumber()+"");
        player_intro_3.setText(playingList.get(2).getUserName()+" "+playingList.get(2).getTmpGameNumber()+"");
        player_intro_4.setText(playingList.get(3).getUserName()+" "+playingList.get(3).getTmpGameNumber()+"");
        player_intro_5.setText(playingList.get(4).getUserName()+" "+playingList.get(4).getTmpGameNumber()+"");

        Picasso.get().load(playingList.get(0).getUserPhoto()).placeholder(R.drawable.tofu).into(civ1);
        Picasso.get().load(playingList.get(1).getUserPhoto()).placeholder(R.drawable.tofu).into(civ2);
        Picasso.get().load(playingList.get(2).getUserPhoto()).placeholder(R.drawable.tofu).into(civ3);
        Picasso.get().load(playingList.get(3).getUserPhoto()).placeholder(R.drawable.tofu).into(civ4);
        Picasso.get().load(playingList.get(4).getUserPhoto()).placeholder(R.drawable.tofu).into(civ5);

    }

    private void updatePlayerInfo2(int playerPosition) {

        switch (playerPosition) {

            case 0:
                player_intro_1.setText(playingList.get(0).getUserName()+" "+playingList.get(0).getTmpGameNumber()+"");
                updatePlayerInfo();
                Picasso.get().load(playingList.get(0).getUserPhoto()).placeholder(R.drawable.tofu).into(civ1);
                break;
            case 1:
                player_intro_2.setText(playingList.get(1).getUserName()+" "+playingList.get(1).getTmpGameNumber());
                updatePlayerInfo();
                Picasso.get().load(playingList.get(1).getUserPhoto()).placeholder(R.drawable.tofu).into(civ2);
                break;
            case 2:
                player_intro_3.setText(playingList.get(2).getUserName()+" "+playingList.get(2).getTmpGameNumber()+"");
                updatePlayerInfo();
                Picasso.get().load(playingList.get(2).getUserPhoto()).placeholder(R.drawable.tofu).into(civ3);
                break;
            case 3:
                player_intro_4.setText(playingList.get(3).getUserName()+" "+playingList.get(3).getTmpGameNumber()+"");
                updatePlayerInfo();
                Picasso.get().load(playingList.get(3).getUserPhoto()).placeholder(R.drawable.tofu).into(civ4);
                break;
            case 4:
                player_intro_5.setText(playingList.get(4).getUserName()+" "+playingList.get(4).getTmpGameNumber()+"");
                updatePlayerInfo();
                Picasso.get().load(playingList.get(4).getUserPhoto()).placeholder(R.drawable.tofu).into(civ5);


        }


    }

    private void swapPlayer(int playerPosition, int spinnerPosition, int spinnerNumber) {

        benchList.get(spinnerPosition).setStartingLineUp(true);
        playingList.get(playerPosition).setStartingLineUp(false);
        InGamePlayersModel tmp = playingList.get(playerPosition);
        playingList.set(playerPosition, benchList.get(spinnerPosition));
        benchList.set(spinnerPosition, tmp);


        spinnerSet.set(spinnerPosition, benchList.get(spinnerPosition).getUserName());
        arrayAdapter.notifyDataSetChanged();
        System.out.println("上場為=" + playingList.get(playerPosition).getUserName());
        System.out.println("下場為=" + benchList.get(spinnerPosition).getUserName());

        updatePlayerInfo2(spinnerNumber);

        if (!currentPlayer.getStartingLineUp()) {
            currentPlayer = playingList.get(playerPosition);
            setCurrentPlayerData();
        }


    }

    private void playerSelected() {


        civ1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPlayer = playingList.get(0);
                playerSelectedEffect(civ1);
                removeSelectedEffect(civ2, civ3, civ4, civ5);
                System.out.println("所選球員為=" + currentPlayer.getUserName());
                setCurrentPlayerData();
            }
        });
        civ2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPlayer = playingList.get(1);
                playerSelectedEffect(civ2);
                removeSelectedEffect(civ1, civ3, civ4, civ5);

                System.out.println("所選球員為=" + currentPlayer.getUserName());
                setCurrentPlayerData();
            }
        });
        civ3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPlayer = playingList.get(2);
                playerSelectedEffect(civ3);
                removeSelectedEffect(civ1, civ2, civ4, civ5);
                System.out.println("所選球員為=" + currentPlayer.getUserName());
                setCurrentPlayerData();
            }
        });
        civ4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPlayer = playingList.get(3);
                playerSelectedEffect(civ4);
                removeSelectedEffect(civ1, civ2, civ3, civ5);
                System.out.println("所選球員為=" + currentPlayer.getUserName());
                setCurrentPlayerData();
            }
        });
        civ5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPlayer = playingList.get(4);
                playerSelectedEffect(civ5);
                removeSelectedEffect(civ1, civ2, civ3, civ4);
                System.out.println("所選球員為=" + currentPlayer.getUserName());
                setCurrentPlayerData();
            }
        });

    }

    private void setCurrentPlayerData() {
        tv_OR.setText("" + currentPlayer.getPgr().getOr());
        tv_DR.setText("" + currentPlayer.getPgr().getDr());
        tv_3PA.setText("" + currentPlayer.getPgr().getThrpa());
        tv_3PM.setText("" + currentPlayer.getPgr().getThrpm());
        tv_AST.setText("" + currentPlayer.getPgr().getAst());
        tv_STL.setText("" + currentPlayer.getPgr().getStl());
        tv_BLK.setText("" + currentPlayer.getPgr().getBlk());
        tv_FGA.setText("" + currentPlayer.getPgr().getFga());
        tv_FGM.setText("" + currentPlayer.getPgr().getFgm());
        tv_FTA.setText("" + currentPlayer.getPgr().getFta());
        tv_FTM.setText("" + currentPlayer.getPgr().getFtm());
        tv_PF.setText("" + currentPlayer.getPgr().getPf());
        tv_TO.setText("" + currentPlayer.getPgr().getTo());

    }

    private void updateCurrentPlayerData() {
        currentPlayer.getPgr().setOr(Integer.valueOf(tv_OR.getText().toString()));
        currentPlayer.getPgr().setDr(Integer.valueOf(tv_DR.getText().toString()));
        currentPlayer.getPgr().setThrpa(Integer.valueOf(tv_3PA.getText().toString()));
        currentPlayer.getPgr().setThrpm(Integer.valueOf(tv_3PM.getText().toString()));
        currentPlayer.getPgr().setAst(Integer.valueOf(tv_AST.getText().toString()));
        currentPlayer.getPgr().setStl(Integer.valueOf(tv_STL.getText().toString()));
        currentPlayer.getPgr().setBlk(Integer.valueOf(tv_BLK.getText().toString()));
        currentPlayer.getPgr().setFga(Integer.valueOf(tv_FGA.getText().toString()));
        currentPlayer.getPgr().setFgm(Integer.valueOf(tv_FGM.getText().toString()));
        currentPlayer.getPgr().setFta(Integer.valueOf(tv_FTA.getText().toString()));
        currentPlayer.getPgr().setFtm(Integer.valueOf(tv_FTM.getText().toString()));
        currentPlayer.getPgr().setPf(Integer.valueOf(tv_PF.getText().toString()));
        currentPlayer.getPgr().setTo(Integer.valueOf(tv_TO.getText().toString()));
    }

    private void playerSelectedEffect(CircleImageView civ) {
        civ.setBorderOverlay(true);
        civ.setBorderColor(getColor(R.color.BERRY));
        civ.setBorderWidth(15);
    }

    private void removeSelectedEffect(CircleImageView civ1, CircleImageView civ2, CircleImageView civ3, CircleImageView civ4) {
        CircleImageView civs[] = new CircleImageView[4];
        civs[0] = civ1;
        civs[1] = civ2;
        civs[2] = civ3;
        civs[3] = civ4;
        for (int i = 0; i < 4; i++) {
            civs[i].setBorderWidth(0);
        }
    }

    private void initPanelButton() {

        btn_add_rival = findViewById(R.id.btn_rival_score);
        btn_minus_rival = findViewById(R.id.btn_rival_minus);
        btn_add_self = findViewById(R.id.btn_self_score);
        btn_minus_self = findViewById(R.id.btn_self_minus);
        tv_selfTotalFault = findViewById(R.id.tv_selfTotalFault);
        tv_rivalTotalFault = findViewById(R.id.tv_rivalTotalFault);

        tv_OR = findViewById(R.id.panel_or);
        tv_DR = findViewById(R.id.panel_dr);
        tv_3PA = findViewById(R.id.panel_3pa);
        tv_3PM = findViewById(R.id.panel_3pm);
        tv_AST = findViewById(R.id.panel_ast);
        tv_STL = findViewById(R.id.panel_stl);
        tv_BLK = findViewById(R.id.panel_blk);
        tv_FGA = findViewById(R.id.panel_fga);
        tv_FGM = findViewById(R.id.panel_fgm);
        tv_FTA = findViewById(R.id.panel_fta);
        tv_FTM = findViewById(R.id.panel_ftm);
        tv_3PM = findViewById(R.id.panel_3pm);
        tv_3PA = findViewById(R.id.panel_3pa);
        tv_PF = findViewById(R.id.panel_pf);
        tv_APF = findViewById(R.id.panel_apf);
        tv_TO = findViewById(R.id.panel_to);

        a_OR = findViewById(R.id.add_or);
        a_DR = findViewById(R.id.add_dr);
        a_3PA = findViewById(R.id.add_3pa);
        a_3PM = findViewById(R.id.add_3pm);
        a_AST = findViewById(R.id.add_ast);
        a_STL = findViewById(R.id.add_stl);
        a_BLK = findViewById(R.id.add_blk);
        a_FGA = findViewById(R.id.add_fga);
        a_FGM = findViewById(R.id.add_fgm);
        a_FTA = findViewById(R.id.add_fta);
        a_FTM = findViewById(R.id.add_ftm);
        a_3PM = findViewById(R.id.add_3pm);
        a_3PA = findViewById(R.id.add_3pa);
        a_PF = findViewById(R.id.add_pf);
        a_APF = findViewById(R.id.add_apf);
        a_TO = findViewById(R.id.add_to);


        m_OR = findViewById(R.id.minus_or);
        m_DR = findViewById(R.id.minus_dr);
        m_3PA = findViewById(R.id.minus_3pa);
        m_3PM = findViewById(R.id.minus_3pm);
        m_AST = findViewById(R.id.minus_ast);
        m_STL = findViewById(R.id.minus_stl);
        m_BLK = findViewById(R.id.minus_blk);
        m_FGA = findViewById(R.id.minus_fga);
        m_FGM = findViewById(R.id.minus_fgm);
        m_FTA = findViewById(R.id.minus_fta);
        m_FTM = findViewById(R.id.minus_ftm);
        m_3PM = findViewById(R.id.minus_3pm);
        m_3PA = findViewById(R.id.minus_3pa);
        m_PF = findViewById(R.id.minus_pf);
        m_APF = findViewById(R.id.minus_apf);
        m_TO = findViewById(R.id.minus_to);


        initButtonClickListenerA(tv_selfScore, btn_add_self, btn_minus_self);
        initButtonClickListenerA(tv_rivalScore, btn_add_rival, btn_minus_rival);
        initButtonClickListenerA(tv_OR, a_OR, m_OR);
        initButtonClickListenerA(tv_DR, a_DR, m_DR);
        initButtonClickListenerA(tv_AST, a_AST, m_AST);
        initButtonClickListenerA(tv_BLK, a_BLK, m_BLK);
        initButtonClickListenerA(tv_STL, a_STL, m_STL);
        initButtonClickListenerA(tv_TO, a_TO, m_TO);
        initButtonClickListenerA(tv_FGA, a_FGA, m_FGA);
        initButtonClickListenerA(tv_FGM, a_FGM, m_FGM);
        initButtonClickListenerA(tv_3PA, a_3PA, m_3PA);
        initButtonClickListenerA(tv_3PM, a_3PM, m_3PM);
        initButtonClickListenerA(tv_FTM, a_FTM, m_FTM);
        initButtonClickListenerA(tv_FTA, a_FTA, m_FTA);
        initButtonClickListenerA(tv_PF, a_PF, m_PF);
        initButtonClickListenerA(tv_APF, a_APF, m_APF);

        initButtonClickListenerB();
    }

    private void initButtonClickListenerA(final TextView tv, final ImageButton add, ImageButton minus) {

        tv.setText(String.valueOf(currentPlayer.getPgr().getOr()));
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentAmount = Integer.valueOf(tv.getText().toString());
                currentAmount++;
                if (currentAmount < 0) {
                    currentAmount = 0;
                }
//              currentAmount -=5;
                tv.setText(String.valueOf(currentAmount));
                currentPlayer.getPgr().setOr(currentAmount);
                switch (v.getId()) {
                    case R.id.add_apf:
                        int currentRivalFault = Integer.valueOf(tv_rivalTotalFault.getText().toString());
                        currentRivalFault++;
                        tv_rivalTotalFault.setText(String.valueOf(currentRivalFault));
                        if (currentRivalFault > 4) {
                            tv_rivalTotalFault.setTextColor(getColor(R.color.BERRY));
                        }
                        break;
                    case R.id.add_pf:
                        int currentSelfFault = Integer.valueOf(tv_selfTotalFault.getText().toString());
                        currentSelfFault++;
                        tv_selfTotalFault.setText(String.valueOf(currentSelfFault));
                        if (currentSelfFault > 4) {
                            tv_selfTotalFault.setTextColor(getColor(R.color.BERRY));
                        }
                }
                updateCurrentPlayerData();
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentAmount = Integer.valueOf(tv.getText().toString());
                currentAmount--;
                if (currentAmount < 0) {
                    currentAmount = 0;
                }
//              currentAmount -=5;
                tv.setText(String.valueOf(currentAmount));
                currentPlayer.getPgr().setOr(currentAmount);

                switch (v.getId()) {
                    case R.id.minus_apf:
                        int currentRivalFault = Integer.valueOf(tv_rivalTotalFault.getText().toString());
                        currentRivalFault--;
                        tv_rivalTotalFault.setText(String.valueOf(currentRivalFault));
                        if (currentRivalFault <= 4) {
                            tv_rivalTotalFault.setTextColor(getColor(R.color.DEEP_TEAL));
                        }
                        break;


                    case R.id.minus_pf:
                        int currentSelfFault = Integer.valueOf(tv_selfTotalFault.getText().toString());
                        currentSelfFault--;
                        tv_selfTotalFault.setText(String.valueOf(currentSelfFault));
                        if (currentSelfFault <= 4) {
                            tv_selfTotalFault.setTextColor(getColor(R.color.DEEP_TEAL));
                        }
                }
                updateCurrentPlayerData();
            }
        });

    }

    private void initButtonClickListenerB() {
        currentQuarter = 1;
        quartersSelfScores = new int[4];
        quartersRivalScores = new int[4];
        btn_nextQuarter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initQuarterChangeDialog();
            }
        });
    }

    private void initQuarterChangeDialog() {
        final AlertDialog.Builder altDlgBuilder = new AlertDialog.Builder(this);
        altDlgBuilder.setTitle(R.string.Warning);
        altDlgBuilder.setMessage(R.string.Are_you_sure_to_next_quarter);
        altDlgBuilder.setIcon(R.drawable.ic_error_outline_black_24dp);
        altDlgBuilder.setCancelable(true);
        altDlgBuilder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (currentQuarter) {
                    case 1:
                        int quarter_1_SelfScore = Integer.valueOf(tv_selfScore.getText().toString());
                        String currentSelfScore = tv_selfScore.getText().toString();
                        tv_quarter_1_selfScore.setText(currentSelfScore);
                        quartersSelfScores[0] = quarter_1_SelfScore;

                        int quarter_1_RivalScore = Integer.valueOf(tv_rivalScore.getText().toString());
                        String currentRivalScore = tv_rivalScore.getText().toString();
                        tv_quarter_1_rivalScore.setText(currentRivalScore);
                        quartersRivalScores[0] = quarter_1_RivalScore;

                        tv_currentQuarter.setText("第二節");
                        quartersSelfTotalFualt[0] = Integer.valueOf(tv_selfTotalFault.getText().toString());
                        currentQuarter = 2;

                        break;
                    case 2:
                        int quarter_2_SelfScore = Integer.valueOf(tv_selfScore.getText().toString()) - quartersSelfScores[0];
                        tv_quarter_2_selfScore.setText(String.valueOf(quarter_2_SelfScore));
                        quartersSelfScores[1] = quarter_2_SelfScore;

                        int quarter_2_RivalScore = Integer.valueOf(tv_rivalScore.getText().toString()) - quartersRivalScores[0];
                        tv_quarter_2_rivalScore.setText(String.valueOf(quarter_2_RivalScore));
                        quartersRivalScores[1] = quarter_2_RivalScore;

                        tv_currentQuarter.setText("第三節");
                        quartersSelfTotalFualt[1] = Integer.valueOf(tv_selfTotalFault.getText().toString());
                        currentQuarter = 3;
                        break;
                    case 3:
                        int quarter_3_SelfScore = Integer.valueOf(tv_selfScore.getText().toString())
                                - quartersSelfScores[0] - quartersSelfScores[1];
                        tv_quarter_3_selfScore.setText(String.valueOf(quarter_3_SelfScore));
                        quartersSelfScores[2] = quarter_3_SelfScore;

                        int quarter_3_RivalScore = Integer.valueOf(tv_rivalScore.getText().toString())
                                - quartersRivalScores[0] - quartersRivalScores[1];
                        tv_quarter_3_rivalScore.setText(String.valueOf(quarter_3_RivalScore));
                        quartersRivalScores[2] = quarter_3_RivalScore;
                        quartersSelfTotalFualt[2] = Integer.valueOf(tv_selfTotalFault.getText().toString());
                        currentQuarter = 4;
                        tv_currentQuarter.setText("第四節");
                        btn_nextQuarter.setText("結算");
                        break;
                    case 4:
                        int quarter_4_SelfScore =
                                Integer.valueOf(tv_selfScore.getText().toString())
                                        - quartersSelfScores[0] - quartersSelfScores[1] - quartersSelfScores[2];
                        tv_quarter_4_selfScore.setText(String.valueOf(quarter_4_SelfScore));
                        quartersSelfScores[3] = quarter_4_SelfScore;

                        int quarter_4_RivalScore =
                                Integer.valueOf(tv_rivalScore.getText().toString())
                                        - quartersRivalScores[0] - quartersRivalScores[1] - quartersRivalScores[2];
                        tv_quarter_4_rivalScore.setText(String.valueOf(quarter_4_RivalScore));
                        quartersRivalScores[3] = quarter_4_RivalScore;
                        quartersSelfTotalFualt[3] = Integer.valueOf(tv_selfTotalFault.getText().toString());


                        initResultSettingDialog();


                }
                tv_selfTotalFault.setText("" + 0);
                tv_rivalTotalFault.setText("" + 0);
                tv_APF.setText("" + 0);
                tv_rivalTotalFault.setTextColor(getColor(R.color.DEEP_TEAL));
                tv_rivalTotalFault.setTextColor(getColor(R.color.DEEP_TEAL));
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

    private void initResultSettingDialog() {

        LayoutInflater inflater = LayoutInflater.from(GamePanelActivity.this);
        final View view = inflater.inflate(R.layout.dialog_result_setting, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(GamePanelActivity.this);
        final AlertDialog resultSettingDialog = builder
                .setView(view) //步驟3. 將 RV(View)設為Dialog的View
                .setCancelable(false)
                .show();// show方法會回傳一個Dialog

        Button btnAddGameRecord = view.findViewById(R.id.addGameRecord_btn_confirmed); //步驟2. 從RV(view)中綁定元件id
        Button btnCancelGameRecord = view.findViewById(R.id.addGameRecord_btn_cancel); //步驟2. 從RV(view)中綁定元件id

        final EditText etGameName = view.findViewById(R.id.gameResult_gameName);
        final EditText etOpponentName = view.findViewById(R.id.gameResult_opponentName);
        final EditText etGameDescription = view.findViewById(R.id.gameResult_description);
        final EditText etGameLocation = view.findViewById(R.id.gameResult_gameLoaction);
        final TextView gameDate = view.findViewById(R.id.tv_addGameRecord_date);


        gameDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(GamePanelActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String date = timeFormat(year, month + 1, dayOfMonth);
                                gameRecord.setCompetitionDate(date);
                                //暫存本次的設定於本Activity
                                gameDate.setText(timeFormat(year, month + 1, dayOfMonth));
                            }
                        }, calendar.get(Calendar.YEAR)
                        , calendar.get(Calendar.MONTH)
                        , calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();

            }
        });

        btnAddGameRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etGameName.getText().toString().equals("") ||
                        etOpponentName.getText().toString().equals("") ||
                        gameRecord.getCompetitionDate() == null) {
                    if (gameRecord.getCompetitionDate() == null) {
                        showToast(R.string.msg_date_wrongFormat);
                    } else
                        showToast(R.string.msg_data_wrongFormat);
                    return;
                }
                gameRecord.setCompetitionName(etGameName.getText().toString());
                gameRecord.setOpponent(etOpponentName.getText().toString());
                gameRecord.setCompetitionDescribe(etGameDescription.getText().toString());
                gameRecord.setCompetitionLocation(etGameLocation.getText().toString());
                gameRecord.setSelfQuarterScore(quartersSelfScores);
                gameRecord.setRivalQuarterScore(quartersRivalScores);

                try {
                    uploadGameRecord();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                resultSettingDialog.dismiss();
            }
        });

        btnCancelGameRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultSettingDialog.dismiss();

            }
        });
    }

    private void showToast(int msgResId) {
        Toast.makeText(this, msgResId, Toast.LENGTH_SHORT).show();
    }

    private String timeFormat(int year, int month, int date) {
        String tmp = year + "-" + ((month < 10) ? "0" + month : month) + "-" + (
                (date < 10) ? "0" + date : date);
        return tmp;
    }

    private void uploadGameRecord() throws JSONException {

        inGamePlayersList.clear();
        inGamePlayersList.addAll(playingList);
        inGamePlayersList.addAll(benchList);

        JSONObject gameRecordJSON = new JSONObject();
        JSONArray playerArray = new JSONArray();
        JSONArray quarterRecordArray = new JSONArray();
        JSONObject recordJSON = new JSONObject();

        for (int i = 0; i < inGamePlayersList.size(); i++) {
            JSONObject playerRecord = new JSONObject();
            playerRecord.put("UserId", inGamePlayersList.get(i).getUserId());
            playerRecord.put("ORB", inGamePlayersList.get(i).getPgr().getOr());
            playerRecord.put("DRB", inGamePlayersList.get(i).getPgr().getDr());
            playerRecord.put("Assists", inGamePlayersList.get(i).getPgr().getAst());
            playerRecord.put("Steals", inGamePlayersList.get(i).getPgr().getStl());
            playerRecord.put("FGA", inGamePlayersList.get(i).getPgr().getFga());
            playerRecord.put("FGM", inGamePlayersList.get(i).getPgr().getFgm());
            playerRecord.put("FG3", inGamePlayersList.get(i).getPgr().getThrpa());
            playerRecord.put("FGM3", inGamePlayersList.get(i).getPgr().getThrpm());
            playerRecord.put("FTA", inGamePlayersList.get(i).getPgr().getFta());
            playerRecord.put("FTM", inGamePlayersList.get(i).getPgr().getFtm());
            playerRecord.put("Turnovers", inGamePlayersList.get(i).getPgr().getTo());
            playerRecord.put("PF", inGamePlayersList.get(i).getPgr().getPf());
            playerRecord.put("PersonScore", inGamePlayersList.get(i).getPgr().getScore());
            playerRecord.put("IsStart", inGamePlayersList.get(i).getStarter().toString());

            playerArray.put(i, playerRecord);
        }

        for (int i = 0; i < TOTAL_QUARTER; i++) {
            JSONObject quarterRecord = new JSONObject();
            quarterRecord.put("Quarter", i + 1);
            quarterRecord.put("QuarterScore", quartersSelfScores[i]);
            quarterRecord.put("TF", quartersSelfTotalFualt[i]);
            quarterRecord.put("OpponentScore", quartersRivalScores[i]);

            quarterRecordArray.put(i, quarterRecord);
        }

        recordJSON.put("GroupId", groupId);
        recordJSON.put("CompetitionName", gameRecord.getCompetitionName());
        recordJSON.put("CompetitionDate", gameRecord.getCompetitionDate());
        recordJSON.put("CompetitionDescribe", gameRecord.getCompetitionDescribe());
        recordJSON.put("Opponent", gameRecord.getOpponent());
        recordJSON.put("TotalScore", gameRecord.getSelfTotalScore());
        recordJSON.put("status", gameRecord.getGameResult());
        recordJSON.put("CompetitionLocation", gameRecord.getCompetitionLocation());
        recordJSON.put("Quarters", quarterRecordArray);
        recordJSON.put("Players", playerArray);

        gameRecordJSON.put("Competition", recordJSON);


        System.out.println("每節紀錄上傳為=" + quarterRecordArray.toString());
        System.out.println("球員紀錄上傳為=" + playerArray.toString());
        System.out.println("其他紀錄為=" + recordJSON.toString());
        System.out.println("完整紀錄為=" + gameRecordJSON.toString());

        NetworkController.getInstance().post(Global.API_UPLOAD_GAME_RECORD
                , gameRecordJSON.toString()
                , new NetworkController.CCallback() {
                    @Override
                    public void onFailure(String errorMsg) {
                        System.out.println("上傳紀錄失敗=" + errorMsg);
                    }

                    @Override
                    public void onResponse(JSONObject data) throws JSONException {
                        Logger.d(TAG, "上傳紀錄結果:" + data.getString("msg"));
                        System.out.println("上傳紀錄成功");


                    }

                    @Override
                    public void onCompleted() {

                    }
                });
        jumpToTeamActivity(teamModel);

    }

    private void jumpToTeamActivity(TeamModel teamModel) {
        Intent intent = new Intent(this, TeamActivity.class);
        //製作附加資料
        Bundle bundle = new Bundle();
        bundle.putParcelable("Team", teamModel);
        //附加到intent上
        intent.putExtras(bundle);
//        startActivityForResult(intent,RESULT_OK,bundle);
        startActivity(intent);
        finish();
    }
}
