package de.ur.mi.android.excercises.starter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class Overview extends Activity implements MyDialog.Communicator{
	
	private static final String SERVER_IP = "192.168.2.103";
	private static final int SERVERPORT = 4444;
	
	private ArrayList<Game> myCurrentGames;
	private MyProtocol myP = new MyProtocol();
	private GameDB myDb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_overview);
		myDb = new GameDB(this);
		myDb.open();
		myCurrentGames = myDb.getCurrentGames();
		myDb.close();
		showCurrentGames();
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

		@Override
		public void onDialogMessage(String message) {
			// TODO Auto-generated method stub
			Toast.makeText(this, message, Toast.LENGTH_LONG).show();
		}
		private void showCurrentGames() {
			String[] currentGames = {"asda","asdas","asdasd"};				//new String[myCurrentGames.size()];
			/*for(int i = 0;i<foundUserList.length;i++) {
				foundUserList[i] = myCurrentGames.get(i).getP1() + "  --  " + myCurrentGames.get(i).getP2();
			}*/
			int i = R.layout.overview_child;
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.alt_overview_child, currentGames);
			
	    	
	    	ListView userListView = (ListView) findViewById(R.id.searchResultList);
	    	
	    	userListView.setAdapter(adapter);
		}

}
