package xyz.jamescarroll.genipass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import xyz.jamescarroll.genipass.Fragment.ExtFragment;
import xyz.jamescarroll.genipass.Fragment.PasswordFragment;

public class PasswordActivity extends AppCompatActivity implements ExtFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportFragmentManager().beginTransaction().replace(R.id.fl_frag,
                findPasswordFragment(null), PasswordFragment.TAG).commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public PasswordFragment findPasswordFragment(byte[] bytes) {
        PasswordFragment pf = (PasswordFragment) getSupportFragmentManager().findFragmentByTag(
                PasswordFragment.TAG);

        if (pf == null) {
            pf = new PasswordFragment();
        }

        pf.setmKey(bytes);

        return pf;
    }

    @Override
    public void onFragmentInteraction(Intent frag) {

    }
}
