package com.example.project.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.example.project.common.Permission;
import com.example.project.common.common;
import com.example.project.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Main_SignIn extends AppCompatActivity {
    EditText edt_phone, edt_pwd;
    Button btn_signin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singin_main);
        edt_phone=(EditText)findViewById(R.id.edt_phone2);
        edt_pwd=(EditText)findViewById(R.id.edt_pwd2);
        btn_signin=(Button)findViewById(R.id.btn_signin2);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");
        btn_signin.setOnClickListener(v -> {
            final ProgressDialog mDialog = new ProgressDialog(Main_SignIn.this);
            mDialog.setMessage("Please waiting :))");
            mDialog.show();
            table_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String phone =  edt_phone.getText().toString();
                    String password = edt_pwd.getText().toString();
                    if(snapshot.child(phone).exists()){
                        mDialog.dismiss();
                        User user = snapshot.child(phone).getValue(User.class);
                        user.setPhone(phone);
                        if(user.getPassword().equals(password)){
                            if (snapshot.child(phone).child("admin").getValue(String.class).equals("false")) {
                                preferences.setDataLogin(Main_SignIn.this, true);
                                preferences.setDataAs(Main_SignIn.this, "false");
                                common.currentUser=user;
                                Permission.permission = "kh";
                                Intent intent = new Intent(Main_SignIn.this, home.class);

                                startActivity(intent);

                                finish();
                            } else if (snapshot.child(phone).child("admin").getValue(String.class).equals("true")){
                                preferences.setDataLogin(Main_SignIn.this, true);
                                common.currentUser=user;
                                Permission.permission = "ad";
                                Intent intent = new Intent(Main_SignIn.this, home.class);

                                preferences.setDataAs(Main_SignIn.this, "true");
                                startActivity(intent);
                                finish();
                            }
                        }
                        else {
                            Toast.makeText(Main_SignIn.this,"password is not correct", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(Main_SignIn.this,"This user is not exist",Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        });
    }
}

