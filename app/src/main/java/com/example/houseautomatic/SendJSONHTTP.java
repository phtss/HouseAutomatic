package com.example.houseautomatic;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SendJSONHTTP extends AsyncTask<JSONObject, String, String> {

    private Context mContext;

    ProgressDialog mProgress;
    private TaskCompleted mCallback;

    public SendJSONHTTP(Context context){
        this.mContext = context;
        this.mCallback = (TaskCompleted) context;
    }


    @Override
    protected void onPreExecute() {
        mProgress = new ProgressDialog(mContext);
        mProgress.setMessage("Downloading nPlease wait...");
        mProgress.show();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        mProgress.setMessage(values[0]);
    }

    @Override
    protected String doInBackground(JSONObject... jsonParam) {
        try {
            URL url = new URL("http://192.168.0.164/body");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            Log.i("JSON", jsonParam[0].toString());
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonParam[0].toString());

            os.flush();
            os.close();


            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String decodedString;
            StringBuilder stringBuilder = new StringBuilder();
            while ((decodedString = in.readLine()) != null) {
                stringBuilder.append(decodedString);
            }
            in.close();
            String response = stringBuilder.toString();


            Log.i("MSG" , response);

            conn.disconnect();
            return response;


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(String results) {
        mProgress.dismiss();
        mCallback.onTaskComplete(results);
    }
}