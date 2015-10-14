package com.dunai.rule34.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.dunai.rule34.R;
import com.dunai.rule34.helpers.Devices;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anderson on 14.10.15.
 */
public class LoginActivity extends ActionBarActivity {
    private SharedPreferences prefs;

    class AuthTask extends AsyncTask<String[], Void, String[]> {
        private static final String TAG = "rule34/LoginActivity";

        @Override
        protected String[] doInBackground(String[]... params) {
            String login = params[0][0];
            String pass = params[0][1];

            HttpParams httpParams = new BasicHttpParams();
            httpParams.setParameter(ClientPNames.HANDLE_REDIRECTS, false);
            HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
            HttpConnectionParams.setSoTimeout(httpParams, 5000);
            HttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpPost request = new HttpPost("http://rule34.xxx/index.php?page=account&s=login&code=00");

            List<NameValuePair> requestParams = new ArrayList<NameValuePair>(2);
            requestParams.add(new BasicNameValuePair("user", login));
            requestParams.add(new BasicNameValuePair("pass", pass));
            try {
                request.setEntity(new UrlEncodedFormEntity(requestParams));
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
                return null;
            }

            HttpResponse response;

            try {
                response = httpClient.execute(request);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
                return null;
            }

            Header[] cookies = response.getHeaders("Set-Cookie");

            try {
                Log.i(TAG, EntityUtils.toString(response.getEntity()));
            } catch (IOException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            for (Header header : response.getAllHeaders()) {
                Log.i(TAG, header.getName() + " = " + header.getValue());
            }

            String[] cookieArray = new String[cookies.length];
            if (cookies.length > 0) {
                for (int i = 0; i < cookies.length; i++) {
                    Header cookie = cookies[i];
                    Log.i(TAG, cookie.getName() + " = " + cookie.getValue());
                    cookieArray[i] = cookie.getValue();
                }
                return cookieArray;
            } else {
                Log.i(TAG, "Auth failed!");
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] cookies) {
            super.onPostExecute(cookies);

            if (cookies != null) {
                Toast.makeText(getApplicationContext(), "Welcome!", Toast.LENGTH_SHORT).show();
                SharedPreferences prefs = getApplicationContext().getSharedPreferences("com.dunai.rule34", Context.MODE_PRIVATE);

                String userId = null;
                String passHash = null;

                for(String cookie : cookies) {
                    String part1 = cookie.split(";")[0];
                    String[] parts = part1.split("=");
                    if (parts[0].equals("user_id")) {
                        userId = parts[1];
                    } else if (parts[0].equals("pass_hash")) {
                        passHash = parts[1];
                    }
                }

                if (userId == null || passHash == null) {
                    Toast.makeText(getApplicationContext(), "Error: missing cookie :(", Toast.LENGTH_SHORT).show();
                    return;
                }

                prefs.edit().putBoolean("authorized", true).putString("user_id", userId).putString("pass_hash", passHash).commit();

//                updateState();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Wrong login or password", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);

        this.prefs = this.getSharedPreferences("com.dunai.rule34", Context.MODE_PRIVATE);

        final EditText loginEditText = (EditText) this.findViewById(R.id.loginEditText);
        final EditText passwordEditText = (EditText) this.findViewById(R.id.passwordEditText);

        this.findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = loginEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                new AuthTask().execute(new String[] { login, password });
            }
        });

        this.findViewById(R.id.logoutButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs.edit().putBoolean("authorized", false).commit();
                updateState();
            }
        });

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.updateState();
    }

    public void updateState() {
        boolean isLoggedIn = this.prefs.getBoolean("authorized", false);
        this.findViewById(R.id.loginLayout).setVisibility(isLoggedIn ? View.GONE : View.VISIBLE);
        this.findViewById(R.id.logoutLayout).setVisibility(isLoggedIn ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
