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

import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

public class Roster extends BaseClass {

	EditText et_search;
	ArrayList<Player> players;
	RosterListViewAdapter rosterListAdapter;
	int[] playerNumbers, playerWeights;
	String[] playerFirsts, playerLasts, playerYears, playerHeights, playerPositions;
	ListView lv_roster;
	View previousSelection;
	Spinner spin_filters;
	ArrayList<String> filtersList;
	ArrayAdapter<String> filtersSpinnerAdapter;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_roster);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		instantiateComponents();
		players = getRoster();
		setupListView();
		makeFiltersSpinner();
	}
	
	private void makeFiltersSpinner() {
		
		filtersList = new ArrayList<String>();
		filtersList.add("Position");
		filtersList.add("Year");
		filtersList.add("Number");
		filtersList.add("Name");
		
		filtersSpinnerAdapter = new ArrayAdapter<String>(getApplicationContext(),R.drawable.spinner_template,filtersList);
		filtersSpinnerAdapter.setDropDownViewResource(R.drawable.spinner_dropdown_template);
		spin_filters.setAdapter(filtersSpinnerAdapter);
		//add
	}
	
	private void setupListView() {
		
		rosterListAdapter = new RosterListViewAdapter(this, R.layout.roster_list_view_layout, players);
		lv_roster.setAdapter(rosterListAdapter);
		
		lv_roster.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				
				if(previousSelection != null) {
					previousSelection.setBackgroundColor(Color.WHITE);
				}
				
				view.setBackgroundColor(Color.parseColor("#ff00ddff"));
				previousSelection = view;			
			}
		});
	}
	
	private ArrayList<Player> getRoster() {
		
		ArrayList<Player> result = new ArrayList<Player>();
		
		String jsonString = "";
		StringBuilder sb = new StringBuilder();
		InputStream is = null;
		
		//db query 
		ArrayList<NameValuePair> nvPairs = new ArrayList<NameValuePair>();
		nvPairs.add(new BasicNameValuePair("school", "Oxford High School"));
		nvPairs.add(new BasicNameValuePair("sport", "Football"));
		nvPairs.add(new BasicNameValuePair("sex", "0"));
		
		//load the JSON into an input stream
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://www.sodaservices.com/HighSchoolHeroes/php/getRoster.php");
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
		
		playerNumbers = new int[jArray.length()];
		playerWeights = new int[jArray.length()];
		playerFirsts = new String[jArray.length()];
		playerLasts = new String[jArray.length()];
		playerYears = new String[jArray.length()];
		playerHeights = new String[jArray.length()];
		playerPositions = new String[jArray.length()];
		
		try {
			JSONObject json_data = new JSONObject();
			
			// iterate through the JSON, adding the data to arrays
			for(int i = 0; i < jArray.length(); i++) {		
				json_data = jArray.getJSONObject(i);
				playerNumbers[i] = json_data.getInt("Number");
				playerWeights[i] = json_data.getInt("Weight");
				playerFirsts[i] = json_data.getString("FirstName");
				playerLasts[i] = json_data.getString("LastName");
				playerYears[i] = json_data.getString("Year");
				playerHeights[i] = json_data.getString("Height");
				playerPositions[i] = json_data.getString("Position");
			}
		}catch(JSONException e) {
			Log.e("no data", "No data found");
		}
		catch(ParseException e) {
			e.printStackTrace();
		}
		
		// populate the ArrayList with info from the JSON
		for(int i = 0; i < jArray.length(); i++)
			result.add(new Player(playerFirsts[i], playerLasts[i], playerHeights[i], playerWeights[i], playerNumbers[i], playerYears[i], playerPositions[i]));
		
		return result;
	}
	
	private void instantiateComponents() {
		
		lv_roster = (ListView)findViewById(R.id.lv_roster);
		et_search = (EditText)findViewById(R.id.et_roster_search);
		et_search.clearFocus();
		spin_filters = (Spinner)findViewById(R.id.spin_roster_filters);
	}

}
