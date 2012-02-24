/**
 * HubwayOverlayItem.java
 * 
 * Customized from https://github.com/jgilfelt/android-mapviewballoons 
 */

package com.ksj.bamft.maps;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class HubwayOverlayItem extends OverlayItem {
	
	private String snippet2 = "";
	private String snippet3 = "";
	private String snippet4 = "";
	private String snippet5 = "";

	public HubwayOverlayItem(GeoPoint point, String title, String snippet1,
			String snippet2, String snippet3, String snippet4, String snippet5) {
		
		super(point, title, snippet1);
		
		this.snippet2 = snippet2;
		this.snippet3 = snippet3;
		this.snippet4 = snippet4;
		this.snippet5 = snippet5;
	}

	public String getSnippet2() {
		return snippet2;
	}

	public String getSnippet3() {
		return snippet3;
	}

	public String getSnippet4() {
		return snippet4;
	}

	public String getSnippet5() {
		return snippet5;
	}
}
