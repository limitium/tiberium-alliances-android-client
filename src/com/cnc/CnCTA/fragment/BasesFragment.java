package com.cnc.CnCTA.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.cnc.CnCTA.R;
import com.cnc.CnCTA.adapter.CityAdapter;
import com.cnc.game.Client;
import com.cnc.game.GameServer;
import com.cnc.model.Player;
import com.cnc.model.base.City;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Map;

public class BasesFragment extends Fragment {
    private final Client client;
    private final ArrayList<City> bases = new ArrayList<City>();

    public BasesFragment(Client client) {
        this.client = client;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bases, container, false);


        ListView serversList = (ListView) view.findViewById(R.id.listBases);
        CityAdapter adapter = new CityAdapter(getActivity(), R.layout.base_list_row, bases);
        serversList.setAdapter(adapter);
        loadData(view);
        return view;
    }

    public void loadData(View view) {
        final ListView serversList = (ListView) view.findViewById(R.id.listBases);
        final TextView username = (TextView) view.findViewById(R.id.username);
        final TextView supplyPoints = (TextView) view.findViewById(R.id.supplyPointValue);
        final TextView combatPoints = (TextView) view.findViewById(R.id.commandPointValue);

        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                client.updateAllData();
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                Player player = BasesFragment.this.client.getPlayer();
                username.setText(player.getName());
                NumberFormat numberFormat= new DecimalFormat("#.##");
                supplyPoints.setText(numberFormat.format(player.getSupplyPoint().getValue()) + " / " + player.getSupplyPoint().getMax());
                combatPoints.setText(numberFormat.format(player.getCombatPoint().getValue()) + " / " + player.getCombatPoint().getMax());
                for (Map.Entry<Long, City> entry : BasesFragment.this.client.getCities().entrySet()) {
                    BasesFragment.this.bases.add(entry.getValue());
                }

                ((CityAdapter) serversList.getAdapter()).notifyDataSetChanged();
            }
        }.execute();
    }
}
