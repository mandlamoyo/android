package com.mandla.pokerscorekeeper;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.mandla.pokerscorekeeper.controllers.GameController;
import com.mandla.pokerscorekeeper.controllers.PlayersController;
import com.mandla.pokerscorekeeper.fragments.PlayersFragmentTab;
import com.mandla.pokerscorekeeper.fragments.PotFragmentTab;
import com.mandla.pokerscorekeeper.model.Mode;

public class MainActivity extends SherlockFragmentActivity {

	public static final String PLAYER_NAME = "com.mandla.pokerscorekeeper.NAME";
	public static final String VALUE = "com.mandla.pokerscorekeeper.VALUE";
	public static final String FIELD = "com.mandla.pokerscorekeeper.FIELD";
	
	private PlayersController pc = new PlayersController( Mode.OVERDRAFT );
	private GameController gc = new GameController();
	
	private ActionBar.Tab tb_players, tb_pot;
	private Fragment fg_players = new PlayersFragmentTab();
	private Fragment fg_pot = new PotFragmentTab();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ActionBar actionBar = getSupportActionBar();
		
		// Set display features/mode
		actionBar.setDisplayShowHomeEnabled( false );
		actionBar.setDisplayShowTitleEnabled( false );
		actionBar.setNavigationMode( ActionBar.NAVIGATION_MODE_TABS );
		
		// Set fragment controllers
		((PlayersFragmentTab) fg_players).setPlayersController( pc );
		//((PlayersFragmentTab) fg_players).setGameController( gc );
		
		((PotFragmentTab) fg_pot).setPlayersController( pc );
		((PotFragmentTab) fg_pot).setGameController( gc );
		
		
		// Set Tab icons/titles
		tb_players = actionBar.newTab().setText( "Players" );
		tb_pot = actionBar.newTab().setText( "Pot" );
		
		// Set Tab Listeners
		tb_players.setTabListener( new TabListener( fg_players ));
		tb_pot.setTabListener( new TabListener( fg_pot ));
		
		// Add Tabs to ActionBar
		actionBar.addTab( tb_players );
		actionBar.addTab( tb_pot );
	}

	/*
	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {
		//MenuInflater inflater = getSupportMenuInflater();
		//inflater.inflate( R.menu.main, menu );
		return true;
	}*/
}
