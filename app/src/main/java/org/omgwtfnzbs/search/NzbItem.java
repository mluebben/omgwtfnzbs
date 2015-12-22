package org.omgwtfnzbs.search;

import java.io.Serializable;


public class NzbItem implements Serializable {

    private String mNzbid;
    private String mRelease;
    private String mGroup;
    private String mSizebytes;
    private String mUsenetage;
    private String mCategoryid;
    private String mCattext;
    private String mWeblink;
    private String mDetails;
    private String mGetnzb;

    public void setNzbid(String nzbid) {
        mNzbid = nzbid;
    }

    public String getNzbid() {
        return mNzbid;
    }

    public void setRelease(String release) {
        mRelease = release;
    }

    public String getRelease() {
        return mRelease;
    }

    public void setGroup(String group) {
        mGroup = group;
    }

    public String getGroup() {
        return mGroup;
    }

    public void setSizebytes(String sizebytes) {
        mSizebytes = sizebytes;
    }

    public String getSizebytes() {
        return mSizebytes;
    }

    public void setUsenetage(String usenetage) {
        mUsenetage = usenetage;
    }

    public String getUsenetage() {
        return mUsenetage;
    }

    public void setCategoryid(String categoryid) {
        mCategoryid = categoryid;
    }

    public String getCategoryid() {
        return mCategoryid;
    }

    public void setCattext(String cattext) {
        mCattext = cattext;
    }

    public String getCattext() {
        return mCattext;
    }

    public void setWeblink(String weblink) {
        mWeblink = weblink;
    }

    public String getWeblink() {
        return mWeblink;
    }

    public void setDetails(String details) {
        mDetails = details;
    }

    public String getDetails() {
        return mDetails;
    }

    public void setGetnzb(String getnzb) {
        mGetnzb = getnzb;
    }

    public String getGetnzb() {
        return mGetnzb;
    }
    
    @Override
    public String toString() {
        return getRelease();
    }
}
