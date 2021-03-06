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

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	
	public String name;
	private GameDB db;

	private static final String SERVER_IP = "hiersollteetwaseinfallsreichesstehen.de";
	private static final int SERVERPORT = 1939;

	private MyProtocol myP = new MyProtocol();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_register);
		db = new GameDB(this);
		Button logbutton = (Button) findViewById(R.id.regcheckbutton);
		logbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					if (checkPassword() && isOnline()) {
						new CallbackHandler().execute();
					} else
						Toast.makeText(RegisterActivity.this,
								"Bitte zweimal das gleiche Passwort eingeben",
								Toast.LENGTH_LONG).show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public boolean isOnline() {
		ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext()
				.getSystemService(RegisterActivity.this.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

		if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
			Toast.makeText(RegisterActivity.this, "No Internet connection!",
					Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

	private boolean checkPassword() {
		String s_name = null, s_pw, s_pwwh;
		EditText name = (EditText) findViewById(R.id.editText1);
		EditText pw = (EditText) findViewById(R.id.editText2);
		EditText pwwh = (EditText) findViewById(R.id.editText3);
		s_name = name.getText().toString();
		s_pw = pw.getText().toString();
		s_pwwh = pwwh.getText().toString();
		if (s_name != null && s_pw != null &&s_pw.equals(s_pwwh)) {
			this.name = s_name;
			safeDataLocal(s_name, s_pw);
			return true;
		}
		return false;
	}

private void safeDataLocal(String username, String pw) {
		User me = new User(1, username, pw, 0, 0, 0);
		db.open();
		db.addUser(me);
		db.updateMyCurrentData(me);
		db.close();
	}

	private class CallbackHandler extends AsyncTask<Void, Void, String> {
		Socket socket = null;

		@Override
		protected String doInBackground(Void... params) {
			try {
				socket = new Socket(SERVER_IP, SERVERPORT);
				System.out.println("gr8 success very nice");

			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			String read = sendData();
			return read;
		}
		
		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(RegisterActivity.this,
					result,
					Toast.LENGTH_LONG).show();
			startActivity(new Intent(RegisterActivity.this, OverviewActivity.class));
			super.onPostExecute(result);
		}
		
		private String sendData() {

			try {
				EditText username = (EditText) findViewById(R.id.editText1);
				EditText password = (EditText) findViewById(R.id.editText2);
				String name = username.getText().toString();
				String pw = password.getText().toString();
				String output = myP.newUserDataOutput(name, pw);

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
	}
}
