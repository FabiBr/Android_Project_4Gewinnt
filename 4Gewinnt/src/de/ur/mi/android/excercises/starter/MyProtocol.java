package de.ur.mi.android.excercises.starter;

public class MyProtocol {
	
	private static String INSERT_NEWUSER_KEY = "newUserInsert";
	private static String INSERT_GAME_KEY = "newGameInsert";
	private static String  INCREMENT_USER_WIN_KEY = "userWins";
	
	public MyProtocol() {
		
	}

	public String newUserDataOutput(String username, String userPw) {
		int gamesWon = 0;
		int gamesLost = 0;
		int premium = 0;
		
		String protocolString = INSERT_NEWUSER_KEY + " " + username + " " + userPw + " " + gamesWon + " " + gamesLost + " " + premium;

		System.out.println(protocolString);
		
		return protocolString;
	}
	
	public String incrementWinCount(String username) {
		
		String protocolString = INCREMENT_USER_WIN_KEY + " " + username;

		return protocolString;
	}
	
}

