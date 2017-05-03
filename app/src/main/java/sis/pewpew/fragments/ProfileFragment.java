package sis.pewpew.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sis.pewpew.MainActivity;
import sis.pewpew.R;

import static com.google.android.gms.internal.zzt.TAG;

public class ProfileFragment extends Fragment {

    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        SharedPreferences settings = getActivity().getSharedPreferences("PROFILE", 0);
        boolean dialogShown = settings.getBoolean("dialogShown", false);

        if (!dialogShown) {
            AlertDialog.Builder newsFragmentWelcomeDialog = new AlertDialog.Builder(getActivity());
            newsFragmentWelcomeDialog.setTitle(getString(R.string.profile_fragment_name));
            newsFragmentWelcomeDialog.setCancelable(false);
            newsFragmentWelcomeDialog.setIcon(R.drawable.ic_menu_profile);
            newsFragmentWelcomeDialog.setMessage("В разделе \"Профиль\" мы собрали всю самую интересную информацию о Вас. " +
                    "А именно все Ваши очки, достижения и заслуги перед планетой. Не забудьте похвастаться ими, " +
                    "коснувшись кнопки \"Поделиться\".");
            newsFragmentWelcomeDialog.setNegativeButton("Не забуду", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            newsFragmentWelcomeDialog.show();

            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("dialogShown", true);
            editor.apply();
        }

        final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.profile_fragment_name));

        mDatabase = FirebaseDatabase.getInstance().getReference();

        ValueEventListener pointsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String statusFromDatabase = dataSnapshot.child("users").child(user.getUid()).child("status").getValue().toString();
                long pointsFromDatabase = (long) dataSnapshot.child("users").child(user.getUid()).child("points").getValue();
                TextView points = (TextView) rootView.findViewById(R.id.achieves_points);
                TextView status = (TextView) rootView.findViewById(R.id.achieves_status);
                TextView rank = (TextView) rootView.findViewById(R.id.achieves_rank);
                TextView achieves = (TextView) rootView.findViewById(R.id.achieves_summary);
                TextView savedTrees = (TextView) rootView.findViewById(R.id.achieves_saved_trees);
                TextView savedAnimals = (TextView) rootView.findViewById(R.id.achieves_saved_animals);
                TextView savedPeople = (TextView) rootView.findViewById(R.id.achieves_saved_people);
                if (user != null && pointsFromDatabase != 0) {
                    points.setText("Очков: " + pointsFromDatabase);
                } else {
                    points.setText("Количество очков");
                }

                savedTrees.setText("Деревьев: " + (int) pointsFromDatabase / 500);
                savedAnimals.setText("Животных: " + (int) pointsFromDatabase / 1000);
                savedPeople.setText("Людей: " + (int) pointsFromDatabase / 1500);

                if (pointsFromDatabase < 100) {
                    achieves.setText("Достижений: 0");
                } else if (pointsFromDatabase >= 100 && pointsFromDatabase < 300) {
                    achieves.setText("Достижений: 1");
                } else if (pointsFromDatabase >= 300 && pointsFromDatabase < 500) {
                    achieves.setText("Достижений: 2");
                } else if (pointsFromDatabase >= 500 && pointsFromDatabase < 700) {
                    achieves.setText("Достижений: 3");
                } else if (pointsFromDatabase >= 700 && pointsFromDatabase < 900) {
                    achieves.setText("Достижений: 4");
                } else if (pointsFromDatabase >= 900 && pointsFromDatabase < 1100) {
                    achieves.setText("Достижений: 5");
                } else if (pointsFromDatabase >= 1100 && pointsFromDatabase < 1300) {
                    achieves.setText("Достижений: 6");
                } else if (pointsFromDatabase >= 1300 && pointsFromDatabase < 1500) {
                    achieves.setText("Достижений: 7");
                } else if (pointsFromDatabase >= 1500 && pointsFromDatabase < 1700) {
                    achieves.setText("Достижений: 8");
                } else if (pointsFromDatabase >= 1700 && pointsFromDatabase < 1900) {
                    achieves.setText("Достижений: 9");
                } else if (pointsFromDatabase >= 1900 && pointsFromDatabase < 2100) {
                    achieves.setText("Достижений: 10");
                } else if (pointsFromDatabase >= 2100 && pointsFromDatabase < 2300) {
                    achieves.setText("Достижений: 11");
                } else if (pointsFromDatabase >= 2300 && pointsFromDatabase < 2500) {
                    achieves.setText("Достижений: 12");
                } else if (pointsFromDatabase >= 2500 && pointsFromDatabase < 2700) {
                    achieves.setText("Достижений: 13");
                } else if (pointsFromDatabase >= 2700 && pointsFromDatabase < 2900) {
                    achieves.setText("Достижений: 14");
                } else {
                    achieves.setText("Достижений: 15");
                }

                if ((int) pointsFromDatabase < 200) {
                    rank.setText("Ранг: Новичок");
                } else if (pointsFromDatabase >= 200 && pointsFromDatabase < 500) {
                    rank.setText("Ранг: Начинающий");
                } else if (pointsFromDatabase >= 500 && pointsFromDatabase < 1000) {
                    rank.setText("Ранг: Опытный");
                } else if (pointsFromDatabase >= 1000 && pointsFromDatabase < 2000) {
                    rank.setText("Ранг: Защитник флоры");
                } else if (pointsFromDatabase >= 2000 && pointsFromDatabase < 3500) {
                    rank.setText("Ранг: Защитник фауны");
                } else if (pointsFromDatabase >= 3500 && pointsFromDatabase < 5000) {
                    rank.setText("Ранг: Защитник людей");
                } else if (pointsFromDatabase >= 5000 && pointsFromDatabase < 8500) {
                    rank.setText("Ранг: Защитник Земли");
                } else if (pointsFromDatabase >= 8500 && pointsFromDatabase < 10000) {
                    rank.setText("Герой");
                } else {
                    rank.setText("Легенда");
                }

                if (statusFromDatabase != null) {
                    switch (statusFromDatabase) {
                        case "1":
                            status.setText("Сотрудник");
                            break;
                        case "2":
                            status.setText("Организатор");
                            break;
                        case "3":
                            status.setText("Модератор");
                            break;
                        case "4":
                            status.setText("Администратор");
                            break;
                        case "5":
                            status.setText("Создатель");
                            break;
                    }
                } else {
                    status.setText("Без пометки");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.addValueEventListener(pointsListener);

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