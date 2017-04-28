package sis.pewpew.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sis.pewpew.MainActivity;
import sis.pewpew.R;

public class AchievementsFragment extends Fragment {

    private AlertDialog.Builder achievementsFragmentWelcomeDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SharedPreferences settings = getActivity().getSharedPreferences("ACHIEVEMENTS", 0);
        boolean dialogShown = settings.getBoolean("dialogShown", false);

        if (!dialogShown) {
            achievementsFragmentWelcomeDialog = new AlertDialog.Builder(getActivity());
            achievementsFragmentWelcomeDialog.setTitle(getString(R.string.achievements_fragment_name));
            achievementsFragmentWelcomeDialog.setIcon(R.drawable.ic_menu_achievements);
            achievementsFragmentWelcomeDialog.setMessage("В разделе \"Достижения\" Вы сможете наблюдать за своим личным прогрессом и текущем рангом, " +
                    "а также исследовать полученные награды.");
            achievementsFragmentWelcomeDialog.setNegativeButton("Понятно", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            achievementsFragmentWelcomeDialog.show();

            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("dialogShown", true);
            editor.apply();
        }

        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.achievements_fragment_name));
        return inflater.inflate(R.layout.fragment_achievements, container, false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
