package xyz.jamescarroll.genipass.Async;

import xyz.jamescarroll.genipass.Crypto.ECKey;

/**
 * Created by James Carroll on 1/20/16.
 */
public class AsyncMasterKeyGen extends AsyncKeyGen {

    public AsyncMasterKeyGen() {
        super();
    }

    @Override
    protected ECKey doInBackground(String... params) {
        return ECKey.genFromSeeds(params[0], params[1]);
    }

    @Override
    protected void onPostExecute(ECKey key) {
        mListener.onKeyGeneration(key);
    }
}
