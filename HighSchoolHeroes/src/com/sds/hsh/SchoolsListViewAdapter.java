package com.sds.hsh;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



public class SchoolsListViewAdapter extends ArrayAdapter<School> {
	
	private ArrayList<School> schools;
	Context ctx;
	int xmlResource;
	TextView tv_currentLabel, tv_current, tv_error;
	
    public SchoolsListViewAdapter(Context context, int textViewResourceId, ArrayList<School> schools, TextView label, TextView current, TextView error) {
            super(context, textViewResourceId, schools);
            this.schools = schools;
            ctx = context;
            xmlResource = textViewResourceId;
            tv_currentLabel = label;
            tv_current = current;
            tv_error = error;
   }
    
    public OnClickListener deleteSchoolBtnListener = new OnClickListener() {
    	public void onClick(View v) {
    		String toDelete = ((TextView)((View)v.getParent()).findViewById(R.id.tv_schools_listView_name)).getText().toString();
    		DBAdapter db = new DBAdapter(ctx);
    		db.open();
       		db.deleteSchoolByName(toDelete);
       		db.close();
       		for(int i = 0; i < schools.size(); i++) {
       			if(toDelete.equalsIgnoreCase(schools.get(i).name)) {
       				schools.remove(i);
       				if(toDelete.equalsIgnoreCase(PreferenceManager.getDefaultSharedPreferences(ctx.getApplicationContext()).getString("currentSchool", ""))) {
       					SharedPreferences.Editor spEditor = PreferenceManager.getDefaultSharedPreferences(ctx.getApplicationContext()).edit();
       					if(schools.size() > 0)
       						spEditor.putString("currentSchool", schools.get(0).name);
       					else {
       						tv_currentLabel.setVisibility(View.GONE);
       						spEditor.putString("currentSchool", "");
       					}
       					spEditor.commit();
       				}
       			}
       		}
       		notifyDataSetChanged();
       		
       		Toast.makeText(ctx, toDelete + " has been deleted.", Toast.LENGTH_LONG).show();
       		tv_current.setText(PreferenceManager.getDefaultSharedPreferences(ctx.getApplicationContext()).getString("currentSchool", ""));
       		
       		if(schools.size() == 0) {
       			tv_error.setVisibility(View.VISIBLE);
       			SharedPreferences.Editor spEditor = PreferenceManager.getDefaultSharedPreferences(ctx.getApplicationContext()).edit();
				spEditor.putBoolean("noSchools", true);
				spEditor.commit();
       		}
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