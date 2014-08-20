package de.ur.mi.android.excercises.starter;

import java.sql.*;

import android.content.Context;
import android.database.sqlite.*;


public class DBController extends SQLiteOpenHelper {
	

	public DBController(Context context) {
		super(context, "dbname", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
}
