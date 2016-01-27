/*
 * Copyright (c) 2016
 *
 * This file, PasswordActivity.java, is apart of GeniPass.
 *
 * GeniPass is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * GeniPass is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with GeniPass.  If not, see http://www.gnu.org/licenses/.
 */

package xyz.jamescarroll.genipass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import xyz.jamescarroll.genipass.Fragment.ExtFragment;
import xyz.jamescarroll.genipass.Fragment.PasswordFragment;

public class PasswordActivity extends AppCompatActivity implements ExtFragment.OnFragmentInteractionListener{
    private static final String TAG = "PasswordActivity.TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportFragmentManager().beginTransaction().replace(R.id.fl_frag,
                findPasswordFragment(), PasswordFragment.TAG).commit();
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            Log.e(TAG, "onCreate: ", e);
        }

    }

    public PasswordFragment findPasswordFragment() {
        PasswordFragment pf = (PasswordFragment) getSupportFragmentManager().findFragmentByTag(
                PasswordFragment.TAG);

        if (pf == null) {
            pf = new PasswordFragment();
        }

        return pf;
    }

    @Override
    public void onFragmentInteraction(Intent frag) {

    }
}
