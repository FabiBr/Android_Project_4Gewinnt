package de.ur.mi.android.excercises.starter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends Activity {
	//Main Game
	TableLayout myLayout;
	Field Field;
	int playernumber = 1;
	int counter = 0;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		Field = new Field();
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
	}

	/*
	 * Checker for next free Field in row
	 */
	private int nextfree(int rownumber) {
		for (int i = 5; i >= 0; i--) {
			int checknum = Field.getField(i, rownumber);
			if (checknum ==0)return i;
		}
		return -1;
	}
	
	protected void clicklistener(LinearLayout row, int rownumber) {
		// alle Daten von gamecontroller abrufen -> hier ausfuehren
		int bottom = nextfree(rownumber);
		if (bottom < 6) {
			setstones(bottom, rownumber, row);
		}
	}


	
	/*
	 * Main Method - Setting of all fields
	 */
	private void setstones(int bottom, int rownumber, LinearLayout row) {
		TextView player = ((TextView) findViewById(R.id.iscurrentlyplaying));
		TextView bottomstone = (TextView) row.getChildAt(bottom);
		TextView playericon = ((TextView) findViewById(R.id.currentPlayerIcon));
		/*
		//wenn an der gesetzten stelle 3 gesetzt ist
		if (field.getField(bottom, rownumber) == 3) {
			TextView currentitem = (TextView) findViewById(R.id.currentitem);
			currentitem.setBackgroundResource(R.drawable.keinextra);}
*/
		if (playernumber == 1) {
			bottomstone.setBackgroundResource(R.drawable.breze);
			playericon.setBackgroundResource(R.drawable.bier);
			playernumber = 2;
			player.setText(R.string.hansl2);
			Field.setField(bottom, rownumber, 1);
			playchecks(bottom, rownumber, row);



		} else if (playernumber == 2) {
			bottomstone.setBackgroundResource(R.drawable.bier);
			playericon.setBackgroundResource(R.drawable.breze);
			playernumber = 1;
			player.setText(R.string.hansl1);
			Field.setField(bottom, rownumber, 2);
			playchecks(bottom, rownumber, row);


		} 
		if(playernumber ==0){
				Button button = (Button) findViewById(R.id.Button);
				button.setText("Gwunna! Nummol?");
				button.setBackgroundColor(getResources()
						.getColor(R.color.green));
		}
	}

	private void playchecks(int bottom, int rownumber, LinearLayout row) {
		if (wincheck()) playernumber = 0;
		counter++;
		drawcheck();
		//extras(bottom, rownumber, row);
	}

	private void extras(int bottom, int rownumber, LinearLayout row) {
		//jedes 4. mal			wenn spiel begonnen		wenn max vorletzter stein gesetzt
		if (counter % 4 == 0 && counter > 0 && bottom > 0) {
			TextView bottomstone = (TextView) row.getChildAt(bottom-1);
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

	/*
	 * Checker for Winning
	 */
	private boolean wincheck() {
		if (vcheck() || hcheck() || dcheck())
			return true;
		return false;

	}

	private boolean dcheck() {
		// diagonal check
		// down up
		for (int i = 0; i <= 3; i++) {
			for (int j = 5; j >= 3; j--) {
				if (Field.getField(j, i) != 0
						&& Field.getField(j, i) == Field.getField(j - 1, i + 1)
						&& Field.getField(j, i) == Field.getField(j - 2, i + 2)
						&& Field.getField(j, i) == Field.getField(j - 3, i + 3))
					return true;

			}
		}
		// up down
		for (int i = 0; i <= 3; i++) {
			for (int j = 0; j <= 2; j++) {
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
		for (int i = 0; i <= 3; i++) {
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

	private boolean vcheck() {
		//vertical check
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 4; j++) {
				if (Field.getField(j, i) != 0
						&& Field.getField(j, i) == Field.getField(j+1, i)
						&& Field.getField(j, i) == Field.getField(j+2, i)
						&& Field.getField(j, i) == Field.getField(j+3, i))
					return true;

			}
		}
		return false;
	}
}
