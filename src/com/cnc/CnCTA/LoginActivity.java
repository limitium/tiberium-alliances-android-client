package com.cnc.CnCTA;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.cnc.api.Authorizator;
import com.cnc.game.Client;
import com.cnc.game.GameServer;
import com.cnc.model.Server;

import java.util.ArrayList;

public class LoginActivity extends Activity {

    public final String TAG = "CnCTA:Login";

    public static final String LOGIN_KEY = "LOGIN";
    public static final String PASSWORD_KEY = "PASSWORD";
    public static final String SHARED_PREFERENCES_KEY = "AUTH";
    public static final String LAST_HASH_KEY = "LAST_HASH";
    public static final String LAST_SERVER_ID_KEY = "SERVER_ID";
    private GameServer gameServer;
    private Client gameClient;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.auth_dialog);

        final SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        final EditText loginInput = (EditText) findViewById(R.id.login);
        final EditText passwordInput = (EditText) findViewById(R.id.password);
        final Button loginButton = (Button) findViewById(R.id.loginButton);
        final TextView loginStatus = (TextView) findViewById(R.id.loginStatus);
        final ProgressBar loginProgress = (ProgressBar) findViewById(R.id.loginProgress);

        loginStatus.setVisibility(View.GONE);
        loginProgress.setVisibility(View.GONE);

        loginInput.setText(sharedPref.getString(LOGIN_KEY, "lworld10@mailinator.com"));
        passwordInput.setText(sharedPref.getString(PASSWORD_KEY, "qweqwe123"));
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(LoginActivity.LOGIN_KEY, loginInput.getText().toString().trim());
                editor.putString(LoginActivity.PASSWORD_KEY, passwordInput.getText().toString().trim());
                editor.commit();

                LoginActivity.this.authorize();
            }
        });

        gameServer = new GameServer();
        gameClient = new Client(gameServer);
    }

    private void authorize() {
        final SharedPreferences sharedPref = getSharedPreferences(LoginActivity.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        final ProgressBar loginProgress = (ProgressBar) findViewById(R.id.loginProgress);
        final TextView loginStatus = (TextView) findViewById(R.id.loginStatus);
        final Button loginButton = (Button) findViewById(R.id.loginButton);

        loginStatus.setVisibility(View.VISIBLE);
        loginProgress.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.GONE);

        loginProgress.setProgress(0);
        loginStatus.setText("");
        final Handler handler = new Handler();
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                Log.d(TAG, params[0] + " " + params[1]);
                return Authorizator.authorize(params[0], params[1], new Authorizator.Progress() {
                    @Override
                    public void onStep(final int step, final String msg) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                loginStatus.setText(msg);
                                loginProgress.setProgress(step * 18);
                            }
                        });
                    }
                });
            }

            @Override
            protected void onPostExecute(String result) {
                Log.d(TAG, "Hash " + result);
                if (result != null) {
                    LoginActivity.this.loadServers(result);
                } else {
                    loginButton.setVisibility(View.VISIBLE);
                }
            }
        }.execute(sharedPref.getString(LOGIN_KEY, ""), sharedPref.getString(PASSWORD_KEY, ""));
    }

    private void loadServers(String result) {
        gameServer.setHash(result);
        final ProgressBar loginProgress = (ProgressBar) findViewById(R.id.loginProgress);
        final TextView loginStatus = (TextView) findViewById(R.id.loginStatus);
        final Button loginButton = (Button) findViewById(R.id.loginButton);

        loginStatus.setText("Get servers");
        new AsyncTask<Void, Void, ArrayList<Server>>() {
            @Override
            protected ArrayList<Server> doInBackground(Void... params) {
                return LoginActivity.this.gameClient.getServers();
            }

            @Override
            protected void onPostExecute(ArrayList<Server> result) {
                if (result == null || result.size() == 0) {
                    loginButton.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "No servers found.", Toast.LENGTH_SHORT).show();
                } else {
                    loginProgress.setProgress(100);
                    loginStatus.setText("Launching game");
                    startActivity(new Intent(LoginActivity.this, TestActivity.class));
                }
            }
        }.execute();
    }

}
