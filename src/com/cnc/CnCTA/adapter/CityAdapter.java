package com.cnc.CnCTA.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.cnc.CnCTA.ClientActivity;
import com.cnc.CnCTA.R;
import com.cnc.model.Player;
import com.cnc.model.base.City;

import java.util.ArrayList;

public class CityAdapter extends ArrayAdapter<City> {


    private final int layoutResourceId;
    private final Player player;
    private final ClientActivity activity;

    public CityAdapter(Player player, ClientActivity activity, int layoutResourceId, ArrayList<City> objects) {
        super(activity, layoutResourceId, objects);
        this.layoutResourceId = layoutResourceId;
        this.player = player;
        this.activity = activity;
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

        ImageView icon_base = (ImageView) row.findViewById(R.id.list_base_image);
        ImageView icon_protection = (ImageView) row.findViewById(R.id.list_base_protection_image);

        City city = getItem(position);
        name.setText(city.getName());
        level.setText(String.valueOf((int) city.getLevel()));
        baseCoordinates.setText(city.getX() + ":" + city.getY());
        buildingCond.setText("100%");
        defenceCond.setText("100%");

        activity.loadImage(icon_base, player.getFraction() + city.getTier());
        activity.loadImage(icon_protection, "protection_" + player.getFraction());
        icon_protection.setVisibility(View.GONE);
        return row;
    }
}