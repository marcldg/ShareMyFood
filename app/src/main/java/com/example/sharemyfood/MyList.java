package com.example.sharemyfood;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MyList extends AppCompatActivity {

    // Declaring variables.
    RecyclerView recyclerView;
    Toolbar toolbar;
    FloatingActionButton add;

    RecyclerViewAdapter recyclerViewAdapter;

    private DBManager dbManager;

    ArrayList<String> images, titles, descriptions, quantities, locations;
    ArrayList<Bitmap> pictures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // Initialising variables.
        add = findViewById(R.id.fab);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.showAllFood);
        setSupportActionBar(toolbar);

        images = new ArrayList<>();
        pictures = new ArrayList<>();
        titles = new ArrayList<>();
        descriptions = new ArrayList<>();
        quantities = new ArrayList<>();
        locations = new ArrayList<>();

        dbManager = new DBManager(this);
        dbManager.open();

        Intent getUsername = getIntent();
        String username = getUsername.getStringExtra("username");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent redirectAddFood = new Intent(getApplicationContext(), AddFood.class);
                redirectAddFood.putExtra("username", username);
                Toast.makeText(MyList.this, username, Toast.LENGTH_SHORT).show();
                startActivity(redirectAddFood);
                finish();
            }
        });

        retrieveData(username);
        recyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext(), pictures, titles, descriptions, quantities, locations);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MyList.this));
    }

    // Convert String back to bitmap.
    private Bitmap stringToBitmap(String string)
    {
        Bitmap bitmap = null;

        try{
            byte[] encodeByteArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(encodeByteArray, 0, encodeByteArray.length);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return bitmap;
    }

    // Retrieve USER food data from database.
    void retrieveData(String username)
    {
        Cursor cursor = dbManager.getAllUserFood(username);

        if(cursor.getCount() == 0){
            Toast.makeText(this, "No food items found.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            while (cursor.moveToNext())
            {
                pictures.add(stringToBitmap(cursor.getString(0)));
                titles.add(cursor.getString(2));
                descriptions.add(cursor.getString(3));
                quantities.add(cursor.getString(6));
                locations.add(cursor.getString(7));
            }
        }
    }

    // Initialise menu bar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    // Handle menu activities.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.home:
                Intent redirectHome = new Intent(getApplicationContext(), Home.class);
                startActivity(redirectHome);
                finish();
                return true;
            case R.id.account:
                Intent redirectAccount = new Intent(getApplicationContext(), Account.class);
                startActivity(redirectAccount);
                finish();
                return true;
            case R.id.myList:
                Intent redirectMyList = new Intent(getApplicationContext(), MyList.class);
                startActivity(redirectMyList);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}