package com.cnc.CnCTA.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.cnc.CnCTA.R;

import java.util.ArrayList;

public class BuildingAdapter extends ArrayAdapter<String> {

    private final int layoutResourceId;
    private Point cellSize;

    public BuildingAdapter(Context context, int layoutResourceId, ArrayList<String> objects) {
        super(context, layoutResourceId, objects);
        this.layoutResourceId = layoutResourceId;
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
        if (Math.random() > 0.7) {
            bonusIcon.setImageResource(Math.random() > 0.5 ? R.drawable.bonus_pckg_1 : R.drawable.bonus_pckg_2);
        } else {
            bonusIcon.setVisibility(View.GONE);
        }

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