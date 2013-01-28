package com.cnc.CnCTA.fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.cnc.CnCTA.ClientActivity;
import com.cnc.CnCTA.R;
import com.cnc.CnCTA.adapter.CityAdapter;
import com.cnc.CnCTA.helper.ErrorHandler;
import com.cnc.api.CncApiException;
import com.cnc.game.Client;
import com.cnc.model.Player;
import com.cnc.model.base.City;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Map;

public class BasesFragment extends Fragment {
    private final Client client;
    private final ArrayList<City> bases = new ArrayList<City>();
    private final ErrorHandler errorHandler;
    private ClientActivity activity;

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


        ListView serversList = (ListView) view.findViewById(R.id.listBases);
        CityAdapter adapter = new CityAdapter(client.getPlayer(), activity, R.layout.base_list_row, bases);
        serversList.setAdapter(adapter);
        loadData(view);
        return view;
    }

    public void loadData(View view) {
        final ListView serversList = (ListView) view.findViewById(R.id.listBases);
        final TextView username = (TextView) view.findViewById(R.id.username);
        final TextView supplyPoints = (TextView) view.findViewById(R.id.supplyPointValue);
        final ImageView combatPointsIcon = (ImageView) view.findViewById(R.id.imageCP);
        final TextView combatPoints = (TextView) view.findViewById(R.id.commandPointValue);
        final TextView creditPoints = (TextView) view.findViewById(R.id.creditValue);
        final TextView researchPoints = (TextView) view.findViewById(R.id.researchPointValue);
        final TextView rank = (TextView) view.findViewById(R.id.rankingValue);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.bases_load_progress);

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
                    Player player = BasesFragment.this.client.getPlayer();

                    activity.loadImage(combatPointsIcon, "command_point_" + player.getFraction());

                    username.setText(player.getName());
                    NumberFormat numberFormat = new DecimalFormat("#.##");
                    supplyPoints.setText(numberFormat.format(player.getSupplyPoint().getValue()) + " / " + player.getSupplyPoint().getMax());
                    combatPoints.setText(numberFormat.format(player.getCombatPoint().getValue()) + " / " + player.getCombatPoint().getMax());
                    creditPoints.setText(numberFormat.format(player.getCredits().getValue()));
                    researchPoints.setText(numberFormat.format(player.getResearchPoint()));
                    rank.setText(numberFormat.format(player.getRating()));

                    for (Map.Entry<Long, City> entry : BasesFragment.this.client.getCities().entrySet()) {
                        BasesFragment.this.bases.add(entry.getValue());
                    }
                    ((CityAdapter) serversList.getAdapter()).notifyDataSetChanged();
                }
            }
        }.execute();
    }
}
