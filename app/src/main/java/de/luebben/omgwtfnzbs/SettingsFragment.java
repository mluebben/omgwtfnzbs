/////////////////////////////////////////////////////////////////////////////
// $Id: SettingsActivity.java 4 2013-04-15 21:06:51Z ml $
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
// Purpose:      Settings activity (application configuration)
// Created:      30.11.2015 (dd.mm.yyyy)
/////////////////////////////////////////////////////////////////////////////

package de.luebben.omgwtfnzbs;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;


/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsFragment extends PreferenceFragment {

    public static final String TAG = "SettingsFragment";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);

        bindPreferenceSummaryToValue(SettingsActivity.KEY_PREF_OMGWTFNZBS_USERNAME, R.string.pref_omgwtfnzbs_username_summary);
        bindPreferenceSummaryToValue(SettingsActivity.KEY_PREF_OMGWTFNZBS_APIKEY, R.string.pref_omgwtfnzbs_apikey_summary);

        bindPreferenceSummaryToValue(SettingsActivity.KEY_PREF_NZBGET_URL, R.string.pref_nzbget_url_summary);
        bindPreferenceSummaryToValue(SettingsActivity.KEY_PREF_NZBGET_USERNAME, R.string.pref_nzbget_username_summary);
        bindPreferenceSummaryToValue(SettingsActivity.KEY_PREF_NZBGET_PASSWORD, R.string.pref_nzbget_password_summary);
    }


    private static class PreferenceSummaryToValueBinder implements Preference.OnPreferenceChangeListener {
        private int defaultSummaryResId;

        public PreferenceSummaryToValueBinder(int defaultSummaryResId) {
            this.defaultSummaryResId = defaultSummaryResId;
        }

        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (stringValue.length() == 0) {
                preference.setSummary(defaultSummaryResId);
            } else {
                if (SettingsActivity.KEY_PREF_NZBGET_PASSWORD.equals(preference.getKey())) {
                    stringValue = "Password is entered";
                }

                preference.setSummary(stringValue);
            }

            return true;
        }
    };


    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     */
    private void bindPreferenceSummaryToValue(String preferenceKey, int defaultSummaryResId) {
        if (preferenceKey == null || preferenceKey.isEmpty()) {
            return;
        }

        Preference preference = findPreference(preferenceKey);
        if (preference == null) {
            return;
        }

        String defaultValue = "";

        PreferenceSummaryToValueBinder l = new PreferenceSummaryToValueBinder(defaultSummaryResId);

        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(l);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());

        // Trigger the listener immediately with the preference's current value.
        l.onPreferenceChange(
                preference,
                preferences.getString(preference.getKey(), defaultValue));
    }
}
