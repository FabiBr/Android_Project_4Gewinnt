package de.ur.mi.android.excercises.starter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private static final String SERVER_IP = "hiersollteetwaseinfallsreichesstehen.de";
	private static final int SERVERPORT = 1939;
	private MyProtocol myP = new MyProtocol();
	private String callback = "0";
	private GameDB myDb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		myDb = new GameDB(this);
		Button logbutton = (Button) findViewById(R.id.logcheckbutton);
		logbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					if(isOnline()) {
						new CallbackHandler().execute();
					}
				} catch (Exception e) {
				}
			}
		});

	}

	public boolean isOnline() {
		ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext()
				.getSystemService(LoginActivity.this.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

		if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
			Toast.makeText(LoginActivity.this, "No Internet connection!",
					Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

	private boolean checkUser() {
		if (!callback.equals("0")) {
			EditText username = (EditText) findViewById(R.id.editText4);
			String name = username.getText().toString();
			JSONArray userData;
			User me = null;
			try {
				userData = new JSONArray(callback);
				me = new User(userData.getInt(0), userData.getString(1), userData.getString(2), userData.getInt(3), userData.getInt(4), userData.getInt(5));

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			myDb.open();
			myDb.updateMyCurrentData(me);
			myDb.close();
			return true;
		}
		return false;
	}

	private class CallbackHandler extends AsyncTask<Void, Void, String> {
		private BufferedReader input;
		Socket socket = null;

		@Override
		protected String doInBackground(Void... arg0) {

			try {
				socket = new Socket(SERVER_IP, SERVERPORT);
				System.out.println("gr8 success very nice");

			} catch (UnknownHostException e1) {
				Toast.makeText(LoginActivity.this, "Keine Internetverbindung",
						Toast.LENGTH_LONG).show();
				e1.printStackTrace();
			} catch (IOException e1) {
				Toast.makeText(LoginActivity.this, "Keine Internetverbindung",
						Toast.LENGTH_LONG).show();
				e1.printStackTrace();
			}
			String read = sendData();
			return read;
		}

		@Override
		protected void onPostExecute(String result) {
			callback = result;
			super.onPostExecute(result);
			if (checkUser()) {
				startActivity(new Intent(LoginActivity.this, OverviewActivity.class));
			} else
				Toast.makeText(LoginActivity.this,
						"Falsche Zugangsdaten. Eventuell neu registrieren?",
						Toast.LENGTH_LONG).show();
		}

		private String sendData() {
			try {
				EditText username = (EditText) findViewById(R.id.editText4);
				EditText password = (EditText) findViewById(R.id.editText5);
				String name = username.getText().toString();
				String pw = password.getText().toString();
				String output = myP.loginPwCheck(name, pw);

				PrintWriter out = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(socket.getOutputStream())), true);
				input = new BufferedReader(new InputStreamReader(
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
			return "";

		}
	}
}
