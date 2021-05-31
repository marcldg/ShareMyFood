package com.example.sharemyfood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {

    // Declaring variables.
    EditText fullName, email, phone, address, passwordSignUp, confirmPassword;
    Button signUpUser;

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        // Change title to Sign Up.
        setTitle("SIGN-UP");

        // Initialising Variables.
        fullName = findViewById(R.id.fullName);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        passwordSignUp = findViewById(R.id.passwordSignUp);
        confirmPassword = findViewById(R.id.confirmPassword);
        signUpUser = findViewById(R.id.signUpUser);

        dbManager = new DBManager(this);
        dbManager.open();

        signUpUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = fullName.getText().toString();
                String userEmail = email.getText().toString();
                String userPhone = phone.getText().toString();
                String userAddress = address.getText().toString();
                String userPassword = passwordSignUp.getText().toString();
                String userConfirmPassword = confirmPassword.getText().toString();

                // Check for empty fields.
                if(userName.equals("") || userEmail.equals("") || userPhone.equals("") || userAddress.equals("") || userPassword.equals("") || userConfirmPassword.equals(""))
                {
                    Toast.makeText(SignUp.this, "Some fields are missing, please fill the form correctly.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    // Check for valid email address.
                    if (Patterns.EMAIL_ADDRESS.matcher(userEmail).matches())
                    {
                        // Check for valid phone number - victorian phone number == 10 digits.
                        if (userPhone.length() == 10)
                        {
                            // Check if passwords match.
                            if (userPassword.equals(userConfirmPassword))
                            {
                                // Check if user already exits
                                Boolean checkUser = dbManager.verifyUsername(userEmail);
                                if (checkUser == false)
                                {
                                    // Insert user into database.
                                    dbManager.insertUser(userEmail, userPassword, userName, userAddress, userPhone);

                                    Toast.makeText(SignUp.this, "Registered successfully", Toast.LENGTH_SHORT).show();

                                    // Create the intent to navigate to the Sign Up activity.
                                    Intent loginNewUser = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(loginNewUser);
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(SignUp.this, "User already exists, please login using your email and password in the login page.", Toast.LENGTH_LONG).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(SignUp.this, "Passwords do not match, please retry.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(SignUp.this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(SignUp.this, "Please enter a valid Email address.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}