package com.mandla.budgetation;

import java.text.DecimalFormat;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mandla.budgetation.model.Purchase;



public class PurchaseAdapter extends ArrayAdapter<Purchase> {

	Context context;
	int layoutResourceId;
	List<Purchase> purchases;
	TextView noContent;
	
	public PurchaseAdapter( Context context, int layoutResourceId, List<Purchase> purchases ) {
		super( context, layoutResourceId, purchases );
		
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.purchases = purchases;
		
		this.noContent = (TextView) ((Activity) context).findViewById( R.id.empty );
		toggleContentDisplay();
	}

	private void toggleContentDisplay() {
		if( isEmpty() ) {
			noContent.setVisibility( View.VISIBLE );

		} else {
			noContent.setVisibility( View.GONE );
		}
	}
	
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		toggleContentDisplay();
	}
	
	@Override
	public View getView( int position, View convertView, ViewGroup parent ) {
		View row = convertView;
		PurchaseHolder holder;
		boolean needSeparator = false;
		
		
		if( row == null ) {
			LayoutInflater inflater = (( Activity ) context ).getLayoutInflater();
			row = inflater.inflate( layoutResourceId, null );
			holder = new PurchaseHolder();
			
			holder.separator = (TextView) row.findViewById( R.id.list_item_separator );
			holder.name = (TextView) row.findViewById( R.id.list_item_name );
			holder.price = (TextView) row.findViewById( R.id.list_item_price );
			holder.quantity = (TextView) row.findViewById( R.id.list_item_quantity );
			holder.currency = (TextView) row.findViewById( R.id.list_item_currency );
			
			row.setTag( holder );
			
		} else {
			holder = (PurchaseHolder ) row.getTag();
		}
		
		Purchase purchase = purchases.get( position );
		DecimalFormat df = new DecimalFormat( "#.00" );
		String currency = ( purchase.symbol.equals( "" )) ? purchase.getCurrency() : "";
		
		
		switch( purchase.getSeparatorState() ) {
			case Purchase.NORMAL:
				needSeparator = false;
				break;
				
			case Purchase.SEPARATOR:
				needSeparator = true;
				break;
				
			case Purchase.UNKNOWN:
				if( position == 0 ) {
					needSeparator = true;
					
				} else {
					Purchase predecessor = purchases.get( position-1 );
					needSeparator = ( purchase.sameDay( predecessor )) ? false : true;
				}
				break;
				
			default:
				break;
		}
		
		if( needSeparator ) {
			holder.separator.setText( purchase.getFormattedDay() );
			holder.separator.setVisibility( View.VISIBLE );
		
		} else if( purchase.name.equals( "TOTAL:" )) {
			holder.separator.setText( "" );
			holder.separator.setVisibility( View.VISIBLE );
			
		} else {
			holder.separator.setVisibility( View.GONE );
		}
		
		
		holder.name.setText( purchase.name );
		String price = df.format( purchase.price );
		String finalPrice = ( price.substring( 0, 1 ).equals( "." )) ? "0" + price : price;
		
		holder.price.setText( purchase.symbol + finalPrice );
		
		if( purchase.name.equals( "TOTAL:" )) {
			holder.name.setTextColor( Color.RED );
			holder.quantity.setVisibility( View.GONE );
			holder.currency.setVisibility( View.GONE );
		
		} else {
			holder.name.setTextColor( Color.WHITE );
			holder.quantity.setVisibility( View.VISIBLE );
			holder.currency.setVisibility( View.VISIBLE );
			holder.quantity.setText( "Quantity: " + String.valueOf( purchase.quantity ));
			holder.currency.setText( currency );
		}
		
		return row;
	}
	
	static class PurchaseHolder
	{
		TextView separator;
		TextView name;
		TextView price;
		TextView quantity;
		TextView currency;
	}
	
}
