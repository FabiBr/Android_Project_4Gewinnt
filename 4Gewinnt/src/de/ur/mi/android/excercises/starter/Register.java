package de.ur.mi.android.excercises.starter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		Button logbutton = (Button) findViewById(R.id.regcheckbutton);
		logbutton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						if(checkPassword()){
						startActivity(new Intent(Register.this,
								Overview.class));}
						else Toast.makeText(Register.this, "Bitte zweimal das gleiche Passwort eingeben", Toast.LENGTH_LONG).show();
					} catch (Exception e) {
					}
				}
			});
		}

	

	private boolean checkPassword() {
		String s_name,s_pw,s_pwwh;
		EditText name = (EditText) findViewById(R.id.editText1);
		EditText pw = (EditText) findViewById(R.id.editText2);
		EditText pwwh = (EditText) findViewById(R.id.editText3);
		s_name = name.getText().toString();
		s_pw = pw.getText().toString();
		s_pwwh = pwwh.getText().toString();
		System.out.println(s_pw);
		System.out.println(s_pwwh);
		//TODO: in Datenbank speichern
		if (s_pw.equals(s_pwwh)) return true;
		return false;
	}

}