package com.sds.hsh;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;

public class SchoolsList extends BaseClass {
	
	ArrayList<School> userSchools;
	DBAdapter db;
	ListView lv_schools;
	ArrayAdapter<School> schoolsListAdapter;
	View previousSelection;
	boolean firstLaunch;
	boolean postApi10;
	Button btn_addSchool;
	Context myContext;
	ImageButton ib_addSchool;
	TextView tv_currentSchool, tv_currentSchoolLabel, tv_error;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schools);
		myContext = this;
		db = new DBAdapter(this);
		db.open();
		firstLaunch = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("firstLaunch", true);
		instantiateComponents();
		
		if(firstLaunch)
			showTutorial();
		else 
			setupActivity();
	}
	
	private void instantiateComponents() {
		
		tv_currentSchool = (TextView)findViewById(R.id.tv_schools_currentSchool);
		tv_currentSchoolLabel = (TextView)findViewById(R.id.tv_schools_currentSchoolLabel);
		tv_error = (TextView)findViewById(R.id.tv_schools_noneFound);
	}
	
	private void configureActionBar() {
		
		if(android.os.Build.VERSION.SDK_INT != android.os.Build.VERSION_CODES.GINGERBREAD_MR1) {
			getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME); 
			getSupportActionBar().setCustomView(R.layout.schools_list_action_bar);
			ib_addSchool = (ImageButton)findViewById(R.id.ib_mySchools_actionBar_add);
			ib_addSchool.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent i = new Intent(myContext, SchoolAdder.class);
					startActivity(i);
				}
			});
			postApi10 = true;
		}
		else {
			btn_addSchool = (Button)findViewById(R.id.btn_schools_add);
			btn_addSchool.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent i = new Intent(myContext, SchoolAdder.class);
					startActivity(i);
				}
			});
			postApi10 = false;
		}
	}
	
	private void showTutorial() {
		
		
		setupActivity();
	}
	
	private void setupListView() {
		
		lv_schools = (ListView)findViewById(R.id.lv_schools);
		
		schoolsListAdapter = new SchoolsListViewAdapter(this, R.layout.schools_list_view_layout, userSchools, tv_currentSchoolLabel, tv_currentSchool, tv_error);
		lv_schools.setAdapter(schoolsListAdapter);
		
		lv_schools.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				
				if(previousSelection != null) {
					previousSelection.setBackgroundColor(Color.WHITE);
				}
				view.setBackgroundColor(Color.parseColor("#ff00ddff"));
				previousSelection = view;			
				tv_currentSchoolLabel.setVisibility(View.VISIBLE);
				tv_currentSchool.setText(userSchools.get(pos).name);
				SharedPreferences.Editor spEditor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
				spEditor.putString("currentSchool", userSchools.get(pos).name);
				spEditor.commit();
				
				Intent toSchedule = new Intent(getApplicationContext(), TeamSchedule.class);
				startActivity(toSchedule);
			}
		});
	}
	
	private void addNewSchool() {
		Intent i = new Intent(this, SchoolAdder.class);
		startActivity(i);
	}
	
	private void setupActivity() {
		
		configureActionBar();
		
		userSchools = db.getSchools();
		if(userSchools.size() == 0) {
			SharedPreferences.Editor spEditor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
			spEditor.putBoolean("noSchools", true);
			spEditor.commit();
			addNewSchool();
		}
		else
			setupListView();
		
		String userCurrentSchool = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("currentSchool", "");
		if(!userCurrentSchool.equals("")) {
			tv_currentSchoolLabel.setVisibility(View.VISIBLE);
			tv_currentSchool.setText(userCurrentSchool);
		}
	}

}