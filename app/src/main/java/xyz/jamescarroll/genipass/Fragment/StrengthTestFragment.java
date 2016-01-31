/*
 * Copyright (c) 2016
 *
 * This file, StrengthTestFragment.java, is apart of GeniPass.
 *
 * GeniPass is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * GeniPass is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with GeniPass.  If not, see http://www.gnu.org/licenses/.
 */

package xyz.jamescarroll.genipass.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;

import xyz.jamescarroll.genipass.Crypto.Password;
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

        setDrawableToFAB(R.drawable.ic_arrow_forward_24dp_white);
    }

    private void setTextToTextView(int view, String text) {
        ((TextView) findView(view)).setText(text);
    }

    @Override
    public void onClick(View v) {
        int e = Password.calcEntropy(getTextFromEditText(R.id.et_password));
        String t = new PrettyTime().format(new Date((long) ((Password.calcTimeToCrack(e) * 1000) +
                System.currentTimeMillis())));

        if (e >= 70) {
            t = "more than " + t;
        }

        setTextToTextView(R.id.tv_entropy, "Entropy: " + e + " bits");
        setTextToTextView(R.id.tv_time, "Time needed to brute force password: " + t);
    }
}
