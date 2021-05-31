package com.example.sharemyfood;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLDBHelper extends SQLiteOpenHelper
{
    // Creating the table names.
    public static final String TABLE_NAME = "USERS";
    public static final String TABLE_NAME_FOOD = "FOOD";

    // Creating the columns of the users table.
    public static final String _EMAIL = "_email";
    public static final String PASSWORD = "password";
    public static final String NAME = "name";
    public static final String ADDRESS = "address";
    public static final String PHONE = "phone";

    // Creating the columns of the FOOD table.
    public static final String _PICTURE = "_picture";
    public static final String USERNAME = "_email";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String DATE = "date";
    public static final String TIME = "time";
    public static final String QUANTITY = "quantity";
    public static final String LOCATION = "location";

    // Creating the Database information
    static final String DB_NAME = "SHARE_MY_FOOD.DB";

    // Setting the Database Version.
    static final int DB_VERSION = 1;

    // Creating the Table Queries.
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _EMAIL + " TEXT PRIMARY KEY, " + PASSWORD + " TEXT NOT NULL, " + NAME + " TEXT NOT NULL, " + ADDRESS
            + " TEXT NOT NULL, " + PHONE + " TEXT NOT NULL);";

    private static final String CREATE_TABLE_FOOD = "create table " + TABLE_NAME_FOOD + "(" + _PICTURE + " TEXT PRIMARY KEY, " + USERNAME + " TEXT NOT NULL, " + TITLE + " TEXT NOT NULL, " + DESCRIPTION
            + " TEXT NOT NULL, " + DATE + " TEXT NOT NULL, " + TIME + " TEXT NOT NULL, " + QUANTITY + " TEXT NOT NULL, " + LOCATION + " TEXT NOT NULL);";

    // Creating the constructor for the Database helper
    public SQLDBHelper(Context context)
    {
        super(context, DB_NAME,null, DB_VERSION);
    }

    // Function to insert the tables in the database.
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Executing the Queries.
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE_FOOD);
    }

    // Function to overwrite the table if it exists already.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}