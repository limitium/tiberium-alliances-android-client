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
import com.cnc.CnCTA.helper.Formater;
import com.cnc.game.Client;
import com.cnc.model.base.Building;
import com.cnc.model.base.BuildingType;
import com.cnc.model.base.City;
import com.cnc.model.base.ResourceFieldType;
import com.cnc.model.resources.CityResourceType;

import java.util.*;

public class BaseFragment extends Fragment {

    private GridView buildingsGrid;
    private BuildingAdapter buildingAdapter;
    private ArrayList<Building> buildings = new ArrayList<Building>();
    private HashMap<ResourceFieldType, Integer> resourceFieldIconMap = new HashMap<ResourceFieldType, Integer>() {{
        put(ResourceFieldType.CRYSTAL, R.drawable.crystal_base);
        put(ResourceFieldType.TIBERIUM, R.drawable.tiberium_base2);
    }};
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
        put(BuildingType.COMMAND_CENTER, R.drawable.base_command_center);
    }};
    private final Client gameClient;
    private City city;

    public BaseFragment(Client gameClient, ErrorHandler errorHandler) {
        this.gameClient = gameClient;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.base, container, false);

        buildingsGrid = (GridView) view.findViewById(R.id.base_buildings);


        TextView name = (TextView) view.findViewById(R.id.base_name);
        TextView tiberium = (TextView) view.findViewById(R.id.base_tiberium_value);
        TextView crystal = (TextView) view.findViewById(R.id.base_crystal_value);
        TextView power = (TextView) view.findViewById(R.id.base_power_value);
        TextView credit = (TextView) view.findViewById(R.id.base_credit_value);
        TextView level = (TextView) view.findViewById(R.id.base_level);
        TextView buildingLimit = (TextView) view.findViewById(R.id.building_limit);

        name.setText(city.getName());
        tiberium.setText(String.valueOf(city.getResource(CityResourceType.TIBERIUM).getValue()));
        crystal.setText(String.valueOf(city.getResource(CityResourceType.CRYSTAL).getValue()));
        power.setText(String.valueOf(city.getResource(CityResourceType.POWER).getValue()));
        credit.setText(String.valueOf(city.getResource(CityResourceType.CREDITS).getValue()));
        level.setText(Formater.number(city.getLevel()));
        buildingLimit.setText(city.getBuildings().size() - 1 + "/" + city.getBuildingLimit());

        buildings.clear();
        buildings.addAll(Arrays.asList(new Building[9 * 8]));

        for (Map.Entry<Long, Building> entry : city.getBuildings().entrySet()) {
            Building building = entry.getValue();
            buildings.set((int) (building.getX() + building.getY() * 9), building);
        }

        buildingAdapter = new BuildingAdapter(getActivity(), R.layout.base_building_cell, city, buildings, buildingIconMap, resourceFieldIconMap);
        buildingsGrid.setAdapter(buildingAdapter);
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

    public void setCity(City city) {
        this.city = city;
    }
}
