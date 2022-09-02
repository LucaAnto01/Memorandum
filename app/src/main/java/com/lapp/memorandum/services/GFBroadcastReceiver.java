package com.lapp.memorandum.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.lapp.memorandum.AddMemoActivity;

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
        this.context = context;
        Toast.makeText(this.context, "Memo successfully saved", Toast.LENGTH_SHORT).show();
    }
}
