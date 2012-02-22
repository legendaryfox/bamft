package com.ksj.bamft.maps;

import java.util.LinkedList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import com.ksj.bamft.model.NavigationStep;
import com.ksj.bamft.model.SimpleLocation;

public class KMLHandler extends DefaultHandler {
	
	// KML tags
	
	private static final String DOCUMENT = "Document";
	private static final String PLACEMARK = "Placemark";
	private static final String NAME = "name";
	private static final String DESCRIPTION = "description";
	private static final String ADDRESS = "address";
	private static final String LATITUDE = "latitude";
	private static final String LONGITUDE = "longitude";
	private static final String GEOMETRY_COLLECTION = "GeometryCollection";
	
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
	
	private boolean inDocument;
	private boolean inPlacemark;
	private boolean inGeometryCollection;
	
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
			
			inDocument = false;
			inPlacemark = true;
			inGeometryCollection = false;
		}
		
		else if (localName.equalsIgnoreCase(DOCUMENT)) {
			inDocument = true;
			inPlacemark = false;
			inGeometryCollection = false;
		}
		
		else if (localName.equalsIgnoreCase(GEOMETRY_COLLECTION)) {
			inDocument = false;
			inPlacemark = false;
			inGeometryCollection = true;
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
		
		else if (localName.equalsIgnoreCase(LATITUDE)) {
			location.setLatitude(Double.parseDouble(elementValue));
		}
		
		else if (localName.equalsIgnoreCase(LONGITUDE)) {
			location.setLongitude(Double.parseDouble(elementValue));
		}
		
		else if (localName.equalsIgnoreCase(PLACEMARK) && !inGeometryCollection && !inDocument) {
			navigationStep.setLocation(location);
			directions.add(navigationStep);
		}
	}
}
