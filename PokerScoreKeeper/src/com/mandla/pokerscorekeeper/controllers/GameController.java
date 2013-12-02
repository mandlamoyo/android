package com.mandla.pokerscorekeeper.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GameController {
	private static final int[] DEFAULT_CHIPS = new int[] {1, 5, 10, 25, 50, 100 };
	private static final String FOLD = "Fold";
	
	private Map<Integer, Boolean> chips;
	
	public GameController()
	{
		chips = new HashMap<Integer, Boolean>();
		for( int i : DEFAULT_CHIPS ) {
			chips.put( i, true );
		}
	}
	
	public void addChipValue( String value )
	{	chips.put( Integer.parseInt( value ), true ); }
	
	public void removeChipValue( String value )
	{	if( chips.get( Integer.parseInt( value )) != null ) chips.put( Integer.parseInt( value ), false ); }
	
	public String[] getChipValues()
	{
		String[] chipValues = new String[chips.size()];
		ArrayList<Integer> int_chipValues = new ArrayList<Integer>(chips.size());
		
		int_chipValues.addAll( chips.keySet() );
		Collections.sort( int_chipValues );
		
		for( int i=0; i<int_chipValues.size(); i++ ) {
			chipValues[i] = String.valueOf( int_chipValues.get( i ));
		}

		return chipValues;
	}
}
