package com.sds.hsh;

import java.util.ArrayList;






import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;



public class ScheduleListViewAdapter extends ArrayAdapter<ScheduledGame> {
	
	private ArrayList<ScheduledGame> games;
	Context ctx;
	int xmlResource;

    public ScheduleListViewAdapter(Context context, int textViewResourceId, ArrayList<ScheduledGame> games) {
            super(context, textViewResourceId, games);
            this.games = games;
            ctx = context;
            xmlResource = textViewResourceId;
    }
          
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater vi = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(xmlResource, null);
        
        ScheduledGame g = games.get(position);
        if (g != null) {
              
        	TextView gameDate = (TextView)v.findViewById(R.id.tv_schedule_listView_date);
        	TextView awayTeam = (TextView)v.findViewById(R.id.tv_schedule_listView_awayTeam);
        	TextView homeScore = (TextView)v.findViewById(R.id.tv_schedule_listView_homeScore);
        	TextView awayScore = (TextView)v.findViewById(R.id.tv_schedule_listView_awayScore);
        	
        	gameDate.setText(formatDate(g.gameDay, g.gameMonth));
        	awayTeam.setText(g.awayTeamName);
        	homeScore.setText(Integer.toString(g.homeTeamPoints));
        	awayScore.setText(Integer.toString(g.awayTeamPoints));
        	
        	
        }
        return v;
    }
    
    public String formatDate(int day, int month) {
		
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
		result = formattedMonth + "/" + formattedDay;
		return result;
	} // end formatDate
	
}