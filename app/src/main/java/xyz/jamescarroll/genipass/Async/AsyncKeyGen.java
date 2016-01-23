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
