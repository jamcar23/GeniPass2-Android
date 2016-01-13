package xyz.jamescarroll.genipass.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        View v = inflater.inflate(R.layout.fragment_login, container, false);
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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private int getDimension(int id) {
        return (int) getResources().getDimension(id);
    }
}
