package com.app.myteammanager.ui.login;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.myteammanager.R;
import com.app.myteammanager.ui.login.data.LoginRepository;
import com.app.myteammanager.ui.login.data.Result;
import com.app.myteammanager.ui.login.data.model.LoggedInUser;
import com.app.myteammanager.utils.Global;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();//登入權限
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();//登入結果
    private LoginRepository loginRepository;//登入資料庫

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    } //回傳登入者權限

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }//回傳登入結果

    public void login(String username, String password) { //登入入口一
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = loginRepository.login(username, password);
        //會呼叫loginRepository的login
        //loginRepository的login會再呼叫LoginDataSource的login
        // 並等待回傳的result(Result.success)過Result.error
        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            //當回傳的Result屬於Success這個類

            Global.CURRENTUSER_TOKEN = data.getUserToken();
            //設置TOKEN全域變數

            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));

        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 2;
    }
}
