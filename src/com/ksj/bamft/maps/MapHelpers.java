package com.ksj.bamft.maps;

public class MapHelpers {
	
	/**
     * Use voodoo math to calculate distance between two sets of coordinates.
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
    	
    	double phi1 = (90 - lat1) * degreesToRadians;
    	double phi2 = (90 - lat2) * degreesToRadians;

    	double theta1 = lon1 * degreesToRadians;
    	double theta2 = lon2 * degreesToRadians;
    	
    	double cos = (Math.sin(phi1) * Math.sin(phi2) *
    			Math.cos(theta1 - theta2) + 
    			Math.cos(phi1) * Math.cos(phi2));
    	
    	return Math.acos(cos);
    }
}
