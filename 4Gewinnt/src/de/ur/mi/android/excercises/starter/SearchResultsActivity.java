package de.ur.mi.android.excercises.starter;

import java.sql.JDBCType;
import java.util.ArrayList;
import java.util.zip.Inflater;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
 
public class SearchResultsActivity extends Activity {
 
    private TextView txtQuery;
    private String[] userList;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        
        //
        Intent intent = getIntent();
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
        	String query = intent.getStringExtra(SearchManager.QUERY);
        	showListResult(query);
        }
        //
 
        // get the action bar
        ActionBar actionBar = getActionBar();
 
        // Enabling Back navigation on Action Bar icon
        actionBar.setDisplayHomeAsUpEnabled(true);
 
        txtQuery = (TextView) findViewById(R.id.txtQuery);
 
        handleIntent(getIntent());
        registerClickCallback();
    }
    
    private void showListResult(String s){
    	ArrayList<User> users;
    	DatabaseState state = new DatabaseState();
    	users = state.getAllUsers();//new ArrayList<User>();//
    	//User mario = new User(1,"mario","123",0,0,0);
    	//users.add(mario);
    	
    	
    	
    	userList = new String[users.size()];
    	for(int i = 0; i < userList.length; i++){
    		userList[i] = users.get(i).getUsername();
    	}

    	
    	String[] foundUserList = search(s, userList);
    	
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.search_result_list_child, foundUserList);
    	
    	ListView userListView = (ListView) findViewById(R.id.searchResultList);
    	
    	userListView.setAdapter(adapter);	
        
    }
    
    private void registerClickCallback(){
    	ListView userListView = (ListView) findViewById(R.id.searchResultList);
    	userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TextView textView = (TextView) view;
				String message = "Du hast " + textView.getText().toString() + " geklickt";
				Toast.makeText(SearchResultsActivity.this, message, Toast.LENGTH_SHORT).show();
				showDialog();//textView.getText().toString());
			}
		});
    }
    
    public void showDialog(){//String spieler){
    	FragmentManager manager= getFragmentManager();
    	MyDialog dialog = new MyDialog();
    	dialog.show(manager,"dialog");
    }
    
    private String[] search(String s, String[] userList){
    	ArrayList<String> results = new ArrayList<String>();
    	
    	for(int i = 0; i<userList.length; i++){
    		if(userList[i].toLowerCase().contains(s.toLowerCase())){
    			results.add(userList[i]);
    		}
    	}
    	
    	String[] resultList = new String[results.size()];
    	for(int i = 0; i<resultList.length; i++){
    		resultList[i] = results.get(i);
    	}
    	return resultList;
    }
 
    @Override
    protected void onNewIntent(Intent intent) {
    	super.onNewIntent(intent);
    	setIntent(intent);
        handleIntent(intent);
    }
 
    /**
     * Handling intent data
     */
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY); 
            /**
             * Use this query to display search results like 
             * 1. Getting the data from SQLite and showing in listview 
             * 2. Making webrequest and displaying the data 
             * For now we just display the query only
             */
            
            
            
            txtQuery.setText("Du suchst an " + query);
 
        }
 
    }
}