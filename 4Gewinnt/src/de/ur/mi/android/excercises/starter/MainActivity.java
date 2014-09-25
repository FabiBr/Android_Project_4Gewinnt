package de.ur.mi.android.excercises.starter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity {
	private static final String SERVER_IP = "132.199.191.205";
	private static final int SERVERPORT = 4444;
	private MyProtocol myP = new MyProtocol();
	


	private boolean isbavarian = true;
	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("App start");
		super.onCreate(savedInstanceState);
		//new ServerSynch().execute();
		setContentView(R.layout.activity_main);
		textviewrun();
	}

	private void textviewrun() {

		// Log Button Click Listener
		Button logbutton = (Button) findViewById(R.id.LoginButton);
		logbutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					startActivity(new Intent(MainActivity.this, Login.class));
				} catch (Exception e) {
				}
			}
		});

		// Reg Button Click Listener
		Button regbutton = (Button) findViewById(R.id.RegButton);
		regbutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					startActivity(new Intent(MainActivity.this, Register.class));
				} catch (Exception e) {
				}
			}
		});
		
		//Lagnuage Button
		Button lanbutton = (Button) findViewById(R.id.language);
		lanbutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					//startActivity(new Intent(MainActivity.this, MainActivity.class));
					setlanguage();
				} catch (Exception e) {
				}
			}

			
		});
	}
	private void setlanguage() {
		
		if(isbavarian)setgerman();
		else setbavarian();
			
	}

	private void setbavarian() {
		isbavarian = true;
		Locale mLocale = new Locale("");
		Locale.setDefault(mLocale); 
	    Configuration config = getBaseContext().getResources().getConfiguration(); 
	    if (!config.locale.equals(mLocale)) 
	    { 
	        config.locale = mLocale; 
	        getBaseContext().getResources().updateConfiguration(config, null); 
	    }
	    setContentView(R.layout.activity_main);
	    textviewrun();
	}

	private void setgerman() {
		isbavarian = false;
		Locale mLocale = new Locale("pr");
	    Locale.setDefault(mLocale); 
	    Configuration config = getBaseContext().getResources().getConfiguration(); 
	    if (!config.locale.equals(mLocale)) 
	    { 
	        config.locale = mLocale; 
	        getBaseContext().getResources().updateConfiguration(config, null); 
	    }
	    setContentView(R.layout.activity_main);
	    textviewrun();
	}
	
	class ServerSynch extends AsyncTask<Void, Void, String> {
		Socket socket = null;
		String data = null;

		@Override
		protected String doInBackground(Void... params) {
			try {
				socket = new Socket(SERVER_IP, SERVERPORT);
				System.out.println("gr8 success very nice");
				sendRequest();
				return data;

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
				processJsonArray(userList);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//state.setAllUsers(result);
		}
		private void processJsonArray(JSONArray userList) throws JSONException {
			ArrayList<User> users = new ArrayList<User>();
			for(int i = 0; i < userList.length();i++) {
				String id = userList.getJSONArray(i).getString(1);
				String username = userList.getJSONArray(i).getString(2);
				String pw = userList.getJSONArray(i).getString(3);
				String wins = userList.getJSONArray(i).getString(4);
				String loses = userList.getJSONArray(i).getString(5);
				String premium = userList.getJSONArray(i).getString(6);
				User newUser = new User(Integer.parseInt(id), username, pw, Integer.parseInt(wins), Integer.parseInt(loses), Integer.parseInt(premium));
				users.add(newUser);
			}
			DatabaseState state = ((DatabaseState) getApplicationContext());
			state.setAllUsers(users);
		}
		
		private void sendRequest() {
			
			try {
				String output = myP.getAllUsers();
				PrintWriter out = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(socket.getOutputStream())), true);
				out.println(output);
				out.flush();
				BufferedReader input = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				data = input.readLine();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
