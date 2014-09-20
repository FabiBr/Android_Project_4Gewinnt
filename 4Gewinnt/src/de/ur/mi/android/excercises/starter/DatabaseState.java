package de.ur.mi.android.excercises.starter;

import android.app.Application;

public class DatabaseState  extends Application{

	private GameDB db = new GameDB(this);
	
	public GameDB getDb() {
		return db;
	}

}
