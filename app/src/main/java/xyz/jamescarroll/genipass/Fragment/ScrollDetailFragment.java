/*
 * Copyright (c) 2016
 *
 * This file, ScrollDetailFragment.java, is apart of GeniPass.
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

import xyz.jamescarroll.genipass.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScrollDetailFragment extends Fragment {
    public static final String TAG = "ScrollDetailFragment.TAG";
    private static final String kExtraDetail = TAG + ".DETAIL";

    public ScrollDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scroll_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            findTextView().setText(getArguments().getString(kExtraDetail, ""));
        }
    }

    private TextView findTextView() {
        return (TextView) getActivity().findViewById(R.id.tv_settings_detail);
    }

    public static ScrollDetailFragment newInstance(String detail) {

        Bundle args = new Bundle();
        args.putString(kExtraDetail, detail);

        ScrollDetailFragment fragment = new ScrollDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
