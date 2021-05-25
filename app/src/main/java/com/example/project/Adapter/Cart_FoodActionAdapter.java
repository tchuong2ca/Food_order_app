package com.example.project.Adapter;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.project.Activity.Cart;
import com.example.project.Database.Database;
import com.example.project.R;
import com.example.project.model.Order;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class Cart_FoodActionAdapter extends RecyclerView.Adapter<CartAdapter>{
    private List<Order> listData;
    Order order;

    public Cart context;
    public ElegantNumberButton ele;
    public Cart_FoodActionAdapter(List<Order> listData, Cart context) {
        this.listData = listData;
        this.context = context;
}
    public CartAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.cart_lo, parent, false);
        return new CartAdapter(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull CartAdapter holder, int position) {
        Picasso.get()
                .load(listData.get(position).getImage())
                .resize(70,70)
                .centerCrop()
                .into(holder.cart_image);
        Locale locale = new Locale("en","US");
        NumberFormat num = NumberFormat.getCurrencyInstance(locale);
        holder.cart_name.setText(listData.get(position).getProductName());
        holder.numb.setNumber(String.valueOf(listData.get(position).getQuantity()));
        int price = (Integer.parseInt(listData.get(position).getPrice()))*(Integer.parseInt(listData.get(position).getQuantity()));
        holder.price.setText(num.format(price));

        holder.numb.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {

                listData.get(position).setQuantity(String.valueOf(newValue));
                int price = (Integer.parseInt(listData.get(position).getPrice()))*(Integer.parseInt(listData.get(position).getQuantity()));
                holder.price.setText(num.format(price));
                Order order = listData.get(position);
                order.setQuantity(String.valueOf(newValue));
                new Database(context).updateCart(order);
                int total = 0;
                List<Order> orders =  new Database(context).getCarts();
                for (Order item : orders)
                    total += (Integer.parseInt(item.getPrice())) * (Integer.parseInt(item.getQuantity()));
                Locale locale = new Locale("en", "US");
                NumberFormat fmt =NumberFormat.getCurrencyInstance(locale);
                context.total_price.setText(fmt.format(total));
            }
        });
        }
    @Override
    public int getItemCount() {
        return listData.size();
    }
}