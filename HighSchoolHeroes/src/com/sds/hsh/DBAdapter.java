package com.sds.hsh;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
 
public class DBAdapter {
	private static final String TAG = "DBAdapter";
	private static final String DATABASE_NAME = "HighSchoolHeroesDB";
	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_CREATE = 
			"create table SCHOOLS_DB(schoolID string, " +
			     "schoolName string, schoolCity string, schoolState string, schoolZip string, schoolSize string);";	
	private final Context context;
	protected DatabaseHelper DBHelper;
	protected static SQLiteDatabase db;
	
	public DBAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}
	
	private class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				db.execSQL(DATABASE_CREATE);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to " +
					newVersion + ", which will destroy all old data");
			
			onCreate(db);
		}
	}
		
	public DBAdapter open() throws SQLException {
		
		db = DBHelper.getWritableDatabase();
			return this;
	}
		
	public void close() {
		DBHelper.close();
	}
	
	public void deleteSchoolByName(String name) {
		
		db.delete("SCHOOLS_DB", "schoolName='" + name + "'", null);
	}
	
	public boolean schoolNotInDB(String id) {
		
		if(db.query("SCHOOLS_DB", new String[] {"schoolID"}, "schoolID='" + id + "'", null, null, null, null).getCount() == 0)
			return true;
		else
			return false;
	
	}
	
	public void addSchool(String id, String name, String city, String state, String zip, String size) {
		
		ContentValues newSchool = new ContentValues();
		newSchool.put("schoolID", id);
		newSchool.put("schoolName", name);
		newSchool.put("schoolCity", city);
		newSchool.put("schoolState", state);
		newSchool.put("schoolZip", zip);
		newSchool.put("schoolSize", size);
		db.insert("SCHOOLS_DB", null, newSchool);
	}
	
	public ArrayList<School> getSchools() {
		
		ArrayList<School> result = new ArrayList<School>();
		Cursor c = db.query("SCHOOLS_DB", new String[] {"schoolID", "schoolName", "schoolCity", "schoolState", "schoolZip", "schoolSize",}, 
				null, null, null, null, "schoolID asc");
		
		for(int i = 0; i < c.getCount(); i++) {
			c.moveToPosition(i);
			School s = new School(c.getString(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5));
			result.add(s);
		}
		return result;
	}
	
			
}