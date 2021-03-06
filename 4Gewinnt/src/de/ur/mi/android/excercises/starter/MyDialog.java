package de.ur.mi.android.excercises.starter;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MyDialog extends DialogFragment implements View.OnClickListener{
	Communicator communicator;
	Button yes;
	Button no;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.my_dialog, null);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		yes = (Button) view.findViewById(R.id.AcceptButton);
		no = (Button) view.findViewById(R.id.DeclineButton);
		yes.setOnClickListener(this);
		no.setOnClickListener(this);
		setCancelable(false);
		return view;
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		communicator = (Communicator) activity;
	}

	interface Communicator
	{
		public void onDialogMessage(String message);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId()==R.id.AcceptButton){
			dismiss();
			communicator.onDialogMessage("Yes Button was clicked");
			//Intent zum Spielfeld
			//Serveraufbau
		}
		else{
			dismiss();
			communicator.onDialogMessage("No Button was clicked");
		}
	}

}
