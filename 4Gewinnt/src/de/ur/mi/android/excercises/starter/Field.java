package de.ur.mi.android.excercises.starter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

import android.util.Base64;

public class Field implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int[][] field;
	private int turns;
	private boolean extrasplayer1 = false;
	private boolean extrasplayer2 = false;

	public Field() {
		field = new int[6][7];
		turns = 0;
	}

	public boolean getExtrasOfPlayer(int playernumber){
		if(playernumber == 1){
			return extrasplayer1;
		}
		else{
			return extrasplayer2;
		}
	}
	
	public void setExtrasOfPlayer(int playernumber, boolean value){
		if(playernumber == 1){
			extrasplayer1 = value;
		}
		else{
			extrasplayer2 = value;
		}
	}
	
	public void setField(int x, int y, int p) {
		if (field[x][y] != 1 && field [x][y] != 2) {
			field[x][y] = p;
			turns++;
		}
	}

	public int getField(int x, int y) {
		return field[x][y];
	}

	public int[][] getField() {
		return field;
	}

	public int getTurns() {
		return turns;
	}

	public String getSerialisedFieldString() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(field);
		oos.close();
		return new String(Base64.encodeToString(baos.toByteArray(), 0));
	}

}
