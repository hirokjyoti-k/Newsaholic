package com.adbu.newsaholic.drivers;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;

import com.adbu.newsaholic.ViewArticle;
import com.adbu.newsaholic.firebase.Firebase;
import com.adbu.newsaholic.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseData extends AppCompatActivity {

    private DatabaseReference userRef;
    private FirebaseAuth auth;
    private User user;
    private ProgressDialog progressDialog;

    public FirebaseData(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        auth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("users/"+auth.getCurrentUser().getUid());
    }

    public void readUser(Firebase firebase){

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                progressDialog.dismiss();
                firebase.user(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });
    }
}
