package xyz.jamescarroll.genipass.Async;

import android.content.Context;

import xyz.jamescarroll.genipass.Crypto.ECKey;

/**
 * Created by James Carroll on 1/23/16.
 */
public class AsyncChildKeyGen extends AsyncKeyGen {

    private MasterKeyHolder mHolder;

    public AsyncChildKeyGen(Context context) {
        super();
        setmListner(context);

        if (context instanceof MasterKeyHolder) {
            mHolder = (MasterKeyHolder) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MasterKeyHolder");
        }
    }

    @Override
    protected ECKey doInBackground(String... params) {
        ECKey gen2, gen3 = null;


        if (mHolder.isMasterKeyFinished()) {
            gen2 = mHolder.getMasterKey().generateChildKey(params[0]);

            if (gen2 != null) {
                gen3 = gen2.generateChildKey(params[1]);
            }
        }

        return gen3;
    }

    @Override
    protected void onPostExecute(ECKey key) {
        mListner.onKeyGeneration(key);
    }

    public interface MasterKeyHolder {
        ECKey getMasterKey();
        boolean isMasterKeyFinished();
    }
}
