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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lapp.memorandum.models.Memo;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

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
        try
        {
            supportMapFragment.getMapAsync(new OnMapReadyCallback() { //Async map
                @Override
                public void onMapReady(@NonNull GoogleMap googleMap) //When map is ready
                {
                    googleMap.clear(); //Clear map from holder marker

                    Realm.init(getContext());
                    Realm realm = Realm.getDefaultInstance();
                    RealmResults<Memo> memoList = realm.where(Memo.class).equalTo("isExpiry", false).
                            equalTo("isCompleted", false).findAll(); //Select and get the valid Memo

                    //Adding map Marker on position
                    for (Memo currentMemo: memoList)
                    {
                        if((currentMemo.getPlace().getLatitude() != -1) && (currentMemo.getPlace().getLongitude() != -1)) //If Memo Location is set
                        {
                            LatLng memoLocation = new LatLng(currentMemo.getPlace().getLatitude(), currentMemo.getPlace().getLongitude());
                            MarkerOptions memoMarker = new MarkerOptions();
                            memoMarker.position(memoLocation);
                            memoMarker.title(currentMemo.getTitle());
                            //memoMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.memo_map_icon));
                            googleMap.addMarker(memoMarker);
                        }
                    }

                    LatLng latLng = new LatLng(44.8036, 10.33); //TODO: change with user location
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10)); //Focus on position animated

                }
            });
        }

        catch (Exception e)
        {
            ShowException.ShowExceptionMessage("Map_Fragment", e.getMessage().toString(), getContext());
        }
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