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

public class ProgressFragment extends Fragment {

    private AlertDialog.Builder progressFragmentWelcomeDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SharedPreferences settings = getActivity().getSharedPreferences("PROGRESS", 0);
        boolean dialogShown = settings.getBoolean("dialogShown", false);

        if (!dialogShown) {
            progressFragmentWelcomeDialog = new AlertDialog.Builder(getActivity());
            progressFragmentWelcomeDialog.setTitle(getString(R.string.progress_fragment_name));
            progressFragmentWelcomeDialog.setIcon(R.drawable.ic_menu_progress);
            progressFragmentWelcomeDialog.setMessage("В разделе \"Прогресс\" показаны результаты совместной работы всего сообщества. " +
                    "Также здесь показан и Ваш личный вклад в спасение планеты.");
            progressFragmentWelcomeDialog.setNegativeButton("Понятно", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            progressFragmentWelcomeDialog.show();

            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("dialogShown", true);
            editor.apply();
        }
        // Inflate the layout for this fragment
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.progress_fragment_name));
        return inflater.inflate(R.layout.fragment_progress, container, false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
