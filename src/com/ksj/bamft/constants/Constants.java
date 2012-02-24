package com.ksj.bamft.constants;

public class Constants {

	// URLs
	
	public static final String LANDMARKS_DUMP_URL = "http://bamftserver.heroku.com/landmarks/full_dump";
	public static final String TRUCKS_DUMP_URL = "http://bamftserver.heroku.com/trucks/full_dump";
	public static final String SCHEDULES_DUMP_URL = "http://bamftserver.heroku.com/schedules/full_dump";
	public static final String FOOD_ITEMS_DUMP_URL = "http://bamftserver.heroku.com/menu_items/full_dump";
	public static final String FACTLETS_DUMP_URL = "http://bamftserver.heroku.com/factlets/full_dump";
	public static final String HUBWAY_XML = "http://www.thehubway.com/data/stations/bikeStations.xml";
	public static final String MBTA_CSV = "http://developer.mbta.com/RT_Archive/RealTimeHeavyRailKeys.csv";
	
	
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
	public static final String SCHEDULE_TO_DISTANCE_MAP = "scheduleToDistanceMap";
	public static final String TRUCK = "truck";
	public static final String MENU = "menu";
	public static final String TWITTER_HANDLE = "twitter_handle";
	public static final String TRUCK_ID = "truck_id";
	public static final String YELP_HANDLE = "yelp_handle";
	public static final String MAP_TYPE = "mapType";
	public static final String HUBWAY_ROUTE_USER_TO_STATION = "hubwayUserToStationRoute";
	public static final String HUBWAY_ROUTE_STATION_TO_STATION = "hubwayStationToStationRoute";
	public static final String HUBWAY_ROUTE_STATION_TO_TRUCK = "hubwayStationToTruckRoute";
	public static final String REFERRER = "referrer";
	
	
	// User location & maps
	
	public static final String USER_LATITUDE = "userLatitude";
	public static final String USER_LONGITUDE = "userLongitude";
	public static final String MILES = "mi";
	public static final int LOCATION_REFRESH_TIME = 60000;
	public static final int LOCATION_REFRESH_DISTANCE = 5;
	public static final double BPL_LATITUDE = 42.348673;
	public static final double BPL_LONGITUDE = -71.078612;
	public static final double HYNES_LATITUDE = 42.348419;
	public static final double HYNES_LONGITUDE = -71.084354; 
	public static final String MAP_TYPE_HUBWAY = "hubway";
	public static final String MAP_TYPE_TRUCKS = "trucks";
	public static final int HUBWAY_STATIONS_MAP_ZOOM = 12;
	public static final int OPEN_TRUCKS_MAP_ZOOM = 13;
	
	// Map overlays
	
	public static final String HUBWAY_NUM_BIKES = "# Bikes: ";
	public static final String HUBWAY_NUM_EMPTY_DOCKS = "# Empty Docks: ";
	
	// Messages and dialogs 
	
	public static final String HUBWAY_STATION_LOCKED = "Note: station locked - bikes can be " +
			"returned but not removed";
	
	public static final String ENABLE_LOCATION_PROVIDERS = 
			"This app will be exponentially spiffier if you enable " +
			"a location provider! True story.";

	public static final String ENABLE_LOCATION_PROVIDERS_YES = "Ooh, yes please!";
	public static final String ENABLE_LOCATION_PROVIDERS_NO = "Don't want.";
	
	public static final String ENABLE_INTERNET =
			"We can't serve you any food truck goodness if you " +
			"can't access the internets! :( Go to settings?";
	
	public static final String ENABLE_INTERNET_YES = "Absofruitly";
	public static final String ENABLE_INTERNET_NO = "No thanks";
	
	public static final String ALL_TRUCKS_CLOSED = "All trucks are currently closed :(";
	public static final String HUBWAY_UNAVAILABLE = 
			"Alas! Either all Hubway stations are currently unavailable or " +
			"we are having trouble retrieving Hubway data. We apologize " +
			"profusely :(";
	
	// Trivia responses
	
	public static final String[] TRIVIA_RESPONSES = 
		{ "No waaaaay", "Sweet!", "I feel smarter now", "Wowzers", "That is most excellent!",
		"Pfft I already knew that"};
	
	// Yelp API Keys
	
	public static final String YELP_CONSUMER_KEY = "-5C4AuSdFEk_CG5eWkkspQ";
	public static final String YELP_CONSUMER_SECRET = "tdqFAH50qL3aIYzmjXluFkWutug";
	public static final String YELP_TOKEN = "mNCtTzIZhsSEKYILLYMq6HZtYXKWtkYB";
	public static final String YELP_TOKEN_SECRET = "gCID4b-mQqNf7MxdYgcPMGywWDU";
	
	// Miscellaneous
	
	public static final String BAMFT_PREFS_NAME = "BamftPrefsFile";
	public static final String PREFS_CACHE_UPDATED = "cacheUpdated"; // preference for last update of cache. type is Long.
	public static final long CACHE_LIFE = 60 * 60 * 1000; // how long the cached SQLite life should be (in millis).
	public static final int VISIBILITY_GONE = 8;
	public static final String BROWSER_PACKAGE = "com.android.browser";
	public static final String BROWSER_CLASS = "com.android.browser.BrowserActivity";
}
