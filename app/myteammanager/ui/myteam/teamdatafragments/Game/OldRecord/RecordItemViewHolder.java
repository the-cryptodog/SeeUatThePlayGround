package com.app.myteammanager.ui.myteam.teamdatafragments.Game.OldRecord;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.myteammanager.R;
import com.app.myteammanager.recycleview.BaseViewHolder;

import java.util.Map;

public class RecordItemViewHolder extends BaseViewHolder<RecordItemModel> {

    public static class Factory extends BaseViewHolder.Factory { //繼承大工廠的小工廠

        public Factory(@Nullable ClickFuncBuilder clickFuncBuilder) {
            super(clickFuncBuilder);
        }

        @NonNull
        public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.viewholder_recorditem, parent, false);
            return new RecordItemViewHolder(view, clickFuncMap);
        }

        @Override
        public String getType() {
            return RecordItemModel.TYPE;
        }
    }



    protected RecordItemViewHolder(View itemView, final Map<Integer, ClickHandler> clickHandlerMap) {
        super(itemView);


    }

    @Override
    public void bind(RecordItemModel rim) {


    }
}
