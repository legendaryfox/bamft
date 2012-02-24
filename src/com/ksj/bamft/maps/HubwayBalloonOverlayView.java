/**
 *	HubwayBalloonOverlayView.java 
 * 
 * 	Customized from https://github.com/jgilfelt/android-mapviewballoons 
 */

package com.ksj.bamft.maps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.maps.OverlayItem;
import com.ksj.bamft.R;
import com.readystatesoftware.mapviewballoons.BalloonOverlayView;

public class HubwayBalloonOverlayView<Item extends OverlayItem> extends BalloonOverlayView<HubwayOverlayItem> {

	private TextView titleView;
	private TextView snippet1View;
	private TextView snippet2View;
	private TextView snippet3View;
	private TextView snippet4View;
	private TextView snippet5View;
	
	public HubwayBalloonOverlayView(Context context, int balloonBottomOffset) {
		super(context, balloonBottomOffset);
	}
	
	@Override
	protected void setupView(Context context, final ViewGroup parent) {
		
		// inflate our custom layout into parent
		LayoutInflater inflater = 
				(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.custom_balloon_overlay, parent);
		
		// setup our fields
		titleView = (TextView) v.findViewById(R.id.balloon_item_title);
		snippet1View = (TextView) v.findViewById(R.id.balloon_item_snippet_1);
		snippet2View = (TextView) v.findViewById(R.id.balloon_item_snippet_2);
		snippet3View = (TextView) v.findViewById(R.id.balloon_item_snippet_3);
		snippet4View = (TextView) v.findViewById(R.id.balloon_item_snippet_4);
		snippet5View = (TextView) v.findViewById(R.id.balloon_item_snippet_5);
	}

	@Override
	protected void setBalloonData(HubwayOverlayItem item, ViewGroup parent) {
		
		// map our custom item data to fields
		String title = item.getTitle();
		String snippet1 = item.getSnippet();
		String snippet2 = item.getSnippet2();
		String snippet3 = item.getSnippet3();
		String snippet4 = item.getSnippet4();
		String snippet5 = item.getSnippet5();
		
		if (title != null && title.length() > 0)
			titleView.setText(title);
		else
			titleView.setVisibility(GONE);
		
		if (snippet1 != null && snippet1.length() > 0)
			snippet1View.setText(snippet1);
		else
			snippet1View.setVisibility(GONE);
		
		if (snippet2 != null && snippet2.length() > 0)
			snippet2View.setText(snippet2);
		else
			snippet2View.setVisibility(GONE);
		
		if (snippet3 != null && snippet3.length() > 0)
			snippet3View.setText(snippet3);
		else
			snippet3View.setVisibility(GONE);
		
		if (snippet4 != null && snippet4.length() > 0)
			snippet4View.setText(snippet4);
		else
			snippet4View.setVisibility(GONE);
		
		if (snippet5 != null && snippet5.length() > 0)
			snippet5View.setText(snippet5);
		else
			snippet5View.setVisibility(GONE);
	}
}
