/**
 *  GoogleMapsConstants.java
 * 
 *  These constants are used for building Google Maps
 *  map activities and for constructing queries to Maps.
 */

package com.ksj.bamft.constants;

public class GoogleMapsConstants {
	
	public static final String URL = "http://maps.google.com/maps";
	public static final String PACKAGE = "com.google.android.apps.maps";
	public static final String CLASS = "com.google.android.maps.MapsActivity";
	
	// Parameters
	
	public static final String SOURCE_ADDR = "saddr=";
	public static final String DEST_ADDR = "daddr=";
	public static final String LAT_LON_DIVIDER = ",";
	public static final String DEST_DIVIDER = "+to:";
	public static final String ROUTE_TYPE = "dirflg=";
	public static final String PUBLIC_TRANSIT_SORT = "sort";
	public static final String OUTPUT = "output=";
	
	// Parameter values
	
	public static final String WALKING_ROUTE = "w";
	public static final String BIKING_ROUTE = "b";
	public static final String PUBLIC_TRANSIT = "r";
	public static final String HTML = "html";
	public static final String KML = "kml";
}
