package de.ur.mi.android.excercises.starter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {


	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("App start");
		super.onCreate(savedInstanceState);
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
	}
}
