package com.ksj.bamft.maps;

import java.util.LinkedList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import com.ksj.bamft.model.NavigationStep;
import com.ksj.bamft.model.SimpleLocation;

public class KMLHandler extends DefaultHandler {
	
	// KML tags
	
	private static final String PLACEMARK = "Placemark";
	private static final String NAME = "name";
	private static final String DESCRIPTION = "description";
	private static final String ADDRESS = "address";
	private static final String COORDINATES = "coordinates";
	
	// The list of directions to be created from the KML
	
	private List<NavigationStep> directions;
	
	// Holds data for the current navigation step being parsed
	
	private NavigationStep navigationStep;
	
	// Holds the SimpleLocation of the current NavigationStep
	// being parsed
	
	private SimpleLocation location;
	
	// Holds the text value of the current XML element being parsed
	
	private String elementValue;
	
	// Keep track of what element we're in
	
	private boolean inPlacemark;
	
	/**
	 * Get the list of directions created by the parser.
	 */
	public List<NavigationStep> getDirections() {
		return directions;
	}
	
	/**
	 * When the KML document is opened, 
	 */
	@Override
	public void startDocument() {
		directions = new LinkedList<NavigationStep>();
	}
	
	/**
	 * Called when a new starting tag is encountered.
	 * Initialize station to a new HubwayStation
	 * when <station> is encountered.
	 */
	@Override
	public void startElement(String uri, String localName,
			String qname, Attributes attributes) {
		
		if (localName.equalsIgnoreCase(PLACEMARK)) {
			navigationStep = new NavigationStep();
			location = new SimpleLocation();
			
			inPlacemark = true;
		}
	}
	
	
	/**
	 * Save the value of the current XML element being parsed into
	 * elementValue.
	 */
	@Override
	public void characters (char[] chars, int start, int length) {
		
		// SAX parsers break up text when special characters like
		// "&" are encountered, so use a StringBuilder to build
		// all the pieces into one string
		
		/*StringBuilder strBuilder = new StringBuilder(100);
		
		for (int i = start; i < start + length; i++) {
			strBuilder.append(chars[i]);
		}
		
		elementValue = strBuilder.toString();*/
		
		elementValue = new String(chars, start, length);
		elementValue.trim();
	}
	
	/**
	 * Set elementValue into the appropriate property in the
	 * HubwayStation object.
	 */
	@Override
	public void endElement (String uri, String localName, String qname) {
		
		if (elementValue == null)
			return;
		
		if (localName.equalsIgnoreCase(NAME) && inPlacemark) {
			navigationStep.setDirections(elementValue);
		}
		
		else if (localName.equalsIgnoreCase(DESCRIPTION))
			navigationStep.setDescription(elementValue);
		
		else if (localName.equalsIgnoreCase(ADDRESS))
			navigationStep.setAddress(elementValue);
		
		else if (localName.equalsIgnoreCase(COORDINATES)) {
			String[] tokens = elementValue.split(",");
			
			if (tokens.length != 3)
				return;
			
			String longitude = tokens[0];
			String latitude = tokens[1];
			
			location.setLongitude(Double.parseDouble(longitude));
			location.setLatitude(Double.parseDouble(latitude));
		}
		
		else if (localName.equalsIgnoreCase(PLACEMARK)) {
			navigationStep.setLocation(location);
			directions.add(navigationStep);
			
			inPlacemark = false;
		}
	}
}
