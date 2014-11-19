package com.weiproduct.zenlead;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

public class SearchResultActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_result);

	    ActionBar actionBar = getActionBar();    
	    actionBar.setDisplayHomeAsUpEnabled(true);
		
		handleIntent(getIntent());

	}
	
	
    @Override    
    protected void onNewIntent(Intent intent) {    	
    	super.onNewIntent(intent);
    	setIntent(intent);
    	
    	handleIntent(intent);    
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_result, menu);
		return true;
	}

	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			// use the query to search your data somehow
		}
	}

}
