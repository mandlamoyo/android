package com.mandla.pokerscorekeeper;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

public class TabListener implements ActionBar.TabListener {

	Fragment fragment;

	public TabListener( Fragment fragment ) {
		this.fragment = fragment;
	}
	
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		ft.replace( R.id.fragment_container, fragment );
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		ft.remove( fragment );
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	
	
}
