package com.cnc.CnCTA.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.cnc.CnCTA.R;
import com.cnc.model.Server;

import java.util.ArrayList;

public class BaseAdapter extends ArrayAdapter<String> {


    private final int layoutResourceId;

    public BaseAdapter(Context context, int layoutResourceId, ArrayList<String> objects) {
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

        TextView name = (TextView) row.findViewById(R.id.base_name);
        TextView level = (TextView) row.findViewById(R.id.base_level);
        TextView baseCoordinates = (TextView) row.findViewById(R.id.base_coords);
        TextView buildingCond = (TextView) row.findViewById(R.id.buildings_cond_value);
        TextView defenceCond = (TextView) row.findViewById(R.id.defence_cond_value);

        ImageView status1 = (ImageView) row.findViewById(R.id.status_icon1);
        ImageView status2 = (ImageView) row.findViewById(R.id.status_icon2);
        ImageView status3 = (ImageView) row.findViewById(R.id.status_icon3);

        ImageView icon = (ImageView) row.findViewById(R.id.list_base_image);
        ImageView icon_potection = (ImageView) row.findViewById(R.id.list_base_protection_image);

        String base = getItem(position);
        name.setText(base);
        level.setText(((int) (Math.random() * 40)) + "");
        baseCoordinates.setText(((int) (Math.random() * 1000)) + ":" + (((int) (Math.random() * 1000)) + ""));
        buildingCond.setText(((int) (Math.random() * 100)) + "%");
        defenceCond.setText(((int) (Math.random() * 100)) + "%");

        int imageResource = context.getResources().getIdentifier("@drawable/n" + (((int) (Math.random() * 8))), null, context.getPackageName());
        icon.setImageResource(imageResource);
        imageResource = context.getResources().getIdentifier("@drawable/protection_n", null, context.getPackageName());
        icon_potection.setImageResource(imageResource);
        icon_potection.setVisibility(Math.random() > 0.6 ? View.GONE : View.VISIBLE);
        return row;
    }
}