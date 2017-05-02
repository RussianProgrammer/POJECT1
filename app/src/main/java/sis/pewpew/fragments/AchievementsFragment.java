package sis.pewpew.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import sis.pewpew.MainActivity;
import sis.pewpew.R;

public class AchievementsFragment extends Fragment {

    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SharedPreferences settings = getActivity().getSharedPreferences("ACHIEVEMENTS", 0);
        boolean dialogShown = settings.getBoolean("dialogShown", false);

        if (!dialogShown) {
            AlertDialog.Builder achievementsFragmentWelcomeDialog = new AlertDialog.Builder(getActivity());
            achievementsFragmentWelcomeDialog.setTitle(getString(R.string.achievements_fragment_name));
            achievementsFragmentWelcomeDialog.setCancelable(false);
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

        View rootView = inflater.inflate(R.layout.fragment_achievements, container, false);
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.achievements_fragment_name));

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.achieves_list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 7));

        if (user != null && user.getDisplayName() != null) {
            TextView username = (TextView) rootView.findViewById(R.id.achieves_username);
            username.setText(user.getDisplayName());
        } else {
            TextView username = (TextView) rootView.findViewById(R.id.achieves_username);
            username.setText("Имя пользователя");
        }
        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
