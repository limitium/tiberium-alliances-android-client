package com.cnc.CnCTA;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.cnc.CnCTA.fragment.BaseFragment;
import com.cnc.CnCTA.fragment.BasesFragment;
import com.cnc.CnCTA.fragment.ServersFragment;
import com.cnc.api.CncApiException;
import com.cnc.game.Client;
import com.cnc.model.Server;

import java.util.ArrayList;


public class ClientActivity extends FragmentActivity implements ServersFragment.ServersProvider, ErrorHandler.HandleActivity {
    private ArrayList<Server> servers;
    private final ErrorHandler errorHandler = new ErrorHandler(this);
    private Client gameClient;
    private Handler interfaceHandler;
    private SharedPreferences sharedPref;
    private ServersFragment serversFragment;
    private BaseFragment baseFragment;
    private BasesFragment basesFragment;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.game);


        interfaceHandler = new Handler();
        servers = (ArrayList<Server>) getIntent().getSerializableExtra("servers");
        sharedPref = getSharedPreferences(LoginActivity.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);

        gameClient = new Client();
        gameClient.setHash(sharedPref.getString(LoginActivity.HASH_KEY, ""));

        serversFragment = new ServersFragment(gameClient);
        basesFragment = new BasesFragment(gameClient);
        baseFragment = new BaseFragment(gameClient);

        showFragment(serversFragment);
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_container, fragment);
        ft.commit();
    }

    @Override
    public void enterServer(Server server, final ProgressBar progressBar) {
        gameClient.selectServer(server);
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    return ClientActivity.this.gameClient.openSession();
                } catch (CncApiException e) {
                    errorHandler.showError(e);
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean isOpen) {
                if (isOpen) {
                    progressBar.setVisibility(View.GONE);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.main_container, basesFragment);
                    ft.commit();
                } else {
                    Toast.makeText(getApplicationContext(), "Can't open server session.", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    @Override
    public ArrayList<Server> getServers() {
        return servers;
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