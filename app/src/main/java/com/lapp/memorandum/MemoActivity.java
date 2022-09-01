package com.lapp.memorandum;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

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