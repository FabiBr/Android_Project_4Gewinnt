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
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONException;

import de.ur.mi.android.excercises.starter.SearchResultsActivity.contactOpponent;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class OverviewActivity extends Activity implements MyDialog.Communicator{
	
	private static final String SERVER_IP = "hiersollteetwaseinfallsreichesstehen.de";
	private static final int SERVERPORT = 1939;
	
	private ArrayList<Game> myCurrentGames;
	private MyProtocol myP = new MyProtocol();
	private GameDB myDb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_overview);
		myDb = new GameDB(this);
		new ServerSynch().execute();
		Button spielbutton = (Button)findViewById(R.id.gamestart);
		spielbutton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(OverviewActivity.this, Gameoffline.class));
				
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
			String[] currentGames = new String[myCurrentGames.size()];
			
			for(int i = 0; i < currentGames.length;i++) {
				Game game = myCurrentGames.get(i);
				currentGames[i] = game.getGameId() + " | " + game.getP1() + " - " + game.getP2();
			}

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.alt_overview_child, currentGames);
			
	    	
	    	ListView userListView = (ListView) findViewById(R.id.searchResultList);
	    	
	    	userListView.setAdapter(adapter);
		}
		
		class ServerSynch extends AsyncTask<Void, Void, String> {
			Socket socket = null;

			@Override
			protected String doInBackground(Void... params) {
				try {
					socket = new Socket(SERVER_IP, SERVERPORT);
					System.out.println("gr8 success very nice");
					String gamesData = sendGamesRequest();
					return gamesData;

				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				JSONArray gamesList;
				try {
					gamesList = new JSONArray(result);
					processGamesJsonArray(gamesList);
					showCurrentGames();
					registerClickCallback();
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			private void registerClickCallback() {
				ListView userListView = (ListView) findViewById(R.id.searchResultList);
				userListView
						.setOnItemClickListener(new AdapterView.OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
								TextView textView = (TextView) view;
								String info = textView.getText().toString();
								StringTokenizer tk = new StringTokenizer(info);
								String gameId = tk.nextToken();
								Bundle bundle = new Bundle();
								bundle.putString("gameId", gameId);
								Intent i = new Intent(OverviewActivity.this, GameActivity.class);
								i.putExtras(bundle);
								startActivity(i);
							}
						});
			}
			
			private void processGamesJsonArray(JSONArray gamesList) throws JSONException {
				ArrayList<Game> allGames = new ArrayList<Game>();
				for (int i = 0; i < gamesList.length(); i++) {
					
					String id = gamesList.getJSONArray(i).getString(1);
					String field = gamesList.getJSONArray(i).getString(2);
					String user1 = gamesList.getJSONArray(i).getString(3);
					String user2 = gamesList.getJSONArray(i).getString(4);
					String currentUser = gamesList.getJSONArray(i).getString(5);
					
					Game newGame = new Game(Integer.parseInt(id), field, user1,
							user2, currentUser);
					allGames.add(newGame);
				}
				myCurrentGames = allGames;
				System.out.println();
			}

			private String sendGamesRequest() {
				
				try {
					myDb.open();
					String myUsername = myDb.getMyData().getUsername();
					String output = myP.getMyCurrentGames(myUsername);
					myDb.close();
					PrintWriter out = new PrintWriter(new BufferedWriter(
							new OutputStreamWriter(socket.getOutputStream())), true);
					out.println(output);
					out.flush();
					BufferedReader input = new BufferedReader(
							new InputStreamReader(socket.getInputStream()));
					String gameData = input.readLine();
					System.out.println();
					return gameData;
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		}
}
