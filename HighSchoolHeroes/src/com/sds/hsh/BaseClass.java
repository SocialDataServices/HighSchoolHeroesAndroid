package com.sds.hsh;

import android.content.Intent;
import android.content.res.Configuration;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;


// action bar that acts as parent for many activities in the project

public class BaseClass extends SherlockActivity{
	
	public void onPause() {
		super.onPause();
	}
	
	public void onResume() {
		super.onResume();
	}
	
	public void onDestroy() {
		super.onDestroy();
	}
	
	public String formatDate(int day, int month, int year) {
		
		String result = "", formattedDay, formattedMonth;
			
		// if day is single digit, add a 0 for consistent format
		if(day < 10) {
			formattedDay = "0" + day;
		}
		else {
			formattedDay = Integer.toString(day);
		}
			
		// do the same for month
		if(month < 10) {
			formattedMonth = "0" + month;
		}
		else {
			formattedMonth = Integer.toString(month);
		}
			
		// concatenate with '/'s to make it look better
		result = formattedMonth + "/" + formattedDay + "/" + year + " (MM/DD/YYYY)";
		return result;
		} // end formatDate
	
	protected boolean isLargeScreen() {
		
		if((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_LARGE) == Configuration.SCREENLAYOUT_SIZE_LARGE)
			return true;
	
		return false;
		
	}
	
	protected boolean isSmallScreen() {
		
		if((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_SMALL) == Configuration.SCREENLAYOUT_SIZE_SMALL)
			return true;
		
		return false;
	}
		
    public boolean onCreateOptionsMenu(Menu menu) {
    	
    	MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.action_bar, menu);
        return true;
    }
	
	// when an option is selected, call custom method Menu Choice
	public boolean onOptionsItemSelected(MenuItem item) {
    
		return MenuChoice(item);    
	} // end onOptionsItemSelected 
	
	// method that just has a switch statement to handle 
	// the ActionBar
	protected boolean MenuChoice(MenuItem item) {
	          
		switch (item.getItemId()) {

	    	case R.id.viewSchedule:
	    		Intent toAddNew = new Intent(getApplicationContext(), TeamSchedule.class);
	    		startActivity(toAddNew);
	        	return true;

	       } // end switch    
		return true;
	} // end MenuChoice
}