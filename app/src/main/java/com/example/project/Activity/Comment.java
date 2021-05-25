package com.example.project.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.project.R;
import com.example.project.Adapter.CommentAdapter;
import com.example.project.common.common;
import com.example.project.model.Rating;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
public class Comment extends AppCompatActivity {
    String foodId ="";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference ratingTbl;
    SwipeRefreshLayout refreshLayout;
    FirebaseRecyclerAdapter<Rating, CommentAdapter> adapter;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (adapter!= null){
            adapter.stopListening();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/cf.otf")
        .setFontAttrId(R.attr.fontPath)
        .build());
        setContentView(R.layout.activity_comment);

        //Firebase
        database = FirebaseDatabase.getInstance();
        ratingTbl = database.getReference("Rating");
        layoutManager =  new LinearLayoutManager(this);
        recyclerView= (RecyclerView)findViewById(R.id.recycle_comment);
        recyclerView.setLayoutManager(layoutManager);

        //SwipeLayout
        refreshLayout =(SwipeRefreshLayout)findViewById(R.id.swipe_layout);
        refreshLayout.setOnRefreshListener(() -> {
            if(getIntent() != null){

                foodId = getIntent().getStringExtra(common.INTENT_FOOD_ID);
                if(!foodId.isEmpty()){

                    Query query =  ratingTbl.orderByChild("foodId").equalTo(foodId);
                    FirebaseRecyclerOptions<Rating> option = new FirebaseRecyclerOptions.Builder<Rating>()
                    .setQuery(query,Rating.class).build();
                    adapter = new FirebaseRecyclerAdapter<Rating, CommentAdapter>(option) {
                        @Override
                        protected void onBindViewHolder(@NonNull CommentAdapter holder, int position, @NonNull Rating model) {
                            holder.ratingBar.setRating(Float.parseFloat(model.getValue()));
                            holder.Comment.setText(model.getComment());
                            holder.Phone.setText(model.getPhone());
                        }
                        @NonNull
                        @Override
                        public CommentAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.comment_layout,parent,false);
                            return new CommentAdapter(view);
                        }
                    };
                    loadComment(foodId);
                }
            }
        });
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
                if(getIntent() != null){
                    foodId = getIntent().getStringExtra(common.INTENT_FOOD_ID);
                    if(!foodId.isEmpty()){
                        Query query =  ratingTbl.orderByChild("foodId").equalTo(foodId);
                        FirebaseRecyclerOptions<Rating> option = new FirebaseRecyclerOptions.Builder<Rating>()
                                .setQuery(query,Rating.class).build();
                        adapter = new FirebaseRecyclerAdapter<Rating, CommentAdapter>(option) {
                            @Override
                            protected void onBindViewHolder(@NonNull CommentAdapter holder, int position, @NonNull Rating model) {
                                holder.ratingBar.setRating(Float.parseFloat(model.getValue()));
                                holder.Comment.setText(model.getComment());
                                holder.Phone.setText(model.getPhone());
                            }
                            @NonNull
                            @Override
                            public CommentAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                View view = LayoutInflater.from(parent.getContext())
                                        .inflate(R.layout.comment_layout,parent,false);
                                return new CommentAdapter(view);
                            }
                        };
                        loadComment(foodId);
                    }
                }
            }
        });
    }
    private void loadComment(String foodId) {
        adapter.startListening();
        recyclerView.setAdapter(adapter);
        refreshLayout.setRefreshing(false);
    }
}