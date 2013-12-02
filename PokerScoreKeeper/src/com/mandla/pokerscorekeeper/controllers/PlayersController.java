package com.mandla.pokerscorekeeper.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mandla.pokerscorekeeper.model.Mode;
import com.mandla.pokerscorekeeper.model.Player;
import com.mandla.pokerscorekeeper.model.PlayerRankComparator;

public class PlayersController {

	private static final int INIT_OVERDRAFT_MONEY = 0;
	private static final int DEFAULT_KNOCKOUT_MONEY = 100;
	
	private Map<String, Player> players;
	private Mode mode;
	private int[] startMoney;
	
	public ArrayList<String> playerNames;
	
	public PlayersController( Mode m ) {
		mode = m; //Mode.OVERDRAFT;
		players =  new HashMap<String, Player>();
		playerNames = new ArrayList<String>();
		startMoney = new int[] { INIT_OVERDRAFT_MONEY, DEFAULT_KNOCKOUT_MONEY };
	}
	
	
	public void addPlayer( String name ) {
		int money = startMoney[mode.ordinal()];
		Player p = new Player( name, money, players.size() );
		//playerNames.add( name );
		players.put( name, p );
		updatePlayerNameList();
	}
	
	
	public void endRound( String winnerName, int pot ) {
		Player winner = players.get( winnerName );
		
		// Update winner's balance
		winner.changeAttribute( Player.BALANCE, pot );
		
		for( Player p : players.values() ) {
			// Update all active player's wins/losses/balance history
			if( p.isActive() ) {
				String field = ( p.getAttribute( Player.NAME ).equals( winnerName )) ? Player.WINS : Player.LOSSES;
				p.changeAttribute( field, 1 );
				p.updateBalanceHistory();
				
			// Set all players to active
			} else {
				p.setActive();
			}
		}
		
		// Update player rankings
		updateRankings();
	}
	
	// SETTERS //
	
	public void setMode( Mode m )
	{	mode = m; }
	
	public void setStartMoney( int value )
	{	startMoney[Mode.KNOCKOUT.ordinal()] = value; }

	
	// GETTERS //
	
	public Mode getMode()
	{	return mode; }
	
	public Player getPlayer( String name )
	{	return players.get( name ); }
	
	public List<Map<String, String>> getAttributeMapList( String name ) {
		Player p = players.get( name );
		return p.getAttributeMapList();
	}
	
	public void updateRankings() {
		HashMap<Integer,String> balances = new HashMap<Integer,String>();
		
		// Get player balances
		for( Player p : players.values() ) {
			balances.put( Integer.parseInt( p.getAttribute( Player.BALANCE )), p.getAttribute( Player.NAME ));
		}
		
		// Update player rankings
		Set<Integer> bSet = balances.keySet();
		ArrayList<Integer> bList = new ArrayList<Integer>();
		bList.addAll( bSet );
		Collections.sort( bList );
		Collections.reverse( bList );
		
		for( int i=0; i<bList.size(); i++ ) {
			Player p = players.get( balances.get( bList.get( i )));
			p.setAttribute( Player.RANK, i+1 );
		}
	}

	public void updatePlayerNameList() {
		playerNames.clear();
		ArrayList<Player> pList = new ArrayList<Player>();
		pList.addAll( players.values() );
		Collections.sort( pList, new PlayerRankComparator() );
		
		for( int i=0; i<players.size(); i++ ) {
			playerNames.add( pList.get( i ).getAttribute( Player.NAME ));
		}
	}
	
	/*
	public ArrayList<String> getPlayerNames() {
		ArrayList<Player> pList = new ArrayList<Player>();
		ArrayList<String> playerNames = new ArrayList<String>();
		
		pList.addAll( players.values() );
		Collections.sort( pList, new PlayerRankComparator() );
		
		for( int i=0; i<players.size(); i++ ) {
			playerNames.add( pList.get( i ).getAttribute( Player.NAME ));
		}
		
		return playerNames;
	}*/
	
	public String[] getActivePlayerNames() {
		ArrayList<String> nameList = new ArrayList<String>();
		
		for( Player p : players.values() ) {
			if( p.isActive() ) nameList.add( p.getAttribute( Player.NAME ));
		}
		
		String[] playerNames = new String[nameList.size()];
		playerNames = nameList.toArray( playerNames );
		Arrays.sort( playerNames );
		return playerNames;
	}
}
