package com.example.memorableplaces;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    private Marker userMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Intent intent = getIntent();
        int intentPosition = intent.getIntExtra("placeNumber", 0);
        Log.i("position2", Integer.toString(intent.getIntExtra("placeNumber", 0)));

        //To activate the location update and be able to save new places the user has to select "Add new places..."
        //This is due to the fact that the locationListener and onLongPressListener are 'locked' behind the if statement.
        if(intentPosition == 0) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    updateUserLocation(location);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {
                }
            };
            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    saveLocation(latLng);
                    Toast.makeText(getApplicationContext(), "Location Saved!", Toast.LENGTH_SHORT).show();
                }
            });

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 10000, locationListener);
                final Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                updateUserLocation(lastKnownLocation);
            }
        }
        else {
            Toast.makeText(this, "Long press to save a location", Toast.LENGTH_SHORT ).show();
            LatLng placeLocation = new LatLng(MainActivity.locations.get(intentPosition).latitude,
                MainActivity.locations.get(intentPosition).longitude);

            userMarker = mMap.addMarker(new MarkerOptions().position(placeLocation).title(MainActivity.places.get(intentPosition)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLocation, 12));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    public void updateUserLocation(Location location) {
        if (userMarker != null)
            userMarker.remove();
        if (location != null) {
            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
            userMarker = mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location!"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 12));
        }
    }
    public void saveLocation(LatLng latLng){
        String address = "";
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            List<Address> addressList  = geocoder.getFromLocation(latLng.latitude, latLng.longitude,1);

            if (addressList != null && addressList.size() > 0) {
                if (addressList.get(0).getThoroughfare() != null) {
                    if (addressList.get(0).getSubThoroughfare() != null)
                        address += addressList.get(0).getSubThoroughfare() + " ";

                    address += addressList.get(0).getThoroughfare();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(address.equals("")){
            SimpleDateFormat sdf  = new SimpleDateFormat(("HH:mm yyyy-MM-dd"));
            address += sdf.format(new Date());
        }

        mMap.addMarker(new MarkerOptions().position(latLng).title(address));
        MainActivity.places.add(address);
        MainActivity.locations.add(latLng);
        MainActivity.listAdapter.notifyDataSetChanged();

        try {
            ArrayList<String> latitudes = new ArrayList<>();
            ArrayList<String> longitudes = new ArrayList<>();

            for (LatLng coord : MainActivity.locations){
                latitudes.add(Double.toString(coord.latitude));
                longitudes.add(Double.toString(coord.longitude));
            }
            MainActivity.sharedPreferences.edit().putString("places", ObjectSerializer.serialize(MainActivity.places)).apply();
            MainActivity.sharedPreferences.edit().putString("lats", ObjectSerializer.serialize(latitudes)).apply();
            MainActivity.sharedPreferences.edit().putString("lons", ObjectSerializer.serialize(longitudes)).apply();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}

