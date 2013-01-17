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
    private final Point cellSize;

    public BuildingAdapter(Context context, int layoutResourceId, Point cellSize, ArrayList<String> objects) {
        super(context, layoutResourceId, objects);
        this.layoutResourceId = layoutResourceId;
        this.cellSize = cellSize;
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
        LinearLayout cell = (LinearLayout) row.findViewById(R.id.building_cell);
        cell.setLayoutParams(new AbsListView.LayoutParams(cellSize.x, cellSize.y));
        return row;
    }
}