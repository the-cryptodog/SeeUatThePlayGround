package com.app.myteammanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.myteammanager.ui.login.LoginActivity;
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

public class RegActivity extends AppCompatActivity {

    public final static String TAG = "RegActivity";
    private static final int CHOOSE_PHOTO = 1;
    private static final int NEW_PHOTO = 2;
    private EditText regName;
    private EditText regEmail;
    private EditText regPassword;
    private EditText regConfirmedPassword;
    private Button btn_Reg;
    private Button btn_Cancel;
    private ImageView imageReg;
    private File tmpFile;
    private Boolean newPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        init();
        initPhoto();
        setItemListener();
    }

    private void init(){
        imageReg = findViewById(R.id.imageReg);
        Picasso.get()
                .load(R.drawable.logo)
                .into(imageReg);
        regName = findViewById(R.id.reg_et_name);
        regPassword = findViewById(R.id.reg_et_password);
        regConfirmedPassword = findViewById(R.id.reg_et_confirmedPassword);
        regEmail = findViewById(R.id.reg_et_Email);
        btn_Reg = findViewById(R.id.reg_btn_reg);
        btn_Cancel = findViewById(R.id.reg_btn_cancel);
        newPhoto= false;

    }

    private void initPhoto() { //將預設照片壓縮成檔案
        Uri uri = ImageUploadController.getInstance().resourceIdToUri(this, R.drawable.logo);
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

    private void setItemListener(){

        checkPermission(imageReg);//設置圖片監聽事件

        btn_Reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String inputRegName = regName.getText().toString();
                final String inputPassword = regPassword.getText().toString();
                final String inputConfirmPassword = regConfirmedPassword.getText().toString();
                final String inputEmail = regEmail.getText().toString();

                if(!inputPassword.equals(inputConfirmPassword) ||
                    !inputCheck(regName,regPassword,regEmail)){
                    return;
                }

                if (!inputEmail.contains("@") || !inputEmail.contains(".")) {
                    Toast.makeText(RegActivity.this,"請輸入有效的電子郵件",Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("UserName", inputRegName)
                            .put("Password", inputPassword)
                            .put("Email",inputEmail);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (!newPhoto) {
                    NetworkController.getInstance().postRegWithoutImage(Global.API_REGISTER
                            , inputEmail,inputPassword,inputRegName
                            , new NetworkController.CCallback() {
                                @Override
                                public void onFailure(String errorMsg) {
                                    System.out.println("註冊失敗，請檢查輸入資料是否正確");
                                }

                                @Override
                                public void onResponse(JSONObject data) throws JSONException {
                                    Logger.d(TAG, "註冊結果:" + data.getString("msg"));
                                    System.out.println("註冊成功");

                                    //製作附加資料傳回LOGIN畫面
                                    Bundle bundle = new Bundle();
                                    bundle.putString("Email", inputEmail);
                                    bundle.putString("Password", inputPassword);

                                    regThenLoginIn(bundle);
                                }

                                @Override
                                public void onCompleted() {

                                }
                            });
                }
                if (newPhoto){

                    NetworkController.getInstance().postRegWithImage(Global.API_REGISTER,
                                inputEmail,inputPassword,inputRegName,tmpFile

                            , new NetworkController.CCallback() {
                                @Override
                                public void onFailure(String errorMsg) {
                                    System.out.println("註冊失敗，請檢查輸入資料是否正確");
                                }

                                @Override
                                public void onResponse(JSONObject data) throws JSONException {
                                    Logger.d(TAG, "註冊結果:" + data.getString("msg"));
                                    System.out.println("註冊成功");

                                    //製作附加資料傳回LOGIN畫面
                                    Bundle bundle = new Bundle();
                                    bundle.putString("Email", inputEmail);
                                    bundle.putString("Password", inputPassword);

                                    regThenLoginIn(bundle);
                                }

                                @Override
                                public void onCompleted() {

                                }
                            });
                }

            }
        });

        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelThenLoginIn();
            }
        });
    }

    public void checkPermission(ImageView image) {
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(RegActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(RegActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
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
                    Picasso.get().load(uri).into(imageReg);
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

    private boolean inputCheck(EditText inputOne, EditText inputTwo, EditText inputThree) {
        if (inputOne.getText().toString().equals("") ||
                inputTwo.getText().toString().equals("")||
                inputThree.getText().toString().equals("")){
            return false;
        }
        return true;
    }

    private void regThenLoginIn(Bundle bundle) {
        Intent intent = new Intent(this, LoginActivity.class);
        //附加到intent上
        intent.putExtras(bundle);
        setResult(RESULT_OK);
        startActivityForResult(intent, NEW_PHOTO);
        finish();
    }

    private void cancelThenLoginIn() {
        finish();
    }


}
