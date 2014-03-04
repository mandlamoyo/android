package com.mandla.budgetation.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mandla.budgetation.model.Purchase;
import com.mandla.budgetation.util.Date;

public class PurchaseDataHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "budgetation.db";
	
	private static final String TABLE_PURCHASES = "purchases";
	
	private static final String KEY_ID = "id";
	private static final String NAME = "name";
	private static final String QUANTITY = "quantity";
	private static final String PRICE = "price";
	private static final String CURRENCY = "currency";
	private static final String DATE = "date";
	
	private static final String CREATE_TABLE_PURCHASES = "CREATE TABLE " + TABLE_PURCHASES + " ( " + 
															KEY_ID + " INTEGER PRIMARY KEY UNIQUE, " +
															NAME + " TEXT, " +
															QUANTITY + " INTEGER, " +
															PRICE + " REAL, " +
															CURRENCY + " TEXT, " +
															DATE + " TEXT )";
	
	private static PurchaseDataHelper dbInstance;
	
	private PurchaseDataHelper( Context context ) {
		super( context, DATABASE_NAME, null, DATABASE_VERSION );
	}
	
	public static PurchaseDataHelper getInstance( Context context ) {
		if( dbInstance == null ) dbInstance = new PurchaseDataHelper( context.getApplicationContext() );
		return dbInstance;
	}

	public void addPurchase( Purchase p ) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = getPurchaseContentValues( p );
		db.insert( TABLE_PURCHASES, null, values );
	}
	
	public void editPurchase( Purchase p ) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		if( purchaseExists( p )) {
			ContentValues values = getPurchaseContentValues( p );
			//db.update( TABLE_PURCHASES, values, DATE + " = '" + p.date + "'", null );
			db.update( TABLE_PURCHASES, values, KEY_ID + " = " + p.pid, null );
			
		} else {
			addPurchase( p );
		}
	}
	
	public void deletePurchase( Purchase p ) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete( TABLE_PURCHASES, KEY_ID + " = " + p.pid, null );
	}
	
	public boolean isEmpty() {
		SQLiteDatabase db = this.getWritableDatabase();
		
		String query = "SELECT * FROM " + TABLE_PURCHASES;
		Cursor cursor = db.rawQuery( query, null );

		if( cursor.moveToFirst() ) return false;
		return true;
	}
	
	public ArrayList<Purchase> getPurchasesByMonth( String date ) {
		SQLiteDatabase db = this.getWritableDatabase();
		ArrayList<Purchase> purchases = new ArrayList<Purchase>();
		
		String startDay = "00";
		String endDay = "32";
		
		String startDate = date.substring( 0, Purchase.DAY_START_INDEX ) + startDay;
		String endDate = date.substring( 0, Purchase.DAY_START_INDEX ) + endDay;
		String query = "SELECT * FROM " + TABLE_PURCHASES + " WHERE " + DATE + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
		
		Cursor cursor = db.rawQuery( query, null );

		if( cursor.moveToNext() ) {
			do { 
				purchases.add( getPurchaseFromCursor( cursor ));

			} while( cursor.moveToNext() );
		}
		
		return purchases;
	}
	
	public ArrayList<Purchase> getPurchasesByDate( String startDate, String endDate ) {
		SQLiteDatabase db = this.getWritableDatabase();
		ArrayList<Purchase> purchases = new ArrayList<Purchase>();
		
		String query = "SELECT * FROM " + TABLE_PURCHASES + " WHERE " + DATE + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
		Cursor cursor = db.rawQuery( query, null );

		if( cursor.moveToNext() ) {
			do { 
				purchases.add( getPurchaseFromCursor( cursor ));

			} while( cursor.moveToNext() );
		}
		
		return purchases;
	}
	
	
	//PRIVATE
	private ContentValues getPurchaseContentValues( Purchase p ) {
		ContentValues values = new ContentValues();

		values.put( KEY_ID, p.pid );
		values.put( NAME, p.name );
		values.put( QUANTITY, p.quantity );
		values.put( PRICE, p.price );
		values.put( CURRENCY, p.getCurrency() );
		values.put( DATE, p.date );
		
		return values;
	}
	
	private Purchase getPurchaseFromCursor( Cursor c ) {
		Purchase p = new Purchase();

		p.pid		= c.getInt(0);
		p.name		= c.getString(1);
		p.quantity	= c.getInt(2);
		p.price		= c.getDouble(3);
		p.setCurrency( c.getString(4) );
		p.date		= c.getString(5);
		
		return p;
	}
	
	public int getLargestId() {
		SQLiteDatabase db = this.getWritableDatabase();
		
		String query = "SELECT " + KEY_ID + " FROM " + TABLE_PURCHASES + " ORDER BY " + KEY_ID + " DESC";
		Cursor cursor = db.rawQuery( query, null );
		
		if( cursor.moveToFirst() ) return cursor.getInt(0);
		return -1;
	}
	
	private boolean purchaseExists( Purchase p ) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		String query = "SELECT * FROM " + TABLE_PURCHASES + " WHERE " + KEY_ID + " = " + p.pid;
		Cursor cursor = db.rawQuery( query, null );

		if( cursor.moveToFirst() ) return true;
		return false;
	}
	
	public boolean earlierPurchasesExist( String date ) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		date = Date.setDay( date, 1 );
		String query = "SELECT * FROM " + TABLE_PURCHASES + " WHERE " + DATE + " < '" + date + "'";
		Cursor cursor = db.rawQuery( query, null );

		if( cursor.moveToFirst() ) return true;
		return false;
	}
	
	public boolean laterPurchasesExist( String date ) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		date = Date.setDay( date, 32 );
		String query = "SELECT * FROM " + TABLE_PURCHASES + " WHERE " + DATE + " > '" + date + "'";
		Cursor cursor = db.rawQuery( query, null );

		if( cursor.moveToFirst() ) return true;
		return false;
	}
	
	@Override
	public void onCreate( SQLiteDatabase db ) {
		db.execSQL( CREATE_TABLE_PURCHASES );
		
	}

	@Override
	public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {
		db.execSQL( "DROP TABLE IF EXISTS " + TABLE_PURCHASES );
		db.execSQL( "VACUUM" );
		onCreate( db );
		
	}
}
