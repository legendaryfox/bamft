package com.ksj.bamft.model;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class MapOverlays extends ItemizedOverlay<OverlayItem> {
	
	private Context context;
	private ArrayList<OverlayItem> overlays = new ArrayList<OverlayItem>();

	public MapOverlays(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}
	
	public MapOverlays(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		this.context = context;
	}
	
	public void addOverlay(OverlayItem overlay) {
		overlays.add(overlay);
		populate();
	}
	

	/*
	 * Called by populate() to retrieve each OverlayItem.
	 * @see com.google.android.maps.ItemizedOverlay#createItem(int)
	 */
	@Override
	protected OverlayItem createItem(int index) {
		return overlays.get(index);
	}

	@Override
	public int size() {
		return overlays.size();
	}
	

	/* 
	 * Handle event when an item is tapped by the user.
	 * @see com.google.android.maps.ItemizedOverlay#onTap(int)
	 */
	@Override
	protected boolean onTap(int index) {
		OverlayItem item = overlays.get(index);
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle(item.getTitle());
		dialog.setMessage(item.getSnippet());
		dialog.show();
		
		return true;
	}

}
