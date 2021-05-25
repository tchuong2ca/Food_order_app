package com.example.project.Adapter;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.Activity.ItemClickListener;
import com.example.project.common.Permission;
import com.example.project.common.common;

public class FoodAdapter extends RecyclerView.ViewHolder
        implements View.OnClickListener,
View.OnCreateContextMenuListener{


    public TextView food_name;
    public ImageView food_image;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    public FoodAdapter(@NonNull View itemView) {
        super(itemView);
        food_image = itemView.findViewById(R.id.food_img);
        food_name = itemView.findViewById(R.id.food_name);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
    }
    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (Permission.permission.equals("ad")){menu.setHeaderTitle("Choose an option");
            menu.add(0,0,getAdapterPosition(), common.UPDATE);
            menu.add(0,1,getAdapterPosition(),common.DELETE);}
    }
}
