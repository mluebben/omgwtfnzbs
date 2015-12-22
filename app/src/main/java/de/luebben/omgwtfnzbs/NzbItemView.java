package de.luebben.omgwtfnzbs;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.omgwtfnzbs.search.NzbItem;

/**
 * Created by Matthias on 21.12.2015.
 */
public class NzbItemView extends RelativeLayout {

    public static final String TAG = "NzbItemView";

    public interface OnAddClickedListener {
        void onAddClicked(NzbItemView parent, View buttonView);
    }

    private ImageView mCategoryImageView;
    private TextView mReleaseTextView;
    private TextView mGroupTextView;
    private TextView mSizebytesTextView;
    private TextView mUsenetageTextView;
    private TextView mCattextTextView;
    private ImageButton mAddButton;

    private NzbItem mNzbItem;

    private OnAddClickedListener mOnAddClickedListener;


    /** inherited constructor. */
    public NzbItemView(Context context) {
        super(context);
    }

    /** inherited constructor. */
    public NzbItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /** inherited constructor. */
    public NzbItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /** inherited constructor */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NzbItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setOnAddClickedListener(OnAddClickedListener listener) {
        mOnAddClickedListener = listener;
    }

    public OnAddClickedListener getOnAddClickedListener() {
        return mOnAddClickedListener;
    }

    protected void fireOnAddClickedListener(View buttonView) {
        if (mOnAddClickedListener != null) {
            mOnAddClickedListener.onAddClicked(this, buttonView);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();


        //ImageView categoryImageView = (ImageView) view.findViewById(R.id.thumbnail);
        //if (thumbnailImageView == null) {
        //    Log.w(TAG, "newView: inflated view hasn't defined an ImageView R.id.thumbnail");
        //}
        mReleaseTextView = (TextView) findViewById(R.id.release);
        if (mReleaseTextView == null) {
            Log.w(TAG, "newView: inflated view hasn't defined a TextView R.id.release");
        }
        mGroupTextView = (TextView) findViewById(R.id.group);
        if (mGroupTextView == null) {
            Log.w(TAG, "newView: inflated view hasn't defined a TextView R.id.group");
        }
        mSizebytesTextView = (TextView) findViewById(R.id.sizebytes);
        if (mSizebytesTextView == null) {
            Log.w(TAG, "newView: inflated view hasn't defined a TextView R.id.sizebytes");
        }
        mUsenetageTextView = (TextView) findViewById(R.id.usenetage);
        if (mUsenetageTextView == null) {
            Log.w(TAG, "newView: inflated view hasn't defined a TextView R.id.usenetage");
        }
        mCattextTextView = (TextView) findViewById(R.id.cattext);
        if (mCattextTextView == null) {
            Log.w(TAG, "newView: inflated view hasn't defined a TextView R.id.cattext");
        }

//
//
//        if (thumbnailImageView != null) {
//            thumbnailImageView.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    ViewHolder holder = getViewHolder(v);
//                    if (mOnThumbnailClickedListener != null && holder != null && holder.position >= 0) {
//                        mOnThumbnailClickedListener.onThumbnailClicked(holder.position);
//                    }
//                }
//
//            });
//        }



        mAddButton = (ImageButton) findViewById(R.id.add_btn);
        if (mAddButton != null) {
            mAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fireOnAddClickedListener(v);
                }
            });
        }
    }




    public void setNzbItem(NzbItem nzbItem) {
        mNzbItem = nzbItem;

        // Extract values from cursor
        String release = nzbItem.getRelease();
        if (release == null) {
            release = "";
        }
        String group = nzbItem.getGroup();
        if (group == null) {
            group = null;
        }
        String sizebytes = nzbItem.getSizebytes();
        if (sizebytes == null) {
            sizebytes = "";
        }
        String usenetage = nzbItem.getUsenetage();
        if (usenetage == null) {
            usenetage = "";
        }
        String cattext = nzbItem.getCattext();
        if (cattext == null) {
            cattext = "";
        }

        long dtUsenetage = Long.valueOf(usenetage) * 1000;
        long dtNow = System.currentTimeMillis();
        long delta = dtNow - dtUsenetage;
        long numberOfMSInADay = 1000 * 60 * 60 * 24;
        long days = Math.max(delta, 0) / numberOfMSInADay;
        usenetage = String.valueOf(days) + " d";

        sizebytes = String.valueOf(Long.valueOf(sizebytes) / 1024 / 1024) + " MiB";

        // Update view
        if (mReleaseTextView != null) {
            mReleaseTextView.setText(release);
        }
        if (mGroupTextView != null) {
            mGroupTextView.setText(group);
        }
        if (mSizebytesTextView != null) {
            mSizebytesTextView.setText(sizebytes);
        }
        if (mUsenetageTextView != null) {
            mUsenetageTextView.setText(usenetage);
        }
        if (mCattextTextView != null) {
            mCattextTextView.setText(cattext);
        }
    }

    public NzbItem getNzbItem() {
        return mNzbItem;
    }
}
