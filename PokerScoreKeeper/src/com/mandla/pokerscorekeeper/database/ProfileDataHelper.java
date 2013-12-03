package com.mandla.pokerscorekeeper.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mandla.pokerscorekeeper.model.Player;

/* SINGLETON */
public class ProfileDataHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "pokerscorekeeper.db";
	
	//public static final String ID_COL = BaseColumns._ID;

	public static final String TABLE_PLAYERS = "players";
	
	private static final String KEY_ID = "id";
	private static final String[] COLUMNS = { KEY_ID, Player.NAME, Player.BALANCE, Player.RANK, Player.WINS, Player.LOSSES, Player.FOLDS };
	private static final String CREATE_PLAYER_TABLE = "CREATE TABLE " + TABLE_PLAYERS + " ( " +
														KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
														Player.NAME + " TEXT, " + 
														Player.BALANCE + " INTEGER, " + 
														Player.RANK + " INTEGER, " + 
														Player.WINS + " INTEGER, " + 
														Player.LOSSES + " INTEGER, " +
														Player.FOLDS + " INTEGER )";
	
	private static ProfileDataHelper dbInstance;
	
	private ProfileDataHelper( Context context ) {
		super( context, DATABASE_NAME, null, DATABASE_VERSION );
	}

	public static ProfileDataHelper getInstance( Context context ) {
		if( dbInstance == null ) dbInstance = new ProfileDataHelper( context.getApplicationContext() );
		return dbInstance;
	}
	
	public void addProfile( Player player ) {
		// get reference to writable db
		SQLiteDatabase db = this.getWritableDatabase();
		
		// create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		values.put( Player.NAME, player.getAttribute( Player.NAME ));
		values.put( Player.BALANCE, player.getAttribute( Player.BALANCE ));
		values.put( Player.RANK, player.getAttribute( Player.RANK ));
		values.put( Player.WINS, player.getAttribute( Player.WINS ));
		values.put( Player.LOSSES, player.getAttribute( Player.LOSSES ));
		values.put( Player.FOLDS, player.getAttribute( Player.FOLDS ));
		
		// need to properly get histories. or not include histories
		//values.put( Player.BALANCE_HISTORY, player.getAttribute( Player.BALANCE_HISTORY ));
		//values.put( Player.BETTING_HISTORY, player.getAttribute( Player.BETTING_HISTORY ));
		
		// insert
		db.insert( TABLE_PLAYERS, null, values );
		
		// close
		db.close();
	}
	
	public ArrayList<String> getProfile( String name ) {
		// get reference to readable db
		SQLiteDatabase db = this.getReadableDatabase();
		
		// build query
		Cursor cursor = db.query(
				TABLE_PLAYERS, 	// table
				COLUMNS,		//column names
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
	
	public String[] getProfileNames() {
		ArrayList<String> nameList = new ArrayList<String>();
		
		// Build query
		String query = "SELECT " + Player.NAME + " FROM " + TABLE_PLAYERS;
		
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
	}

	@Override
	public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {
		db.execSQL( "DROP TABLE IF EXISTS players" );
		db.execSQL( "VACUUM" );
		onCreate( db );
	}
	
}
