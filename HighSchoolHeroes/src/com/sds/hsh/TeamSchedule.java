package com.sds.hsh;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

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
import android.widget.ListView;

public class TeamSchedule extends BaseClass {

	ListView lv_schedule;
	View previousSelection = null;
	ScheduleListViewAdapter scheduleListAdapter;
	ArrayList<ScheduledGame> games;
	String[] opponent, scores, date, location;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team_schedule);
		
		// set ThreadPolicy, this is needed to pull down info from the db on the main thread
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		instantiateComponents();
		games = getSchedule();
		setupListView();
	}
	
	private ArrayList<ScheduledGame> getSchedule() {
		
		ArrayList<ScheduledGame> result = new ArrayList<ScheduledGame>();
		
		String home = "", jsonString = "";
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
			HttpPost httpPost = new HttpPost("http://www.sodaservices.com/HighSchoolHeroes/php/getSchedule.php");
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
		
		scores = new String[jArray.length()];
		opponent = new String[jArray.length()];
		date = new String[jArray.length()];
		location = new String[jArray.length()];
		
		try {
			JSONObject json_data = new JSONObject();
			
			// iterate through the JSON, adding the data to arrays
			home = json_data.getString("School");
			for(int i = 0; i < jArray.length(); i++) {
				json_data = jArray.getJSONObject(i);
				scores[i] = json_data.getString("Score");
				opponent[i] = json_data.getString("Opponent");
				date[i] = json_data.getString("Date");
				location[i] = json_data.getString("Location");
			}
		}catch(JSONException e) {
			Log.e("no data", "No data found");
		}
		catch(ParseException e) {
			e.printStackTrace();
		}
		
		// populate the ArrayList with info from the JSON
		for(int i = 0; i < jArray.length(); i++)
			result.add(new ScheduledGame(home, opponent[i], didHomeTeamWin(i), getHomePoints(i), getAwayPoints(i), getDate(i)));
		
		return result;
	}
	
	private boolean didHomeTeamWin(int index) {
		
		String[] splitScores = scores[index].split("-");
		
		int homeScore = Integer.parseInt(splitScores[0]);
		int awayScore = Integer.parseInt(splitScores[1]);
		
		if(homeScore > awayScore)
			return true;
		else
			return false;
	}
	
	private int getHomePoints(int index) {
		
		String[] splitScores = scores[index].split("-");	
		return Integer.parseInt(splitScores[0]);		
	}
	
	private int getAwayPoints(int index) {
		
		String[] splitScores = scores[index].split("-");	
		return Integer.parseInt(splitScores[1]);
		
	}
	
	private Calendar getDate(int index) {
		
		String thisDate = date[index];	
		Calendar result = Calendar.getInstance();	
		String[] splitDate = thisDate.split("-");
		
		result.set(Integer.parseInt(splitDate[0]), Integer.parseInt(splitDate[1]), Integer.parseInt(splitDate[2]));
		return result;
	}
	
	private void setupListView() {
			
		scheduleListAdapter = new ScheduleListViewAdapter(this, R.layout.schedule_list_view_layout, games);
		lv_schedule.setAdapter(scheduleListAdapter);
		
		lv_schedule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				
				if(previousSelection != null) {
					previousSelection.setBackgroundColor(Color.WHITE);
				}
				
				view.setBackgroundColor(Color.parseColor("#ff00ddff"));
				previousSelection = view;			
			}
		});
	}

	private void instantiateComponents() {
		
		lv_schedule = (ListView)findViewById(R.id.lv_schedule_teamSchedule);
	}
}
