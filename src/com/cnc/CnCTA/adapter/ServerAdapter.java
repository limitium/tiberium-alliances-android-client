package com.cnc.CnCTA.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
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
import java.util.Date;

public class ServerAdapter extends ArrayAdapter<Server> {


    private final int layoutResourceId;

    public ServerAdapter(Context context, int layoutResourceId, ArrayList<Server> objects) {
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

        TextView name = (TextView) row.findViewById(R.id.server_name); // name
        TextView isOnline = (TextView) row.findViewById(R.id.server_is_online); // isOnline name
        TextView lastSeen = (TextView) row.findViewById(R.id.server_last_seen); // lastSeen
        ImageView fraction = (ImageView) row.findViewById(R.id.server_image); // thumb image


        Server server = getItem(position);

        name.setText(server.getName());
        isOnline.setText(server.isOnline() ? context.getString(R.string.offline) : context.getString(R.string.online));

        lastSeen.setText(DateUtils.getRelativeTimeSpanString(server.getLastSeen(), System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS));
        fraction.setImageResource(server.getFaction() == 1 ? R.drawable.icon_gdi : R.drawable.icon_nod);
        return row;
    }
}