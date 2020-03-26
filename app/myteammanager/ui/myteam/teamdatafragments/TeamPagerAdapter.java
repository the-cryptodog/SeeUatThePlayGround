package com.app.myteammanager.ui.myteam.teamdatafragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.app.myteammanager.ui.myteam.teamdatafragments.Finance.FinanceFragment;
import com.app.myteammanager.ui.myteam.teamdatafragments.Game.GameFragment;
import com.app.myteammanager.ui.myteam.teamdatafragments.TeamMember.TeamMemberFragment;


public class TeamPagerAdapter extends FragmentPagerAdapter {

    Bundle bundle;

    public TeamPagerAdapter(FragmentManager fragmentManager, Bundle bundle){
        super(fragmentManager);
        this.bundle = bundle;

    }
    private final int FRAGMENT_SIZE = 3;

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;
        switch(position){
            case 0 : fragment = new TeamMemberFragment();
                System.out.println("建立成員片段");
                break;
            case 1 : fragment = new GameFragment();
                System.out.println("建立賽程片段");
                break;
            case 2 : fragment = new FinanceFragment();
                System.out.println("建立總務片段");

        }
        fragment.setArguments(bundle); //攜帶隊伍資料

        return fragment;
    }

    @Override
    public int getCount() {
        return FRAGMENT_SIZE; //這裡是頁面的總數
    }

}
