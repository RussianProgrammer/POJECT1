package sis.pewpew.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import sis.pewpew.MainActivity;
import sis.pewpew.R;

import static android.content.Context.LOCATION_SERVICE;

public class MapFragment extends Fragment {

    private MapView mMapView;
    private LatLng mCurrentLocationLatLng;
    private LatLng mDefaultLocation = new LatLng(55.755826, 37.6173);
    private MainActivity mainActivity = new MainActivity();
    private AlertDialog.Builder mapFragmentWelcomeDialog;


    LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mCurrentLocationLatLng = new LatLng(location.getLatitude(), location.getLongitude());
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        if (mainActivity.mLocationPermissionGranted) {
            LocationManager mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
            Location mLastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                    0, mLocationListener);
            try {
                if (mCurrentLocationLatLng == null) {
                    mCurrentLocationLatLng = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                }
            } catch (NullPointerException e) {
                mCurrentLocationLatLng = mDefaultLocation;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SharedPreferences settings = getActivity().getSharedPreferences("MAP", 0);
        boolean dialogShown = settings.getBoolean("dialogShown", false);

        if (!dialogShown) {
            mapFragmentWelcomeDialog = new AlertDialog.Builder(getActivity());
            mapFragmentWelcomeDialog.setTitle(getString(R.string.map_fragment_name));
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
                if (mainActivity.mLocationPermissionGranted) {
                    mMap.addMarker(new MarkerOptions().position(mCurrentLocationLatLng).title("Вы находитесь здесь"));
                    mMap.setMinZoomPreference(10.0f);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, 12));

                    MarkerOptions markerOptions = new MarkerOptions();

                    final LatLng[] points = new LatLng[1000];
                    points[0] = new LatLng(55.744733, 37.619666);
                    points[1] = new LatLng(55.756459, 37.620181);
                    points[2] = new LatLng(55.757485, 37.606491);

                    final Marker[] markers = new Marker[1000];
                    markers[0] = mMap.addMarker(markerOptions.position(points[0]));
                    markers[1] = mMap.addMarker(markerOptions.position(points[1]));
                    markers[2] = mMap.addMarker(markerOptions.position(points[2]));

                    /*LocationListener mLocationListener = new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            float[] distance = new float[2];
                            for (int i = 0; i < markers.length; i++) {
                                Location.distanceBetween(location.getLatitude(), location.getLongitude(), points[i].latitude, points[i].longitude, distance);
                                if (distance[0] > 100) {
                                    Toast.makeText(getActivity(), "Outside, distance from center: " + distance[0] + " radius: ", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getActivity(), "Inside, distance from center: " + distance[0] + " radius: ", Toast.LENGTH_LONG).show();
                                }
                            }
                        }


                        @Override
                        public void onStatusChanged(String s, int i, Bundle bundle) {
                        }

                        @Override
                        public void onProviderEnabled(String s) {
                        }

                        @Override
                        public void onProviderDisabled(String s) {
                        }*/
                }
                ;
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