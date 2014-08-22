package de.ur.mi.android.excercises.starter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class GameDB extends SQLiteOpenHelper {

	private static final String DB_NAME = "gameDB";
	private static final int DB_Version = 1;
	
	// usersTableKeys
	private static final String USER_TABLE = "users";
	
	private static final String USER_ID_KEY = "id";
	private static final String USER_NAME_KEY="name";
	private static final String USER_PW_KEY="pw";
	private static final String USER_GWON_KEY="gamesWon";
	private static final String USER_GLOST_KEY="gamesLost";
	private static final String USER_PREMIUM_KEY="premium";

	public GameDB(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DB_NAME, null, DB_Version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_USER_TABLE = "CREATE TABLE users ( "
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "name TEXT, "
				+ "pw TEXT, " + "gamesWon INTEGER, " + "gamesLost INTEGER, "
				+ "premium INTEGER )";
		db.execSQL(CREATE_USER_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	public void addUser(User user) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(USER_NAME_KEY, user.getUsername());
		values.put(USER_PW_KEY, user.getPW());
		values.put(USER_GWON_KEY, user.getWon());
		values.put(USER_GLOST_KEY, user.getLost());
		values.put(USER_PREMIUM_KEY, user.getPremiumStatus());

		db.insert(USER_TABLE, null, values);

		db.close();
	}

	public User getUser(int id){
		 
        String[] columns = {USER_ID_KEY, USER_NAME_KEY, USER_PW_KEY, USER_GWON_KEY, USER_GLOST_KEY, USER_PREMIUM_KEY};
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor =
                db.query(USER_TABLE, columns, " id = ?", new String[] { String.valueOf(id) }, null, null, null);
 
        if (cursor != null)
            cursor.moveToFirst();
 
        User userAtID = new User(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)),Integer.parseInt(cursor.getString(4)), Integer.parseInt(cursor.getString(5)));
        return userAtID;
    }
}
