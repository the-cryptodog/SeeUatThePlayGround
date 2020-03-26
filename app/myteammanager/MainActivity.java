package com.app.myteammanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;

import com.app.myteammanager.ui.myteam.teamdatafragments.Finance.FinanceFragment;
import com.app.myteammanager.ui.myteam.teamlist.TeamListFragment;
import com.app.myteammanager.ui.notifications.Member.MemberFragment;
import com.app.myteammanager.ui.settings.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;



public class MainActivity extends AppCompatActivity {

    public final static String TAG = "MainActivity";
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nagvigation_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_person, R.id.navigation_myteam, R.id.navigation_notifications,R.id.navigation_settings)
                .build();

        navController = Navigation.findNavController(this, R.id.navigation_host_fragment);

//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( resultCode == 9){
            navController.navigate(R.id.navigation_myteam);
        }
    }
}
