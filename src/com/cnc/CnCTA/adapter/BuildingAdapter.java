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
        int measuredWidth = cell.getMeasuredWidth();
        int width = cell.getWidth();
        int width2 = cell.getVerticalScrollbarWidth();
        int width3 = cell.getMeasuredWidthAndState();
        cell.setLayoutParams(new AbsListView.LayoutParams(cellSize.x, cellSize.y));
//        level.setText(((int) (Math.random() * 40)) + "");
//        int imageResource = context.getResources().getIdentifier("@drawable/n" + (((int) (Math.random() * 8))), null, context.getPackageName());
//        int imageResource = context.getResources().getIdentifier("@drawable/base_silo", null, context.getPackageName());
//        icon.setImageResource(imageResource);
//        imageResource = context.getResources().getIdentifier("@drawable/protection_n", null, context.getPackageName());
//        icon_potection.setImageResource(imageResource);
//        icon_potection.setVisibility(Math.random() > 0.6 ? View.GONE : View.VISIBLE);

        return row;
    }
}