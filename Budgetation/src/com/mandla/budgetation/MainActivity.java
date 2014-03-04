package com.mandla.budgetation;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
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
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.mandla.budgetation.database.PurchaseDataHelper;
import com.mandla.budgetation.model.Price;
import com.mandla.budgetation.model.Purchase;
import com.mandla.budgetation.model.PurchaseContainer;
import com.mandla.budgetation.parser.Parser;
import com.mandla.budgetation.util.Date;


public class MainActivity extends SherlockActivity {

	public static final String NAME = "com.mandla.budgetation.NAME";
	public static final String INFO = "com.mandla.budgetation.INFO";
	
	public static final int QUANTITY_INFO = 0;
	public static final int PRICE_INFO = 1;
	public static final int CURRENCY_INFO = 2;
	
	private static final String DEFAULT_CURRENCY = "GBP";
	private static final int REQUEST_CODE = 1234;
	
	public static int pidList = 0;
	
	private ListView purchaseListView;
	//private ArrayList<Purchase> purchaseList;
	//private ArrayList<String> purchaseList;
	//private Map<String,String> purchaseMap;
	//private List<Map<String,String>> purchaseMapList;
	
	private PurchaseDataHelper pData;
	private PurchaseContainer purchaseContainer;
	private ActionBar actionBar;
	private Parser parser;
	private String currentDate;
	private String chosenDate;
	private int chosenMonth; // 1-12
	private int monthTest;
	private int dayTest;
	
	
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );
		
		currentDate = Date.getDate();
		chosenDate = currentDate.substring(0);
		chosenMonth = Integer.parseInt( Date.getMonth( chosenDate ));
		
		actionBar = getSupportActionBar();
		
		// Set display features/mode
		actionBar.setDisplayShowHomeEnabled( false );
		actionBar.setTitle( Date.getMonthName( chosenMonth ));
		
		//actionBar.setDisplayShowTitleEnabled( false );
		
		Button speakButton = (Button) findViewById( R.id.speakButton );
		purchaseListView = (ListView) findViewById( R.id.list );
		//purchaseList = new ArrayList<String>();
		//purchaseMap = new HashMap<String,String>();
		//purchaseMapList = new ArrayList<Map<String,String>>();
		//purchaseList = new ArrayList<Purchase>();
		
		purchaseContainer = PurchaseContainer.getInstance();
		pData = PurchaseDataHelper.getInstance( this );
		pidList = Math.max( pData.getLargestId(), 0 );
		parser = new Parser();
		
		
		
		//purchaseListView.setAdapter( new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, purchaseList ));
		
		// Disable button if no recognition service is present
		PackageManager pm = getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities( new Intent( RecognizerIntent.ACTION_RECOGNIZE_SPEECH ), 0 );
		if( activities.size() == 0 ) {
			speakButton.setEnabled( false );
			speakButton.setText( "Recognizer not present" );
		}
		
		String[] fromMapKey = new String[] { NAME, INFO };
		int[] toLayoutId = new int[] { android.R.id.text1, android.R.id.text2 };
		
		
		PurchaseAdapter adapter = new PurchaseAdapter( this, R.layout.list_item, purchaseContainer.purchases );
		purchaseListView.setAdapter( adapter );
		
		
		//ListAdapter listAdapter = new SimpleAdapter( this, purchaseContainer.purchaseMapList, android.R.layout.simple_list_item_2, fromMapKey, toLayoutId );
		//ListAdapter listAdapter = new SimpleAdapter( this, purchaseMapList, android.R.layout.simple_list_item_2, fromMapKey, toLayoutId );
		//purchaseListView.setAdapter( listAdapter );
		
		purchaseListView.setOnItemClickListener( new OnItemClickListener() {
			@Override
			public void onItemClick( AdapterView<?> parent, View view, int position, long id ) {
				//String field = ((TextView) view.findViewById( android.R.id.text1 )).getText().toString();
				//Toast.makeText( getBaseContext(), field , Toast.LENGTH_LONG ).show();
				//showEditPurchaseDialog( view, position );
				showAlterPurchaseDialog( view, position );
			}
		});
		


		dayTest = 1;
		monthTest = 1;
		
		if( pData.isEmpty() ) for( int i=0; i<25; i++ ) testAdd();
			
		
		setCurrentMonthDisplay();
		/*
		if( purchaseContainer.purchases.isEmpty() ) {
			ArrayList<Purchase> res = pData.getPurchasesByMonth( "20130101" );
			for( Purchase p : res ) {
				purchaseContainer.addPurchase( p );
				((BaseAdapter) purchaseListView.getAdapter()).notifyDataSetChanged();
			}
		}*/
	}
	
	private void testAdd() {
		String[] testItems = new String[] {
				"Apples", "Books", "Pears", "Cokes", "Bottles", "Caps", "Bells", "Tables", "Chairs",
				"Iphones", "TVs", "Phones", "Ipods", "Notepads", "Sofas", "DVDs", "Chargers"
		};
		
		String s = (int)(Math.random()*20 + 1) + " " + testItems[(int)(Math.random()*testItems.length-1)] + " for £" + (int)(Math.random()*99 + 1) + " " + (int)(Math.random()*99);
		Purchase purchase = parser.parse( s );
		int yearTest = Integer.parseInt( Date.getYear( purchase.date ));
		int monthTestTemp = monthTest;
		
		//int month = (int) (Math.random()*10);
		//int day = (int) (Math.random()*27);
		
		if( (int) (Math.random()*3) == 0 && dayTest < 27 ) dayTest++;
		if( (int) (Math.random()*5) == 0 && monthTest < 12 ) {
			monthTest++;
			monthTestTemp = monthTest;
			dayTest = (int) (Math.random()*15 + 1);
		}
		
		if( (int) (Math.random()*4) == 0 ) {
			yearTest--;
			monthTestTemp = 12;
		}
		
		String dval = ( dayTest < 10 ) ? "0" + String.valueOf( dayTest ) : String.valueOf( dayTest );
		String mval = ( monthTestTemp < 10 ) ? "0" + String.valueOf( monthTestTemp ) : String.valueOf( monthTestTemp );
		String yval = String.valueOf( yearTest );
		
		//String dpref = purchase.date.substring( Purchase.YEAR_START_INDEX, Purchase.MONTH_START_INDEX );
		String dsuff = purchase.date.substring( Purchase.DAY_END_INDEX, purchase.date.length() );
		
		purchase.date = yval + mval + dval + dsuff;
		System.out.println( purchase.date );
		pData.addPurchase( purchase );
		
		//purchaseContainer.addPurchase( purchase );
		//((BaseAdapter) purchaseListView.getAdapter()).notifyDataSetChanged();
	}
	
	private void setCurrentMonthDisplay() {
		actionBar.setTitle( Date.getMonthName( chosenMonth ) + " " + Date.getYear( chosenDate ));
		updatePurchaseList();
	}
	
	private void updatePurchaseList() {
		purchaseContainer.purchases.clear();
		
		ArrayList<Purchase> res = pData.getPurchasesByMonth( chosenDate );
		for( Purchase p : res ) purchaseContainer.addPurchase( p );
		((BaseAdapter) purchaseListView.getAdapter()).notifyDataSetChanged();
	}
	
	private void addPurchase( Purchase p ) {
		pData.addPurchase( p );
		
		chosenDate = p.date;
		chosenMonth = Integer.parseInt( Date.getMonth( chosenDate ));
		setCurrentMonthDisplay();
		
		//purchaseContainer.addPurchase( p );
		((BaseAdapter) purchaseListView.getAdapter()).notifyDataSetChanged();
	}
	
	private void deletePurchase( Purchase p ) {
		pData.deletePurchase( p );
		updatePurchaseList();
	}
	
	public void showManualAddPurchaseDialog() {
		AlertDialog.Builder alert = new AlertDialog.Builder( this );
		View addPurchaseDialogView = View.inflate( this, R.layout.edit_purchase_dialog, null );
		
		final EditText nameEdit = (EditText) addPurchaseDialogView.findViewById( R.id.name_edit );
		final EditText quantityEdit = (EditText) addPurchaseDialogView.findViewById( R.id.quantity_edit );
		final EditText priceEdit = (EditText) addPurchaseDialogView.findViewById( R.id.price_edit );
		final EditText currencyEdit = (EditText) addPurchaseDialogView.findViewById( R.id.currency_edit );
		
		currencyEdit.setText( DEFAULT_CURRENCY );
		
		alert.setTitle( R.string.add_purchase )
		.setView( addPurchaseDialogView )
		.setPositiveButton( R.string.add, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick( DialogInterface dialog, int whichButton ) {
				//Map<String,String> purchase = new HashMap<String,String>();
				
				int quantity = Integer.parseInt( quantityEdit.getText().toString() );
				String name = nameEdit.getText().toString();
				Price price = new Price( Double.parseDouble( priceEdit.getText().toString() ), currencyEdit.getText().toString() );
				
				
				//String p = priceEdit.getText().toString();
				//String c = currencyEdit.getText().toString();
				//String newInfo = "Price: " + p + " (" + c + ") - Quantity: " + q; 
				
				//purchase.put( NAME, nameEdit.getText().toString() );
				//purchase.put( PRICE, newInfo );
				//purchaseMapList.add( purchase );
				
				String date = Date.getDate();
				Purchase purchase = new Purchase( quantity, name, price, date );
				
				addPurchase( purchase );
				//purchaseContainer.addPurchase( purchase );
				//((BaseAdapter) purchaseListView.getAdapter()).notifyDataSetChanged();
				
			}
			
		}).setNegativeButton( android.R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick( DialogInterface dialog, int whichButton ) {
				// Do nothing
				
			}
			
		}).show();
	}
	
	public void showAlterPurchaseDialog( View view, int position ) {
		AlertDialog.Builder alert = new AlertDialog.Builder( this );
		
		final View vw = view;
		final int pos = position;
		
		
		alert.setTitle( R.string.alter_dialog_title )
		.setMessage( R.string.alter_dialog_message )
		.setPositiveButton( R.string.edit_purchase, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				showEditPurchaseDialog( vw, pos );
				
				
			}
		}).setNegativeButton( R.string.delete_purchase, new DialogInterface.OnClickListener() {
			@Override
			public void onClick( DialogInterface dialog, int which ) {
				showDeletePurchaseDialog( vw, pos );
			}
			
		}).show();

	}
	
	public void showDeletePurchaseDialog( View view, int position ) {
		AlertDialog.Builder alert = new AlertDialog.Builder( this );
		final Purchase purchase = purchaseContainer.getPurchase( position );
		
		alert.setTitle( R.string.delete_dialog_title )
		.setMessage( R.string.delete_dialog_message )
		.setPositiveButton( android.R.string.ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				deletePurchase( purchase );
				
				
			}
		}).setNegativeButton( android.R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick( DialogInterface dialog, int whichButton ) {
				// Do nothing
			}
			
		}).show();
	}
	
	public void showEditPurchaseDialog( View view, int position ) {
		AlertDialog.Builder alert = new AlertDialog.Builder( this );
		View editPurchaseDialogView = View.inflate( this, R.layout.edit_purchase_dialog, null );
		
		
		//final int index = position;
		final Purchase purchase = purchaseContainer.getPurchase( position );
		final EditText nameEdit = (EditText) editPurchaseDialogView.findViewById( R.id.name_edit );
		final EditText quantityEdit = (EditText) editPurchaseDialogView.findViewById( R.id.quantity_edit );
		final EditText priceEdit = (EditText) editPurchaseDialogView.findViewById( R.id.price_edit );
		final EditText currencyEdit = (EditText) editPurchaseDialogView.findViewById( R.id.currency_edit );
		
		/*
		nameEdit.setText(( (TextView) view.findViewById( android.R.id.text1 )).getText().toString() );
		String info = ((TextView) view.findViewById( android.R.id.text2 )).getText().toString();
		String[] infoList = parser.parsePurchaseInfo( info );
		
		quantityEdit.setText( infoList[QUANTITY_INFO] );
		priceEdit.setText( infoList[PRICE_INFO] );
		currencyEdit.setText( infoList[CURRENCY_INFO] );
		*/
		
		nameEdit.setText( purchase.name );
		quantityEdit.setText( String.valueOf( purchase.quantity ));
		priceEdit.setText( String.valueOf( purchase.price ));
		currencyEdit.setText( purchase.getCurrency() );
		
		alert.setTitle( R.string.edit_dialog_title )
		.setView( editPurchaseDialogView )
		.setPositiveButton( R.string.save, new DialogInterface.OnClickListener() {
			@Override
			public void onClick( DialogInterface dialog, int whichButton ) {
				/*
				Map<String,String> purchase = new HashMap<String,String>();
				String q = quantityEdit.getText().toString();
				String p = priceEdit.getText().toString();
				String c = currencyEdit.getText().toString();
				String newInfo = "Price: " + p + " (" + c + ") - Quantity: " + q; 
				
				purchase.put( NAME, nameEdit.getText().toString() );
				purchase.put( PRICE, newInfo ); //priceEdit.getText().toString() );
				purchaseMapList.set( index, purchase );
				((BaseAdapter) purchaseListView.getAdapter()).notifyDataSetChanged();
				*/
				
				/*
				int quantity = Integer.parseInt( quantityEdit.getText().toString() );
				String name = nameEdit.getText().toString();
				Price price = new Price( Double.parseDouble( priceEdit.getText().toString() ), currencyEdit.getText().toString() );
				*/
				
				purchase.quantity = Integer.parseInt( quantityEdit.getText().toString() );
				purchase.name = nameEdit.getText().toString();
				purchase.price = Double.parseDouble( priceEdit.getText().toString() );
				purchase.setCurrency( currencyEdit.getText().toString() );
				
				//Purchase purchase = new Purchase( quantity, name, price );
				//purchaseContainer.addPurchase( purchase );
				//purchaseContainer.changePurchase( purchase, index );
				pData.editPurchase( purchase );
				setCurrentMonthDisplay();
				//((BaseAdapter) purchaseListView.getAdapter()).notifyDataSetChanged();
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
					addPurchase( purchase );
					//purchaseContainer.addPurchase( purchase );

					/*
					Map<String,String> purchaseMap = new HashMap<String,String>();
					//purchaseMap.put( NAME, s );
					//purchaseMap.put( PRICE, "£5.00" );
					String purchaseInfo = "Price: " + purchase.price + " (" + purchase.currency + ") - Quantity: " + purchase.quantity;
					purchaseMap.put( NAME, purchase.name );
					purchaseMap.put( PRICE, purchaseInfo );
					purchaseMapList.add( purchaseMap );
					purchaseList.add( purchase );
					*/
				}

				//((BaseAdapter) purchaseListView.getAdapter()).notifyDataSetChanged();
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
		
		/*
		MenuItem prev = menu.findItem( R.id.action_next );
		if( pData.earlierPurchasesExist( chosenDate )) {
			prev.setEnabled( true );
		} else {
			prev.setEnabled( false );
		}
		
		MenuItem next = menu.findItem( R.id.action_next );
		if( Date.isEarlierMonth( chosenDate, currentDate )) {
			next.setEnabled( true );
		} else {
			next.setEnabled( false );
		}*/
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected( MenuItem item ) {
		switch( item.getItemId() ) {
			case R.id.action_add_manually:
				/*Toast.makeText( this.getBaseContext(), "Save" , Toast.LENGTH_LONG ).show();
				for( String name : pc.playerNames ) {
					pData.addProfile( pc.getPlayer( name ));
				}*/
				showManualAddPurchaseDialog();
				break;
				
			case R.id.action_settings:
				Toast.makeText( this.getBaseContext(), "Settings" , Toast.LENGTH_LONG ).show();
				break;
				
			case R.id.action_clear:
				//for( Purchase p : purchaseContainer.purchases ) {
				//	deletePurchase( p );
				//}
				
				// get purchase list
				// put in new adapter
				/*
				String[] slist = new String[] { "first", "second", "third" };
				ListAdapter newAdapter = new ArrayAdapter<String>( this, R.layout.purchase_checkbox_item, slist );
				
				purchaseListView.setAdapter( newAdapter );
				
				purchaseListView.setOnItemClickListener( new OnItemClickListener() {
					@Override
					public void onItemClick( AdapterView<?> parent, View view, int position, long id ) {
						view.setSelected( true );
					}
				});*/
				break;
				
			case R.id.action_prev:
				if( pData.earlierPurchasesExist( chosenDate )) {
					
					chosenMonth = (chosenMonth-1 == 0) ? 12 : chosenMonth-1;
					
					if( chosenMonth == 12 ) {
						chosenDate = Date.setYear( Date.setMonth( chosenDate, chosenMonth ),
													Integer.parseInt( Date.getYear( chosenDate ))-1 );
						
					} else {
						chosenDate = Date.setMonth( chosenDate, chosenMonth );
					}

					setCurrentMonthDisplay();
				}
				break;
				
			case R.id.action_next:
				if( Date.isEarlierMonth( chosenDate, currentDate )) {
				//if( pData.laterPurchasesExist( chosenDate )) {
					//item.setEnabled( true );
					chosenMonth = (chosenMonth+1 == 13) ? 1 : chosenMonth+1;
	
					if( chosenMonth == 1 ) {
						chosenDate = Date.setYear( Date.setMonth( chosenDate, chosenMonth ),
													Integer.parseInt( Date.getYear( chosenDate ))+1 );
						
					} else {
						chosenDate = Date.setMonth( chosenDate, chosenMonth );
					}
					setCurrentMonthDisplay();
					
				}/*} else {
					item.setEnabled( false );
				}*/
				break;
				
			default:
				return super.onOptionsItemSelected( item );
				
		}
		return true;
	}
}
