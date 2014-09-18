package de.ur.mi.android.excercises.starter;

import java.io.*;

public class SerializeField {

	public static void main(String[] args){

	
		Field f = new Field();
		f.setField(0, 0, 1);
		
	    try
	    {
	       FileOutputStream fileOut =
	       new FileOutputStream("/tmp/field.ser");
	       ObjectOutputStream out = new ObjectOutputStream(fileOut);
	       out.writeObject(f);
	       out.close();
	       fileOut.close();
	       System.out.printf("Serialized data is saved in /tmp/field.ser");
	    }catch(IOException i)
	    {
	        i.printStackTrace();
	    }
		
	}
	
}
