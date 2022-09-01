package com.lapp.memorandum.utils;

import android.Manifest;
import android.content.Context;

import com.lapp.memorandum.models.Location;

public class MemoAppData
{
    private static String apiKey = "AIzaSyDoi0uX-_x-iFBQtlSZpeFnTRWAybdbM_E";
    private static final String[] permissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.INTERNET};
    private static PermissionManager permissionManager = null; //Default is null
    private static Location userLocation = null;

    public static String getApiKey()
    {
        return apiKey;
    }

    public static String[] getPermissions() { return permissions; }

    /**
     * Method to get the Permission manager
     * @param context
     * @return
     */
    public static PermissionManager getPermissionManager(Context context)
    {
        if(permissionManager == null)
            permissionManager = new PermissionManager();

        permissionManager.init(context);

        return permissionManager;
    }

    public static Location getUserLocation() { return userLocation; }

    public static void setUserLocation(Location userLocation) { MemoAppData.userLocation = userLocation; }
}
