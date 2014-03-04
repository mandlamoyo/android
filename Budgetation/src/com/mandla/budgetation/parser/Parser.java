package com.mandla.budgetation.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.mandla.budgetation.model.Price;
import com.mandla.budgetation.model.Purchase;
import com.mandla.budgetation.util.Date;


public class Parser {
	private ArrayList<String> tokens;
	private Iterator<String> itr;
	
	private static final Map<String,String> errorMap = new HashMap<String,String>(); {
		errorMap.put( "won", "1" );
		errorMap.put( "to", "2" );
		errorMap.put( "too", "2" );
		
	};
	
	
	public Parser() {
		//tokens = new ArrayList<String>();
	}
	
	private void getTokens( String sentence ) {
		tokens = new ArrayList<String>();
		String[] tokenArray = sentence.split( "\\s" );

		for( String t : tokenArray ) {
			tokens.add( t );
			System.out.println( t );
		}
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
		String[] usd = new String[] {"$","dollars","dollar","bucks","cents","cent","c"};
		String[] gbp = new String[] {"£","pounds","quid","p","pennies","pence","penny"};
		
		if( contains( usd,symbol )) return "USD";
		if( contains( gbp,symbol )) return "GBP";
		
		System.out.println( "Currency not recognized" );
		return "";
	}
	
	private Price parsePrice() {
		if( !itr.hasNext() ) {
			System.out.print( "Incorrect Syntax!" );
			return null;
		}
		
		double unit = 0;
		double centismal = 0;
		String centString = "";
		String currency = "TST"; //"GBP";
		String priceType = "";
		String v = (String) itr.next();
		
		// Get main price and currency
		char c = v.charAt(0);
		char d = v.charAt( v.length()-1 );
		
		ArrayList<String> centNames = new ArrayList<String>();
		centNames.add( "cent" );
		centNames.add( "cents" );
		centNames.add( "penny" );
		centNames.add( "pence" );
		centNames.add( "pennies" );
		centNames.add( "p" );
		
		System.out.println( v + " <- HERE!" );
		System.out.println();
		
		if( Character.isDigit( d )) {
			// last char is a number, string v is not in form '50p'
			// 'int'/'£int'
			
			if( Character.isDigit( c )) {
				// first char is a number, string v doesnt have a symbol prefix '£'
				// 'int' - could be unit or centismal
				System.out.println( "1: " + v );
				//double value = Double.parseDouble( v );
				
				// get next symbol..
				priceType = itr.next();
				currency = getCurrency( priceType );
				
				if( !priceType.equals( "" ) && centNames.contains( priceType )) {
					// value is centismal
					//System.out.println( "2: " + value );
					centismal = getCentismal( v );
					
				} else {
					// value is unit
					//System.out.println( "3: " + value );
					unit = Double.parseDouble( v );
				}
				
				// get centismal
				if( itr.hasNext() ) {
					v = itr.next();
					if( currency.equals( "" )) currency = getCurrency( itr.next() );
					centismal = getCentismal( v );
					System.out.println( "c" );
				}
			} else {
				// '£':'int' - unit only
				System.out.println( "4: " + v );
				currency = getCurrency( String.valueOf( c ).substring( 0, 1 ));
				System.out.println( "a" );
				unit = Double.parseDouble( v.substring( 1 ));
				System.out.println( "b" );
				// get centismal, if it exists
				
				if( itr.hasNext() ) {
					centismal = getCentismal( itr.next() );
					System.out.println( "c" );
				}
			}
		
			/*
			if( centNames.contains( priceType )) {
				centismal = unit/100;
				unit = 0;
			} else {
				if( itr.hasNext() ) centString = itr.next(); //centismal = getCentismal( itr.next() );
			}*/
			
		} else {
			// 'int':'p'
			System.out.println( "5: " + v );
			centismal = getCentismal( v );
			currency = getCurrency( v.substring( v.length()-1, v.length() ));
			//centString = v;
		}
		
		System.out.println( unit );
		System.out.println( centismal );
		
		/*
		if( Character.isDigit( c )) {
			
			
			if( Character.isDigit( d )) {
				unit = Double.parseDouble( v );
				priceType = itr.next();
				currency = getCurrency( priceType );
				
				if( centNames.contains( priceType )) {
					centismal = unit/100;
					unit = 0;
				} else {
					if( itr.hasNext() ) centString = itr.next(); //centismal = getCentismal( itr.next() );
				}
			} else {
				centString = v; //centismal = getCentismal( v );
			}
		} else {
			currency = getCurrency( String.valueOf( c ));
			unit = Double.parseDouble( v.substring( 1 ));
		}
		
		if( !centString.equals( "" )) {
			c = centString.charAt( centString.length()-1 );
			if( Character.isDigit( c )) {
				centismal = Double.parseDouble( centString );
				if( itr.hasNext() ) priceType = itr.next(); // Ignore centismal name
				if( currency == null ) currency = getCurrency( priceType );
				
			} else {
				centismal = Double.parseDouble( centString.substring(0,centString.length()-1 ));
				if( currency == null ) currency = getCurrency( centString.substring(centString.length()-1, centString.length())); //will this work or oob?
				//v.substring(v.length()-1);
			}
		} else {
			centismal = 0;
		}
		
		centismal /= 100;
		*/
		/*
		if( Character.isDigit( c )) {
			unit = Double.parseDouble( v );
			currency = getCurrency( (String)itr.next() );
			
		} else {
			currency = getCurrency( String.valueOf( c ));
			unit = Double.parseDouble( v.substring( 1 ));
		}
		*/
		/*
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
		*/
		Price p = new Price();
		p.currency = currency;
		p.price = unit + centismal;
		System.out.println( p.currency + " : " + p.price );
		return p;
	}
	
	
	private Double getCentismal( String v ) {
		
		double centismal;
		char c;
		
		// Get centismal value
		//if( itr.hasNext() ) {
		//v = (String) itr.next();
		c = v.charAt( v.length()-1 );
		if( Character.isDigit( c )) {
			centismal = Double.parseDouble( v );
			//if( itr.hasNext() ) itr.next(); // Ignore centismal name
			
		} else {
			centismal = Double.parseDouble( v.substring(0,v.length()-1 ));
			//v.substring(v.length()-1);
		}
		//} else {
		//	centismal = 0;
		//}
		
		centismal /= 100;
		return centismal;
	}
	
	
	private Purchase parsePurchase() {
		if( !itr.hasNext() ) return null;
		String v = (String) itr.next();
		
		try {
			if( !Character.isDigit( v.charAt( 0 ))) {
				for( String w : errorMap.keySet() ) {
					if( v.equalsIgnoreCase( w )) {
						v = errorMap.get(w);
						break;
					}
				}
			}
			
			int quantity = Integer.parseInt( v );
			String name = parseName();
			Price price = parsePrice();
			String date = Date.getDate();
			
			Purchase p = new Purchase( quantity, name, price, date );
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
		p.printDetails();
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
