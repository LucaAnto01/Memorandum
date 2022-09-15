package com.lapp.memorandum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.lapp.memorandum.databinding.ActivityMainBinding;
import com.lapp.memorandum.models.Memo;
import com.lapp.memorandum.services.GeoFencingManaging;
import com.lapp.memorandum.services.LocationManaging;
import com.lapp.memorandum.utils.MemoAppData;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity
{
    private ActivityMainBinding binding;
    private LocationManaging locationManaging;
    private final int permissionRequestCode = 100;

    private GeofencingClient geofencingClient;
    private GeoFencingManaging geoFencingManaging;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);

            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            locationManaging = new LocationManaging(this);

            ReplaceFragment(new MemoFragment()); //Default selected item
            binding.bottomNavigationView.setSelectedItemId(R.id.memo);

            //Setting bottomNavigationView listener
            binding.bottomNavigationView.setOnItemSelectedListener(this::NavigationItemClick);

            checkUserPermission(); //Check user permission


            geofencingClient = LocationServices.getGeofencingClient(this);
            geoFencingManaging = new GeoFencingManaging(this);
            setGeofence(); //Adding geofence

        }

        catch (Exception e)
        {
            ShowException.ShowExceptionMessage("MainActivity", e.getMessage().toString(), this);
        }
    }

    /**
     * Method to manage user permission
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case 1:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();

                return;
            }
        }
    }

    /**
     * Bottom navigation view item click listener
     * @param item
     * @return
     */
    public boolean NavigationItemClick(MenuItem item)
    {
        try
        {
            switch (item.getItemId())
            {
                case R.id.all_memo: ReplaceFragment(new AllMemo_Fragment());
                    break;
                case R.id.memo: ReplaceFragment(new MemoFragment());
                    break;
                case R.id.map: ReplaceFragment(new MapFragment());
                    break;
            }

            return true;
        }

        catch (Exception e)
        {
            ShowException.ShowExceptionMessage("MainActivity", e.getMessage().toString(), this);
        }

        return false;
    }

    /**
     * Method to replace the fragment in according with the selected item
     * @param fragment
     */
    private void ReplaceFragment(Fragment fragment)
    {
        try
        {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.mainFrame, fragment); //Replace the fragment
            fragmentTransaction.commit();
        }

        catch (Exception e)
        {
            ShowException.ShowExceptionMessage("MainActivity", e.getMessage().toString(), this);
        }
    }

    /**
     * Method to check the user permission
     */
    private void checkUserPermission()
    {

        if(!MemoAppData.getPermissionManager(this).checkPermissions(MemoAppData.getPermissions()))
            MemoAppData.getPermissionManager(this).askPermission(this, MemoAppData.getPermissions(), permissionRequestCode);

        else
            Toast.makeText(this, "All permission granted!", Toast.LENGTH_SHORT).show();

        /*if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET}, 101);
        }*/
    }

    /**
     * Method to set and adding geofence
     */
    private void setGeofence()
    {
        try
        {
            Realm.init(this);
            Realm realm = Realm.getDefaultInstance();
            RealmResults<Memo> memoList = realm.where(Memo.class).equalTo("isExpiry", false).
                    equalTo("isCompleted", false).findAll(); //Select and get the valid Memo

            //Adding map Marker on position
            for (Memo currentMemo : memoList) {
                if ((currentMemo.getPlace().getLatitude() != -1) && (currentMemo.getPlace().getLongitude() != -1)) //If Memo Location is set
                {
                    LatLng memoLocation = new LatLng(currentMemo.getPlace().getLatitude(), currentMemo.getPlace().getLongitude());

                    addGeoFence(memoLocation, currentMemo.getId()); //Adding geofence
                }
            }
        }

        catch (Exception e)
        {
            ShowException.ShowExceptionMessage("MainActivity", e.getMessage().toString(), this);
        }

    }

    /**
     * Method to add a new GeoFence
     * @param latLng
     * @param id --> corresponding with Memo id
     */
    private void addGeoFence(LatLng latLng, int id)
    {
        try
        {
            String idGeoFence = String.valueOf(id);
            Geofence geofence = geoFencingManaging.createGeofence(idGeoFence, latLng, Geofence.GEOFENCE_TRANSITION_ENTER); /*| Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT*/
            //Create a request
            GeofencingRequest geofencingRequest = geoFencingManaging.createGeofencingRequest(geofence);
            PendingIntent pendingIntent = geoFencingManaging.getPendingIntent();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                geofencingClient.addGeofences(geofencingRequest, pendingIntent).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused)
                    {

                        //ShowException.ShowExceptionMessage("MapFragment", "Successfully added GeoFans", getContext());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        String errorMessage = geoFencingManaging.getErrorString(e);
                        ShowException.ShowExceptionMessage("MainActivity", errorMessage, getBaseContext());
                    }
                });
            }
        }

        catch (Exception e)
        {
            ShowException.ShowExceptionMessage("MainActivity", e.getMessage().toString(), this);
        }
    }
}