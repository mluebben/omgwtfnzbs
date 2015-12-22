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

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;


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
public class SettingsActivity extends AppCompatActivity {

    public static final String TAG = "SettingsActivity";

    public static final String KEY_PREF_OMGWTFNZBS_USERNAME = "omgwtfnzbs_username";

    public static final String KEY_PREF_OMGWTFNZBS_APIKEY = "omgwtfnzbs_apikey";

    public static final String KEY_PREF_NZBGET_URL = "nzbget_url";

    public static final String KEY_PREF_NZBGET_USERNAME = "nzbget_username";

    public static final String KEY_PREF_NZBGET_PASSWORD = "nzbget_password";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
