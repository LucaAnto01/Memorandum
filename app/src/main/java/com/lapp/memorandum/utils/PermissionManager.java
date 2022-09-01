package com.lapp.memorandum.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

/**
 * Class to manage the permission
 */
public class PermissionManager
{
    private Context context;

    /**
     * Constructor Method
     */
    public PermissionManager() {}

    /**
     * Method to set the context --> init the PermissionManger
     * @param context
     */
    public void init(Context context)
    {
        this.context = context;
    }

    /**
     * Method to check a list of permissions
     * @param permissions
     * @return
     */
    public boolean checkPermissions(String[] permissions)
    {
        int numberElements = permissions.length;

        for(int i = 0; i < numberElements; i++)
        {
            if(ContextCompat.checkSelfPermission(context, permissions[i]) == PermissionChecker.PERMISSION_DENIED)
                return false; //No i-permission
        }

        return true; //Have all permission
    }

    /**
     * Method to ask permissions
     * @param activity
     * @param permissions
     * @param requestCode
     */
    public void askPermission(Activity activity, String[] permissions, int requestCode)
    {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    /*private boolean checkPermissionResult(Activity activity, int requestCode, String[] permissions, int[] grantResults)
    {
        boolean isAllOk = true; //Have all permissions

        if(grantResults.length > 0)
        {
            int numberElements = grantResults.length;

            for(int i = 0; i < numberElements; i++)
            {
                if(grantResults[i] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(activity, "Permission granted", Toast.LENGTH_SHORT).show();

                else
                {
                    isAllOk = false;
                    Toast.makeText(activity, "Permission denied", Toast.LENGTH_SHORT).show();
                    showPermissionRational(activity, requestCode, permissions, permissions[i]);
                    break;
                }
            }
        }

        else isAllOk = false;

        return isAllOk;
    }*/

    /**
     * Method to re-ask the permission after denied
     * @param activity
     * @param requestCode
     * @param permissions
     * @param deniedPermission
     */
    private void showPermissionRational(Activity activity, int requestCode, String[] permissions, String deniedPermission)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity, deniedPermission))
            {
                showMessageOkCancel("You need to allow access to the permission!", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which)
                    {
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                            askPermission(activity, permissions, requestCode);
                    }
                });
                return;
            }
        }
    }

    /**
     * Method to show an alert dialog with OK or Cancel option
     * @param message
     * @param onClickListener
     */
    private void showMessageOkCancel(String message, DialogInterface.OnClickListener onClickListener)
    {
        new AlertDialog.Builder(context).setMessage(message).setPositiveButton("Ok", onClickListener).setNegativeButton("Cancel", onClickListener)
            .create().show();
    }
}
