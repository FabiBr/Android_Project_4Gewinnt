package de.ur.mi.android.excercises.starter;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Gameoffline extends Activity {
	// Main Game
	private Field Field;
	private int playernumber = 1;
	private GameWinCheck win;
	private boolean ExtraCanBeSet = false;
	private MediaPlayer mp;
	private MediaPlayer mp2;
	private boolean muted = false;

	/*
	 * Start Method
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gameoffline);
		Field = new Field();
		win = new GameWinCheck(Field);
		try {
			makemusik();
			listenerCreate();
		} catch (Exception e) {
		}
		Toast.makeText(Gameoffline.this, getText(R.string.gamestart),
				Toast.LENGTH_LONG).show();

	}

	//makes music
	private void makemusik() {
		mp = MediaPlayer.create(getApplicationContext(), R.raw.baydef);
		mp2 = MediaPlayer.create(getApplicationContext(), R.raw.prosit);
		if(!muted)mp.start();
	}

	// Creates a Menu
	public boolean onCreateOptionsMenu(Menu newMenu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.gameoffline, newMenu);
		return true;
	}

	// Creates a info button listener
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==R.id.music){
			if(mp.isPlaying()){mp.stop();
			muted = true;}
			else {makemusik();;muted = false;}
		}
		else{}
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
							decisionMaker(row, rownumber);
						} catch (Exception e) {
						}
					}
				});
			}
		}
		((Button) findViewById(R.id.Button))
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						try {
							setContentView(R.layout.activity_gameoffline);
							mp2.stop();
							mp.stop();
							if(!muted)makemusik();
							listenerCreate();
							Field = new Field();
							win = new GameWinCheck(Field);
							playernumber = 1;
							Field.setTurns(0);
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
								mp.stop();
								mp2.stop();
								startActivity(new Intent(Gameoffline.this,
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
								ExtraCanBeSet = true;
								Toast.makeText(Gameoffline.this,
										getText(R.string.setblock),
										Toast.LENGTH_LONG).show();
							}
						} catch (Exception e) {
						}
					}
				});
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
		} else {

			// wenn in blockierte Reihe gesetzt werden will
			Toast.makeText(Gameoffline.this, getText(R.string.blockedrow),
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

	// lasst blocker verschwinden
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

	// set player extra attitude
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
		if (playernumber == 1) {
			bottomstone.setBackgroundResource(R.drawable.breze);
			playericon.setBackgroundResource(R.drawable.bier);
			player.setText(R.string.hansl2);
			playernumber = 2;
			Field.setField(bottom, rownumber, 1);
			Field.setTurns(Field.getTurns() + 1);
			playchecks(bottom, rownumber, row);

		} else if (playernumber == 2) {
			bottomstone.setBackgroundResource(R.drawable.bier);
			playericon.setBackgroundResource(R.drawable.breze);
			player.setText(R.string.hansl1);
			playernumber = 1;
			Field.setField(bottom, rownumber, 2);
			Field.setTurns(Field.getTurns() + 1);
			playchecks(bottom, rownumber, row);

		}
		updateField();
		if (playernumber == 0) {
			Button button = (Button) findViewById(R.id.Button);
			button.setText(R.string.winstring);
			button.setBackgroundColor(getResources().getColor(R.color.green));
		}
		drawcheck();
	}

	/*
	 * Default checks
	 */
	private void playchecks(int bottom, int rownumber, LinearLayout row) {
		if (win.wincheck()) {
			playernumber = 0;
			mp.stop();
			if(!muted)mp2.start();
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
			mp.stop();
			if(!muted)mp2.start();
			playernumber = 0;
			Button button = (Button) findViewById(R.id.Button);
			button.setText(R.string.drawstring);
			button.setBackgroundColor(getResources().getColor(R.color.green));
		}
	}
}
