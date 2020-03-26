package com.app.myteammanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.myteammanager.utils.Global;
import com.app.myteammanager.utils.ImageUploadController;
import com.app.myteammanager.utils.Logger;
import com.app.myteammanager.utils.NetworkController;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import java.io.InputStream;


public class AddTeamActivity extends AppCompatActivity {


    private static final int CHOOSE_PHOTO = 1;
    public final static String TAG = "AddTeamActivity";
    ImageView add_image;
    EditText et_teamName;
    EditText et_teamIntro;
    Button btnAddTeam;
    Button btnCancel;
    File tmpFile;
    Boolean newPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team);
        init();
        initPhoto();
        setListener();
        newPhoto = false;
    }

    private void init() {

        add_image = findViewById(R.id.member_iv_photo);
        et_teamName = findViewById(R.id.addteam_dialog_et_name);
        et_teamIntro = findViewById(R.id.addteam_dialog_et_intro);
        btnAddTeam = findViewById(R.id.addteam_dialog_btn_create);
        btnCancel = findViewById(R.id.addteam_dialog_btn_cancel);

    }

    private void initPhoto() {
        Uri uri = ImageUploadController.getInstance().resourceIdToUri(this, R.drawable.baseball);
        ContentResolver cr = this.getContentResolver();
        InputStream inputStream = null;
        try {
            inputStream = cr.openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        tmpFile = ImageUploadController.getInstance().compressImage(bitmap);
    }

    private void setListener() {

        checkPermission(add_image);//設置圖片監聽事件

        btnAddTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String addTeamName = et_teamName.getText().toString();
                String addTeamIntro = et_teamIntro.getText().toString();
                if (!inputCheck(et_teamName, et_teamIntro)) {
                    return;
                }
                uploadGroupWithImage(tmpFile, Global.CURRENTUSER_TOKEN, addTeamName, "19~22", addTeamIntro);
                Logger.d(TAG, "創立隊伍:" + addTeamName + "/" + "隊伍介紹:" + addTeamIntro);
                setResult(9);
                finish();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    //檢查許可權
    public void checkPermission(ImageView image) {
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(AddTeamActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddTeamActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
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
                    Toast.makeText(this, "授權失敗，無法操作", Toast.LENGTH_SHORT).show();
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
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, "選擇照片", Toast.LENGTH_SHORT).show();
                    Uri uri = data.getData(); //把data中相片的uri取出
//                    try {
                        Picasso.get().load(uri).into(add_image);
                        ContentResolver cr = this.getContentResolver();
//                        InputStream inputStream = cr.openInputStream(uri);
                        System.out.println(uri.toString());
                        tmpFile = ImageUploadController.getInstance().uriToFile(this,uri);
//                        //開啟檔案路徑, 讀取檔案, 轉成inputStream(輸入串流)型態
//                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                        //透過inputStream輸入串流, 將檔案轉成bitmap
//                        add_image.setImageBitmap(bitmap);
//                        tmpFile = compressImage(bitmap);
                        newPhoto = true;

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

    private void uploadGroupWithImage(File file, String token, String groupName, String age, String groupIntro) {
        if (!newPhoto) {
            NetworkController.getInstance().postGroupWithoutImage(Global.API_ADD_GROUP
                    , token, groupName, age, groupIntro, new NetworkController.CCallback() {
                        @Override
                        public void onFailure(String errorMsg) {
                            System.out.println("上傳無照片團體失敗" + errorMsg);
                        }

                        @Override
                        public void onResponse(JSONObject data) throws JSONException {
                            System.out.println("上傳無照片團體成功");

                        }

                        @Override
                        public void onCompleted() {

                        }
                    });
        } else {
            NetworkController.getInstance().postGroupNewImage(Global.API_ADD_GROUP, file
                    , token, groupName, age, groupIntro, new NetworkController.CCallback() {
                        @Override
                        public void onFailure(String errorMsg) {
                            System.out.println("上傳新照片失敗" + errorMsg);
                        }

                        @Override
                        public void onResponse(JSONObject data) throws JSONException {
                            System.out.println("上傳成功");

                        }

                        @Override
                        public void onCompleted() {

                        }
                    });
        }
    }

    private boolean inputCheck(EditText inputOne, EditText inputTwo) {
        if (inputOne.getText().toString().equals("") ||
                inputTwo.getText().toString().equals("")) {
            return false;
        }
        return true;
    }

    private void jumpMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
