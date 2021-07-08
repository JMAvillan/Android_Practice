package com.example.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import dalvik.system.PathClassLoader;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLocation(location);
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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        else{
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER,0,0, locationListener);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation != null)
                updateLocation(lastKnownLocation);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            startListening();
    }

    public void startListening(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER,0,0, locationListener);
    }


    public void updateLocation(Location location){
        TextView latTextView = findViewById(R.id.latittude);
        TextView lonTextView = findViewById(R.id.longitude);
        TextView accTextView = findViewById(R.id.accuracy);
        TextView altTextView = findViewById(R.id.altitude);
        TextView addressTextView = findViewById(R.id.address);

        latTextView.setText("Latitude: " + Double.toString(location.getLatitude()));
        lonTextView.setText("Longitude: " + Double.toString(location.getLongitude()));
        accTextView.setText("Accuracy: " + Double.toString(location.getAccuracy()));
        altTextView.setText("Altitude: " + Double.toString(location.getAltitude()));

        String address = "Could not find address.";

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if(listAddresses != null && listAddresses.size() > 0) {
                address = "Address:\n";

                Log.i("Address: ", listAddresses.get(0).toString());
                if(listAddresses.get(0).getThoroughfare() != null)
                    address += listAddresses.get(0).getThoroughfare() + "\n";
                if(listAddresses.get(0).getLocality() != null)
                    address += listAddresses.get(0).getLocality() + "\n";
                if(listAddresses.get(0).getPostalCode() != null)
                    address += listAddresses.get(0).getPostalCode() + "\n";
                if(listAddresses.get(0).getAdminArea() != null)
                    if(listAddresses.get(0).getAdminArea().compareTo(listAddresses.get(0).getLocality()) != 0)
                        address += listAddresses.get(0).getAdminArea() + "\n";
                    else
                        address += listAddresses.get(0).getCountryName() + "\n";

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        addressTextView.setText(address);
        }
}
