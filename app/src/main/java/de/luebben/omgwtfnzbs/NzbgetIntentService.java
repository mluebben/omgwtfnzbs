package de.luebben.omgwtfnzbs;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import net.nzbget.client.NzbgetClient;
import net.nzbget.client.PPParameter;

import org.omgwtfnzbs.search.NzbItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class NzbgetIntentService extends IntentService {

    public static final String TAG = "NzbgetIntentService";

    private static final String ACTION_APPEND = "de.luebben.omgwtfnzbs.action.APPEND";

    private static final String EXTRA_NZBITEM = "de.luebben.omgwtfnzbs.extra.NZBITEM";

    private static final Object lock = new Object();

    private static int nextId = 10;



    private NotificationManager mNotificationManager = null;

    public NzbgetIntentService() {
        super("NzbgetIntentService");
    }

    /**
     * Starts this service to perform action Append with the given parameters. If
     * the service is already performing a task this action will be queued.
     */
    public static void startActionAppend(Context context, NzbItem nzbItem) {
        Intent intent = new Intent(context, NzbgetIntentService.class);
        intent.setAction(ACTION_APPEND);
        intent.putExtra(EXTRA_NZBITEM, nzbItem);
        context.startService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_APPEND.equals(action)) {
                final NzbItem nzbItem = (NzbItem) intent.getSerializableExtra(EXTRA_NZBITEM);
                handleActionAppend(nzbItem);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionAppend(NzbItem nzbItem) {
        int notificationId = getNextId();
        try {
            notify(notificationId, new Notification.Builder(this)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle(nzbItem.getRelease())
                    .setContentText("Appending ...")
                    .setOngoing(true)
                    .build());

            SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);

            NzbgetClient client = new NzbgetClient();
            client.setEndpoint(p.getString(SettingsActivity.KEY_PREF_NZBGET_URL, null));
            client.setUsername(p.getString(SettingsActivity.KEY_PREF_NZBGET_USERNAME, null));
            client.setPassword(p.getString(SettingsActivity.KEY_PREF_NZBGET_PASSWORD, null));

            List<PPParameter> parameters = new ArrayList<PPParameter>();

            int nzbid = client.append("", nzbItem.getGetnzb(), "", 0, false, false, "", 0, "Score", parameters);
            if (nzbid <= 0) {
                notify(notificationId, new Notification.Builder(this)
                        .setContentTitle(nzbItem.getRelease())
                        .setContentText("Failed")
                        .setOngoing(true)
                        .build());
            }

            mNotificationManager.cancel(notificationId);

        } catch (Exception e) {
            Log.wtf(TAG, e);
            notify(notificationId, new Notification.Builder(this)
                    .setContentTitle(nzbItem.getRelease())
                    .setContentText("Failed: " + e.getMessage())
                    .setOngoing(false)
                    .build());
        }
    }

    private void notify(int notificationId, Notification notification) {
        if (mNotificationManager != null) {
            mNotificationManager.notify(notificationId, notification);
        }
    }

    private void cancel(int notificationId) {
        if (mNotificationManager != null) {
            mNotificationManager.cancel(notificationId);
        }
    }

    private int getNextId() {
        int id;
        synchronized (lock) {
            id = nextId++;
        }
        return id;
    }
}
