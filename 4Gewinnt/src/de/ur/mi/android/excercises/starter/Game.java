package de.ur.mi.android.excercises.starter;

public class Game {
	
	private int id;
	private String field;
	private String player1;
	private String player2;
	private String currentPlayer;
	
	
	public Game() {
		
	}
	
	public Game(int id, String field, String player1, String player2, String lastPlayer){
		this.id = id; this.field = field; this.player1 = player1; this.player2 = player2; this.currentPlayer = lastPlayer;
	}
	

	public int getGameId() {
		return id;
	}
	public String getField() {
		return field;
	}
	public String getP1() {
		return player1;
	}
	public String getP2() {
		return player2;
	}
	public String getLastPlayer() {
		return currentPlayer;
	}

}
