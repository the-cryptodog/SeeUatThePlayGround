package com.app.myteammanager.ui.myteam.teamdatafragments.TeamMember;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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


public class MemberToShowViewHolder extends BaseViewHolder<MemberToShowModel> {

    public static class Factory extends BaseViewHolder.Factory { //繼承大工廠的小工廠

        public Factory(@Nullable ClickFuncBuilder clickFuncBuilder) {
            super(clickFuncBuilder);
        }

        @NonNull
        public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.viewholder_memberlist, parent, false);
            return new MemberToShowViewHolder(view, clickFuncMap);
        }

        @Override
        public String getType() {
            return MemberToShowModel.TYPE;
        }

    }

    ImageView coverImage;
    Button setManager;
    Button deleteMember;

    TextView userName;
    TextView userNumber;
    TextView userRole;

    protected MemberToShowViewHolder(View itemView, final Map<Integer, ClickHandler> clickHandlerMap) {

        super(itemView);
        coverImage=itemView.findViewById(R.id.show_member_iv_photo);
        setManager=itemView.findViewById(R.id.btn_setManager);
        deleteMember= itemView.findViewById(R.id.btn_kickMember);


        userName = itemView.findViewById(R.id.tv_show_player_name);
        userNumber = itemView.findViewById(R.id.tv_show_uniformNumber);
        userRole = itemView.findViewById(R.id.tv_show_player_role);



        setManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickHandlerMap.get(v.getId()).onClick(getLayoutPosition()); //會回傳他是第幾個ViewHolder


            }
        });
        deleteMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickHandlerMap.get(v.getId()).onClick(getLayoutPosition()); //會回傳他是第幾個ViewHolder


            }
        });

    }

    @Override
    public void bind(MemberToShowModel mts) {
        if(mts.getUserId()== Global.CURRENTUSER_ID && !mts.getRole().equals("is member")){
            setManager.setVisibility(View.INVISIBLE);
            deleteMember.setVisibility(View.INVISIBLE);
        }
        Picasso.get().load(mts.getUserPhoto())
                .placeholder(R.drawable.tofu)
                .into(coverImage);
        if(mts.getRole().equals("is member")){
            setManager.setText("設為管理員");
        }else{
            setManager.setText("卸除管理員");
        }

        userName.setText(mts.getUserName());
//        userNumber.setText(""+mts.getUniformNumber());
        if(mts.getRole().equals("is member")){
            userRole.setText("隊員");
        }else {
            userRole.setText("管理員");
        }
    }


}