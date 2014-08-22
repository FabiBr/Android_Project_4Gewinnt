package de.ur.mi.android.excercises.starter;

public class User {

	private int id;
	private String md5username;
	private String md5pw;
	private int gamesWon;
	private int gamesLost;
	private int premium;

	public User() {
	}

	public User(int id, String md5username, String md5pw, int gamesWon,
			int gamesLost, int premium) {
		this.id = id;
		this.md5username = md5username;
		this.md5pw = md5pw;
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
}
