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

	public Field() {
		field = new int[6][7];
		turns = 0;
	}

	public void setField(int x, int y, int p) {
		if (field[x][y] == 0) {
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
