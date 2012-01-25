package com.ksj.bamft;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;




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
        		//Toast.makeText(BamftActivity.this, "" + position, Toast.LENGTH_SHORT).show();
        		Intent myIntent = new Intent(BamftActivity.this, MorningTruckActivity.class);
        		BamftActivity.this.startActivity(myIntent);
        	}
        	
        });
        
        
        
    }
    
   
        
}
