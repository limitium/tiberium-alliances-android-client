package com.cnc.CnCTA.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.cnc.CnCTA.ClientActivity;
import com.cnc.CnCTA.LoginActivity;
import com.cnc.CnCTA.R;
import com.cnc.CnCTA.adapter.ServerAdapter;
import com.cnc.CnCTA.helper.ErrorHandler;
import com.cnc.api.CncApiException;
import com.cnc.game.Client;
import com.cnc.model.Server;

import java.util.ArrayList;


public class ServersFragment extends Fragment {
    private final Client gameClient;
    private final ArrayList<Server> servers;
    private final ErrorHandler errorHandler;
    private ClientActivity activity;


    public ServersFragment(Client gameClient, ErrorHandler errorHandler, ArrayList<Server> servers) {
        this.gameClient = gameClient;
        this.servers = servers;
        this.errorHandler = errorHandler;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (ClientActivity) activity;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.servers_list, container, false);

        final SharedPreferences sharedPref = activity.getSharedPreferences(LoginActivity.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        ((TextView) view.findViewById(R.id.login_text)).setText(sharedPref.getString(LoginActivity.LOGIN_KEY, ""));


        final ListView serversList = (ListView) view.findViewById(R.id.servers_list);

        ServerAdapter adapter = new ServerAdapter(activity, R.layout.servers_list_row, servers);
        serversList.setAdapter(adapter);
        serversList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Server server = (Server) (serversList.getItemAtPosition(position));
                ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.server_enter_progress);
                progressBar.setVisibility(View.VISIBLE);
                enterServer(server);
            }
        });
        return view;
    }

    public void enterServer(Server server) {
        gameClient.selectServer(server);
        final ProgressBar progressBar = (ProgressBar) getView().findViewById(R.id.server_enter_progress);
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    return ServersFragment.this.gameClient.openSession();
                } catch (CncApiException e) {
                    errorHandler.showError(e);
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean isOpen) {
                if (isOpen) {
                    progressBar.setVisibility(View.GONE);
                    activity.showBases();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Can't open server session.", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
}