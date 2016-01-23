package xyz.jamescarroll.genipass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import xyz.jamescarroll.genipass.Async.AsyncChildKeyGen;
import xyz.jamescarroll.genipass.Async.AsyncMasterKeyGen;
import xyz.jamescarroll.genipass.Crypto.ECKey;
import xyz.jamescarroll.genipass.Fragment.ExtFragment;
import xyz.jamescarroll.genipass.Fragment.LoginFragment;
import xyz.jamescarroll.genipass.Fragment.ServiceTagFragment;
import xyz.jamescarroll.genipass.Fragment.StrengthTestFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ExtFragment.OnFragmentInteractionListener, AsyncMasterKeyGen.OnKeyGeneration,
        AsyncChildKeyGen.MasterKeyHolder {

    private ECKey mMaster;
    private ProgressDialog mProgress;
    private boolean mMasterBegin = false;
    private boolean mMasterFinished = false;
    private boolean mRequestChildKeys = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        getSupportFragmentManager().beginTransaction().replace(R.id.fl_frag, findLoginFragment(),
                LoginFragment.TAG).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mMasterFinished = mMaster != null;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity_main in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_mng:
                if (mMasterBegin || mMasterFinished) {
                    replaceFragment(findServiceTagFragment(), ServiceTagFragment.TAG);
                } else {
                    replaceFragment(findLoginFragment(), LoginFragment.TAG);
                }
            break;
            case R.id.nav_tester:
                replaceFragment(findStrengthTestFragment(), StrengthTestFragment.TAG);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Intent frag) {
        ExtFragment f = null;
        String t = "";

        if (frag.hasExtra(IntentUtil.kExtraFragmentTag)) {
            switch (frag.getStringExtra(IntentUtil.kExtraFragmentTag)) {
                case LoginFragment.TAG:
                    f = findLoginFragment();
                    t = LoginFragment.TAG;
                    break;
                case StrengthTestFragment.TAG:
                    f = findStrengthTestFragment();
                    t = StrengthTestFragment.TAG;
                    break;
                case ServiceTagFragment.TAG:
                    f = findServiceTagFragment();
                    t = ServiceTagFragment.TAG;
                    break;
            }

            if (f != null && !t.isEmpty()) {
                replaceFragment(f, t);
            }
        }

        if (frag.hasExtra(IntentUtil.kExtraMasterBegin)) {
            mMasterBegin = frag.getBooleanExtra(IntentUtil.kExtraMasterBegin, false);
        } else if (frag.hasExtra(IntentUtil.kExtraChildBegin)) {
            mRequestChildKeys = frag.getBooleanExtra(IntentUtil.kExtraChildBegin, false);
            mProgress = new ProgressDialog(this);
            mProgress.setTitle("Please Wait.");
            mProgress.setMessage("Doing math.");
            mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgress.setCancelable(false);
            mProgress.setCanceledOnTouchOutside(false);
            mProgress.show();
        }

    }

    @Override
    public void onKeyGeneration(ECKey key) {

        if (key.ismMaster()) {
            this.mMaster = key;
            this.mMasterFinished = true;

            if (mRequestChildKeys) {
                findServiceTagFragment().callAsyncChildKeyGen();
            }
        } else {
            mProgress.dismiss();

        }
    }

    @Override
    public ECKey getMasterKey() {
        return mMaster;
    }

    @Override
    public boolean isMasterKeyFinished() {
        return mMasterFinished;
    }

    private void replaceFragment(Fragment frag, String tag) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_frag, frag, tag).commit();
    }

    private LoginFragment findLoginFragment() {
        LoginFragment lf = (LoginFragment) getSupportFragmentManager().findFragmentByTag(
                LoginFragment.TAG);

        if (lf == null) {
            lf = new LoginFragment();
        }

        return lf;
    }

    private StrengthTestFragment findStrengthTestFragment() {
        StrengthTestFragment stf = (StrengthTestFragment) getSupportFragmentManager().
                findFragmentByTag(StrengthTestFragment.TAG);

        if (stf == null) {
            stf = new StrengthTestFragment();
        }

        return stf;
    }

    private ServiceTagFragment findServiceTagFragment() {
        ServiceTagFragment stf = (ServiceTagFragment) getSupportFragmentManager().
                findFragmentByTag(ServiceTagFragment.TAG);

        if (stf == null) {
            stf = new ServiceTagFragment();
        }

        return stf;
    }
}
