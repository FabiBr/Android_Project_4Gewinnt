package de.ur.mi.android.excercises.starter;

import java.io.*;
import javax.xml.bind.*;

public class Serializer
{
	public static String serialize(Object o) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(o);
		oos.flush();
		String s = baos.toString();
		
		s = DatatypeConverter.printBase64Binary(baos.toByteArray());
		return s;
	}

	public static Object deserialize(String s) throws IOException, ClassNotFoundException
	{
		byte[] b = s.getBytes();

		b = DatatypeConverter.parseBase64Binary(s);

		ByteArrayInputStream bais = new ByteArrayInputStream(b);
		ObjectInputStream ois = new ObjectInputStream(bais);
		return ois.readObject();
	}
}