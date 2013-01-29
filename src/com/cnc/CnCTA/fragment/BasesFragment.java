package com.cnc.CnCTA.fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.cnc.CnCTA.ClientActivity;
import com.cnc.CnCTA.R;
import com.cnc.CnCTA.adapter.CityAdapter;
import com.cnc.CnCTA.helper.ErrorHandler;
import com.cnc.CnCTA.helper.Formater;
import com.cnc.api.CncApiException;
import com.cnc.game.Client;
import com.cnc.model.Player;
import com.cnc.model.base.City;

import java.util.ArrayList;

public class BasesFragment extends Fragment {
    private final Client client;
    private final ArrayList<City> bases = new ArrayList<City>();
    private final ErrorHandler errorHandler;
    private ClientActivity activity;
    private boolean loaded = false;

    public BasesFragment(Client client, ErrorHandler errorHandler) {
        this.client = client;
        this.errorHandler = errorHandler;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (ClientActivity) activity;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bases, container, false);


        final ListView basesList = (ListView) view.findViewById(R.id.listBases);
        CityAdapter adapter = new CityAdapter(client.getPlayer(), activity, R.layout.base_list_row, bases);
        basesList.setAdapter(adapter);
        basesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                City city = (City) (basesList.getItemAtPosition(position));
                activity.openCity(city);
            }
        });
        loadData(view);
        return view;
    }

    public void loadData(final View view) {
        final ImageView combatPointsIcon = (ImageView) view.findViewById(R.id.imageCP);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.bases_load_progress);
        final ListView serversList = (ListView) view.findViewById(R.id.listBases);

        progressBar.setVisibility(View.GONE);
        bases.clear();
        ((CityAdapter) serversList.getAdapter()).notifyDataSetChanged();

        if (loaded) {
            updateUI(view);
            return;
        }

        activity.loadImage(combatPointsIcon, "transparent_24");
        progressBar.setVisibility(View.VISIBLE);


        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    client.updateAllData();
                    return true;
                } catch (CncApiException e) {
                    errorHandler.showError(e);
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean done) {
                progressBar.setVisibility(View.GONE);
                if (done) {
                    BasesFragment.this.loaded = true;
                    updateUI(view);
                }
            }
        }.execute();
    }

    private void updateUI(View view) {
        final ListView serversList = (ListView) view.findViewById(R.id.listBases);
        final TextView username = (TextView) view.findViewById(R.id.username);
        final TextView supplyPoints = (TextView) view.findViewById(R.id.supplyPointValue);
        final ImageView combatPointsIcon = (ImageView) view.findViewById(R.id.imageCP);
        final TextView combatPoints = (TextView) view.findViewById(R.id.commandPointValue);
        final TextView creditPoints = (TextView) view.findViewById(R.id.creditValue);
        final TextView researchPoints = (TextView) view.findViewById(R.id.researchPointValue);
        final TextView rank = (TextView) view.findViewById(R.id.rankingValue);

        Player player = client.getPlayer();

        activity.loadImage(combatPointsIcon, "command_point_" + player.getFraction());

        username.setText(player.getName());
        supplyPoints.setText(Formater.number(player.getSupplyPoint().getValue()) + " / " + player.getSupplyPoint().getMax());
        combatPoints.setText(Formater.number(player.getCombatPoint().getValue()) + " / " + player.getCombatPoint().getMax());
        creditPoints.setText(Formater.resource(player.getCredits().getValue()));
        researchPoints.setText(Formater.resource(player.getResearchPoint()));
        rank.setText(Formater.number(player.getRating()));

        bases.addAll(client.getCities().values());
        ((CityAdapter) serversList.getAdapter()).notifyDataSetChanged();
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }
}
