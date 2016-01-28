/*
 * Copyright (c) 2016
 *
 * This file, SettingsDetailActivity.java, is apart of GeniPass.
 *
 * GeniPass is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * GeniPass is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with GeniPass.  If not, see http://www.gnu.org/licenses/.
 */

package xyz.jamescarroll.genipass;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import xyz.jamescarroll.genipass.Async.AsyncTestVector;
import xyz.jamescarroll.genipass.Crypto.TestManager;


public class SettingsDetailActivity extends AppCompatActivity implements AsyncTestVector.OnResult {
    private static final String TAG = "SettingsDetailActivity";
    public static final String kExtraOpenLicense = TAG + ".OPEN_LICENSE";
    public static final String kExtraTestVector = TAG + "TEST_VECTOR";

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            Log.e(TAG, "onCreate: ", e);
        }

        findTextView().setMovementMethod(new ScrollingMovementMethod());

        if (getIntent() != null && getIntent().getAction() != null) {
            if (getIntent().getAction().equals(kExtraOpenLicense)) {
                handleOpenLicense();
            }

            if (getIntent().getAction().equals(kExtraTestVector)) {
                showProgress();
                TestManager.getInstance().setmStartTest(true);
            }
        }

        if (TestManager.getInstance().ismStartTest() && !TestManager.getInstance().ismEndTest()) {
            showProgress();
            new AsyncTestVector(this, this).execute();
        }
    }

    private void createProgress() {
        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Running Crypto Test");
        mProgress.setMessage("Please wait.");
        mProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.setCancelable(false);
        mProgress.setProgress(0);
        mProgress.setMax(3);
    }

    private void showProgress() {
        if (mProgress == null) {
            createProgress();
        }

        mProgress.show();
    }

    private void handleOpenLicense() {
        BufferedReader br = new BufferedReader(new InputStreamReader(
                getResources().openRawResource(R.raw.license)));
        String t, l = "";

        try {
            while ((t = br.readLine()) != null) {
                l += t;
            }

            findTextView().setText(l);
        } catch (IOException e) {
            Log.e(TAG, "handleOpenLicense: ", e);
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                Log.e(TAG, "handleOpenLicense: ", e);
            }
        }
    }

    private TextView findTextView() {
        return (TextView) findViewById(R.id.tv_settings_detail);
    }

    @Override
    public void onResult(String result) {
        findTextView().setText(result);
        mProgress.dismiss();
    }

    @Override
    public void onUpdate(int i) {
        mProgress.setProgress(i);
    }
}
