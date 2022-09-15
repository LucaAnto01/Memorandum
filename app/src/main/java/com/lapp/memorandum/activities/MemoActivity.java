package com.lapp.memorandum.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.lapp.memorandum.R;
import com.lapp.memorandum.activities.ShowException;

public class MemoActivity extends AppCompatActivity {

    /*Attributes*/
    private TextView twDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_memo);

            twDescription = findViewById(R.id.twDescription);
            twDescription.setMovementMethod(new ScrollingMovementMethod()); //Doing this to set the description scrollable
        }

        catch (Exception e)
        {
            ShowException.ShowExceptionMessage("MemoActivity", e.getMessage().toString(), this);
        }
    }
}