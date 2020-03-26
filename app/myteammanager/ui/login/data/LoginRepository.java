package com.app.myteammanager.ui.login.data;

import com.app.myteammanager.ui.login.data.model.LoggedInUser;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {
    //這個類紀錄登入狀況(login)+呼叫LoginIn或者out並呼叫LoginDataSource儲存判斷傳進來的登入資料
    private static volatile LoginRepository instance; //單例

    private LoginDataSource dataSource;//LoginDataSource儲存傳進來的登入資料

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null; //預設登入者

    // private constructor : singleton access
    private LoginRepository(LoginDataSource dataSource) { //傳入一個初始LoginDataSource
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) { //單例
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return (user != null);
    }

    public void logout() {
        user = null;
        dataSource.logout();
    }

    private void setLoggedInUser(LoggedInUser user) { //紀錄目前使用者
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public Result<LoggedInUser> login(String username, String password) {
        //呼叫LoginDataSource檢查登入資料，接收LoginDataSource傳回來的RESULT並往外傳送
        // handle login
        Result<LoggedInUser> result = dataSource.login(username, password);
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        }
        return result;
    }
}
