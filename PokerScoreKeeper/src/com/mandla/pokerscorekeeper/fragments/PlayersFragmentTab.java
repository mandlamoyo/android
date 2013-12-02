package com.mandla.pokerscorekeeper.fragments;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.mandla.pokerscorekeeper.PlayerInfoActivity;
import com.mandla.pokerscorekeeper.R;
import com.mandla.pokerscorekeeper.controllers.GameController;
import com.mandla.pokerscorekeeper.controllers.PlayersController;
import com.mandla.pokerscorekeeper.model.Player;

public class PlayersFragmentTab extends SherlockListFragment implements OnClickListener {
	
	public static final String PLAYER = "com.mandla.pokerscorekeeper.fragments.PLAYER";
	
	private PlayersController playersController;
	private GameController gameController;
	//private PlayersController playersController = new PlayersController( Mode.OVERDRAFT );
	private ArrayList<String> playersList = new ArrayList<String>();
	
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
		View rootView = inflater.inflate( R.layout.fragment_players, container, false );
		//String[] playersList = new String[] {"Josh", "Dave", "Mike", "Ian" };
		setListAdapter( new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, playersList ));
		
		Button b_addPlayer = (Button) rootView.findViewById( R.id.add_player );
		b_addPlayer.setOnClickListener( this );
		setHasOptionsMenu( true );
		
		return rootView;
	}
	
	public void setPlayersController( PlayersController pc )
	{	playersController = pc; }

	public void setGameController( GameController gc )
	{	gameController = gc; }
	
	// Don't let items be clicked until the activity has finished drawing everything
	@Override
	public void onActivityCreated( Bundle savedInstanceState ) {
		super.onActivityCreated( savedInstanceState );
		getListView().setOnItemClickListener( new OnItemClickListener() {
			@Override
			public void onItemClick( AdapterView<?> parent, View view, int position, long id ) {
				Intent showPlayerInfo = new Intent( getActivity(), PlayerInfoActivity.class );
				String name = ((TextView) view).getText().toString();
				Player p = playersController.getPlayer( name );
				showPlayerInfo.putExtra( PLAYER, p );
				startActivity( showPlayerInfo );
			}
		});
	}
	
	
	public void addPlayerDialog( View view ) {
		AlertDialog.Builder alert = new AlertDialog.Builder( getActivity() );
		final EditText input = new EditText( getActivity() );
		
		alert.setTitle( getString( R.string.add_player ))
		.setMessage( getString( R.string.add_player_dialog_message ))
		.setView( input )
		.setPositiveButton( android.R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick( DialogInterface dialog, int whichButton ) {
				final String name = input.getText().toString();
				if( playersList.contains( name )) {
					
				} else {
					if( !playersController.addPlayer( name )) {
						Toast.makeText( getActivity().getBaseContext(), "'" + name + "' is already a player!" , Toast.LENGTH_LONG ).show();
					}
					playersList.add( name );
					((BaseAdapter) getListAdapter()).notifyDataSetChanged();
				}
			}
		
		}).setNegativeButton( android.R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick( DialogInterface dialog, int whichButton ) {
				// Cancelled
			}
		
		}).show();
	}

	@Override
	public void onClick( View view ) {
		addPlayerDialog( view );
		
	}
	
	
	public void onCreateOptionsMenu( Menu menu, MenuInflater inflater ) {
		inflater.inflate( R.menu.players, menu );
		super.onCreateOptionsMenu( menu, inflater );
	}
}
