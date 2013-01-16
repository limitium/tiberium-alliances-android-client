package com.cnc.CnCTA.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import com.cnc.CnCTA.R;

public class BaseFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base, container, false);


        GridView gridview = (GridView) view.findViewById(R.id.base_grid);
        String[] items = {"this", "is", "a", "really", "really2", "really3",
                "really4", "really5", "silly", "list"};

        ArrayAdapter<String> aa = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,
                items);

        gridview.setAdapter(aa);

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(R.id.main_container, new BasesFragment());
//                ft.commit();
//            }
//        });
        return view;
    }
}
