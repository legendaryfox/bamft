package com.ksj.bamft;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;




public class BamftActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));
        
        gridview.setOnItemClickListener(new OnItemClickListener() {
        	
        	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

        		Bundle timeOfDayBundle = new Bundle();
        		timeOfDayBundle.putString("timeOfDay", "Afternoon"); //just in case...note that putString automatically overwrites existing values too
        		
        		Intent loadTruckListIntent = new Intent(BamftActivity.this, TruckListActivity.class);
        		
        		switch(position) {
        		case 0:
        			//Load Morning trucks
        			Toast.makeText(BamftActivity.this, "Morning Trucks", Toast.LENGTH_SHORT).show();
        			timeOfDayBundle.putString("timeOfDay", "morning");
        			break;
        		case 1:
        			//Load Afternoon trucks
        			Toast.makeText(BamftActivity.this, "Afternoon Trucks", Toast.LENGTH_SHORT).show();
        			timeOfDayBundle.putString("timeOfDay", "afternoon");
        			break;
        		case 2:
        			//Load Evening trucks
        			Toast.makeText(BamftActivity.this, "Evening Trucks", Toast.LENGTH_SHORT).show();
        			timeOfDayBundle.putString("timeOfDay", "evening");
        			break;
        		case 3:
        			//Load ALL THE TRUCKS!!!!11!!! but i don't know how to do this yet.
        			Toast.makeText(BamftActivity.this, "All Trucks", Toast.LENGTH_SHORT).show();
        			break;
        		}        				
        		
        		loadTruckListIntent.putExtras(timeOfDayBundle);
        		BamftActivity.this.startActivity(loadTruckListIntent);
        	}
        	
        });
        
        
        
    }
    
   
        
}
