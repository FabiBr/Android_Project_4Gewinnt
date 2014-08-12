package de.ur.mi.android.excercises.starter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.TableLayout;

public class GameController {
	private boolean[][] fields;
	private Color currentPlayerColor;

	public GameController(TableLayout layout) {
	}

	public void startGame() {
		fields = new boolean[6][7];
	}

	public void setStone(LinearLayout row) {
		if (!topStoneAlreadySet()) {
			setStoneOnTopPosition(row);
			if(winCheck()){
				//TODO: Meldung dass das spiel vorbei ist
			}
			else{
				//TODO: aktuelle Spielerfarbe wechseln
				//TODO: später: vom "spielmodus" auf "wartemodus"
			}
		} else {
			// TODO: eventuell kleine Meldung irgendwo anzeigen mit "diese
			// Spalte ist bereits voll
		}
	}


	public boolean topStoneAlreadySet() {
		// TODO prüfen ob oberster Stein von row bereits gesetzt
		// if(row.childAt(0).color != "red") -> return false
		// ansonsten ist er schon gesetzt und return true
		return true;
	}
	
	private void setStoneOnTopPosition(LinearLayout row) {
		// TODO farbe des obersten steins in die jeweilige spielerfarbe wechseln
		row.getChildAt(5).setBackgroundColor(Color.RED);
		//TODO: enthardcoden sh main
	}
	
	
	private boolean winCheck() {
		// TODO wenn vier steine horizontal / diagonal / vertikal in einer Reihe sind
		if(hCheck() || dCheck() || vCheck()){
			return true;
		}
		return false;
	}
	
	private boolean hCheck() {
		return false;
	}
	private boolean dCheck() {
		return false;
	}
	private boolean vCheck() {
		return false;
	}
	

}
