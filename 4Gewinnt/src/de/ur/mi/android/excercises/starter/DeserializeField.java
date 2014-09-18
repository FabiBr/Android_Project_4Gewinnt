package de.ur.mi.android.excercises.starter;

import java.io.*;

public class DeserializeField {
	
	public static void main(String[] args){

		
		Field f = null;
		
		try
	    {
	       FileInputStream fileIn = new FileInputStream("/tmp/field.ser");
	       ObjectInputStream in = new ObjectInputStream(fileIn);
	       f = (Field) in.readObject();
	       in.close();
	       fileIn.close();
	    }catch(IOException i)
	    {
	       i.printStackTrace();
	       return;
	    }catch(ClassNotFoundException c)
	    {
	       System.out.println("Field class not found");
	       c.printStackTrace();
	       return;
	    }
		
		System.out.println("Field 0,0 is: " + f.getField(0,0));
		System.out.println("Deserialization successful");
		
	}
	
}
