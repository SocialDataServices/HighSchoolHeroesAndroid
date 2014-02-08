package com.sds.hsh;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class SchoolAdder extends BaseClass {
		
	Spinner spin_schools, spin_states, spin_cities;
	ArrayList<School> schools;
	ArrayList<String> list_schools, list_states, list_cities;
	ArrayAdapter<String> schoolsSpinnerAdapter, statesSpinnerAdapter, citiesSpinnerAdapter;
	TextView tv_state, tv_city;
	String selectedState, selectedCity;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_school);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		makeStatesSpinner();
		tv_state = (TextView)findViewById(R.id.tv_addSchool_state);
		tv_city = (TextView)findViewById(R.id.tv_addSchool_city);
	}	
	
	private void resetCitiesSpinner() {
		
		list_cities.clear();
		citiesSpinnerAdapter.notifyDataSetChanged();
		spin_cities.setVisibility(View.GONE);
		tv_city.setText("");
		
		LinearLayout.LayoutParams params = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f);
		spin_states.setLayoutParams(params);
	}
	
	private void resetSchoolsSpinner() {
		
		list_schools.clear();
		schoolsSpinnerAdapter.notifyDataSetChanged();
		spin_schools.setVisibility(View.GONE);
	}
	
	public OnItemSelectedListener spinListener = new OnItemSelectedListener() {

		public void onItemSelected(AdapterView<?> parent, View selected, int pos, long id) {
			
				switch(parent.getId()) {
					case R.id.spin_addSchool_states:
						// reset other spinners if no state is selected
						if(pos == 0) {
							if(spin_cities != null)
								resetCitiesSpinner();
							if(spin_schools != null)
								resetSchoolsSpinner();
							tv_state.setText("");
						}
						else {
							selectedState = (String)parent.getItemAtPosition(pos);
							tv_state.setText(selectedState);
							makeCitiesSpinner();
						}
						break;
					case R.id.spin_addSchool_cities:
						if(pos != 0) {
							selectedCity = (String)parent.getItemAtPosition(pos);
							tv_city.setText(selectedCity);
							makeSchoolsSpinner();
						}
						// reset schools spinner if no city is selected
						else
							if(spin_schools != null)
								resetSchoolsSpinner();
						break;
					case R.id.spin_addSchool_schools:
						if(pos != 0)
							handleSchoolSelection(schools.get(pos-1));
						break;
				}
		}
		public void onNothingSelected(AdapterView<?> arg0) {		
		}
	};
	
	private void makeStatesSpinner() {
		
		spin_states = (Spinner)findViewById(R.id.spin_addSchool_states);
		list_states = new ArrayList<String>();
		list_states.add("Select a state..");
		list_states.addAll(getStates());
		
		spin_states.setSelection(0);
		statesSpinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), R.drawable.spinner_template, list_states);
		statesSpinnerAdapter.setDropDownViewResource(R.drawable.spinner_dropdown_template);
		spin_states.setAdapter(statesSpinnerAdapter);	
		spin_states.setOnItemSelectedListener(spinListener);
	}
	
	private void makeSchoolsSpinner() {
		
		spin_schools = (Spinner)findViewById(R.id.spin_addSchool_schools);
		list_schools = new ArrayList<String>();
		list_schools.add("Select a school..");
		list_schools.addAll(getSchools());
		
		spin_schools.setSelection(0);
		schoolsSpinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), R.drawable.spinner_template, list_schools);
		schoolsSpinnerAdapter.setDropDownViewResource(R.drawable.spinner_dropdown_template);
		spin_schools.setAdapter(schoolsSpinnerAdapter);	
		spin_schools.setOnItemSelectedListener(spinListener);
		
		spin_schools.setVisibility(View.VISIBLE);
	}
	
	private void handleSchoolSelection(School s) {
		
		DBAdapter db = new DBAdapter(this);
		db.open();
		if(db.schoolNotInDB(s.id)) {
			db.addSchool(s.id, s.name, s.city, s.state, s.zip, s.size);
			SharedPreferences.Editor spEditor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
			spEditor.putString("currentSchool", s.name);
			spEditor.putBoolean("noSchools", false);
			spEditor.commit();
			Intent i = new Intent(this, TeamSchedule.class);
			startActivity(i);
		}
		else
			Toast.makeText(this, "You've already added " + s.name + ".", Toast.LENGTH_LONG).show();	
		db.close();
	}
	
	private void makeCitiesSpinner() {
		
		spin_cities = (Spinner)findViewById(R.id.spin_addSchool_cities);
		list_cities = new ArrayList<String>();
		list_cities.add("Select a city..");
		list_cities.addAll(getCities());
		
		spin_cities.setSelection(0);
		citiesSpinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), R.drawable.spinner_template, list_cities);
		citiesSpinnerAdapter.setDropDownViewResource(R.drawable.spinner_dropdown_template);
		spin_cities.setAdapter(citiesSpinnerAdapter);	
		spin_cities.setOnItemSelectedListener(spinListener);
		
		LinearLayout.LayoutParams params = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.5f);
		spin_cities.setLayoutParams(params);
		spin_states.setLayoutParams(params);
		spin_cities.setVisibility(View.VISIBLE);
	}
	
	private ArrayList<String> getStates() {
		
		ArrayList<String> result = new ArrayList<String>();
		
		String jsonString = "";
		StringBuilder sb = new StringBuilder();
		InputStream is = null;
				
		//load the JSON into an input stream
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://www.sodaservices.com/HighSchoolHeroes/php/getStates.php");
			HttpResponse response = httpClient.execute(httpPost);			
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch(Exception e) {
			Log.e("log_tag", "Pull From Server" + e.toString());
		}	
		
		// parse the JSON and load it into a string
		// each row should be delimited by \n's
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			
			sb = new StringBuilder();
			sb.append(reader.readLine() + "\n");
			
			String line ="";
			while((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			reader.close();
			is.close();
			jsonString = sb.toString();
		}
		catch (Exception e) {
			Log.e("log_tag", "JSON to String" + e.toString());
		}
		
		// Print the JSON to log for debugging
		Log.d("JSON", jsonString);
		
		JSONArray jArray = new JSONArray();
		try {
			jArray = new JSONArray(jsonString);
		} catch(JSONException e) {
			e.printStackTrace();
		}		
		try {
			JSONObject json_data = new JSONObject();
			
			// iterate through the JSON, adding the data to arrays
			for(int i = 0; i < jArray.length(); i++) {		
				json_data = jArray.getJSONObject(i);
				result.add(json_data.getString("State"));
			}
		}catch(JSONException e) {
			Log.e("no data", "No data found");
		}
		catch(ParseException e) {
			e.printStackTrace();
		}
		
		// populate the ArrayList with info from the JSON
	//	for(int i = 0; i < jArray.length(); i++)
		
		return result;
		
	}
	
	private ArrayList<String> getCities() {
		
		ArrayList<String> result = new ArrayList<String>();
		
		String jsonString = "";
		StringBuilder sb = new StringBuilder();
		InputStream is = null;
				
		//load the JSON into an input stream
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://www.sodaservices.com/HighSchoolHeroes/php/getCities.php");
			ArrayList<NameValuePair> nvPairs = new ArrayList<NameValuePair>();
			nvPairs.add(new BasicNameValuePair("state", selectedState));
			httpPost.setEntity(new UrlEncodedFormEntity(nvPairs));
			HttpResponse response = httpClient.execute(httpPost);			
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch(Exception e) {
			Log.e("log_tag", "Pull From Server" + e.toString());
		}	
		
		// parse the JSON and load it into a string
		// each row should be delimited by \n's
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			
			sb = new StringBuilder();
			sb.append(reader.readLine() + "\n");
			
			String line ="";
			while((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			reader.close();
			is.close();
			jsonString = sb.toString();
		}
		catch (Exception e) {
			Log.e("log_tag", "JSON to String" + e.toString());
		}
		
		// Print the JSON to log for debugging
		Log.d("JSON", jsonString);
		
		JSONArray jArray = new JSONArray();
		try {
			jArray = new JSONArray(jsonString);
		} catch(JSONException e) {
			e.printStackTrace();
		}		
		try {
			JSONObject json_data = new JSONObject();
			
			// iterate through the JSON, adding the data to arrays
			for(int i = 0; i < jArray.length(); i++) {		
				json_data = jArray.getJSONObject(i);
				result.add(json_data.getString("City"));
			}
		}catch(JSONException e) {
			Log.e("no data", "No data found");
		}
		catch(ParseException e) {
			e.printStackTrace();
		}
				
		return result;
	}
	
	private ArrayList<String> getSchools() {
		
		ArrayList<String> result = new ArrayList<String>();
		schools = new ArrayList<School>();
		
		String jsonString = "";
		StringBuilder sb = new StringBuilder();
		InputStream is = null;
				
		//load the JSON into an input stream
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://www.sodaservices.com/HighSchoolHeroes/php/getSchools.php");
			ArrayList<NameValuePair> nvPairs = new ArrayList<NameValuePair>();
			nvPairs.add(new BasicNameValuePair("state", selectedState));
			nvPairs.add(new BasicNameValuePair("city", selectedCity));
			httpPost.setEntity(new UrlEncodedFormEntity(nvPairs));
			HttpResponse response = httpClient.execute(httpPost);			
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch(Exception e) {
			Log.e("log_tag", "Pull From Server" + e.toString());
		}	
		
		// parse the JSON and load it into a string
		// each row should be delimited by \n's
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			
			sb = new StringBuilder();
			sb.append(reader.readLine() + "\n");
			
			String line ="";
			while((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			reader.close();
			is.close();
			jsonString = sb.toString();
		}
		catch (Exception e) {
			Log.e("log_tag", "JSON to String" + e.toString());
		}
		
		// Print the JSON to log for debugging
		Log.d("JSON", jsonString);
		
		JSONArray jArray = new JSONArray();
		try {
			jArray = new JSONArray(jsonString);
		} catch(JSONException e) {
			e.printStackTrace();
		}		
		try {
			JSONObject json_data = new JSONObject();
			
			// iterate through the JSON, adding the data to arrays
			for(int i = 0; i < jArray.length(); i++) {		
				json_data = jArray.getJSONObject(i);
				result.add(json_data.getString("Name"));
				schools.add(new School(json_data.getString("SchoolId"),json_data.getString("Name"),json_data.getString("City"),
							json_data.getString("State"),json_data.getString("ZIP"),json_data.getString("Size")));
			}
		}catch(JSONException e) {
			Log.e("no data", "No data found");
		}
		catch(ParseException e) {
			e.printStackTrace();
		}
		
		// populate the ArrayList with info from the JSON
	//	for(int i = 0; i < jArray.length(); i++)
		
		return result;
	}
}