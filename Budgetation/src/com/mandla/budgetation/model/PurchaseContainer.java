package com.mandla.budgetation.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PurchaseContainer {

	private static final String NAME = "com.mandla.budgetation.NAME";
	private static final String INFO = "com.mandla.budgetation.INFO";
	private static final String HOME_CURRENCY = "GBP"; //Set in preferences
	
	public ArrayList<Purchase> purchases;
	public List<Map<String,String>> purchaseMapList;
	
	private static PurchaseContainer pc;
	
	private PurchaseContainer() {
		purchases = new ArrayList<Purchase>();
		purchaseMapList = new ArrayList<Map<String,String>>();
	}
	
	public static PurchaseContainer getInstance() {
		if( pc == null ) pc = new PurchaseContainer();
		return pc;
	}
	
	
	private void updateTotal() {
		if( !purchases.isEmpty() ) {
			ArrayList<Purchase> purchaseBuffer = new ArrayList<Purchase>();
			double sum = 0;
			
			for( Purchase p : purchases ) {
				if( !p.name.equals( "TOTAL:" )) {
				
					// TODO - take currency into consideration
					sum += p.price; 
					purchaseBuffer.add( p );
				}
			}
			
			purchases.clear();
			purchases.addAll( purchaseBuffer );
			
			Purchase p = new Purchase();
			p.name = "TOTAL:";
			p.price = sum;
			p.setCurrency( "GBP" );
			p.quantity = 1;
			p.date = purchases.get( purchases.size()-1 ).date;
			
			purchases.add( p );
		}
	}
	
	public Map<String,String> getPurchaseInfo( Purchase p ) {
		// Define info to put into list items for display
		Map<String,String> pMap = new HashMap<String,String>();
		
		pMap.put( NAME, p.name );
		pMap.put( INFO, p.getInfo() );
		
		return pMap;
	}
	
	public void addPurchase( Purchase p ) {
		purchases.add( p );
		updateTotal();
		purchaseMapList.add( getPurchaseInfo( p ));
		
		/*
		Map<String,String> purchaseMap = new HashMap<String,String>();
		String purchaseInfo = "Price: " + purchase.price + " (" + purchase.currency + ") - Quantity: " + purchase.quantity;
		purchaseMap.put( NAME, purchase.name );
		purchaseMap.put( PRICE, purchaseInfo );
		purchaseMapList.add( purchaseMap );
		purchaseList.add( purchase );*/
	}
	
	public void changePurchase( Purchase p, int index ) {
		purchases.set( index, p );
		updateTotal();
		purchaseMapList.set( index, getPurchaseInfo( p ) );
	}
	
	public Purchase getPurchase( int index )
	{	return purchases.get( index ); }

}
