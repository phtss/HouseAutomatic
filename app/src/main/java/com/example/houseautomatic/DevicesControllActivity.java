package com.example.houseautomatic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class DevicesControllActivity extends AppCompatActivity implements TaskCompleted  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices_controll);
    }

    public void device1Click(View view) throws JSONException {
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("Command", "DeviceOnOff");
        new SendJSONHTTP(this).execute(jsonParam);

    }

    public void BackClick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onTaskComplete(String result) {
        JSONObject obj;
        try {
            //obj = new JSONObject(result);
            Toast.makeText(this, "Gotowe", Toast.LENGTH_LONG).show();
        } catch (Throwable t) {
            Log.d("My App", "Could not parse malformed JSON: \"" + result + "\"");
        }

    }
}
