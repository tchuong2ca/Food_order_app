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
import com.example.project.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Main_SignUp extends AppCompatActivity {
    EditText phone,mail,name, pwd;
    Button btn_signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singup_main);
        phone=(EditText)findViewById(R.id.edt_phone);
        pwd=(EditText)findViewById(R.id.edt_pwd);
        name=(EditText)findViewById(R.id.edt_name);
        mail=(EditText)findViewById(R.id.edt_mail);
        btn_signup=(Button)findViewById(R.id.btn_signup2);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");
        btn_signup.setOnClickListener(v -> {


            String number = phone.getText().toString();
            if (number.isEmpty() || number.length() < 10) {
                phone.setError("Phone number is required");
                phone.requestFocus();
                return;
            }
            final ProgressDialog mDialog = new ProgressDialog(Main_SignUp.this);
            mDialog.show();

            table_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // check if user already exits  through phone number

                    if (dataSnapshot.child(phone.getText().toString()).exists()) {
                        Toast.makeText(Main_SignUp.this,"This account already exists", Toast.LENGTH_SHORT).show();
                        mDialog.dismiss();
                    } else {
                        mDialog.dismiss();
                        // add the user with phone number as key value with name and password as child.
                        // use user class to get the name and password and save it to user object

                        User user = new User("false", mail.getText().toString(),name.getText().toString(),pwd.getText().toString());

                        // below this table user have phone number with value name and password through obj user
                        table_user.child(phone.getText().toString()).setValue(user);
                        Toast.makeText(Main_SignUp.this, "Sign Up Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Main_SignUp.this,Main_SignIn.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        });
    }
}
