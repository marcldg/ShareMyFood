package com.example.sharemyfood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Create the intent to navigate to the Sign Up activity.
    public void signUp(View view)
    {
        Intent signUp = new Intent(this, SignUp.class);
        startActivity(signUp);
    }

    // Declaring variables.
    EditText userName, password;
    Button login, signUp;

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialising Variables.
        userName = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        signUp = findViewById(R.id.signUp);

        dbManager = new DBManager(this);
        dbManager.open();

        // Create OnClick Listener for the login button.
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userName.getText().length() != 0 && password.getText().length() != 0)
                {
                    String getUsername = userName.getText().toString();
                    String getPassword = password.getText().toString();

                    Boolean checkUserPass = dbManager.verifyUsernamePassword(getUsername,getPassword);
                    if(checkUserPass == false)
                    {
                        Toast.makeText(MainActivity.this, "Invalid credentials. Please try again or click on the sign up button below.", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Welcome " + getUsername, Toast.LENGTH_SHORT).show();

                        // Redirect user to homepage upon successful login.
                        Intent redirectHome = new Intent(getApplicationContext(), Home.class);
                        redirectHome.putExtra("username", getUsername);
                        startActivity(redirectHome);
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Please enter a valid username and password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}