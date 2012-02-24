package com.ksj.bamft.hubway;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.util.Log;

import com.ksj.bamft.constants.Constants;
import com.ksj.bamft.maps.MapHelpers;
import com.ksj.bamft.model.HubwayStation;

public class HubwayHelpers {
	
	/**
	 * Returns the complete list of HubwayStation objects created
	 * from the Hubway XML data.
	 * 
	 * @return
	 */
	public static List<HubwayStation> getAvailableStations() {
		StationsXMLHandler stationsHandler = new StationsXMLHandler();
		
		SAXParserFactory factory =  SAXParserFactory.newInstance();
		
		try {
			SAXParser saxParser = factory.newSAXParser();
			XMLReader reader = saxParser.getXMLReader();
			URL url = new URL(Constants.HUBWAY_XML);
			
			reader.setContentHandler(stationsHandler);
			reader.parse(new InputSource(url.openStream()));
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			
		} catch (SAXException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return stationsHandler.getStations();
	}
	
	/**
	 * Given a point, return the closest currently operating
	 * Hubway station (i.e. installed && not locked).
	 */
	public static HubwayStation getNearestStation(List<HubwayStation> stations,
			double sourceLat, double sourceLon) {
		
		HubwayStation nearestStation = null;
		
		if (stations == null)
			return null;
		
		double smallestDistanceSeen = Double.MAX_VALUE;
		
		for (HubwayStation station : stations) {

			// In release mode, uncomment this. For testing,
			// we need to comment out the locked() check 
			// since all stations are currently closed in winter
			if (!station.isInstalled() || station.isLocked())
				continue;
			
			// Get rid of this in release mode. Some stations that 
			// haven't been installed yet have locations of (0.0, 0.0),
			// but all stations are currently listed as uninstalled (winter)
			if (station.getLongitude() == 0.0)
				continue;
			
			double distance = MapHelpers.calculateDistance(
					sourceLat, station.getLatitude(),
					sourceLon, station.getLongitude());
			
			if (distance < smallestDistanceSeen) {
				nearestStation = station;
				smallestDistanceSeen = distance;
			}
		}
		
		//Log.d("NearestHubway", nearestStation.getName());
		
		return nearestStation;
	}
}
