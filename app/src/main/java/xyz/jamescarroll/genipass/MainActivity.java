/*
 * Copyright (c) 2016
 *
 * This file, MainActivity.java, is apart of GeniPass.
 *
 * GeniPass is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * GeniPass is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with GeniPass.  If not, see http://www.gnu.org/licenses/.
 */

package xyz.jamescarroll.genipass;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import xyz.jamescarroll.genipass.Crypto.KeyManager;
import xyz.jamescarroll.genipass.Fragment.ExtFragment;
import xyz.jamescarroll.genipass.Fragment.LoginFragment;
import xyz.jamescarroll.genipass.Fragment.ServiceTagFragment;
import xyz.jamescarroll.genipass.Fragment.StrengthTestFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ExtFragment.OnFragmentInteractionListener, KeyManager.ControlUI {
    public static final String kExtraFragmentTag = "xyz.jamescarroll.genipass.MainActivity.EXTRA_FRAGMENT_TAG";
    private boolean mVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        KeyManager.getInstance().setmControl(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        handleMemoryRequirements();
        handleManagerFirstFragment();
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
        switch (item.getItemId()) {
            case R.id.action_logout:
                KeyManager.getInstance().clear();
                finish();
                return true;
            case R.id.action_visibility_toggle:
                mVisible = !mVisible;
                toggleVisibilityIcon();

                if (findViewById(R.id.et_password) != null) {
                    toggleEditTextVisibility(R.id.et_password);
                } else if (findViewById(R.id.et_service) != null &&
                        findViewById(R.id.et_tag) != null) {
                    toggleEditTextVisibility(R.id.et_service);
                    toggleEditTextVisibility(R.id.et_tag);
                }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_mng:
                handleManagerFirstFragment();
            break;
            case R.id.nav_tester:
                replaceFragment(findStrengthTestFragment(), StrengthTestFragment.TAG);
                break;
            case R.id.nav_settings:
                startActivity(new Intent(this, SettingsActivity.class));
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

        if (frag.hasExtra(kExtraFragmentTag)) {
            switch (frag.getStringExtra(kExtraFragmentTag)) {
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
    }

    @Override
    public void callAsyncChildKeyGen() {
        findServiceTagFragment().callAsyncChildKeyGen();
    }

    @Override
    public void dismissProgressDialog() {
        findServiceTagFragment().dismissProgressDialog();
    }

    @Override
    public void toPasswordActivity() {
        Intent toPasswordActivity = new Intent(this, PasswordActivity.class);
        startActivity(toPasswordActivity);
    }

    @Override
    public void handleLogOut() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_frag, findLoginFragment(),
                LoginFragment.TAG).commitAllowingStateLoss();


        try {
            Snackbar.make(getCurrentFocus(), "User has been logged out.", Snackbar.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void toggleVisibilityIcon() {
        ActionMenuItemView v = (ActionMenuItemView) findViewById(R.id.action_visibility_toggle);

        if (v != null) {
            v.setIcon(mVisible ? getDrawable(R.drawable.ic_visibility_off_24dp_white) :
                    getDrawable(R.drawable.ic_visibility_24dp_white));
        }
    }

    private void toggleEditTextVisibility(int view) {
        if (mVisible) {
            ((EditText) findViewById(view)).setInputType(
                    InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            ((EditText) findViewById(view)).setInputType(
                    InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    private void handleMemoryRequirements() {
        long mem = Runtime.getRuntime().maxMemory();

        if (mem < 191889408) {
            new AlertDialog.Builder(this)
                    .setTitle("Error: Not Enough Memory!")
                    .setMessage("Your device doesn't have enough memory to run GeniPass. " +
                            "Try closing a few apps and relaunching GeniPass.")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
    }

    private void handleManagerFirstFragment() {
        if (KeyManager.getInstance().ismMasterBegin()) {
            replaceFragment(findServiceTagFragment(), ServiceTagFragment.TAG);
        } else {
            replaceFragment(findLoginFragment(), LoginFragment.TAG);
        }
    }

    private void replaceFragment(Fragment frag, String tag) {
        mVisible = false;
        toggleVisibilityIcon();
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
