package com.app.myteammanager.ui.myteam.teamdatafragments.Game.NewRecord;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.myteammanager.R;
import com.app.myteammanager.recycleview.BaseViewHolder;
import com.app.myteammanager.utils.Global;
import com.squareup.picasso.Picasso;


import java.util.Map;

public class InGamePlayerViewHolder extends BaseViewHolder<InGamePlayersModel> {

    public static class Factory extends BaseViewHolder.Factory { //繼承大工廠的小工廠

        public Factory(@Nullable ClickFuncBuilder clickFuncBuilder) {
            super(clickFuncBuilder);
        }

        @NonNull
        public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.viewholder_ingameplayerlist, parent, false);
            return new InGamePlayerViewHolder(view, clickFuncMap);
        }

        @Override
        public String getType() {
            return InGamePlayersModel.TYPE;
        }
    }

    ImageView coverImage;
    TextView playerName;
    TextView playerIntro;
    TextView playerNumber;
    LinearLayout linLay_inGamePlayers;
    CheckBox checkStartPlayer;


    protected InGamePlayerViewHolder(View itemView, final Map<Integer, ClickHandler> clickHandlerMap) {
        super(itemView);


        coverImage = itemView.findViewById(R.id.ingamemember_iv_photo);
        playerName = itemView.findViewById(R.id.tv_ingameplayer_name);
        playerNumber = itemView.findViewById(R.id.tv_ingameplayer_number);

        checkStartPlayer = itemView.findViewById(R.id.checkBox_startupline);
        checkStartPlayer.setChecked(false);

        linLay_inGamePlayers = itemView.findViewById(R.id.ingameplayers_linLay);
        checkStartPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkStartPlayer.isChecked()) {
                    if (Global.checkCount >= 5) {
                        checkStartPlayer.setChecked(false);
                        System.out.println("先發球員已滿");
                        return;
                    }
                    System.out.println(Global.checkCount);
                    Global.checkCount++;
                    clickHandlerMap.get(v.getId()).onClick(getLayoutPosition()); //會回傳他是第幾個ViewHolder
                } else {
                    Global.checkCount--;
                    System.out.println(Global.checkCount);
                    clickHandlerMap.get(v.getId()).onClick(getLayoutPosition()); //會回傳他是第幾個ViewHolder

                }
            }
        });

    }

    @Override
    public void bind(InGamePlayersModel iGPM) {
        Picasso.get()
                .load(iGPM.getUserPhoto())
                .placeholder(R.drawable.tofu)
                .into(coverImage);
        if(iGPM.getChosen()){
            checkStartPlayer.setChecked(true);
        }else {
            checkStartPlayer.setChecked(false);
        }
        playerName.setText(iGPM.getUserName());
//        playerIntro.setText(iGPM.getUserIntro());
        String startNumber = String.valueOf(iGPM.getTmpGameNumber());
        playerNumber.setText((startNumber.equals("-1")?"無背號":startNumber));

    }
}
