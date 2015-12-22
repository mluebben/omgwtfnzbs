package de.luebben.omgwtfnzbs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import org.omgwtfnzbs.search.NzbItem;
import org.omgwtfnzbs.search.SearchQuery;
import org.omgwtfnzbs.search.SearchResult;
import org.omgwtfnzbs.search.SearchTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private static final String KEY_SEARCH = "search";
    private static final String KEY_CAT = "cat";
    private static final String KEY_ITEMS = "items";
    private static final String KEY_SORTMODE = "sortmode";

    private static final int PICK_CATEGORIES_REQUEST = 3;

    private static final int SORT_BY_RELEASE_ASC = 0;
    private static final int SORT_BY_RELEASE_DESC = 1;
    private static final int SORT_BY_AGE_ASC = 2;
    private static final int SORT_BY_AGE_DESC = 3;

    private Toolbar mToolbar = null;

    private SearchView mSearchView = null;

    private RecyclerView mListView = null;

    private ProgressBar mProgressBar = null;

    private String mCatid = null;

    private NzbItemAdapter mAdapter = null;

    private SearchAsyncTask mSearchTask = null;


    private static final Comparator<NzbItem> SORT_BY_RELEASE_COMPARATOR = new Comparator<NzbItem>() {
        @Override
        public int compare(NzbItem lhs, NzbItem rhs) {
            String a = lhs.getRelease();
            String b = rhs.getRelease();
            return a.compareToIgnoreCase(b);
        }
    };

    private static final Comparator<NzbItem> SORT_BY_AGE_COMPARATOR = new Comparator<NzbItem>() {
        @Override
        public int compare(NzbItem lhs, NzbItem rhs) {
            long a = Long.parseLong(lhs.getUsenetage());
            long b = Long.parseLong(rhs.getUsenetage());
            return Long.compare(a, b);
        }
    };

    private int mSortMode = SORT_BY_RELEASE_ASC;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);

        if (savedInstanceState == null) {
            savedInstanceState = new Bundle();
        }

        mAdapter = new NzbItemAdapter(this);
        mAdapter.setData((ArrayList<NzbItem>) savedInstanceState.getSerializable(KEY_ITEMS));
        mAdapter.setOnItemClickedListener(new NzbItemAdapter.OnItemClickedListener() {
            @Override
            public void onItemClicked(NzbItemAdapter adapter, int position) {
                NzbItem nzbItem = adapter.getItem(position);
                if (nzbItem == null) {
                    return;
                }

                showDetails(nzbItem);
            }
        });
        mAdapter.setOnAddClickedListener(new NzbItemAdapter.OnAddClickedListener() {
            @Override
            public void onAddClicked(NzbItemAdapter adapter, int position) {
                NzbItem nzbItem = adapter.getItem(position);
                if (nzbItem != null) {
                    append(nzbItem);
                }
            }
        });

        mSortMode = savedInstanceState.getInt(KEY_SORTMODE, SORT_BY_RELEASE_ASC);

        mSearchView = (SearchView) findViewById(R.id.search);
        mSearchView.setQuery(savedInstanceState.getString(KEY_SEARCH, ""), false);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mListView = (RecyclerView) findViewById(R.id.list);
        mListView.setLayoutManager(new LinearLayoutManager(this));
        mListView.setAdapter(mAdapter);

        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
    }


    @Override
    protected void onStart() {
        super.onStart();
        startLogoAnimation();
    }


    @Override
    protected void onPause() {
        super.onPause();
        stopLogoAnimation();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_categories:
                showCategories();
                return true;
            case R.id.action_sort_release_asc:
                setSortMode(SORT_BY_RELEASE_ASC);
                return true;
            case R.id.action_sort_release_desc:
                setSortMode(SORT_BY_RELEASE_DESC);
                return true;
            case R.id.action_sort_age_asc:
                setSortMode(SORT_BY_AGE_ASC);
                return true;
            case R.id.action_sort_age_desc:
                setSortMode(SORT_BY_AGE_DESC);
                return true;
            case R.id.action_settings:
                showSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_SEARCH, mSearchView.getQuery().toString());
        outState.putString(KEY_CAT, mCatid);
        outState.putSerializable(KEY_ITEMS, new ArrayList<>(mAdapter.getItems()));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CATEGORIES_REQUEST && resultCode == RESULT_OK) {
            mCatid = data.getStringExtra(CategoryActivity.EXTRA_CATID);
        }
    }


    private void showCategories() {
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra(CategoryActivity.EXTRA_CATID, mCatid);
        startActivityForResult(intent, PICK_CATEGORIES_REQUEST);
    }


    private void showSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }


    private void showDetails(NzbItem nzbItem) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(DetailsActivity.EXTRA_ITEM, nzbItem);
        startActivity(intent);
    }



    private void performSearch() {
        String search = mSearchView.getQuery().toString();
        if (search == null) {
            search = "";
        }

        String catid = mCatid;
        if (catid == null) {
            catid = "";
        }

        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);

        String user = p.getString(SettingsActivity.KEY_PREF_OMGWTFNZBS_USERNAME, "");
        String api = p.getString(SettingsActivity.KEY_PREF_OMGWTFNZBS_APIKEY, "");
        if (user == null || api == null || user.isEmpty() || api.isEmpty()) {
            Toast.makeText(this, "Goto Settings first", Toast.LENGTH_SHORT).show();
            return;
        }

        SearchQuery q = new SearchQuery();
        q.setSearch(search);
        q.setCatid(catid);
        q.setUser(user);
        q.setApi(api);

        if (mSearchTask != null) {
            mSearchTask.cancel(true);
        }

        mSearchTask = new SearchAsyncTask();
        mSearchTask.execute(q);
    }


    private void startLogoAnimation() {
        Drawable logo = mToolbar.getLogo();
        if (logo instanceof  AnimationDrawable) {
            ((AnimationDrawable) logo).start();
        }
    }


    private void stopLogoAnimation() {
        Drawable logo = mToolbar.getLogo();
        if (logo instanceof  AnimationDrawable) {
            ((AnimationDrawable) logo).stop();
        }
    }


    private void append(NzbItem nzbItem) {
        NzbgetIntentService.startActionAppend(this, nzbItem);
    }


    static class SearchContext {

        public static final String TAG = "SearchContext";


        private SearchQuery mQuery = null;

        private SearchResult mResult = null;

        private Throwable mError = null;


        public void setQuery(SearchQuery query) {
            this.mQuery = query;
        }

        public SearchQuery getQuery() {
            return mQuery;
        }


        public void setResult(SearchResult result) {
            this.mResult = result;
        }

        public SearchResult getResult() {
            return mResult;
        }


        public void setError(Throwable error) {
            this.mError = error;
        }

        public Throwable getError() {
            return mError;
        }
    }


    class SearchAsyncTask extends AsyncTask<SearchQuery, Void, SearchContext> {

        public static final String TAG = "SearchAsyncTask";

        @Override
        protected void onPreExecute() {
            mListView.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected SearchContext doInBackground(SearchQuery... params) {
            SearchContext context = null;

            try {
                context = new SearchContext();
                context.setQuery(params[0]);
                context.setResult(new SearchTask().search(params[0]));

            } catch (Throwable e) {
                if (context != null) {
                    context.setError(e);
                } else {
                    Log.wtf(TAG, e);
                }
            }

            return context;
        }


        @Override
        protected void onPostExecute(SearchContext context) {
            mProgressBar.setVisibility(View.GONE);

            if (isCancelled()) {
                return;
            }

            mListView.setVisibility(View.VISIBLE);

            if (context == null) {
                Toast.makeText(MainActivity.this, "This a classic wtf moment?", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error during search: unknown reason");
                return;
            }

            Throwable e = context.getError();
            if (e != null) {
                String message = e.getMessage();
                if (message == null) {
                    message = "null";
                }
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error during search: " + message);
                return;
            }

            SearchResult result = context.getResult();
            if (result == null) {
                Toast.makeText(MainActivity.this, "no response", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error during search: no response");
                return;
            }

            String notice = result.getNotice();
            if (notice != null && !notice.isEmpty()) {
                Toast.makeText(MainActivity.this, notice, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error during search: " + notice);
                return;
            }

            mAdapter.setData(sort(mSortMode, result.getItems()));
        }
    }


    private void setSortMode(int sortMode) {
        if (mSortMode != sortMode) {
            mSortMode = sortMode;
            mAdapter.setData(sort(sortMode, mAdapter.getItems()));
        }
    }


    private ArrayList<NzbItem> sort(int sortMode, List<NzbItem> nzbItems) {
        ArrayList<NzbItem> copy = new ArrayList<>(nzbItems.size());
        copy.addAll(nzbItems);

        switch (sortMode) {
            case SORT_BY_RELEASE_ASC:
                Collections.sort(copy, SORT_BY_RELEASE_COMPARATOR);
                break;

            case SORT_BY_RELEASE_DESC:
                Collections.sort(copy, SORT_BY_RELEASE_COMPARATOR);
                Collections.reverse(copy);
                break;

            case SORT_BY_AGE_ASC:
                Collections.sort(copy, SORT_BY_AGE_COMPARATOR);
                break;

            case SORT_BY_AGE_DESC:
                Collections.sort(copy, SORT_BY_AGE_COMPARATOR);
                Collections.reverse(copy);
                break;
        }

        return copy;
    }
}
