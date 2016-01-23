package xyz.jamescarroll.genipass.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xyz.jamescarroll.genipass.Async.AsyncChildKeyGen;
import xyz.jamescarroll.genipass.Crypto.KeyManager;
import xyz.jamescarroll.genipass.R;

;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceTagFragment extends ExtFragment {
    public static final String TAG = "ServiceTagFragment.TAG";
    private ProgressDialog mProgress;


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
        KeyManager km = KeyManager.getInstance();

        if (km.ismRequestChildKeys() && !km.isChildKeyFinished()) {
            showProgressDialog();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void callAsyncChildKeyGen() {
        new AsyncChildKeyGen().execute(getTextFromEditText(R.id.et_service),
                getTextFromEditText(R.id.et_tag));
    }

    private void createProgressDialog() {
        mProgress = new ProgressDialog(getActivity());
        mProgress.setTitle("Doing Math.");
        mProgress.setMessage("Please wait, this could take +30 seconds.");
        mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgress.setCancelable(false);
        mProgress.setCanceledOnTouchOutside(false);
    }

    public void showProgressDialog() {
        if (mProgress == null) {
            createProgressDialog();
        }

        mProgress.show();
    }

    public void dismissProgressDialog() {
        if (mProgress != null) {
            mProgress.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        KeyManager km = KeyManager.getInstance();

        switch (v.getId()) {
            case R.id.fab:
                showProgressDialog();

                if (km.isMasterKeyFinished()) {
                    callAsyncChildKeyGen();
                } else {
                    km.setmRequestChildKeys(true);
                }

                break;
        }
    }


}
