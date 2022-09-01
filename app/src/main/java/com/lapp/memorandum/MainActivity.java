package com.lapp.memorandum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import com.lapp.memorandum.databinding.ActivityMainBinding;
import com.lapp.memorandum.utils.MemoAppData;

public class MainActivity extends AppCompatActivity
{
    private ActivityMainBinding binding;
    private final int permissionRequestCode = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);

            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            //Intent servIntent = new Intent("android.intent.action.TRAKPOSITIONSERVICE");
            //startService(servIntent);

            ReplaceFragment(new MemoFragment()); //Default selected item
            binding.bottomNavigationView.setSelectedItemId(R.id.memo);

            //Setting bottomNavigationView listener
            binding.bottomNavigationView.setOnItemSelectedListener(this::NavigationItemClick);

            checkUserPermission(); //Check user permission
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
}