package com.cnc.CnCTA.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.cnc.CnCTA.R;
import com.cnc.CnCTA.adapter.BaseAdapter;

import java.util.ArrayList;

public class BasesFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bases, container, false);


        ArrayList<String> bases = new ArrayList<String>();
        bases.add("qwe");
        bases.add("asd");
        bases.add("zx");
        bases.add("zxc");
        bases.add("23rfwef");
        bases.add("fsdfxv");
        bases.add("cv");
        bases.add("sdf");
        bases.add("xcv");
        bases.add("wer");
        bases.add("xcvxcv");


        ListView serversList = (ListView) view.findViewById(R.id.listBases);

        BaseAdapter adapter = new BaseAdapter(getActivity(), R.layout.base_list_row, bases);
        serversList.setAdapter(adapter);

        adapter.notifyDataSetChanged();
        return view;
    }
}
