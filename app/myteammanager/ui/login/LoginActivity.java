package com.app.myteammanager.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.app.myteammanager.MainActivity;
import com.app.myteammanager.R;
import com.app.myteammanager.RegActivity;
import com.app.myteammanager.ui.login.data.LoginRepository;
import com.app.myteammanager.utils.Global;
import com.app.myteammanager.utils.Logger;


public class LoginActivity extends AppCompatActivity {


    private LoginViewModel loginViewModel;
    private static final String TAG = "LoginActivity";
    private static final int FUNC_REG = 6;
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        //四個元件綁定
        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.btn_login);
        final Button btn_register = findViewById(R.id.btn_reg);


        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    jumpToMain();
                    updateUiWithUser(loginResult.getSuccess());
                    Logger.d(TAG,"轉跳到Main");
                    Logger.d(TAG,loginResult.getSuccess().getDisplayName());//登入成功印出使用者名字
                }
                setResult(Activity.RESULT_OK);
                //Complete and destroy login activity once successful
//                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToRegActivity();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == FUNC_REG && resultCode == RESULT_OK){
            data = getIntent();
            Bundle bundle = data.getExtras();
            String regEmail = bundle.getString("Email");
            String regPassword = bundle.getString("Password");
            Logger.d(TAG,"註冊回傳的EMAIL為"+regEmail);
            Logger.d(TAG,"註冊回傳的密碼為"+regPassword);
            loginViewModel.login(regEmail,regPassword);
        }
    }


    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        Logger.d(TAG,"轉跳到Main");
        // TODO : initiate successful logged in experience
//        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void jumpToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        //製作附加資料
        Bundle bundle = new Bundle();
        //附加到intent上
        intent.putExtras(bundle);
        startActivity(intent);
//        finish();// 上一頁變成結束程式
    }

    private void jumpToRegActivity() { //註冊頁面請求代號3
        Intent intent = new Intent(this, RegActivity.class);
        //製作附加資料
        Bundle bundle = new Bundle();
        //附加到intent上
        intent.putExtras(bundle);
        startActivityForResult(intent,FUNC_REG);

    }


}
