package com.lapp.memorandum.services;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;

import com.lapp.memorandum.ShowException;
import com.lapp.memorandum.utils.MemoAppData;

/**
 * Class to manage the user Location
 */
public class LocationManaging implements LocationListener
{
    /*Attributes*/
    private LocationManager locationManager;
    private Context context;
    private final int timeToUpdate =  300000; //5 minutes
    private final int distanceToUpdate = 150; //meters

    /**
     * Constructor method
     * @param context
     */
    public LocationManaging(Context context)
    {
        this.context = context;
        init();
    }

    /**
     * Method to init the Location manager
     */
    private void init()
    {
        try
        {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (!(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) && !(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED))
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, timeToUpdate, distanceToUpdate, (LocationListener) this);
        }

        catch (Exception e)
        {
            ShowException.ShowExceptionMessage("LocationManaging", e.getMessage().toString(), context);
        }
    }

    /**
     * Method called when location changed
     * @param location
     */
    @Override
    public void onLocationChanged(Location location)
    {
        try
        {
            if(MemoAppData.getUserLocation()  == null)
                MemoAppData.setUserLocation(new com.lapp.memorandum.models.Location());

            //Update user current location
            MemoAppData.getUserLocation().setLatitude(location.getLatitude());
            MemoAppData.getUserLocation().setLongitude(location.getLongitude());
        }

        catch (Exception e)
        {
            ShowException.ShowExceptionMessage("LocationManaging", e.getMessage().toString(), context);
        }
    }

}
