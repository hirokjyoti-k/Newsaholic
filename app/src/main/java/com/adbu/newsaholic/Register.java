package com.adbu.newsaholic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.adbu.newsaholic.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private EditText fullname, email, password;
    private FirebaseAuth auth;
    private User user;
    private ProgressDialog progressDialog;
    private String Password;
    private Spinner country;
    private String countryCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullname = (EditText) findViewById(R.id.fullname);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        country = (Spinner) findViewById(R.id.country);

        auth = FirebaseAuth.getInstance();
        user = new User();
        getCountry();
    }

    public void register(View view) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating account...");
        progressDialog.setCancelable(false);

        user.setName(fullname.getText().toString().trim());
        user.setEmail(email.getText().toString().trim());
        user.setCountry(countryCode);
        Password = password.getText().toString().trim();

        if(validatedata()){
            progressDialog.show();
            auth.createUserWithEmailAndPassword(user.getEmail(), Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        createAccount();
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(Register.this, ""+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else {
            return;
        }
    }

    private void getCountry() {

        String[] code = {"in","au","ca","fr","il","it","jp","ru","sa","sg","tr","us","ae"};
        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                countryCode = code[position];
//                Toast.makeText(Register.this, ""+countryCode, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private boolean validatedata() {

        if(user.getName().isEmpty()){
            fullname.setError("Name can't be empty");
            return false;
        }
        if(user.getEmail().isEmpty()){
            email.setError("Email can't be empty");
            return false;
        }

        if(Password.isEmpty()){
            password.setError("Password can't be empty");
            return false;
        }

        return true;
    }

    private void createAccount() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users/"+auth.getCurrentUser().getUid());
        reference.setValue(user);
        progressDialog.dismiss();
        startActivity(new Intent(Register.this, InterestArea.class));
        finish();
    }


    public void gotoLogin(View view) {
        startActivity(new Intent(Register.this, Login.class));
        finish();
    }
}