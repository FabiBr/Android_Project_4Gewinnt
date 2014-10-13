package de.ur.mi.android.excercises.starter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {

	private int id;
	private String md5username;
	private String md5pw;
	private int gamesWon;
	private int gamesLost;
	private int premium;

	public User() {
	}

	public User(int id, String username, String pw, int gamesWon,
			int gamesLost, int premium) {
		this.id = id;
		this.md5username = username;
		this.md5pw = pw;
		this.gamesWon = gamesWon;
		this.gamesLost = gamesLost;
		this.premium = premium;
	}

	public int getID() {
		return this.id;
	}

	public String getUsername() {
		return this.md5username;
	}

	public String getPW() {
		return this.md5pw;
	}

	public int getWon() {
		return this.gamesWon;
	}

	public int getLost() {
		return this.gamesLost;
	}

	public int getPremiumStatus() {
		return this.premium;
	}
	
	
	//md5 encoding for pw and username
	public String computeMD5Hash(String password)
    {
		String result = "";
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(password.getBytes());
            byte messageDigest[] = digest.digest();
      
            StringBuffer MD5Hash = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
            {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                MD5Hash.append(h);
            }
                  
            result = MD5Hash.toString();
             
            }
            catch (NoSuchAlgorithmException e)
            {
            e.printStackTrace();
            }
        return result;
         
    }
}
