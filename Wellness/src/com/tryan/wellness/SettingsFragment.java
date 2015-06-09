package com.tryan.wellness;

import android.preference.PreferenceFragment;
import android.os.Bundle;

public class SettingsFragment extends PreferenceFragment {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.preferences);
	}
}
