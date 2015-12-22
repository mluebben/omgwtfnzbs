/////////////////////////////////////////////////////////////////////////////
// $Id: ArtistAdapter.java 4 2013-04-15 21:06:51Z ml $
// Copyright (C) 2015 Matthias Lübben <ml81@gmx.de>
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// as published by the Free Software Foundation; either
// version 2 of the License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
/////////////////////////////////////////////////////////////////////////////
// Purpose:      Adapter for artist view
// Created:      10.09.2015 (dd.mm.yyyy)
/////////////////////////////////////////////////////////////////////////////

package de.luebben.omgwtfnzbs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.omgwtfnzbs.search.NzbItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class NzbItemAdapter extends RecyclerView.Adapter<NzbItemViewHolder> {

    public static final String TAG = "NzbItemAdapter";


    public interface OnItemClickedListener {
        void onItemClicked(NzbItemAdapter adapter, int position);
    }

    public interface OnAddClickedListener {
        void onAddClicked(NzbItemAdapter adapter, int position);
    }



    private LayoutInflater mInflater;

    private OnItemClickedListener mOnItemClickedListener = null;

    private OnAddClickedListener mOnAddClickedListener = null;


    private ArrayList<NzbItem> mItems = new ArrayList<>();


    private final NzbItemViewHolder.OnItemClickedListener ITEM_CLICKED_LISTENER = new NzbItemViewHolder.OnItemClickedListener() {
        @Override
        public void onItemClicked(NzbItemViewHolder viewHolder, View clickedView) {
            int position = viewHolder.getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                fireOnItemClicked(position);
            }
        }
    };

    private final NzbItemViewHolder.OnAddClickedListener ADD_CLICKED_LISTENER = new NzbItemViewHolder.OnAddClickedListener() {
        @Override
        public void onAddClicked(NzbItemViewHolder viewHolder, View buttonView) {
            int position = viewHolder.getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                fireOnAddClicked(position);
            }
        }
    };




    public NzbItemAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void setOnItemClickedListener(OnItemClickedListener l) {
        mOnItemClickedListener = l;
    }

    protected void fireOnItemClicked(int position) {
        if (mOnItemClickedListener != null) {
            mOnItemClickedListener.onItemClicked(this, position);
        }
    }


    public void setOnAddClickedListener(OnAddClickedListener l) {
        mOnAddClickedListener = l;
    }

    public OnAddClickedListener getOnAddClickedListener() {
        return mOnAddClickedListener;
    }

    protected void fireOnAddClicked(int position) {
        if (mOnAddClickedListener != null) {
            mOnAddClickedListener.onAddClicked(this, position);
        }
    }


    public void setData(ArrayList<NzbItem> data) {
        mItems.clear();
        if (data != null) {
            mItems.addAll(data);
        }
        notifyDataSetChanged();
    }

    public NzbItem getItem(int index) {
        return mItems.get(index);
    }

    public List<NzbItem> getItems() {
        return Collections.unmodifiableList(mItems);
    }


    @Override
    public NzbItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        NzbItemViewHolder viewHolder = new NzbItemViewHolder(mInflater.inflate(R.layout.row, parent, false));
        viewHolder.setOnItemClickedListener(ITEM_CLICKED_LISTENER);
        viewHolder.setOnAddClickedListener(ADD_CLICKED_LISTENER);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NzbItemViewHolder holder, int position) {
        holder.setNzbItem(getItem(position));
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

}
