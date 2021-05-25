package com.example.project.Adapter;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.project.R;
import com.example.project.Activity.foodlist;
import com.example.project.Activity.home;
import com.example.project.common.Permission;
import com.example.project.model.AllFood;
import com.example.project.model.category;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;
public class MenuViewHolder extends BaseAdapter {

    private final home context;
    private final int layout;
    public final List<category> categoryList;
    DatabaseReference reference;
    public String key, key2;
    public MenuViewHolder(home context, int layout, List<category> categoryList) {
        this.context = context;
        this.layout = layout;
        this.categoryList = categoryList;
    }
    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private static class ViewHolder
    {ImageView imageView;
        TextView textView;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder viewHolder;


        if (convertView == null){
            convertView = layoutInflater.inflate(layout, null);
            viewHolder = new ViewHolder();

           viewHolder.imageView = convertView.findViewById(R.id.menu_image);
            viewHolder.textView = convertView.findViewById(R.id.menu_name);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder=(ViewHolder)convertView.getTag();
        }
        Picasso.get().load(categoryList.get(position).getImage()).into(viewHolder.imageView);

        viewHolder.textView.setText(categoryList.get(position).getName());
        convertView.setId(position);
        //check permission
        if(Permission.permission.equals("ad")){

            reference = FirebaseDatabase.getInstance().getReference();

            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent foodIntent = new Intent(context, foodlist.class);
        // category id is key,we just get the key of this item
        Bundle bundle = new Bundle();
        bundle.putSerializable("ID", context.keys.get(position));
        bundle.putSerializable("Food", context.myarr.get(position));
        foodIntent.putExtra("CategoryID", bundle);
        context.startActivity(foodIntent);
    }
});
            viewHolder.imageView.setOnLongClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle("Warning !!!")
                        .setMessage("Do you really wanna delete this item ?")
                        .setCancelable(true)
                        .setPositiveButton("Kill it", (dialog, which) -> {
                            reference.child("Category").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    category category1 = snapshot.getValue(category.class);
                                    if (category1.getName().equals(categoryList.get(position).getName())){

                                        key = snapshot.getKey();
                                        reference.child("Category").child(key).removeValue();
                                    }
                                }
                                @Override
                                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                            new CountDownTimer(500, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                }
                                @Override
                                public void onFinish() {
                                    reference.child("Food").addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                            AllFood allFood = snapshot.getValue(AllFood.class);
                                            if (allFood.getMenuid().equals(key)){
                                                reference.child("Food").child(snapshot.getKey()).removeValue();
                                                key2 = snapshot.getKey();
                                            }
                                        }

                                        @Override
                                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                        }
                                        @Override
                                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                        }

                                        @Override
                                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }.start();
                            // gọi hàm refresh ở home để làm mới sau khi xóa 1 category
                            context.refresh();
                        })
                        .setNegativeButton("Wrong click", (dialog, which) -> dialog.dismiss());
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            });

        }
        return convertView;
    }
}