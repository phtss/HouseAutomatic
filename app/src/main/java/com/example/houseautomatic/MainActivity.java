package com.example.houseautomatic;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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

public class MainActivity extends AppCompatActivity implements TaskCompleted {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    public void refreshClick(View view) throws IOException, JSONException {
       //Toast.makeText(this, "M", Toast.LENGTH_LONG).show();

        JSONObject jsonParam = new JSONObject();
        jsonParam.put("Command", "RefreshPower");
        //jsonParam.put("latitude", "test");
        //jsonParam.put("longitude", 0D);
        //jsonParam.put("red", 255);

        new SendJSONHTTP(this).execute(jsonParam);
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
