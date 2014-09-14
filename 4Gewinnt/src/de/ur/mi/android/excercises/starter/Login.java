package de.ur.mi.android.excercises.starter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		Button logbutton = (Button) findViewById(R.id.logcheckbutton);
		logbutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					if(checkUser()){
					startActivity(new Intent(Login.this, Overview.class));}
					else Toast.makeText(Login.this, "Falsche Zugangsdaten. Eventuell neu registrieren?", Toast.LENGTH_LONG).show();
				} catch (Exception e) {
				}
				
			}
		});
		
			
	}

	private boolean checkUser() {
		String s_name,s_pw;
		EditText name = (EditText) findViewById(R.id.editText4);
		EditText pw = (EditText) findViewById(R.id.editText5);
		s_name = name.getText().toString();
		s_pw = pw.getText().toString();
		//TODO: aus Datenbank user abrufen
		return false;
	}
}
