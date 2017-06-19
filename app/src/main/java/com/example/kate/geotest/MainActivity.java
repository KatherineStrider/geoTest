package com.example.kate.geotest;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kate.geotest.sqlite.DBGeoList;
import com.example.kate.geotest.sqlite.DBGeoListProvider;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private ProgressBar progressBar;
    private TextView textLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        getLocation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        switch (item.getItemId()) {

            case R.id.geoHistory:
                intent = new Intent(MainActivity.this, GeoListActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    private void initView() {

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        textLocation = (TextView) findViewById(R.id.textLocation);
    }

    private void getLocation() {

        LocationManager locationManager;
        Location location;

        locationManager = (LocationManager)
                this.getSystemService(Context.LOCATION_SERVICE);

        /**
         * Для определения города используем приблизительное местоположение на основе данных сети
         */

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        } else {

            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            progressBar.setVisibility(View.INVISIBLE);
            textLocation.setVisibility(View.VISIBLE);

            if (location == null) {
                textLocation.setText(R.string.MainActivity_GeoExсeption);
                return;
            } else {
                textLocation.setText(getCity(location));
            }
        }
    }

    private String getCity(Location location) {

        String city = "";
        Geocoder geo = new Geocoder(this);

        try {

            List<Address> aList = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (aList.size() > 0) {

                Address a = aList.get(0);
                int maxAddrLine = a.getMaxAddressLineIndex();
                if (maxAddrLine < 0) {
                    return null;
                }
                city = a.getLocality();
                saveLocation(city);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return e.getLocalizedMessage();
        }
        return city;
    }

    private void saveLocation(String city) {
        ContentValues values = new ContentValues();
        values.put(DBGeoList.TableItems.C_GEO, city);
        getContentResolver().insert(DBGeoListProvider.CONTENT_URI_ITEMS, values);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        Location location;
        LocationManager locationManager = (LocationManager)
                this.getSystemService(Context.LOCATION_SERVICE);
        ;

        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                progressBar.setVisibility(View.INVISIBLE);
                textLocation.setVisibility(View.VISIBLE);

                if (location == null) {
                    textLocation.setText(R.string.MainActivity_GeoExсeption);
                    return;
                } else {
                    textLocation.setText(getCity(location));
                }
            } else {
                // Permission was denied or request was cancelled
            }
        }
    }
}
