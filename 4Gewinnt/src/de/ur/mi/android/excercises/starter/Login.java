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

public class Login extends Activity {

	// Socket socket = null;
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
		// new Thread(new ClientThread()).start();
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
				.getSystemService(Login.this.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

		if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
			Toast.makeText(Login.this, "No Internet connection!",
					Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

	private boolean checkUser() {
		if (callback.equals("1")) {
			EditText username = (EditText) findViewById(R.id.editText4);
			String name = username.getText().toString();
			myDb.open();
			myDb.updateMyCurrentData(name);
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
				// InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

				socket = new Socket(SERVER_IP, SERVERPORT);
				System.out.println("gr8 success very nice");

			} catch (UnknownHostException e1) {
				Toast.makeText(Login.this, "Keine Internetverbindung",
						Toast.LENGTH_LONG).show();
				e1.printStackTrace();
			} catch (IOException e1) {
				Toast.makeText(Login.this, "Keine Internetverbindung",
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
				startActivity(new Intent(Login.this, Overview.class));
			} else
				Toast.makeText(Login.this,
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

	/*
	 * class ClientThread implements Runnable {
	 * 
	 * 
	 * @Override public void run() {
	 * 
	 * try { // InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
	 * 
	 * socket = new Socket(SERVER_IP, SERVERPORT);
	 * System.out.println("gr8 success very nice");
	 * 
	 * } catch (UnknownHostException e1) { e1.printStackTrace(); } catch
	 * (IOException e1) { e1.printStackTrace(); } } }
	 */
}
