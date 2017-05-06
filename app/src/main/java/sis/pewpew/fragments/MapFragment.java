package sis.pewpew.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import sis.pewpew.MainActivity;
import sis.pewpew.R;
import sis.pewpew.utils.DelayService;

import static android.content.Context.LOCATION_SERVICE;
import static com.google.android.gms.internal.zzt.TAG;

public class MapFragment extends Fragment {

    private MapView mMapView;
    private LatLng mDefaultLocation = new LatLng(55.755826, 37.6173);
    private MainActivity mainActivity = new MainActivity();
    private GoogleMap mMap;
    private boolean dialogShown = false;
    private boolean dialogCanceled;
    private boolean dialogAccepted;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final LatLng[] points = new LatLng[3];
    final private Marker[] markers = new Marker[3];

    LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            if (ContextCompat.checkSelfPermission(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mainActivity.mLocationPermissionGranted = true;
            } else {
                int PERMISSION_REQUEST_CODE = 5;
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQUEST_CODE);
            }

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

            for (int i = 0; i < markers.length; i++) {
                Location loc = new Location(location);
                loc.setLatitude(markers[i].getPosition().latitude);
                loc.setLongitude(markers[i].getPosition().longitude);

                if (location.distanceTo(loc) < 100) {

                    final AlertDialog.Builder profileAchievesCardDialog = new AlertDialog.Builder(getActivity());
                    profileAchievesCardDialog.setTitle("Карточка достижений");
                    profileAchievesCardDialog.setIcon(R.drawable.profile_achieves_icon);
                    profileAchievesCardDialog.setCancelable(false);
                    profileAchievesCardDialog.setMessage("");
                    profileAchievesCardDialog.setNegativeButton("Закрыть", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            dialogShown = false;
                            dialogCanceled = true;
                        }
                    });
                    profileAchievesCardDialog.setPositiveButton("Поделиться", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DatabaseReference mProfilePoints = FirebaseDatabase.getInstance().getReference()
                                    .child("users").child(user.getUid()).child("points");
                            DatabaseReference mPublicPoints = FirebaseDatabase.getInstance().getReference()
                                    .child("progress").child("points");
                            onProfilePointsAdded(mProfilePoints);
                            onPublicPointsAdded(mPublicPoints);
                            dialogShown = false;
                            dialogAccepted = true;
                        }
                    });

                    if (!dialogShown) {
                        Thread delay = new Thread();
                        delay.start();
                        if (dialogCanceled) {
                            Toast.makeText(getActivity(), "5000", Toast.LENGTH_SHORT).show();
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else if (dialogAccepted) {
                            Toast.makeText(getActivity(), "10000", Toast.LENGTH_SHORT).show();
                            try {
                                Thread.sleep(10000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            markers[i].remove();
                            Intent intent = new Intent(getActivity(), DelayService.class);
                            getActivity().startService(intent);
                        }
                        profileAchievesCardDialog.show();
                        dialogShown = true;
                    }
                }
            }
        }

        private void onProfilePointsAdded(DatabaseReference postRef) {
            postRef.runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {
                    long pointsFromDatabase = 0;
                    if (mutableData != null) {
                        pointsFromDatabase = (long) mutableData.getValue();
                    }
                    pointsFromDatabase = pointsFromDatabase + 200;
                    mutableData.setValue(pointsFromDatabase);
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean b,
                                       DataSnapshot dataSnapshot) {
                    Toast.makeText(getActivity(), "Очки успешно добавлены!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "postTransaction:onComplete:" + databaseError);
                }
            });
        }

        private void onPublicPointsAdded(DatabaseReference postRef) {
            postRef.runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {
                    long pointsFromDatabase = 0;
                    if (mutableData != null) {
                        pointsFromDatabase = (long) mutableData.getValue();
                    }
                    pointsFromDatabase = pointsFromDatabase + 200;
                    mutableData.setValue(pointsFromDatabase);
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean b,
                                       DataSnapshot dataSnapshot) {
                    Toast.makeText(getActivity(), "Очки успешно добавлены к прогрессу!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "postTransaction:onComplete:" + databaseError);
                }
            });
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SharedPreferences settings = getActivity().getSharedPreferences("MAP", 0);
        boolean dialogShown = settings.getBoolean("dialogShown", false);

        if (!dialogShown) {
            AlertDialog.Builder mapFragmentWelcomeDialog = new AlertDialog.Builder(getActivity());
            mapFragmentWelcomeDialog.setTitle(getString(R.string.map_fragment_name));
            mapFragmentWelcomeDialog.setCancelable(false);
            mapFragmentWelcomeDialog.setIcon(R.drawable.ic_menu_map);
            mapFragmentWelcomeDialog.setMessage("В разделе \"Карта\" Вы сможете увидеть все доступные экопункты в Вашем городе. Коснувшись любого флажка, " +
                    "Вы сможете просмотреть подробную информацию о нем, а также проложить к нему маршрут. Кроме того, не забудьте открыть приложение, " +
                    "когда решите посетить один из них. Как только Вы окажетесь в зоне флажка, Вам будут начислены специальные очки, " +
                    "которые будут отображаться в Вашем профиле.");
            mapFragmentWelcomeDialog.setNegativeButton("Понятно", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            mapFragmentWelcomeDialog.show();

            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("dialogShown", true);
            editor.apply();
        }

        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.map_fragment_name));

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                MapFragment.this.mMap = mMap;

                if (ContextCompat.checkSelfPermission(getActivity(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mainActivity.mLocationPermissionGranted = true;
                } else {
                    int PERMISSION_REQUEST_CODE = 5;
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSION_REQUEST_CODE);
                }

                mMap.setMinZoomPreference(10.0f);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, 12));

                if (mainActivity.mLocationPermissionGranted) {

                    MarkerOptions markerOptions = new MarkerOptions();

                    points[0] = new LatLng(55.744733, 37.619666);
                    points[1] = new LatLng(55.756459, 37.620181);
                    points[2] = new LatLng(55.757485, 37.606491);
                    markers[0] = mMap.addMarker(markerOptions.position(points[0]));
                    markers[1] = mMap.addMarker(markerOptions.position(points[1]));
                    markers[2] = mMap.addMarker(markerOptions.position(points[2]));

                    LocationManager mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
                    Location mLastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    mLocationListener.onLocationChanged(mLastKnownLocation);
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                            0, mLocationListener, null);
                }
            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}