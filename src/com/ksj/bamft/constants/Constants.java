package com.ksj.bamft.constants;

public class Constants {

	// URLs
	
	public static final String LANDMARKS_DUMP_URL = "http://bamftserver.heroku.com/landmarks/full_dump";
	public static final String TRUCKS_DUMP_URL = "http://bamftserver.heroku.com/trucks/full_dump";
	public static final String SCHEDULES_DUMP_URL = "http://bamftserver.heroku.com/schedules/full_dump";
	public static final String FOOD_ITEMS_DUMP_URL = "http://bamftserver.heroku.com/menu_items/full_dump";
	public static final String FACTLETS_DUMP_URL = "http://bamftserver.heroku.com/factlets/full_dump";

	// Time
	
	public static final String[] DAYS_OF_WEEK = new String[] {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
	public static final String[] TIMES_OF_DAY = new String[] {"Morning", "Afternoon", "Evening"};
	public static final String MORNING_MEAL_STRING = "Morning";
	public static final String AFTERNOON_MEAL_STRING = "Afternoon";
	public static final String EVENING_MEAL_STRING = "Evening";
	public static final String CLOSED_MEAL_STRING = "Closed";
	public static final int MORNING_START_HOUR = 7;
	public static final int AFTERNOON_START_HOUR = 10;
	public static final int EVENING_START_HOUR = 15;
	public static final int CLOSING_HOUR = 23;
	public static final String EMPTY_FIELD_STRING = "";
	
	// Bundle names
	
	public static final String TIME_OF_DAY = "timeOfDay";
	public static final String DAY_OF_WEEK = "dayOfWeek";
	public static final String SCHEDULE = "schedule";
	public static final String TRUCK = "truck";
	public static final String MENU = "menu";
	public static final String TWITTER_HANDLE = "twitter_handle";
	public static final String TRUCK_ID = "truck_id";
	public static final String YELP_HANDLE = "yelp_handle";
	
	// User location & maps
	
	public static final String USER_LATITUDE = "userLatitude";
	public static final String USER_LONGITUDE = "userLongitude";
	public static final int LOCATION_REFRESH_TIME = 60000;
	public static final int LOCATION_REFRESH_DISTANCE = 5;
	public static final double BPL_LATITUDE = 42.349673;
	public static final double BPL_LONGITUDE = -71.078612;
	
	// Messages (toasts, etc.)
	
	public static final String ALL_TRUCKS_CLOSED = "All trucks are currently closed :(";
	
	// Yelp API Keys
	
	public static final String YELP_CONSUMER_KEY = "-5C4AuSdFEk_CG5eWkkspQ";
	public static final String YELP_CONSUMER_SECRET = "tdqFAH50qL3aIYzmjXluFkWutug";
	public static final String YELP_TOKEN = "mNCtTzIZhsSEKYILLYMq6HZtYXKWtkYB";
	public static final String YELP_TOKEN_SECRET = "gCID4b-mQqNf7MxdYgcPMGywWDU";
	
	// Miscellaneous
	
	public static final int MAX_NUM_TRUCKS = 50; // used when creating data structures to determine how much space to allocate
	public static final String BAMFT_PREFS_NAME = "BamftPrefsFile";
	public static final String PREFS_CACHE_UPDATED = "cacheUpdated"; // preference for last update of cache. type is Long.
	public static long CACHE_LIFE = 60 * 60 * 1000; // how long the cached SQLite life should be (in millis).
}
