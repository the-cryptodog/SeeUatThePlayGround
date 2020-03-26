package com.app.myteammanager.ui.notifications.Manager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.myteammanager.R;
import com.app.myteammanager.recycleview.BaseViewHolder;

import java.util.Map;


public class ManagerNotificationListViewHolder extends BaseViewHolder<ManagerNotificationModel> {


    public static class Factory extends BaseViewHolder.Factory{ //繼承大工廠的小工廠

        public Factory(@Nullable ClickFuncBuilder clickFuncBuilder) {
            super(clickFuncBuilder);
        }

        @NonNull
        public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.viewholder_managernotification, parent, false);
            return new ManagerNotificationListViewHolder(view,clickFuncMap);
        }
        @Override
        public String getType() {
            return ManagerNotificationModel.TYPE;
        }
    }

    TextView applyName;
    TextView appliedGroupName;
    Button btn_agree;
    Button btn_disagree;

    protected ManagerNotificationListViewHolder(View itemView, final Map<Integer, ClickHandler> clickHandlerMap) {
        super(itemView);

        applyName = itemView.findViewById(R.id.tv_applyName);
        appliedGroupName = itemView.findViewById(R.id.tv_appliedGroupName);
        btn_agree = itemView.findViewById(R.id.notification_mbtn_agree);
        btn_disagree = itemView.findViewById(R.id.notification_mbtn_deny);

        btn_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickHandlerMap.get(v.getId()).onClick(getLayoutPosition()); //會回傳他是第幾個ViewHolder
            }
        });

        btn_disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickHandlerMap.get(v.getId()).onClick(getLayoutPosition()); //會回傳他是第幾個ViewHolder
            }
        });
    }
    @Override
    public void bind(ManagerNotificationModel managerNotificationModel) {
        applyName.setText(managerNotificationModel.getRequestName());
        appliedGroupName.setText(managerNotificationModel.getGroupName());
    }

}