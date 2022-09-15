package com.lapp.memorandum.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lapp.memorandum.R;
import com.lapp.memorandum.activities.ShowException;
import com.lapp.memorandum.models.Memo;
import com.lapp.memorandum.utils.MemoAppData;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Map fragment
 */
public class MapFragment extends Fragment {
    /*Attributes*/
    private Context mapContext;
    private SupportMapFragment supportMapFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;
        try {
            //Set attributes
            view = inflater.inflate(R.layout.fragment_map, container, false);
            mapContext = getActivity().getBaseContext();

            //Check user permission
            checkUSerPermission();

            supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frgMap);

            setMap();

        } catch (Exception e) {
            ShowException.ShowExceptionMessage("MapFragment", e.getMessage().toString(), getContext());
        }

        return view;
    }

    /**
     * Method to set map and relative marker
     */
    private void setMap() {
        try {
            supportMapFragment.getMapAsync(new OnMapReadyCallback() { //Async map
                @Override
                public void onMapReady(@NonNull GoogleMap googleMap) //When map is ready
                {
                    googleMap.clear(); //Clear map from holder marker

                    if (ActivityCompat.checkSelfPermission(mapContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mapContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                        googleMap.setMyLocationEnabled(true);

                    else
                        //Ask for permission
                        ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 0);


                    Realm.init(getContext());
                    Realm realm = Realm.getDefaultInstance();
                    RealmResults<Memo> memoList = realm.where(Memo.class).equalTo("isExpiry", false).
                            equalTo("isCompleted", false).findAll(); //Select and get the valid Memo

                    //Adding map Marker on position
                    for (Memo currentMemo : memoList) {
                        if ((currentMemo.getPlace().getLatitude() != -1) && (currentMemo.getPlace().getLongitude() != -1)) //If Memo Location is set
                        {
                            LatLng memoLocation = new LatLng(currentMemo.getPlace().getLatitude(), currentMemo.getPlace().getLongitude());
                            MarkerOptions memoMarker = new MarkerOptions();
                            memoMarker.position(memoLocation);
                            memoMarker.title(currentMemo.getTitle());
                            memoMarker.snippet(currentMemo.getDescription());
                            memoMarker.icon(createMarker(mapContext, R.drawable.memo_map_icon));
                            googleMap.addMarker(memoMarker);

                        }
                    }

                    //Set user location
                    if (MemoAppData.getUserLocation() != null) {
                        //Adding user position
                        LatLng latLng = new LatLng(MemoAppData.getUserLocation().getLatitude(), MemoAppData.getUserLocation().getLongitude());
                        MarkerOptions userMarker = new MarkerOptions();
                        userMarker.position(latLng);
                        userMarker.title("You're here!");
                        googleMap.addMarker(userMarker);
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10)); //Focus on position animated
                    }
                }
            });
        }
        catch (Exception e) {
            ShowException.ShowExceptionMessage("MapFragment", e.getMessage().toString(), getContext());
        }
    }

    /**
     * Method to custom Maps Marker
     * @param context
     * @param vectorDrawableResourceId
     * @return
     */
    private BitmapDescriptor createMarker(Context context, @DrawableRes int vectorDrawableResourceId)
    {
        try
        {
            Drawable background = ContextCompat.getDrawable(context, vectorDrawableResourceId);
            background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
            Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            background.draw(canvas);
            //vectorDrawable.draw(canvas);
            return BitmapDescriptorFactory.fromBitmap(bitmap);
        }


        catch (Exception e)
        {
            ShowException.ShowExceptionMessage("MapFragment", e.getMessage().toString(), getContext());
        }

        return null;
    }

    /**
     * Method to close app
     */
    private void closeApp()
    {
        getActivity().finish();
        System.exit(0);
    }

    /**
     * Method to check the user permission
     */
    private void checkUSerPermission()
    {
        try
        {
            if(!MemoAppData.getPermissionManager(getMapContext()).checkPermissions(MemoAppData.getPermissions()))
                Toast.makeText(getMapContext(), "Please, leave the necessary permissions", Toast.LENGTH_SHORT).show();
        }

        catch (Exception e)
        {
            ShowException.ShowExceptionMessage("MapFragment", e.getMessage().toString(), getContext());
        }
    }

    /*Getters*/
    public Context getMapContext() { return mapContext; }
}