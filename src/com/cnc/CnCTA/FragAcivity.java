package com.cnc.CnCTA;

import android.content.res.Configuration;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import com.cnc.CnCTA.fragment.BaseFragment;

public class FragAcivity extends FragmentActivity {

    private BaseFragment baseFragment;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        baseFragment = new BaseFragment();
//        ft.replace(R.id.main_container, baseFragment);
//        ft.commit();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        baseFragment.updateGridMargins();
    }
}
