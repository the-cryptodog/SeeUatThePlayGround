package com.app.myteammanager.ui.notifications;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.app.myteammanager.ui.notifications.Manager.ManagerFragment;
import com.app.myteammanager.ui.notifications.Member.MemberFragment;

public class NotificationsPagerAdapter extends FragmentPagerAdapter {

    public NotificationsPagerAdapter(FragmentManager fragmentManager){ super(fragmentManager);
    }
    private final int FRAGMENT_SIZE = 2;

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch(position){
            case 0 : fragment = new MemberFragment();
                break;
            case 1 : fragment = new ManagerFragment();

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return FRAGMENT_SIZE; //這裡是頁面的總數
    }
}
