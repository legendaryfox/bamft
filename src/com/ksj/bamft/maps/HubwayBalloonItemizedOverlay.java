/**
 *	HubwayBalloonItemizedOverlay.java
 * 
 * Customized from https://github.com/jgilfelt/android-mapviewballoons
 */

package com.ksj.bamft.maps;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;
import com.readystatesoftware.mapviewballoons.BalloonOverlayView;

public class HubwayBalloonItemizedOverlay<Item extends OverlayItem>
	extends BalloonItemizedOverlay<HubwayOverlayItem> {
	
	private ArrayList<HubwayOverlayItem> overlays = new ArrayList<HubwayOverlayItem>();
	private Context context;

	public HubwayBalloonItemizedOverlay(Drawable defaultMarker, MapView mapView) {
		super(boundCenter(defaultMarker), mapView);
		populate();
	}
	
	public void addOverlay(HubwayOverlayItem overlay) {
	    overlays.add(overlay);
	}
	
	public void populateNow() {
		populate();
	}

	@Override
	protected HubwayOverlayItem createItem(int i) {
		return overlays.get(i);
	}

	@Override
	public int size() {
		return overlays.size();
	}

	@Override
	protected boolean onBalloonTap(int index, HubwayOverlayItem item) {
		super.hideBalloon();
		return true;
	}

	@Override
	protected BalloonOverlayView<HubwayOverlayItem> createBalloonOverlayView() {
		// use our custom balloon view with our custom overlay item type:
		return new HubwayBalloonOverlayView<HubwayOverlayItem>(getMapView().getContext(), getBalloonBottomOffset());
	}
}
