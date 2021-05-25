package com.example.project.Adapter;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.project.Activity.ItemClickListener;
import com.example.project.R;
import com.example.project.common.common;

public class CartAdapter extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener{
    public TextView cart_name, price,total_price;
private ItemClickListener itemClickListener;
public ElegantNumberButton numb;
public ImageView cart_image;
public void setCart_name(TextView cart_name){
    this.cart_name=cart_name;
}
    public CartAdapter(@NonNull View itemView) {
        super(itemView);
        cart_name =(TextView)itemView.findViewById(R.id.cart_item_name);
        price =(TextView)itemView.findViewById(R.id.cart_item_Price);
        itemView.setOnCreateContextMenuListener(this);
        numb = (ElegantNumberButton) itemView.findViewById(R.id.count_button_in_cart);
        total_price= (TextView)itemView.findViewById(R.id.total);
        cart_image= (ImageView) itemView.findViewById(R.id.cart_food_img);
        itemView.setOnCreateContextMenuListener(this);
}
    @Override
    public void onClick(View v) {
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Action");
        menu.add(0,0, getAdapterPosition(), common.DELETE);
    }
}
