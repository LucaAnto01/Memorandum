package com.lapp.memorandum.services;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.lapp.memorandum.ShowException;

/**
 * Class to manage Geofencing
 */
public class GeoFencingManaging extends ContextWrapper
{
    /*Attributes*/
    private Context context;
    private PendingIntent pendingIntent; //To execute operations with permissions
    private final float geoFenceRadius = 500; //Meters

    /**
     * Constructor method
     * @param base
     */
    public GeoFencingManaging(Context base)
    {
        super(base);
        this.context = base;
    }

    /**
     * Method to set a new GeoFence request, necessary to create a new GeoFence
     * @param geofence
     * @return
     */
    public GeofencingRequest createGeofencingRequest(Geofence geofence)
    {
        try
        {
            return (new GeofencingRequest.Builder()
                    .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                    .addGeofence(geofence)
                    .build());
        }

        catch (Exception e)
        {
            ShowException.ShowExceptionMessage("LocationManaging", e.getMessage().toString(), context);
        }

        return null;
    }

    /**
     * Method to create a new GeoFence
     * @param ID --> Geofence id
     * @param latLng
     * @param transitionTypes --> ENTER | DWELL | EXIT
     * @return
     */
    public Geofence createGeofence(String ID, LatLng latLng, int transitionTypes) {
        return (new Geofence.Builder()
                .setCircularRegion(latLng.latitude, latLng.longitude, geoFenceRadius)
                .setRequestId(ID)
                .setTransitionTypes(transitionTypes)
                .setLoiteringDelay(5000) //Second before receiving a notification after entering in the geofence
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build());
    }

    /**
     * Method to get pendingIntent
     * @return
     */
    public PendingIntent getPendingIntent()
    {
        try
        {
            if (pendingIntent != null)
                return pendingIntent;

            Intent intent = new Intent(this, GFBroadcastReceiver.class); //Create new Intent

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            else
                pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


            //pendingIntent = PendingIntent.getBroadcast(context, 2607, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            return pendingIntent;
        }

        catch (Exception e)
        {
            ShowException.ShowExceptionMessage("LocationManaging", e.getMessage().toString(), context);
        }

        return null;
    }

    /**
     * Method to manage possible error
     * @param e
     * @return
     */
    public String getErrorString(Exception e)
    {
        try
        {
            if (e instanceof ApiException)
            {
                ApiException apiException = (ApiException) e;
                switch (apiException.getStatusCode())
                {
                    case GeofenceStatusCodes
                            .GEOFENCE_NOT_AVAILABLE:
                        return "GEOFENCE_NOT_AVAILABLE";
                    case GeofenceStatusCodes
                            .GEOFENCE_TOO_MANY_GEOFENCES:
                        return "GEOFENCE_TOO_MANY_GEOFENCES";
                    case GeofenceStatusCodes
                            .GEOFENCE_TOO_MANY_PENDING_INTENTS:
                        return "GEOFENCE_TOO_MANY_PENDING_INTENTS";
                }
            }
            return e.getLocalizedMessage();
        }

        catch (Exception exc)
        {
            ShowException.ShowExceptionMessage("LocationManaging", exc.getMessage().toString(), context);
        }

        return "";
    }

}
