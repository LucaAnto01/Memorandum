package com.lapp.memorandum.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

/**
 * Class to manage Geofencing
 */
public class GeoFencingService extends Service
{

    /**
     * Meyhod called on start command
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    /**
     * Method called on destroy moment
     */
    @Override
    public void onDestroy()
    {
        stopSelf();
        super.onDestroy();
    }
}
