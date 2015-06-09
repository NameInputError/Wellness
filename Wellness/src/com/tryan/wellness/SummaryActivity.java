package com.tryan.wellness;

import android.app.Activity;
import android.os.Bundle;

public class SummaryActivity extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getFragmentManager().beginTransaction().replace(android.R.id.content, 
	    		new FoodFragment()).commit();
	}
}
