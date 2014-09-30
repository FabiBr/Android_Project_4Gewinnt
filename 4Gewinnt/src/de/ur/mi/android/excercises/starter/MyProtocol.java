package de.ur.mi.android.excercises.starter;

import java.io.IOException;

public class MyProtocol {
	
	private static final String INSERT_NEWUSER_KEY = "newUserInsert";
	private static final String INSERT_GAME_KEY = "newGameInsert";
	private static final String INCREMENT_USER_WIN_KEY = "userWins";
	private static final String PASSWORD_CHECK_KEY = "checkPw";
	private static final String GET_ALL_USERS_KEY = "allUsersGet";
	private static final String GET_GAMES_KEY = "myGamesGet";
	

	
	
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
	
	public String loginPwCheck(String username, String userPw) {
		
		String protocolString = PASSWORD_CHECK_KEY + " " + username + " " + userPw;
		return protocolString;
		
	}
	public String getAllUsers() {
		return GET_ALL_USERS_KEY;
	}
	
	public String getMyCurrentGames(String myUsername) {
		String protocolString = GET_GAMES_KEY + " " + myUsername;
			
		return protocolString;
	}
	
	public String addNewGame(String user1me, String user2) {
		Field temp = new Field();
		String emptyField = "";
		try {
			emptyField = Serializer.serialize(temp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String protocolString = INSERT_GAME_KEY + " "  + emptyField + " " + user1me + " " + user2 + " " + user1me;
		return protocolString;
	}
	
}

