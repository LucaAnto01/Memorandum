package com.lapp.memorandum;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lapp.memorandum.models.Memo;
import com.lapp.memorandum.utils.RVAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class MemoFragment extends Fragment implements View.OnClickListener
{
    /*Attributes*/
    private FloatingActionButton fbAddMemo;
    private Context memoContext;
    private RealmResults<Memo> memoList;
    private RecyclerView rwMemo;
    private RVAdapter rvAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = null;
        try
        {
            view = inflater.inflate(R.layout.fragment_memo, container, false);

            updateMemo(); //Update memo

            //Setting attributes
            fbAddMemo = (FloatingActionButton) view.findViewById(R.id.fbAddMemo);
            fbAddMemo.setOnClickListener(this);
            memoContext = getContext(); //Set context
            //Getting data from database
            Realm.init(memoContext);
            Realm realm = Realm.getDefaultInstance();



            memoList = realm.where(Memo.class).equalTo("isExpiry", false).equalTo("isCompleted", false).
                       sort("expiryDate", Sort.ASCENDING).findAll(); //Select and get the valid Memo

            //memoList = realm.where(Memo.class).findAll();
            rwMemo = (RecyclerView)view.findViewById(R.id.rwMemo);
            rwMemo.setLayoutManager(new LinearLayoutManager(memoContext));

            //Setting adapter to Memo list
            rvAdapter = new RVAdapter(memoContext, memoList);
            rwMemo.setAdapter(rvAdapter);

            //Setting memo list change event listener
            memoList.addChangeListener(new RealmChangeListener<RealmResults<Memo>>()
            {
                @Override
                public void onChange(RealmResults<Memo> memos)
                {
                    rvAdapter.notifyDataSetChanged(); //This permit refresh the view when something in the list change
                }
            });
        }

        catch (Exception e)
        {
            ShowException.ShowExceptionMessage("Memo_Fragment", e.getMessage().toString(), getContext());
        }

        return view;
    }

    /**
     * Method to update the state of expiry date of the stored Memo
     */
    private void updateMemo()
    {
        try
        {
            Realm.init(getContext());
            Realm realm = Realm.getDefaultInstance();

            RealmResults<Memo> memoToUpdate = realm.where(Memo.class).findAll(); //Select and get all Memo

            for (Memo currentMemo: memoToUpdate)
            {
                realm.beginTransaction();
                currentMemo.setIfIsExpiry();
                realm.commitTransaction();
            }

            //realm.copyToRealmOrUpdate(memoToUpdate);
        }

        catch (Exception e)
        {
            ShowException.ShowExceptionMessage("Memo_Fragment", e.getMessage().toString(), getContext());
        }
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