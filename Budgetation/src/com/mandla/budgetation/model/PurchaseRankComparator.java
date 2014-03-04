package com.mandla.budgetation.model;

import java.util.Comparator;

public class PurchaseRankComparator implements Comparator<Purchase> {

	@Override
	public int compare( Purchase p1, Purchase p2 ) {
		
		int d1 = Integer.parseInt( p1.getDay() );
		int d2 = Integer.parseInt( p2.getDay() );
		
		if( d1 > d2 ) {
			return 1;
		} else if( d1 < d2 ) {
			return -1;
		} else {
			return 0;
		}
	}
}
