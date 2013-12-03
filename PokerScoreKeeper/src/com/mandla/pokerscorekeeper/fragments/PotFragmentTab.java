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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.mandla.pokerscorekeeper.R;
import com.mandla.pokerscorekeeper.controllers.GameController;
import com.mandla.pokerscorekeeper.controllers.PlayersController;
import com.mandla.pokerscorekeeper.model.Mode;
import com.mandla.pokerscorekeeper.model.Player;

public class PotFragmentTab extends SherlockFragment implements OnClickListener {

	private PlayersController playersController;
	private GameController gameController;
	
	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
		View rootView = inflater.inflate( R.layout.fragment_pot, container, false );
		
		Button b_addBet = (Button) rootView.findViewById( R.id.add_bet );
		Button b_endRound = (Button) rootView.findViewById( R.id.end_round );
		
		b_addBet.setOnClickListener( this );
		b_endRound.setOnClickListener( this );
		
		setHasOptionsMenu( true );
		return rootView;
	}
	
	// Don't set item values until the activity has finished drawing everything
	@Override
	public void onActivityCreated( Bundle savedInstanceState ) {
		super.onActivityCreated( savedInstanceState );
		updateDisplay();
	}

		
	
	public void setPlayersController( PlayersController pc )
	{	playersController = pc; }

	public void setGameController( GameController gc )
	{	gameController = gc; }
	
	public void addBetDialog( View view ) {
		AlertDialog.Builder alert = new AlertDialog.Builder( getSherlockActivity() );
		View betDialogView = View.inflate( getSherlockActivity(), R.layout.add_bet_dialog, null );
		
		final Spinner nameList = (Spinner) betDialogView.findViewById( R.id.name_spinner );
		final EditText chipValue = (EditText) betDialogView.findViewById( R.id.value_edit );
		final ToggleButton foldButton = (ToggleButton) betDialogView.findViewById( R.id.fold_toggle );
		
		foldButton.setOnCheckedChangeListener( new ToggleButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged( CompoundButton buttonView, boolean isChecked ) {
				chipValue.setText( "" );
				chipValue.setEnabled( !isChecked );
			}
		});
		
		
		String[] playerNames = playersController.getActivePlayerNames();	
		ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>( getSherlockActivity(), android.R.layout.simple_spinner_item, playerNames );
		nameAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
		nameList.setAdapter( nameAdapter );

		
		alert.setTitle( getString( R.string.add_bet ))
		//.setMessage( getString( R.string.add_player_dialog_message ))
		.setView( betDialogView )
		.setPositiveButton( android.R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick( DialogInterface dialog, int whichButton ) {
				String name = nameList.getSelectedItem().toString();
				Player p = playersController.getPlayer( name );
				
				if( foldButton.isChecked() ) {
					Toast.makeText( getSherlockActivity().getBaseContext(), name + " folded" , Toast.LENGTH_LONG ).show();
					p.fold();
					
				} else {
					String bet = chipValue.getText().toString();
					Mode gameMode = playersController.getMode();

					int iBet = Integer.parseInt( bet );
					int minBet = gameController.getMinBet();
					int maxBet = gameController.getMaxBet();
					int balance = Integer.parseInt( p.getAttribute( Player.BALANCE ));
					int overdraftLimit = Integer.parseInt( p.getAttribute( Player.OVERDRAFT_LIMIT ));
					
					if( iBet < minBet || iBet > maxBet ) {
						Toast.makeText( getSherlockActivity().getBaseContext(), minBet + " <= bet <= " + maxBet, Toast.LENGTH_LONG ).show();
						
					} else if(( !p.hasFunds( iBet ) && gameMode == Mode.KNOCKOUT ) || ( balance - iBet < -overdraftLimit )) {
						Toast.makeText( getSherlockActivity().getBaseContext(), "Insufficient funds!" , Toast.LENGTH_LONG ).show();
						 
					} else {
						gameController.setLastBet( iBet );
						gameController.addToPot( iBet );
						updateDisplay();
						p.addBet( bet );
					}
				}
			}
		
		}).setNegativeButton( android.R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick( DialogInterface dialog, int whichButton ) {
				// Cancelled
			}
		
		}).show();
	}

	public void endRoundDialog( View view ) {
		AlertDialog.Builder alert = new AlertDialog.Builder( getSherlockActivity() );
		String[] playerNames = playersController.getActivePlayerNames();
		
		final Spinner winnerSelector = new Spinner( getSherlockActivity() );
			
		ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>( getSherlockActivity(), android.R.layout.simple_spinner_item, playerNames );
		nameAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
		winnerSelector.setAdapter( nameAdapter );
		
		alert.setTitle( getString( R.string.round_over ))
		.setMessage( getString( R.string.end_round_dialog_message ))
		.setView( winnerSelector )
		.setPositiveButton( android.R.string.ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick( DialogInterface dialog, int whichButton ) {
				String winner = winnerSelector.getSelectedItem().toString();
				playersController.endRound( winner, gameController.getPot() );
				gameController.endRound();
				updateDisplay();
			}
		}).show();
	}
	
	public void updateDisplay() {
		
		LinearLayout potLayout = (LinearLayout) getSherlockActivity().findViewById( R.id.pot_view );
		TextView potDisplay = (TextView) potLayout.findViewById( R.id.pot_value );
		potDisplay.setText( String.valueOf( gameController.getPot() ));
		
		LinearLayout lastBetLayout = (LinearLayout) getSherlockActivity().findViewById( R.id.last_bet_view );
		TextView lastBetDisplay = (TextView) lastBetLayout.findViewById( R.id.last_bet_value );
		lastBetDisplay.setText( String.valueOf( gameController.getLastBet() ));
	}

	@Override
	public void onClick( View view ) {
		switch( view.getId() ) {
			case R.id.add_bet:
				addBetDialog( view );
				break;
				
			case R.id.end_round:
				endRoundDialog( view );
				break;
		}
		
	}
	
	@Override
	public boolean onOptionsItemSelected( MenuItem item ) {
		switch( item.getItemId() ) {
			case R.id.action_add_bet:
				Toast.makeText( getActivity().getBaseContext(), "Add Bet" , Toast.LENGTH_LONG ).show();
				break;
				
			case R.id.action_end_round:
				Toast.makeText( getActivity().getBaseContext(), "End Round" , Toast.LENGTH_LONG ).show();
				break;
				
			default:
				return super.onOptionsItemSelected( item );
				
		}
		return true;
	}
	
	public void onCreateOptionsMenu( Menu menu, MenuInflater inflater ) {
		inflater.inflate( R.menu.pot, menu );
		super.onCreateOptionsMenu( menu, inflater );
	}
}
