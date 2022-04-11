package com.adbu.newsaholic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.adbu.newsaholic.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private String Email, Password;
    private EditText email, password;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser()!=null){
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        }

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

    }

    public void login(View view) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Login to your account...");
        progressDialog.setCancelable(false);

        Email = email.getText().toString().trim();
        Password = password.getText().toString().trim();

        if(validateData()){
            progressDialog.show();
            auth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        startActivity(new Intent(Login.this, MainActivity.class));
                        finish();
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(Login.this, ""+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else {
            return;
        }
    }

    private boolean validateData() {
        if(Email.isEmpty()){
            email.setError("Email can't be empty");
            return false;
        }

        if(Password.isEmpty()){
            password.setError("Password can't be empty");
            return false;
        }
        return true;
    }

    public void forgotpassword(View view) {

        Email = email.getText().toString().trim();
        if(Email.isEmpty()){
            email.setError("Email can't be empty");
            return;
        }

        new AlertDialog.Builder(Login.this)
                .setMessage("Do you want to reset ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        auth.sendPasswordResetEmail(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(Login.this, "Reset link sent successfully", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(Login.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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

    public void gotoRegister(View view) {
        startActivity(new Intent(Login.this, Register.class));
        finish();
    }
}