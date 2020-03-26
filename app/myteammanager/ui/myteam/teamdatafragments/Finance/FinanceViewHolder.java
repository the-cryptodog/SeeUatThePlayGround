package com.app.myteammanager.ui.myteam.teamdatafragments.Finance;

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


public class FinanceViewHolder extends BaseViewHolder<FinanceRecordModel> {

    public static class Factory extends BaseViewHolder.Factory{ //繼承大工廠的小工廠

        public Factory(@Nullable ClickFuncBuilder clickFuncBuilder) {
            super(clickFuncBuilder);
        }

        @NonNull
        public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.viewholder_finance, parent, false);

            return new FinanceViewHolder(view,clickFuncMap);
        }
        @Override
        public String getType() {
            return FinanceRecordModel.TYPE;
        }
    }

    TextView date;
    TextView trade;
    TextView cashflow;
    TextView sum;
    Button btn_discard;
    Button btn_edit;

    protected FinanceViewHolder(View itemView, final Map<Integer, ClickHandler> clickHandlerMap) {
        super(itemView);

        date = itemView.findViewById(R.id.finance_tv_date);
        trade = itemView.findViewById(R.id.finance_tv_trade);
        cashflow = itemView.findViewById(R.id.finance_tv_cashflow);
        sum = itemView.findViewById(R.id.finance_tv_sum);
        btn_discard= itemView.findViewById(R.id.finance_btn_discard);
        btn_edit= itemView.findViewById(R.id.finance_btn_edit);

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickHandlerMap.get(v.getId()).onClick(getLayoutPosition()); //會回傳他是第幾個ViewHolder
            }
        });

        btn_discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickHandlerMap.get(v.getId()).onClick(getLayoutPosition()); //會回傳他是第幾個ViewHolder
            }
        });
    }
    @Override
    public void bind(FinanceRecordModel frm) {
        date.setText(frm.getDate());
        trade.setText(frm.getWalletDescribe());
        cashflow.setText(String.valueOf(frm.getCashFlow()));
        sum.setText(String.valueOf(frm.getSum()));

    }


}