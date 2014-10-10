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
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends Activity {
	// Main Game
	TableLayout myLayout;
	Field Field;
	int playernumber = 1;
	int counter = 0;
	GameWinCheck win;
	
	private static final String SERVER_IP = "hiersollteetwaseinfallsreichesstehen.de";
	private static final int SERVERPORT = 1939;
	
	private MyProtocol myP;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*
		myP = new MyProtocol();
		Bundle bundle = getIntent().getExtras();
		String gameId = bundle.getString("gameId");
		//new ServerSynch().execute(gameId);
*/
		setContentView(R.layout.game);
		
		

		setContentView(R.layout.game);
		Field = new Field();
		win = new GameWinCheck(Field);


		updateField();
		try {
			textviewrun();
		} catch (Exception e) {
		}
		Toast.makeText(GameActivity.this, getText(R.string.gamestart),
				Toast.LENGTH_LONG).show();

	}

	/*
	 * Erstellt fuer jede Reihe einen Listener
	 */
	private void textviewrun() {
		myLayout = (TableLayout) findViewById(R.id.layout);
		TableRow playground = (TableRow) findViewById(R.id.playground);
		for (int i = 0; i < playground.getChildCount(); i++) {
			final LinearLayout row = (LinearLayout) playground.getChildAt(i);
			final int rownumber = i;
			for (int j = 0; j < row.getChildCount(); j++) {
				row.getChildAt(j).setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						try {
							clicklistener(row, rownumber);
						} catch (Exception e) {
						}
					}
				});
			}
		}

		// Button Click Listener
		((Button) findViewById(R.id.Button))
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						try {
							setContentView(R.layout.game);
							try {
								textviewrun();
								Field = new Field();
								playernumber = 1;
								counter = 0;

							} catch (Exception e) {
							}
						} catch (Exception e) {
						}
					}
				});
		
		((Button) findViewById(R.id.Mainmenue))
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						try {
							setContentView(R.layout.game);
							try {
								startActivity(new Intent(GameActivity.this,
										MainActivity.class));

							} catch (Exception e) {
							}
						} catch (Exception e) {
						}
					}
				});
		((TextView) findViewById(R.id.currentitem))
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						try {
							if (Field.getExtrasOfPlayer(playernumber)) {
								Toast.makeText(GameActivity.this,
										"Setze den Blocker in eine Reihe.",
										Toast.LENGTH_LONG).show();
							}
						} catch (Exception e) {
						}
					}
				});
	}

	private boolean isBlocked(int rownumber){
		if(Field.getField(0, rownumber) < 0){
			return true;
		}
		return false;
	}
	
	
	private void updateField(){
		TableRow playground = (TableRow) findViewById(R.id.playground);
		LinearLayout row;
		TextView stone;
			
		for(int i = 0; i< 7;i++){
			row = (LinearLayout) playground.getChildAt(i);
			for(int j = 0; j <6; j++){
				stone = (TextView) row.getChildAt(j);
				if(Field.getField(j, i)== 0){
					stone.setBackgroundResource(R.drawable.weiss);
				}
				if(Field.getField(j, i)== 1){
					stone.setBackgroundResource(R.drawable.breze);
				}
				if(Field.getField(j, i)== 2){
					stone.setBackgroundResource(R.drawable.bier);
				}
				if(Field.getField(j, i)== 3){
					stone.setBackgroundResource(R.drawable.extra);
				}
				if(Field.getField(j, i) < 0){
					stone.setBackgroundResource(R.drawable.ic_launcher);
				}
			}
		}
	}
	/*
	 * Checker for next free Field in row
	 */
	private int nextfree(int rownumber) {
		for (int i = 5; i >= 0; i--) {
			int checknum = Field.getField(i, rownumber);
			if (checknum == 3) {
				return i;
			}
			if (checknum < 0) {
				return 6;
			}
			if (checknum == 0)
				return i;

		}
		return -1;
	}

	protected void clicklistener(LinearLayout row, int rownumber) {
		// alle Daten von gamecontroller abrufen -> hier ausfuehren
		int bottom = nextfree(rownumber);
		if (bottom < 6 && Field.getExtrasOfPlayer(playernumber) && bottom > 0) {
			// extra setzen 
			TextView stone = (TextView) row.getChildAt(0);
			stone.setBackgroundResource(R.drawable.ic_launcher);
			Field.setField(0, rownumber, -2);
			Field.setExtrasOfPlayer(playernumber, false);
		} else if (bottom < 6 && !isBlocked(rownumber)) {
			// stein setzen
			
			for(int i = 0; i < 7; i++){
				if(isBlocked(i)){
					int var =Field.getField(0, i)+1;
					Field.setField(0, i, var);
					if ( Field.getField(0,i)==0){
						TextView stone = (TextView) row.getChildAt(0);
						stone.setBackgroundResource(R.drawable.weiss);
					}
						
				}
			}
			setstones(bottom, rownumber, row);
		} else {
			Toast.makeText(GameActivity.this, "Bist ebba dodal deppad woan",
					Toast.LENGTH_LONG).show();
		}

	}

	/*
	 * Main Method - Setting of all fields
	 */
	private void setstones(int bottom, int rownumber, LinearLayout row) {
		TextView player = ((TextView) findViewById(R.id.iscurrentlyplaying));
		TextView bottomstone = (TextView) row.getChildAt(bottom);
		TextView playericon = ((TextView) findViewById(R.id.currentPlayerIcon));
		
		if(Field.getField(bottom,rownumber)==3)Field.setExtrasOfPlayer(playernumber, true);

		
		if (playernumber == 1) {
			bottomstone.setBackgroundResource(R.drawable.breze);
			playericon.setBackgroundResource(R.drawable.bier);
			playernumber = 2;
			updateField();
			player.setText(R.string.hansl2);
			Field.setField(bottom, rownumber, 1);
			playchecks(bottom, rownumber, row);
			

		} else if (playernumber == 2) {
			bottomstone.setBackgroundResource(R.drawable.bier);
			playericon.setBackgroundResource(R.drawable.breze);
			playernumber = 1;
			updateField();
			player.setText(R.string.hansl1);
			Field.setField(bottom, rownumber, 2);
			playchecks(bottom, rownumber, row);


		}
		
		for (int i = 0; i < 7; i++){
			if(nextfree(i)<0){
				Field.setField(nextfree(i), i,nextfree(i)+1);
			}
		}
		updateField();
		
		if (Field.getExtrasOfPlayer(playernumber)) {
			TextView currentitem = (TextView) findViewById(R.id.currentitem);
			currentitem.setBackgroundResource(R.drawable.extra);
		}
		else{
			TextView currentitem = (TextView) findViewById(R.id.currentitem);
			currentitem.setBackgroundResource(R.drawable.keinextra);
		}
		
		if (playernumber == 0) {
			Button button = (Button) findViewById(R.id.Button);
			button.setText("Gwunna! Nummol?");
			button.setBackgroundColor(getResources().getColor(R.color.green));
		}
		updateField();
	}
	
	
	
	
	
	
	
	

	private void playchecks(int bottom, int rownumber, LinearLayout row) {
		if (win.wincheck())
			playernumber = 0;
		counter++;
		drawcheck();
		extras(bottom, rownumber, row);

	}

	private void extras(int bottom, int rownumber, LinearLayout row) {
		// jedes 5. mal wenn spiel begonnen wenn max vorletzter stein gesetzt
		if (counter % 5 == 0 && counter > 0 && bottom > 0) {
			TextView bottomstone = (TextView) row.getChildAt(bottom - 1);
			bottomstone.setBackgroundResource(R.drawable.extra);
			Field.setField(bottom - 1, rownumber, 3);

		}
	}

	/*
	 * Checker for Drawing
	 */
	private void drawcheck() {
		if (counter == 42) {
			playernumber = 0;
			Button button = (Button) findViewById(R.id.Button);
			button.setText("Unentschieden. Nochmal?");
			button.setBackgroundColor(getResources().getColor(R.color.green));
		}
	}

	
	class ServerSynch extends AsyncTask<String, Void, String> {
		Socket socket = null;

		@Override
		protected String doInBackground(String... params) {
			try {
				socket = new Socket(SERVER_IP, SERVERPORT);
				System.out.println("gr8 success very nice");
				String gamesData = sendRequest(params[0]);
				return gamesData;

			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			JSONArray gamesList;
			try {
				gamesList = new JSONArray(result);
				
			} catch (JSONException e) {
				e.printStackTrace();
			}

			// state.setAllUsers(result);
		}
		
		private String sendRequest(String gameId) {
			
			try {
				
				String output = myP.getGameById(gameId);
				PrintWriter out = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(socket.getOutputStream())), true);
				out.println(output);
				out.flush();
				BufferedReader input = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));
				String gameData = input.readLine();
				System.out.println();
				return gameData;
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}
