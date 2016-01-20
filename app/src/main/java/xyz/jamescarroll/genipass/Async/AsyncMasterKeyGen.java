package xyz.jamescarroll.genipass.Async;

import android.content.Context;
import android.os.AsyncTask;

import xyz.jamescarroll.genipass.Crypto.ECKey;

/**
 * Created by James Carroll on 1/20/16.
 */
public class AsyncMasterKeyGen extends AsyncTask<String, Void, ECKey> {

    private OnKeyGeneration mListner;

    public AsyncMasterKeyGen(Context context) {
        this.mListner = (OnKeyGeneration) context;
    }

    @Override
    protected ECKey doInBackground(String... params) {
        return ECKey.genFromSeeds(params[0], params[1]);
    }

    @Override
    protected void onPostExecute(ECKey key) {
        mListner.onKeyGeneration(key);
    }

    public interface OnKeyGeneration {
        void onKeyGeneration(ECKey key);
    }
}
