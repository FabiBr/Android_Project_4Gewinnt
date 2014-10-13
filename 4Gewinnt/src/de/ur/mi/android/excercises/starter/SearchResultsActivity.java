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
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchResultsActivity extends Activity {

	private TextView txtQuery;
	private String[] userList;
	private GameDB myDb;

	private static final String SERVER_IP = "hiersollteetwaseinfallsreichesstehen.de";
	private static final int SERVERPORT = 1939;

	private MyProtocol myP = new MyProtocol();
	private ArrayList<User> users;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myDb = new GameDB(this);
		new ServerSynch().execute();
		setContentView(R.layout.activity_search_results);
	}

	private void showListResult(String s) {
		userList = new String[users.size()];
		for (int i = 0; i < userList.length; i++) {
			userList[i] = users.get(i).getUsername();
		}

		String[] foundUserList = search(s, userList);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.search_result_list_child, foundUserList);

		ListView userListView = (ListView) findViewById(R.id.searchResultList);

		userListView.setAdapter(adapter);

	}

	private void registerClickCallback() {
		ListView userListView = (ListView) findViewById(R.id.searchResultList);
		userListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						TextView textView = (TextView) view;
						String message = "Du hast "
								+ textView.getText().toString() + " herausgefordert";
						Toast.makeText(SearchResultsActivity.this, message,
								Toast.LENGTH_SHORT).show();
						new contactOpponent().execute(textView.getText()
								.toString());					}
				});
	}

	public void showDialog() {
		FragmentManager manager = getFragmentManager();
		MyDialog dialog = new MyDialog();
		dialog.show(manager, "dialog");
	}

	private String[] search(String s, String[] userList) {
		ArrayList<String> results = new ArrayList<String>();

		for (int i = 0; i < userList.length; i++) {
			if (userList[i].toLowerCase().contains(s.toLowerCase())) {
				results.add(userList[i]);
			}
		}

		String[] resultList = new String[results.size()];
		for (int i = 0; i < resultList.length; i++) {
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
			 * Use this query to display search results like 1. Getting the data
			 * from SQLite and showing in listview 2. Making webrequest and
			 * displaying the data For now we just display the query only
			 */
			txtQuery.setText(getResources().getString(R.string.suchtext) +" " + query);

		}

	}

	class contactOpponent extends AsyncTask<String, Void, String> {
		Socket socket = null;

		@Override
		protected String doInBackground(String... params) {
			try {
				socket = new Socket(SERVER_IP, SERVERPORT);
				System.out.println("gr8 success very nice");
				String data = sendRequest(params[0]);
				return data;

			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			return null;
		}

		private String sendRequest(String opponentName) {
			try {
				myDb.open();
				String myName = myDb.getMyData().getUsername();
				myDb.close();
				
				String output = myP.addNewGame(myName, opponentName);
				PrintWriter out = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(socket.getOutputStream())), true);
				BufferedReader input = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				out.println(output);
				out.flush();
				String read = input.readLine();
				return read;
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "User already exists, pick another username";
		}


		@Override
		protected void onPostExecute(String result) {
			
			startActivity(new Intent(SearchResultsActivity.this, OverviewActivity.class));
		}

	}

	class ServerSynch extends AsyncTask<Void, Void, String> {
		Socket socket = null;

		@Override
		protected String doInBackground(Void... params) {
			try {
				socket = new Socket(SERVER_IP, SERVERPORT);
				System.out.println("gr8 success very nice");
				String userData = sendUserRequest();
				return userData;

			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			JSONArray userList;
			try {
				userList = new JSONArray(result);
				processUserJsonArray(userList);
				searchQuery();
				registerClickCallback();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		private void processUserJsonArray(JSONArray userList) throws JSONException {
			ArrayList<User> allUsers = new ArrayList<User>();
			for (int i = 0; i < userList.length(); i++) {
				String id = userList.getJSONArray(i).getString(1);
				String username = userList.getJSONArray(i).getString(2);
				String pw = userList.getJSONArray(i).getString(3);
				String wins = userList.getJSONArray(i).getString(4);
				String loses = userList.getJSONArray(i).getString(5);
				String premium = userList.getJSONArray(i).getString(6);
				User newUser = new User(Integer.parseInt(id), username, pw,
						Integer.parseInt(wins), Integer.parseInt(loses),
						Integer.parseInt(premium));
				allUsers.add(newUser);
			}
			users = allUsers;
			System.out.println();
		}

		private void searchQuery() {
			Intent intent = getIntent();
			if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
				String query = intent.getStringExtra(SearchManager.QUERY);
				showListResult(query);
			}
			// get the action bar
			ActionBar actionBar = getActionBar();

			// Enabling Back navigation on Action Bar icon
			actionBar.setDisplayHomeAsUpEnabled(true);

			txtQuery = (TextView) findViewById(R.id.txtQuery);

			handleIntent(getIntent());
		}

		private String sendUserRequest() {

			try {
				String output = myP.getAllUsers();
				PrintWriter out = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(socket.getOutputStream())), true);
				out.println(output);
				out.flush();
				BufferedReader input = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));
				String inputString = input.readLine();
				System.out.println(inputString);
				return inputString;
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