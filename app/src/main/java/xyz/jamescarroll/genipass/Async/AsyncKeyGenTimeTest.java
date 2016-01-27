/*
 * Copyright (c) 2016
 *
 * This file, AsyncKeyGenTimeTest.java, is apart of GeniPass.
 *
 * GeniPass is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * GeniPass is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with GeniPass.  If not, see http://www.gnu.org/licenses/.
 */

package xyz.jamescarroll.genipass.Async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.TimingLogger;

import xyz.jamescarroll.genipass.Crypto.ECKey;

/**
 * Created by James Carroll on 1/22/16.
 */
public class AsyncKeyGenTimeTest extends AsyncTask<Void, Integer, Void> {

    private Context mContext;
    private ProgressDialog mProgress;
    private TimingLogger mTime = new TimingLogger("ECTime", "Key gen");

    public AsyncKeyGenTimeTest(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgress = new ProgressDialog(mContext);
        mProgress.setTitle("Key generation test");
        mProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgress.setProgress(0);
        mProgress.setMax(10);
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.setCancelable(false);
        mProgress.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        for (int i = 0; i < 10; i++) {
            ECKey.ECTimeTest.timeTest(mTime, mContext, i);
            publishProgress(i + 1);
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        mProgress.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        mTime.dumpToLog();
        mProgress.dismiss();
    }
}
