package com.lapp.memorandum.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lapp.memorandum.R;
import com.lapp.memorandum.ShowException;
import com.lapp.memorandum.models.Memo;

import java.text.DateFormat;

import io.realm.RealmResults;

/**
 * Class for managing Recycler View
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.MemoViewHolder>
{

    Context rvAdapterContext;
    RealmResults<Memo> memoList;

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
                holder.twDescription.setText(memo.getDescription()); //Setting description

                String composeAddress = memo.getComposeAddress();
                if(composeAddress.equals(""))
                    holder.twAddress.setVisibility(View.INVISIBLE); //Setting invisible because there isn't

                holder.twAddress.setText(composeAddress);

                String expiryDateFormatted = memo.getExpiryDateFormatted();
                if(expiryDateFormatted.equals(""))
                    holder.twExpiryDate.setVisibility(View.INVISIBLE); //Setting invisible because there isn't

                holder.twExpiryDate.setText(expiryDateFormatted);
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
