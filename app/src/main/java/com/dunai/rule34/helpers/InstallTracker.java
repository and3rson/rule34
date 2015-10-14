package com.dunai.rule34.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by anderson on 14.10.15.
 */
public class InstallTracker extends AsyncTask<Void, Void, Void> {
    // I want this to know how many people installed an app.

    private static final String TAG = "rule34/InstallTracker";

    private Context context;

    public InstallTracker(Context context) {
        this.context = context;
    }

    public Void doInBackground(Void... params) {
        SharedPreferences prefs = this.context.getSharedPreferences("com.dunai.rule34", Context.MODE_PRIVATE);

        if (prefs.getBoolean("tracked", false)) {
            return null;
        }

        String url;

        try {
            url = "http://rpc.andrewdunai.com/log/?v=" + URLEncoder.encode(Devices.getDeviceName(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }

        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
        HttpConnectionParams.setSoTimeout(httpParams, 5000);
        HttpClient httpClient = new DefaultHttpClient(httpParams);
        HttpRequestBase request = new HttpGet(url);

        try {
            httpClient.execute(request);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }

        prefs.edit().putBoolean("tracked", true).apply();

        return null;
    }
}
