package com.mandla.pokerscorekeeper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mandla.pokerscorekeeper.database.ProfileDataHelper;

public class WelcomeActivity extends Activity {

	private ProfileDataHelper pData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		pData = ProfileDataHelper.getInstance( this );
		
	}

	public void loadProfileDialog( View view ) {
		AlertDialog.Builder alert = new AlertDialog.Builder( this );
		//String[] playerNames = playersController.getActivePlayerNames();
		
		//final Spinner winnerSelector = new Spinner( getSherlockActivity() );
		String[] playerNames = pData.getProfileNames();
		final ListView playerList = new ListView( this );
		
		ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, playerNames );
		playerList.setAdapter( nameAdapter );
		
		alert.setTitle( getString( R.string.load_profile ))
		.setMessage( getString( R.string.select_profile_message ))
		.setView( playerList )
		.setPositiveButton( android.R.string.ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick( DialogInterface dialog, int whichButton ) {
				// Do something
			}
		}).show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome, menu);
		return true;
	}

	public void startNewProfile( View view )
	{
		Intent showProfile = new Intent( this, MainActivity.class );
		startActivity( showProfile );
	}
	
	public void loadProfile( View view )
	{
		loadProfileDialog( view );
	}
	
	public void showSettings( View view )
	{
		Intent showSettings = new Intent( this, SettingsActivity.class );
		startActivity( showSettings );
	}
}
