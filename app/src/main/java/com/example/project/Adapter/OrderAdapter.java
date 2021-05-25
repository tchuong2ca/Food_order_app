package com.example.project.Adapter;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Activity.ItemClickListener;
import com.example.project.R;

public class OrderAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {
public TextView odID,odStt ,odPhone, odAddress;

private ItemClickListener itemClickListener;

public OrderAdapter(View view){
    super(view);
    odID = (TextView)view.findViewById(R.id.order_id);
    odAddress = (TextView)view.findViewById(R.id.order_address);
    odStt= (TextView)view.findViewById(R.id.order_status);
    odPhone = (TextView)view.findViewById(R.id.order_phone);

    view.setOnClickListener(this);
}

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
itemClickListener.onClick(v, getAdapterPosition(),false);
    }
}
