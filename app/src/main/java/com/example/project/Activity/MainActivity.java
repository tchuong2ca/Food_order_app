package com.example.project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.project.R;
import com.example.project.model.OnSwipeTouchListener;

public class MainActivity extends AppCompatActivity {
    Button btnSI, btnSU;
    TextView txtSlogan;
    ImageView imageView;
    TextView textView;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        btnSI=(Button)findViewById(R.id.btn_signin);
        btnSU=(Button)findViewById(R.id.btn_signup);
        txtSlogan=(TextView)findViewById(R.id.txtSolgan);

        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        btnSI.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Main_SignIn.class);
            startActivity(intent);
        });
        btnSU.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Main_SignUp.class);
            startActivity(intent);
        });

        imageView.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {

            public void onSwipeTop() {
            }
            public void onSwipeRight() {
                if (count == 0) {
                    imageView.setImageResource(R.drawable.good_night_img);
                    textView.setText("Night");
                    count = 1;
                } else {
                    imageView.setImageResource(R.drawable.good_morning_img);
                    textView.setText("Morning");
                    count = 0;
                }
            }
            public void onSwipeLeft() {
                if (count == 0) {
                    imageView.setImageResource(R.drawable.good_night_img);
                    textView.setText("Night");
                    count = 1;
                } else {
                    imageView.setImageResource(R.drawable.good_morning_img);
                    textView.setText("Morning");
                    count = 0;
                }
            }

            public void onSwipeBottom() {
            }

        });
    }
}