package com.lapp.memorandum;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lapp.memorandum.models.Memo;
import com.lapp.memorandum.utils.RVAdapter;
import com.lapp.memorandum.utils.RVTouchAdapter;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * All memo fragment class
 */
public class AllMemo_Fragment extends Fragment
{

    /*Attributes*/
    private Context allMemoContext;
    private RealmResults<Memo> memoList;
    private RecyclerView rwAllMemo;

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
            view = inflater.inflate(R.layout.fragment_all_memo_, container, false);
            //Setting attributes
            allMemoContext = getContext(); //Set context
            //Getting data from database
            updateAllMemo(); //Update memo

            rwAllMemo = (RecyclerView)view.findViewById(R.id.rwAllMemo);
            rwAllMemo.setLayoutManager(new LinearLayoutManager(allMemoContext));

            //Setting adapter to Memo list
            RVAdapter rvAdapter = new RVAdapter(allMemoContext, memoList);
            rwAllMemo.setAdapter(rvAdapter);
            ItemTouchHelper rvTouchAdapter = new ItemTouchHelper(new RVTouchAdapter(rvAdapter));
            rvTouchAdapter.attachToRecyclerView(rwAllMemo);

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
            ShowException.ShowExceptionMessage("AllMemoFragment", e.getMessage().toString(), getContext());
        }

        return view;
    }

    /**
     * Method to update the state of expiry date of the stored Memo
     */
    private void updateAllMemo()
    {
        try
        {
            Realm.init(allMemoContext);
            Realm realm = Realm.getDefaultInstance();

            RealmResults<Memo> memoToUpdate = realm.where(Memo.class).findAll(); //Select and get all Memo

            for (Memo currentMemo: memoToUpdate)
            {
                realm.beginTransaction();
                currentMemo.setIfIsExpiry();
                realm.commitTransaction();
            }

            updateAllMemoList();
        }

        catch (Exception e)
        {
            ShowException.ShowExceptionMessage("AllMemoFragment", e.getMessage().toString(), getContext());
        }
    }

    /**
     * Method to update AllMemo List
     */
    private void updateAllMemoList()
    {
        try
        {
            Realm.init(allMemoContext);
            Realm realm = Realm.getDefaultInstance();

            memoList = realm.where(Memo.class).sort("expiryDate", Sort.ASCENDING).findAll(); //Select and get all Memo
        }

        catch (Exception e)
        {
            ShowException.ShowExceptionMessage("AllMemoFragment", e.getMessage().toString(), getContext());
        }
    }

}