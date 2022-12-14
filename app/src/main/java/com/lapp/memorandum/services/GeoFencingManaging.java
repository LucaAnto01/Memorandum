package com.lapp.memorandum.services;

import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.model.LatLng;
import com.lapp.memorandum.activities.ShowException;

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

        try
        {
            this.context = base;
        }

        catch (Exception e)
        {
            ShowException.ShowExceptionMessage("GeoFencingManaging", e.getMessage().toString(), context);
        }
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
                    .addGeofence(geofence)
                    .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                    .build());
        }

        catch (Exception e)
        {
            ShowException.ShowExceptionMessage("GeoFencingManaging", e.getMessage().toString(), context);
        }

        return null;
    }

    /**
     * Method to create a new GeoFence
     * @param id --> Geofence id
     * @param latLng
     * @param transitionTypes --> ENTER | DWELL | EXIT
     * @return
     */
    public Geofence createGeofence(String id, LatLng latLng, int transitionTypes)
    {
        try
        {
            return (new Geofence.Builder()
                    .setCircularRegion(latLng.latitude, latLng.longitude, geoFenceRadius)
                    .setRequestId(id)
                    .setTransitionTypes(transitionTypes)
                    .setLoiteringDelay(2500) //Second before receiving a notification after entering in the geofence
                    .setExpirationDuration(43200000) //Self destruction
                    .build());
        }

        catch (Exception e)
        {
            ShowException.ShowExceptionMessage("GeoFencingManaging", e.getMessage().toString(), context);
        }

        return null;
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

            Intent intent = new Intent(context, GFBroadcastReceiver.class); //Create new Intent

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            else
                pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


            //pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

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
            ShowException.ShowExceptionMessage("GeoFencingManaging", exc.getMessage().toString(), context);
        }

        return "";
    }

}
