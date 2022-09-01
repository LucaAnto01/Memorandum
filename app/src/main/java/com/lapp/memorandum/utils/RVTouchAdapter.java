package com.lapp.memorandum.utils;

import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Class to manage touch and swipe action of RV
 */
public class RVTouchAdapter extends ItemTouchHelper.SimpleCallback
{
    /*Attribute*/
    private RVAdapter rvAdapter;

    /**
     * Constructor method
     * @param rvAdapter
     */
    public RVTouchAdapter(RVAdapter rvAdapter)
    {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.rvAdapter = rvAdapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target)
    {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction)
    {
        final int position = viewHolder.getAdapterPosition(); //Get selected item

        if (direction == ItemTouchHelper.RIGHT)
            rvAdapter.editMemo(position);

    }
}
