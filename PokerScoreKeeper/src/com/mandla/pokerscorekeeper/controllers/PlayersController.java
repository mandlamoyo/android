package com.mandla.pokerscorekeeper.controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mandla.pokerscorekeeper.model.Mode;
import com.mandla.pokerscorekeeper.model.Player;

public class PlayersController {

	private static final int INIT_OVERDRAFT_MONEY = 0;
	private static final int DEFAULT_KNOCKOUT_MONEY = 100;
	
	private Map<String, Player> players;
	private Mode mode;
	private int[] startMoney;
	
	public PlayersController( Mode m ) {
		mode = m; //Mode.OVERDRAFT;
		players =  new HashMap<String, Player>();
		startMoney = new int[] { INIT_OVERDRAFT_MONEY, DEFAULT_KNOCKOUT_MONEY };
	}
	
	
	public boolean addPlayer( String name ) {
		if( players.get( name ) != null ) return false;
		
		int money = startMoney[mode.ordinal()];
		Player p = new Player( name, money, players.size() );
		players.put( name, p );
		return true;
	}
	
	
	// SETTERS //
	
	public void setMode( Mode m )
	{	mode = m; }
	
	public void setStartMoney( int value )
	{	startMoney[Mode.KNOCKOUT.ordinal()] = value; }

	
	// GETTERS //
	
	public Player getPlayer( String name )
	{	return players.get( name ); }
	
	public List<Map<String, String>> getAttributeMapList( String name ) {
		Player p = players.get( name );
		return p.getAttributeMapList();
	}
	
	public String[] getInfo(String name) {
		return new String[] { name, "£1500", "32" };
	}

	public String[] getPlayerNames() {
		String[] playerNames = new String[players.size()];
		playerNames = players.keySet().toArray( playerNames );
		Arrays.sort( playerNames );
		return playerNames;
	}
}
