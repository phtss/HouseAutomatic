package com.example.houseautomatic;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import android.preference.PreferenceManager;
public class MainActivity extends AppCompatActivity implements TaskCompleted{
    public static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 0;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public static String ipserwera;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String ipT = prefs.getString("ip", "");
        ipserwera = ipT;


        if(ipserwera.equals(new String("192.168.0.164")) || ipserwera.equals(new String("192.168.43.33"))) {
            Random r = new Random();
            float temperatura = r.nextInt(22 - 20) + 19;
            int pobor = r.nextInt(850 - 700) + 700;
            int wilgotnosc = r.nextInt(68 - 65) + 65;

            boolean temps = prefs.getBoolean("temperature", false);

            TextView temp = (TextView) findViewById(R.id.tempText);
            if(temps)
                temp.setText("Temperatura: " + temperatura + " C");
            else{
                temperatura = (float) (temperatura*1.8)+32;
                temp.setText("Temperatura: " + temperatura + " F");
            }
            TextView wilg = (TextView) findViewById(R.id.wilgText);
            wilg.setText("Wilgotność: " + wilgotnosc + "%");
            TextView power = (TextView) findViewById(R.id.powerText);
            power.setText("Pobór mocy: " + pobor + "Wat");
        }
        else {
            TextView temp = (TextView) findViewById(R.id.tempText);
            temp.setText("Temperatura: -");
            TextView wilg = (TextView) findViewById(R.id.wilgText);
            wilg.setText("Wilgotność: -");
            TextView power = (TextView) findViewById(R.id.powerText);
            power.setText("Pobór mocy: -");

        }


        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            Log.i("RSAD","Pytamy o uprawnienia");
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSIONS_REQUEST_FINE_LOCATION);

        }
        else{
            Log.i("RSAD", "Są uprawnienia");

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();
                    double alt = location.getAltitude();

                    Locale locale = Locale.getDefault();

                    String locInfo = String.format(locale, "Aktualne położenie: %f, %f, wysokość: %f", lat, lon, alt);
                    Log.i("RSAD",locInfo);

                    Geocoder gcd = new Geocoder(getApplicationContext(), locale);
                    List<Address> address = null;

                    try {
                        address = gcd.getFromLocation(lat,lon,1);
                        String countryName = address.get(0).getCountryName();
                        String addressLine1 = address.get(0).getAddressLine(0);
                        String addressLine2 = address.get(0).getAddressLine(1);
                        String addressInfo;
                        if (addressLine2 == null) {
                            addressInfo = String.format(locale, "Adres: %s, %s", addressLine1, countryName);
                        }
                        else{
                            addressInfo = String.format(locale, "Adres: %s, %s, %s", addressLine1, addressLine2, countryName);
                        }
                        Log.i("RSAD", addressInfo);

                        TextView tvAddress = (TextView) findViewById(R.id.localText);
                        tvAddress.setText(addressInfo);


                        // PG: lat 54.3714246, lon 18.6191359

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                    switch (status) {
                        case GpsStatus.GPS_EVENT_STARTED:
                            Log.i("RSAD", "Start");
                            break;

                        case GpsStatus.GPS_EVENT_STOPPED:
                            Log.i("RSAD", "Stop");
                            break;

                        case GpsStatus.GPS_EVENT_FIRST_FIX:
                            Log.i("RSAD", "Fix");
                            break;

                        case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                            Log.i("RSAD", "Satellites");
                            break;
                    }

                }

                @Override
                public void onProviderEnabled(String provider) {
                    Log.i("RSAD","Ok, wracamy do zabawy!");

                }

                @Override
                public void onProviderDisabled(String provider) {
                    Log.i("RSAD","Hej, włącz lokalizację!");

                }
            });


        }

    }

    public void LightsClick(View view){
        //Toast.makeText(this, "M", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, LightsActivity.class);
        startActivity(intent);
    }

    public void DevicesClick(View view){
        //Toast.makeText(this, "M", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, DevicesControllActivity.class);
        startActivity(intent);
    }

    public void SettingsClick(View view){
       // Toast.makeText(this, "M", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void refreshClick(View view) throws IOException, JSONException, InterruptedException {
        if(ipserwera.equals(new String("192.168.0.164")) || ipserwera.equals(new String("192.168.43.33"))) {
            Random r = new Random();
            float temperatura = r.nextInt(22 - 20) + 19;
            int pobor = r.nextInt(850 - 700) + 700;
            int wilgotnosc = r.nextInt(68 - 65) + 65;

            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            boolean temps = prefs.getBoolean("temperature", false);

            TextView temp = (TextView) findViewById(R.id.tempText);
            if (temps)
                temp.setText("Temperatura: " + temperatura + " C");
            else {
                temperatura = (float) (temperatura * 1.8) + 32;
                temp.setText("Temperatura: " + temperatura + " F");
            }
            TextView wilg = (TextView) findViewById(R.id.wilgText);
            wilg.setText("Wilgotność: " + wilgotnosc + "%");
            TextView power = (TextView) findViewById(R.id.powerText);
            power.setText("Pobór mocy: " + pobor + " Wat");

            Thread.sleep(500);

            //Toast.makeText(this, "M", Toast.LENGTH_LONG).show();
            //JSONObject jsonParam = new JSONObject();
            //jsonParam.put("Command", "RefreshPower");
            //jsonParam.put("latitude", "test");
            //jsonParam.put("longitude", 0D);
            //jsonParam.put("red", 255);

            // new SendJSONHTTP(this).execute(jsonParam);
            Toast.makeText(this, "Pobrano dane", Toast.LENGTH_LONG).show();
        } else {

            TextView temp = (TextView) findViewById(R.id.tempText);
            temp.setText("Temperatura: -");
            TextView wilg = (TextView) findViewById(R.id.wilgText);
            wilg.setText("Wilgotność: -");
            TextView power = (TextView) findViewById(R.id.powerText);
            power.setText("Pobór mocy: -");
        }
    }


    @Override
    public void onTaskComplete(String result) {
        JSONObject obj;
        try {
            obj = new JSONObject(result);
            Toast.makeText(this, obj.get("actualPower").toString(), Toast.LENGTH_LONG).show();
        } catch (Throwable t) {
            Log.d("My App", "Could not parse malformed JSON: \"" + result + "\"");
        }

    }
}
