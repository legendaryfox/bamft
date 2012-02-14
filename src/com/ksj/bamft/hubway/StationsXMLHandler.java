package com.ksj.bamft.hubway;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import com.ksj.bamft.model.HubwayStation;

public class StationsXMLHandler extends DefaultHandler {
	
	// Hubway XML tags
	
	private static final String STATION = "station";
	private static final String NAME = "name";
	private static final String LATITUDE = "lat";
	private static final String LONGITUDE = "long";
	private static final String INSTALLED = "installed";
	private static final String LOCKED = "locked";
	private static final String NUM_BIKES = "nbBikes";
	private static final String NUM_EMPTY_DOCKS = "nbEmptyDocks";
	
	// The list of stations to be created from the XML
	
	private List<HubwayStation> stations;
	
	// Holds the data for the current station being parsed
	
	private HubwayStation station;
	
	// Holds the text value of the current XML element being parsed
	
	private String elementValue;
	
	/**
	 * Return the list of stations created by the parser.
	 * 
	 * @return
	 */
	public List<HubwayStation> getStations() {
		return stations;
	}
	
	/**
	 * When the XML document is opened, initialize the list
	 * of Hubway stations the parser will return.
	 */
	@Override
	public void startDocument() {
		stations = new ArrayList<HubwayStation>();
	}
	
	@Override
	public void startElement(String uri, String localName,
			String qname, Attributes attributes) {
		
		if (localName.equals(STATION))
			station = new HubwayStation();
	}
	
	
	/**
	 * Save the value of the current XML element being parsed into
	 * elementValue.
	 */
	@Override
	public void characters (char[] chars, int start, int length) {
		elementValue = new String(chars, start, length);
		elementValue.trim();
	}
	
	/**
	 * Set elementValue into the appropriate property in the
	 * HubwayStation object.
	 */
	@Override
	public void endElement (String uri, String localName, String qname) {
		if (localName.equals(NAME))
			station.setName(elementValue);
		
		else if (localName.equals(LATITUDE)) {
			if (elementValue != null && elementValue.length() > 0)
				station.setLatitude(Double.parseDouble(elementValue));
		}
		
		else if (localName.equals(LONGITUDE)) {
			if (elementValue != null && elementValue.length() > 0)
				station.setLongitude(Double.parseDouble(elementValue));
		}
		
		else if (localName.equals(INSTALLED))
			station.setInstalled(Boolean.parseBoolean(elementValue));
		
		else if (localName.equals(LOCKED))
			station.setLocked(Boolean.parseBoolean(elementValue));
		
		else if (localName.equals(NUM_BIKES)) {
			if (elementValue != null && elementValue.length() > 0)
				station.setNumBikes(Integer.parseInt(elementValue));
		}
		
		else if (localName.equals(NUM_EMPTY_DOCKS)) {
			if (elementValue != null && elementValue.length() > 0)
				station.setNumEmptyDocks(Integer.parseInt(elementValue));
		}
		
		else if (localName.equals(STATION)) {
			stations.add(station);
		}
	}
}
