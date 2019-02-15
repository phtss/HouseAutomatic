package com.example.houseautomatic;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class LightsActivity extends AppCompatActivity implements TaskCompleted {

    protected int Red;
    protected int Green;
    protected int Blue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lights);
        Red = Green = Blue = 0;
        Log.d("test","");
        SeekBar setRed = (SeekBar) findViewById(R.id.seekBarRed);
        SeekBar setGreen = (SeekBar) findViewById(R.id.seekBarGreen);
        SeekBar setBlue = (SeekBar) findViewById(R.id.seekBarBlue);

        setRed.setOnSeekBarChangeListener(new yourListener());
        setGreen.setOnSeekBarChangeListener(new yourListener());
        setBlue.setOnSeekBarChangeListener(new yourListener());
    }

    @Override
    public void onTaskComplete(String result) {
        JSONObject obj;
        try {
            Toast.makeText(this, "wysłano", Toast.LENGTH_LONG).show();
        } catch (Throwable t) {
            Log.d("My App", "Could not parse malformed JSON: \"" + result + "\"");
        }
    }

    private class yourListener implements SeekBar.OnSeekBarChangeListener {

        @SuppressLint("ResourceType")
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            SeekBar setRed = (SeekBar) findViewById(R.id.seekBarRed);
            SeekBar setGreen = (SeekBar) findViewById(R.id.seekBarGreen);
            SeekBar setBlue = (SeekBar) findViewById(R.id.seekBarBlue);

            if(seekBar.getId() == setRed.getId()) Red = (progress*255)/100;
            else if(seekBar.getId() == setGreen.getId()) Green = (progress*255)/100;
            else if(seekBar.getId() == setBlue.getId()) Blue = (progress*255)/100;


            Log.d("Kolor" , String.valueOf(seekBar.getId()));
            Button  setbtn = (Button) findViewById(R.id.setbtn);
            setbtn.setBackgroundColor(Color.argb(255,Red,Green,Blue));
        }

        public void onStartTrackingTouch(SeekBar seekBar) {}

        public void onStopTrackingTouch(SeekBar seekBar) {}

    }

    public void SetColorClick(View view) throws JSONException {
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("Command", "SetLight");
        jsonParam.put("red", Red);
        jsonParam.put("green", Green);
        jsonParam.put("blue", Blue);
        
        new SendJSONHTTP(this).execute(jsonParam);
    }

    public void BackClick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
