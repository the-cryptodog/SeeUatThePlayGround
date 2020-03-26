package com.app.myteammanager.ui.myteam.teamlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.myteammanager.R;
import com.app.myteammanager.recycleview.BaseViewHolder;
import com.squareup.picasso.Picasso;


import java.util.Map;


public class TeamListViewHolder extends BaseViewHolder<TeamModel> {

    public static class Factory extends BaseViewHolder.Factory{ //繼承大工廠的小工廠

        public Factory(@Nullable ClickFuncBuilder clickFuncBuilder) {
            super(clickFuncBuilder);
        }

        @NonNull
        public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.viewholder_teamlist, parent, false);
            return new TeamListViewHolder(view,clickFuncMap);
        }
        @Override
        public String getType() {
            return TeamModel.TYPE;
        }
    }

    ImageView coverImage;
    TextView groupName;
    TextView nextEvent;
    TextView groupIntro;

    protected TeamListViewHolder(View itemView, final Map<Integer, ClickHandler> clickHandlerMap) {
        super(itemView);

        groupName = itemView.findViewById(R.id.tv_teamlist_name);
        coverImage = itemView.findViewById(R.id.member_iv_photo);
        nextEvent = itemView.findViewById(R.id.tv_teamlist_nextevent);
        groupIntro = itemView.findViewById(R.id.tv_teamlist_intro);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickHandlerMap.get(v.getId()).onClick(getLayoutPosition()); //會回傳他是第幾個ViewHolder
            }
        });
    }
    @Override
    public void bind(TeamModel teamModel) {
        Picasso.get()
                .load(teamModel.getImageURL())
                .placeholder(R.drawable.baseball)
                .into(coverImage);
        groupName.setText(teamModel.getGroupName());
//        nextEvent.setText(teamModel.getNextEvent());
        groupIntro.setText(teamModel.getGroupIntro());


    }


}