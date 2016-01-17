package xyz.jamescarroll.genipass.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import xyz.jamescarroll.genipass.Password;
import xyz.jamescarroll.genipass.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PasswordFragment extends ExtFragment {
    public static final String TAG = "PasswordFragment.TAG";

    private byte[] mKey;


    public PasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return setPadding(inflater.inflate(R.layout.fragment_password, container, false));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        handlePassword();
    }

    private void setTexttoTextView(int view, String text) {
        ((TextView) findView(view)).setText(text);
    }

    private void handlePassword() {
        String p;

        if (getmKey() != null && getmKey().length > 0) {
            p = Password.pickPassword(getmKey(), getActivity());
        } else {
            p = Password.pickRandomPassword(getActivity());
        }

        setTexttoTextView(R.id.tv_password, p);
        setTexttoTextView(R.id.tv_entropy, "Entropy: " + Password.calcEntropy(p) + " bits");
    }

    public byte[] getmKey() {
        return mKey;
    }

    public void setmKey(byte[] mKey) {
        this.mKey = mKey;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                handlePassword();
                break;
        }
    }
}
