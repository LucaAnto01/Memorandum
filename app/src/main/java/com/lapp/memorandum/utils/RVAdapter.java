package com.lapp.memorandum.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lapp.memorandum.activities.AddMemoActivity;
import com.lapp.memorandum.R;
import com.lapp.memorandum.activities.ShowException;
import com.lapp.memorandum.models.Memo;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Class for managing Recycler View
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.MemoViewHolder>
{
    private Context rvAdapterContext;
    private RealmResults<Memo> memoList;

    public RVAdapter(Context context, RealmResults<Memo> memoList)
    {
        try
        {
            setRvAdapterContext(context);
            setMemoList(memoList);
        }

        catch (Exception e)
        {
            ShowException.ShowExceptionMessage("RVAdapter", e.getMessage().toString(), rvAdapterContext);
        }
    }

    /**
     * Create view holder with activity_memo item
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public MemoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new MemoViewHolder(LayoutInflater.from(getRvAdapterContext()).inflate(R.layout.activity_memo,parent,false));
    }

    /**
     * Method to create the activity_memo item for each element
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull MemoViewHolder holder, int position)
    {
        try
        {
            Memo memo = getMemoList().get(position); //Get i-element
            if((!memo.getTitle().equals(null)) ||  (!memo.getTitle().equals("")))//If Memo is setting
            {
                holder.twTitle.setText(memo.getTitle()); //Setting title

                if(memo.getExpiry()) //Check if memo is expiry
                    holder.twTitle.setTextColor(Color.parseColor("#FFA500"));

                if(memo.isCompleted()) //Check if memo is completed change title color to show the completed status
                    holder.twTitle.setTextColor(Color.parseColor("#03A50E"));

                holder.twDescription.setText(memo.getDescription()); //Setting description

                String composeAddress = memo.getComposeAddress();
                if(composeAddress.equals(""))
                    holder.twAddress.setVisibility(View.INVISIBLE); //Setting invisible because there isn't

                holder.twAddress.setText(composeAddress);

                String expiryDateFormatted = memo.expiryDateToString();
                if(expiryDateFormatted.equals(""))
                    holder.twExpiryDate.setVisibility(View.INVISIBLE); //Setting invisible because there isn't

                holder.twExpiryDate.setText(expiryDateFormatted);

                setDeleteItemMethod(holder, memo); //Setting on long click method to delete the Memo
            }
        }

        catch (Exception e)
        {
            ShowException.ShowExceptionMessage("RVAdapter", e.getMessage().toString(), rvAdapterContext);
        }
    }

    /**
     * Method to get the count of the Memo element
     * @return
     */
    @Override
    public int getItemCount()
    {
        return getMemoList().size();
    }

    /**
     * Method to set the delete item method after long press click
     * @param holder
     * @param memo
     */
    private void setDeleteItemMethod(MemoViewHolder holder, Memo memo)
    {
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view)
            {
                PopupMenu menu = new PopupMenu(rvAdapterContext, view);
                menu.getMenu().add("Click to delete");
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        if(item.getTitle().equals("Click to delete"))
                        {
                            //delete memo
                            Realm realm = Realm.getDefaultInstance();
                            realm.beginTransaction();
                            memo.deleteFromRealm();
                            realm.commitTransaction();
                            Toast.makeText(rvAdapterContext,"Memo deleted",Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });

                menu.show();

                return true;
            }
        });
    }

    /**
     * Method to edit Memo
     */
    public void editMemo(int position)
    {
        Memo selectedMemo = memoList.get(position);

        //Passing data to add form for edit it
        /*Bundle bundle = new Bundle();
        bundle.putInt("id", selectedMemo.getId());
        bundle.putInt("isUpdate", 1); //--> update operation*/

        //Prepare to update memo
        Intent intent = new Intent(rvAdapterContext, AddMemoActivity.class);
        intent.putExtra("action", "update");
        intent.putExtra("id", selectedMemo.getId());
        rvAdapterContext.startActivity(intent);
    }

    /*Getters & Setters*/
    public Context getRvAdapterContext() { return rvAdapterContext; }

    public void setRvAdapterContext(Context rvAdapterContext) { this.rvAdapterContext = rvAdapterContext; }

    public RealmResults<Memo> getMemoList() { return memoList; }

    public void setMemoList(RealmResults<Memo> memoList) { this.memoList = memoList; }


    /**
     * Class for managing single Memo view
     */
    public class MemoViewHolder extends RecyclerView.ViewHolder
    {
        private TextView twTitle;
        private TextView twDescription;
        private TextView twAddress;
        private TextView twExpiryDate;

        /**
         * Constructor method
         * @param itemView
         */
        public MemoViewHolder(@NonNull View itemView)  //View --> activity memo
        {
            super(itemView);

            twTitle = itemView.findViewById(R.id.twTitle);
            twDescription = itemView.findViewById(R.id.twDescription);
            twAddress = itemView.findViewById(R.id.twAddress);
            twExpiryDate = itemView.findViewById(R.id.twExpiryDate);
        }
    }
}
