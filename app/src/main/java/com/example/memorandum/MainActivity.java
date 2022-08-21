package com.example.memorandum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.memorandum.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity
{
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);

            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            ReplaceFragment(new MemoFragment()); //Default selected item
            binding.bottomNavigationView.setSelectedItemId(R.id.memo);

            /**
             * Bottom navigation view item click listener
             */
            binding.bottomNavigationView.setOnItemSelectedListener(item -> {

                switch (item.getItemId())
                {
                    case R.id.all_memo: ReplaceFragment(new AllMemo_Fragment());
                        break;
                    case R.id.memo: ReplaceFragment(new MemoFragment());
                        break;
                    case R.id.map: ReplaceFragment(new MapFragment());
                        break;
                }

                return  true;
            });
        }

        catch (Exception e)
        {
            ShowException.ShowExceptionMessage("MainActivity", e.getMessage().toString(), this);
        }

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
}