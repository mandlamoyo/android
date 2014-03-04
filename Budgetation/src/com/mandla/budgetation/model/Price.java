package com.mandla.budgetation.model;


public class Price {
	
	private static final String DEFAULT_CURRENCY = "GBP";
	
	public double price;
	public String currency;
	
	
	public Price() {
		price = 0;
		currency = DEFAULT_CURRENCY;
	}
	
	public Price( double price, String currency ) {
		this.price = price;
		this.currency = currency;
	}
	
}
