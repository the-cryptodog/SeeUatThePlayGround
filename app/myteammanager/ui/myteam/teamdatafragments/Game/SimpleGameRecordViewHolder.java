package com.app.myteammanager.ui.myteam.teamdatafragments.Game;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.myteammanager.MainActivity;
import com.app.myteammanager.R;
import com.app.myteammanager.recycleview.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.Map;

import static androidx.core.content.ContextCompat.getColor;

public class SimpleGameRecordViewHolder extends BaseViewHolder<SimpleGameRecordModel> {

    public static class Factory extends BaseViewHolder.Factory{ //繼承大工廠的小工廠

        public Factory(@Nullable ClickFuncBuilder clickFuncBuilder) {
            super(clickFuncBuilder);
        }

        @NonNull
        public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.viewholder_gamelist, parent, false);
            return new SimpleGameRecordViewHolder(view,clickFuncMap);
        }
        @Override
        public String getType() {
            return SimpleGameRecordModel.TYPE;
        }
    }

    LinearLayout lin_Lay_simpleGameRecord;
    TextView competitionLocation;
    TextView opponentName;
    TextView competitionName;
    TextView competitionDate;
    TextView gameScore;
    TextView gameResult;
    TextView selfName;



    protected SimpleGameRecordViewHolder(View itemView, final Map<Integer, ClickHandler> clickHandlerMap) {
        super(itemView);
        selfName=itemView.findViewById(R.id.tv_self_name);
        competitionLocation=itemView.findViewById(R.id.tv_game_location);
        competitionName= itemView.findViewById(R.id.tv_game_name);
        opponentName= itemView.findViewById(R.id.tv_rival_name);
        competitionDate= itemView.findViewById(R.id.tv_game_date);
        gameScore= itemView.findViewById(R.id.tv_game_score);
        gameResult= itemView.findViewById(R.id.tv_game_result);
        lin_Lay_simpleGameRecord = itemView.findViewById(R.id.lin_lay_simpleGameRecord);

        lin_Lay_simpleGameRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickHandlerMap.get(v.getId()).onClick(getLayoutPosition()); //會回傳他是第幾個ViewHolder
            }
        });

    }
    @Override
    public void bind(SimpleGameRecordModel simpleGameRecordModel) {
        competitionLocation.setText(simpleGameRecordModel.getCompetitionLocation());
        competitionName.setText(simpleGameRecordModel.getCompetitionName());
        selfName.setText(simpleGameRecordModel.getSelfName());
        opponentName.setText(simpleGameRecordModel.getOpponentName());
        competitionDate.setText(simpleGameRecordModel.getCompetitionDate());
        String score = simpleGameRecordModel.getOurScore()+" : "+ simpleGameRecordModel.getOpponentScore();
        gameScore.setText(score);
        if((simpleGameRecordModel.getOurScore()-simpleGameRecordModel.getOpponentScore())>0){
            gameResult.setText("勝");
            gameResult.setTextColor(itemView.getContext().getColor(R.color.TEAL));
        }
        if((simpleGameRecordModel.getOurScore()-simpleGameRecordModel.getOpponentScore())<0) {
            gameResult.setText("敗");
            gameResult.setTextColor(itemView.getContext().getColor(R.color.RED));
        }
        if((simpleGameRecordModel.getOurScore()-simpleGameRecordModel.getOpponentScore())==0) {
            gameResult.setText("和");
            gameResult.setTextColor(itemView.getContext().getColor(R.color.DEEP_TEAL));
        }

    }
}
