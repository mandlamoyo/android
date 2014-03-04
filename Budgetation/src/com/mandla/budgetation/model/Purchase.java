package com.mandla.budgetation.model;

import java.text.DateFormatSymbols;

import com.mandla.budgetation.MainActivity;
import com.mandla.budgetation.util.Date;

public class Purchase {
	
	private static final String[] SYMBOLS = { "", "£", "$" };
	
	private static final int NONE = 0;
	private static final int GBP = 1;
	private static final int USD = 2;
	
	public static final int YEAR_START_INDEX = 0;
	public static final int YEAR_END_INDEX = 4;
	public static final int MONTH_START_INDEX = 4;
	public static final int MONTH_END_INDEX = 6;
	public static final int DAY_START_INDEX = 6;
	public static final int DAY_END_INDEX = 8;
	
	public static final int UNKNOWN = 0;
	public static final int SEPARATOR = 1;
	public static final int NORMAL = 2;

	public int pid;
	public String name;
	public double price;
	public int quantity;
	private String currency;
	public String date;
	public String symbol = SYMBOLS[NONE];
	private int separatorState = UNKNOWN;
	
	
	public Purchase() {
		pid = MainActivity.pidList++;
	}
	
	public Purchase( int quantity, String name, Price price, String date ) {
		this.pid = MainActivity.pidList++;
		this.name = name;
		this.quantity = quantity;
		this.price = price.price;
		this.currency = price.currency;
		this.date = date;
		
		setSymbol();
	}
	
	public void printDetails() {
		System.out.println( name + " - " + quantity );
		System.out.println( price + " (" + currency + ")" );
		System.out.println( date );
		System.out.println();
	}
	
	public String getCurrency()
	{	return currency; }
	
	public void setCurrency( String c )
	{	
		currency = c;
		setSymbol();
	}
	
	private void setSymbol() {
		if( currency.equals( "GBP" )) symbol = SYMBOLS[GBP];
		else if( currency.equals( "USD" )) symbol = SYMBOLS[USD];
		else symbol = SYMBOLS[NONE];
	}
	
	public String getInfo()
	{	return "Price: " + price + " (" + currency + ") - Quantity: " + quantity; }

	
	public int getSeparatorState()
	{	return separatorState; }
	
	public boolean sameDay( Purchase other )
	{	return getDay().equals( other.getDay() ); }
	
	public String getDay() {
		return date.substring( YEAR_START_INDEX, DAY_END_INDEX );
	}
	
	public String getFormattedDay() {
		String day = date.substring( DAY_START_INDEX, DAY_END_INDEX );
		if( day.substring( 0, 1 ).equals( "0" )) day = day.substring( 1 );
		
		int month = Integer.parseInt( date.substring( MONTH_START_INDEX, MONTH_END_INDEX ));
		String monthName = new DateFormatSymbols().getMonths()[month-1];
		
		return monthName + " " + Date.daySuffix( day );
	}
}