package com.tryan.wellness;

public class Food {
	
	private int eatID;
	private String eatDate;
	private String eatTime;
	private String eatItem;
	private int calories;
	private String eatGroup;
	
	
	//may need lighter constructors. book has an empty and one with 5/6 items
	public Food(String eatDate, String eatTime, String eatItem,
			int calories, String eatGroup) {
		this.eatDate = eatDate;
		this.eatTime = eatTime;
		this.eatItem = eatItem;
		this.calories = calories;
		this.eatGroup = eatGroup;
	}
	
	public Food(int eatID, String eatDate, String eatTime, String eatItem,
			int calories, String eatGroup) {
		this.eatID = eatID;
		this.eatDate = eatDate;
		this.eatTime = eatTime;
		this.eatItem = eatItem;
		this.calories = calories;
		this.eatGroup = eatGroup;
	}
	
	
	public int getId() {
		return eatID;
	}
	public void setId(int eatID) {
		this.eatID = eatID;
	}
	
	
	public String getDate() {
		return eatDate;
	}
	public void setDate(String eatDate) {
		this.eatDate = eatDate;
	}
	
	
	public String getTime() {
		return eatTime;
	}
	public void setTime(String eatTime) {
		this.eatTime = eatTime;
	}
	
	
	public String getItem() {
		return eatItem;
	}
	public void setItem(String eatItem) {
		this.eatItem = eatItem;
	}
	
	
	public int getCalories() {
		return calories;
	}
	public void calories(int calories) {
		this.calories = calories;
	}
	
	
	public String getGroup() {
		return eatGroup;
	}
	public void setGroup(String eatGroup) {
		this.eatGroup = eatGroup;
	}

}
