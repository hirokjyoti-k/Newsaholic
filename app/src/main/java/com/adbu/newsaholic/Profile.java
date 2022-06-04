package com.adbu.newsaholic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.adbu.newsaholic.drivers.FirebaseData;
import com.adbu.newsaholic.firebase.Firebase;
import com.adbu.newsaholic.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

public class Profile extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseData firebaseData;
    private EditText username, useremail;
    private TextView usercountry;
    private String countrycode;
    private CardView showCountry, selectCountry;
    private LinearLayout editBtnsView;
    private MaterialButton editBtn;
    private Spinner country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        auth = FirebaseAuth.getInstance();
        firebaseData = new FirebaseData(this);

        username = (EditText) findViewById(R.id.username);
        useremail = (EditText) findViewById(R.id.useremail);
        usercountry = (TextView) findViewById(R.id.usercountry);

        showCountry = (CardView) findViewById(R.id.showCountryCardview);
        selectCountry = (CardView) findViewById(R.id.selectCountryCardview);

        editBtn = (MaterialButton) findViewById(R.id.editButton);
        editBtnsView = (LinearLayout) findViewById(R.id.editButtonsView);

        country = (Spinner) findViewById(R.id.country);

        firebaseData.getUser(new Firebase() {
            @Override
            public void user(User user) {
                username.setText(user.getName());
                useremail.setText(user.getEmail());
                countrycode = user.getCountry();

                String[] code = {"in","au","ca","fr","il","it","jp","ru","sa","sg","tr","us","ae"};
                String[] country = {"India","Australia","Canada","France","Israel","Italy","Japan","Russia","Saudi",
                        "Singapore","Turkey","United State","UAE"};
                usercountry.setText(country[Arrays.binarySearch(code, countrycode)]);

            }
        });

    }

    public void editProfile(View view) {

        username.setEnabled(true);

        editBtnsView.setVisibility(View.VISIBLE);
        editBtn.setVisibility(View.GONE);

        selectCountry.setVisibility(View.VISIBLE);
        showCountry.setVisibility(View.GONE);

        //sync spinner country value with database value
        String[] code = {"in","au","ca","fr","il","it","jp","ru","sa","sg","tr","us","ae"};
        country.setSelection(Arrays.binarySearch(code,countrycode));

    }

    public void cancel(View view) {

        username.setEnabled(false);

        editBtnsView.setVisibility(View.GONE);
        editBtn.setVisibility(View.VISIBLE);

        selectCountry.setVisibility(View.GONE);
        showCountry.setVisibility(View.VISIBLE);
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