package com.app.myteammanager.ui.notifications.Member;

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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.myteammanager.R;
import com.app.myteammanager.recycleview.BaseModel;
import com.app.myteammanager.recycleview.BaseViewHolder;
import com.app.myteammanager.recycleview.CommonAdapter;
import com.app.myteammanager.ui.notifications.Manager.ManagerNotificationModel;
import com.app.myteammanager.utils.Global;
import com.app.myteammanager.utils.NetworkController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class MemberFragment extends Fragment {

    public final static String TAG = "TeamMemberFragment";
    private CommonAdapter commonAdapter;
    private static ArrayList<BaseModel> dataList;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        dataList = new ArrayList<>();

        View fragmentView = inflater.inflate(R.layout.notification_member_fragment, container, false);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView recyclerView = fragmentView.findViewById(R.id.rv_member_notification);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.smoothScrollToPosition(0);


        commonAdapter = new CommonAdapter(
                Arrays.asList(new MemberNotificationListViewHolder.Factory(new BaseViewHolder.ClickFuncBuilder()
                        .add(R.id.btn_removenotification, new BaseViewHolder.ClickHandler() {
                            @Override
                            public void onClick(int position) {
                                removeNotification(position);

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

        init(fragmentView);//CALL API 拿資料

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                //  上下拖移callback
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // 左右滑動callback
            }
        }).attachToRecyclerView(recyclerView);

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

        NetworkController.getInstance().post(Global.API_NOTIFICATION_MEMBER,jsonObject.toString(), new NetworkController.CCallback() {
            @Override
            public void onFailure(final String errorMsg) {

                System.out.println("讀取隊員通知失敗");
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        TextView noMessage = fragmentView.findViewById(R.id.tv_member_nomessage);
                        if(noMessage==null) {
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
                    String requestName = jsonObject.getString("groupName");
                    String acceptedOrNot = jsonObject.getString("acceptedOrNot");

                    dataList.add(new MemberNotificationModel(notifyId,requestName,acceptedOrNot));
                    System.out.println("隊員通知檔案建立成功");
                    System.out.println("隊員通知檔案長度" + dataList.size());
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

        commonAdapter.notifyDataSetChanged();
    }

    private void removeNotification(final int Position){

        JSONObject jsonObject = new JSONObject();

        int groupId = ((MemberNotificationModel)dataList.get(Position)).getNotifyId();

        System.out.println("目前的groupId是 : " + groupId);
        try {
            jsonObject.put("NotifyId", groupId );

        } catch (JSONException e) {
            e.printStackTrace();
        }

        NetworkController.getInstance().post(Global.API_CHECK_NOTIFICATION_MEMBER,jsonObject.toString(), new NetworkController.CCallback() {
            @Override
            public void onFailure(final String errorMsg) {

                System.out.println("移除失敗");
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"移除失敗",Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(final JSONObject data) throws JSONException {
                final String response = data.getString("msg");
                System.out.println("response");
                System.out.println("(成功回應)移除位置為=" + Position + "的通知" );

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        dataList.remove(Position);
                        Toast.makeText(getActivity(),response,Toast.LENGTH_SHORT).show();
                        commonAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onCompleted() {

            }
        });

    }

}
