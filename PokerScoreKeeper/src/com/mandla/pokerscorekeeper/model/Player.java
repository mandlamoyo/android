package com.mandla.pokerscorekeeper.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

import com.mandla.pokerscorekeeper.MainActivity;

public class Player implements Parcelable {
	private static final String EMPTY_STRING = "";
	
	public static final String BALANCE = "Balance";
	public static final String BETTING_HISTORY = "Betting History";
	public static final String BALANCE_HISTORY = "Balance History";
	
	public static final String[] FIELDS = new String[] { "Name", "Balance", "Rank", "Folds", "Wins", "Losses", "Betting History", "Balance History" };
	
	
	private ArrayList<String> history_bets;
	private ArrayList<String> history_balance;
	
	private Map<String, String> attributes;
	
	
	public Player( String name, int balance, int playerCount ) {
		String[] initValues = new String[] { 
				name,
				String.valueOf( balance ),
				String.valueOf( playerCount + 1 ),
				"0",
				"0",
				"0",
				EMPTY_STRING,
				EMPTY_STRING 
		};
		
		initialize( initValues);
	}
	
	public Player( String[] initValues )
	{	initialize( initValues ); }
	
	private void initialize( String[] values )
	{
		history_bets = new ArrayList<String>();
		history_balance = new ArrayList<String>();
		
		attributes = new HashMap<String, String>();
		setAttributes( values );
	}

	public void addBet( String bet )
	{	history_bets.add( bet ); }
	
	// SETTERS
	public void setAttribute( String field, String value )
	{	
		if( field.equals( BALANCE )) history_balance.add( value );
		if( field.equals( BALANCE_HISTORY )) value = EMPTY_STRING;
		if( field.equals( BETTING_HISTORY )) {
			history_bets.add( value );
			value = EMPTY_STRING;
		}
		
		attributes.put( field, value );
	}
	
	public void setAttribute( String field, int value )
	{	setAttribute( field, String.valueOf( value )); }
	
	public void changeField( String field, String change )
	{	setAttribute( field, change ); }
	
	public void changeField( String field, int change )
	{
		int current = Integer.parseInt( attributes.get( field ));
		setAttribute( field, String.valueOf( current + change ));
	}
	
	public void setAttributes( String[] values )
	{
		for( int i=0; i<FIELDS.length; i++ ) {
			String value = ( values == null ) ? EMPTY_STRING : values[i];
			setAttribute( FIELDS[i], value );
		}
	}
	
	// GETTERS
	public String getAttribute( String field ) 
	{	return attributes.get( field ); }
	
	public String[] getAttributes()
	{	
		String[] values = new String[FIELDS.length];
		for( int i=0; i<FIELDS.length; i++ ) {
			values[i] = getAttribute( FIELDS[i] );
		}
		
		return values;
	}
	
	public List<Map<String, String>> getAttributeMapList() {
		List<Map<String, String>> AttributeMapList = new ArrayList<Map<String, String>>( FIELDS.length );
		
		for( String field : FIELDS ) {
			Map<String, String> valueMap = new HashMap<String, String>();
			String valueLabel = ( field.endsWith( "History" )) ? field : getAttribute( field );
			String fieldLabel = ( field.endsWith( "History" )) ? EMPTY_STRING : field;
			
			valueMap.put( MainActivity.VALUE, valueLabel );
			valueMap.put( MainActivity.FIELD, fieldLabel );
			AttributeMapList.add( valueMap );
		}
		
		return AttributeMapList;
	}


	//-------------------------- PARCELABLE --------------------------//
	
	public static final Parcelable.Creator<Player> CREATOR = new Parcelable.Creator<Player>() {
		public Player createFromParcel( Parcel in ) {
			return new Player( in.createStringArray() );
		}
		
	public Player[] newArray( int size ) {
			return new Player[size];
		}
	};
	
	@Override
	public int describeContents() {
		return 0;
	}


	@Override
	public void writeToParcel( Parcel out, int flags ) {
		out.writeStringArray( getAttributes() );
	}

	public String[] getHistory( String type ) {
		ArrayList<String> chosenHistory = ( type.equals( BETTING_HISTORY )) ? history_bets : history_balance;
		String[] history = new String[chosenHistory.size()];
		history = chosenHistory.toArray( history );
		return history;
	}
}
