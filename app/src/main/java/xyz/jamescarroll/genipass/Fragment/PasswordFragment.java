/*
 * Copyright (c) 2016
 *
 * This file, PasswordFragment.java, is apart of GeniPass.
 *
 * GeniPass is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * GeniPass is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with GeniPass.  If not, see http://www.gnu.org/licenses/.
 */

package xyz.jamescarroll.genipass.Fragment;


import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Arrays;

import xyz.jamescarroll.genipass.Crypto.KeyManager;
import xyz.jamescarroll.genipass.Crypto.Password;
import xyz.jamescarroll.genipass.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PasswordFragment extends ExtFragment implements CompoundButton.OnCheckedChangeListener {
    public static final String TAG = "PasswordFragment.TAG";
    private char[] mChars;
    private boolean mSpaces;


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

        Switch s = ((Switch) findView(R.id.s_toggle_space));
        mSpaces = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(
                getString(R.string.sp_toggle_spaces), true);

        handlePassword(mSpaces);

        s.setChecked(mSpaces);
        s.setOnCheckedChangeListener(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mChars = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void setTexttoTextView(int view, String text) {
        ((TextView) findView(view)).setText(text);
    }

    private void handlePassword(boolean hasSpaces) {
        String p;
        KeyManager km = KeyManager.getInstance();

        if (km.isChildKeyFinished()) {
            findView(R.id.fab).setVisibility(View.INVISIBLE);
            p = Password.pickPassword(Arrays.copyOfRange(km.getChildKey().getmKey(), 1, 33),
                    getActivity(), true);
            km.clearChildKey();
        } else {
            findView(R.id.fab).setVisibility(View.VISIBLE);
            p = Password.pickRandomPassword(getActivity(), true);
        }

        mChars = p.toCharArray();
        handleSetPassword(Password.toggleSpaces(mChars, mSpaces));
    }

    private void handleSetPassword(String p) {
        setTexttoTextView(R.id.tv_password, p);
        setTexttoTextView(R.id.tv_entropy, "Entropy: " + Password.calcEntropy(p) + " bits");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                handlePassword(mSpaces);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mSpaces = isChecked;
        handleSetPassword(Password.toggleSpaces(mChars, isChecked));
    }
}
