package com.app.myteammanager.ui.myteam.teamdatafragments.TeamMember;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
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


public class TeamMemberViewHolder extends BaseViewHolder<TeamMemberModel> {

    public static class Factory extends BaseViewHolder.Factory { //繼承大工廠的小工廠

        public Factory(@Nullable ClickFuncBuilder clickFuncBuilder) {
            super(clickFuncBuilder);
        }

        @NonNull
        public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.viewholder_playerlist, parent, false);
            return new TeamMemberViewHolder(view, clickFuncMap);
        }

        @Override
        public String getType() {
            return TeamMemberModel.TYPE;
        }

    }

    ImageView coverImage;
    EditText editNumber;

    TextView userName;
    TextView userNumber;


    CheckBox checkPlayer;
    FrameLayout frameLayout;
    LinearLayout linearLayout;


    protected TeamMemberViewHolder(View itemView, final Map<Integer, ClickHandler> clickHandlerMap) {

        super(itemView);

        coverImage = itemView.findViewById(R.id.member_iv_photo);
        frameLayout = itemView.findViewById(R.id.teammember_framelayout);
        linearLayout = itemView.findViewById(R.id.teammember_linLay);

        editNumber = itemView.findViewById(R.id.tv_chooseplayer_number);
        checkPlayer = itemView.findViewById(R.id.player_checkbox);

        checkPlayer.setChecked(false);

        userName = itemView.findViewById(R.id.tv_chooseplayer_name);
        userNumber = itemView.findViewById(R.id.tv_teammember_uniformNumber);

        editNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editNumber.getText().toString().equals("")) {
                    return;
                }
                clickHandlerMap.get(R.id.tv_chooseplayer_number)
                        .reTurnData(getLayoutPosition(),Integer.valueOf(editNumber.getText().toString()));
            }
        });

        checkPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPlayer.isChecked()) {
                    userNumber.setText(editNumber.getText().toString());
                    userNumber.setVisibility(View.VISIBLE);
                    editNumber.setVisibility(View.INVISIBLE);
                    Global.checkCount++;
                    System.out.println(Global.checkCount);
                    clickHandlerMap.get(v.getId()).onClick(getLayoutPosition());
                    clickHandlerMap.get(v.getId()).reTurnData(getLayoutPosition()
                            , Integer.valueOf(userNumber.getText().toString())); //會回傳他是第幾個ViewHolder

                } else {
                    if (editNumber.getText().toString().equals("")) {
                        return;
                    }
                    Global.checkCount--;
                    System.out.println(Global.checkCount);
                    userNumber.setVisibility(View.INVISIBLE);
                    editNumber.setVisibility(View.VISIBLE);
                    clickHandlerMap.get(v.getId()).onClick(getLayoutPosition()); //會回傳他是第幾個ViewHolder
                }
            }
        });
    }

    @Override
    public void bind(final TeamMemberModel frm) {


        Picasso.get()
                .load(frm.getUserPhoto())
                .placeholder(R.drawable.tofu)
                .into(coverImage);
        System.out.println("getReadyForChosen=" + frm.getReadyForChosen());

        userName.setText(frm.getUserName()); // 名字

//        if(frm.getRole().equals("is member")){
//            userRole.setText("隊員");
//        }else {
//            userRole.setText("管理員");
//        }

//        if (frm.getReadyForGame()) {   // 先發名單
//            System.out.println("進入先發單單");
//            frameLayout.removeView(editNumber);// 移除編輯背號EditText
//            userName.setText(frm.getUserName());// 顯示隊員名稱
//            userNumber.setVisibility(View.VISIBLE);// 顯示背號元件
//            userNumber.setTextSize(40);// 調整背號大小
//            userNumber.setText(String.valueOf(frm.getTmpGameNumber()));// 確認背號
//        }

        if (frm.getIsChosen()) {
            checkPlayer.setChecked(true);
            userNumber.setText("" + frm.getTmpGameNumber());
            userNumber.setVisibility(View.VISIBLE);
            editNumber.setVisibility(View.INVISIBLE);
        } else {
            String tmpNumber = frm.getTmpGameNumber();
            editNumber.setText((tmpNumber == null) ? "沒背號" : tmpNumber);
            checkPlayer.setChecked(false);
            userNumber.setVisibility(View.INVISIBLE);
            editNumber.setVisibility(View.VISIBLE);
        }

    }


}


