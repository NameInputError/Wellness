package com.tryan.wellness;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class FoodFragment extends Fragment {
	private TableLayout tbl;
	private NumberFormat f;
	private WellnessDB db;
	private StringBuilder sb;
	private Calendar cal;
	private String date;
	private SharedPreferences otherValues;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = new WellnessDB(getActivity());
        sb = new StringBuilder();
        cal = Calendar.getInstance();
		
        setHasOptionsMenu(true);
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.activity_summary, container, false);
		
		tbl = (TableLayout) view.findViewById(R.id.foodTable);
		buildFoods();
		return view;
		
	}
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		getActivity().getMenuInflater().inflate(R.menu.summary_menu, menu);
    }
	
	public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_return) {
        	getActivity().finish();
            return true;
        } 
        return super.onOptionsItemSelected(item);
    }

	
	@Override
	public void onResume() {
		super.onResume();
	}

	private void buildFoods() {
		int totcal = 0;
		otherValues = PreferenceManager.getDefaultSharedPreferences(getActivity()); //getBaseContext()
		date = otherValues.getString("date", "");
		
		TableRow tr;
		TextView itemView, calView;
		
		ArrayList<Food> foods = db.getFoods(date);
		
		for (Food f : foods) {
			tr = new TableRow(getActivity());
			itemView = new TextView(getActivity());
			itemView.setText(f.getItem());
			itemView.setGravity(Gravity.CENTER);
			tr.addView(itemView, new TableRow.LayoutParams(0, 
					TableRow.LayoutParams.WRAP_CONTENT, 1f));
			
			calView = new TextView(getActivity());
			calView.setText(String.valueOf(f.getCalories()));
			calView.setGravity(Gravity.CENTER);
			tr.addView(calView, new TableRow.LayoutParams(0, 
					TableRow.LayoutParams.WRAP_CONTENT, 1f));
			
			tbl.addView(tr);
			
			totcal += f.getCalories();
		}
		
		tr = new TableRow(getActivity());
		itemView = new TextView(getActivity());
		itemView.setText("Total Calories:");
		
		tr.addView(itemView, new TableRow.LayoutParams(0, 
				TableRow.LayoutParams.WRAP_CONTENT, 1f));
		
		calView = new TextView(getActivity());
		calView.setText(String.valueOf(totcal));
		calView.setGravity(Gravity.CENTER);
		tr.addView(calView, new TableRow.LayoutParams(0, 
				TableRow.LayoutParams.WRAP_CONTENT, 1f));
		
		tbl.addView(tr);
		
	}
}
