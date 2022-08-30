package com.lapp.memorandum;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Map fragment
 */
public class MapFragment extends Fragment
{
    /*Attributes*/
    private SupportMapFragment supportMapFragment;

    @Override
    public void onCreate(Bundle savedInstanceState)
    { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = null;

        try
        {
            view = inflater.inflate(R.layout.fragment_map, container, false);
            if ((ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||
               (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) //If don't have map permission
            {
                ShowException.ShowExceptionMessage("Map_Fragment", "Before being able to access this functionality, it is necessary grant permissions for the position", getContext());
                (new Handler()).postDelayed(this::closeApp, 10000);
            }

            supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frgMap);
            setMap();
        }

        catch (Exception e)
        {
            ShowException.ShowExceptionMessage("Map_Fragment", e.getMessage().toString(), getContext());
        }

        return view;
    }

    /**
     * Method to set map and relative marker
     */
    private void setMap()
    {
        supportMapFragment.getMapAsync(new OnMapReadyCallback() { //Async map
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) //When map is ready
            {
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
                {
                    @Override
                    public void onMapClick(@NonNull LatLng latLng) //Click on map
                    {
                        MarkerOptions userPosition = new MarkerOptions();
                        userPosition.position(latLng); //User location
                        userPosition.title("You're here!"); //Set the title of the marker

                        googleMap.clear(); //Clear map from holder marker

                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10)); //Focus on position animated

                        googleMap.addMarker(userPosition); //Add marker to map
                    }
                });
            }
        });
    }

    /**
     * Method to close app
     */
    private void closeApp()
    {
        getActivity().finish();
        System.exit(0);
    }
}