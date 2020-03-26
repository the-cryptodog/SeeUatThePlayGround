package com.app.myteammanager.ui.notifications.Member;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.myteammanager.R;
import com.app.myteammanager.recycleview.BaseViewHolder;
import com.app.myteammanager.ui.notifications.Manager.ManagerNotificationModel;

import java.util.Map;


public class MemberNotificationListViewHolder extends BaseViewHolder<MemberNotificationModel> {


    public static class Factory extends BaseViewHolder.Factory{ //繼承大工廠的小工廠

        public Factory(@Nullable ClickFuncBuilder clickFuncBuilder) {
            super(clickFuncBuilder);
        }

        @NonNull
        public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.viewholder_membernotification, parent, false);
            return new MemberNotificationListViewHolder(view,clickFuncMap);
        }
        @Override
        public String getType() {
            return ManagerNotificationModel.TYPE;
        }
    }
    private int notifyId ;
    private String groupName;
    private String acceptedOrNot ;

    TextView appliedGroupName;
    TextView status;
    Button btn_remove;


    protected MemberNotificationListViewHolder(View itemView, final Map<Integer, ClickHandler> clickHandlerMap) {
        super(itemView);

        appliedGroupName = itemView.findViewById(R.id.tv_noti_member_group);
        status = itemView.findViewById(R.id.tv_noti_member_status);
        btn_remove = itemView.findViewById(R.id.btn_removenotification);

        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickHandlerMap.get(v.getId()).onClick(getLayoutPosition()); //會回傳他是第幾個ViewHolder
            }
        });


    }
    @Override
    public void bind(MemberNotificationModel memberNotificationModel) {
        appliedGroupName.setText(memberNotificationModel.getGroupName());
        if(memberNotificationModel.getAcceptedOrNot().equals("been rejected")){
            status.setText("已拒絕你的入隊申請");
        }else {
            status.setText("已接受你的入隊申請");
        }
    }

}