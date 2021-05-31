package com.example.sharemyfood;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.service.media.MediaBrowserService;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

public class AddFood extends AppCompatActivity {

    // Add a permission code for access to gallery.
    private static final int PERMISSION_GALLERY = 50;
    // Add a request code for access to gallery.
    private static final int GALLERY_REQUEST = 20;
    // Add a permission code for access to camera.
    private static final int PERMISSION_CAMERA = 70;
    // Add a request code for access to camera.
    private static final int CAMERA_REQUEST = 10;

    // Declaring Variables
    ImageView imageView;
    Button shootImage, addImage, save;
    EditText title, description, date, pickUpTime, quantity, location;

    private DBManager dbManager;

    DatePickerDialog.OnDateSetListener setListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        // Initialising Variables.
        imageView = findViewById(R.id.imageView);
        shootImage = findViewById(R.id.shootImage);
        addImage = findViewById(R.id.addImage);
        save = findViewById(R.id.save);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        date = findViewById(R.id.date);
        pickUpTime = findViewById(R.id.pickUpTime);
        quantity = findViewById(R.id.quantity);
        location = findViewById(R.id.location);

        dbManager = new DBManager(this);
        dbManager.open();

        imageView.setDrawingCacheEnabled(true);

        // Initialising the calendar functionality.
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddFood.this, new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day)
                    {
                        month = month + 1;
                        String getDate = day + "/" + month + "/" + year;
                        date.setText(getDate);
                    }
                }, year,month,day);
                datePickerDialog.show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(v);
            }
        });
    }

    // Method to save new food item.
    public void save(View view)
    {
        Bitmap foodImage = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        // Get username.
        Intent getName = getIntent();
        String username = getName.getStringExtra("username");
        // Gather the data from all the fields.
        String foodPic = bitmapToString(foodImage);
        String foodTitle = title.getText().toString();
        String foodDescription = description.getText().toString();
        String foodDate = date.getText().toString();
        String foodTime = pickUpTime.getText().toString();
        String foodQuantity = quantity.getText().toString();
        String foodLocation = location.getText().toString();

        // Check for empty fields.
        if(foodTitle.equals("") || foodDescription.equals("") || foodDate.equals("") || foodTime.equals("") || foodQuantity.equals("") || foodLocation.equals(""))
        {
            Toast.makeText(AddFood.this, "Some fields are missing, please fill *ALL* fields from the form.", Toast.LENGTH_SHORT).show();
        }
        else{
            // Save new food record to database.
            dbManager.insertFood(foodPic,username,foodTitle, foodDescription, foodDate, foodTime, foodQuantity, foodLocation);
            Toast.makeText(AddFood.this, "success", Toast.LENGTH_SHORT).show();
            Intent redirectHome = new Intent(getApplicationContext(), Home.class);
            startActivity(redirectHome);
            finish();
        }
    }

    // Method to convert bitmap to string.
    private String bitmapToString(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    // Function used to open the media gallery.
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void openMediaGallery(View view) {

        // Check for permission.
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
        {
            // Enable permission
            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};

            // Show popup request.
            requestPermissions(permission, PERMISSION_GALLERY);
        }
        else {
            // Permission already granted.
            // Run the gallery request code.
            Intent openGallery = new Intent();
            openGallery.setType("image/*");
            openGallery.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(openGallery, "Pick a picture"), GALLERY_REQUEST);
        }
    }

    // Run the camera activity.
    @RequiresApi(api = Build.VERSION_CODES.M)
    public  void openCamera(View view){

        // Check for permission.
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
        {
            // Enable permission
            String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

            // Show popup request.
            requestPermissions(permission, PERMISSION_CAMERA);
        }
        else {
            // Permission already granted.
            // Setup a new intent to get access to camera.
            Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            // Make sure that the phone can handle the intent. I.E if the phone does not have a camera then throw an error.
            if (openCamera.resolveActivity(getPackageManager()) != null)
            {
                startActivityForResult(openCamera, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "Error opening camera.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Handling permission result.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case PERMISSION_GALLERY:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // Run the gallery request code.
                    Intent openGallery = new Intent();
                    openGallery.setType("image/*");
                    openGallery.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(openGallery, "Pick a picture"), GALLERY_REQUEST);
                }
                else{
                    Toast.makeText(this, "Permission to access gallery denied...", Toast.LENGTH_SHORT).show();
                }
                break;
            case PERMISSION_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // Setup a new intent to get access to camera.
                    Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    // Make sure that the phone can handle the intent. I.E if the phone does not have a camera then throw an error.
                    if (openCamera.resolveActivity(getPackageManager()) != null)
                    {
                        startActivityForResult(openCamera, CAMERA_REQUEST);
                    }
                    else
                    {
                        Toast.makeText(this, "Error opening camera.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(this, "Permission to access camera denied...", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    // Handle the activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // If request returns positive then set picture to imageView.
        if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST) {
            try {
                Uri selectedImage = intent.getData();
                InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                imageView.setImageBitmap(BitmapFactory.decodeStream(imageStream));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        // If request returns positive then set picture to imageView.
        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {
            Bundle image = intent.getExtras();
            Bitmap picture = (Bitmap) image.get("data");
            imageView.setImageBitmap(picture);
        }
    }
}