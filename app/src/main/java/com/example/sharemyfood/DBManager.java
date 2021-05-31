package com.example.sharemyfood;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.security.PublicKey;

public class DBManager
{
    private SQLDBHelper sqldbHelper;
    private Context context;
    private SQLiteDatabase database;

    // Class constructor.
    public DBManager(Context contxt)
    {
        context = contxt;
    }

    // Method to open database.
    public DBManager open() throws SQLException
    {
        sqldbHelper = new SQLDBHelper(context);
        database = sqldbHelper.getWritableDatabase();
        return this;
    }

    // Method to close database.
    public void close()
    {
        sqldbHelper.close();
    }

    // Method to insert user to database.
    public void insertUser(String _email, String password, String name, String address, String phone)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLDBHelper._EMAIL, _email);
        contentValues.put(SQLDBHelper.PASSWORD, password);
        contentValues.put(SQLDBHelper.NAME, name);
        contentValues.put(SQLDBHelper.ADDRESS, address);
        contentValues.put(SQLDBHelper.PHONE, phone);
        database.insert(SQLDBHelper.TABLE_NAME,null, contentValues);
    }

    // Method to insert food to database.
    public void insertFood(String _picture, String _email, String title, String description, String date, String time, String quantity, String location)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLDBHelper._PICTURE, _picture);
        //Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
        contentValues.put(SQLDBHelper.USERNAME, _email);
        //Toast.makeText(context, "2", Toast.LENGTH_SHORT).show();
        contentValues.put(SQLDBHelper.TITLE, title);
        //Toast.makeText(context, "3", Toast.LENGTH_SHORT).show();
        contentValues.put(SQLDBHelper.DESCRIPTION, description);
        //Toast.makeText(context, "4", Toast.LENGTH_SHORT).show();
        contentValues.put(SQLDBHelper.DATE, date);
        //Toast.makeText(context, "5", Toast.LENGTH_SHORT).show();
        contentValues.put(SQLDBHelper.TIME, time);
        //Toast.makeText(context, "6", Toast.LENGTH_SHORT).show();
        contentValues.put(SQLDBHelper.QUANTITY, quantity);
        //Toast.makeText(context, "7", Toast.LENGTH_SHORT).show();
        contentValues.put(SQLDBHelper.LOCATION, location);
        //Toast.makeText(context, "8", Toast.LENGTH_SHORT).show();
        database.insert(SQLDBHelper.TABLE_NAME_FOOD,null, contentValues);
        //Toast.makeText(context, "9", Toast.LENGTH_SHORT).show();
    }

    // Method to get all the Food in the database.
    Cursor getAllFood() {
        Cursor cursor = null;

        if (database != null)
        {
            cursor = database.rawQuery("Select * from FOOD", null);
        }
        return cursor;
    }

    // Method to get all the Food uploaded by a specific user.
    Cursor getAllUserFood(String _email)
    {
        Cursor cursor = null;

        if (database != null)
        {
            Toast.makeText(context, _email, Toast.LENGTH_SHORT).show();
            cursor = database.rawQuery("Select * from FOOD where _email = ?", new String[]{_email});
        }

        return cursor;
    }

    // Method to check if username exists.
    public Boolean verifyUsername (String _email)
    {
        Cursor cursor = database.rawQuery("Select * from USERS where _EMAIL = ?", new String[] {_email});

        if (cursor.getCount() > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    // Method to check if username password exists.
    public Boolean verifyUsernamePassword (String _email, String password)
    {
        Cursor cursor = database.rawQuery("Select * from USERS where _EMAIL = ? and PASSWORD = ?", new String[] {_email, password});

        if (cursor.getCount() > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

}
