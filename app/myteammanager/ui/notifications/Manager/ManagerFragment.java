package com.app.myteammanager.ui.notifications.Manager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.myteammanager.R;
import com.app.myteammanager.recycleview.BaseModel;
import com.app.myteammanager.recycleview.BaseViewHolder;
import com.app.myteammanager.recycleview.CommonAdapter;
import com.app.myteammanager.ui.notifications.NotificationsViewModel;
import com.app.myteammanager.utils.Global;
import com.app.myteammanager.utils.NetworkController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class ManagerFragment extends Fragment {

    public final static String TAG = "ManagerFragment";
    private CommonAdapter commonAdapter;
    private static ArrayList<BaseModel> dataList;
    private Context context ;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        dataList = new ArrayList<>();
        context = getActivity();
        View fragmentView = inflater.inflate(R.layout.notification_manager_fragment, container, false);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView recyclerView = fragmentView.findViewById(R.id.rv_manager_notification);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.smoothScrollToPosition(0);


        commonAdapter = new CommonAdapter(
                Arrays.asList(new ManagerNotificationListViewHolder.Factory(new BaseViewHolder.ClickFuncBuilder()
                        .add(R.id.notification_mbtn_agree, new BaseViewHolder.ClickHandler() {
                            @Override
                            public void onClick(int position) {
//                                Toast.makeText(getActivity(), "點擊同意" + position, Toast.LENGTH_SHORT).show();
                                int notifyId = ((ManagerNotificationModel)dataList.get(position)).getNotifyId();
                                initAlertDialog(notifyId,true,position);


                            }

                            @Override
                            public void reTurnData(int position, int integerData) {

                            }
                        })
                        .add(R.id.notification_mbtn_deny, new BaseViewHolder.ClickHandler() {
                            @Override
                            public void onClick(int position) {
                                int notifyId = ((ManagerNotificationModel)dataList.get(position)).getNotifyId();
                                initAlertDialog(notifyId,false,position);

//                              Toast.makeText(getActivity(), "點擊拒絕" + position, Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void reTurnData(int position, int integerData) {

                            }
                        })
                )));

        System.out.println("dataList =" + dataList.size());
        commonAdapter.bindDataSource(dataList);
        recyclerView.setAdapter(commonAdapter);

        init(fragmentView);//CALL API 拿資料

        return fragmentView;

    }


    private void init(final View fragmentView){

        JSONObject jsonObject = new JSONObject();

        System.out.println("目前的TOKEN是 : " + Global.CURRENTUSER_TOKEN);
        try {
            jsonObject.put("token", Global.CURRENTUSER_TOKEN);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        NetworkController.getInstance().post(Global.API_NOTIFICATION_MANAGER,jsonObject.toString(), new NetworkController.CCallback() {
            @Override
            public void onFailure(final String errorMsg) {
                System.out.println("讀取管理員通知失敗");
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        TextView noMessage = fragmentView.findViewById(R.id.tv_manager_nomessage);
                        if(noMessage == null){
                            return;
                        }
                        noMessage.setText("目前無通知");
                    }
                });

            }

            @Override
            public void onResponse(JSONObject data) throws JSONException {
                JSONArray jsonArray = data.getJSONArray("mail");
                System.out.println("讀取通知成功");
                for(int i = 0 ; i <jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int notifyId = jsonObject.getInt("notifyId");
                    int requestId = jsonObject.getInt("requestId");
                    String requestName = jsonObject.getString("requestName");
                    String groupName = jsonObject.getString("groupName");
                    dataList.add(new ManagerNotificationModel(notifyId,requestId,requestName,groupName));
                    System.out.println("通知檔案建立成功");
                    System.out.println("通知檔案長度" + dataList.size());
                    System.out.println(jsonArray.getJSONObject(i));
                    System.out.println(dataList.get(i).toString());

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

    private void putAnswerToApi(int notifyId , boolean agreedApplication, final int position){

        JSONObject jsonObject = new JSONObject();

        String agreement;

        if(agreedApplication){
            agreement = "accept";
        }else{
            agreement = "reject";
        }


        System.out.println(agreement + notifyId);
        try {
            jsonObject.put("NotifyId", notifyId)
                    .put("Status",agreement);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkController.getInstance().post(Global.API_CHECK_NOTIFICATION_MANAGER,jsonObject.toString(), new NetworkController.CCallback() {
            @Override
            public void onFailure(String errorMsg) {

            }

            @Override
            public void onResponse(JSONObject data) throws JSONException {
                final String result = data.getString("msg");
                System.out.println("申請結果=" + result);

                dataList.remove(position);

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        commonAdapter.notifyDataSetChanged();
//                        Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onCompleted() {

            }
        });

    }

    private void initAlertDialog(final int notifyId, final boolean agreement, final int position){
        AlertDialog.Builder altDlgBuilder = new AlertDialog.Builder(getActivity());
        altDlgBuilder.setTitle(R.string.Warning);
        altDlgBuilder.setMessage(R.string.Are_you_sure);
        altDlgBuilder.setIcon(R.drawable.ic_error_outline_black_24dp);
        altDlgBuilder.setCancelable(true);
        altDlgBuilder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                putAnswerToApi(notifyId, agreement,position);
            }
        });
        altDlgBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();


    }

}


