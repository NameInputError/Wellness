package com.tryan.wellness;

import java.util.Date;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;


public class WellnessActivity extends Activity
		implements OnClickListener, OnEditorActionListener,
			OnItemSelectedListener, OnTimeChangedListener {
	
	private EditText date;
	private TimePicker tm;
	private EditText eaten;
	private EditText calories;
	private Spinner foodGroupSpinner;
	private Button saveButton;
	private int foodpref = 0;
	private int foodpreftype = 0;
	private SharedPreferences food_prefs;
	private int cal_target;
	private WellnessDB db;
	private TableLayout tbl;
	private NumberFormat f;
	private Calendar cal;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		
        setContentView(R.layout.activity_wellness);
        date = (EditText) findViewById(R.id.dateEditText);
        tm = (TimePicker) findViewById(R.id.timeTimePicker);
        eaten = (EditText) findViewById(R.id.eatenEditText);
        calories = (EditText) findViewById(R.id.caloriesEditText);
        foodGroupSpinner = (Spinner) findViewById(R.id.foodGroupSpinner);
        saveButton = (Button) findViewById(R.id.saveButton);
        db = new WellnessDB(this);
		cal = Calendar.getInstance();
		saveButton.setOnClickListener(this);
        
        food_prefs = PreferenceManager.getDefaultSharedPreferences(this);
        
        setSpinner();
        
    }
    
    
    @Override
    public void onPause() {
    	super.onPause();
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	foodpref = Integer.parseInt(food_prefs.getString("pref_foodpref", "0"));
    	cal_target = Integer.parseInt(food_prefs.getString("calorie_target", "0"));
    	setSpinner();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.wellness, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
        	startActivity(new Intent(getApplicationContext(),
					SettingsActivity.class));
            return true;
        } else if (id == R.id.menu_foodsummary) {
        	Editor ed = food_prefs.edit();
        	
        	Date inputDate = new Date();
    		SimpleDateFormat dateFormat = new SimpleDateFormat("M/d");
    		try {
    			inputDate = dateFormat.parse(date.getText().toString());
    		} catch (Exception e) {
    			Toast.makeText(this, "Date format: MM/DD", Toast.LENGTH_LONG).show();
    			return false;
    		}
    		
    		String dateEaten = dateFormat.format(inputDate);
        	ed.putString("date", dateEaten);
        	ed.commit();
        	startActivity(new Intent(getApplicationContext(),
					SummaryActivity.class));
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    
	@Override
	public void onClick(View arg0) {
		String dateEaten = "";
		int foodCalories = 0;
		String foodEaten = "";
		
		Date inputDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("M/d");
		try {
			inputDate = dateFormat.parse(date.getText().toString());
			dateEaten = dateFormat.format(inputDate);
		} catch (Exception e) {
			Toast.makeText(this, "Date format: MM/DD", Toast.LENGTH_LONG).show();
			return;
		}
		
		int hours = tm.getCurrentHour();
		int minutes = tm.getCurrentMinute();
		String timeString = String.valueOf(hours) + ":" + String.valueOf(minutes);
		
		try {
			foodEaten = eaten.getText().toString();
		} catch (Exception e) {
			Toast.makeText(this, "You must a food name.", Toast.LENGTH_LONG).show();
			return;
		}
		
		try {
			foodCalories = Integer.parseInt(calories.getText().toString());
		} catch (Exception e) {
			Toast.makeText(this, "You must a calorie amount.", Toast.LENGTH_LONG).show();
			return;
		}
		String foodGroup = foodGroupSpinner.getSelectedItem().toString();
		Food food = new Food(dateEaten, timeString, foodEaten, foodCalories, foodGroup);
		db.insertFood(food);
		Toast.makeText(this, "Food inserted.", Toast.LENGTH_LONG).show();
		Intent serviceIntent = new Intent(this, FoodService.class);
        startService(serviceIntent);
		
	}


	@Override
	public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onTimeChanged(TimePicker arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	
	private void setSpinner() {
		ArrayAdapter<CharSequence> adapter;
        		
		switch (foodpref) {
		 	case 1:
		 		adapter = 
	 			ArrayAdapter.createFromResource(this, R.array.foodvegetarian,
        				android.R.layout.simple_spinner_item);
	 			break;
		 	case 2:
		 		adapter = 
	 			ArrayAdapter.createFromResource(this, R.array.foodvegan,
        				android.R.layout.simple_spinner_item);
		 		break;
		 	default:
		 		adapter = 
		 			ArrayAdapter.createFromResource(this, R.array.foodgroups,
	        				android.R.layout.simple_spinner_item);
		 			break;
		}
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		foodGroupSpinner.setAdapter(adapter);
	}
}