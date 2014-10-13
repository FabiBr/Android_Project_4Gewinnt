package de.ur.mi.android.excercises.starter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends Activity {
	// Main Game
	private Field Field;
	private int playernumber = 1;
	// private int counter = 0;
	private GameWinCheck win;
	private boolean ExtraCanBeSet = false;
	private MediaPlayer mp2;
	private boolean muted = false;
	private Game thisGame;

	private static final String SERVER_IP = "192.168.2.102";
	private static final int SERVERPORT = 1939;
	private MyProtocol myP;
	private GameDB myDb;
	private String gameId;
	private User me;
	private Handler myHandler;
	private boolean gameOver = false;

	/*
	 * Start Method
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		myP = new MyProtocol();
		Bundle bundle = getIntent().getExtras();
		gameId = bundle.getString("gameId");
		myDb = new GameDB(this);
		myDb.open();
		me = myDb.getMyData();
		myDb.close();
		myHandler = new Handler();
		new ServerSynch().execute(gameId);

	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopRepeatingUpdate();
	}

	private void makemusik() {
		mp2 = MediaPlayer.create(getApplicationContext(), R.raw.prosit);
	}

	// Creates a Menu
	public boolean onCreateOptionsMenu(Menu newMenu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.gameoffline, newMenu);
		return true;
	}

	// Creates a info button listener
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.music) {
				makemusik();
				muted = false;
		} else {
		}
		return true;
	}

	/*
	 * Update Methode
	 */
	private void updateField() {
		TableRow playground = (TableRow) findViewById(R.id.playground);
		LinearLayout row;
		TextView stone;

		for (int i = 0; i < 7; i++) {
			row = (LinearLayout) playground.getChildAt(i);
			for (int j = 0; j < 6; j++) {
				stone = (TextView) row.getChildAt(j);
				if (Field.getField(j, i) == 0) {
					stone.setBackgroundResource(R.drawable.weiss);
				}
				if (Field.getField(j, i) == 1) {
					stone.setBackgroundResource(R.drawable.breze);
				}
				if (Field.getField(j, i) == 2) {
					stone.setBackgroundResource(R.drawable.bier);
				}
				if (Field.getField(j, i) == 3) {
					stone.setBackgroundResource(R.drawable.extra);
				}
				if (Field.getField(j, i) < 0) {
					stone.setBackgroundResource(R.drawable.euro);
				}
			}
		}
	}

	/*
	 * Erstellt fuer jede Reihe und Button einen Listener
	 */
	private void listenerCreate() {
		TableRow playground = (TableRow) findViewById(R.id.playground);
		for (int i = 0; i < playground.getChildCount(); i++) {
			final LinearLayout row = (LinearLayout) playground.getChildAt(i);
			final int rownumber = i;
			for (int j = 0; j < row.getChildCount(); j++) {
				row.getChildAt(j).setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						try {
							String currentPlayer = thisGame.getLastPlayer();
							if (currentPlayer.equals(me.getUsername()) && !gameOver) {
								decisionMaker(row, rownumber);
								//new sendTurn().execute();
							} else {
								Toast.makeText(GameActivity.this,
										"Du bist ned dro!", Toast.LENGTH_LONG)
										.show();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		}
		
		((Button) findViewById(R.id.Button))
		.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					setContentView(R.layout.game);
					try {
						startActivity(new Intent(GameActivity.this,
								OverviewActivity.class));
						mp2.stop();

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
										OverviewActivity.class));
								mp2.stop();

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
								ExtraCanBeSet = true;
								Toast.makeText(GameActivity.this,
										getText(R.string.setblock),
										Toast.LENGTH_LONG).show();
							}
						} catch (Exception e) {
						}
					}
				});
	}
	
	private void changeCurrentPlayerInstant() {
		if(thisGame.getLastPlayer().equals(thisGame.getP1())) {
			thisGame.setcurrentPlayer(thisGame.getP2());
		} else if (thisGame.getLastPlayer().equals(thisGame.getP2())) {
			thisGame.setcurrentPlayer(thisGame.getP1());
		}
	}

	/*
	 * Ab hier Logik
	 */
	private boolean isBlocked(int rownumber) {
		if (Field.getField(0, rownumber) < 0) {
			return true;
		}
		return false;
	}

	/*
	 * Checker for next free Field in row
	 */
	private int nextfree(int rownumber) {
		for (int i = 5; i >= 0; i--) {
			int checknum = Field.getField(i, rownumber);
			if (checknum < 0)
				return 10;
			if (checknum == 0 || checknum == 3)
				return i;
		}
		return -1;
	}

	protected void decisionMaker(LinearLayout row, int rownumber) {
		int bottom = nextfree(rownumber);

		// extra setzen
		if (!isBlocked(rownumber) && ExtraCanBeSet
				&& Field.getExtrasOfPlayer(playernumber)) {
			TextView stone = (TextView) row.getChildAt(0);
			stone.setBackgroundResource(R.drawable.euro);
			Field.setField(0, rownumber, -2);// 2 wegen verzoegerung
			Field.setExtrasOfPlayer(playernumber, false);
			ExtraCanBeSet = false;
			// stein setzen
		} else if (!isBlocked(rownumber)) {
			setstones(bottom, rownumber, row);
			new sendTurn().execute();
			changeCurrentPlayerInstant();
		} else {

			// wenn in blockierte Reihe gesetzt werden will
			Toast.makeText(GameActivity.this, getText(R.string.blockedrow),
					Toast.LENGTH_LONG).show();
		}

	}

	/*
	 * Main Method - Setting of all fields
	 */
	private void setstones(int bottom, int rownumber, LinearLayout row) {
		// lass zuerst evtl Blocker verschwinden
		hideblocker(row);

		// wenn auf dem Feld ein ? bekommt Player ein Extra
		if (Field.getField(bottom, rownumber) == 3)
			Field.setExtrasOfPlayer(playernumber, true);

		// Eigentliches setzen
		actualTurn(bottom, rownumber, row);

		// setze jeden Blocker +1
		for (int i = 0; i < 7; i++) {
			if (nextfree(i) < 0) {
				Field.setField(nextfree(i), i, nextfree(i) + 1);
			}
		}

		// Setze die Symbole ob Extra oder nicht
		setPlayerExtras();

		// Update komplettes Spielfeld
		updateField();
	}

	private void hideblocker(LinearLayout row) {
		for (int i = 0; i < 7; i++) {
			if (isBlocked(i)) {
				int var = Field.getField(0, i) + 1;
				Field.setField(0, i, var);
				if (Field.getField(0, i) == 0) {
					TextView stone = (TextView) row.getChildAt(0);
					stone.setBackgroundResource(R.drawable.weiss);
				}

			}
		}
	}

	private void setPlayerExtras() {
		if (Field.getExtrasOfPlayer(playernumber)) {
			TextView currentitem = (TextView) findViewById(R.id.currentitem);
			currentitem.setBackgroundResource(R.drawable.euro);
		} else {
			TextView currentitem = (TextView) findViewById(R.id.currentitem);
			currentitem.setBackgroundResource(R.drawable.keinextra);
		}
	}

	/*
	 * Eigentliches Setzen
	 */
	private void actualTurn(int bottom, int rownumber, LinearLayout row) {
		TextView player = ((TextView) findViewById(R.id.iscurrentlyplaying));
		TextView bottomstone = (TextView) row.getChildAt(bottom);
		TextView playericon = ((TextView) findViewById(R.id.currentPlayerIcon));
		if (thisGame.getLastPlayer().equals(thisGame.getP1())) {
			bottomstone.setBackgroundResource(R.drawable.breze);
			playericon.setBackgroundResource(R.drawable.bier);
			player.setText(thisGame.getP2());
			playernumber = 2;
			Field.setField(bottom, rownumber, 1);
			Field.setTurns(Field.getTurns() + 1);
			playchecks(bottom, rownumber, row);

		} else if (thisGame.getLastPlayer().equals(thisGame.getP2())) {
			bottomstone.setBackgroundResource(R.drawable.bier);
			playericon.setBackgroundResource(R.drawable.breze);
			player.setText(thisGame.getP1());
			playernumber = 1;
			Field.setField(bottom, rownumber, 2);
			Field.setTurns(Field.getTurns() + 1);
			playchecks(bottom, rownumber, row);

		}
		updateField();
		if (playernumber == 0) {
			gameOver = true;
			Toast.makeText(GameActivity.this,
					"Gwunna! Ju'che!",
					Toast.LENGTH_LONG).show();
		}
		drawcheck();
	}

	/*
	 * Default checks
	 */
	private void playchecks(int bottom, int rownumber, LinearLayout row) {
		if (win.wincheck()) {
			playernumber = 0;
			if (!muted)
				mp2.start();
			Button button = (Button) findViewById(R.id.Button);
			button.setVisibility(View.VISIBLE);
			button.setBackgroundColor(getResources().getColor(R.color.green));
		}
		extras(bottom, rownumber, row);
	}

	/*
	 * Extraset
	 */
	private void extras(int bottom, int rownumber, LinearLayout row) {
		// jedes 5. mal wenn spiel begonnen wenn max vorletzter stein gesetzt
		int counter = Field.getTurns();
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
		if (Field.getTurns() == 42) {
			if (!muted)
				mp2.start();
			playernumber = 0;
			/*Button button = (Button) findViewById(R.id.Button);
			button.setText(R.string.drawstring);
			button.setBackgroundColor(getResources().getColor(R.color.green));*/
		}
	}

	/*
	 * Serveranbindung
	 */


	Runnable myUpdater = new Runnable() {

		@Override
		public void run() {
			try {
				
				new RepeatedUpdate().execute(String.valueOf(thisGame.getGameId()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			myHandler.postDelayed(myUpdater, 3000);

		}
	};
	
	private void startRepeatingUpdate() {
		myUpdater.run();
	}
	
	private void stopRepeatingUpdate() {
		myHandler.removeCallbacks(myUpdater);
	}

	class RepeatedUpdate extends AsyncTask<String, Void, String> {
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
				thisGame = processGameJsonArray(gamesList);
				setField(thisGame);
				updateField();
				if(me.getUsername().equals(thisGame.getLastPlayer())) {
					stopRepeatingUpdate();
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
			if(win.wincheck()) {
				gameOver = true;
				Toast.makeText(GameActivity.this,
						"Verlorn! Noob!",
						Toast.LENGTH_LONG).show();
			}
		}

		private void setField(Game thisGame) {
			String field = thisGame.getField();
			try {
				Field currentField = (Field) Serializer.deserialize(field);
				Field = currentField;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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

		private Game processGameJsonArray(JSONArray gamesData)
				throws JSONException {
			Game game;

			String id = gamesData.getString(0);
			String field = gamesData.getString(1);
			String user1 = gamesData.getString(2);
			String user2 = gamesData.getString(3);
			String currentUser = gamesData.getString(4);

			game = new Game(Integer.parseInt(id), field, user1, user2,
					currentUser);
			return game;
		}

	}

	class sendTurn extends AsyncTask<Void, Void, String> {
		Socket socket = null;

		@Override
		protected String doInBackground(Void... params) {
			try {
				socket = new Socket(SERVER_IP, SERVERPORT);
				System.out.println("gr8 success very nice");
				sendRequest();
				return null;

			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			startRepeatingUpdate();
		}

		private void sendRequest() {
			try {
				String id = String.valueOf(thisGame.getGameId());
				String field = Serializer.serialize(Field);
				String output = myP.makeTurn(id, field);
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
				thisGame = processGameJsonArray(gamesList);
				setField(thisGame);
				createUI();
				updateField();
				TextView player = ((TextView) findViewById(R.id.iscurrentlyplaying));
				player.setText(thisGame.getLastPlayer());

			} catch (JSONException e) {
				e.printStackTrace();
			}
			if(win.wincheck()) {
				gameOver = true;
				Toast.makeText(GameActivity.this,
						"Verlorn! Noob!",
						Toast.LENGTH_LONG).show();
			}
		}

		private void createUI() {
			setContentView(R.layout.game);
			win = new GameWinCheck(Field);
			try {
				makemusik();
				listenerCreate();
			} catch (Exception e) {
			}
			Toast.makeText(GameActivity.this, getText(R.string.gamestart),
					Toast.LENGTH_LONG).show();
		}

		private void setField(Game thisGame) {
			String field = thisGame.getField();
			try {
				Field currentField = (Field) Serializer.deserialize(field);
				Field = currentField;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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

		private Game processGameJsonArray(JSONArray gamesData)
				throws JSONException {
			Game game;

			String id = gamesData.getString(0);
			String field = gamesData.getString(1);
			String user1 = gamesData.getString(2);
			String user2 = gamesData.getString(3);
			String currentUser = gamesData.getString(4);

			game = new Game(Integer.parseInt(id), field, user1, user2,
					currentUser);
			return game;
		}
	}
}
