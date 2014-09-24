package de.ur.mi.android.excercises.starter;

public class MyProtocol {
	
	private static final String INSERT_NEWUSER_KEY = "newUserInsert";
	private static final String INSERT_GAME_KEY = "newGameInsert";
	private static final String INCREMENT_USER_WIN_KEY = "userWins";
	private static final String PASSWORD_CHECK_KEY = "checkPw";
	private static final String GET_ALL_USERS_KEY = "allUsersGet";
	
	
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
	
}

