package de.luebben.omgwtfnzbs;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.omgwtfnzbs.search.NzbItem;

/**
 * Created by Matthias on 21.12.2015.
 */
public class NzbItemViewHolder extends RecyclerView.ViewHolder {

    public static final String TAG = "NzbItemViewHolder";

    public interface OnItemClickedListener {
        void onItemClicked(NzbItemViewHolder viewHolder, View clickedView);
    }

    public interface OnAddClickedListener {
        void onAddClicked(NzbItemViewHolder viewHolder, View buttonView);
    }

//    private ImageView mCategoryImageView;
//    private TextView mReleaseTextView;
//    private TextView mGroupTextView;
//    private TextView mSizebytesTextView;
//    private TextView mUsenetageTextView;
//    private TextView mCattextTextView;
//    private ImageButton mAddButton;

    private OnItemClickedListener mOnItemClickedListener;
    private OnAddClickedListener mOnAddClickedListener;

    private NzbItem mNzbItem;





    public NzbItemViewHolder(View itemView) {
        super(itemView);

        if (!(itemView instanceof NzbItemView)) {
            throw new IllegalArgumentException("Expected itemView of type NzbItemView");
        }

        NzbItemView nzbItemView = (NzbItemView) itemView;
        nzbItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fireOnItemClicked(v);
            }
        });
        nzbItemView.setOnAddClickedListener(new NzbItemView.OnAddClickedListener() {
            @Override
            public void onAddClicked(NzbItemView parent, View buttonView) {
                fireOnAddClicked(buttonView);
            }
        });
    }


    public void setOnItemClickedListener(OnItemClickedListener listener) {
        mOnItemClickedListener = listener;
    }

    public OnItemClickedListener getOnItemClickedListener() {
        return mOnItemClickedListener;
    }

    protected void fireOnItemClicked(View v) {
        if (mOnItemClickedListener != null) {
            mOnItemClickedListener.onItemClicked(this, v);
        }
    }


    public void setOnAddClickedListener(OnAddClickedListener listener ) {
        mOnAddClickedListener = listener;
    }

    public OnAddClickedListener getOnAddClickedListener() {
        return mOnAddClickedListener;
    }

    protected void fireOnAddClicked(View v) {
        if (mOnAddClickedListener != null) {
            mOnAddClickedListener.onAddClicked(this, v);
        }
    }


    public void setNzbItem(NzbItem nzbItem) {
        mNzbItem = nzbItem;
        ((NzbItemView) itemView).setNzbItem(nzbItem);


//        mNzbItem = nzbItem;
//
//        // Extract values from cursor
//        String release = nzbItem.getRelease();
//        if (release == null) {
//            release = "";
//        }
//        String group = nzbItem.getGroup();
//        if (group == null) {
//            group = null;
//        }
//        String sizebytes = nzbItem.getSizebytes();
//        if (sizebytes == null) {
//            sizebytes = "";
//        }
//        String usenetage = nzbItem.getUsenetage();
//        if (usenetage == null) {
//            usenetage = "";
//        }
//        String cattext = nzbItem.getCattext();
//        if (cattext == null) {
//            cattext = "";
//        }
//
//        long dtUsenetage = Long.valueOf(usenetage) * 1000;
//        long dtNow = System.currentTimeMillis();
//        long delta = dtNow - dtUsenetage;
//        long numberOfMSInADay = 1000 * 60 * 60 * 24;
//        long days = Math.max(delta, 0) / numberOfMSInADay;
//        usenetage = String.valueOf(days) + " d";
//
//        sizebytes = String.valueOf(Long.valueOf(sizebytes) / 1024 / 1024) + " MiB";
//
//        // Update view
//        if (mReleaseTextView != null) {
//            mReleaseTextView.setText(release);
//        }
//        if (mGroupTextView != null) {
//            mGroupTextView.setText(group);
//        }
//        if (mSizebytesTextView != null) {
//            mSizebytesTextView.setText(sizebytes);
//        }
//        if (mUsenetageTextView != null) {
//            mUsenetageTextView.setText(usenetage);
//        }
//        if (mCattextTextView != null) {
//            mCattextTextView.setText(cattext);
//        }
    }

    public NzbItem getNzbItem() {
        return mNzbItem;
    }
}
