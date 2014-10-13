package de.ur.mi.android.excercises.starter;

import java.io.*;
import javax.xml.bind.*;

import android.util.Base64;

public class Serializer {
	public static String serialize(Serializable o) throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(o);
		oos.close();
		return new String(Base64.encode(baos.toByteArray(), 0));
	}

	public static Object deserialize(String s) throws IOException,
			ClassNotFoundException {

		
		
		byte [] data = Base64.decode( s, 0 );
        ObjectInputStream ois = new ObjectInputStream( 
                                        new ByteArrayInputStream(  data ) );
        Object o  = ois.readObject();
        ois.close();
        return o;
	}
}