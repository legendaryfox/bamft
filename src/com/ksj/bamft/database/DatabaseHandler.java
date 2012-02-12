package com.ksj.bamft.database;

import java.util.ArrayList;
import java.util.List;

import com.ksj.bamft.model.Factlet;
import com.ksj.bamft.model.FoodItem;
import com.ksj.bamft.model.Landmark;
import com.ksj.bamft.model.Schedule;
import com.ksj.bamft.model.Truck;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
 
public class DatabaseHandler extends SQLiteOpenHelper {
 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "bamftRecords";
 
    // Tables name
    private static final String TABLE_LANDMARKS = "landmarks";
    private static final String TABLE_TRUCKS = "trucks";
    private static final String TABLE_SCHEDULES = "schedules";
    private static final String TABLE_FOOD_ITEMS = "food_items";
    private static final String TABLE_FACTLETS = "factlets";
 

    // Table column names
    private static final String KEY_LANDMARK_ID = "id";
    private static final String KEY_LANDMARK_NAME = "name";
    private static final String KEY_LANDMARK_XCOORD = "xcoord";
    private static final String KEY_LANDMARK_YCOORD = "ycoord";

    private static final String KEY_TRUCK_ID = "id";
    private static final String KEY_TRUCK_NAME = "name";
    private static final String KEY_TRUCK_CUISINE = "cuisine";
    private static final String KEY_TRUCK_DESCRIPTION = "description";
    private static final String KEY_TRUCK_EMAIL = "email";
    private static final String KEY_TRUCK_MENU = "menu";
    private static final String KEY_TRUCK_TWITTER = "twitter";
    private static final String KEY_TRUCK_FACEBOOK = "facebook";
    private static final String KEY_TRUCK_WEBSITE = "website";
    private static final String KEY_TRUCK_YELP = "yelp";
    
    private static final String KEY_SCHEDULE_ID = "id";
    private static final String KEY_SCHEDULE_DAY_OF_WEEK = "day_of_week";
    private static final String KEY_SCHEDULE_TIME_OF_DAY = "time_of_day";
    private static final String KEY_SCHEDULE_TRUCK_ID = "truck_id";
    private static final String KEY_SCHEDULE_LANDMARK_ID = "landmark_id";
    
    private static final String KEY_FOOD_ITEM_ID = "id";
    private static final String KEY_FOOD_ITEM_NAME = "name";
    private static final String KEY_FOOD_ITEM_DESCRIPTION = "description";
    private static final String KEY_FOOD_ITEM_PRICE = "price";
    private static final String KEY_FOOD_ITEM_TRUCK_ID = "truck_id";
    
    private static final String KEY_FACTLET_ID = "id";
    private static final String KEY_FACTLET_TITLE = "title";
    private static final String KEY_FACTLET_CONTENT = "content";
    private static final String KEY_FACTLET_TRUCK_ID = "truck_id";
    
    
    private static final String CREATE_LANDMARKS_TABLE = "CREATE TABLE " + TABLE_LANDMARKS
			+ "(" 
			+ KEY_LANDMARK_ID + " INTEGER PRIMARY KEY,"
			+ KEY_LANDMARK_NAME + " TEXT,"
			+ KEY_LANDMARK_XCOORD + " TEXT,"
			+ KEY_LANDMARK_YCOORD + " TEXT"
			+ ")";
	
    private static final String CREATE_TRUCKS_TABLE = "CREATE TABLE " + TABLE_TRUCKS
			+ "(" 
			+ KEY_TRUCK_ID + " INTEGER PRIMARY KEY,"
			+ KEY_TRUCK_NAME + " TEXT,"
			+ KEY_TRUCK_CUISINE + " TEXT,"
			+ KEY_TRUCK_DESCRIPTION + " TEXT,"
			+ KEY_TRUCK_EMAIL + " TEXT,"
			+ KEY_TRUCK_MENU + " TEXT,"
			+ KEY_TRUCK_TWITTER + " TEXT,"
			+ KEY_TRUCK_FACEBOOK + " TEXT,"
			+ KEY_TRUCK_WEBSITE + " TEXT,"
			+ KEY_TRUCK_YELP + " TEXT"
			+ ")";
	
    private static final String CREATE_SCHEDULES_TABLE = "CREATE TABLE " + TABLE_SCHEDULES
			+ "("
			+ KEY_SCHEDULE_ID + " INTEGER PRIMARY KEY,"
			+ KEY_SCHEDULE_DAY_OF_WEEK + " TEXT,"
			+ KEY_SCHEDULE_TIME_OF_DAY + " TEXT,"
			+ KEY_SCHEDULE_TRUCK_ID + " TEXT,"
			+ KEY_SCHEDULE_LANDMARK_ID + " TEXT"
			+ ")";
    
    private static final String CREATE_FOOD_ITEMS_TABLE = "CREATE TABLE " + TABLE_FOOD_ITEMS
    		+ "("
    		+ KEY_FOOD_ITEM_ID + " INTEGER PRIMARY KEY,"
    		+ KEY_FOOD_ITEM_NAME + " TEXT,"
    		+ KEY_FOOD_ITEM_DESCRIPTION + " TEXT,"
    		+ KEY_FOOD_ITEM_PRICE + " TEXT,"
    		+ KEY_FOOD_ITEM_TRUCK_ID + " TEXT"
    		+ ")";
    
    private static final String CREATE_FACTLETS_TABLE = "CREATE TABLE " + TABLE_FACTLETS
    		+ "("
    		+ KEY_FACTLET_ID + " INTEGER PRIMARY KEY,"
    		+ KEY_FACTLET_TITLE + " TEXT,"
    		+ KEY_FACTLET_CONTENT + " TEXT,"
    		+ KEY_FACTLET_TRUCK_ID + " TEXT"
    		+ ")";
    
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        
    	
    	db.execSQL(CREATE_LANDMARKS_TABLE);
    	db.execSQL(CREATE_TRUCKS_TABLE);
    	db.execSQL(CREATE_SCHEDULES_TABLE);
    	db.execSQL(CREATE_FOOD_ITEMS_TABLE);
    	db.execSQL(CREATE_FACTLETS_TABLE);
    	
    	//db.close();
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LANDMARKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRUCKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FACTLETS);
 
        // Create tables again
        onCreate(db);
    }
    
    public void recreateTable(String tableName) {
    	 	
    	SQLiteDatabase db = this.getWritableDatabase();

    	if (TABLE_LANDMARKS == tableName) {
    		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LANDMARKS);
    		db.execSQL(CREATE_LANDMARKS_TABLE);
    		Log.d("RECREATE", "Recreating Landmark table");
    		
    	} else if (TABLE_TRUCKS == tableName) {
    		
    		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRUCKS);
    		db.execSQL(CREATE_TRUCKS_TABLE);
    		Log.d("RECREATE", "Recreating Trucks table: " + CREATE_TRUCKS_TABLE);
    		
    	} else if (TABLE_SCHEDULES == tableName) {
    		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULES);
    		db.execSQL(CREATE_SCHEDULES_TABLE);
    		Log.d("RECREATE", "Recreating Schedules table");
    		
    	} else if (TABLE_FOOD_ITEMS == tableName) {
    		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD_ITEMS);
    		db.execSQL(CREATE_FOOD_ITEMS_TABLE);
    		Log.d("RECREATE", "Recreating FoodItems table");
    		
    	} else if (TABLE_FACTLETS == tableName) {
    		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FACTLETS);
    		db.execSQL(CREATE_FACTLETS_TABLE);
    		Log.d("RECREATE", "Recreating Factlets table");
    	} else {
    		//error...
    	}
    	
    	
    }
    
    public void recreateAllTables() {
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	db.execSQL("DROP TABLE IF EXISTS " + TABLE_LANDMARKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRUCKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FACTLETS);
 
        // Create tables again
        onCreate(db);
    }
    
    /*
     * Begin custom functions
     */
    
    /**
     * Gets all schedules based upon a given time and day (use string format, ie "Monday", "Evening");
     * Days: "Monday" to "Sunday";
     * Time: "Morning", "Afternoon", "Evening"
     * @param dayOfWeek
     * @param timeOfDay
     * @return List<Schedule> of schedules matching parameters
     */
    public List<Schedule> getSchedulesByDayAndTime(String dayOfWeek, String timeOfDay) {
    	String query = "SELECT \"schedules\".* FROM \"" + TABLE_SCHEDULES + "\""
    			+ "WHERE (\"schedules\".\"day_of_week\" = '" + dayOfWeek + "')"
    			+ "AND (\"schedules\".\"time_of_day\" = '" + timeOfDay + "')";
    	
    	return getSchedulesListByQuery(query);
    }
    /**
     * Gets all schedules given a truck
     * @param truck
     * @return
     */
    public List<Schedule> getSchedulesByTruck(Truck truck) {
    	String query = 
    			"SELECT \"schedules\".* " +
    			"FROM \"" + TABLE_SCHEDULES + "\"" + 
    			"WHERE (\"schedules\".\"truck_id\" = '" + truck.getId() + "')";
    	
    	return getSchedulesListByQuery(query);
    }
    
    /**
     * Get all foodItems given a truck.
     * @param truck
     * @return
     */
    public List<FoodItem> getFoodItemsByTruck(Truck truck) {
    	String query = 
    			"SELECT \"food_items\".* " +
    			"FROM \"" + TABLE_FOOD_ITEMS + "\"" +
    			"WHERE (\"food_items\".\"truck_id\" = '" + truck.getId() + "')";
    	return getFoodItemsListByQuery(query);
    }
    
    /**
     * Gets all schedules by a given truck, day, and time.
     * @param truck
     * @param dayOfWeek
     * @param timeOfDay
     * @return
     */
    public Schedule getScheduleByTruckAndDayAndTime(Truck truck, String dayOfWeek, String timeOfDay) {
    	
    	SQLiteDatabase db = this.getReadableDatabase();
    	
    	/*
    	Cursor cursor = db.query(TABLE_SCHEDULES, 
    			new String[] { KEY_SCHEDULE_ID, KEY_SCHEDULE_DAY_OF_WEEK, KEY_SCHEDULE_TIME_OF_DAY, KEY_SCHEDULE_TRUCK_ID, KEY_SCHEDULE_LANDMARK_ID }, 
    			KEY_SCHEDULE_TRUCK_ID + "=? AND " + KEY_SCHEDULE_DAY_OF_WEEK + "=? AND " + KEY_SCHEDULE_TIME_OF_DAY + "=?",
    			new String[] { String.valueOf(truck.getId()), dayOfWeek, timeOfDay },
    			null,
    			null,
    			null,
    			null);
    	*/
    	String query = "SELECT \"schedules\".* FROM \"" + TABLE_SCHEDULES + "\""
    			+ "WHERE (\"schedules\".\"day_of_week\" = '" + dayOfWeek + "')"
    			+ "AND (\"schedules\".\"time_of_day\" = '" + timeOfDay + "')"
    			+ "AND (\"schedules\".\"truck_id\" = '" + truck.getId() + "')";
    	
    	Cursor cursor = db.rawQuery(query, null);
    	if (cursor != null)
    		cursor.moveToFirst();
    	if (cursor.getCount() > 0) {
    		Schedule schedule = new Schedule(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)));
    		db.close();
    		return schedule;
    	} else {
    		db.close();
    		return null;
    	}
    	
    }
    
    
    
   
    
    
    /*
     * End custom functions
     */
    
    
    /*
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
    public void addLandmark(Landmark landmark) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	ContentValues values = new ContentValues();
    	values.put(KEY_LANDMARK_ID, landmark.getId()); // Landmark ID
    	values.put(KEY_LANDMARK_NAME, landmark.getName()); // Landmark Name
    	values.put(KEY_LANDMARK_XCOORD, landmark.getXcoord()); // Landmark Xcoord
    	values.put(KEY_LANDMARK_YCOORD, landmark.getYcoord()); // Landmark Ycoord
    	
    	// Insert row
    	db.insert(TABLE_LANDMARKS, null, values);
    	db.close();
    }
    
    public void addTruck(Truck truck) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	 /*
	     * private String email;
		private String menu;
		private String twitter;
		private String facebook;
		private String website;
		private String yelp;
	     */
    	
    	ContentValues values = new ContentValues();
    	values.put(KEY_TRUCK_ID, truck.getId()); // Truck ID
    	values.put(KEY_TRUCK_NAME, truck.getName()); // Truck Name
    	values.put(KEY_TRUCK_CUISINE, truck.getCuisine()); // Truck cuisine
    	values.put(KEY_TRUCK_DESCRIPTION, truck.getDescription()); // Truck description
    	values.put(KEY_TRUCK_EMAIL, truck.getEmail());
    	values.put(KEY_TRUCK_MENU, truck.getMenu());
    	values.put(KEY_TRUCK_TWITTER, truck.getTwitter());
    	values.put(KEY_TRUCK_FACEBOOK, truck.getFacebook());
    	values.put(KEY_TRUCK_WEBSITE, truck.getWebsite());
    	values.put(KEY_TRUCK_YELP, truck.getYelp());
    	
    	// Insert row
    	db.insert(TABLE_TRUCKS, null, values);
    	db.close();
    }
    
    public void addSchedule(Schedule schedule) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	ContentValues values = new ContentValues();
    	values.put(KEY_SCHEDULE_ID, schedule.getId()); // Schedule ID
    	values.put(KEY_SCHEDULE_DAY_OF_WEEK, schedule.getDayOfWeek()); // Schedule Day of Week
    	values.put(KEY_SCHEDULE_TIME_OF_DAY, schedule.getTimeOfDay()); // Schedule Time of Day
    	values.put(KEY_SCHEDULE_TRUCK_ID, schedule.getTruckId()); // Schedule truck_id
    	values.put(KEY_SCHEDULE_LANDMARK_ID, schedule.getLandmarkId()); // Schedule landmark_id
    	
    	
    	// Insert row
    	db.insert(TABLE_SCHEDULES, null, values);
    	db.close();
    }
    
    public void addFoodItem(FoodItem foodItem) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	ContentValues values = new ContentValues();
    	values.put(KEY_FOOD_ITEM_ID, foodItem.getId());
    	values.put(KEY_FOOD_ITEM_NAME, foodItem.getName());
    	values.put(KEY_FOOD_ITEM_DESCRIPTION, foodItem.getDescription());
    	values.put(KEY_FOOD_ITEM_PRICE, foodItem.getPrice());
    	values.put(KEY_FOOD_ITEM_TRUCK_ID, foodItem.getTruckId());
    	
    	// Insert row
    	db.insert(TABLE_FOOD_ITEMS, null, values);
    	db.close();
    	
    }
    
    public void addFactlet(Factlet factlet) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	ContentValues values = new ContentValues();
    	values.put(KEY_FACTLET_ID, factlet.getId());
    	values.put(KEY_FACTLET_TITLE, factlet.getTitle());
    	values.put(KEY_FACTLET_CONTENT, factlet.getContent());
    	values.put(KEY_FACTLET_TRUCK_ID, factlet.getTruckId());
    	
    	// Insert row
    	db.insert(TABLE_FACTLETS, null, values);
    	db.close();
    	
    }
    
    
    
    public Landmark getLandmark(int id) {
    	SQLiteDatabase db = this.getReadableDatabase();
    	
    	Cursor cursor = db.query(TABLE_LANDMARKS, 
    			new String[] { KEY_LANDMARK_ID, KEY_LANDMARK_NAME, KEY_LANDMARK_XCOORD, KEY_LANDMARK_YCOORD }, 
    			KEY_LANDMARK_ID + "=?",
    			new String[] { String.valueOf(id) }, null, null, null, null);
    	
    	
    			
    	if (cursor != null)
    		cursor.moveToFirst();
    	
    	Landmark landmark = new Landmark(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3));
    	db.close();
    	return landmark;
    }
    
    public Truck getTruck(int id) {
    	SQLiteDatabase db = this.getReadableDatabase();
    	
    	
    	Cursor cursor = db.query(TABLE_TRUCKS, 
    			new String[] { KEY_TRUCK_ID, KEY_TRUCK_NAME, KEY_TRUCK_CUISINE, KEY_TRUCK_DESCRIPTION, 
    			KEY_TRUCK_EMAIL, KEY_TRUCK_MENU, KEY_TRUCK_TWITTER, KEY_TRUCK_FACEBOOK, KEY_TRUCK_WEBSITE, KEY_TRUCK_YELP  }, 
    			KEY_TRUCK_ID + "=?",
    			new String[] { String.valueOf(id) }, null, null, null, null);
    	if (cursor != null)
    		cursor.moveToFirst();
    	
    	Truck truck = new Truck(Integer.parseInt(cursor.getString(0)), 
    			cursor.getString(1), 
    			cursor.getString(2), 
    			cursor.getString(3),
    			cursor.getString(4),
    			cursor.getString(5),
    			cursor.getString(6),
    			cursor.getString(7),
    			cursor.getString(8),
    			cursor.getString(9));
    	db.close();
    	return truck;
    }
    
    public Schedule getSchedule(int id) {
    	SQLiteDatabase db = this.getReadableDatabase();
    	
    	Cursor cursor = db.query(TABLE_SCHEDULES, 
    			new String[] { KEY_SCHEDULE_ID, KEY_SCHEDULE_DAY_OF_WEEK, KEY_SCHEDULE_TIME_OF_DAY, KEY_SCHEDULE_TRUCK_ID, KEY_SCHEDULE_LANDMARK_ID }, 
    			KEY_SCHEDULE_ID + "=?",
    			new String[] { String.valueOf(id) }, null, null, null, null);
    	if (cursor != null)
    		cursor.moveToFirst();
    	
    	Schedule schedule = new Schedule(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)));
    	db.close();
    	return schedule;
    }
    
    public FoodItem getFoodItem(int id) {
    	SQLiteDatabase db = this.getReadableDatabase();
    	
    	Cursor cursor = db.query(TABLE_FOOD_ITEMS,
    			new String[] { KEY_FOOD_ITEM_ID, KEY_FOOD_ITEM_NAME, KEY_FOOD_ITEM_DESCRIPTION, KEY_FOOD_ITEM_PRICE, KEY_FOOD_ITEM_TRUCK_ID },
    			KEY_FOOD_ITEM_ID + "=?",
    			new String[] { String.valueOf(id) }, null, null, null, null);
    	if (cursor != null)
    		cursor.moveToFirst();
    	
    	FoodItem foodItem = new FoodItem(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), Integer.parseInt(cursor.getString(4)));
    	db.close();
    	return foodItem;
    }
    
    public Factlet getFactlet(int id) {
    	SQLiteDatabase db = this.getReadableDatabase();
    	
    	Cursor cursor = db.query(TABLE_FACTLETS,
    			new String[] { KEY_FACTLET_ID, KEY_FACTLET_TITLE, KEY_FACTLET_CONTENT, KEY_FACTLET_TRUCK_ID },
    			KEY_FACTLET_ID + "=?",
    			new String[] { String.valueOf(id) }, null, null, null, null);
    	if (cursor != null)
    		cursor.moveToFirst();
    	
    	Factlet factlet = new Factlet(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2),Integer.parseInt(cursor.getString(3)));
    	db.close();
    	return factlet;
    }
    
    
    
    public List<Landmark> getAllLandmarks() {
    	
    	String query = "SELECT * FROM " + TABLE_LANDMARKS;
    	return getLandmarksListByQuery(query);
    }
    
    public List<Truck> getAllTrucks() {
    	
    	String query = "SELECT * FROM " + TABLE_TRUCKS;
    	return getTrucksListByQuery(query);
    }
    
    public List<Schedule> getAllSchedules() {
    	
    	String query = "SELECT * FROM " + TABLE_SCHEDULES;
    	return getSchedulesListByQuery(query);
    }
    
    public List<FoodItem> getAllFoodItems() {
    	
    	String query = "SELECT * FROM " + TABLE_FOOD_ITEMS;
    	return getFoodItemsListByQuery(query);
    	
    }
    
public List<Factlet> getAllFactlets() {
    	
    	String query = "SELECT * FROM " + TABLE_FACTLETS;
    	return getFactletsByQuery(query);
    	
    }
    
    public int updateLandmark(Landmark landmark) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	ContentValues values = new ContentValues();
    	values.put(KEY_LANDMARK_NAME, landmark.getName());
    	values.put(KEY_LANDMARK_XCOORD, landmark.getXcoord());
    	values.put(KEY_LANDMARK_YCOORD, landmark.getYcoord());
    	
    	// update row
    	int return_val = db.update(TABLE_LANDMARKS, values, KEY_LANDMARK_ID + " = ?",
    			new String[] { String.valueOf(landmark.getId()) });
    	db.close();
    	return return_val;
    	
    }
    
    public int updateTruck(Truck truck) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	ContentValues values = new ContentValues();

    	
    	
    	values.put(KEY_TRUCK_NAME, truck.getName());
    	values.put(KEY_TRUCK_CUISINE, truck.getCuisine());
    	values.put(KEY_TRUCK_DESCRIPTION, truck.getDescription());
    	values.put(KEY_TRUCK_EMAIL, truck.getEmail());
    	values.put(KEY_TRUCK_MENU, truck.getMenu());
    	values.put(KEY_TRUCK_TWITTER, truck.getTwitter());
    	values.put(KEY_TRUCK_FACEBOOK, truck.getFacebook());
    	values.put(KEY_TRUCK_WEBSITE, truck.getWebsite());
    	values.put(KEY_TRUCK_YELP, truck.getYelp());
    	
    	// update row
    	int return_val =  db.update(TABLE_TRUCKS, values, KEY_TRUCK_ID + " = ?",
    			new String[] { String.valueOf(truck.getId()) });
    	db.close();
    	return return_val;
    	
    }
    
    public int updateSchedule(Schedule schedule) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	ContentValues values = new ContentValues();

    	values.put(KEY_SCHEDULE_DAY_OF_WEEK, schedule.getDayOfWeek());
    	values.put(KEY_SCHEDULE_TIME_OF_DAY, schedule.getTimeOfDay());
    	values.put(KEY_SCHEDULE_TRUCK_ID, schedule.getTruckId());
    	values.put(KEY_SCHEDULE_LANDMARK_ID, schedule.getLandmarkId());
    	
    	// update row
    	int return_val = db.update(TABLE_SCHEDULES, values, KEY_SCHEDULE_ID + " = ?",
    			new String[] { String.valueOf(schedule.getId()) });
    	db.close();
    	return return_val;
    	
    }
    
    public int updateFoodItem(FoodItem foodItem) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	ContentValues values = new ContentValues();
    	values.put(KEY_FOOD_ITEM_ID, foodItem.getId());
    	values.put(KEY_FOOD_ITEM_NAME, foodItem.getName());
    	values.put(KEY_FOOD_ITEM_DESCRIPTION, foodItem.getDescription());
    	values.put(KEY_FOOD_ITEM_PRICE, foodItem.getPrice());
    	values.put(KEY_FOOD_ITEM_TRUCK_ID, foodItem.getTruckId());
    	
    	// update row
    	int return_val = db.update(TABLE_FOOD_ITEMS, values, KEY_FOOD_ITEM_ID + " = ?",
    			new String[] { String.valueOf(foodItem.getId()) });
    	db.close();
    	return return_val;
    	
    }
    
    public int updateFactlet(Factlet factlet) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	ContentValues values = new ContentValues();
    	values.put(KEY_FACTLET_ID, factlet.getId());
    	values.put(KEY_FACTLET_TITLE, factlet.getTitle());
    	values.put(KEY_FACTLET_CONTENT, factlet.getContent());
    	values.put(KEY_FACTLET_TRUCK_ID, factlet.getTruckId());
    	
    	// update row
    	int return_val = db.update(TABLE_FACTLETS, values, KEY_FACTLET_ID + " = ?",
    			new String[] { String.valueOf(factlet.getId()) });
    	db.close();
    	return return_val;
    	
    }
    
    
    // Deleting a single landmark
    public void deleteLandmark(Landmark landmark) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	db.delete(TABLE_LANDMARKS, KEY_LANDMARK_ID + " = ?",
    			new String[] { String.valueOf(landmark.getId()) } );
    	db.close();
    	
    }
    
    public void deleteTruck(Truck truck) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	db.delete(TABLE_TRUCKS, KEY_TRUCK_ID + " = ?",
    			new String[] { String.valueOf(truck.getId()) } );
    	db.close();
    	
    }
    
    public void deleteSchedule(Schedule schedule) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	db.delete(TABLE_SCHEDULES, KEY_SCHEDULE_ID + " = ?",
    			new String[] { String.valueOf(schedule.getId()) } );
    	db.close();
    	
    }
    
    public void deleteFoodItem(FoodItem foodItem) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	db.delete(TABLE_FOOD_ITEMS, KEY_FOOD_ITEM_ID + " =?",
    			new String[] { String.valueOf(foodItem.getId()) } );
    	db.close();
    }
    
    public void deleteFactlet(Factlet factlet) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	db.delete(TABLE_FACTLETS, KEY_FACTLET_ID + " =?",
    			new String[] { String.valueOf(factlet.getId()) } );
    	db.close();
    }
    
    // Get landmark count
    public int getLandmarksCount() {
    	String countQuery = "SELECT * FROM " + TABLE_LANDMARKS;
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery(countQuery, null);
    	cursor.close();
    	
    	// return count
    	db.close();
    	return cursor.getCount();
    	
    }
    
    public int getTrucksCount() {
    	String countQuery = "SELECT * FROM " + TABLE_TRUCKS;
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery(countQuery, null);
    	cursor.close();
    	
    	// return count
    	db.close();
    	return cursor.getCount();
    	
    }
    
    public int getSchedulesCount() {
    	String countQuery = "SELECT * FROM " + TABLE_SCHEDULES;
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery(countQuery, null);
    	cursor.close();
    	
    	// return count
    	db.close();
    	return cursor.getCount();
    	
    }
    
    public int getFoodItemsCount() {
    	String countQuery = "SELECT * FROM " + TABLE_FOOD_ITEMS;
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery(countQuery, null);
    	cursor.close();
    	
    	// return count
    	db.close();
    	return cursor.getCount();
    }
    
    public int getFactletsCount() {
    	String countQuery = "SELECT * FROM " + TABLE_FACTLETS;
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery(countQuery, null);
    	cursor.close();
    	
    	// return count
    	db.close();
    	return cursor.getCount();
    }
    
    private List<Landmark> getLandmarksListByQuery(String selectQuery) {
    	List<Landmark> landmarkList = new ArrayList<Landmark>();
    	
    	SQLiteDatabase db = this.getWritableDatabase();
    	Cursor cursor = db.rawQuery(selectQuery, null);
    	
    	// looping through all rows and adding to list
    	if (cursor.moveToFirst()) {
    		do {
    			Landmark landmark = new Landmark();
    			landmark.setId(Integer.parseInt(cursor.getString(0)));
    			landmark.setName(cursor.getString(1));
    			landmark.setXcoord(cursor.getString(2));
    			landmark.setYcoord(cursor.getString(3));
    			
    			// add back to the list
    			landmarkList.add(landmark);
    		} while (cursor.moveToNext());
    	}
    	
    	// return
    	db.close();
    	return landmarkList;
    }
    
    private List<Truck> getTrucksListByQuery(String selectQuery) {
    	List<Truck> truckList = new ArrayList<Truck>();
    	
    	// Select ALL Query
    	//String selectQuery = "SELECT * FROM " + TABLE_TRUCKS;
    	//String selectQuery = query;
    	
    	SQLiteDatabase db = this.getWritableDatabase();
    	Cursor cursor = db.rawQuery(selectQuery, null);
    	
    	// looping through all rows and adding to list
    	if (cursor.moveToFirst()) {
    		do {
    			
    		
    			Truck truck = new Truck();
    			truck.setId(Integer.parseInt(cursor.getString(0)));
    			truck.setName(cursor.getString(1));
    			truck.setCuisine(cursor.getString(2));
    			truck.setDescription(cursor.getString(3));
    			truck.setEmail(cursor.getString(4));
    			truck.setMenu(cursor.getString(5));
    			truck.setTwitter(cursor.getString(6));
    			truck.setFacebook(cursor.getString(7));
    			truck.setWebsite(cursor.getString(8));
    			truck.setWebsite(cursor.getString(9));
    			
    			
    			
    			
    			
    			
    			
    			
    			
    			// add back to the list
    			truckList.add(truck);
    		} while (cursor.moveToNext());
    	}
    	
    	// return
    	db.close();
    	return truckList;
    }
    
    private List<Schedule> getSchedulesListByQuery(String selectQuery) {
    	List<Schedule> scheduleList = new ArrayList<Schedule>();
    	
    	SQLiteDatabase db = this.getWritableDatabase();
    	Cursor cursor = db.rawQuery(selectQuery, null);
    	
    	// looping through all rows and adding to list
    	if (cursor.moveToFirst()) {
    		do {
    			Schedule schedule = new Schedule();
    			schedule.setId(Integer.parseInt(cursor.getString(0)));
    			schedule.setDayOfWeek(cursor.getString(1));
    			schedule.setTimeOfDay(cursor.getString(2));
    			schedule.setTruckId(Integer.parseInt(cursor.getString(3)));
    			schedule.setLandmarkId(Integer.parseInt(cursor.getString(4)));

    			
    			// add back to the list
    			scheduleList.add(schedule);
    		} while (cursor.moveToNext());
    	}
    	
    	// return
    	db.close();
    	return scheduleList;
    }
    
    private List<FoodItem> getFoodItemsListByQuery(String selectQuery) {
    	List<FoodItem> foodItemList = new ArrayList<FoodItem>();
    	
    	SQLiteDatabase db = this.getWritableDatabase();
    	Cursor cursor = db.rawQuery(selectQuery, null);
    	
    	// looping through all rows and adding to list
    	if (cursor.moveToFirst()) {
    		do {
    			FoodItem foodItem = new FoodItem();
    			foodItem.setId(Integer.parseInt(cursor.getString(0)));
    			foodItem.setName(cursor.getString(1));
    			foodItem.setDescription(cursor.getString(2));
    			foodItem.setPrice(cursor.getString(3));
    			foodItem.setTruckId(Integer.parseInt(cursor.getString(4)));
    			
    			
    			// add back to the list
    			foodItemList.add(foodItem);
    		} while (cursor.moveToNext());
    	}
    	
    	// return
    	db.close();
    	return foodItemList;
    }
    
    private List<Factlet> getFactletsByQuery(String selectQuery) {
    	List<Factlet> factletList = new ArrayList<Factlet>();
    	
    	SQLiteDatabase db = this.getWritableDatabase();
    	Cursor cursor = db.rawQuery(selectQuery, null);
    	
    	// looping through all rows and adding to list
    	if (cursor.moveToFirst()) {
    		do {
    			Factlet factlet = new Factlet();
    			factlet.setId(Integer.parseInt(cursor.getString(0)));
    			factlet.setTitle(cursor.getString(1));
    			factlet.setContent(cursor.getString(2));
    			factlet.setTruckId(Integer.parseInt(cursor.getString(3)));
    			
    			
    			// add back to the list
    			factletList.add(factlet);
    		} while (cursor.moveToNext());
    	}
    	
    	// return
    	db.close();
    	return factletList;
    }
    
    
    
   
 
}
