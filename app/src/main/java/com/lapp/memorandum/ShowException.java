package com.lapp.memorandum;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Class to display an error message
 */
public class ShowException
{
    /**
     *  Method to display an error message
     * @param fragment
     * @param description
     * @param context
     */
    public static void ShowExceptionMessage(String fragment, String description, Context context)
    {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(fragment);
        builder.setMessage(description);

        // add a button
        builder.setPositiveButton("OK", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
