package com.mandla.budgetation.model;

public class Purchase {
	public String name;
	public double price;
	public int quantity;
	public String currency;
	
	public Purchase( int quantity, String name, Price price ) {
		this.name = name;
		this.quantity = quantity;
		this.price = price.price;
		this.currency = price.currency;
	}
	
	public void printDetails() {
		System.out.println( name + " - " + quantity );
		System.out.println( price + " (" + currency + ")" );
		System.out.println();
	}
}