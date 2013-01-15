package com.cnc.CnCTA;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import com.cnc.CnCTA.adapter.ServerAdapter;
import com.cnc.model.Server;

import java.util.ArrayList;


public class ClientActivity extends Activity {
    private ArrayList<Server> servers;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.game);

        servers = (ArrayList<Server>) getIntent().getSerializableExtra("servers");





    }
}