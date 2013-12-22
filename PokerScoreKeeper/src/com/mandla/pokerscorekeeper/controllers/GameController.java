package com.mandla.pokerscorekeeper.controllers;


public class GameController {
	private static final int ABS_MIN_BET = 1;
	private static final int ABS_MAX_BET = 1000000;
	
	private static GameController gcInstance;
	
	private int pot;
	private int betMin;
	private int betMax;
	private int lastBet;
	
	private GameController()
	{
		pot = 0;
		betMin = 1;
		betMax = 1000;
		lastBet = 0;
	}

	public static GameController getInstance() {
		if( gcInstance == null ) gcInstance = new GameController();
		return gcInstance;
	}
	
	public void reset()
	{
		pot = 0;
		betMin = 1;
		betMax = 1000;
		lastBet = 0;
	}
	
	public void endRound()
	{
		pot = 0;
		lastBet = 0;
	}
	
	public void addToPot(int iBet)
	{	pot += iBet; }

	//--- GETTERS ---//
	public int getPot()
	{	return pot; }
	
	public int getMinBet()
	{	return betMin; }
	
	public int getMaxBet()
	{	return betMax; }
	
	public int getLastBet()
	{	return lastBet; }
	
	//--- SETTERS ---//
	public void setMinBet( int newMin )
	{	betMin = Math.max( ABS_MIN_BET, newMin ); }
	
	public void setMaxBet( int newMax )
	{	betMax = Math.min( ABS_MAX_BET, newMax ); }
	
	public void setLastBet( int newLast )
	{	lastBet = newLast; }
}
