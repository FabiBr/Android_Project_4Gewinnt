package de.ur.mi.android.excercises.starter;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;

import android.util.SparseIntArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

public class GameActivity extends Activity {
	TableLayout myLayout;
	SparseIntArray rowsIDs = new SparseIntArray();
	int[][] playfield = new int[6][7];
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

	}

	/*
	 * Erstellt f�r jede Reihe einen Listener
	 */
	private void textviewrun() {
		System.out.println("Listeners setzen");
		myLayout = (TableLayout) findViewById(R.id.layout);
		// playground = spielfeld
		TableRow playground = (TableRow) myLayout.getChildAt(1); // TODO:
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
							System.out.println("rownumber " + rownumber
									+ " clicked");
							clicklistener(row, rownumber);
						} catch (Exception e) {
						}
					}
				});
			}
		}

		// Button Click Listener
		final RelativeLayout buttonlay = (RelativeLayout) myLayout
				.getChildAt(2);
		buttonlay.getChildAt(0).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					setContentView(R.layout.game);
					try {
						textviewrun();
						playfield = new int[6][7];
						playernumber = 1;

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
				((ViewGroup) myLayout.getChildAt(2)).getChildAt(0)
						.setBackgroundColor(Color.GREEN);
			}
		}
	}

	private void setstones(int bottom, int rownumber, LinearLayout row) {
		if (playernumber == 1) {
			row.getChildAt(bottom).setBackgroundColor(Color.RED);
			playernumber = 2;
			playfield[bottom][rownumber] = 1;
			if (wincheck(bottom, rownumber)) {
				playernumber = 0;
			}
		} else if (playernumber == 2) {
			row.getChildAt(bottom).setBackgroundColor(Color.BLUE);
			playernumber = 1;
			playfield[bottom][rownumber] = 2;
			if (wincheck(bottom, rownumber)) {
				playernumber = 0;
			}
		} else {
		}
	}

	private boolean wincheck(int bottom, int rownumber) {
		if (vcheck(bottom, rownumber) || hcheck(bottom, rownumber)
				|| dcheck(bottom, rownumber))
			return true;
		return false;

	}

	private boolean dcheck(int bottom, int rownumber) {
		// diagonal check
		// for first and last stone
		if (rownumber <= 3 && bottom > 2) {
			// first quater
			if (playfield[bottom][rownumber] == playfield[bottom - 1][rownumber + 1]
					&& playfield[bottom][rownumber] == playfield[bottom - 2][rownumber + 2]
					&& playfield[bottom][rownumber] == playfield[bottom - 3][rownumber + 3]) {
				return true;
			}
		}
		if (rownumber <= 3 && bottom <= 2) {
			// second quater
			if (playfield[bottom][rownumber] == playfield[bottom + 1][rownumber + 1]
					&& playfield[bottom][rownumber] == playfield[bottom + 2][rownumber + 2]
					&& playfield[bottom][rownumber] == playfield[bottom + 3][rownumber + 3]) {
				return true;
			}
		}
		if (rownumber >= 3 && bottom > 2) {
			// third quater
			if (playfield[bottom][rownumber] == playfield[bottom - 1][rownumber - 1]
					&& playfield[bottom][rownumber] == playfield[bottom - 2][rownumber - 2]
					&& playfield[bottom][rownumber] == playfield[bottom - 3][rownumber - 3]) {
				return true;
			}
		}
		if (rownumber >= 3 && bottom <= 2) {
			// forth quater
			if (playfield[bottom][rownumber] == playfield[bottom + 1][rownumber - 1]
					&& playfield[bottom][rownumber] == playfield[bottom + 2][rownumber - 2]
					&& playfield[bottom][rownumber] == playfield[bottom + 3][rownumber - 3]) {
				return true;
			}
		}
		// for second stone
		if (rownumber <= 4 && bottom >= 2) {
			if (playfield[bottom][rownumber] == playfield[bottom + 1][rownumber - 1]
					&& playfield[bottom][rownumber] == playfield[bottom - 1][rownumber + 1]
					&& playfield[bottom][rownumber] == playfield[bottom - 2][rownumber + 2]) {
				return true;
			}
		}
		// for third stone
		if (rownumber >= 2 && bottom <= 3) {
			if (playfield[bottom][rownumber] == playfield[bottom - 2][rownumber - 2]
					&& playfield[bottom][rownumber] == playfield[bottom - 1][rownumber - 1]
					&& playfield[bottom][rownumber] == playfield[bottom + 1][rownumber + 1]) {
				return true;
			}
		}
		return false;
	}

	private boolean hcheck(int bottom, int rownumber) {
		// horizontal check
		if (rownumber < 4) {
			// first stone
			if (playfield[bottom][rownumber] == playfield[bottom][rownumber + 1]
					&& playfield[bottom][rownumber] == playfield[bottom][rownumber + 2]
					&& playfield[bottom][rownumber] == playfield[bottom][rownumber + 3]) {
				return true;
			}
		}
		if (rownumber >= 4) {
			// last stone
			if (playfield[bottom][rownumber] == playfield[bottom][rownumber - 1]
					&& playfield[bottom][rownumber] == playfield[bottom][rownumber - 2]
					&& playfield[bottom][rownumber] == playfield[bottom][rownumber - 3]) {
				return true;
			}
		}
		if (rownumber <= 5) {
			// third stone
			if (playfield[bottom][rownumber] == playfield[bottom][rownumber - 2]
					&& playfield[bottom][rownumber] == playfield[bottom][rownumber - 1]
					&& playfield[bottom][rownumber] == playfield[bottom][rownumber + 1]) {
				return true;
			}
		}
		if (rownumber != 0) {
			// second stone
			if (playfield[bottom][rownumber] == playfield[bottom][rownumber - 1]
					&& playfield[bottom][rownumber] == playfield[bottom][rownumber + 1]
					&& playfield[bottom][rownumber] == playfield[bottom][rownumber + 2]) {
				return true;
			}
		}
		if (playfield[bottom][1] == playfield[bottom][0]
				&& playfield[bottom][1] == playfield[bottom][2]
				&& playfield[bottom][1] == playfield[bottom][3]) {
			return true;
		}

		return false;
	}

	private boolean vcheck(int bottom, int rownumber) {
		// vertical check
		if (bottom < 3
				&& playfield[bottom][rownumber] == playfield[bottom + 1][rownumber]
				&& playfield[bottom + 1][rownumber] == playfield[bottom + 2][rownumber]
				&& playfield[bottom + 2][rownumber] == playfield[bottom + 3][rownumber])
			return true;
		return false;
	}

	private int nextfree(int rownumber) {
		for (int i = 5; i >= 0; i--) {
			int checknum = playfield[i][rownumber];
			if (checknum != 1 && checknum != 2) {
				return i;
			}
		}
		return -1;
	}

}
