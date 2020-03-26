package com.app.myteammanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.app.myteammanager.ui.myteam.teamdatafragments.TeamPagerAdapter;
import com.app.myteammanager.ui.myteam.teamlist.TeamModel;
import com.google.android.material.tabs.TabLayout;

public class TeamActivity extends AppCompatActivity {

    public final static String TAG = "TeamActivity";
    TeamPagerAdapter teamPagerAdapter;
    private Toolbar addTeamToolbar;
    ViewPager viewPager;
    private static Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teamboard);

        bundle = this.getIntent().getExtras(); //攜帶隊伍資料

        //建立PagerAdapter
        teamPagerAdapter = new TeamPagerAdapter(getSupportFragmentManager(), bundle);

        //把PagerAdapter設定給ViewPager
        viewPager = findViewById(R.id.team_viewPager);
        viewPager.setAdapter(teamPagerAdapter);

        //把ViewPager設定給TabLayout
        //setupWithViewPager(viewPager)這個方法會讓tabLayout去偵測FragmentAdapter裡面的getCount()並依據來增加tab頁籤
        TabLayout tabLayout = findViewById(R.id.team_tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("成員");
        tabLayout.getTabAt(1).setText("賽程");
        tabLayout.getTabAt(2).setText("總務");
        viewPager.setCurrentItem(1);
        setToolbar();
    }

    //取消新增總務紀錄時可以回到總務的FRAGMENT
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("轉跳號碼為"+requestCode+"/"+resultCode);
        if(requestCode ==RESULT_OK){
            viewPager.setCurrentItem(1);
        }
    }

    private void setToolbar (){
        addTeamToolbar = findViewById(R.id.teammember_toolbar);
//        addTeamToolbar.inflateMenu(R.menu.add_teammate_menu);
//        addTeamToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch(item.getItemId()){
//
//                }
//                return true;
//            }
//        });
    }



}
