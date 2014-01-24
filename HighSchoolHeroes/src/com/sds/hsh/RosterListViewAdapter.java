package com.sds.hsh;

import java.util.ArrayList;






import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;



public class RosterListViewAdapter extends ArrayAdapter<Player> {
	
	private ArrayList<Player> players;
	Context ctx;
	int xmlResource;

    public RosterListViewAdapter(Context context, int textViewResourceId, ArrayList<Player> players) {
            super(context, textViewResourceId, players);
            this.players = players;
            ctx = context;
            xmlResource = textViewResourceId;
    }
          
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater vi = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(xmlResource, null);
        
        Player p = players.get(position);
        if (p != null) {
              
        	TextView name = (TextView)v.findViewById(R.id.tv_roster_listView_name);
        	TextView number = (TextView)v.findViewById(R.id.tv_roster_listView_number);
        	TextView year = (TextView)v.findViewById(R.id.tv_roster_listView_year);
        	TextView height = (TextView)v.findViewById(R.id.tv_roster_listView_height);
        	TextView weight = (TextView)v.findViewById(R.id.tv_roster_listView_weight);
        	TextView pos = (TextView)v.findViewById(R.id.tv_roster_listView_position);
        	
        	name.setText(p.firstName + " " + p.lastName);
        	number.setText(Integer.toString(p.number));
        	year.setText(p.year);
        	height.setText(p.height);
        	weight.setText(Integer.toString(p.weight));
        	pos.setText(p.position);
        	
        	
        }
        return v;
    }	
}