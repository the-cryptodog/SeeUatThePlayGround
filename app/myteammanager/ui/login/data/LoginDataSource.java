package com.app.myteammanager.ui.login.data;

import com.app.myteammanager.ui.login.data.model.LoggedInUser;
import com.app.myteammanager.utils.Global;
import com.app.myteammanager.utils.NetworkController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    //這個類只有一個方法，就是被LoginRepository呼叫登入或登出
    //它會依照使用者的登入資料來決定告訴外層登入者的資料是什麼
    NetworkController networkController = NetworkController.getInstance();

    private Result result;
    private boolean isResponsed;

    public Result<LoggedInUser> login(String Email, String password) {

        System.out.println(Email);
        System.out.println(password);
        isResponsed = false;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Email", Email)
                      .put("Password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        networkController.post(Global.API_LOGIN, jsonObject.toString()
                , new NetworkController.CCallback() {
                    @Override
                    public void onFailure(String errorMsg) {
                        System.out.println("失敗!!!!"+errorMsg);
                        result =  new Result.Error(new IOException("errorMsg"));
                        isResponsed = true;
                    }
                    @Override
                    public void onResponse(JSONObject data) throws JSONException {
                        System.out.println("REPONSED成功");
                        String tokenGiven = data.getString("token");
                        int userId = data.getInt("userId");
                        Global.CURRENTUSER_ID = userId;
                        System.out.println("參數擷取成功");
                        LoggedInUser tmpUser = new LoggedInUser(
                                        java.util.UUID.randomUUID().toString(),
                                tokenGiven+"name");
                        tmpUser.setUserToken(tokenGiven);
                        System.out.println("成功!!!!="+tmpUser.getDisplayName());
                        result = new Result.Success<>(tmpUser);
                        isResponsed = true;
                        int count=0;
                    }
                    @Override
                    public void onCompleted() {

                    }
                });
        //這一段是等待網路回應

            while(true){
                if(isResponsed){
                    System.out.println("沒有卡住!!!!");
                    break;
                }
                System.out.println("卡住!!!!");
            }

        return result;
    }
    public void logout() {
        // TODO: revoke authentication
    }

}
