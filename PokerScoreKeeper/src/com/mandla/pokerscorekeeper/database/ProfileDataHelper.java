package com.mandla.pokerscorekeeper.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mandla.pokerscorekeeper.model.Player;
import com.mandla.pokerscorekeeper.model.Profile;
import com.mandla.pokerscorekeeper.model.State;

/* SINGLETON */
public class ProfileDataHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "pokerscorekeeper.db";
	
	private static final String OVERDRAFT_LIMIT = "overdraft";
	private static final String GAME_ID = "gameId";
	private static final String GAME_NAME = "gameName";
	private static final String POT = "pot";
	private static final String LAST_BET = "lastBet";
	
	//public static final String ID_COL = BaseColumns._ID;

	public static final String TABLE_PLAYERS = "players";
	public static final String TABLE_STATES = "state";
	
	private static final String KEY_ID = "id";
	//private static final String[] PLAYER_COLUMNS = { KEY_ID, Player.NAME, Player.BALANCE, Player.RANK, Player.WINS, Player.LOSSES, Player.FOLDS, OVERDRAFT_LIMIT, Player.ACTIVE, GAME_ID };
	private static final String CREATE_PLAYER_TABLE = "CREATE TABLE " + TABLE_PLAYERS + " ( " +
														KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
														Player.NAME + " TEXT, " + 
														Player.BALANCE + " INTEGER, " + 
														Player.RANK + " INTEGER, " + 
														Player.WINS + " INTEGER, " + 
														Player.LOSSES + " INTEGER, " +
														Player.FOLDS + " INTEGER, " +
														OVERDRAFT_LIMIT + " INTEGER, " +
														Player.ACTIVE + " INTEGER, " +
														GAME_ID + " INTEGER )";
	
	//private static final String[] STATE_COLUMNS = { GAME_ID, GAME_NAME, POT, LAST_BET };
	private static final String CREATE_STATE_TABLE = "CREATE TABLE " + TABLE_STATES + " ( " +
														GAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
														GAME_NAME + " TEXT, " +
														POT + " INTEGER, " + 
														LAST_BET + " INTEGER )";
	
	private static ProfileDataHelper dbInstance;
	
	private ProfileDataHelper( Context context ) {
		super( context, DATABASE_NAME, null, DATABASE_VERSION );
	}

	public static ProfileDataHelper getInstance( Context context ) {
		if( dbInstance == null ) dbInstance = new ProfileDataHelper( context.getApplicationContext() );
		return dbInstance;
	}
	
	public boolean gameExists( String name ) {
		SQLiteDatabase db = this.getReadableDatabase();
		
		String query = "SELECT * FROM " + TABLE_STATES + " WHERE " + GAME_NAME + " = '" + name + "'";
		Cursor cursor = db.rawQuery( query, null );

		if( cursor.moveToFirst() ) return true;
		return false;
	}
	
	public void deleteProfile( String name ) {
		SQLiteDatabase db = this.getWritableDatabase();
		int gameId = this.getGameId( name );
		
		db.delete( TABLE_STATES, GAME_NAME + " = '" + name + "'", null );
		db.delete( TABLE_PLAYERS, GAME_ID + " = '" + gameId + "'", null );
	}
	
	
	public void addProfile( ArrayList<Player> players, State state ) {
		addState( state );
		int gameId = getGameId( state.gameName );
		for( Player p : players ) addPlayer( p, gameId );
		
		SQLiteDatabase db = this.getWritableDatabase();
		db.close();
	}
	
	public void updateProfile( ArrayList<Player> players, State state ) {
		updateState( state );
		int gameId = getGameId( state.gameName );
		
		for( Player p : players ) {
			updatePlayer( p, gameId );
		}
		
		SQLiteDatabase db = this.getWritableDatabase();
		db.close();
	}
	
	private ContentValues getPlayerContentValues( Player player, int gameId ) {
		// create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		values.put( Player.NAME, player.getAttribute( Player.NAME ));
		values.put( Player.BALANCE, player.getAttribute( Player.BALANCE ));
		values.put( Player.RANK, player.getAttribute( Player.RANK ));
		values.put( Player.WINS, player.getAttribute( Player.WINS ));
		values.put( Player.LOSSES, player.getAttribute( Player.LOSSES ));
		values.put( Player.FOLDS, player.getAttribute( Player.FOLDS ));
		values.put( OVERDRAFT_LIMIT, player.getAttribute( Player.OVERDRAFT_LIMIT ));
		values.put( Player.ACTIVE, player.getActiveFlag() );
		values.put( GAME_ID, String.valueOf( gameId ));
		
		return values;
	}
	
	private void addPlayer( Player player, int gameId ) {
		// get reference to writable db
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = getPlayerContentValues( player, gameId );
		
		
		// need to properly get histories. or not include histories
		//values.put( Player.BALANCE_HISTORY, player.getAttribute( Player.BALANCE_HISTORY ));
		//values.put( Player.BETTING_HISTORY, player.getAttribute( Player.BETTING_HISTORY ));
		
		// insert
		db.insert( TABLE_PLAYERS, null, values );
	}
	
	private boolean playerExists( Player player, int gameId ) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		String query = "SELECT * FROM " + TABLE_PLAYERS + " WHERE " + Player.NAME + " = '" + player.getAttribute( Player.NAME ) + "' AND " + GAME_ID + " = '" + gameId + "'";
		Cursor cursor = db.rawQuery( query, null );

		if( cursor.moveToFirst() ) return true;
		return false;
	}
	
	private void updatePlayer( Player player, int gameId ) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		if( playerExists( player, gameId )) {
			ContentValues values = getPlayerContentValues( player, gameId );
			db.update( TABLE_PLAYERS, values, Player.NAME + " = '" + player.getAttribute( Player.NAME ) + "'", null );
			
		} else {
			addPlayer( player, gameId );
		}
	}
	
	private void addState( State state ) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put( GAME_NAME, state.gameName );
		values.put( POT, state.pot );
		values.put( LAST_BET, state.lastBet );
		
		db.insert( TABLE_STATES, null, values );
		db.close();
	}
	
	
	private void updateState( State state ) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put( GAME_NAME, state.gameName );
		values.put( POT, state.pot );
		values.put( LAST_BET, state.lastBet );
		
		db.update( TABLE_STATES, values, GAME_NAME + " = '" + state.gameName + "'", null );
		db.close();
	}
	
	private int getGameId( String name ) {
		String query = "SELECT " + GAME_ID + " FROM " + TABLE_STATES + " WHERE " + GAME_NAME + " = '" + name + "'";
		
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = db.rawQuery( query,  null );
		
		// Go over each row, build
		if( cursor.moveToFirst() ) return cursor.getInt(0);
		return -1;
	}
	
	public void editProfile() {
		// Implement method
	}
	
	public Profile getProfile( String gameName ) {
		//SQLiteDatabase db = this.getReadableDatabase();
		Profile profile = new Profile();
		
		profile.players = getPlayers( gameName );
		profile.state = getState( gameName );
		
		return profile;
	}
	
	private State getState( String gameName ) {
		SQLiteDatabase db = this.getReadableDatabase();
		State state = new State();
		
		String query = "SELECT * FROM " + TABLE_STATES + " WHERE " + GAME_NAME + " = '" + gameName + "'";
		Cursor cursor = db.rawQuery( query, null );
		
		if( cursor.moveToFirst() ) {
			state.gameName 	= cursor.getString(1);
			state.pot 		= cursor.getInt(2);
			state.lastBet 	= cursor.getInt(3);
		}
		
		return state;
	}
	
	private ArrayList<Player> getPlayers( String gameName ) {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<Player> plist = new ArrayList<Player>();
		
		String query = "SELECT * FROM " + TABLE_PLAYERS + " WHERE " + GAME_ID + " = '" + getGameId( gameName ) + "'";
		Cursor cursor = db.rawQuery( query, null );
		
		if( cursor.moveToFirst() ) {
			do {
				String name 	= cursor.getString(1);
				String balance 	= cursor.getString(2);
				String rank 	= cursor.getString(3);
				String folds	= cursor.getString(4);
				String wins		= cursor.getString(5);
				String losses	= cursor.getString(6);
				String odl		= cursor.getString(7);
				int active		= Integer.parseInt( cursor.getString(8) );
				
				Player p = new Player( new String[] { name, balance, rank, folds, wins, losses, odl, null, null });
				if( active == 0 ) p.fold();
				plist.add(p);
				
			} while( cursor.moveToNext());
		}
		
		return plist;
	}
	
	/*
	public ArrayList<String> getPlayer( String name ) {
		// get reference to readable db
		SQLiteDatabase db = this.getReadableDatabase();
		
		// build query
		Cursor cursor = db.query(
				TABLE_PLAYERS, 	// table
				PLAYER_COLUMNS,		//column names
				" name = ?",	//selections
				new String[] { String.valueOf( name ) }, // selections args
				null,			//group by
				null,			//having
				null,			//order by
				null );			//limit
		
		// if results, get first
		if( cursor != null ) cursor.moveToFirst();
		
		// return player info
		ArrayList<String> info = new ArrayList<String>();
		for( int i=0; i<cursor.getCount(); i++ ) {
			info.add( cursor.getString(i) );
		}
		
		return info;
	}
	*/
	
	public String[] getProfileNames() {
		ArrayList<String> nameList = new ArrayList<String>();
		
		// Build query
		String query = "SELECT " + GAME_NAME + " FROM " + TABLE_STATES;
		
		// Get reference to writable db
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = db.rawQuery( query,  null );
		
		// Go over each row, build
		if( cursor.moveToFirst() ) {
			do {
				nameList.add(cursor.getString(0));
			} while( cursor.moveToNext());
		}
		
		String[] names = new String[nameList.size()];
		for( int i=0; i<nameList.size(); i++ ) {
			names[i] = nameList.get(i);
		}
		return names;
	}
	
	
	@Override
	public void onCreate( SQLiteDatabase db ) {
		db.execSQL( CREATE_PLAYER_TABLE );
		db.execSQL( CREATE_STATE_TABLE );
	}

	@Override
	public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {
		db.execSQL( "DROP TABLE IF EXISTS " + TABLE_PLAYERS );
		db.execSQL( "DROP TABLE IF EXISTS " + TABLE_STATES );
		db.execSQL( "VACUUM" );
		onCreate( db );
	}
	
}
