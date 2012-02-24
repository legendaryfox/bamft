package com.ksj.bamft.actionbarhelpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.ksj.bamft.R;
import com.ksj.bamft.activity.BamftActivity;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class ActionBarTitleHelper {


	public static void setTitleBar(Context context) {
		// Action Bar Left Icon
		final ActionBar actionBar = (ActionBar) ((Activity) context).findViewById(R.id.actionbar);
		if (actionBar != null) {
			actionBar.setHomeAction(new IntentAction(context, createIntent(context), R.drawable.ic_launcher));
			actionBar.setTitle("BAMFT!");
		}
	}
	
	public static Intent createIntent(Context context) {
		Intent i = new Intent(context, BamftActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		return i;
	}
}
