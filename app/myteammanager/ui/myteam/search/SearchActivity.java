package com.app.myteammanager.ui.myteam.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.app.myteammanager.R;

import com.app.myteammanager.recycleview.BaseModel;
import com.app.myteammanager.recycleview.BaseViewHolder;
import com.app.myteammanager.recycleview.CommonAdapter;
import com.app.myteammanager.utils.Global;
import com.app.myteammanager.utils.Logger;
import com.app.myteammanager.utils.NetworkController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchActivity extends AppCompatActivity {

    public final static String TAG = "SearchActivity";
    private CommonAdapter commonAdapter;
    private ArrayList<BaseModel> dataList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        dataList = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.rv_searchResult);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.smoothScrollToPosition(0);

        commonAdapter = new CommonAdapter(
                Arrays.asList(new SearchResultViewHolder.Factory(new BaseViewHolder.ClickFuncBuilder()
                        .add(R.id.btn_search_join, new BaseViewHolder.ClickHandler() {
                            @Override
                            public void onClick(int position) {


                                JSONObject jsonObject = new JSONObject();

                                int clickedGroupId = Integer.valueOf(((SearchResultModel)dataList.get(position)).groupId);
                                System.out.println("申請GroupId是 = "+clickedGroupId);

                                try {
                                    jsonObject.put("token", Global.CURRENTUSER_TOKEN)
                                            .put("GroupId", clickedGroupId);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                NetworkController.getInstance().post(Global.API_APPLY_GROUP,
                                        jsonObject.toString(), new NetworkController.CCallback() {

                                            @Override
                                            public void onFailure(String errorMsg) {
                                                System.out.println("申請失敗");
                                            }

                                            @Override
                                            public void onResponse(JSONObject data) throws JSONException {
                                                System.out.println("申請成功");
                                                System.out.println(data.getString("msg"));
                                            }

                                            @Override
                                            public void onCompleted() {

                                            }
                                        });

                            }

                            @Override
                            public void reTurnData(int position, int integerData) {

                            }
                        })
                )));

        System.out.println("dataList =" + dataList.size());
        commonAdapter.bindDataSource(dataList);
        recyclerView.setAdapter(commonAdapter);


        init();


    }
    private void init(){

        RadioGroup rbg_radioButtons = findViewById(R.id.rbg_search);
        final RadioButton rb_teamName = findViewById(R.id.rb_teamname);
        final RadioButton rb_invitedCode = findViewById(R.id.rb_inviteCoed);
        final EditText et_search = findViewById(R.id.et_search);
        final ImageButton btn_search = findViewById(R.id.btn_search);

        //預設輸入隊伍名稱
        rb_teamName.setChecked(true);

        //切換時清空輸入欄位
        rbg_radioButtons.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                et_search.getText().clear();
            }
        });


        btn_search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dataList.clear();//先清除原有的檔案

                String searchInput = et_search.getText().toString();

                JSONObject jsonObject = new JSONObject();

                if(rb_teamName.isChecked()) {
                    try {
                        jsonObject.put("token", Global.CURRENTUSER_TOKEN)
                                .put("GroupName", searchInput);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else
                    try {
                        jsonObject.put("token", Global.CURRENTUSER_TOKEN)
                                .put("InvitedCode", searchInput);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                NetworkController.getInstance().post(Global.API_SEARCH_CLOUD_GROUP,
                        jsonObject.toString(), new NetworkController.CCallback() {
                            @Override
                            public void onFailure(String errorMsg) {
                                System.out.println("搜尋失敗");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(SearchActivity.this, getText(R.string.cannot_find_result), Toast.LENGTH_SHORT).show();
                                        dataList.clear();
                                        commonAdapter.notifyDataSetChanged();

                                    }
                                });

                            }

                            @Override
                            public void onResponse(JSONObject data) throws JSONException {
                                System.out.println("搜尋成功");

                                if(rb_teamName.isChecked()) {

                                    JSONArray groupDetail = data.getJSONArray("group");
                                    int dataLength = groupDetail.length();
                                    //印出資料
                                    for (int i = 0; i < dataLength; i++) {
                                        Logger.d(TAG, "抓取結果:" + groupDetail.getJSONObject(i));
                                    }

                                    for (int i = 0; i < dataLength; i++) {
                                        JSONObject jsonObject = groupDetail.getJSONObject(i);
                                        String groupId = jsonObject.getString("groupId");
                                        String groupName = jsonObject.getString("groupName");
                                        String getImageURL = jsonObject.getString("groupPhoto");
                                        String groupIntro = jsonObject.getString("groupIntro");
                                        String status = jsonObject.getString("status");

                                        dataList.add(new SearchResultModel(groupId, groupName, getImageURL,
                                                groupIntro, status));

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                commonAdapter.notifyDataSetChanged();
                                            }
                                        });


                                    }
                                }else{

                                    JSONObject jsonObject = data.getJSONObject("group");

                                    String groupId = jsonObject.getString("groupId");
                                    String groupName = jsonObject.getString("groupName");
                                    String groupPhoto = jsonObject.getString("groupPhoto");
                                    String groupIntro = jsonObject.getString("groupIntro");
                                    String status = jsonObject.getString("status");

                                    dataList.add(new SearchResultModel(groupId, groupName, groupPhoto,
                                            groupIntro, status));

                                    runOnUiThread(new Runnable() {
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

        });
    }
}
