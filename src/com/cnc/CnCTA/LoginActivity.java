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
import com.cnc.api.CncApiException;
import com.cnc.game.Client;
import com.cnc.model.Server;

import java.util.ArrayList;

public class LoginActivity extends Activity implements ErrorHandler.HandleActivity{

    public final String TAG = "CnCTA:Login";

    public static final String LOGIN_KEY = "LOGIN";
    public static final String PASSWORD_KEY = "PASSWORD";
    public static final String HASH_KEY = "HASH";
    public static final String SHARED_PREFERENCES_KEY = "AUTH";
    private final ErrorHandler errorHandler = new ErrorHandler(this);
    private Client gameClient;

    private EditText passwordInput;
    private SharedPreferences sharedPref;
    private EditText loginInput;
    private Button loginButton;
    private TextView loginStatus;
    private ProgressBar loginProgress;
    private Handler interfaceHandler;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        sharedPref = getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);

        interfaceHandler = new Handler();

        gameClient = new Client();

        loginInput = (EditText) findViewById(R.id.login);
        passwordInput = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.loginButton);
        loginStatus = (TextView) findViewById(R.id.loginStatus);
        loginProgress = (ProgressBar) findViewById(R.id.loginProgress);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = loginInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                LoginActivity.this.authorize(login, password);
            }
        });
        loginInput.setText(sharedPref.getString(LOGIN_KEY, "lworld10@mailinator.com"));
        passwordInput.setText(sharedPref.getString(PASSWORD_KEY, "qweqwe123"));

        loginStatus.setVisibility(View.GONE);
        loginProgress.setVisibility(View.GONE);
    }

    private void authorize(String login, String password) {
        setStatus("", 0);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(LoginActivity.LOGIN_KEY, login);
        editor.putString(LoginActivity.PASSWORD_KEY, password);
        editor.commit();

        loginStatus.setVisibility(View.VISIBLE);
        loginProgress.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.GONE);

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                Log.d(TAG, params[0] + " " + params[1]);
                try {
                    return Authorizator.authorize(params[0], params[1], new Authorizator.Progress() {
                        @Override
                        public void onStep(final int step, final String msg) {
                            interfaceHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    setStatus(msg, step * 18);
                                }
                            });
                        }
                    });
                } catch (final CncApiException e) {
                    errorHandler.showError(e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(String hash) {
                Log.d(TAG, "Hash " + hash);
                if (hash != null) {
                    LoginActivity.this.loadServers(hash);
                } else {
                    Toast.makeText(getApplicationContext(), "Can't take token.", Toast.LENGTH_SHORT).show();
                    loginButton.setVisibility(View.VISIBLE);
                }
            }
        }.execute(login, password);
    }

    private void showError(final CncApiException e) {
        errorHandler.showError(e);
    }


    private void loadServers(String hash) {
        setStatus("Get servers");
        gameClient.setHash(hash);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(LoginActivity.HASH_KEY, hash);
        editor.commit();

        new AsyncTask<Void, Void, ArrayList<Server>>() {
            @Override
            protected ArrayList<Server> doInBackground(Void... params) {
                try {
                    return LoginActivity.this.gameClient.updateServers();
                } catch (CncApiException e) {
                    errorHandler.showError(e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<Server> servers) {
                if (servers == null || servers.size() == 0) {
                    Toast.makeText(getApplicationContext(), "No servers found.", Toast.LENGTH_SHORT).show();
                } else {
                    startGameActivity(servers);
                }
                loginButton.setVisibility(View.VISIBLE);
            }
        }.execute();
    }


    private void startGameActivity(ArrayList<Server> result) {
        setStatus("Launching game", 100);

        Intent intent = new Intent(this, ClientActivity.class);
        intent.putExtra("servers", result);
        startActivity(intent);

        loginStatus.setVisibility(View.GONE);
        loginProgress.setVisibility(View.GONE);
    }

    private void setStatus(String status) {
        loginStatus.setText(status);
    }

    private void setStatus(String status, int progress) {
        loginProgress.setProgress(progress);
        loginStatus.setText(status);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public Handler getHandler() {
        return interfaceHandler;
    }
}
