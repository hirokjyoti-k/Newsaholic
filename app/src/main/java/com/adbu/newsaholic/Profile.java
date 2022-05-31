package com.adbu.newsaholic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.adbu.newsaholic.drivers.FirebaseData;
import com.adbu.newsaholic.firebase.Firebase;
import com.adbu.newsaholic.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Profile extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseData firebaseData;
    private EditText username, useremail;
    private TextView usercountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        auth = FirebaseAuth.getInstance();
        firebaseData = new FirebaseData(this);

        username = (EditText) findViewById(R.id.username);
        useremail = (EditText) findViewById(R.id.useremail);
        usercountry = (TextView) findViewById(R.id.usercountry);

        firebaseData.getUser(new Firebase() {
            @Override
            public void user(User user) {
                username.setText(user.getName());
                useremail.setText(user.getEmail());
                usercountry.setText(user.getCountry());
            }
        });

    }

    public void save(View view) {
    }

    public void resetpassword(View view) {
        new AlertDialog.Builder(Profile.this)
                .setMessage("Do you want to reset ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        auth.sendPasswordResetEmail(useremail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(Profile.this, "Reset link sent successfully", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(Profile.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }
}