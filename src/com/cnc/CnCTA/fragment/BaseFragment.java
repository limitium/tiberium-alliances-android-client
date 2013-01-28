package com.cnc.CnCTA.fragment;

import android.support.v4.app.Fragment;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.cnc.CnCTA.R;
import com.cnc.CnCTA.adapter.BuildingAdapter;
import com.cnc.CnCTA.helper.ErrorHandler;
import com.cnc.game.Client;
import com.cnc.model.base.Building;
import com.cnc.model.base.BuildingType;

import java.util.ArrayList;
import java.util.HashMap;

public class BaseFragment extends Fragment {

    private GridView buildingsGrid;
    private BuildingAdapter buildingAdapter;
    private ArrayList<String> buildings;
    private Building[] buildings2;
    private HashMap<BuildingType, Integer> buildingIconMap = new HashMap<BuildingType, Integer>() {{
        put(BuildingType.CONSTRUCTION_YARD, R.drawable.base_construction_yard);
        put(BuildingType.ACCUMULATOR, R.drawable.base_accumulator);
        put(BuildingType.AIRFIELD, R.drawable.base_airfield);
        put(BuildingType.BARRACKS, R.drawable.base_barracks);
        put(BuildingType.DEFENCE_FACILITY, R.drawable.base_defense_facility);
        put(BuildingType.DEFENCE_HQ, R.drawable.base_defense_hq);
        put(BuildingType.FACTORY, R.drawable.base_factory);
        put(BuildingType.HARVESTER, R.drawable.base_harvester2);
        put(BuildingType.POWER_PLANT, R.drawable.base_power_plant);
        put(BuildingType.REFINARY, R.drawable.base_refinery);
        put(BuildingType.SILO, R.drawable.base_silo);
        put(BuildingType.STRIKE_SUPPORT, R.drawable.base_strike_support);
    }};
    private final Client gameClient;

    public BaseFragment(Client gameClient, ErrorHandler errorHandler) {
        this.gameClient = gameClient;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.base, container, false);

        buildingsGrid = (GridView) view.findViewById(R.id.base_buildings);

        buildings = new ArrayList<String>();
        buildingAdapter = new BuildingAdapter(getActivity(), R.layout.base_building_cell, buildings);
        buildingsGrid.setAdapter(buildingAdapter);

        for (int i = 0; i < 8 * 9; i++) {
            buildings.add("");
        }

//        int i = x + y * x;
        updateGridMargins();
        return view;
    }

    public void updateGridMargins() {
        Point size = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(size);
        float scaleX = size.x / 1024.0f;
        float scaleY = size.y / 768.0f;
        int bottom = (int) (38 * scaleY);
        int top = (int) (75 * scaleY);
        int left = (int) (90 * scaleX);
        int right = (int) (90 * scaleX);
        buildingsGrid.setPadding(left, top, right, bottom);
        buildingAdapter.setCellSize(new Point((size.x - left - right) / 9, (size.y - top - bottom) / 8));
        buildingAdapter.notifyDataSetChanged();
    }

}
