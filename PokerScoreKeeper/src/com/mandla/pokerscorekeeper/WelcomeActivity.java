package com.mandla.pokerscorekeeper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.mandla.pokerscorekeeper.controllers.PlayersController;
import com.mandla.pokerscorekeeper.database.ProfileDataHelper;
import com.mandla.pokerscorekeeper.model.Profile;

public class WelcomeActivity extends Activity {

	private ProfileDataHelper pData;
	private PlayersController pc = PlayersController.getInstance();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		pData = ProfileDataHelper.getInstance( this );
		
	}

	private void deleteProfileDialog() {
		AlertDialog.Builder alert = new AlertDialog.Builder( this );
		
		final Spinner profileSelector = new Spinner( this );
		String[] profileNames = pData.getProfileNames();
		
		ArrayAdapter<String> profileAdapter = new ArrayAdapter<String>( this, android.R.layout.simple_spinner_item, profileNames );
		profileAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
		profileSelector.setAdapter( profileAdapter );
		
		alert.setTitle( getString( R.string.delete_profile ))
		.setMessage( getString( R.string.delete_profile_message ))
		.setView( profileSelector )
		.setPositiveButton( android.R.string.ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick( DialogInterface dialog, int whichButton ) {
				String profileName = profileSelector.getSelectedItem().toString();
				pData.deleteProfile( profileName );
			}
			
		}).setNegativeButton( android.R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick( DialogInterface dialog, int whichButton ) {
				// Do nothing
			}
			
		}).show();
	}
	
	private void loadProfileDialog() {
		AlertDialog.Builder alert = new AlertDialog.Builder( this );
		//String[] playerNames = playersController.getActivePlayerNames();
		
		final Spinner profileSelector = new Spinner( this );
		String[] profileNames = pData.getProfileNames();
		
		ArrayAdapter<String> profileAdapter = new ArrayAdapter<String>( this, android.R.layout.simple_spinner_item, profileNames );
		profileAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
		profileSelector.setAdapter( profileAdapter );
		
		
		alert.setTitle( getString( R.string.load_profile ))
		.setMessage( getString( R.string.select_profile_message ))
		.setView( profileSelector )
		.setPositiveButton( android.R.string.ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick( DialogInterface dialog, int whichButton ) {
				String profileName = profileSelector.getSelectedItem().toString();
				Profile profile = pData.getProfile( profileName );
				pc.loadProfile( profile.players );
				
				Intent showProfile = new Intent( getBaseContext(), MainActivity.class );
				startActivity( showProfile );
			}
			
		}).setNegativeButton( android.R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick( DialogInterface dialog, int whichButton ) {
				// Do nothing
			}
			
		}).show();
	}
	
	// Action Buttons
	public void startNewProfile( View view )
	{
		pc.reset();
		Intent showProfile = new Intent( this, MainActivity.class );
		startActivity( showProfile );
	}
	
	public void loadProfile( View view )
	{	loadProfileDialog(); }
	
	public void showSettings( View view )
	{
		Intent showSettings = new Intent( this, SettingsActivity.class );
		startActivity( showSettings );
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected( MenuItem item ) {
		switch( item.getItemId() ) {
			case R.id.action_delete_profile:
				deleteProfileDialog();
				break;
				
			case R.id.action_settings:
				Toast.makeText( this.getBaseContext(), "Settings" , Toast.LENGTH_LONG ).show();
				break;
				
			default:
				return super.onOptionsItemSelected( item );
				
		}
		return true;
	}
	
}
