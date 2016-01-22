package xyz.jamescarroll.genipass.Fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xyz.jamescarroll.genipass.Crypto.ECKey;
import xyz.jamescarroll.genipass.IntentUtil;
import xyz.jamescarroll.genipass.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceTagFragment extends ExtFragment {
    public static final String TAG = "ServiceTagFragment.TAG";

    private MasterKeyHolder mHolder;


    public ServiceTagFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return setPadding(inflater.inflate(R.layout.fragment_service_tag, container, false));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setDrawableToFAB(R.drawable.ic_arrow_forward_24dp_white);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof MasterKeyHolder) {
            mHolder = (MasterKeyHolder) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement MasterKeyHolder.");
        }
    }

    public ECKey handleChildKeyDerivation() {
        final String s = getTextFromEditText(R.id.et_service);
        final String t = getTextFromEditText(R.id.et_tag);
        ECKey gen2, gen3 = null;
        Intent requestedChild;

        if (mHolder.isMasterKeyFinished()) {
            gen2 = mHolder.getMasterKey().generateChildKey(s);

            if (gen2 != null) {
                gen3 = gen2.generateChildKey(t);
            }
        } else {
            requestedChild = new Intent();
            requestedChild.putExtra(IntentUtil.kExtraChildBegin, true);
            mListener.onFragmentInteraction(requestedChild);
        }

        return gen3;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                handleChildKeyDerivation();
                break;
        }
    }

    public interface MasterKeyHolder {
        ECKey getMasterKey();
        boolean isMasterKeyFinished();
    }
}
