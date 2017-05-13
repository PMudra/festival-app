package com.example.drachim.festivalapp.common;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.support.v13.app.FragmentCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dr. Achim on 09.05.2017.
 */

public class Utilities {

    public static void strikeThru(TextView view, boolean strikeThru) {
        if (strikeThru) {
            view.setPaintFlags(view.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            view.setPaintFlags(view.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }

    /**
     * Check for permissions, request them if they're not granted.
     *
     * @return true if permissions are already granted, else request them and return false.
     */
    private static boolean checkAndRequestPermissions(Activity activity, int requestCode, String[] permissionList) {
        List<String> toRequest = new ArrayList<>();
        for (String permission : permissionList) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                toRequest.add(permission);
            }
        }
        if (toRequest.size() > 0) {
            String[] requestedPermissions = toRequest.toArray(new String[toRequest.size()]);
            ActivityCompat.requestPermissions(activity, requestedPermissions, requestCode);
            return false;
        }
        return true;
    }

    /**
     * Check for permissions, request them if they're not granted.
     *
     * @return true if permissions are already granted, else request them and return false.
     */
    private static boolean checkAndRequestPermissions(Fragment fragment, int requestCode, String[] permissionList) {
        List<String> toRequest = new ArrayList<>();
        for (String permission : permissionList) {
            Context context = fragment.getActivity();
            if (context != null && ContextCompat.checkSelfPermission(context, permission) != PackageManager
                    .PERMISSION_GRANTED) {
                toRequest.add(permission);
            }
        }
        if (toRequest.size() > 0) {
            String[] requestedPermissions = toRequest.toArray(new String[toRequest.size()]);
            FragmentCompat.requestPermissions(fragment, requestedPermissions, requestCode);
            return false;
        }
        return true;
    }

    /**
     * Check for permissions without requesting them
     *
     * @return true if all permissions are granted
     */
    private static boolean checkPermissions(Activity activity, String[] permissionList) {
        for (String permission : permissionList) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkAndRequestReadContactsPermission(Fragment fragment, int requestCode) {
        return checkAndRequestPermissions(fragment, requestCode, new String[]{Manifest.permission.READ_CONTACTS});
    }

    public static boolean checkReadContactsPermission(Activity activity) {
        return checkPermissions(activity, new String[]{Manifest.permission.READ_CONTACTS});
    }

}
