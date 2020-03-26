package com.app.myteammanager.ui.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.app.myteammanager.R;
import com.app.myteammanager.ui.login.LoginActivity;
import com.app.myteammanager.ui.myteam.teamdatafragments.Game.NewRecord.InGamePlayersModel;
import com.app.myteammanager.utils.BackHandlerHelper;
import com.app.myteammanager.utils.FragmentBackHandler;

public class SettingsFragment extends Fragment implements FragmentBackHandler {

    private Toolbar addTeamToolbar;
    TextView logout;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_setting, container, false);
        setToolbar(root);
        logout=root.findViewById(R.id.tv_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmAlertDialog( );
            }
        });
        return root;
    }

    @Override
    public boolean onBackPressed() {
        return BackHandlerHelper.handleBackPress(getFragmentManager());
    }

    private void setToolbar (View root){
        addTeamToolbar = root.findViewById(R.id.teammember_toolbar);

    }
    private void jumpToLoginActivity() { //註冊頁面請求代號3
        Intent intent = new Intent(getActivity(),LoginActivity.class);
        startActivity(intent);
        getActivity().finish();

    }


    private void confirmAlertDialog( ) {
        final AlertDialog.Builder altDlgBuilder = new AlertDialog.Builder(getActivity());
        altDlgBuilder.setTitle(R.string.Warning);
        altDlgBuilder.setMessage(R.string.Are_you_sure);
        altDlgBuilder.setIcon(R.drawable.ic_error_outline_black_24dp);
        altDlgBuilder.setCancelable(true);
        altDlgBuilder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                jumpToLoginActivity();
                dialog.dismiss();

            }
        });
        altDlgBuilder.setNegativeButton("等等", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();


    }

}