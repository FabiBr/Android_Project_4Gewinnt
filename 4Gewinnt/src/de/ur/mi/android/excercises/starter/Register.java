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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends Activity {
	Socket socket = null;
	public String name;
	private GameDB db;

	// zum Testen: Server Main() einfach in Eclipse laugen lassen. Beim debuggen
	// mit echtem ger�t bei Server IP die eignene IP eingeben console ->
	// ipconfig
	// beim testen mit virtual device einfach localhost verwenden
	private static final String SERVER_IP = "132.199.206.59";
	private static final int SERVERPORT = 4444;

	private MyProtocol myP = new MyProtocol();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_register);
		DatabaseState state = ((DatabaseState) getApplicationContext());
		db = state.getDb();
		ArrayList<User> users = state.getAllUsers();
		new Thread(new ClientThread()).start();
		Button logbutton = (Button) findViewById(R.id.regcheckbutton);
		logbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					if (checkPassword()) {
						sendData();
						//new CallbackHandler().execute(socket);
						startActivity(new Intent(Register.this, Overview.class));
					} else
						Toast.makeText(Register.this,
								"Bitte zweimal das gleiche Passwort eingeben",
								Toast.LENGTH_LONG).show();
				} catch (Exception e) {
				}
			}
		});
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

	private void sendData() {

		try {
			EditText username = (EditText) findViewById(R.id.editText1);
			EditText password = (EditText) findViewById(R.id.editText2);
			String name = username.getText().toString();
			String pw = password.getText().toString();
			String output = myP.newUserDataOutput(name, pw);

			PrintWriter out = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream())), true);
			out.println(output);
			out.flush();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void safeDataLocal(String username, String pw) {
		User me = new User(1, username, pw, 0, 0, 0);
		db.open();
		db.addUser(me);
		db.close();
	}

	private class CallbackHandler extends AsyncTask<Socket, Void, String> {

		@Override
		protected String doInBackground(Socket... params) {
			Socket socket = params[0];
			try {
				BufferedReader input = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				String read = input.readLine();
				return read;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(Register.this,
					result,
					Toast.LENGTH_LONG).show();
			super.onPostExecute(result);
		}
	}

	class ClientThread implements Runnable {

		@Override
		public void run() {

			try {
				// InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

				socket = new Socket(SERVER_IP, SERVERPORT);
				System.out.println("gr8 success very nice");

			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
