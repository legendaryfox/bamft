package com.ksj.bamft.maps;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;

import com.ksj.bamft.constants.GoogleMapsConstants;
import com.ksj.bamft.model.Location;

public class MapHelpers {
	
	/**
     * Use voodoo math to calculate distance between two sets of coordinates.
     * Return distance in miles.
     * 
     * Translated from http://www.johndcook.com/python_longitude_latitude.html
     * 
     * @param phi1
     * @param phi2
     * @param theta1
     * @param theta2
     * @return
     */
	public static double calculateDistance(double lat1, double lat2,
    		double lon1, double lon2) {
    	
    	double degreesToRadians = Math.PI / 180.0;
    	
    	double phi1 = (90.0 - lat1) * degreesToRadians;
    	double phi2 = (90.0 - lat2) * degreesToRadians;

    	double theta1 = lon1 * degreesToRadians;
    	double theta2 = lon2 * degreesToRadians;
    	
    	double cos = (Math.sin(phi1) * Math.sin(phi2) *
    			Math.cos(theta1 - theta2) + 
    			Math.cos(phi1) * Math.cos(phi2));
    	
    	return Math.acos(cos) * 3960;
    }
	
	/** 
	 * Round distance to a given number of decimal places.
	 */
	public static String roundDistanceToDecimalPlace(int decimalPlaces, double distance) {
		BigDecimal bigDecimal = new BigDecimal(distance);
		BigDecimal roundedBigDecimal = bigDecimal.setScale(decimalPlaces, RoundingMode.HALF_UP);

		return NumberFormat.getInstance().format(roundedBigDecimal.doubleValue());
	}
	
	/** 
	 * Return a Google Maps URL that links to the requested directions.
	 * 
	 * @param source
	 * @param destinations
	 * @param routeType
	 * @return
	 */
	public static String getDirections(Location source, List<Location> destinationList,
			String routeType) {
		
		if (source == null || destinationList == null || destinationList.size() < 1)
			return "";
		
		String url = 
				GoogleMapsConstants.URL + "?" +
				GoogleMapsConstants.SOURCE_ADDR + getCommaSeparatedLatLon(source) + "&" +
				GoogleMapsConstants.DEST_ADDR + getDestinationsString(destinationList) + "&" +
				GoogleMapsConstants.ROUTE_TYPE + routeType;
		;
		
		return url;
	}
	
	/**
	 * Given a location object, return the string "latitude,longitude". 
	 * 
	 * @param location
	 * @return
	 */
	private static String getCommaSeparatedLatLon(Location location) {
		if (location == null)
			return "";
		
		return Double.toString(location.getLatitude()) +
				GoogleMapsConstants.LAT_LON_DIVIDER + 
				Double.toString(location.getLongitude());
	}
	
	/**
	 * Return a Google-Maps-friendly string of destinations.
	 * i.e. dest1Lat,dest1Lon+to:dest2Lat,dest2Lon+to:etc.
	 * 
	 * @param destinationList
	 * @return
	 */
	private static String getDestinationsString(List<Location> destinationList) {
		StringBuilder destinations = new StringBuilder();
		
		Iterator<Location> iterator = destinationList.iterator();
		
		while (iterator.hasNext()) {
			destinations.append(getCommaSeparatedLatLon(iterator.next()));
			
			if (iterator.hasNext())
				destinations.append(GoogleMapsConstants.DEST_DIVIDER);
		}
		
		return destinations.toString();
	}
}
