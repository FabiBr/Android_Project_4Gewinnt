package de.ur.mi.android.excercises.starter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends Activity {
	TableLayout myLayout;
	SparseIntArray rowsIDs = new SparseIntArray();
	int[][] playfield;
	Field Field = new Field();
	int playernumber = 1;
	Field field;

	private GameDB db;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		field = new Field();
		try {
			textviewrun();
		} catch (Exception e) {
		}
		Toast.makeText(GameActivity.this, "Grabsch moi afs spuifoid",
				Toast.LENGTH_LONG).show();

	}

	/*
	 * Erstellt f�r jede Reihe einen Listener
	 */
	private void textviewrun() {
		System.out.println("Listeners setzen");
		myLayout = (TableLayout) findViewById(R.id.layout);
		// playground = spielfeld
		TableRow playground = (TableRow) findViewById(R.id.playground); // TODO:
																	// enthardcoden
		// TODO: Layout playground als eigenes Layout abspeichern und hier
		// direkt aufrufen
		for (int i = 0; i < playground.getChildCount(); i++) {
			// row = jede Reihe im Spielfeld ( 7 St�ck )
			final LinearLayout row = (LinearLayout) playground.getChildAt(i);
			rowsIDs.put(i, row.getId());
			final int rownumber = i;
			for (int j = 0; j < row.getChildCount(); j++) {
				row.getChildAt(j).setOnClickListener(new OnClickListener() {
					@Override
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
								playfield = Field.getField();
								playernumber = 1;

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
	}

	protected void clicklistener(LinearLayout row, int rownumber) {
		// alle Daten von gamecontroller abrufen -> hier ausf�hren
		int bottom = nextfree(rownumber);
		if (bottom < 6) {
			setstones(bottom, rownumber, row);
			if (playernumber == 0) {
				Button button = (Button) findViewById(R.id.Button);
				button.setText("Gwunna! Nummol?");
				button.setBackgroundColor(getResources()
						.getColor(R.color.green));
			}
		}
	}

	private void setstones(int bottom, int rownumber, LinearLayout row) {
		TextView player = ((TextView) findViewById(R.id.iscurrentlyplaying));
		TextView bottomstone = (TextView) row.getChildAt(bottom);
		TextView playericon = ((TextView) findViewById(R.id.currentPlayerIcon));
		if (playernumber == 1) {
		
			bottomstone.setBackgroundResource(R.drawable.rot);
			playericon.setBackgroundResource(R.drawable.blau);
			playernumber = 2;
			player.setText(R.string.hansl2);
			Field.setField(bottom, rownumber, 1);
			if (wincheck(bottom, rownumber)) {
				playernumber = 0;
			}
			if (drawcheck()) {
				playernumber = 0;
				Toast.makeText(GameActivity.this, "Unentschieden !!!!!",
						Toast.LENGTH_LONG).show();
			}

		} else if (playernumber == 2) {
			bottomstone.setBackgroundResource(R.drawable.blau);
			playericon.setBackgroundResource(R.drawable.rot);
			playernumber = 1;
			player.setText(R.string.hansl1);
			Field.setField(bottom, rownumber, 2);
			if (wincheck(bottom, rownumber)) {
				playernumber = 0;
			}
			if (drawcheck()) {
				playernumber = 0;
				Toast.makeText(GameActivity.this, "Unentschieden !!!!!",
						Toast.LENGTH_LONG).show();
			}

		} else {
		}
	}

	private boolean drawcheck() {
		for (int i = 0; i < 7; i++) {
				if (nextfree(i) != -1)
					return false;

			
		}
		return true;
	}

	private boolean wincheck(int bottom, int rownumber) {
		if (vcheck(bottom, rownumber) || hcheck() || dcheck())
			return true;
		return false;

	}

	private boolean dcheck() {
		// diagonal check
		// down up
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 6; j++) {
				if (Field.getField(j, i) != 0
						&& Field.getField(j, i) == Field.getField(j - 1, i + 1)
						&& Field.getField(j, i) == Field.getField(j - 2, i + 2)
						&& Field.getField(j, i) == Field.getField(j - 3, i + 3))
					return true;

			}
		}
		// up down
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 6; j++) {
				if (Field.getField(j, i) != 0
						&& Field.getField(j, i) == Field.getField(j + 1, i + 1)
						&& Field.getField(j, i) == Field.getField(j + 2, i + 2)
						&& Field.getField(j, i) == Field.getField(j + 3, i + 3))
					return true;

			}
		}
		return false;
	}

	private boolean hcheck() {
		// horizontal check
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 6; j++) {
				if (Field.getField(j, i) != 0
						&& Field.getField(j, i) == Field.getField(j, i + 1)
						&& Field.getField(j, i) == Field.getField(j, i + 2)
						&& Field.getField(j, i) == Field.getField(j, i + 3))
					return true;

			}
		}
		return false;
	}

	private boolean vcheck(int bottom, int rownumber) {
		// vertical check
		if (bottom < 3
				&& Field.getField(bottom, rownumber) == Field.getField(
						bottom + 1, rownumber)
				&& Field.getField(bottom, rownumber) == Field.getField(
						bottom + 2, rownumber)
				&& Field.getField(bottom, rownumber) == Field.getField(
						bottom + 3, rownumber))
			return true;
		return false;
	}

	private int nextfree(int rownumber) {
		for (int i = 5; i >= 0; i--) {
			int checknum = Field.getField(i, rownumber);
			if (checknum != 1 && checknum != 2) {
				return i;
			}
		}
		return -1;
	}

}
