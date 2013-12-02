package com.mandla.pokerscorekeeper.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.SherlockFragment;
import com.mandla.pokerscorekeeper.R;
import com.mandla.pokerscorekeeper.controllers.GameController;
import com.mandla.pokerscorekeeper.controllers.PlayersController;

public class PotFragmentTab extends SherlockFragment implements OnClickListener {

	private PlayersController playersController;
	private GameController gameController;
	
	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
		View rootView = inflater.inflate( R.layout.fragment_pot, container, false );
		
		Button b_addBet = (Button) rootView.findViewById( R.id.add_bet );
		b_addBet.setOnClickListener( this );
		setHasOptionsMenu( true );
		
		return rootView;
	}
	/*
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
		
		*/
	
	public void setPlayersController( PlayersController pc )
	{	playersController = pc; }

	public void setGameController( GameController gc )
	{	gameController = gc; }
	
	public void addBetDialog( View view ) {
		AlertDialog.Builder alert = new AlertDialog.Builder( getActivity() );
		
		//final Spinner nameList = new Spinner( getActivity() );
		//final Spinner chipList = new Spinner( getActivity() );
		
		View betDialogView = View.inflate( getActivity(), R.layout.add_bet_dialog, null );
		
		final Spinner nameList = (Spinner) betDialogView.findViewById( R.id.name_spinner );
		final Spinner chipList = (Spinner) betDialogView.findViewById( R.id.value_spinner );
		
		String[] playerNames = playersController.getPlayerNames();
		String[] chipValues = gameController.getChipValues();
		
		//String[] testNames = new String[] { "Bobby", "Phil", "Andrew" };
		//String[] testChips = new String[] { "1", "10", "50" };
		
		ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>( getActivity(), android.R.layout.simple_spinner_item, playerNames );
		nameAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
		nameList.setAdapter( nameAdapter );
		
		ArrayAdapter<String> chipAdapter = new ArrayAdapter<String>( getActivity(), android.R.layout.simple_spinner_item, chipValues );
		chipAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
		chipList.setAdapter( chipAdapter );

		/*
		LinearLayout lila1 = new LinearLayout( getActivity() );
		lila1.setOrientation( 0 );
		lila1.addView( nameList );
		lila1.addView( chipList );
		*/
		
		alert.setTitle( getString( R.string.add_bet ))
		//.setMessage( getString( R.string.add_player_dialog_message ))
		.setView( betDialogView )
		.setPositiveButton( android.R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick( DialogInterface dialog, int whichButton ) {
				
				/*final String name = input.getText().toString();
				if( playersList.contains( name )) {
					
				} else {
					if( !playersController.addPlayer( name )) {
						Toast.makeText( getActivity().getBaseContext(), "'" + name + "' is already a player!" , Toast.LENGTH_LONG ).show();
					}
					playersList.add( name );
					((BaseAdapter) getListAdapter()).notifyDataSetChanged();
				}*/
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
		addBetDialog( view );
		
	}
}
