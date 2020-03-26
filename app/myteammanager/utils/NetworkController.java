package com.app.myteammanager.utils;



import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkController {

    private static final String TAG = "NetworkController";
    private static NetworkController networkController;
    private OkHttpClient client;
    private Map <String,Call> callMap;
    private static final MediaType MEDIA_TYPE_PNG =
            MediaType.parse( "image/png");
    private static final MediaType MEDIA_TYPE_JPG =
            MediaType.parse( "image/jpeg");
    public static final MediaType FORM = MediaType.parse("multipart/form-data");


    private NetworkController() {

            callMap = new HashMap<>();
            client = new OkHttpClient();
    }

    public static NetworkController getInstance() {
        if (networkController == null) {
            networkController = new NetworkController();
        }
        return networkController;
    }

    private static class CallbackAdapter implements Callback {

        private CCallback cCallback;

        public CallbackAdapter(CCallback cCallback) {
            this.cCallback = cCallback;
        }

        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            cCallback.onFailure(e.getMessage());
            cCallback.onCompleted();
        }


        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            String str = response.body().string();
            System.out.println("回傳!!!!!!!=" + str);
            try {
                JSONObject jsonObject = new JSONObject(str);
                System.out.println("TRYRTYRYRYR!!!!");
                if (!jsonObject.getString("errorcode").equals("-1")) {
                    System.out.println("TRYRTYRYRYR11111!!!!");
                    cCallback.onFailure(jsonObject.getString("msg"));
                } else {
                    System.out.println("TRYRTYRYRYR2222!!!!");
                    cCallback.onResponse(jsonObject);
                }
            } catch (JSONException e) {
                System.out.println("抓到例外!!!!");
                cCallback.onFailure(e.getMessage());
            } finally {
                cCallback.onCompleted();//客製加入的功能
            }
        }
    }

    public interface CCallback {
        void onFailure(String errorMsg);//偷來的方法
        void onResponse(JSONObject data) throws JSONException; //偷來的方法
        void onCompleted(); // 橋接的目的方法(額外新增)
    }

    public void get(String ApiCommand, String postData, CCallback callback){
        String dataUrl = ApiCommand + postData;
        Request request = new Request.Builder()
                .url(dataUrl)
                .get()
                .build();
        Call call = client.newCall(request);
        call.enqueue(new CallbackAdapter(callback));
        return;
    }

    public void delete(String ApiCommand,  CCallback callback){
        Request request = new Request.Builder()
                .url(ApiCommand)
                .delete()
                .build();
        Call call = client.newCall(request);
        call.enqueue(new CallbackAdapter(callback));
        return;
    }

    public void post(String ApiCommand, String postData, CCallback callback){
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = FormBody.create(postData, mediaType);
        Request request = new Request.Builder()
                .url(ApiCommand)
                .post(requestBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new CallbackAdapter(callback));
        return ;
    }

    public void postGroupWithoutImage(String ApiCommand, String token, String groupName
            , String age, String groupIntro , CCallback callback){

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody. FORM)
                .addFormDataPart("Token", token)
                .addFormDataPart("GroupIntro",groupIntro)
                .addFormDataPart("Age",age)
                .addFormDataPart("GroupName",groupName)

                .build();
        Request request = new Request.Builder()
                .url(ApiCommand)
                .post(requestBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new CallbackAdapter(callback));
        return;
    }

    public void postRegWithImage(String ApiCommand, String email, String password
            , String userName, File file , CCallback callback){
        System.out.println("註冊新照片並上傳= "+file );
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody. FORM)
                .addFormDataPart("Email", email)
                .addFormDataPart("Password",password)
                .addFormDataPart("UserName",userName)
                .addFormDataPart("UserPhoto",userName+".jpg"
                        , RequestBody.create (file , MediaType.parse("image/png")))

                .build();
        Request request = new Request.Builder()
                .url(ApiCommand)
                .post(requestBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new CallbackAdapter(callback));
        return;
    }

    public void postRegWithoutImage(String ApiCommand, String email, String password
            , String userName , CCallback callback){

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody. FORM)
                .addFormDataPart("Email", email)
                .addFormDataPart("Password",password)
                .addFormDataPart("UserName",userName)

                .build();
        Request request = new Request.Builder()
                .url(ApiCommand)
                .post(requestBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new CallbackAdapter(callback));
        return;
    }

    public void postGroupNewImage(String ApiCommand, File file, String token, String groupName
            , String age, String groupIntro , CCallback callback){

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody. FORM)
                .addFormDataPart("Token", token)
                .addFormDataPart("GroupIntro",groupIntro)
                .addFormDataPart("Age",age)
                .addFormDataPart("GroupName",groupName)
                .addFormDataPart("GroupPhoto", groupName+".jpg"
                        , RequestBody.create (file , MediaType.parse("image/png")))
                .build();
        Request request = new Request.Builder()
                .url(ApiCommand)
                .post(requestBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new CallbackAdapter(callback));
        return;

    }
    public void postProfilePhoto(String ApiCommand, File file ,String token, CCallback callback){

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody. FORM)
                .addFormDataPart("Token", token)
                .addFormDataPart("Photo", "profile.jpg"
                        , RequestBody.create (file , MediaType.parse("image/png")))
                .build();
        Request request = new Request.Builder()
                .url(ApiCommand)
                .post(requestBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new CallbackAdapter(callback));
        return;

    }


}


//    public void Login(CCallback callback){
//        RequestBody body = new FormBody.Builder() //建立body
//                .add("Email","frank24@gmail.com")
//                .add("Password", "123er")
//                .build();
//        Request request = new Request.Builder()
//                .url(URL_LOGIN)
//                .post(body)//送出post的請求讓js執行doPost後抓command值(get)，js執行get;
//                .build();
//        client.newCall(request).enqueue(new CallbackAdapter(callback));//建立一個request請求、enqueue就是傳到一個佇列，OKhttp偵測到有訊息就回傳入
//
//    }//newCall有一些方法 execute()同執行緒網路連線(報錯) cancel()取消 enqueue佇列執行
//     //callback負責回傳失敗跟成功的訊息



