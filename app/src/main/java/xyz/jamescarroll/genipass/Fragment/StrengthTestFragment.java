package xyz.jamescarroll.genipass.Fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;

import xyz.jamescarroll.genipass.Password;
import xyz.jamescarroll.genipass.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StrengthTestFragment extends ExtFragment {
    public static final String TAG = "StrengthTestFragment.TAG";

    public StrengthTestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return setPadding(inflater.inflate(R.layout.fragment_strength_test, container, false));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((FloatingActionButton) findView(R.id.fab)).setImageDrawable(getResources().getDrawable(
                R.drawable.ic_arrow_forward_24dp_white, getActivity().getTheme()));
    }

    private void setTextToTextView(int view, String text) {
        ((TextView) findView(view)).setText(text);
    }

    @Override
    public void onClick(View v) {
        int e = Password.calcEntropy(getTextFromEditText(R.id.et_password));
        String t = new PrettyTime().format(new Date((long) ((Password.calcTimeToCrack(e) * 1000) +
                System.currentTimeMillis())));
        setTextToTextView(R.id.tv_entropy, "Entropy: " + e + " bits");
        setTextToTextView(R.id.tv_time, "Time needed to brute force password: " + t);
    }
}
