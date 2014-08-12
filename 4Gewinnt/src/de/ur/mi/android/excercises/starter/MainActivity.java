package de.ur.mi.android.excercises.starter;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

/*
 --------------------------------------------------------------------------------------------------------------------------------------------------
 1. Start: Anmelden oder Registrieren   
 1.1 Anmelden: Username und PW an Server 
 wenn korrekt -> Menü
 wenn falsch -> nochmal | registrieren
 1.2 Registrieren: E-Mail und Password(2x) und Benutzername -> Server, dann -> Menü
 2. Menü: Spieler suchen mit Benutzername in Serverdatenbank -> Anfrage
 3. Nach Bestätigung auf in Server gespeicherte Anfrage -> Spielbeginn
 4. Spieler 1 "Spielmodus" - Spieler 2 "Wartemodus"
 Anmerkungen:
 - komplettes Spielfeld muss auf Server zwischengespeichert werden ( int[6][7] mit zahlen von 0-2 ) nach Ende jedes Zuges
 - komplettes Spielfeld muss am Anfang jedes Zuges abgerufen werden
 5. Spielverlauf bis Sieg.
 --------------------------------------------------------------------------------------------------------------------------------------------------
 */
/*
 -------------------------------------------------------------------------------------------------------------------------------------------------- 
 Themen:
 - Git dreck geht nicht !
 - ich würde Logik machen - zuerst für auf ein Gerät spielbar -> bereits angefangen
 - einer muss Menüsteuerung machen
 - einer Serveranfragen und Antworten regeln
 - Die engine wird nicht benötigt - was sagt ihr
 --------------------------------------------------------------------------------------------------------------------------------------------------
 */

public class MainActivity extends Activity {
	GameController controller;
	TableLayout myLayout;
	SparseIntArray rowsIDs = new SparseIntArray();
	int[][] playfield = new int[6][7];

	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("App start");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try {
			textviewrun();
			 controller = new GameController(myLayout); 
			 controller.startGame();
			
		} catch (Exception e) {
		}

	}

	/*
	 * Erstellt für jede Reihe einen Listener
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
			// row = jede Reihe im Spielfeld ( 7 Stück )
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
							clicklistener(row , rownumber);
						} catch (Exception e) {
						}
					}
				});
			}
		}
		
		//Button Click Listener
		final RelativeLayout buttonlay = (RelativeLayout) myLayout.getChildAt(2);
		buttonlay.getChildAt(0).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				try {
					System.out.println("button clicked");
					//buttonlay.getChildAt(0).setBackgroundColor(Color.RED);
					setContentView(R.layout.activity_main);
					try {
						textviewrun();
						 controller = new GameController(myLayout); 
						 controller.startGame();
						
					} catch (Exception e) {
					}
				} catch (Exception e) {
				}
			}
		});
	}

	protected void clicklistener(LinearLayout row, int rownumber) {
		//alle Daten von gamecontroller abrufen -> hier ausführen
		int bottom = 5;
		row.getChildAt(bottom).setBackgroundColor(Color.RED);
		playfield[nextfree()][rownumber] = 2;
	}

	private int nextfree() {
		return 0;
	}
}
