package com.mandla.pokerscorekeeper;

import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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
		player.setHistory( Player.BETTING_HISTORY, intent.getStringArrayExtra( PlayersFragmentTab.BETTING_HISTORY ));
		player.setHistory( Player.BALANCE_HISTORY, intent.getStringArrayExtra( PlayersFragmentTab.BALANCE_HISTORY ));
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
			final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1 ) {
				@Override
				public View getView( int position, View convertView, ViewGroup parent ) {
					TextView textView = (TextView ) super.getView( position, convertView, parent );

					int textColor = R.color.abs__primary_text_holo_light;
					textView.setTextColor( PlayerInfoActivity.this.getResources().getColor( textColor ));
					return textView;
				}
			};
			
			AlertDialog.Builder history = new AlertDialog.Builder( this );
			String[] historyInfo = p.getHistory( field );
			
			for( String h : historyInfo ) {
				arrayAdapter.add( h );
			}
			
			history.setTitle( field )
			.setAdapter( arrayAdapter, new DialogInterface.OnClickListener() {
				@Override
				public void onClick( DialogInterface dialog, int which )
				{	/* Do nothing */ }
				
			}).setPositiveButton( "Ok", new DialogInterface.OnClickListener() {
				public void onClick( DialogInterface dialog, int which )
				{	/* Do nothing */ }
				
			}).show();
		}
	}
}
