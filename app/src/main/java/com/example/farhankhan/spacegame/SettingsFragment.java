package com.example.farhankhan.spacegame;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by FarhanKhan on 4/20/2017.
 */

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

}
