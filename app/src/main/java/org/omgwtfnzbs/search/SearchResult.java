package org.omgwtfnzbs.search;

import java.util.ArrayList;
import java.util.List;


public class SearchResult {

    private String mNotice = "";
    private List<NzbItem> mItems = new ArrayList<NzbItem>();


    public void setNotice(String notice) {
        mNotice = notice;
    }

    public String getNotice() {
        return mNotice;
    }

    public void setItems(List<NzbItem> items) {
        mItems = items;
    }

    public List<NzbItem> getItems() {
        return mItems;
    }

}