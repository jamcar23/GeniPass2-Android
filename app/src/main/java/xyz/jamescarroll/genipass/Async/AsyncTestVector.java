/*
 * Copyright (c) 2016
 *
 * This file, AsyncTestVector.java, is apart of GeniPass.
 *
 * GeniPass is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * GeniPass is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with GeniPass.  If not, see http://www.gnu.org/licenses/.
 */

package xyz.jamescarroll.genipass.Async;

import android.content.Context;
import android.os.AsyncTask;

import java.math.BigInteger;
import java.util.Arrays;

import xyz.jamescarroll.genipass.Crypto.ECKey;
import xyz.jamescarroll.genipass.Crypto.Password;
import xyz.jamescarroll.genipass.Crypto.TestManager;

/**
 * Created by jamescarroll on 1/28/16.
 */
public class AsyncTestVector extends AsyncTask<Void, Integer, String> {
    private OnResult mListener;
    private Context mContext;
    private int mProgress = 0;

    public AsyncTestVector(OnResult mListener, Context mContext) {
        this.mListener = mListener;
        this.mContext = mContext;
    }

    @Override
    protected String doInBackground(Void... params) {
        TestManager.Vector[] vectors = TestManager.getInstance().getTestVectors(mContext);
        TestManager.Vector vec;
        String result = "", u, p;

        for (int i = 0; i < vectors.length; i++) {
            mProgress = i + 1;
            result += "--- Begin Test Vector " + mProgress + " ---\n\n";
            vec = vectors[i];

            u = ECKey.UnitTest.ripemd160Hex(vec.getmUsername());
            p = ECKey.UnitTest.ripemd160Hex(vec.getmPassword());
            vec.compareTo(u, vec.getmRipemdUsername(),
                    TestManager.Vector.kRipemdUsername);
            vec.compareTo(p, vec.getmRipemdPassword(),
                    TestManager.Vector.kRipemdPassword);

            u = ECKey.UnitTest.genMasterExtPrivateKey(u, p);
            p = ECKey.UnitTest.genExtPublicKey(u);
            vec.compareTo(u, vec.getmMasterExtPrivate(),
                    TestManager.Vector.kMasterExtPrivate);
            vec.compareTo(p, vec.getmMasterExtPublic(), TestManager.Vector.kMasterExtPublic);

            u = ECKey.UnitTest.genChildExtPrivatecKey(p, vec.getmService());
            p = ECKey.UnitTest.genExtPublicKey(u);
            vec.compareTo(u, vec.getmFirstChildExtPrivate(),
                    TestManager.Vector.kFirstChildExtPrivate);
            vec.compareTo(p, vec.getmFirstChildExtPublic(),
                    TestManager.Vector.kFirstChildExtPublic);

            u = ECKey.UnitTest.genChildExtPrivatecKey(p, vec.getmTag());
            p = ECKey.UnitTest.genExtPublicKey(u);
            vec.compareTo(u, vec.getmSecondChildExtPrivate(),
                    TestManager.Vector.kSecondChildExtPrivate);
            vec.compareTo(p, vec.getmSecondChildExtPublic(),
                    TestManager.Vector.kSecondChildExtPublic);

            u = Password.pickPassword(Arrays.copyOfRange(new BigInteger(p, 16).toByteArray(), 32,
                    64), mContext);
            vec.compareTo(u, vec.getmChildPassword(), TestManager.Vector.kGenPassword);

            result += vec.getmResult() + "\n--- End Test Vector " + mProgress + " ---\n\n";
            publishProgress(mProgress);

        }

        return result;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        mListener.onUpdate(values[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        TestManager.getInstance().endTest();
        mListener.onResult(s);
    }

    public int getmProgress() {
        return mProgress;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public void setmListener(OnResult mListener) {
        this.mListener = mListener;
    }

    public interface OnResult {
        void onResult(String result);
        void onUpdate(int i);
    }
}
