package de.ur.mi.android.excercises.starter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class GameDB {

	private static final String DB_NAME = "gameDB";
	private static final int DB_Version = 1;

	// usersTableKeys
	private static final String USER_TABLE = "users";

	private static final String USER_ID_KEY = "id";
	private static final String USER_NAME_KEY = "name";
	private static final String USER_PW_KEY = "pw";
	private static final String USER_GWON_KEY = "gamesWon";
	private static final String USER_GLOST_KEY = "gamesLost";
	private static final String USER_PREMIUM_KEY = "premium";
	
	//GamesTableKeys
	
	private static final String LAST_OPPONENTS_TABLE = "opponents";
	
	private static final String OPPONENTS_ID_KEY = "id";
	private static final String OPPONENTS_NAME_KEY = "oppName";
	
	private static final String GAMES_TABLE = "games";

	private static final String GAMES_ID_KEY = "id";
	private static final String GAMES_FIELD_KEY = "field";
	private static final String GAMES_P1_KEY = "player1";
	private static final String GAMES_P2_KEY = "player2";
	private static final String GAMES_LASTPLAYER_KEY = "lastPlayer";
	
	private static final String MY_CURRENT_ACCOUNT = "myCurrentData";

	private static final String MY_ID_KEY = "id";
	private static final String MY_NAME_KEY = "name";
	private static final String MY_PW_KEY = "pw";
	private static final String MY_GWON_KEY = "gamesWon";
	private static final String MY_GLOST_KEY = "gamesLost";
	private static final String MY_PREMIUM_KEY = "premium";
	
	
	


	

	private myDbHelper dbHelper;
	private SQLiteDatabase db;
	
	public GameDB(Context context) {
		dbHelper = new myDbHelper(context, DB_NAME, null, DB_Version);
	}
	
	
	// general method to open/close the db
	public void open() throws SQLException {
		try {
			db = dbHelper.getWritableDatabase();
		} catch (SQLException e) {
			db = dbHelper.getReadableDatabase();
		}
	}

	public void close() {
		db.close();
	}
	
	//userTable methods add/get User
		public void addUser(User user) {

			ContentValues values = new ContentValues();
			values.put(USER_NAME_KEY, user.getUsername());
			values.put(USER_PW_KEY, user.getPW());
			values.put(USER_GWON_KEY, user.getWon());
			values.put(USER_GLOST_KEY, user.getLost());
			values.put(USER_PREMIUM_KEY, user.getPremiumStatus());

			db.insert(USER_TABLE, null, values);
		}
		
		public void updateMyCurrentData(String username) {
			String[] columns = { USER_ID_KEY, USER_NAME_KEY, USER_PW_KEY,
					USER_GWON_KEY, USER_GLOST_KEY, USER_PREMIUM_KEY };
			Cursor cursor = db.query(USER_TABLE, columns, USER_NAME_KEY + " = ?",
					new String[] { username }, null, null, null);
			
			ContentValues values = new ContentValues();
			cursor.moveToFirst();
			
			values.put(MY_NAME_KEY, cursor.getString(1));
			values.put(MY_PW_KEY, cursor.getString(2));
			values.put(MY_GWON_KEY, cursor.getString(3));
			values.put(MY_GLOST_KEY, cursor.getString(4));
			values.put(MY_PREMIUM_KEY, cursor.getString(5));
			db.update(MY_CURRENT_ACCOUNT, values, "id" + "='1'", null);
		}
		
		
		// use id = 1
		public User getMyData() {
			int id = 1;
			String[] columns = { MY_ID_KEY, MY_NAME_KEY, MY_PW_KEY,
					MY_GWON_KEY, MY_GLOST_KEY, MY_PREMIUM_KEY };
			Cursor cursor = db.query(MY_CURRENT_ACCOUNT, columns, " id = ?",
					new String[] { String.valueOf(id) }, null, null, null);

			if (cursor != null)
				cursor.moveToFirst();

			User userAtID = new User(Integer.parseInt(cursor.getString(0)),
					cursor.getString(1), cursor.getString(2),
					Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor
							.getString(4)), Integer.parseInt(cursor.getString(5)));
			return userAtID;
		}
	
		
		public void addOpponent(String name) {
			ContentValues values = new ContentValues();
			values.put(USER_NAME_KEY, name);
			db.insert(LAST_OPPONENTS_TABLE, null, values);
		}
		
		/*
		public ArrayList<Game> getCurrentGames() {
			ArrayList<Game> myGames = new ArrayList<Game>();
			String[] columns = {GAMES_ID_KEY, GAMES_P1_KEY, GAMES_P2_KEY};
			Cursor cursor = null;
			try {
				String sql = "SELECT * FROM " + GAMES_TABLE;
				cursor = db.rawQuery(sql, null);
				//cursor = db.query(GAMES_TABLE, columns, null, null, null, null, null);
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			
			if (cursor.moveToFirst()) {
				do {
					int id = cursor.getInt(0);
					String p1 = cursor.getString(2);
					String p2 = cursor.getString(3);
					//TODO
				} while (cursor.moveToNext());
			}
			return myGames;
		}
		
		
		public void addGame(Game game) throws IOException {
			ContentValues values = new ContentValues();
			values.put(GAMES_ID_KEY, game.getGameId());
			values.put(GAMES_FIELD_KEY, game.getField());
			values.put(GAMES_P1_KEY, game.getP1());
			values.put(GAMES_P2_KEY, game.getP2());
			values.put(GAMES_LASTPLAYER_KEY, game.getLastPlayer());

			db.insert(GAMES_TABLE, null, values);
		}
		
		public void deleteGame(int id) {
			String whereClause = GAMES_ID_KEY+ " = '" + String.valueOf(id) + "'";

			db.delete(GAMES_TABLE, whereClause, null);
		}
		*/
		public ArrayList<String> getAllOpponents() {
			ArrayList<String> names = new ArrayList<String>();
			Cursor cursor = db.query(LAST_OPPONENTS_TABLE, new String[] { OPPONENTS_ID_KEY,
					OPPONENTS_NAME_KEY }, null, null, null, null, null);
			if (cursor.moveToFirst()) {
				do {
					String name = cursor.getString(1);
					names.add(name);

				} while (cursor.moveToNext());
			}
			return names;
		}

	
	private class myDbHelper extends SQLiteOpenHelper {
		public myDbHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, DB_NAME, null, DB_Version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			String CREATE_USER_TABLE = "create table " + USER_TABLE + " ("
					+ USER_ID_KEY + " integer primary key autoincrement, "
					+ USER_NAME_KEY + " text, " + USER_PW_KEY + " text, "
					+ USER_GWON_KEY + " integer, " + USER_GLOST_KEY
					+ " integer, " + USER_PREMIUM_KEY + " integer)";
			
			String CREATE_LAST_OPPONENTS_TABLE = "create table " + LAST_OPPONENTS_TABLE + " ("
					+ OPPONENTS_ID_KEY + " integer primary key autoincrement, "
					+ OPPONENTS_NAME_KEY  + " text)";
			String CREATE_CURRENT_GAMES_TABLE = "CREATE TABLE "
					+ GAMES_TABLE + " (" + GAMES_ID_KEY
					+ " INTEGER PRIMARY KEY , " + GAMES_FIELD_KEY
					+ " TEXT, " + GAMES_P1_KEY + " TEXT, " + GAMES_P2_KEY
					+ " TEXT, " + GAMES_LASTPLAYER_KEY + " INTEGER)";
			
			String CREATE_MY_CURRENT_ACCOUNT_TABLE = "create table " + MY_CURRENT_ACCOUNT + " ("
					+ MY_ID_KEY + " integer primary key autoincrement, "
					+ MY_NAME_KEY + " text, " + MY_PW_KEY + " text, "
					+ MY_GWON_KEY + " integer, " + MY_GLOST_KEY
					+ " integer, " + MY_PREMIUM_KEY + " integer)";
			
			db.execSQL(CREATE_USER_TABLE);
			db.execSQL(CREATE_LAST_OPPONENTS_TABLE);
			db.execSQL(CREATE_CURRENT_GAMES_TABLE);
			db.execSQL(CREATE_MY_CURRENT_ACCOUNT_TABLE);
			
			ContentValues values = new ContentValues();
			values.put(MY_ID_KEY, 1);
			values.put(MY_PW_KEY, " ");
			values.put(MY_GWON_KEY, " ");
			values.put(MY_GLOST_KEY, " ");
			values.put(MY_PREMIUM_KEY, " ");

			db.insert(MY_CURRENT_ACCOUNT, null, values);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}
}
