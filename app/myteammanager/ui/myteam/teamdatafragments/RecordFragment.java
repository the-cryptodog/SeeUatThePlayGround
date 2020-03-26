package com.app.myteammanager.ui.myteam.teamdatafragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.app.myteammanager.R;

public class RecordFragment extends Fragment {

    public RecordFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.myteam_teamboard_fragment_recoed,container,false);

        return v;
    }
}
