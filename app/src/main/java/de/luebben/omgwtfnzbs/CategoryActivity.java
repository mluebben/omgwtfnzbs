package de.luebben.omgwtfnzbs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import org.omgwtfnzbs.search.SearchTask;


public class CategoryActivity extends AppCompatActivity {

    public static final String TAG = "CategoryActivity";

    public static final String EXTRA_CATID = "catid";

    /** Checkbox ids for group movies. */
    private static final int[] MOVIE_CHECKBOX_IDS = {
            R.id.cat_movies_std_checkbox,
            R.id.cat_movies_hd_checkbox,
            R.id.cat_movies_dvd_checkbox,
            R.id.cat_movies_other_checkbox
    };

    /** Checkbox ids for group tv. */
    private static final int[] TV_CHECKBOX_IDS = {
            R.id.cat_tv_std_checkbox,
            R.id.cat_tv_hd_checkbox,
            R.id.cat_tv_other_checkbox
    };

    /** Checkbox ids for group music. */
    private static final int[] MUSIC_CHECKBOX_IDS = {
            R.id.cat_music_mp3_checkbox,
            R.id.cat_music_flac_checkbox,
            R.id.cat_music_mvid_checkbox,
            R.id.cat_music_other_checkbox,
            R.id.cat_audiobook_checkbox
    };

    /** Checkbox ids for group games. */
    private static final int[] GAMES_CHECKBOX_IDS = {
            R.id.cat_games_pc_checkbox,
            R.id.cat_games_mac_checkbox,
            R.id.cat_games_other_checkbox
    };

    /** Checkbox ids for group apps. */
    private static final int[] APPS_CHECKBOX_IDS = {
            R.id.cat_apps_pc_checkbox,
            R.id.cat_apps_mac_checkbox,
            R.id.cat_apps_linux_checkbox,
            R.id.cat_apps_phone_checkbox,
            R.id.cat_apps_other_checkbox
    };

    /** Checkbox ids for group xxx. */
    private static final int[] XXX_CHECKBOX_IDS = {
            R.id.cat_xxx_clips_std_checkbox,
            R.id.cat_xxx_clips_std_checkbox,
            R.id.cat_xxx_movies_std_checkbox,
            R.id.cat_xxx_movies_hd_checkbox,
            R.id.cat_xxx_dvd_checkbox,
            R.id.cat_xxx_other_checkbox
    };

    /** Checkbox ids for group other. */
    public static final int[] OTHER_CHECKBOX_IDS = {
            R.id.cat_other_ebooks_checkbox,
            R.id.cat_other_extra_pars_checkbox,
            R.id.cat_other_other_checkbox
    };

    /** Mapping between checkbox ids and category ids. */
    private static final int[][] map = {
            { R.id.cat_movies_std_checkbox, SearchTask.CAT_MOVIES_STD },
            { R.id.cat_movies_hd_checkbox, SearchTask.CAT_MOVIES_HD },
            { R.id.cat_movies_dvd_checkbox, SearchTask.CAT_MOVIES_DVD },
            { R.id.cat_movies_other_checkbox, SearchTask.CAT_MOVIES_OTHER },
            { R.id.cat_tv_std_checkbox, SearchTask.CAT_TV_STD },
            { R.id.cat_tv_hd_checkbox, SearchTask.CAT_TV_HD },
            { R.id.cat_tv_other_checkbox, SearchTask.CAT_TV_OTHER },
            { R.id.cat_music_mp3_checkbox, SearchTask.CAT_MUSIC_MP3 },
            { R.id.cat_music_flac_checkbox, SearchTask.CAT_MUSIC_FLAC },
            { R.id.cat_music_mvid_checkbox, SearchTask.CAT_MUSIC_MVID },
            { R.id.cat_music_other_checkbox, SearchTask.CAT_MUSIC_OTHER },
            { R.id.cat_audiobook_checkbox, SearchTask.CAT_AUDIOBOOK },
            { R.id.cat_games_pc_checkbox, SearchTask.CAT_GAMES_PC },
            { R.id.cat_games_mac_checkbox, SearchTask.CAT_GAMES_MAC },
            { R.id.cat_games_other_checkbox, SearchTask.CAT_GAMES_OTHER },
            { R.id.cat_apps_pc_checkbox, SearchTask.CAT_APPS_PC },
            { R.id.cat_apps_mac_checkbox, SearchTask.CAT_APPS_MAC },
            { R.id.cat_apps_linux_checkbox, SearchTask.CAT_APPS_LINUX },
            { R.id.cat_apps_phone_checkbox, SearchTask.CAT_APPS_PHONE },
            { R.id.cat_apps_other_checkbox, SearchTask.CAT_APPS_OTHER },
            { R.id.cat_xxx_clips_std_checkbox, SearchTask.CAT_XXX_SD_CLIPS },
            { R.id.cat_xxx_clips_hd_checkbox, SearchTask.CAT_XXX_HD_CLIPS },
            { R.id.cat_xxx_movies_std_checkbox, SearchTask.CAT_XXX_SD_MOVIES },
            { R.id.cat_xxx_movies_hd_checkbox, SearchTask.CAT_XXX_HD_MOVIES },
            { R.id.cat_xxx_dvd_checkbox, SearchTask.CAT_XXX_DVD },
            { R.id.cat_xxx_other_checkbox, SearchTask.CAT_XXX_OTHER },
            { R.id.cat_other_ebooks_checkbox, SearchTask.CAT_OTHER_EBOOKS },
            { R.id.cat_other_extra_pars_checkbox, SearchTask.CAT_OTHER_EXTRA_PARS },
            { R.id.cat_other_other_checkbox, SearchTask.CAT_OTHER_OTHER }
    };


    /** Flag to indicate if we are updating checkbox states. */
    private boolean mUpdating = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        initListener(R.id.cat_movies_checkbox, MOVIE_CHECKBOX_IDS);
        initListener(R.id.cat_tv_checkbox, TV_CHECKBOX_IDS);
        initListener(R.id.cat_music_checkbox, MUSIC_CHECKBOX_IDS);
        initListener(R.id.cat_games_checkbox, GAMES_CHECKBOX_IDS);
        initListener(R.id.cat_apps_checkbox, APPS_CHECKBOX_IDS);
        initListener(R.id.cat_xxx_checkbox, XXX_CHECKBOX_IDS);
        initListener(R.id.cat_other_checkbox, OTHER_CHECKBOX_IDS);

        String catid = "15,16";

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            catid = extras.getString(EXTRA_CATID, catid);
        }

        load(catid);
    }


    private void load(String catid) {
        for (int[] x : map) {
            CheckBox cb = (CheckBox) findViewById(x[0]);
            cb.setChecked(false);
        }

        for (String id : catid.split(",")) {
            int n = Integer.parseInt(id.trim());

            for (int[] x : map) {
                if (x[1] == n) {
                    CheckBox cb = (CheckBox) findViewById(x[0]);
                    cb.setChecked(true);
                }
            }

        }
    }


    private String getString() {
        String out = "";
        for (int[] x : map) {
            CheckBox cb = (CheckBox) findViewById(x[0]);
            if (cb.isChecked()) {
                if (!out.isEmpty()) {
                    out += ",";
                }
                out += "" + x[1];
            }
        }
        return out;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_categories, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_ok:
                done();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void done() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_CATID, getString());
        setResult(RESULT_OK, intent);
        finish();
    }


    private void initListener(int masterId, int[] slaveIds) {
        CheckBox cb = (CheckBox) findViewById(masterId);
        cb.setOnCheckedChangeListener(new MasterListener(slaveIds));

        for (int slaveId : slaveIds) {
            cb = (CheckBox) findViewById(slaveId);
            cb.setOnCheckedChangeListener(new SlaveListener(masterId, slaveId, slaveIds));
        }
    }


    private class MasterListener implements CompoundButton.OnCheckedChangeListener {

        private int[] ids;

        public MasterListener(int[] ids) {
            this.ids = ids;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (mUpdating) {
                return;
            }

            mUpdating = true;
            for (int id : ids) {
                CheckBox cb = (CheckBox) findViewById(id);
                cb.setChecked(isChecked);
            }
            mUpdating = false;
        }
    }


    private class SlaveListener implements CompoundButton.OnCheckedChangeListener {

        private int masterId;
        private int slaveId;
        private int[] slaveIds;

        private SlaveListener(int masterId, int slaveId, int[] slaveIds) {
            this.masterId = masterId;
            this.slaveId = slaveId;
            this.slaveIds = slaveIds;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            boolean check = true;
            for (int id : slaveIds) {
                if (id != slaveId) {
                    check = check && ((CheckBox) findViewById(id)).isChecked();
                } else {
                    check = check && isChecked;
                }
            }

            if (mUpdating) {
                return;
            }

            mUpdating = true;
            ((CheckBox) findViewById(masterId)).setChecked(check);
            mUpdating = false;
        }

    }
}
