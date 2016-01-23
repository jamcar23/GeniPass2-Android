package xyz.jamescarroll.genipass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import xyz.jamescarroll.genipass.Fragment.ExtFragment;
import xyz.jamescarroll.genipass.Fragment.PasswordFragment;

public class PasswordActivity extends AppCompatActivity implements ExtFragment.OnFragmentInteractionListener{
    private static final String TAG = "PasswordActivity.TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportFragmentManager().beginTransaction().replace(R.id.fl_frag,
                findPasswordFragment(), PasswordFragment.TAG).commit();
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            Log.e(TAG, "onCreate: ", e);
        }

    }

    public PasswordFragment findPasswordFragment() {
        PasswordFragment pf = (PasswordFragment) getSupportFragmentManager().findFragmentByTag(
                PasswordFragment.TAG);

        if (pf == null) {
            pf = new PasswordFragment();
        }

        return pf;
    }

    @Override
    public void onFragmentInteraction(Intent frag) {

    }
}
