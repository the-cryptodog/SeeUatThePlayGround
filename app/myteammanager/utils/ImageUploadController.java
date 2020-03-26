package com.app.myteammanager.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageUploadController {

    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String FORWARD_SLASH = "/";

    private static ImageUploadController imageUploadController;

    private ImageUploadController() {

    }

    public static ImageUploadController getInstance() {
        if (imageUploadController == null) {
            imageUploadController = new ImageUploadController();
        }
        return imageUploadController;
    }

    public Uri resourceIdToUri(Context context, int resourceId) {
        System.out.println("上傳路徑為" + ANDROID_RESOURCE + context.getPackageName() + FORWARD_SLASH + resourceId);
        return Uri.parse(ANDROID_RESOURCE + context.getPackageName() + FORWARD_SLASH + resourceId);
    }

    public File uriToFile(Context context, Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor actualImageCursor =  context.getContentResolver().query(uri, proj, null, null, null);
        int actual_image_column_index = actualImageCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        actualImageCursor.moveToFirst();
        String img_path = actualImageCursor.getString(actual_image_column_index);
        File file = new File(img_path);
        return file;
    }

    public File compressImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//質量壓縮方法,這裡100表示不壓縮,把壓縮後的資料存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 500) {  //迴圈判斷如果壓縮後圖片是否大於500kb,大於繼續壓縮
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都減少10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//這裡壓縮options%,把壓縮後的資料存放到baos中
            long length = baos.toByteArray().length;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

        Date date = new Date(System.currentTimeMillis());

        File file = new File(Environment.getExternalStorageState(), date + ".jpg");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            try {
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return file;
    }
    public static File getFileFromMediaUri(Context ac, Uri uri) {
        if(uri.getScheme().toString().compareTo("content") == 0){
            ContentResolver cr = ac.getContentResolver();
            Cursor cursor = cr.query(uri, null, null, null, null);// 根據Uri從資料庫中找
            if (cursor != null) {
                cursor.moveToFirst();
                String filePath = cursor.getString(cursor.getColumnIndex("_data"));// 獲取圖片路徑
                cursor.close();
                if (filePath != null) {
                    return new File(filePath);
                }
            }
        }else if(uri.getScheme().toString().compareTo("file") == 0){
            return new File(uri.toString().replace("file://",""));
        }
        return null;
    }

}
