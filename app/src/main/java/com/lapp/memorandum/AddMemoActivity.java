package com.lapp.memorandum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.button.MaterialButton;
import com.lapp.memorandum.models.Location;
import com.lapp.memorandum.models.Memo;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;

public class AddMemoActivity extends AppCompatActivity
{
    /*Attributes*/
    private PlacesClient placesClient;
    private EditText etTitle;
    private EditText etDescription;
    private EditText etDate;
    private AutocompleteSupportFragment acPlace;
    private MaterialButton mtAddMemo;

    private Calendar calendar;
    private Location selectLocation;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_memo);

            //Setting attributes
            etTitle = findViewById(R.id.etTitle);
            etDescription = findViewById(R.id.etDescription);
            etDate = findViewById(R.id.etDate);
            mtAddMemo = findViewById(R.id.mtAddMemo);

            calendar = Calendar.getInstance(); //Setting calendar
            selectLocation = new Location(); //Setting selected location

            Realm.init(this);
            realm = Realm.getDefaultInstance();

            setEtDateDatePickerDialog(); //Setting etDate Functionality

            //Initialize Places
            if(!Places.isInitialized()) //If Places isn't initialized
                Places.initialize(getApplicationContext(), MemoAppData.getApiKey());

            setPlacesClient(Places.createClient(this));
            //setOnClickListener(); //Setting acPlace on click event listener

            //Source: https://developers.google.com/maps/documentation/places/android-sdk/autocomplete
            acPlace = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.acPlace);
            // Specify the types of place data to return.
            acPlace.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
            acPlace.setCountries("IT");

            //Setting acPlace onclick listener
            setAcPlaceOnClickListener();

            //Setting mtAddMemo onclick listener
            setMtOnClickListener();
        }

        catch (Exception e)
        {
            ShowException.ShowExceptionMessage("AddMemoActivity", e.getMessage().toString(), this);
        }
    }

    /*Getters & Setters*/
    public PlacesClient getPlacesClient()
    {
        return placesClient;
    }

    public void setPlacesClient(PlacesClient placesClient)
    {
        this.placesClient = placesClient;
    }

    /**
     * Method to set DatePickerDialog for etDate and etDate click listener
     */
    private void setEtDateDatePickerDialog()
    {
        try
        {
            DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day_of_month)
                {
                    //Update calendar variable with the selected date
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, day_of_month);

                    updateEtDateText();
                }
            };

            etDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    new DatePickerDialog(AddMemoActivity.this, date,
                            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
        }

        catch (Exception e)
        {
            ShowException.ShowExceptionMessage("AddMemoActivity", e.getMessage().toString(), this);
        }
    }

    /**
     * Method for updating the etDate text after selecting the date
     */
    private void updateEtDateText()
    {
        String dateFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.ITALY);

        etDate.setText(sdf.format(calendar.getTime()));
    }

    /**
     * Method to set on click event listener for acPlace
     */
    private void setAcPlaceOnClickListener()
    {
        try
        {
            // Set up a PlaceSelectionListener to handle the response.
            acPlace.setOnPlaceSelectedListener(new PlaceSelectionListener()
            {
                @Override
                public void onPlaceSelected(@NonNull Place place)
                {
                    // TODO: CREATE OBJECT LOCATION AND MEMORIZE IT
                    String test1 = place.getName();
                    String test2 = place.getId();
                    String test3 = place.getAddress(); // --> empty
                    selectLocation.setAddress(place.getName());
                    Log.i("Ciao", "Place: " + place.getName() + ", " + place.getId());
                    //getAddress()  //getLatLang()
                }


                @Override
                public void onError(@NonNull Status status)
                {
                    ShowException.ShowExceptionMessage("AddMemoActivity", status.getStatusMessage(), AddMemoActivity.this);
                }
            });
        }

        catch (Exception e)
        {
            ShowException.ShowExceptionMessage("AddMemoActivity", e.getMessage().toString(), this);
        }
    }

    /**
     * Method to set on click event listener for mtAddMemo
     */
    private void setMtOnClickListener()
    {
        try
        {
            mtAddMemo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    try
                    {
                        String title = etTitle.getText().toString();
                        String description = etDescription.getText().toString();
                        String expiryDate = etDate.getText().toString();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date dateExpiryDate = sdf.parse(expiryDate);
                        //TODO: CREATE METHOD TO CHECK THE VALUE OF EDIT TEXT

                        //Storing new data
                        realm.beginTransaction();
                        //TODO: populate test Location object with the value
                        Location testLocation = realm.createObject(Location.class);
                        testLocation.setAddress(selectLocation.getAddress());
                        Memo memo = realm.createObject(Memo.class);
                        memo.setTitle(title);
                        memo.setDescription(description);
                        memo.setExpiryDate(dateExpiryDate);
                        memo.setDateOfCreation(new Date());
                        memo.setCompleted(false);
                        memo.setPlace(testLocation);
                        realm.commitTransaction();
                        Toast.makeText(AddMemoActivity.this, "Memo successfully saved", Toast.LENGTH_SHORT).show();
                    }

                    catch (Exception e)
                    {
                        ShowException.ShowExceptionMessage("AddMemoActivity", e.getMessage().toString(), AddMemoActivity.this);
                    }
                }
            });
        }
        catch (Exception e)
        {
            ShowException.ShowExceptionMessage("AddMemoActivity", e.getMessage().toString(), this);
        }
    }
}