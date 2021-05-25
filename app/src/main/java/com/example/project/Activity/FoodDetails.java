package com.example.project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.project.Database.Database;
import com.example.project.R;
import com.example.project.common.common;
import com.example.project.model.AllFood;
import com.example.project.model.Order;
import com.example.project.model.Rating;
import com.example.project.model.User;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.util.Arrays;


public class FoodDetails extends AppCompatActivity implements RatingDialogListener {
User user;
    TextView food_name, food_price, food_description;
    ImageView food_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart, btn_rating;
    ElegantNumberButton numberButton;
    String foodId = "";
    AllFood food;

    FirebaseDatabase database;
    DatabaseReference foods;
    Button btn_comment;
    RatingBar ratingBar ;
    DatabaseReference ratingTbl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fooddetail);
        btn_comment = (Button)findViewById(R.id.btn_comment) ;
        btn_comment.setOnClickListener(v -> {
            Intent intent = new Intent(FoodDetails.this, Comment.class);
            intent.putExtra(common.INTENT_FOOD_ID,foodId);
            startActivity(intent);
        });
        // Init Firebase
        database = FirebaseDatabase.getInstance();
        foods = database.getReference("Food");
        ratingTbl = database.getReference("Rating");

        //init view
        numberButton = findViewById(R.id.number_button);
        btnCart = findViewById(R.id.btnCart);
        btnCart.setOnClickListener(v -> {
            if(numberButton.getNumber().equals(String.valueOf(0))){
                Toast.makeText(FoodDetails.this, "Please take some food into your cart", Toast.LENGTH_SHORT).show();
            }else{
                new Database(getBaseContext()).addToCart(new Order(
                        foodId,
                        food.getName(),
                        numberButton.getNumber(),
                        food.getPrice(),
                        food.getDiscount(),
                        food.getImage()

                ));
                Toast.makeText(FoodDetails.this, "Added to Cart", Toast.LENGTH_SHORT).show();
            }
        });
        btn_rating = (FloatingActionButton) findViewById(R.id.btn_rating);
        btn_rating.setOnClickListener(v -> showRatingDialog());


        food_name = findViewById(R.id.name_food);
        food_image = findViewById(R.id.img_food);
        food_description = findViewById(R.id.food_description);
        food_price = findViewById(R.id.food_price);

        collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        // get food id from intent
        if (getIntent() != null)
            foodId = getIntent().getStringExtra("FoodId");
        if (!foodId.isEmpty() && foodId != null)
            getDetailFood(foodId);
        getRatingFood(foodId);
    }
    private void getRatingFood(String foodId) {
        ratingBar = (RatingBar)findViewById(R.id.ratingbar);
        Query foodRating = ratingTbl.orderByChild("foodId").equalTo(foodId);
        foodRating.addValueEventListener(new ValueEventListener() {
            int count= 0,sum=0; @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot:snapshot.getChildren()){
                    Rating item = postSnapshot.getValue(Rating.class);
                    sum+=Integer.parseInt(item.getValue());
                    count++;
                }
                if(count!=0){
                    float avg = sum/count;
                    ratingBar.setRating(avg);
                }}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void showRatingDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Awful","Bad","Acceptable","Great","Excellent"))
                .setDefaultRating(1)
                .setTitle("Write down your feeling")
                .setDescription("Choose the appropriate number of stars and give your feedback")
                .setTitleTextColor(R.color.orange)
                .setDescriptionTextColor(R.color.black)
                .setHint("Write down your comment here...")
                .setHintTextColor(R.color.white)
                .setCommentTextColor(R.color.white)
                .setCommentBackgroundColor(R.color.purple_700)
                .setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(FoodDetails.this).show();
    }

    private void getDetailFood(String foodId) {
        foods.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                food = dataSnapshot.getValue(AllFood.class);

                // setting the image from firebase into appbar;
                Picasso.get().load(food.getImage()).into(food_image);
                //set title in appbar
                collapsingToolbarLayout.setTitle(food.getName());
                food_price.setText(food.getPrice());
                food_description.setText(food.getDescription());
                food_name.setText(food.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onNegativeButtonClicked() {
    }
    @Override
    public void onPositiveButtonClicked(int value, @NonNull String comment) {
        Rating rating =  new Rating(common.currentUser.getPhone(),
                foodId,
                String.valueOf(value),
                comment);

        ratingTbl.push()
                .setValue(rating)
                .addOnCompleteListener(task -> Toast.makeText(FoodDetails.this,"Thanks for your feedback",Toast.LENGTH_SHORT).show());
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }
}