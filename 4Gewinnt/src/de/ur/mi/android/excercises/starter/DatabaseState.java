package de.ur.mi.android.excercises.starter;

import java.util.ArrayList;

import android.app.Application;

public class DatabaseState extends Application{

	private GameDB db = new GameDB(this);
	private ArrayList<User> allUsers;
	
	public GameDB getDb() {
		return db;
	}
	public void setAllUsers(ArrayList<User> users) {
		allUsers = users;
	}

	public ArrayList<User> getAllUsers() {
		ArrayList<User> users = allUsers;
		return allUsers;
	}

}
