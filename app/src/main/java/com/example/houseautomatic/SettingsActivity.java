package com.example.houseautomatic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        TextView ip = (TextView) findViewById(R.id.ipText);
        TextView port = (TextView) findViewById(R.id.portText);
        Switch temp = (Switch) findViewById(R.id.tempSwitch);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String ipT = prefs.getString("ip", null);
        String portT = prefs.getString("port", null);
        boolean tempT = prefs.getBoolean("temperature", false);

        ip.setText(ipT);
        port.setText(portT);
        temp.setChecked(tempT);
    }

    public void BackClick(View view){

        TextView ip = (TextView) findViewById(R.id.ipText);
        TextView port = (TextView) findViewById(R.id.portText);
        Switch temp = (Switch) findViewById(R.id.tempSwitch);

        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("ip", String.valueOf(ip.getText()));
        editor.putString("port", String.valueOf(port.getText()));
        editor.putBoolean("temperature", temp.isChecked());
        editor.apply();

        Toast.makeText(this, "Zapisano", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
