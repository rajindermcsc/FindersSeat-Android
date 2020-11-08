package com.websoftquality.findersseat.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.websoftquality.findersseat.views.main.SignupHeightActivity.RequestPermissionCode;

/**
 * Created by dss04 on 26/7/17.
 */
public class RequiredPermissions
{
    Activity activity;
    public RequiredPermissions(Activity activity)
    {
        this.activity = activity;
    }
    public boolean checkWritePermission() {
        int permissionReadWrite = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionReadWrite != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= 23) {
                activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 4);
                return false;
            }else {
                return true;
            }

        }
        else
        {
            return true;
        }
    }
    public boolean Camera_storage_PermissionIsEnabledOrNot()
    {
        int FirstPermissionResult = ContextCompat.checkSelfPermission(activity, CAMERA);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(activity, READ_EXTERNAL_STORAGE);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(activity, WRITE_EXTERNAL_STORAGE);
        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED ;
    }
    public void RequestMultiplePermission_Camera_storage_PermissionIsEnabledOrNot() {

        ActivityCompat.requestPermissions(activity, new String[]
                {
                        CAMERA,
                        READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE
                }, RequestPermissionCode);

    }

}