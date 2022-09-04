package com.lapp.memorandum.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.lapp.memorandum.R;
import com.lapp.memorandum.ShowException;

import java.util.Random;

public class NotificationManaging extends ContextWrapper
{
    /*Attributes*/
    private Context context;
    private final String channelName = "Memorandum notification channel";
    private final String channelId = "com.lapp.memorandum.services: " + channelName;

    /**
     * Constructor method
     * @param base
     */
    public NotificationManaging(Context base)
    {
        super(base);
        try
        {
            this.context = base;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) //Check version
                createChannel();
        }

        catch (Exception e)
        {
            ShowException.ShowExceptionMessage("NotificationManaging", e.getMessage().toString(), context);
        }
    }

    /**
     * Method to create a NotificationChannel
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannel()
    {
        try
        {
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Memorandum notification");
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notificationChannel);
        }

        catch (Exception e)
        {
            ShowException.ShowExceptionMessage("NotificationManaging", e.getMessage().toString(), context);
        }
    }

    /**
     * Method to create new notification
     * @param title
     * @param description
     * @param id --> corresponds to Memo id
     * @param activityName
     */
    public void createHighPriorityNotification(String title, String description, int id, Class activityName)
    {
        try
        {
            //This permit to open app (in a activityName interface) when we tap the notification
            Intent intent = new Intent(this, activityName);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Notification notification =(new NotificationCompat.Builder(this, channelId) //Create a notification
                    //.setContentTitle(title)
                    //.setContentText(body)
                    .setSmallIcon(R.drawable.memo_map_icon)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setStyle(new NotificationCompat.BigTextStyle().setSummaryText("Memorandum").setBigContentTitle(title).bigText(description))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true) //Delete when click on the notification
                    .build());

            //Send the notification
            //--> use id to not repeat the notification
            NotificationManagerCompat.from(this).notify(id, notification);
        }

        catch (Exception e)
        {
            ShowException.ShowExceptionMessage("NotificationManaging", e.getMessage().toString(), context);
        }
    }
}
