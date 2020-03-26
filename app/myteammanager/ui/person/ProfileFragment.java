package com.app.myteammanager.ui.person;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.app.myteammanager.AddTeamActivity;
import com.app.myteammanager.MainActivity;
import com.app.myteammanager.R;
import com.app.myteammanager.ui.login.data.model.LoggedInUser;
import com.app.myteammanager.utils.BackHandlerHelper;
import com.app.myteammanager.utils.FragmentBackHandler;
import com.app.myteammanager.utils.Global;
import com.app.myteammanager.utils.ImageUploadController;
import com.app.myteammanager.utils.Logger;
import com.app.myteammanager.utils.NetworkController;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment implements FragmentBackHandler {


    TextView name, mail, lifeScore, scoreRate;
    CircleImageView profilePhoto;
    String userName;
    String userPhoto;
    String userMail;
    File tmpFile;
    int careerScore;
    double fieldGoal;

    private static final int CHOOSE_PHOTO = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_person, container, false);
        init(root);
        getProfileData();
        setItem();
        return root;
    }

    private void init(View root) {
        profilePhoto = root.findViewById(R.id.profile_iv_photo);
        name = root.findViewById(R.id.profile_iv_name);
        mail = root.findViewById(R.id.profile_iv_mail);
        lifeScore = root.findViewById(R.id.profile_iv_lifescore);
        scoreRate = root.findViewById(R.id.profile_iv_scoreRate);
        setListener();
    }

    private void getProfileData() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", Global.CURRENTUSER_TOKEN);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        NetworkController.getInstance().post(Global.API_GET_PROFILE, jsonObject.toString()
                , new NetworkController.CCallback() {
                    @Override
                    public void onFailure(String errorMsg) {
                        System.out.println("個人頁面錯誤" + errorMsg);
                    }

                    @Override
                    public void onResponse(JSONObject data) throws JSONException {
                        System.out.println("個人頁面成功");
                        JSONObject userInfo = data.getJSONObject("userInfo");


                        userName = userInfo.getString("userName");
                        userPhoto = userInfo.getString("userPhoto");
                        userMail = userInfo.getString("email");
                        careerScore = userInfo.getInt("careerPoint");
                        fieldGoal = userInfo.getDouble("fieldGoal");


                        System.out.println("個人資料導入成功");
                        System.out.println("userName=" + userName);
                        System.out.println("userMail=" + userMail);
                        System.out.println("careerScore=" + careerScore);
                        System.out.println("fieldGoal=" + fieldGoal);


                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                setItem();
                            }
                        });

                    }

                    @Override
                    public void onCompleted() {

                    }
                });


    }

    private void setItem() {

        name.setText(userName);
        mail.setText(userMail);
        lifeScore.setText("" + careerScore);
        scoreRate.setText("" + fieldGoal);
        Picasso.get().load(userPhoto).placeholder(R.drawable.logo).into(profilePhoto);

    }

    private void setListener() {

        checkPermission(profilePhoto);//設置圖片監聽事件

//        profilePhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(tmpFile!=null) {
//                    uploadGroupWithImage(tmpFile);
//                }
//            }
//        });

    }

    //檢查許可權
    public void checkPermission(ImageView image) {
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    //發現沒有許可權，呼叫requestPermissions方法向用戶申請許可權，requestPermissions接收三個引數
                    //第一個是context，第二個是一個String陣列，我們把要申請的許可權名放在陣列中即可，第三個是請求碼，只要是唯一值就行
                } else {
                    openAlbum();//有許可權就開啟相簿
                }
            }
        });
    }

    public void openAlbum() {
        //通過intent開啟相簿，使用startActivityForResult方法啟動actvity，會返回到onActivityResult方法，所以我們還得複寫onActivityResult方法
        //Intent intent = new Intent("android.intent.action.GET_CONTENT");//這是打開檔案總管的寫法
        //intent.setType("image/*");//類型
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//打開相簿
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    //彈出視窗向用戶申請許可權
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);//彈出授權的視窗，這條語句也可以刪除，沒有影響
        //獲得了使用者的授權結果，儲存在grantResults中，判斷grantResult中的結果來決定接下來的操作
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(getActivity(), "授權失敗，無法操作", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHOOSE_PHOTO:
                if (resultCode == getActivity().RESULT_OK) {
                    Toast.makeText(getActivity(), "選擇照片", Toast.LENGTH_SHORT).show();
                    Uri uri = data.getData(); //把data中相片的uri取出
//                    try {
                    Picasso.get().load(uri).into(profilePhoto);
//                    ContentResolver cr = getActivity().getContentResolver();
//                        InputStream inputStream = cr.openInputStream(uri);
                    System.out.println(uri.toString());
                    tmpFile = ImageUploadController.getInstance().uriToFile(getActivity(), uri);
                    uploadGroupWithImage(tmpFile);
//                        //開啟檔案路徑, 讀取檔案, 轉成inputStream(輸入串流)型態
//                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                        //透過inputStream輸入串流, 將檔案轉成bitmap
//                        add_image.setImageBitmap(bitmap);
//                        tmpFile = compressImage(bitmap);
                    //將Bitmap裁切後, 設定到ImageView
//                    } catch (FileNotFoundException e) {
//                        Log.e("Exception", e.getMessage(), e);
//                    }

                }
                break;
            default:
                break;
        }
    }

    private void uploadGroupWithImage(File file) {

        NetworkController.getInstance().postProfilePhoto(Global.API_UPLOAD_PHOTO, file
                , Global.CURRENTUSER_TOKEN, new NetworkController.CCallback() {
                    @Override
                    public void onFailure(String errorMsg) {
                        System.out.println("上傳新照片失敗" + errorMsg);
                    }

                    @Override
                    public void onResponse(JSONObject data) throws JSONException {
                        System.out.println("上傳Profile成功");

                    }

                    @Override
                    public void onCompleted() {

                    }
                });
    }


    private boolean inputCheck(EditText inputOne, EditText inputTwo) {
        if (inputOne.getText().toString().equals("") ||
                inputTwo.getText().toString().equals("")) {
            return false;
        }
        return true;
    }

    private void jumpMainActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onBackPressed() {
        return BackHandlerHelper.handleBackPress(getFragmentManager());
    }


}