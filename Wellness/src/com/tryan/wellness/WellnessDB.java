package com.tryan.wellness;


import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class WellnessDB {
	
	public static final String DB_NAME = "wellness.db";
	public static final int    DB_VERSION = 1;
	
	public static final String EATEN_TABLE = "Eaten";
	
	public static final String EATEN_ID = "EatID";
	public static final int    EATEN_ID_COL = 0;
	
	public static final String EATEN_DATE = "EatDt";
	public static final int    EATEN_DATE_COL = 1;
	
	public static final String EATEN_TIME = "EatTm";
	public static final int    EATEN_TIME_COL = 2;
	
	public static final String EATEN_ITEM = "EatItem";
	public static final int    EATEN_ITEM_COL = 3;
	
	public static final	String EATEN_CAL = "EatCal";
	public static final int    EATEN_CAL_COL = 4;
	
	public static final String EATEN_GROUP = "EatGroup";
	public static final int    EATEN_GROUP_COL = 5;
	
	private SQLiteDatabase db;
	private DBHelper dbHelper;
	
	public static final String CREATE_EATEN_TABLE =
		"CREATE TABLE " + EATEN_TABLE + " (" +
		EATEN_ID     + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
		EATEN_DATE   + " TEXT NOT NULL, " +
		EATEN_TIME   + " TEXT NOT NULL, " +
		EATEN_ITEM   + " TEXT, " +
		EATEN_CAL    + " INTEGER, " +
		EATEN_GROUP  + " TEXT);";
	
	public static final String DROP_TABLE_EATEN = 
		"DROP TABLE IF EXISTS " + EATEN_TABLE;
	
	public WellnessDB(Context context) {
		dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
	}
	
	private void openReadableDB() {
		db = dbHelper.getReadableDatabase();
	}
	
	private void openWritableDB() {
		db = dbHelper.getWritableDatabase();
	}
	
	private void closeDB() {
		if (db != null) {
			db.close();
		}
	}
	
	public ArrayList<Food> getFoods(String eatDate) {
		String where;
		String[] whereArgs;
		if (eatDate.isEmpty()) {
			where = null;
			whereArgs = null;
		} else {
			where = EATEN_DATE + "= ? ";
			whereArgs = new String[] { eatDate };
		}
		
		this.openReadableDB();
		Cursor cursor = db.query(EATEN_TABLE, null, where, whereArgs, 
				null, null, null);
		ArrayList<Food> foods = new ArrayList<Food>();
		while (cursor.moveToNext()) {
			foods.add(getFoodFromCursor(cursor));
		}
		if (cursor != null) {
			cursor.close();
			this.closeDB();
		}
		return foods;
	}
	
	public Food getFood(int id) {
		String where = EATEN_ID + "= ?";
		String[] whereArgs = { Integer.toString(id) };
		
		this.openReadableDB();
		Cursor cursor = db.query(EATEN_TABLE,null,where,whereArgs,null,null,null);
		cursor.moveToFirst();
		Food food = getFoodFromCursor(cursor);
		if (cursor != null) {
			cursor.close();
		}
		this.closeDB();
		
		return food;
		
	}
	
	public static Food getFoodFromCursor(Cursor cursor) {
		if (cursor == null || cursor.getCount() == 0) {
			return null;
		} else {
			try {
				Food food = new Food(
					cursor.getInt(EATEN_ID_COL),
					cursor.getString(EATEN_DATE_COL),
					cursor.getString(EATEN_TIME_COL),
					cursor.getString(EATEN_ITEM_COL),
					cursor.getInt(EATEN_CAL_COL),
					cursor.getString(EATEN_GROUP_COL));
				return food;
				} catch (Exception e) {
					return null;
				}
		}
		
		
	}
	
	public long insertFood(Food food) {
		ContentValues cv = new ContentValues();
		cv.put(EATEN_DATE, food.getDate());
		cv.put(EATEN_TIME, food.getTime());
		cv.put(EATEN_ITEM, food.getItem());
		cv.put(EATEN_CAL, food.getCalories());
		cv.put(EATEN_GROUP, food.getGroup());
		
		this.openWritableDB();
		long rowID = db.insert(EATEN_TABLE, null, cv);
		this.closeDB();
		
		return rowID;
	}
	
	public long updateFood(Food food) {
		ContentValues cv = new ContentValues();
		cv.put(EATEN_DATE, food.getDate());
		cv.put(EATEN_TIME, food.getTime());
		cv.put(EATEN_ITEM, food.getItem());
		cv.put(EATEN_CAL, food.getCalories());
		cv.put(EATEN_GROUP, food.getGroup());
		
		String where = EATEN_ID + "= ?";
		String[] whereArgs = { String.valueOf(food.getId()) };
		
		this.openWritableDB();
		long rowCount = db.update(EATEN_TABLE, cv, where, whereArgs);
		this.closeDB();
		
		return rowCount;
	}
	
	public int deleteTask(long id) {
		String where = EATEN_ID + "= ?";
		String[] whereArgs = { String.valueOf(id) };
		
		this.openWritableDB();
		int rowCount = db.delete(EATEN_TABLE, where, whereArgs);
		this.closeDB();
		
		return rowCount;
	}
	
	public static class DBHelper extends SQLiteOpenHelper {
		
		public DBHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
		}
		
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_EATEN_TABLE);
		}
		
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(WellnessDB.DROP_TABLE_EATEN);
			onCreate(db);
			
		}
	}
}
