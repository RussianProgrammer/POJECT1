package sis.pewpew.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import sis.pewpew.MainActivity;
import sis.pewpew.R;

public class EventsFragment extends Fragment {

    private AlertDialog.Builder eventsFragmentWelcomeDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SharedPreferences settings = getActivity().getSharedPreferences("EVENTS", 0);
        boolean dialogShown = settings.getBoolean("dialogShown", false);

        if (!dialogShown) {
            eventsFragmentWelcomeDialog = new AlertDialog.Builder(getActivity());
            eventsFragmentWelcomeDialog.setTitle(getString(R.string.events_fragment_name));
            eventsFragmentWelcomeDialog.setMessage("В разделе \"События\" Вы сможете наблюдать за появлением новых экологических фестивалей и других подобных мероприятний, проходящих в Вашем городе. " +
                            "Эти мероприятия будут также отмечены специальным флажком на карте, а за явку на такое событие Вы получите увеличенную награду."
                    );
            eventsFragmentWelcomeDialog.setNegativeButton("Понятно", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            eventsFragmentWelcomeDialog.show();

            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("dialogShown", true);
            editor.apply();
        }

        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.events_fragment_name));
        return inflater.inflate(R.layout.fragment_feedback, container, false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
