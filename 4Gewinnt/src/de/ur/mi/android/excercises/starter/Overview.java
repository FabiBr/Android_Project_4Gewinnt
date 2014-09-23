package de.ur.mi.android.excercises.starter;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SearchView;

public class Overview extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_overview);
		Button spielbutton = (Button)findViewById(R.id.gamestart);
		spielbutton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(Overview.this, GameActivity.class));
				
			}
			
		});
				}
	
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        MenuInflater inflater = getMenuInflater();
	        inflater.inflate(R.menu.activity_main_actions, menu);
	 
	     // Associate searchable configuration with the SearchView
	        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
	                .getActionView();
	        searchView.setSearchableInfo(searchManager
	                .getSearchableInfo(getComponentName()));
	        
	        return super.onCreateOptionsMenu(menu);
	    }
	 
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        // Take appropriate action for each action item click
	        switch (item.getItemId()) {
	        case R.id.action_search:
	        	System.out.println("SUCHINTENT GESTARTET !!");
	        	System.out.println("SUCHINTENT GESTARTET !!");
	        	System.out.println("SUCHINTENT GESTARTET !!");
	            // search action
	            return true;
	        default:
	        	System.out.println("SUCHE FEHLGESCHLAGEN !!");
	        	System.out.println("SUCHE FEHLGESCHLAGEN !!");
	        	System.out.println("SUCHE FEHLGESCHLAGEN !!");
	        	 return super.onOptionsItemSelected(item);
	        }
	    }

}
