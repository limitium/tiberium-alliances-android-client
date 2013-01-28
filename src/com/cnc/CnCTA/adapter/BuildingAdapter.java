package com.cnc.CnCTA.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.cnc.CnCTA.R;
import com.cnc.model.Server;
import com.cnc.model.base.Building;
import com.cnc.model.base.BuildingType;

import java.util.ArrayList;
import java.util.HashMap;

public class BuildingAdapter extends ArrayAdapter<Building> {

    private final int layoutResourceId;
    private Point cellSize;
    private final HashMap<BuildingType, Integer> buildingIconMap;

    public BuildingAdapter(Context context, int layoutResourceId, ArrayList<Building> buildings, HashMap<BuildingType, Integer> buildingIconMap) {
        super(context, layoutResourceId, buildings);
        this.layoutResourceId = layoutResourceId;
        this.buildingIconMap = buildingIconMap;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Context context = getContext();
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
        }


        ImageView icon = (ImageView) row.findViewById(R.id.building_image);
        TextView level = (TextView) row.findViewById(R.id.building_level);
        ImageView bonusIcon = (ImageView) row.findViewById(R.id.building_bonus_icon);

        Building building = getItem(position);
        if (building != null) {
            level.setText(String.valueOf(building.getLevel()));
            icon.setImageResource(buildingIconMap.get(building.getType()));
            bonusIcon.setVisibility(View.GONE);
        } else {
            bonusIcon.setVisibility(View.GONE);
            icon.setVisibility(View.GONE);
            level.setVisibility(View.GONE);
        }

//        if (Math.random() > 0.7) {
//            bonusIcon.setImageResource(Math.random() > 0.5 ? R.drawable.bonus_pckg_1 : R.drawable.bonus_pckg_2);
//        } else {
//            bonusIcon.setVisibility(View.GONE);
//        }

        calculateLayout(row, level);

        return row;
    }

    private void calculateLayout(View row, TextView level) {
        LinearLayout cell = (LinearLayout) row.findViewById(R.id.building_cell);
        cell.setLayoutParams(new AbsListView.LayoutParams(cellSize.x, cellSize.y));
        RelativeLayout bonus = (RelativeLayout) row.findViewById(R.id.building_bonus);
        ViewGroup.MarginLayoutParams bonusParams = new ViewGroup.MarginLayoutParams(cellSize.x / 2, cellSize.y / 4);
        bonusParams.setMargins(cellSize.x / 4, (int) (cellSize.y / 2.5), 0, 0);
        bonus.setLayoutParams(new RelativeLayout.LayoutParams(bonusParams));
        level.setTextSize((float) (Math.hypot(cellSize.x, cellSize.y) / 13));
    }

    public void setCellSize(Point cellSize) {
        this.cellSize = cellSize;
    }
}