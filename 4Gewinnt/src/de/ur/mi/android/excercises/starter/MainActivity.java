package de.ur.mi.android.excercises.starter;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {


	private boolean isbavarian = true;
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
}
