package sis.pewpew.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import sis.pewpew.MainActivity;
import sis.pewpew.R;
import sis.pewpew.utils.NetworkStatusInspectorActivity;


public class SettingsFragment extends PreferenceFragment {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference mDatabase;
    private AlertDialog.Builder signOutDialog;
    private AlertDialog.Builder deleteAccountDialog;
    private NetworkStatusInspectorActivity inspector = new NetworkStatusInspectorActivity();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.settings_fragment_name));
        addPreferencesFromResource(R.xml.preferences);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        deleteAccountDialog = new AlertDialog.Builder(getActivity());
        deleteAccountDialog.setTitle("Удаление аккаунта");
        deleteAccountDialog.setMessage("Вы уверены, что хотите удалить текущий аккаунт?" +
                " Все данные, связанные с ним, будут безвозвратно удалены, включая очки и достижения.");
        deleteAccountDialog.setPositiveButton("Продолжить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (inspector.isConnected) {
                    mDatabase.child("users").child(user.getUid()).removeValue();
                    FirebaseAuth.getInstance().signOut();
                    logOut();
                } else {
                    Toast.makeText(getActivity(), "Пожалуйста, проверьте подключение к Интернету", Toast.LENGTH_SHORT).show();
                }
            }
        });
        deleteAccountDialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });


        signOutDialog = new AlertDialog.Builder(getActivity());
        signOutDialog.setTitle("Выход");
        signOutDialog.setMessage("Вы уверены, что хотите выйти из текущего аккаунта?");
        signOutDialog.setPositiveButton("Выйти", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (inspector.isConnected) {
                    FirebaseAuth.getInstance().signOut();
                    logOut();
                } else {
                    Toast.makeText(getActivity(), "Пожалуйста, проверьте подключение к Интернету", Toast.LENGTH_SHORT).show();
                }
            }
        });
        signOutDialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        final Preference myPref1 = findPreference("delete_account_button");
        myPref1.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                deleteAccountDialog.show();
                return false;
            }
        });

        final Preference myPref = findPreference("sign_out_button");
        myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                signOutDialog.show();
                return false;
            }
        });
        return rootView;
    }

    private void logOut() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}