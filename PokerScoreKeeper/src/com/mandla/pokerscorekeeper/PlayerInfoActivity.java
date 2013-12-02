package com.mandla.pokerscorekeeper;

import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.mandla.pokerscorekeeper.fragments.PlayersFragmentTab;
import com.mandla.pokerscorekeeper.model.Player;

public class PlayerInfoActivity extends SherlockListActivity {

	
	
	private Player player;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_player_info);
		
		// Set action bar display options
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled( false );
		actionBar.setDisplayShowTitleEnabled( true );
		
		
		// Get player and ListView
		Intent intent = getIntent();
		player = (Player) intent.getParcelableExtra( PlayersFragmentTab.PLAYER );
		//ListView listViewInfo = (ListView) findViewById( R.id.player_info );
		
		
		// Retrieve relevant information regarding chosen player
		String[] fromMapKey = new String[] { MainActivity.VALUE, MainActivity.FIELD };
		int[] toLayoutId = new int[] { android.R.id.text1, android.R.id.text2 };
		List<Map<String, String>> list = player.getAttributeMapList();
				
		ListAdapter listAdapter = new SimpleAdapter( this, list, android.R.layout.simple_list_item_2, fromMapKey, toLayoutId );
		setListAdapter( listAdapter );
		
		
		getListView().setOnItemClickListener( new OnItemClickListener() {
			@Override
			public void onItemClick( AdapterView<?> parent, View view, int position, long id ) {
				String field = ((TextView) view.findViewById( android.R.id.text1 )).getText().toString();
				showInfoDialog( field, player );
			}
		});
	}

	private void showInfoDialog( String field, Player p )
	{
		if( field.equals( Player.BETTING_HISTORY ) || field.equals( Player.BALANCE_HISTORY )) {
			AlertDialog.Builder history = new AlertDialog.Builder( this );
			String[] historyInfo = p.getHistory( field );
			history.setTitle( field );
			
			final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, historyInfo );
			history.setAdapter( arrayAdapter, new DialogInterface.OnClickListener() {
				@Override
				public void onClick( DialogInterface dialog, int which )
				{	/* Do nothing */ }
			});
			
			history.setPositiveButton( "Ok", new DialogInterface.OnClickListener() {
				public void onClick( DialogInterface dialog, int which )
				{	/* Do nothing */ }
			});
			
			history.show();
		}
	}
}
