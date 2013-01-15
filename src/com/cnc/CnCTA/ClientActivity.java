package com.cnc.CnCTA;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import com.cnc.model.Server;

import java.util.ArrayList;


public class ClientActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.servers_list);
        final SharedPreferences sharedPref = getSharedPreferences(LoginActivity.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        ((TextView) findViewById(R.id.textLogin)).setText(getString(R.string.login) + sharedPref.getString(LoginActivity.LOGIN_KEY, ""));

        ArrayList<Server> servers = (ArrayList<Server>) getIntent().getSerializableExtra("servers");


        ListView serversList = (ListView) findViewById(R.id.listServers);

        ServerAdapter adapter = new ServerAdapter(this, R.layout.servers_list_row, servers);
        serversList.setAdapter(adapter);

        adapter.notifyDataSetChanged();


    }
}