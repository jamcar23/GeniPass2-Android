/*
 * Copyright (c) 2016
 *
 * This file, SettingsFragment.java, is apart of GeniPass.
 *
 * GeniPass is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * GeniPass is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with GeniPass.  If not, see http://www.gnu.org/licenses/.
 */

package xyz.jamescarroll.genipass.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import xyz.jamescarroll.genipass.Crypto.TestManager;
import xyz.jamescarroll.genipass.R;
import xyz.jamescarroll.genipass.SettingsDetailActivity;

/**
 * Created by jamescarroll on 1/27/16.
 */
public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

    public static final String TAG = "SettingsFragment.TAG";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_debug);
        findPreference(getString(R.string.btn_test_vectors)).setOnPreferenceClickListener(this);
        findPreference(getString(R.string.btn_privacy)).setOnPreferenceClickListener(this);

    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        final String key = preference.getKey();
        Intent toDetail = new Intent(getActivity(), SettingsDetailActivity.class);

        if (key.equals(getString(R.string.btn_test_vectors))) {
            TestManager.getInstance().clearTest();
            toDetail.setAction(SettingsDetailActivity.kExtraTestVector);
        } else if (key.equals(getString(R.string.btn_privacy))) {
            toDetail.setAction(SettingsDetailActivity.kExtraPrivacyPolicy);
        }

        startActivity(toDetail);

        return true;
    }
}
