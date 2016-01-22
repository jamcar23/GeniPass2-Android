package xyz.jamescarroll.genipass.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xyz.jamescarroll.genipass.Async.AsyncMasterKeyGen;
import xyz.jamescarroll.genipass.IntentUtil;
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
        setDrawableToFAB(R.drawable.ic_add_white_24dp);
    }

    private void handleLoginBtnClick() {
        new AsyncMasterKeyGen(getActivity()).execute(getTextFromEditText(R.id.et_username),
                getTextFromEditText(R.id.et_password));
        Intent toService = new Intent();
        toService.putExtra(IntentUtil.kExtraFragmentTag, ServiceTagFragment.TAG);
        toService.putExtra(IntentUtil.kExtraMasterBegin, true);

        mListener.onFragmentInteraction(toService);

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
