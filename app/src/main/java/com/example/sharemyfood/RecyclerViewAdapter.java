package com.example.sharemyfood;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    // Declaring variables and array lists.
    private Context context;
    private ArrayList pictures, titlesArray, descriptionsArray, quantitiesArray, locationsArray;

    RecyclerViewAdapter(Context contxt, ArrayList images, ArrayList titles, ArrayList descriptions, ArrayList quantities, ArrayList locations)
    {
        this.context = contxt;
        this.pictures = images;
        this.titlesArray = titles;
        this.descriptionsArray = descriptions;
        this.quantitiesArray = quantities;
        this.locationsArray = locations;
    }

    // Method to inflate the recycler view.
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recyclerviewlayout, parent, false);
        return new RecyclerViewAdapter.MyViewHolder(view);
    }

    // Bind variable to the holder.
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.pic.setImageBitmap(Bitmap.createBitmap((Bitmap) pictures.get(position)));
        holder.title.setText(String.valueOf(titlesArray.get(position)));
        holder.description.setText(String.valueOf(descriptionsArray.get(position)));
        holder.quantity.setText("Quantity: " + String.valueOf(quantitiesArray.get(position)));
        holder.location.setText("Location: " + String.valueOf(locationsArray.get(position)));
    }

    // Method to get the number of items to be displayed on the recycler view.
    @Override
    public int getItemCount() {
        return titlesArray.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        // Declaring variables.
        ImageView pic;
        TextView title, description, quantity, location;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialising variables.
            pic = itemView.findViewById(R.id.foodPic);
            title = itemView.findViewById(R.id.foodTitle);
            description = itemView.findViewById(R.id.foodDescription);
            quantity = itemView.findViewById(R.id.foodQuantity);
            location = itemView.findViewById(R.id.foodLocation);
        }
    }
}
