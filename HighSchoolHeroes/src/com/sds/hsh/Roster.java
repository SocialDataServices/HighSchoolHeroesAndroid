package com.sds.hsh;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

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
	TextView tv_nameHeader, tv_weightHeader, tv_heightHeader, tv_positionHeader, tv_yearHeader, tv_numberHeader;
	boolean nameInAscending = false, weightInAscending = false, heightInAscending = false, positionInAscending = false, yearInAscending = false, 
			numberInAscending = false;
	
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
	
	private void sortByHeightAscending() {
		Collections.sort(players, new AscendingHeightComparer());
		rosterListAdapter.notifyDataSetChanged();
	}
	
	private void sortByHeightDescending() {
		Collections.sort(players, new DescendingHeightComparer());
		rosterListAdapter.notifyDataSetChanged();
	}
	
	private void sortByWeightAscending() {
		Collections.sort(players, new AscendingWeightComparer());
		rosterListAdapter.notifyDataSetChanged();
	}
	
	private void sortByWeightDescending() {
		Collections.sort(players, new DescendingWeightComparer());
		rosterListAdapter.notifyDataSetChanged();
	}
	
	private void sortByPositionAscending() {
		
	}
	
	private void sortByPositionDescending() {
		
	}

	private void sortByYearAscending() {
		Collections.sort(players, new AscendingYearComparer());
		rosterListAdapter.notifyDataSetChanged();
	}
	
	private void sortByYearDescending() {
		Collections.sort(players, new DescendingYearComparer());
		rosterListAdapter.notifyDataSetChanged();
	}
	
	private void sortByNumberAscending() {
		Collections.sort(players, new AscendingNumberComparer());
		rosterListAdapter.notifyDataSetChanged();
	}
	
	private void sortByNumberDescending() {
		Collections.sort(players, new DescendingNumberComparer());
		rosterListAdapter.notifyDataSetChanged();
	}
	
	private void sortByNameAscending() {
		Collections.sort(players, new AscendingNameComparer());
		rosterListAdapter.notifyDataSetChanged();
	}
	
	private void sortByNameDescending() {
		Collections.sort(players, new DescendingNameComparer());
		rosterListAdapter.notifyDataSetChanged();
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
		
		spin_filters.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
				switch(pos) {
					case 0:
						sortByPositionAscending();
						break;
					case 1:
						sortByYearAscending();
						break;
					case 2:
						sortByNumberAscending();
						break;
					case 3:
						sortByNameAscending();
						break;
				}
				
			}
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
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
	
	private void setAllSortBooleansFalse() {
		
		nameInAscending = false;
		heightInAscending = false;
		weightInAscending = false;
		numberInAscending = false;
		positionInAscending = false;
		yearInAscending = false;
	}
	
	private void setBooleansForNameAscending() {
		
		nameInAscending = true;
		heightInAscending = false;
		weightInAscending = false;
		numberInAscending = false;
		positionInAscending = false;
		yearInAscending = false;
	}
	
	private void setBooleansForHeightAscending() {
		
		nameInAscending = false;
		heightInAscending = true;
		weightInAscending = false;
		numberInAscending = false;
		positionInAscending = false;
		yearInAscending = false;
	}

	private void setBooleansForWeightAscending() {
	
	nameInAscending = false;
	heightInAscending = false;
	weightInAscending = true;
	numberInAscending = false;
	positionInAscending = false;
	yearInAscending = false;
}

	private void setBooleansForNumberAscending() {
	
	nameInAscending = false;
	heightInAscending = false;
	weightInAscending = false;
	numberInAscending = true;
	positionInAscending = false;
	yearInAscending = false;
}

	private void setBooleansForPositionAscending() {
	
	nameInAscending = false;
	heightInAscending = false;
	weightInAscending = false;
	numberInAscending = false;
	positionInAscending = true;
	yearInAscending = false;
}

	private void setBooleansForYearAscending() {
	
	nameInAscending = false;
	heightInAscending = false;
	weightInAscending = false;
	numberInAscending = false;
	positionInAscending = false;
	yearInAscending = true;
}
	
	public OnClickListener columnHeaderListener = new OnClickListener() {
		public void onClick(View v) {
			
			switch(v.getId()) {
				case R.id.tv_roster_header_name:
					if(nameInAscending) {
						sortByNameDescending();
						setAllSortBooleansFalse();
					}
					else {
						sortByNameAscending();
						setBooleansForNameAscending();
					}
					break;
				case R.id.tv_roster_header_height:
					if(heightInAscending) {
						sortByHeightDescending();
						setAllSortBooleansFalse();
					}
					else {
						sortByHeightAscending();
						setBooleansForHeightAscending();
					}
					break;
				case R.id.tv_roster_header_number:
					if(numberInAscending) {
						sortByNumberDescending();
						setAllSortBooleansFalse();
					}
					else {
						sortByNumberAscending();
						setBooleansForNumberAscending();
					}
					break;
				case R.id.tv_roster_header_weight:
					if(weightInAscending) {
						sortByWeightDescending();
						setAllSortBooleansFalse();
					}
					else {
						sortByWeightAscending();
						setBooleansForWeightAscending();
					}
					break;
				case R.id.tv_roster_header_position:
					if(positionInAscending) {
						sortByPositionDescending();
						setAllSortBooleansFalse();
					}
					else {
						sortByPositionAscending();
						setBooleansForPositionAscending();
					}
					break;
				case R.id.tv_roster_header_year:
					if(yearInAscending) {
						sortByYearDescending();
						setAllSortBooleansFalse();
					}
					else {
						sortByYearAscending();
						setBooleansForYearAscending();
					}
					break;	
			}
		}
	};
	
	private void instantiateComponents() {
		
		lv_roster = (ListView)findViewById(R.id.lv_roster);
		et_search = (EditText)findViewById(R.id.et_roster_search);
		et_search.clearFocus();
		spin_filters = (Spinner)findViewById(R.id.spin_roster_filters);
		tv_nameHeader = (TextView)findViewById(R.id.tv_roster_header_name);
		tv_weightHeader = (TextView)findViewById(R.id.tv_roster_header_weight);
		tv_heightHeader = (TextView)findViewById(R.id.tv_roster_header_height);
		tv_positionHeader = (TextView)findViewById(R.id.tv_roster_header_position);
		tv_yearHeader = (TextView)findViewById(R.id.tv_roster_header_year);
		tv_numberHeader = (TextView)findViewById(R.id.tv_roster_header_number);
		
		tv_nameHeader.setOnClickListener(columnHeaderListener);
		tv_weightHeader.setOnClickListener(columnHeaderListener);
		tv_heightHeader.setOnClickListener(columnHeaderListener);
		tv_positionHeader.setOnClickListener(columnHeaderListener);
		tv_yearHeader.setOnClickListener(columnHeaderListener);
		tv_numberHeader.setOnClickListener(columnHeaderListener);
	}

}
