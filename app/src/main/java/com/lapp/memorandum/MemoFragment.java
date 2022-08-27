package com.lapp.memorandum;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MemoFragment extends Fragment implements View.OnClickListener
{
    private FloatingActionButton fbAddMemo;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_memo, container, false);
        fbAddMemo = (FloatingActionButton) view.findViewById(R.id.fbAddMemo);
        fbAddMemo.setOnClickListener(this);

        return view;
    }

    /**
     * Listener method click fbAddMemo
     * Start new memo creation procedure
     * @param v
     */
    @Override
    public void onClick(View v)
    {
        startActivity(new Intent(getContext(), AddMemoActivity.class));
    }
}