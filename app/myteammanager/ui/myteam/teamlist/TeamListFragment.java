package com.app.myteammanager.ui.myteam.teamlist;

import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.myteammanager.AddTeamActivity;
import com.app.myteammanager.R;
import com.app.myteammanager.TeamActivity;
import com.app.myteammanager.recycleview.BaseModel;
import com.app.myteammanager.recycleview.BaseViewHolder;
import com.app.myteammanager.recycleview.CommonAdapter;
import com.app.myteammanager.ui.myteam.search.SearchActivity;
import com.app.myteammanager.utils.Global;
import com.app.myteammanager.utils.Logger;
import com.app.myteammanager.utils.NetworkController;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.Arrays;


public class TeamListFragment extends Fragment {

    public final static String TAG = "TeamListFragmentActivity";
    private CommonAdapter commonAdapter;
    private static ArrayList<BaseModel> dataList;
    private Toolbar addTeamToolbar;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        dataList = new ArrayList<>();
        System.out.println("onCreateView!!!!!!");

        //---------------------------------這裡把view吹出來
        View fragmentView = inflater.inflate(R.layout.fragment_myteam, container, false);
        //---------------------------------設定ToolBar
        setToolbar(fragmentView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView recyclerView = fragmentView.findViewById(R.id.rv_teamList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.smoothScrollToPosition(0);


        commonAdapter = new CommonAdapter(
                Arrays.asList(new TeamListViewHolder.Factory(new BaseViewHolder.ClickFuncBuilder()
                        .add(R.id.vh_teamlist, new BaseViewHolder.ClickHandler() {
                            @Override
                            public void onClick(int position) {
//                                Toast.makeText(getActivity(), "點擊球隊" + position, Toast.LENGTH_SHORT).show();
                                TeamModel teamModel = ((TeamModel)dataList.get(position));
                                jumpToTeamActivity(teamModel);
//                                     getActivity().getSupportFragmentManager()
//                                        .beginTransaction()
//                                        .replace(R.id.xx, new FinanceFragment(), null)
//                                        .addToBackStack(null)
//                                        .commit();
                            }

                            @Override
                            public void reTurnData(int position, int integerData) {

                            }
                        })
                )));

        System.out.println("dataList =" + dataList.size());
        commonAdapter.bindDataSource(dataList);
        recyclerView.setAdapter(commonAdapter);
        getTeamListFromCloud();

        return fragmentView;

    }

    private void getTeamListFromCloud() {

        dataList.clear();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", Global.CURRENTUSER_TOKEN);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        NetworkController.getInstance().post(Global.API_SHOW_GROUP, jsonObject.toString()
                , new NetworkController.CCallback() {
                    @Override
                    public void onFailure(String errorMsg) {
                        System.out.println("抓取資料失敗，請檢查輸入資料是否正確");
                    }

                    @Override
                    public void onResponse(JSONObject data) throws JSONException {

                        JSONArray groupDetail = data.getJSONArray("myGroups");
                        int dataLength = groupDetail.length();
                        //印出資料
                        for (int i = 0; i < dataLength; i++) {
                            Logger.d(TAG, "抓取結果:" + groupDetail.getJSONObject(i));
                        }
                        for (int i = 0; i < dataLength; i++) {

                            JSONObject jsonObject = groupDetail.getJSONObject(i);
                            int groupId = jsonObject.getInt("groupId");
                            String groupName = jsonObject.getString("groupName");
                            String imageURL = jsonObject.getString("groupPhoto");
                            String nextEvent = jsonObject.getString("nextEvent");
                            String groupIntro = jsonObject.getString("groupIntro");
                            String inviteCode = jsonObject.getString("invitedCode");

                            dataList.add(new TeamModel(groupId, groupName,
                                    nextEvent, groupIntro, inviteCode, imageURL));


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
        System.out.println("資料數量: "+dataList.size());


    }

    private void setToolbar(View view) {
        addTeamToolbar = view.findViewById(R.id.profile_toolbar);
        addTeamToolbar.inflateMenu(R.menu.add_team_menu);
        addTeamToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.search_group:
                        jumpToSearchActivity();
                        System.out.println("點擊搜尋!!!");
                        break;
                    case R.id.add_group:
                        jumpAddTeamActivity();
                        System.out.println("點擊新增!!!");
                        break;
                }
                return true;
            }
        });
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        getTeamListFromCloud();
//        System.out.println("資料刷新數量: "+dataList.size());
//    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        getTeamListFromCloud();
//        commonAdapter.refresh(dataList);
//    }

    private void jumpToTeamActivity(TeamModel teamModel) {
        Intent intent = new Intent(this.getActivity(), TeamActivity.class);
        //製作附加資料
        Bundle bundle = new Bundle();
        bundle.putParcelable("Team",teamModel);
        //附加到intent上
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void jumpToSearchActivity() {
        Intent intent = new Intent(this.getActivity(), SearchActivity.class);
        //製作附加資料
        Bundle bundle = new Bundle();
        //附加到intent上
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void jumpAddTeamActivity() {
        Intent intent = new Intent(this.getActivity(), AddTeamActivity.class);
        //製作附加資料
        Bundle bundle = new Bundle();
        //附加到intent上
        intent.putExtras(bundle);
        startActivityForResult(intent,2);
    }


    private boolean inputCheck(EditText inputOne, EditText inputTwo) {
        if (inputOne.getText().toString().equals("") ||
                inputTwo.getText().toString().equals("")) {
            return false;
        }
        return true;
    }

    private void showToast(int msgResId) {
        Toast.makeText(getActivity(), msgResId, Toast.LENGTH_SHORT).show();
    }




}
