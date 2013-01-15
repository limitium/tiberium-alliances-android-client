package com.cnc.CnCTA.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.cnc.CnCTA.LoginActivity;
import com.cnc.CnCTA.R;
import com.cnc.CnCTA.adapter.ServerAdapter;
import com.cnc.model.Server;

import java.util.ArrayList;


public class ServersFragment extends Fragment {
    public interface ServersProvider {
        public ArrayList<Server> getServers();
    }
    ServersProvider provider;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            provider = (ServersProvider) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement ServersProvider");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bases, container, false);

        final SharedPreferences sharedPref = getActivity().getSharedPreferences(LoginActivity.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        ((TextView) view.findViewById(R.id.textLogin)).setText(getString(R.string.login) + sharedPref.getString(LoginActivity.LOGIN_KEY, ""));


        ListView serversList = (ListView) view.findViewById(R.id.listServers);

        ServerAdapter adapter = new ServerAdapter(getActivity(), R.layout.servers_list_row, provider.getServers());
        serversList.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        return view;
    }
}