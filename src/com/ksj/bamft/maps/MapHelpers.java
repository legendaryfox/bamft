package com.ksj.bamft.maps;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.ksj.bamft.constants.GoogleMapsConstants;
import com.ksj.bamft.model.NavigationStep;
import com.ksj.bamft.model.SimpleLocation;

public class MapHelpers {
	
	/** 
	 * Return a location provider with the given accuracy.
	 */
	
	public static String getLocationProvider(Context context, LocationManager locationManager,
			int accuracy) {
    	
    	Criteria criteria = new Criteria();
    	criteria.setAccuracy(accuracy);
    	
    	return locationManager.getBestProvider(criteria, true);
	}
	
	/**
	 * Return user location, given a LocationManager.
	 */
	public static Location getUserLocation(LocationManager locationManager,
			String locationProvider) {
		
		if (locationManager == null || locationProvider == null)
			return null;
		
		Location userLocation;
		int x = 0;
    	
    	do {
    		userLocation = locationManager.getLastKnownLocation(locationProvider);
    		x++;
    	} while (userLocation != null && x < 20);
        
        return userLocation;
	}
	
	/** 
	 * Convert a value in degrees to a value in microdegrees.
	 * 
	 * @param degree
	 * @return
	 */
	public static int degreesToMicrodegrees(double degree) {
		return (int) (degree * 1e6);
	}
	
	
	/**
	 * Given a latitude and longitude, return a GeoPoint object. 
	 * 
	 * @param lat
	 * @param lon
	 * @return
	 */
	public static GeoPoint getGeoPoint(double lat, double lon) {
		return new GeoPoint(degreesToMicrodegrees(lat), degreesToMicrodegrees(lon));
	}
	
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
	public static String getMapsQuery(SimpleLocation source, List<SimpleLocation> destinations,
			String routeType, String output) {
		
		if (source == null || destinations == null || destinations.size() < 1)
			return "";
		
		String url = 
				GoogleMapsConstants.URL + "?" +
				GoogleMapsConstants.SOURCE_ADDR + getCommaSeparatedLatLon(source) + "&" +
				GoogleMapsConstants.DEST_ADDR + getDestinationsString(destinations) + "&" +
				GoogleMapsConstants.ROUTE_TYPE + routeType + "&" + 
				GoogleMapsConstants.OUTPUT + output;
		
		Log.d("MapQuery", url);
		
		return url;
	}
	
	public static List<NavigationStep> getDirections(SAXParserFactory factory, String mapsQuery) {
		
		KMLHandler kmlHandler = new KMLHandler();
		
		try {
			URL mapsQueryURL = new URL(mapsQuery);
			SAXParser parser = factory.newSAXParser();
			XMLReader reader = parser.getXMLReader();
			reader.setContentHandler(kmlHandler);
			reader.parse(new InputSource(mapsQueryURL.openStream()));
		}
		
		catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		catch (SAXException e) {
			e.printStackTrace();
		}
		
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return kmlHandler.getDirections();
	}
	
	/**
	 * Given a set of directions in the form of List<NavigationStep>, return a 
	 * List<RouteOverlay> that will draw the route on a MapActivity. 
	 * 
	 * @param directions
	 * @return
	 */
	public static List<RouteOverlay> getRouteOverlay(List<NavigationStep> directions, int color) {
		List<RouteOverlay> routeOverlays = new LinkedList<RouteOverlay>();
		
		for (int i = 1; i < directions.size(); i++) {
			
			// Get point A
			
			SimpleLocation locationA = directions.get(i - 1).getLocation();
			GeoPoint pointA = MapHelpers.getGeoPoint(
					locationA.getLatitude(), locationA.getLongitude());
			
			// Get point B
			
			SimpleLocation locationB = directions.get(i).getLocation();
			GeoPoint pointB = MapHelpers.getGeoPoint(
					locationB.getLatitude(), locationB.getLongitude());
			
			RouteOverlay routeOverlay = new RouteOverlay(pointA, pointB, color);
			
			routeOverlays.add(routeOverlay);
		}
		
		return routeOverlays;
	}
	
	/**
	 * Given a location object, return the string "latitude,longitude". 
	 * 
	 * @param location
	 * @return
	 */
	private static String getCommaSeparatedLatLon(SimpleLocation location) {
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
	private static String getDestinationsString(List<SimpleLocation> destinationList) {
		StringBuilder destinations = new StringBuilder();
		
		Iterator<SimpleLocation> iterator = destinationList.iterator();
		
		while (iterator.hasNext()) {
			destinations.append(getCommaSeparatedLatLon(iterator.next()));
			
			if (iterator.hasNext())
				destinations.append(GoogleMapsConstants.DEST_DIVIDER);
		}
		
		return destinations.toString();
	}
}
