package com.example.project.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.Adapter.MainSliderAdapter;
import com.example.project.Adapter.MenuViewHolder;
import com.example.project.common.Permission;
import com.example.project.common.common;
import com.example.project.model.PicassoImageLoadingService;
import com.example.project.model.category;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.UUID;

import ss.com.bannerslider.Slider;

public class home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseDatabase database;
    DatabaseReference Category;
    TextView txt_name;
    public GridView gridView;
    public RecyclerView.LayoutManager layoutManager;
    public ArrayList<category> myarr = new ArrayList<>();
    public MenuViewHolder menuViewHolder;
    public ArrayList<String> keys = new ArrayList<>();
    category cate;
    Uri saveuri;
    /* REQUEST_CODE là một giá trị int dùng để định danh mỗi request. Khi nhận được kết quả,
    hàm callback sẽ trả về cùng REQUEST_CODE này để ta có thể xác định và xử lý kết quả. */
    private final int PICK_IMAGE_REQUEST = 71;
    FloatingActionButton fab, add_food;

    private Slider slider;
    FirebaseStorage storage;
    StorageReference storageReference;

    EditText new_name;
    Button select, upload;


    @Override

    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);

        database = FirebaseDatabase.getInstance();
        Category = database.getReference("Category");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        add_food = findViewById(R.id.add_food);
        fab= findViewById(R.id.fab);


        if (Permission.permission.equals("kh"))
        {
            Toast.makeText(home.this,"hello "+ common.currentUser.getName(),Toast.LENGTH_LONG).show();
            fab.setVisibility(View.VISIBLE);
            add_food.setVisibility(View.INVISIBLE);
            DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
            NavigationView navigationView = findViewById(R.id.nav_view);
            ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
            drawerLayout.setDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();
            navigationView.setNavigationItemSelectedListener(this);
            View headerView = navigationView.getHeaderView(0);
            txt_name = headerView.findViewById(R.id.txt_name);
            txt_name.setText(common.currentUser.getName());
        }
        else if (Permission.permission.equals("ad"))
        {
            fab.setVisibility(View.INVISIBLE);
            add_food.setVisibility(View.VISIBLE);
            Toast.makeText(home.this,"WELCOME, MY LORD !!!",Toast.LENGTH_LONG).show();
        }
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(home.this, Cart.class);
            startActivity(intent);
        });
        add_food.setOnClickListener(v -> showDialog());
        gridView = findViewById(R.id.gridview);


        Slider.init(new PicassoImageLoadingService(this));
        slider = findViewById(R.id.banner_slider1);
        slider.setAdapter(new MainSliderAdapter());
        slider.setLoopSlides(true); //cho phép lặp lại slide
        slider.setInterval(4000); //set thời gian
        //
        Category.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                category cat = snapshot.getValue(category.class);
                keys.add(snapshot.getKey());
                myarr.add(cat);
                menuViewHolder.notifyDataSetChanged();}
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
        //hiển thị menu
        menuViewHolder = new MenuViewHolder(home.this, R.layout.menu_item, myarr);
        gridView.setAdapter(menuViewHolder);
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            // get category id and send it to the new activity.
            Intent foodIntent = new Intent(home.this, foodlist.class);
            // category id is key, get the key of this item
            Bundle bundle = new Bundle();
            bundle.putSerializable("ID", keys.get(position));
            bundle.putSerializable("Food", myarr.get(position));
            foodIntent.putExtra("CategoryID", bundle);
            startActivity(foodIntent);
        });
    }
    private void showDialog() {
        //tạo dialog
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(home.this);
        alertdialog.setTitle("Add new Category");
        alertdialog.setMessage("Fill full information");
        LayoutInflater inflater = this.getLayoutInflater();
        //đặt layout hiển thị cho dialog
        View add_menu =  inflater.inflate(R.layout.add_new_menu,null);

        new_name =add_menu.findViewById(R.id.new_name);
        select =add_menu.findViewById(R.id.select);
        upload =add_menu.findViewById(R.id.upload);
        select.setOnClickListener(v -> chooseImg());
        upload.setOnClickListener(v -> uploadImg());

        alertdialog.setView(add_menu);
        alertdialog.setIcon(R.drawable.ic_baseline_add_24);

        alertdialog.setPositiveButton("Okela", (dialog, which) -> {
            dialog.dismiss();
            if(cate!=null){
                Category.push().setValue(cate);
                Toast.makeText(home.this,cate.getName()+" was added",Toast.LENGTH_LONG).show();
            }
        });
        alertdialog.setNegativeButton("Oh no no", (dialog, which) -> dialog.dismiss());
        alertdialog.show();
    }

    private void uploadImg() {
        if(saveuri != null){
            //progress dialog
            ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading...");
            mDialog.show();
            //sinh tên ngẫu nhiên cho ảnh
            String img_name = UUID.randomUUID().toString();
            StorageReference imgFolder = storageReference.child("image/"+img_name);
            imgFolder.putFile(saveuri)
                    .addOnSuccessListener(taskSnapshot -> {
                        mDialog.dismiss();
                        Toast.makeText(home.this,"Uploaded",Toast.LENGTH_SHORT).show();
                        imgFolder.getDownloadUrl().addOnSuccessListener(uri -> {
                            //set value for new category if img upload and we can download link
                            cate = new category(uri.toString(),new_name.getText().toString());
                        });
                    }).addOnFailureListener(e -> {
                mDialog.dismiss();
                Toast.makeText(home.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }).addOnProgressListener(snapshot -> {
                double progress = (100.0 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                mDialog.setMessage("Uploaded"+progress+"%");
            });
        }
    }

    @Override
    // Khi kết quả được trả về từ Activity khác, hàm onActivityResult sẽ được gọi.
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data != null && data.getData()!=null)
        {
            saveuri = data.getData();
            select.setText("Selected !!!");
        }
    }

    private void chooseImg() {
        Intent intent  = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select an image"),PICK_IMAGE_REQUEST);
    }

    public interface ItemClickListener{
        void onClick(View view, int position, boolean isLongClick);
    }
    @Override
    public boolean onNavigationItemSelected( MenuItem item) {
        int id =  item.getItemId();
        if(id ==R.id.nav_menu){
            Intent intent = getIntent();
            startActivity(intent);
        }
        else if (id ==R.id.nav_cart){
            Intent cart = new Intent(home.this, Cart.class);
            startActivity(cart);
        }
        else if (id ==R.id.nav_order){
            Intent orderstt = new Intent(home.this, OrderStt.class);
            startActivity(orderstt);
        }
        else if (id ==R.id.nav_signout){
            Intent logout = new Intent(home.this, MainActivity.class);
            startActivity(logout);
        }
        return false;
    }
//refresh after delete menu
    public void refresh(){
        Intent intent = getIntent();
        startActivity(intent);
    }

}
