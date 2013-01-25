package com.cnc.CnCTA.fragment;

import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.cnc.CnCTA.R;
import com.cnc.CnCTA.adapter.BuildingAdapter;
import com.cnc.model.base.BuildingType;

import java.util.ArrayList;
import java.util.HashMap;

public class BaseFragment extends Fragment {

    private GridView buildingsGrid;
    private BuildingAdapter buildingAdapter;
    private ArrayList<String> buildings;
    private HashMap<BuildingType, Integer> buildingIconMap = new HashMap<BuildingType, Integer>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        buildingIconMap.put(BuildingType.CONSTRUCTION_YARD, R.drawable.base_construction_yard);
        buildingIconMap.put(BuildingType.ACCUMULATOR, R.drawable.base_accumulator);
        buildingIconMap.put(BuildingType.AIRFIELD, R.drawable.base_airfield);

        View view = inflater.inflate(R.layout.base, container, false);

        buildingsGrid = (GridView) view.findViewById(R.id.base_buildings);

        buildings = new ArrayList<String>();
        buildingAdapter = new BuildingAdapter(getActivity(), R.layout.base_building_cell, buildings);
        buildingsGrid.setAdapter(buildingAdapter);

        for (int i = 0; i < 8 * 9; i++) {
            buildings.add("");
        }

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
