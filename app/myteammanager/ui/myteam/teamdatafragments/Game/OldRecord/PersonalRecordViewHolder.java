package com.app.myteammanager.ui.myteam.teamdatafragments.Game.OldRecord;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.myteammanager.R;
import com.app.myteammanager.recycleview.BaseViewHolder;
import com.app.myteammanager.ui.myteam.teamdatafragments.Game.NewRecord.PersonGameRecord;


import java.util.Map;

public class PersonalRecordViewHolder extends BaseViewHolder<PersonGameRecord> {

    public static class Factory extends BaseViewHolder.Factory { //繼承大工廠的小工廠

        public Factory(@Nullable BaseViewHolder.ClickFuncBuilder clickFuncBuilder) {
            super(clickFuncBuilder);
        }

        @NonNull
        public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.viewholder_recorddetail, parent, false);
            return new PersonalRecordViewHolder(view, clickFuncMap);
        }

        @Override
        public String getType() {
            return PersonGameRecord.TYPE;
        }
    }

    TextView isStart, userName, score, or, dr, ast, blk, stl, pf, fga, fgm, thrpa, thrpm, fta, ftm, to;


    protected PersonalRecordViewHolder(View itemView, final Map<Integer, BaseViewHolder.ClickHandler> clickHandlerMap) {
        super(itemView);
        isStart = itemView.findViewById(R.id.tv_isStart);
        userName = itemView.findViewById(R.id.tv_name);
        score = itemView.findViewById(R.id.tv_personScore);
        or = itemView.findViewById(R.id.tv_orb);
        dr = itemView.findViewById(R.id.tv_drb);
        ast = itemView.findViewById(R.id.tv_assist);
        blk = itemView.findViewById(R.id.tv_blocks);
        stl = itemView.findViewById(R.id.tv_steals);
        pf = itemView.findViewById(R.id.tv_pf);
        fga = itemView.findViewById(R.id.tv_fga);
        fgm = itemView.findViewById(R.id.tv_fgm);
        thrpa = itemView.findViewById(R.id.tv_fG3);
        thrpm = itemView.findViewById(R.id.tv_fgM3);
        fta = itemView.findViewById(R.id.tv_fta);
        ftm = itemView.findViewById(R.id.tv_ftm);
        to = itemView.findViewById(R.id.tv_turnovers);


    }

    @Override
    public void bind(PersonGameRecord pgr) {

        isStart.setText((pgr.isStart())?"先發":" ");
        userName.setText(pgr.getUserName());
        score.setText(""+pgr.getSetScore());
        or.setText(""+pgr.getOr());
        dr.setText(""+pgr.getDr());
        ast.setText(""+pgr.getAst());
        blk.setText(""+pgr.getBlk());
        stl.setText(""+pgr.getStl());
        pf.setText(""+pgr.getPf());
        fga.setText(""+pgr.getFga());
        fgm.setText(""+pgr.getFgm());
        thrpa.setText(""+pgr.getThrpa());
        thrpm.setText(""+pgr.getThrpm());
        fta.setText(""+pgr.getFta());
        ftm.setText(""+pgr.getFtm());
        to.setText(""+pgr.getTo());


    }
}
