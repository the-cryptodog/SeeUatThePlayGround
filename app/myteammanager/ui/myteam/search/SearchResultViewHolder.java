package com.app.myteammanager.ui.myteam.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.myteammanager.R;
import com.app.myteammanager.recycleview.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class SearchResultViewHolder extends BaseViewHolder<SearchResultModel> {

public static class Factory extends BaseViewHolder.Factory{ //繼承大工廠的小工廠

    public Factory(@Nullable BaseViewHolder.ClickFuncBuilder clickFuncBuilder) {
        super(clickFuncBuilder);
    }

    @NonNull
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewholder_searchresult, parent, false);
        return new SearchResultViewHolder(view,clickFuncMap);
    }
    @Override
    public String getType() {
        return SearchResultModel.TYPE;
    }
}
    TextView groupName;
    TextView groupIntro;
    TextView alreadyjoined;

    ImageView img_searched_Group;
    FrameLayout frameLayout;
    Button btn_search_join;


    protected SearchResultViewHolder(View itemView, final Map<Integer, BaseViewHolder.ClickHandler> clickHandlerMap) {
        super(itemView);

        img_searched_Group =itemView.findViewById(R.id.image_search_photo);
        groupName = itemView.findViewById(R.id.tv_search_name);
        groupIntro = itemView.findViewById(R.id.tv_search_intro);

        frameLayout = itemView.findViewById(R.id.framelayout_search_join);
        btn_search_join = itemView.findViewById(R.id.btn_search_join);
        alreadyjoined = itemView.findViewById(R.id.tv_alreadyjoined);
        alreadyjoined.setVisibility(View.INVISIBLE);

        btn_search_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickHandlerMap.get(v.getId()).onClick(getLayoutPosition()); //會回傳他是第幾個ViewHolder
                alreadyjoined.setVisibility(View.VISIBLE);
                System.out.println("申請!!!");
                frameLayout.removeView(btn_search_join);

            }
        });
    }

    @Override
    public void bind(SearchResultModel searchResultModel) {
        groupName.setText(searchResultModel.getGroupName());
        if(searchResultModel.getGroupIntro().equals("no groupIntro")){
            groupIntro.setText("");
        }else {
            groupIntro.setText(searchResultModel.getGroupIntro());
        }
//        groupPhoto.setText(searchResultModel.getGroupPhoto());
        Picasso.get()
                .load(searchResultModel.getImageURL())
                .placeholder(R.drawable.baseball)
                .into(img_searched_Group);

        System.out.println("社團狀態!!!!="+ searchResultModel.getStatus());
        switch (searchResultModel.getStatus()) {
            case "isMember":
                frameLayout.removeView(btn_search_join);
                alreadyjoined.setText(R.string.alreadyJoined);
                alreadyjoined.setVisibility(View.VISIBLE);
                break;

            case "alreadySend":
                frameLayout.removeView(btn_search_join);
                alreadyjoined.setText(R.string.alreadyApplied);
                alreadyjoined.setVisibility(View.VISIBLE);
        }
    }
}
