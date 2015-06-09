package com.tryan.wellness;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class FoodService extends Service {
	private Timer timer;
	private NotificationManager notificationManager;
	private final int NOTIFICATION_ID = 1;
	private WellnessDB db;
	private int totcal;
	private SharedPreferences foodprefs;
	
	@Override
	public void onCreate() {
		Log.d("MOD", "MOD Service Created");
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("MOD", "MOD Service started.");
		startTimer();
		return START_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void onDestroy() {
		stopTimer();
	}
	
	private void startTimer() {
		foodprefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				totcal = 0;
				
				Intent notificationIntent = 
						new Intent(getApplicationContext(), WellnessActivity.class);
				notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				
				int flags = PendingIntent.FLAG_UPDATE_CURRENT;
				PendingIntent pendingIntent = 
				PendingIntent.getActivity(getApplicationContext(), 0, 
						notificationIntent, flags);
				
				db = new WellnessDB(getBaseContext());
				
				Date date = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("M/d");
				String today = dateFormat.format(date);
				int icon = R.drawable.ic_launcher;
				ArrayList<Food> foods = db.getFoods(today);
				for (Food f : foods) {
					totcal += f.getCalories();
					
				}
				
				int calTarget = Integer.parseInt(foodprefs.getString("calorie_target", "0"));
				String cal_remaining = String.valueOf(calTarget - totcal);
				String message = "You have " + cal_remaining + " calories left until you have reached your target.";
				
				CharSequence tickerText = getText(R.string.app_name);
				CharSequence contentTitle = getText(R.string.app_name);
				CharSequence contentText = message; 
				
				Notification notification = 
						new NotificationCompat.Builder(getApplicationContext())
				.setSmallIcon(icon)
				.setTicker(tickerText)
				.setContentTitle(contentTitle)
				.setContentText(contentText)
				.setContentIntent(pendingIntent)
				.build(); 
			notification.flags = Notification.FLAG_AUTO_CANCEL;
				
			notificationManager.notify(NOTIFICATION_ID, notification);
			}
		};
		timer = new Timer(true);
		int delay = 0;
		int interval = 500 * 60;
		timer.schedule(task, delay, interval);
	} 
	
	private void stopTimer() {
		if (timer != null) {
			timer.cancel();
			notificationManager.cancel(NOTIFICATION_ID);
		}
	}
	

}
