package com.mandla.budgetation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.mandla.budgetation.model.Purchase;
import com.mandla.budgetation.parser.Parser;


public class MainActivity extends SherlockActivity {

	public static final String NAME = "com.mandla.budgetation.NAME";
	public static final String PRICE = "com.mandla.budgetation.PRICE";
	
	public static final int QUANTITY_INFO = 0;
	public static final int PRICE_INFO = 1;
	public static final int CURRENCY_INFO = 2;
	
	private static final int REQUEST_CODE = 1234;
	private ListView purchaseListView;
	//private ArrayList<String> purchaseList;
	//private Map<String,String> purchaseMap;
	private List<Map<String,String>> purchaseMapList;
	private Parser parser;
	
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );
		
		ActionBar actionBar = getSupportActionBar();
		
		// Set display features/mode
		actionBar.setDisplayShowHomeEnabled( false );
		//actionBar.setDisplayShowTitleEnabled( false );
		
		Button speakButton = (Button) findViewById( R.id.speakButton );
		purchaseListView = (ListView) findViewById( R.id.list );
		//purchaseList = new ArrayList<String>();
		//purchaseMap = new HashMap<String,String>();
		purchaseMapList = new ArrayList<Map<String,String>>();
		parser = new Parser();
		
		//purchaseListView.setAdapter( new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, purchaseList ));
		
		// Disable button if no recognition service is present
		PackageManager pm = getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities( new Intent( RecognizerIntent.ACTION_RECOGNIZE_SPEECH ), 0 );
		if( activities.size() == 0 ) {
			speakButton.setEnabled( false );
			speakButton.setText( "Recognizer not present" );
		}
		
		String[] fromMapKey = new String[] { NAME, PRICE };
		int[] toLayoutId = new int[] { android.R.id.text1, android.R.id.text2 };
		ListAdapter listAdapter = new SimpleAdapter( this, purchaseMapList, android.R.layout.simple_list_item_2, fromMapKey, toLayoutId );
		purchaseListView.setAdapter( listAdapter );
		
		purchaseListView.setOnItemClickListener( new OnItemClickListener() {
			@Override
			public void onItemClick( AdapterView<?> parent, View view, int position, long id ) {
				String field = ((TextView) view.findViewById( android.R.id.text1 )).getText().toString();
				Toast.makeText( getBaseContext(), field , Toast.LENGTH_LONG ).show();
				showEditPurchaseDialog( view, position );
			}
		});
		
		
	}
	
	public void showEditPurchaseDialog( View view, int position ) {
		AlertDialog.Builder alert = new AlertDialog.Builder( this );
		View editPurchaseDialogView = View.inflate( this, R.layout.edit_purchase_dialog, null );

		final int index = position;
		final EditText nameEdit = (EditText) editPurchaseDialogView.findViewById( R.id.name_edit );
		final EditText quantityEdit = (EditText) editPurchaseDialogView.findViewById( R.id.quantity_edit );
		final EditText priceEdit = (EditText) editPurchaseDialogView.findViewById( R.id.price_edit );
		final EditText currencyEdit = (EditText) editPurchaseDialogView.findViewById( R.id.currency_edit );
		
		nameEdit.setText(( (TextView) view.findViewById( android.R.id.text1 )).getText().toString() );
		String info = ((TextView) view.findViewById( android.R.id.text2 )).getText().toString();
		String[] infoList = parser.parsePurchaseInfo( info );
		
		quantityEdit.setText( infoList[QUANTITY_INFO] );
		priceEdit.setText( infoList[PRICE_INFO] );
		currencyEdit.setText( infoList[CURRENCY_INFO] );
		
		alert.setTitle( R.string.edit_dialog_title )
		.setView( editPurchaseDialogView )
		.setPositiveButton( R.string.save, new DialogInterface.OnClickListener() {
			@Override
			public void onClick( DialogInterface dialog, int whichButton ) {
				Map<String,String> purchase = new HashMap<String,String>();
				String q = quantityEdit.getText().toString();
				String p = priceEdit.getText().toString();
				String c = currencyEdit.getText().toString();
				String newInfo = "Price: " + p + " (" + c + ") - Quantity: " + q; 
				
				purchase.put( NAME, nameEdit.getText().toString() );
				purchase.put( PRICE, newInfo ); //priceEdit.getText().toString() );
				purchaseMapList.set( index, purchase );
				((BaseAdapter) purchaseListView.getAdapter()).notifyDataSetChanged();
			}
			
		}).setNegativeButton( android.R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick( DialogInterface dialog, int whichButton ) {
				// Do nothing
			}
			
		}).show();

	}
	
	public void showSelectMatchDialog( ArrayList<String> matches ) {
		AlertDialog.Builder alert = new AlertDialog.Builder( this );
		
		final Spinner matchList = new Spinner( this );
		ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>( this, android.R.layout.simple_spinner_item, matches );
		nameAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
		matchList.setAdapter( nameAdapter );
		
		alert.setTitle( R.string.match_dialog_title )
		.setMessage( R.string.match_dialog_message )
		.setView( matchList )
		.setPositiveButton( android.R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick( DialogInterface dialog, int whichButton ) {
				String match = matchList.getSelectedItem().toString();
				String[] mlist = match.split("and\\s");
				for( String s : mlist ) {
					
					Purchase purchase = parser.parse( s );
					Map<String,String> purchaseMap = new HashMap<String,String>();
					//purchaseMap.put( NAME, s );
					//purchaseMap.put( PRICE, "£5.00" );
					String purchaseInfo = "Price: " + purchase.price + " (" + purchase.currency + ") - Quantity: " + purchase.quantity;
					purchaseMap.put( NAME, purchase.name );
					purchaseMap.put( PRICE, purchaseInfo );
					purchaseMapList.add( purchaseMap );
				}

				((BaseAdapter) purchaseListView.getAdapter()).notifyDataSetChanged();
			}
			
		}).setNegativeButton( android.R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick( DialogInterface dialog, int whichButton ) {
				// Cancelled
			}
		
		}).show();
	}
	
	public void speakButtonClicked( View view ) {
		startVoiceRecognitionActivity();
	}

	private void startVoiceRecognitionActivity() {
		Intent intent = new Intent( RecognizerIntent.ACTION_RECOGNIZE_SPEECH );
		intent.putExtra( RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM );
		intent.putExtra( RecognizerIntent.EXTRA_PROMPT, "Voice recognition demo.." );
		//intent.putExtra( RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 60000 );
		startActivityForResult( intent, REQUEST_CODE );
	}
	
	@Override
	protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
		if( requestCode == REQUEST_CODE && resultCode == RESULT_OK ) {
			// Populate the wordsList with the String values the recognition engine thought it heard
			ArrayList<String> matches = data.getStringArrayListExtra( RecognizerIntent.EXTRA_RESULTS );
			if( !matches.isEmpty() ) showSelectMatchDialog( matches );
		}
		
		super.onActivityResult( requestCode, resultCode, data );
	}
	
	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate( R.menu.main, menu );
		return true;
	}
}
