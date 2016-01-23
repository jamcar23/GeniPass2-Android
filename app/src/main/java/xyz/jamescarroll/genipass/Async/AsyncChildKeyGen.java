package xyz.jamescarroll.genipass.Async;

import xyz.jamescarroll.genipass.Crypto.ECKey;
import xyz.jamescarroll.genipass.Crypto.KeyManager;

/**
 * Created by James Carroll on 1/23/16.
 */
public class AsyncChildKeyGen extends AsyncKeyGen {

    public AsyncChildKeyGen() {
        super();
    }

    @Override
    protected ECKey doInBackground(String... params) {
        ECKey gen2, gen3 = null;
        KeyManager km = KeyManager.getInstance();


        if (km.isMasterKeyFinished()) {
            gen2 = km.getMasterKey().generateChildKey(params[0]);

            if (gen2 != null) {
                gen3 = gen2.generateChildKey(params[1]);
            }
        }

        return gen3;
    }

    @Override
    protected void onPostExecute(ECKey key) {
        mListener.onKeyGeneration(key);
    }
}
