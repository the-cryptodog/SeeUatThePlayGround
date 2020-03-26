package com.app.myteammanager.utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.List;

public class BackHandlerHelper {

    public static boolean handleBackPress(FragmentManager fragmentManager) {
        List<Fragment> fragments = fragmentManager.getFragments();

        if (fragments == null) return false;

        for (int i = fragments.size() - 1; i >= 0; i--) {
            Fragment child = fragments.get(i);
            if (isFragmentBackHandled(child)) {
                return true;
            }
        }
        if(fragmentManager.getBackStackEntryCount()>0){
            fragmentManager.popBackStack();
            return true;
        }
        return false;
    }
    public static boolean isFragmentBackHandled(Fragment fragment){
        return fragment != null;
    }
}
