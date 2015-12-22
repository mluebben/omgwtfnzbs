package org.omgwtfnzbs.search;

import android.util.JsonReader;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class SearchTask {

    public static final String TAG = "SearchTask";

    /** Connect timeout 30 seconds. */
    public static final int CONNECT_TIMEOUT = 30000;

    /** Read timeout 30 seconds. */
    public static final int READ_TIMEOUT = 30000;


    public static final int CAT_APPS_PC = 1;
    public static final int CAT_APPS_MAC = 2;
    public static final int CAT_APPS_LINUX = 4;
    public static final int CAT_APPS_PHONE = 5;
    public static final int CAT_APPS_OTHER = 6;
    public static final int CAT_MUSIC_MP3 = 7;
    public static final int CAT_MUSIC_MVID = 8;
    public static final int CAT_MUSIC_FLAC = 22;
    public static final int CAT_MUSIC_OTHER = 3;
    public static final int CAT_GAMES_PC = 12;
    public static final int CAT_GAMES_MAC = 13;
    public static final int CAT_GAMES_OTHER = 14;
    public static final int CAT_MOVIES_STD = 15;
    public static final int CAT_MOVIES_HD = 16;
    public static final int CAT_MOVIES_DVD = 17;
    public static final int CAT_MOVIES_OTHER = 18;
    public static final int CAT_TV_STD = 19;
    public static final int CAT_TV_HD = 20;
    public static final int CAT_TV_OTHER= 21;
    public static final int CAT_XXX_HD_CLIPS = 24;
    public static final int CAT_XXX_SD_CLIPS = 25;
    public static final int CAT_XXX_SD_MOVIES = 26;
    public static final int CAT_XXX_HD_MOVIES = 27;
    public static final int CAT_XXX_DVD = 28;
    public static final int CAT_XXX_OTHER = 23;
    public static final int CAT_OTHER_EBOOKS = 9;
    public static final int CAT_OTHER_EXTRA_PARS = 10;
    public static final int CAT_OTHER_OTHER = 11;
    public static final int CAT_AUDIOBOOK = 29;



    public SearchResult search(SearchQuery request) throws JSONException, IOException {
        SearchResult response = new SearchResult();

        HttpURLConnection urlConn = null;
        JsonReader reader = null;

        try {
            // Create URL instance
            URL url = request.toURL();

            // Create Connection instance
            urlConn = (HttpURLConnection) url.openConnection();

            // No caching, we want the real thing.
            urlConn.setUseCaches(false);

            // Let the RTS know we want to make a POST request.
            urlConn.setRequestMethod("GET");

            // Define timeouts
            urlConn.setConnectTimeout(CONNECT_TIMEOUT);
            urlConn.setReadTimeout(READ_TIMEOUT);

            // Establish connection to server
            urlConn.connect();

            // Get response data.
            InputStream input = urlConn.getInputStream();


            reader = new JsonReader(new BufferedReader(new InputStreamReader(input)));
            processResponse(reader, response);

            return response;

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Throwable e) {
                    Log.wtf(TAG, e);
                }
                //reader = null;
            }
            if (urlConn != null) {
                try {
                    urlConn.disconnect();
                } catch (Throwable e) {
                    Log.wtf(TAG, e);
                }
            }
        }
    }








    private void processResponse(JsonReader reader, SearchResult response) throws JSONException, IOException {
        switch (reader.peek()) {
            case BEGIN_OBJECT:
                processResponseObject(reader, response);
                break;
            case BEGIN_ARRAY:
                processResponseArray(reader, response);
                break;
            default:
                throw new IOException("Malformed response");
        }
    }

    private void processResponseObject(JsonReader reader, SearchResult response) throws JSONException, IOException {
        String notice = null;

        // Parse object
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equalsIgnoreCase("notice")) {
                notice = readString(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        response.setNotice(notice);
    }

    private void processResponseArray(JsonReader reader, SearchResult response) throws JSONException, IOException {
        reader.beginArray();
        while (reader.hasNext()) {
            processResultItemObject(reader, response);
        }
        reader.endArray();
    }


    private void processResultItemObject(JsonReader reader, SearchResult response) throws JSONException, IOException {
        String nzbid = null;
        String release = null;
        String group = null;
        String sizebytes = null;
        String usenetage = null;
        String categoryid = null;
        String cattext = null;
        String weblink = null;
        String details = null;
        String getnzb = null;

        // Parse object
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();

            if (name.equalsIgnoreCase("nzbid")) {
                nzbid = readString(reader);
            } else if (name.equalsIgnoreCase("release")) {
                release = readString(reader);
            } else if (name.equalsIgnoreCase("group")) {
                group = readString(reader);
            } else if (name.equalsIgnoreCase("sizebytes")) {
                sizebytes = readString(reader);
            } else if (name.equalsIgnoreCase("usenetage")) {
                usenetage = readString(reader);
            } else if (name.equalsIgnoreCase("categoryid")) {
                categoryid = readString(reader);
            } else if (name.equalsIgnoreCase("cattext")) {
                cattext = readString(reader);
            } else if (name.equalsIgnoreCase("weblink")) {
                weblink = readString(reader);
            } else if (name.equalsIgnoreCase("details")) {
                details = readString(reader);
            } else if (name.equalsIgnoreCase("getnzb")) {
                getnzb = readString(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        NzbItem item = new NzbItem();
        item.setNzbid(nzbid);
        item.setRelease(release);
        item.setGroup(group);
        item.setSizebytes(sizebytes);
        item.setUsenetage(usenetage);
        item.setCategoryid(categoryid);
        item.setCattext(cattext);
        item.setWeblink(weblink);
        item.setDetails(details);
        item.setGetnzb(getnzb);

        response.getItems().add(item);
    }

    private String readString(JsonReader reader) throws IOException {
        switch (reader.peek()) {
            case NULL:
                reader.nextNull();
                return null;
            default:
                return reader.nextString();
        }
    }

}
