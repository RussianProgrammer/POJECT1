package sis.pewpew.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sis.pewpew.MainActivity;
import sis.pewpew.R;

public class AboutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.about_fragment_name));
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
