package de.ur.mi.android.excercises.starter;

public class Game {
	
	private int id;
	private Field field;
	private User player1;
	private User player2;
	private int lastPlayer;
	
	
	public Game() {
		
	}
	
	public Game(int id, Field field, User player1, User player2, int lastPlayer){
		this.id = id; this.field = field; this.player1 = player1; this.player2 = player2; this.lastPlayer = lastPlayer;
	}
	
	public int getGameId() {
		return id;
	}
	public Field getField() {
		return field;
	}
	public User getP1() {
		return player1;
	}
	public User getP2() {
		return player2;
	}
	public int getLastPlayer() {
		return lastPlayer;
	}

}
