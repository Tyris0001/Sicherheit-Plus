package com.sicherheitplus;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.Menu;
import android.Manifest;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.sicherheitplus.classes.Contact;
import com.sicherheitplus.databinding.ActivityMainBinding;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private LocationManager locationManager;
    private double latitude;
    private double longitude;
    private View ctx;
    private SharedPreferences sharedPreferences;

    public HashMap<String, String> getNearestLocation(double latitude, double longitude) throws IOException {
        if (ctx == null || (latitude == 0.0 && longitude == 0.0))
            return new HashMap<String, String>();
        Geocoder geocoder = new Geocoder(ctx.getContext());
        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
        if (addresses.isEmpty()) {
            throw new IOException("No address found for latitude " + latitude + " and longitude " + longitude);
        }
        Address address = addresses.get(0);
        String country = address.getCountryName();
        String city = address.getLocality();
        String street = address.getThoroughfare();
        HashMap<String, String> result = new HashMap<>();
        result.put("Country", country);
        result.put("City", city);
        result.put("Street", street);
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // create test contact if not exists
        // check if test contact exists, if not create new one in shared preferences
        sharedPreferences = getSharedPreferences("Contacts", MODE_PRIVATE);

        HashMap<String, String> contacts = (HashMap<String, String>) sharedPreferences.getAll();
        if (contacts.isEmpty()) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Test", "123456789");
            editor.apply();
        }
        //ask for permission to send SMS
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    REQUEST_LOCATION_PERMISSION);
        }

        // Berechtigungsanfrage
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ctx = view;
                // read all data from peristence database under key "Contacts"
                try {
                    HashMap<String, String> geoInfo = getNearestLocation(latitude, longitude);
                    if (geoInfo.isEmpty()) {
                        Snackbar.make(view, "Something went wrong, you're on your own now.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        return;
                    }
                    String country = geoInfo.get("Country");
                    String city = geoInfo.get("City");
                    String street = geoInfo.get("Street");

                    for (Map.Entry<String, String> entry : contacts.entrySet()) {
                        String name = entry.getKey();
                        String phone = entry.getValue();
                        String message = String.format("Hallo %s, ich bin in Gefahr. Ich befinde mich in %s, %s, %s. Bitte ruf mich nicht an. Diese nachricht wurde mit SicherheitPlus versandt!", name, street, city, country);
                        SmsManager smsManager = SmsManager.getDefault();
                        try {
                            smsManager.sendTextMessage(phone, null, message, null, null);
                            Snackbar.make(view, "SMS sent.", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        } catch (Exception e) {
                            Snackbar.make(view, String.format("Unable to send message to contact %s, make sure their phone number is valid!", name), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            throw new RuntimeException(e);
                        }
                    }

                } catch (IOException e) {
                    Snackbar.make(view, "Something went wrong, you're on your own now.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    throw new RuntimeException(e);
                }


            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                }

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public void onLocationChanged(Location location) {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }
}