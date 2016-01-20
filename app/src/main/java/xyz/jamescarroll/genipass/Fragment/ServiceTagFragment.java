package xyz.jamescarroll.genipass.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xyz.jamescarroll.genipass.Crypto.ECKey;
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
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof MasterKeyHolder) {
            mHolder = (MasterKeyHolder) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement MasterKeyHolder.");
        }
    }

    @Override
    public void onClick(View v) {

    }

    public interface MasterKeyHolder {
        ECKey getMasterKey();
    }
}
