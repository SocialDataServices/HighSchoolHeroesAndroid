package com.sds.hsh;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;



public class SchoolsListViewAdapter extends ArrayAdapter<School> {
	
	private ArrayList<School> schools;
	Context ctx;
	int xmlResource;

    public SchoolsListViewAdapter(Context context, int textViewResourceId, ArrayList<School> schools) {
            super(context, textViewResourceId, schools);
            this.schools = schools;
            ctx = context;
            xmlResource = textViewResourceId;
    }
    
    public OnClickListener deleteSchoolBtnListener = new OnClickListener() {
    	public void onClick(View v) {
    		String toDelete = ((TextView)((View)v.getParent()).findViewById(R.id.tv_schools_listView_name)).getText().toString();
    		DBAdapter db = new DBAdapter(ctx);
    		db.open();
       		db.deleteSchoolByName(toDelete);
       		db.close();
       		
       		for(int i = 0; i < schools.size(); i++) {
       			if(toDelete.equalsIgnoreCase(schools.get(i).name))
       				schools.remove(i);
       		}
       		notifyDataSetChanged();
       		Toast.makeText(ctx, toDelete + " has been deleted.", Toast.LENGTH_LONG).show();
    	}
    };
          
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater vi = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(xmlResource, null);
        
        v.findViewById(R.id.ib_schools_listView_delete).setOnClickListener(deleteSchoolBtnListener);
        
        School s = schools.get(position);
        if (s != null) {
        	TextView tv_name = (TextView)v.findViewById(R.id.tv_schools_listView_name);
        	tv_name.setText(s.name);
        }
        return v;
    }	
}