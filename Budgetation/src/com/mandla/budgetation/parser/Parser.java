package com.mandla.budgetation.parser;

import java.util.ArrayList;
import java.util.Iterator;

import com.mandla.budgetation.model.Price;
import com.mandla.budgetation.model.Purchase;


public class Parser {
	private ArrayList<String> tokens;
	private Iterator itr;
	
	public Parser() {
		//tokens = new ArrayList<String>();
	}
	
	private void getTokens( String sentence ) {
		tokens = new ArrayList<String>();
		String[] tokenArray = sentence.split( "\\s" );

		for( String t : tokenArray ) tokens.add( t );
	}
	
	
	private String parseName() {
		if( !itr.hasNext() ) {
			System.out.print( "Incorrect Syntax!" );
			return "";
		}
		
		String segment;
		String name = "";
		
		while( !( segment = (String) itr.next()).equals( "for" )) {
			name += segment + " ";
		}

		return name.substring(0,1).toUpperCase() + name.substring(1);
	}
	
	private boolean contains( String[] container, String symbol ) {
		for( String s : container ) {
			if( s.equalsIgnoreCase( symbol )) return true;
		}
		
		return false;
	}
	
	private String getCurrency( String symbol ) {
		String[] usd = new String[] {"$","dollars","dollar","bucks"};
		String[] gbp = new String[] {"£","pounds","quid"};
		
		if( contains( usd,symbol )) return "USD";
		if( contains( gbp,symbol )) return "GPB";
		
		System.out.println( "Currency not recognized" );
		return "";
	}
	
	private Price parsePrice() {
		if( !itr.hasNext() ) {
			System.out.print( "Incorrect Syntax!" );
			return null;
		}
		
		double unit;
		double centismal;
		String currency;
		String v = (String) itr.next();
		
		// Get main price and currency
		char c = v.charAt(0);
		if( Character.isDigit( c )) {
			unit = Double.parseDouble( v );
			currency = getCurrency( (String)itr.next() );
			
		} else {
			currency = getCurrency( String.valueOf( c ));
			unit = Double.parseDouble( v.substring( 1 ));
		}
		
		// Get centismal value
		if( itr.hasNext() ) {
			v = (String) itr.next();
			c = v.charAt( v.length()-1 );
			if( Character.isDigit( c )) {
				centismal = Double.parseDouble( v );
				if( itr.hasNext() ) itr.next(); // Ignore centismal name
				
			} else {
				centismal = Double.parseDouble( v.substring(0,v.length()-1 ));
				//v.substring(v.length()-1);
			}
		} else {
			centismal = 0;
		}
		
		centismal /= 100;
		Price p = new Price();
		p.currency = currency;
		p.price = unit + centismal;
		return p;
	}
	
	private Purchase parsePurchase() {
		if( !itr.hasNext() ) return null;
		String v = (String) itr.next();
		
		try {
			int quantity = Integer.parseInt( v );
			String name = parseName();
			Price price = parsePrice();
			
			Purchase p = new Purchase( quantity, name, price );
			return p;
			
		} catch (NumberFormatException e ) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*
	 * Takes a string of format: "Price: <price> (<currency>) - Quantity: <quantity>"
	 * Returns a string array [quantity, price, currency]
	 */
	public String[] parsePurchaseInfo( String s ) {
		getTokens( s );
		String c = tokens.get(2);
		String currency = c.substring(1, c.length()-1);
		
		return new String[] {  tokens.get(5), tokens.get(1), currency };
	}
	
	public Purchase parse( String s ) {
		//System.out.println(s);
		getTokens( s );
		//for( String t : tokens ) System.out.println( t );
		
		itr = tokens.iterator();
		
		Purchase p = parsePurchase();
		//p.printDetails();
		return p;
	}
	
	/*
	public static void main( String[] args ) {
		Parser p = new Parser();

		p.parse( "5 bottles of wine for £3 99" );
		p.parse( "1 iphone 4 for 23 dollars" );
		p.parse( "3 chessboards for 12 pounds 95p" );
		p.parse( "22 Mars bars for £15 22 pence" );
		p.parse( "1 can of coke for 2 dollars 50 cents" );
	}*/
}
