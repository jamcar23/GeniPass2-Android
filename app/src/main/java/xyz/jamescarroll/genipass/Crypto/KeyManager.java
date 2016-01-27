/*
 * Copyright (c) 2016
 *
 * This file, KeyManager.java, is apart of GeniPass.
 *
 * GeniPass is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * GeniPass is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with GeniPass.  If not, see http://www.gnu.org/licenses/.
 */

package xyz.jamescarroll.genipass.Crypto;

import xyz.jamescarroll.genipass.Async.AsyncKeyGen;

/**
 * Created by James Carroll on 1/23/16.
 */
public class KeyManager implements AsyncKeyGen.OnKeyGeneration {
    private static KeyManager ourInstance = new KeyManager();
    private ECKey mMaster = null;
    private ECKey mChild = null;
    private ControlUI mControl;
    private boolean mMasterBegin = false;
    private boolean mRequestChildKeys = false;

    private KeyManager() {
    }

    public static KeyManager getInstance() {
        return ourInstance;
    }

    public boolean ismMasterBegin() {
        return mMasterBegin;
    }

    public void setmMasterBegin(boolean mMasterBegin) {
        this.mMasterBegin = mMasterBegin;
    }

    public boolean ismRequestChildKeys() {
        return mRequestChildKeys;
    }

    public void setmRequestChildKeys(boolean mRequestChildKeys) {
        this.mRequestChildKeys = mRequestChildKeys;
    }

    public void setmMaster(ECKey mMaster) {
        this.mMaster = mMaster;
    }

    public ECKey getMasterKey() {
        return mMaster;
    }

    public void setmChild(ECKey mChild) {
        this.mChild = mChild;
    }

    public ECKey getChildKey() {
        return mChild;
    }

    public void clearChildKey() {
        mChild = null;
        mRequestChildKeys = false;
    }

    public void clear() {
        clearChildKey();
        mMaster = null;
        mControl = null;
        mMasterBegin = false;
    }

    public void setmControl(ControlUI mControl) {
        this.mControl = mControl;
    }

    public boolean isMasterKeyFinished() {
        return mMaster != null && mMaster.getmChain() != null;
    }

    public boolean isChildKeyFinished() {
        return mChild != null && mChild.getmKey() != null && mChild.getmKey().length > 0;
    }

    @Override
    public void onKeyGeneration(ECKey key) {
        if (mControl != null) {
            if (key.ismMaster()) {
                setmMaster(key);

                if (ismRequestChildKeys()) {
                    mControl.callAsyncChildKeyGen();
                }
            } else {
                setmChild(key);
                mControl.dismissProgressDialog();
                mControl.toPasswordActivity();
            }
        }
    }

    public interface ControlUI {
        void callAsyncChildKeyGen();
        void dismissProgressDialog();
        void toPasswordActivity();
    }
}
