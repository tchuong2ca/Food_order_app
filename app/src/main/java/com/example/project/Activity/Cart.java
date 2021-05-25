package com.example.project.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Adapter.Cart_FoodActionAdapter;
import com.example.project.Database.Database;
import com.example.project.R;
import com.example.project.common.common;
import com.example.project.model.Address;
import com.example.project.model.Order;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.os.Build.VERSION_CODES.N;

public class Cart extends AppCompatActivity {
RecyclerView recyclerView;
RecyclerView.LayoutManager layoutManager;
FirebaseDatabase database;
DatabaseReference Orders;
public TextView total_price;
Button btn;
List<Order> cart = new ArrayList<>();
Cart_FoodActionAdapter cartFoodActionAdapter;
    @RequiresApi(api = N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        database = FirebaseDatabase.getInstance();
        Orders = database.getReference("Orders");
        recyclerView = (RecyclerView)findViewById(R.id.recycler_cart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        total_price = (TextView)findViewById(R.id.total);
        loadListFood();
        btn = findViewById(R.id.btnPlaceOrder);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cart.size()>0)
               dialog();
                else
                    Toast.makeText(Cart.this,"Your cart is empty, please add some thing to continue",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void dialog() {

        AlertDialog.Builder alert = new AlertDialog.Builder(Cart.this);
        alert.setMessage("Enter your address:");
        LayoutInflater inflater =  this.getLayoutInflater();
        View note = inflater.inflate(R.layout.od_note,null);
        EditText edt_address  = (EditText) note.findViewById(R.id.address);
        MaterialEditText edt_note  = (MaterialEditText)note.findViewById(R.id.note);
        alert.setView(note);
        alert.setIcon(R.drawable.ic_baseline_shopping_cart_24);
        alert.setPositiveButton("Order", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Address address = new Address(
                        common.currentUser.getName(),
                        common.currentUser.getPhone(),
                        edt_address.getText().toString(),
                        total_price.getText().toString(),
                        "0",
                        edt_note.getText().toString(),
                        cart
                );
                //send data to firebase
                Orders.child(String.valueOf(System.currentTimeMillis())).setValue(address);
                new Database(getBaseContext()).cleanCart();
                Toast.makeText(Cart.this, "Perfect :))",Toast.LENGTH_SHORT).show();
                finish();

            }
        });
        alert.setNegativeButton("Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    @RequiresApi(api = N)
    private void loadListFood() {

        cart = new Database(this).getCarts();
        cartFoodActionAdapter = new Cart_FoodActionAdapter(cart, this);
        cartFoodActionAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(cartFoodActionAdapter);

        //calculating total price
        int total = 0;
        for (Order order : cart)

            total += (Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getQuantity()));
        Locale locale = new Locale("en", "US");
        android.icu.text.NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        total_price.setText(fmt.format(total));
    }

    @RequiresApi(api = N)
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        item.getTitle().equals("DELETE");
        deleteCart(item.getOrder());
        return true;

    }
    @RequiresApi(api = N)
    public void deleteCart(int a) {
        cart.remove(a);
        new Database(this).cleanCart();
        for (Order item:cart)
            new Database(this).addToCart(item);
        loadListFood();
    }
}