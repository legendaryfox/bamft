package com.ksj.bamft.maps;

import java.io.Serializable;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class RouteOverlay extends Overlay implements Serializable {

	private static final long serialVersionUID = 4860542916076119990L;
	private GeoPoint pointA;
	private GeoPoint pointB;
	private int color;
	
	public RouteOverlay(GeoPoint pointA, GeoPoint pointB, int color) {
		this.pointA = pointA;
		this.pointB = pointB;
		this.color = color;
	}
	
	public GeoPoint getPointA() {
		return pointA;
	}
	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
	    Projection projection = mapView.getProjection();

	    Point point1 = new Point();
	    projection.toPixels(pointA, point1);
	    
	    Point point2 = new Point();
	    projection.toPixels(pointB, point2);

	    Paint paint = new Paint();
	    paint.setColor(color);
	    paint.setStrokeWidth(5);
	    paint.setAlpha(120);

	    canvas.drawLine(point1.x, point1.y, point2.x, point2.y, paint);
	    super.draw(canvas, mapView, shadow);
	}
}
