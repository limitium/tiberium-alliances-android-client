package com.cnc.CnCTA;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FragAcivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.main_container, new BasesFragment());
        ft.commit();
    }
}
