package xyz.jamescarroll.genipass.Async;

import android.content.Context;
import android.os.AsyncTask;

import xyz.jamescarroll.genipass.Crypto.ECKey;

/**
 * Created by James Carroll on 1/23/16.
 */
public abstract class AsyncKeyGen extends AsyncTask<String, Void, ECKey> {

    protected OnKeyGeneration mListner;

    public AsyncKeyGen() {
    }

    protected OnKeyGeneration getmListner() {
        return mListner;
    }

    protected void setmListner(Context context) {
        if (context instanceof OnKeyGeneration) {
            mListner = (OnKeyGeneration) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AsyncKeyGen.OnKeyGen");
        }
    }

    public interface OnKeyGeneration {
        void onKeyGeneration(ECKey key);
    }
}
