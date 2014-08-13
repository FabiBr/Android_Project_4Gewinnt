package de.ur.mi.android.excercises.starter;

import java.io.Serializable;

public class Field implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int[][] field;
	private int turns;
	
	public Field(){
		field = new int[6][7];
		turns = 0;
	}
	
	public void setField(int x, int y, int p){
		if(field[x][y] == 0){
			field[x][y] = p;
			turns++;
		}
	}
	
	public int getField(int x, int y){
		return field[x][y];
	}
	
	public int[][] getField(){
		return field;
	}
	
	public int getTurns(){
		return turns;
	}

}
