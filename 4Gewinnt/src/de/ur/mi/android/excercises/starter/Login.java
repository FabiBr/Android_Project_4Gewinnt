package de.ur.mi.android.excercises.starter;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {

	Socket socket = null;
	private static final String SERVER_IP = null;
	private static final int SERVERPORT = 4444;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		Button logbutton = (Button) findViewById(R.id.logcheckbutton);
		logbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					if (checkUser()) {
						startActivity(new Intent(Login.this, Overview.class));
					} else
						Toast.makeText(
								Login.this,
								"Falsche Zugangsdaten. Eventuell neu registrieren?",
								Toast.LENGTH_LONG).show();
				} catch (Exception e) {
				}

			}
		});

	}

	private boolean checkUser() {
		String s_name, s_pw;
		EditText name = (EditText) findViewById(R.id.editText4);
		EditText pw = (EditText) findViewById(R.id.editText5);
		s_name = name.getText().toString();
		s_pw = pw.getText().toString();
		// TODO: aus Datenbank user abrufen
		return false;
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
