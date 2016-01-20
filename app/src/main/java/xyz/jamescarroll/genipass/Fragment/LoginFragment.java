package xyz.jamescarroll.genipass.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xyz.jamescarroll.genipass.Async.AsyncMasterKeyGen;
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
        ((FloatingActionButton) findView(R.id.fab)).setImageDrawable(getResources().getDrawable(
                R.drawable.ic_add_white_24dp, getActivity().getTheme()));
    }

    private void handleLoginBtnClick() {
        new AsyncMasterKeyGen(getActivity()).execute(getTextFromEditText(R.id.et_username),
                getTextFromEditText(R.id.et_password));

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
