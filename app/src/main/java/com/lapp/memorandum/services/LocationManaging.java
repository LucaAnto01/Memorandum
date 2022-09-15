package com.lapp.memorandum.services;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;
import com.lapp.memorandum.activities.ShowException;
import com.lapp.memorandum.models.Memo;
import com.lapp.memorandum.utils.MemoAppData;

import io.realm.Realm;
import io.realm.RealmResults;

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
        try
        {
            this.context = context;
            init();
        }

        catch (Exception e)
        {
            ShowException.ShowExceptionMessage("LocationManaging", e.getMessage().toString(), context);
        }
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

            checkMemo();
        }

        catch (Exception e)
        {
            ShowException.ShowExceptionMessage("LocationManaging", e.getMessage().toString(), context);
        }
    }

    /**
     * Method to check if there's an active Memo near the user
     */
    public void checkMemo()
    {
        try
        {
            Realm.init(context);
            Realm realm = Realm.getDefaultInstance();
            RealmResults<Memo> memoList = realm.where(Memo.class).equalTo("isExpiry", false).
                    equalTo("isCompleted", false).findAll(); //Select and get the valid Memo

            //Check Memo near the user
            for (Memo currentMemo : memoList) {
                if ((currentMemo.getPlace().getLatitude() != -1) && (currentMemo.getPlace().getLongitude() != -1)) //If Memo Location is set
                {
                    LatLng memoLatLng = new LatLng(currentMemo.getPlace().getLatitude(), currentMemo.getPlace().getLongitude());
                    Location memoLocation = new Location("Memo" + Integer.toString(currentMemo.getId())); //Create new Location object for the current Memo
                    memoLocation.setLatitude(memoLatLng.latitude);
                    memoLocation.setLongitude(memoLatLng.longitude);

                    if(MemoAppData.getUserLocation() != null)
                    {
                        Location usrLocation = new Location("UserLoc"); //Create new Location object for the user
                        usrLocation.setLatitude(MemoAppData.getUserLocation().getLatitude());
                        usrLocation.setLongitude(MemoAppData.getUserLocation().getLongitude());

                        double distance = memoLocation.distanceTo(usrLocation); //Calculate the distance --> meters

                        if(distance <= 1000) //--> range: 1km
                        {
                            NotificationManaging notificationManaging = new NotificationManaging(context);
                            notificationManaging.createHighPriorityNotification(currentMemo.getTitle(), currentMemo.getDescription(), currentMemo.getId());
                        }
                    }
                }
            }


        }

        catch (Exception e)
        {
            ShowException.ShowExceptionMessage("LocationManaging", e.getMessage().toString(), context);
        }
    }

    private void notification(Memo memo)
    {

    }

}
