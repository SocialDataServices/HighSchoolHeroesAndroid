package com.sds.hsh;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class PlayerBioDialogFragment extends SherlockDialogFragment {
	
	TextView tv_name, tv_weight, tv_height, tv_position, tv_year, tv_number;
	ImageView iv_playerPic;
		
	public PlayerBioDialogFragment() {
	} // empty constructor
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.dialog_frag_player_bio, container);
		getDialog().setTitle("Player Bio");
		
		tv_name = (TextView)view.findViewById(R.id.tv_playerBio_name);
		tv_weight = (TextView)view.findViewById(R.id.tv_playerBio_weight);
		tv_height = (TextView)view.findViewById(R.id.tv_playerBio_height);
		tv_number = (TextView)view.findViewById(R.id.tv_playerBio_number);
		tv_position = (TextView)view.findViewById(R.id.tv_playerBio_position);
		tv_year = (TextView)view.findViewById(R.id.tv_playerBio_year);
		
		tv_name.setText(getArguments().getString("name"));
		tv_weight.setText("Weight: " + Integer.toString(getArguments().getInt("weight")));
		tv_height.setText("Height: " + getArguments().getString("height"));
		tv_number.setText("# " + Integer.toString(getArguments().getInt("number")));
		tv_position.setText("Position: " + getArguments().getString("position"));
		tv_year.setText("Year: " + getArguments().getString("year"));
		
		return view;
	} // end onCreateView
}