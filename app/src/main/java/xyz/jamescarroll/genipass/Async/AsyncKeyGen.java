/*
 * Copyright (c) 2016
 *
 * This file, AsyncKeyGen.java, is apart of GeniPass.
 *
 * GeniPass is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * GeniPass is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with GeniPass.  If not, see http://www.gnu.org/licenses/.
 */

package xyz.jamescarroll.genipass.Async;

import android.os.AsyncTask;

import xyz.jamescarroll.genipass.Crypto.ECKey;
import xyz.jamescarroll.genipass.Crypto.KeyManager;

/**
 * Created by James Carroll on 1/23/16.
 */
public abstract class AsyncKeyGen extends  AsyncTask<String, Void, ECKey> {

    protected OnKeyGeneration mListener;

    public AsyncKeyGen() {
        mListener = KeyManager.getInstance();
    }

    protected OnKeyGeneration getmListener() {
        return mListener;
    }

    public interface OnKeyGeneration {
        void onKeyGeneration(ECKey key);
    }
}
