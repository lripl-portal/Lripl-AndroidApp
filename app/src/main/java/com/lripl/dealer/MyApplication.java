package com.lripl.dealer;


import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class MyApplication extends Application  {
    public static int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    public static final int REQUEST_RECORD_AUDIO = 13;
    public static final int REQUEST_CORE_LOCATION = 14;
    public static final int REQUEST_FINE_LOCATION = 15;
    public static final int REQUEST_FINE_LOCATION_FROM_CAMERA_SETTINGS = 16;
    public static final int GRANTED = 0;
    public static final int DENIED = 1;
    public static final int BLOCKED_OR_NEVER_ASKED = 2;
    public static MyApplication get(Context context) {
        return (MyApplication) context.getApplicationContext();
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static void requestWriteExternalStoragePermission(final Context context) {
        if (ContextCompat.checkSelfPermission(((Activity) context), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(((Activity) context), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    public static void requestCameraPermission(final Context context) {
        if (ContextCompat.checkSelfPermission(((Activity) context), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(((Activity) context), new String[]{Manifest.permission.CAMERA}, 0);
        }
    }


}
