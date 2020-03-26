package com.app.myteammanager.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.app.myteammanager.R;
import com.app.myteammanager.utils.Global;
import com.app.myteammanager.utils.NetworkController;
import com.google.android.material.tabs.TabLayout;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private NotificationsPagerAdapter notificationsPagerAdapter;
    private Toolbar notificationToolbar;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View fragmentView = inflater.inflate(R.layout.fragment_notifications, container, false);

        //建立PagerAdapter
        notificationsPagerAdapter = new NotificationsPagerAdapter(getChildFragmentManager());

        //把PagerAdapter設定給ViewPager
        ViewPager viewPager = fragmentView.findViewById(R.id.notification_viewPager);
        viewPager.setAdapter(notificationsPagerAdapter);

        //把ViewPager設定給TabLayout
        //setupWithViewPager(viewPager)這個方法會讓tabLayout去偵測FragmentAdapter裡面的getCount()並依據來增加tab頁籤
        TabLayout tabLayout = fragmentView.findViewById(R.id.notification_tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("個人通知");
        tabLayout.getTabAt(1).setText("管理員通知");

//        setToolbar (fragmentView);

        return fragmentView;
    }

//    private void setToolbar (View view){
//        notificationToolbar = view.findViewById(R.id.notification_toolbar);
////        notificationToolbar.inflateMenu(R.menu.notifiication_menu);
////        notificationToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
////            @Override
////            public boolean onMenuItemClick(MenuItem item) {
////                switch(item.getItemId()){
////                    case R.id.notificationOption:
////                        Toast.makeText(getActivity(),"點擊通知OPTION",Toast.LENGTH_SHORT).show();
////                }
////                return true;
////            }
////        });
//    }

}