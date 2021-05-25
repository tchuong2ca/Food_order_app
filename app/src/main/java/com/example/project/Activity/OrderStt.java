package com.example.project.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.Adapter.OrderAdapter;
import com.example.project.common.common;
import com.example.project.model.Address;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderStt extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Address, OrderAdapter> adapter;
    FirebaseDatabase database;
    DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_stt);

        //firebase init
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Orders");

        recyclerView = findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (getIntent() == null)
            loadOrders(common.currentUser.getPhone());
        else
            loadOrders(getIntent().getStringExtra("userPhone"));

        loadOrders(common.currentUser.getPhone());
    }

    private void loadOrders(String phone) {

        FirebaseRecyclerOptions<Address> options = new FirebaseRecyclerOptions.Builder<Address>().setQuery(ref.orderByChild("phone").equalTo(phone), Address.class).build();


        adapter = new FirebaseRecyclerAdapter<Address, OrderAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderAdapter orderAdapter, int i, @NonNull Address request) {
                orderAdapter.odID.setText(adapter.getRef(i).getKey());
                orderAdapter.odStt.setText(common.convertCodeToStatus(request.getOrder_status()));
                orderAdapter.odAddress.setText(request.getAddress());
                orderAdapter.odPhone.setText(request.getPhone());
                orderAdapter.setItemClickListener((view, position, isLongClick) -> {
                });
            }
            @NonNull
            @Override
            public OrderAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout, parent, false);
                return new OrderAdapter(view);
            }
        };
        //set adapter
        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }


}