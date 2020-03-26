package com.app.myteammanager.ui.myteam.teamdatafragments.Finance;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.myteammanager.MainActivity;
import com.app.myteammanager.R;
import com.app.myteammanager.recycleview.BaseModel;
import com.app.myteammanager.recycleview.BaseViewHolder;
import com.app.myteammanager.recycleview.CommonAdapter;
import com.app.myteammanager.ui.myteam.teamdatafragments.RecordFragment;
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

public class FinanceFragment extends Fragment {

    public final static String TAG = "FinanceFragment";
    private CommonAdapter commonAdapter;
    private static ArrayList<BaseModel> dataList;
    private static ArrayList<BaseModel> dataListToShow;
    private TeamModel teamModel;
    private FloatingActionButton fab;
    FinanceRecordModel financeRecordModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.myteam_teamboard_fragment_finance, container, false);

        dataList = new ArrayList<>();

        teamModel = (TeamModel) getArguments().get("Team");

        System.out.println("隊伍ID=" + teamModel.getGroupId());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView recyclerView = fragmentView.findViewById(R.id.rv_financelist);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.smoothScrollToPosition(0);

        commonAdapter = new CommonAdapter(
                Arrays.asList(new FinanceViewHolder.Factory(new BaseViewHolder.ClickFuncBuilder()
                                .add(R.id.finance_btn_discard, new BaseViewHolder.ClickHandler() {
                                    @Override
                                    public void onClick(int position) {
                                        int groupId = teamModel.getGroupId();
                                        int walletId = ((FinanceRecordModel) dataList.get(position)).getWalletId();
                                        System.out.println("id" + groupId + "/" + walletId);
                                        deleteAlertDialog(groupId, walletId, position);
                                    }

                                    @Override
                                    public void reTurnData(int position, int integerData) {

                                    }
                                })
                                .add(R.id.finance_btn_edit, new BaseViewHolder.ClickHandler() {
                                    @Override
                                    public void onClick(int position) {
                                        int groupId = teamModel.getGroupId();
                                        financeRecordModel = ((FinanceRecordModel) dataList.get(position));
                                        editWallet(financeRecordModel, groupId);

                                    }

                                    @Override
                                    public void reTurnData(int position, int integerData) {

                                    }
                                })
                        )
                ));

        System.out.println("dataList =" + dataList.size());
        commonAdapter.bindDataSource(dataList);
        recyclerView.setAdapter(commonAdapter);
        getFinanceListFromCloud();
        init(fragmentView);
        return fragmentView;
    }

    private void init(View fragmentView) {

        fab = fragmentView.findViewById(R.id.fab_add_finance);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWallet(teamModel.getGroupId());
            }
        });
    }

    private void removeFinanceRecord(int groupId, final int walletId, final int position) {


        String deleteAPI = Global.API_DELETE_FINANCE_RECORD + groupId + "/" + walletId;
        System.out.println("API = "+Global.API_DELETE_FINANCE_RECORD + groupId + "/" + walletId);

        NetworkController.getInstance().delete(deleteAPI, new NetworkController.CCallback() {
            @Override
            public void onFailure(final String errorMsg) {

                System.out.println("移除失敗");
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "移除失敗", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(final JSONObject data) throws JSONException {
                final String response = data.getString("msg");

                System.out.println("(成功回應)移除id為=" + walletId + "的交易紀錄");
                System.out.println(response);

            }

            @Override
            public void onCompleted() {

            }
        });

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                dataList.remove(position);
                commonAdapter.notifyDataSetChanged();
            }
        });

    }

    private void getFinanceListFromCloud() {

        int groupId = teamModel.getGroupId();

        NetworkController.getInstance().get(Global.API_GET_FINANCE_LIST, String.valueOf(groupId)
                , new NetworkController.CCallback() {
                    @Override
                    public void onFailure(String errorMsg) {
                        System.out.println("抓取資料失敗，請檢查輸入資料是否正確");
                    }

                    @Override
                    public void onResponse(JSONObject data) throws JSONException {

                        JSONArray groupDetail = data.getJSONArray("walletRecords");
                        int dataLength = groupDetail.length();

                        System.out.println("總務檔案長度=" + dataLength);
                        //印出資料
                        for (int i = 0; i < dataLength; i++) {
                            System.out.println("i=" + i);
                            Logger.d(TAG, "抓取總務資料:" + groupDetail.getJSONObject(i));
                        }
                        for (int i = dataLength - 1; i >= 0; i--) {
                            JSONObject jsonObject = groupDetail.getJSONObject(i);
                            int walletRecordId = jsonObject.getInt("walletRecordId");
                            String recordDate = jsonObject.getString("recordDate");
                            String tmp = jsonObject.getString("walletDescribe");
                            tmp.replace("\\/","-");
                            String walletDescribe = tmp;

                            int moneyTrack = jsonObject.getInt("moneyTrack");
                            int totalMoney = jsonObject.getInt("totalMoney");

                            dataList.add(new FinanceRecordModel(walletRecordId
                                    , recordDate, walletDescribe, moneyTrack, totalMoney));

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==9) {
            System.out.println("財務重新讀取!!!!!");
            getFinanceListFromCloud();
            commonAdapter.refresh(dataList);
        }

    }

    private void deleteAlertDialog(final int groupId, final int walletId, final int position) {
        final AlertDialog.Builder altDlgBuilder = new AlertDialog.Builder(getActivity());
        altDlgBuilder.setTitle(R.string.Warning);
        altDlgBuilder.setMessage(R.string.Are_you_sure);
        altDlgBuilder.setIcon(R.drawable.ic_error_outline_black_24dp);
        altDlgBuilder.setCancelable(true);
        altDlgBuilder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeFinanceRecord(groupId, walletId, position);

            }
        });
        altDlgBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();


    }

    private void editWallet(FinanceRecordModel financeRecordModel, int groupId) {
        Intent intent = new Intent(getActivity(), AddOrEditFinanceRecordActivity.class);
        //製作附加資料
        Bundle bundle = new Bundle();
        bundle.putParcelable("walletForEdit", financeRecordModel);
        bundle.putInt("groupId", groupId);
        //附加到intent上
        intent.putExtras(bundle);
        startActivityForResult(intent, 2);

    }

    private void addWallet(int groupId) {
        Intent intent = new Intent(getActivity(), AddOrEditFinanceRecordActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("groupId", groupId);
        //附加到intent上
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }

}
