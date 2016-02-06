/*
 * Copyright (c) 2016
 *
 * This file, LoginFragment.java, is apart of GeniPass.
 *
 * GeniPass is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * GeniPass is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with GeniPass.  If not, see http://www.gnu.org/licenses/.
 */

package xyz.jamescarroll.genipass.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xyz.jamescarroll.genipass.Async.AsyncMasterKeyGen;
import xyz.jamescarroll.genipass.Crypto.KeyManager;
import xyz.jamescarroll.genipass.Crypto.Password;
import xyz.jamescarroll.genipass.MainActivity;
import xyz.jamescarroll.genipass.PasswordActivity;
import xyz.jamescarroll.genipass.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LoginFragment extends ExtFragment {
    public static final String TAG = "LoginFragment.TAG";
    private AlertDialog mAlert;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return setPadding(inflater.inflate(R.layout.fragment_login, container, false));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findView(R.id.btn_login).setOnClickListener(this);
        setDrawableToFAB(R.drawable.ic_add_white_24dp);
    }

    private void handleLoginBtnClick() {
        KeyManager km = KeyManager.getInstance();
        String p = getTextFromEditText(R.id.et_password);
        String t;
        Intent toService;

        if (Password.calcEntropy(p) >= 100) {
            new AsyncMasterKeyGen().execute(getTextFromEditText(R.id.et_username), p);
            km.setmMasterBegin(true);

            toService = new Intent();
            toService.putExtra(MainActivity.kExtraFragmentTag, ServiceTagFragment.TAG);

            mListener.onFragmentInteraction(toService);

            t = PreferenceManager.getDefaultSharedPreferences(getActivity()).
                    getString(getString(R.string.lp_timeout), "");

            if (!t.equals("0")) {
                KeyManager.getInstance().beginTimeOut(t);
            }
        } else {
            if (mAlert == null) {
                mAlert = new AlertDialog.Builder(getActivity())
                        .setTitle("Insecure Password!")
                        .setMessage("The only is requirement is your password must contain greater " +
                                "than 100 bits of entropy (a measurement of randomness.) " +
                                "\n\nIf you can't come up with a password tap the + button in the lower " +
                                "right corner to have GeniPass create one for you.")
                        .setPositiveButton("Okay", null)
                        .create();
            }

            mAlert.show();
        }
    }

    private void handleFabClick() {
        Intent toPassword = new Intent(getActivity(), PasswordActivity.class);
        startActivity(toPassword);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                handleLoginBtnClick();
                break;
            case R.id.fab:
                handleFabClick();
                break;
        }
    }
}
