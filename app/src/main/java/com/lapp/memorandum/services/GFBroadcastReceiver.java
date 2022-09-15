package com.lapp.memorandum.services;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;
import com.lapp.memorandum.AddMemoActivity;
import com.lapp.memorandum.MainActivity;
import com.lapp.memorandum.MapFragment;
import com.lapp.memorandum.ShowException;
import com.lapp.memorandum.models.Memo;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

//https://developer.android.com/training/location/geofencing#java

/**
 * Class to manage Geo Fencing interactions
 */
public class GFBroadcastReceiver extends BroadcastReceiver
{
    /*Attributes*/
    private Context context;

    /**
     * Method called when receive new geofencing
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent)
    {
        try
        {
            this.context = context;
            Toast.makeText(this.context, "You've something to do...", Toast.LENGTH_SHORT).show();

            NotificationManaging notificationManaging = new NotificationManaging(context);
            //notificationManaging.createHighPriorityNotification("Memorandum", "You've something to do...", MapFragment.class);

            GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
            if (geofencingEvent.hasError()) {
                int errorCode = geofencingEvent.getErrorCode();
                String errorMessage = GeofenceStatusCodes.getStatusCodeString(errorCode);
                //Log.e(TAG, errorMessage);
                return;
            }
            List<Geofence> triggeringGeofences = new ArrayList<Geofence>();
            triggeringGeofences = geofencingEvent.getTriggeringGeofences(); //Getting geofence witch trigger action

            Memo triggerMemo = null;
            triggerMemo = getMemo(Integer.parseInt(triggeringGeofences.get(0).getRequestId()));

            if(triggerMemo != null)
            {
                int transitionType = geofencingEvent.getGeofenceTransition(); //Getting the event

                switch (transitionType)
                {
                    case Geofence.GEOFENCE_TRANSITION_ENTER:
                        Toast.makeText(context, "GEOFENCE_TRANSITION_ENTER", Toast.LENGTH_SHORT).show();
                        notificationManaging.createHighPriorityNotification(triggerMemo.getTitle(), triggerMemo.getDescription(), triggerMemo.getId(), MainActivity.class);
                        break;

                /*case Geofence.GEOFENCE_TRANSITION_DWELL:

                    break;
                case Geofence.GEOFENCE_TRANSITION_EXIT:

                    break;*/
                }
            }

        }

        catch (Exception e)
        {
            ShowException.ShowExceptionMessage("GFBroadcastReceiver", e.getMessage().toString(), context);
        }
    }

    /**
     * Method to get info of a specific Memo
     * @param id
     * @return
     */
    private Memo getMemo(int id)
    {
        try
        {
            Realm.init(context);
            Realm realm = Realm.getDefaultInstance();
            return realm.where(Memo.class).equalTo("id", id).findFirst();
        }

        catch (Exception e)
        {
            ShowException.ShowExceptionMessage("GFBroadcastReceiver", e.getMessage().toString(), context);
        }

        return null;
    }
}
