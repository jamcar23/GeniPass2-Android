/*
 * Copyright (c) 2016
 *
 * This file, SettingsDetailFragment.java, is apart of GeniPass.
 *
 * GeniPass is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * GeniPass is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with GeniPass.  If not, see http://www.gnu.org/licenses/.
 */

package xyz.jamescarroll.genipass.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xyz.jamescarroll.genipass.R;

/**
 * Created by jamescarroll on 1/28/16.
 */
public class SettingsDetailFragment extends Fragment {
    public static final String TAG = "SettingsDetailFragment.TAG";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return setPadding(inflater.inflate(R.layout.fragment_settings_detail, container, false));
    }

    private int getDimension(int id) {
        return (int) getResources().getDimension(id);
    }

    private View setPadding(View v) {
        TypedValue tv = new TypedValue();

        if (getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            v.setPadding(getDimension(R.dimen.activity_vertical_margin),
                    TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics())
                            + getDimension(R.dimen.activity_horizontal_margin),
                    getDimension(R.dimen.activity_vertical_margin),
                    getDimension(R.dimen.activity_horizontal_margin));
        }

        return v;
    }
}
