package com.example.project.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.Adapter.FoodAdapter;
import com.example.project.common.Permission;
import com.example.project.common.common;
import com.example.project.model.AllFood;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class foodlist extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference foodList;
    FirebaseRecyclerAdapter<AllFood, FoodAdapter> adapter;
    String categoryId = "";
    FloatingActionButton fab, add_food;
AllFood allFood;
    Uri saveuri;
    private final int PICK_IMAGE_REQUEST = 71;
    EditText add_name,add_des, add_price, add_discount;
    Button select_food_img, upload;
    FirebaseStorage storage;
    StorageReference storageReference;
public String getUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodlist);

        //init firebase
        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Food");

        recyclerView = findViewById(R.id.recycler_food);
        recyclerView.hasFixedSize();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();



        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("CategoryID");
        categoryId =  bundle.getString("ID");
        loadListFood(categoryId);
        fab= findViewById(R.id.fab);
        add_food=findViewById(R.id.add_food);
        add_food.setOnClickListener(v -> AddFoodDialog());

        if(Permission.permission.equals("ad")){
            fab.setVisibility(View.INVISIBLE);
            add_food.setVisibility(View.VISIBLE);
        }
        else if(Permission.permission.equals("kh")){
            fab.setVisibility(View.VISIBLE);
            add_food.setVisibility(View.INVISIBLE);
        }

        fab.setOnClickListener(v -> {
            Intent cac = new Intent(foodlist.this, Cart.class);
            startActivity(cac);
        });


    }

    private void AddFoodDialog() {
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(foodlist.this);
        alertdialog.setTitle("Add new Food");
        alertdialog.setMessage("Fill full information");
        LayoutInflater inflater = this.getLayoutInflater();
        View add_food =  inflater.inflate(R.layout.add_new_food,null);

        add_name =add_food.findViewById(R.id.add_name);
        add_des =add_food.findViewById(R.id.add_des);
        add_price =add_food.findViewById(R.id.add_price);
        add_discount =add_food.findViewById(R.id.add_discount);
        select_food_img =add_food.findViewById(R.id.choose_food_img);
        upload =add_food.findViewById(R.id.upload_food);
        select_food_img.setOnClickListener((View v) -> {
            chooseImg();
        });
        upload.setOnClickListener(v -> uploadImg());

        alertdialog.setView(add_food);
        alertdialog.setIcon(R.drawable.ic_baseline_add_24);

        alertdialog.setPositiveButton("Okela", (dialog, which) -> {
            dialog.dismiss();
            if(allFood!=null){
                foodList.push().setValue(allFood);
                Toast.makeText(foodlist.this,allFood.getName()+" was added",Toast.LENGTH_LONG).show();
            }

        });
        alertdialog.setNegativeButton("Oh no no", (dialog, which) -> dialog.dismiss());
        alertdialog.show();
    }
    private void uploadImg() {
        if(saveuri != null){
            ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading...");
            mDialog.show();

            String img_name = UUID.randomUUID().toString();
            StorageReference imgFolder = storageReference.child("image/"+img_name);
            imgFolder.putFile(saveuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                            Toast.makeText(foodlist.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            imgFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //set value for new category if img upload and we can download link
                                    allFood = new AllFood(add_des.getText().toString(), add_discount.getText().toString(), uri.toString(), categoryId, add_name.getText().toString(), add_price.getText().toString());
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mDialog.dismiss();
                    Toast.makeText(foodlist.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(snapshot -> {
                double progress = (100.0 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                mDialog.setMessage("Uploaded"+progress+"%");
            });
        }
    }
    private  void changeImg(final AllFood item){
        if(saveuri != null){
            ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading...");
            mDialog.show();

            String img_name = UUID.randomUUID().toString();
            StorageReference imgFolder = storageReference.child("image/"+img_name);
            imgFolder.putFile(saveuri)
                    .addOnSuccessListener(taskSnapshot -> {
                        mDialog.dismiss();
                        Toast.makeText(foodlist.this,"Uploaded",Toast.LENGTH_SHORT).show();

                        imgFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                //set value for new category if img upload and we can download link
                                allFood = new AllFood(add_des.getText().toString(), add_discount.getText().toString(), uri.toString(), categoryId, add_name.getText().toString(), add_price.getText().toString());
                                getUri=uri.toString();
                                Log.i("uriimg",uri.toString());
                            }
                        });
                    }).addOnFailureListener(e -> {
                mDialog.dismiss();
                Toast.makeText(foodlist.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }).addOnProgressListener(snapshot -> {
                double progress = (100.0 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                mDialog.setMessage("Uploaded"+progress+"%");
            });
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data != null && data.getData()!=null)
        {
            saveuri = data.getData();
            select_food_img.setText("Selected !!!");
        }
    }
    private void chooseImg() {
        Intent intent  = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select an image"),PICK_IMAGE_REQUEST);
    }

    @Override
    //xử lý sự kiện click trên từng item
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals((common.UPDATE))){
            showUpdateFood(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }
        else if(item.getTitle().equals((common.DELETE))){
            deleteFood(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }
        return super.onContextItemSelected(item);
    }

    private void deleteFood(String key, AllFood item) {
        foodList.child(key).removeValue();
    }

    private void showUpdateFood(String key,AllFood item) {
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(foodlist.this);
        alertdialog.setTitle("Edit Food");
        alertdialog.setMessage("Fill full information");
        LayoutInflater inflater = this.getLayoutInflater();
        View add_food =  inflater.inflate(R.layout.add_new_food,null);

        add_name =add_food.findViewById(R.id.add_name);
        add_des =add_food.findViewById(R.id.add_des);
        add_price =add_food.findViewById(R.id.add_price);
        add_discount =add_food.findViewById(R.id.add_discount);

        add_name.setText(item.getName());
        add_des.setText(item.getDescription());
        add_price.setText(item.getPrice());
        add_discount.setText(item.getDiscount());


        select_food_img =add_food.findViewById(R.id.choose_food_img);
        upload =add_food.findViewById(R.id.upload_food);
        select_food_img.setOnClickListener(v -> chooseImg());
        upload.setOnClickListener(v -> changeImg(item));

        alertdialog.setView(add_food);
        alertdialog.setIcon(R.drawable.ic_baseline_add_24);

        alertdialog.setPositiveButton("Okela", (dialog, which) -> {
            dialog.dismiss();

                item.setName(add_name.getText().toString());
                item.setDescription(add_des.getText().toString());
                item.setPrice(add_price.getText().toString());
                item.setDiscount(add_discount.getText().toString());
                item.setImage(getUri);

                foodList.child(key).setValue(item);
                Toast.makeText(foodlist.this,allFood.getName()+" was added",Toast.LENGTH_LONG).show();


        });
        alertdialog.setNegativeButton("Oh no no", (dialog, which) -> dialog.dismiss());
        alertdialog.show();

    }

    private void loadListFood(String categoryId) {
        FirebaseRecyclerOptions<AllFood> options =
                new FirebaseRecyclerOptions.Builder<AllFood>().setQuery(foodList.orderByChild("menuid").equalTo(categoryId), AllFood.class).build();

        adapter = new FirebaseRecyclerAdapter<AllFood, FoodAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FoodAdapter foodAdapter, int i, @NonNull final AllFood food) {
                foodAdapter.food_name.setText(food.getName());
                Picasso.get().load(food.getImage()).into(foodAdapter.food_image);


                    foodAdapter.setItemClickListener((view, position, isLongClick) -> {
                        if(Permission.permission.equals("kh")){
                        Intent intent = new Intent(foodlist.this, FoodDetails.class);
                        intent.putExtra("FoodId",adapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                    });

            }
            @NonNull
            @Override
            public FoodAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_food, parent, false);
                return new FoodAdapter(view);
            }
        };

        //set Adapter
        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }
}
