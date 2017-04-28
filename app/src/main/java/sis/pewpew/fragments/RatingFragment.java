package sis.pewpew.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import sis.pewpew.MainActivity;
import sis.pewpew.R;

public class RatingFragment extends Fragment {

    private ArrayList<String> list = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    //private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        SharedPreferences settings = getActivity().getSharedPreferences("RATING", 0);
        boolean dialogShown = settings.getBoolean("dialogShown", false);

        if (!dialogShown) {
            AlertDialog.Builder ratingFragmentWelcomeDialog = new AlertDialog.Builder(getActivity());
            ratingFragmentWelcomeDialog.setTitle(getString(R.string.rating_fragment_name));
            ratingFragmentWelcomeDialog.setIcon(R.drawable.ic_menu_rating);
            ratingFragmentWelcomeDialog.setMessage("В разделе \"Рейтинг\" показана таблица лидеров по очкам, заработанным в приложении," +
                    " среди членов сообщества. Вы также можете найти свою позицию в рейтинге, используя фильтр.");
            ratingFragmentWelcomeDialog.setNegativeButton("Понятно", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            ratingFragmentWelcomeDialog.show();

            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("dialogShown", true);
            editor.apply();
        }
        View rootView = inflater.inflate(R.layout.fragment_rating, container, false);
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.rating_fragment_name));

        /*ListView listView = (ListView) rootView.findViewById(R.id.listview);
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, list);
        listView.setAdapter(adapter);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.child("rating").getValue(String.class);
                list.add(value);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


        });*/
        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
