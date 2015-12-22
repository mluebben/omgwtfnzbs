package org.omgwtfnzbs.search;


import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class SearchQuery {

    /** No terms no search! */
    private String mSearch = "";
    /** Your account username. */
    private String mUser = "";
    /** Your API Key. */
    private String mApi = "";
    /** Optional, search multiple categorys. */
    private String mCatid = "";
    /** Optional, limit the search to X amount of days ago. */
    private String mRetention = "";
    /** Optional, return only english/unknown posts. */
    private String mEng = "";


    public void setSearch(String search) {
        mSearch = search;
    }

    public String getSearch() {
        return mSearch;
    }

    public void setUser(String user) {
        mUser = user;
    }

    public String getUser() {
        return mUser;
    }

    public void setApi(String api) {
        mApi = api;
    }

    public String getApi() {
        return mApi;
    }


    public void setCatid(String catid) {
        mCatid = catid;
    }

    public String getCatid() {
        return mCatid;
    }

    public void setRetention(String retention) {
        mRetention = retention;
    }

    public String getRetention() {
        return mRetention;
    }

    public void setEng(String eng) {
        mEng = eng;
    }

    public String getEng() {
        return mEng;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("https://api.omgwtfnzbs.org/json/?search=");
        sb.append(encode(mSearch));
        sb.append("&user=");
        sb.append(encode(mUser));
        sb.append("&api=");
        sb.append(encode(mApi));
        if (!mCatid.isEmpty()) {
            sb.append("&catid=");
            sb.append(encode(mCatid));
        }
        if (!mRetention.isEmpty()) {
            sb.append("&retention=");
            sb.append(encode(mRetention));
        }
        if (!mEng.isEmpty()) {
            sb.append("&eng=");
            sb.append(encode(mEng));
        }
        return sb.toString();
    }

    public URL toURL() throws MalformedURLException {
        return new URL(toString());
    }

    private String encode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8").replace("+", "%20");

        } catch (UnsupportedEncodingException e) {
            return s;
        }
    }

}