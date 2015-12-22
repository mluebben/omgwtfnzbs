package net.nzbget.client;

import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;

/**
 * NZBGet JSON-RPC Client.
 *
 * @author mluebben
 */
public class NzbgetClient {

    public static final String TAG = "NzbgetClient";

    public static final String DUPEMODE_SCORE = "Score";
    public static final String DUPEMODE_ALL = "All";
    public static final String DUPEMODE_FORCE = "Force";

    /** HTTP connect timeout in milliseconds, 30 seconds. */
    private static final int CONNECT_TIMEOUT = 30 * 1000;
    /** HTTP read timeout in milliseconds, 30 seconds. */
    private static final int READ_TIMEOUT = 30 * 1000;

    /** NZBGet URL. */
    private String mEndpoint;
    /** NZBGet username. */
    private String mUsername;
    /** NZBGet password. */
    private String mPassword;


    public void setEndpoint(String endpoint) {
        mEndpoint = endpoint;
    }

    public String getEndpoint() {
        return mEndpoint;
    }


    public void setUsername(String username) {
        mUsername = username;
    }

    public String getUsername() {
        return mUsername;
    }


    public void setPassword(String password) {
        mPassword = password;
    }

    public String getPassword() {
        return mPassword;
    }


    /**
     *
     * @param NZBFilename
     *     Name of nzb-file. Server uses the name to build destination directory. This name can contain full path or only filename.
     * @param NZBContent
     *     Content of nzb-file encoded with Base64 or URL to fetch nzb-file from.
     * @param Category
     *     Category for nzb-file. Can be empty string.
     * @param Priority
     *     Priority for nzb-file. 0 for "normal priority", positive values for high priority and
     *     negative values for low priority. Downloads with priorities equal to or greater than 900
     *     are downloaded and post-processed even if the program is in paused state (force mode).
     *     Default priorities are: -100 (very low), -50 (low), 0 (normal), 50 (high), 100 (very high), 900 (force).
     * @param AddToTop
     *     "True" if the file should be added to the top of the download queue or "False" if to the end.
     * @param AddPaused
     *     "True" if the file should be added in paused state.
     * @param DupeKey
     *     Duplicate key for nzb-file.
     * @param DupeScore
     *     Duplicate score for nzb-file.
     * @param DupeMode
     *     Duplicate mode for nzb-file.
     *     'Score' - this is default duplicate mode. Only nzb-files with higher scores (when already downloaded) are downloaded;
     *     'All' - all nzb-files regardless of their scores are downloaded;
     *     'Force' - force download and disable all duplicate checks.
     * @param PPParameters
     *     Post-processing parameters. The array consists of structures with following fields:
     *     Name (string) - name of post-processing parameter.
     *     Value (string) - value of post-processing parameter.
     * @return
     *     Positive number representing NZBID of the queue item. "0" and negative numbers represent
     *     error codes. Current version uses only error code "0", newer versions may use other error
     *     codes for detailed information about error.
     */
    public int append(String NZBFilename, String NZBContent, String Category,
               int Priority, boolean AddToTop, boolean AddPaused, String DupeKey,
               int DupeScore, String DupeMode, Collection<PPParameter> PPParameters) throws NzbgetException {

        try {
            JSONArray params = new JSONArray();
            params.put(NZBFilename);
            params.put(NZBContent);
            params.put(Category);
            params.put(Priority);
            params.put(AddToTop);
            params.put(AddPaused);
            params.put(DupeKey);
            params.put(DupeScore);
            params.put(DupeMode);

            JSONObject request = new JSONObject();
            request.put("method", "append");
            request.put("params", params);
            request.put("id", "1");

            JSONObject response = post(request);

            JSONObject error = response.optJSONObject("error");
            if (error != null) {
                String message = error.getString("message");
                throw new NzbgetException(message);
            }

            return response.getInt("result");

        } catch (JSONException e) {
            throw new NzbgetException(e.getMessage(), e);
        } catch (IOException e) {
            throw new NzbgetException(e.getMessage(), e);
        }
    }





    private JSONObject post(JSONObject requestData) throws JSONException, IOException {
        if (mEndpoint == null || mEndpoint.isEmpty()) {
            throw new IllegalStateException("Endpoint must be set");
        }

        // Create URL instance
        URL url = new URL(Uri.parse(mEndpoint).buildUpon().appendPath("jsonrpc").toString());

        // Extract username and password from url
        String username = null;
        if (mUsername != null && !mUsername.isEmpty()) {
            username = mUsername;
        }

        String password = null;
        if (mPassword != null && !mPassword.isEmpty()) {
            password = mPassword;
        }

        // Create Connection instance
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();

        // Setup HTTP URL connection object
        urlConn.setInstanceFollowRedirects(false);
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        urlConn.setUseCaches(false);
        urlConn.setRequestMethod("POST");
        urlConn.setRequestProperty("Content-Type", "application/json");

        // Add basic authorization header to request if username and password is specified.
        if (username != null && password != null) {
            String userpass = username + ":" + password;
            String basicAuth = "Basic " + Base64.encodeToString(userpass.getBytes(), Base64.NO_WRAP);
            urlConn.setRequestProperty ("Authorization", basicAuth);
        }

        // Define timeouts
        urlConn.setConnectTimeout(CONNECT_TIMEOUT);
        urlConn.setReadTimeout(READ_TIMEOUT);

        // Establish connection to server
        urlConn.connect();

        // Send request data
        DataOutputStream output = new DataOutputStream(urlConn.getOutputStream());

        // Construct the POST data.
        String content = requestData.toString();

        Log.d(TAG, "Request: " + content);

        // Send the request data.
        output.writeBytes(content);
        output.flush();
        output.close();

        int responseCode = urlConn.getResponseCode();  // can call this instead of con.connect()
        if (responseCode >= 400 && responseCode <= 499) {
            throw new IOException("Bad authentication status: " + responseCode); // provide a more meaningful exception message
        }

        // Get response data.
        InputStream inStream = null;
        Reader inReader = null;
        try {
            // Open input stream to read response from the server
            inStream = urlConn.getInputStream();
            inReader = new InputStreamReader(inStream, "UTF-8");

            // Create string from response data
            char[] buf = new char[4096];
            int len;
            StringBuilder sb = new StringBuilder();
            while ((len = inReader.read(buf)) > 0) {
                sb.append(buf, 0, len);
            }

            Log.d(TAG, "Response: " + sb.toString());

            // Return response object
            return new JSONObject(sb.toString());

        } finally {
            // Ensure input reader is closed
            if (inReader != null) {
                try {
                    inReader.close();
                } catch (Throwable e) {
                    Log.wtf(TAG, e);
                }
            }
            // Ensure input stream is closed
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (Throwable e) {
                    Log.wtf(TAG, e);
                }
            }
        }
    }
}
