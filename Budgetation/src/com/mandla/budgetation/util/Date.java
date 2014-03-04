package com.mandla.budgetation.util;

import java.text.DateFormatSymbols;

import com.mandla.budgetation.model.Purchase;

import android.text.format.Time;

public class Date {
	
	public static String getDate() {
		Time now = new Time( Time.getCurrentTimezone() );
		now.setToNow();
		return now.toString();
	}
	
	public static String formattedDate( int date ) {
		String fDate = ( date < 10 ) ? "0" + String.valueOf( date ) : String.valueOf( date );
		return fDate;
	}
	
	public static String setDay( String date, int day )
	{	return date.substring( 0, Purchase.DAY_START_INDEX) + formattedDate( day ) + date.substring( Purchase.DAY_END_INDEX ); }
	
	public static String getYear( String date )
	{	return date.substring( Purchase.YEAR_START_INDEX, Purchase.YEAR_END_INDEX ); }
	
	public static String setYear( String date, int year )
	{	return date.substring( 0, Purchase.YEAR_START_INDEX) + year + date.substring( Purchase.YEAR_END_INDEX ); }
	
	public static String getMonth( String date )
	{	return date.substring( Purchase.MONTH_START_INDEX, Purchase.MONTH_END_INDEX ); }
	
	public static String setMonth( String date, int month )
	{	return date.substring( 0, Purchase.MONTH_START_INDEX) + formattedDate( month ) + date.substring( Purchase.MONTH_END_INDEX ); }
	
	public static String getMonthName( int month )
	{	return new DateFormatSymbols().getMonths()[month-1]; }
	
	public static boolean isEarlierMonth( String month, String other ) {
		int year1 = Integer.parseInt( getYear( month ));
		int year2 = Integer.parseInt( getYear( other ));
		
		if( year1 == year2 ) {
			int month1 = Integer.parseInt( getMonth( month ));
			int month2 = Integer.parseInt( getMonth( other ));
			
			return month1 < month2;	
		} 
		
		return year1 < year2;
	}
	
	public static String daySuffix( String day ) {
		char lastLetter = day.charAt( day.length() - 1 );
		String suffix;
		
		switch( lastLetter ) {
			case '1':
				suffix = "st";
				break;
				
			case '2':
				suffix = "nd";
				break;
				
			case '3':
				suffix = "rd";
				break;
				
			default:
				suffix = "th";
				break;
		}
		
		return day + suffix;
	}
}
