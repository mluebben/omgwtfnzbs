package de.luebben.omgwtfnzbs;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.omgwtfnzbs.search.NzbItem;

public class DetailsActivity extends AppCompatActivity {

    public static final String TAG = "DetailsActivity";

    public static final String EXTRA_ITEM = "item";

    private WebView mWebView = null;

    private NzbItem mNzbItem = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mNzbItem = (NzbItem) extras.getSerializable(EXTRA_ITEM);
        } else {
            mNzbItem = null;
        }

        String title = mNzbItem == null ? null : mNzbItem.getRelease();
        if (title != null && !title.isEmpty()) {
            setTitle(title);
        }

        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());

        navigate();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_web:
                showDetailsInBrowser();
                return true;
            case R.id.action_nzb:
                append();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void showDetailsInBrowser() {
        try {
            String url = mNzbItem == null ? null : mNzbItem.getDetails();
            if (url == null || url.isEmpty()) {
                return;
            }

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(url), "text/html");
            intent.addCategory(Intent.CATEGORY_DEFAULT);

            startActivity(intent);

        } catch (ActivityNotFoundException e) {
            Log.wtf(TAG, e);
            Toast.makeText(this, "No app to open a website is installed.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.wtf(TAG, e);
            Toast.makeText(this, "An error occurred.", Toast.LENGTH_SHORT).show();
        }
    }


    private void append() {
        NzbgetIntentService.startActionAppend(this, mNzbItem);
    }


    private void navigate() {
        if (mWebView == null) {
            return;
        }

        String url = mNzbItem == null ? null : mNzbItem.getDetails();
        if (url == null || url.isEmpty()) {
            return;
        }

        mWebView.loadUrl(url);
    }
}
