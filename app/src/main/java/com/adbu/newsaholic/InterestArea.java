package com.adbu.newsaholic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.adbu.newsaholic.model.User;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InterestArea extends AppCompatActivity {

    private MaterialCardView general, entertainment, business, health, science, sports, technology;
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;
    private String category = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_area);

        auth = FirebaseAuth.getInstance();

        general = (MaterialCardView) findViewById(R.id.general);
        entertainment = (MaterialCardView) findViewById(R.id.entertainment);
        business = (MaterialCardView) findViewById(R.id.business);
        health = (MaterialCardView) findViewById(R.id.health);
        science = (MaterialCardView) findViewById(R.id.science);
        sports = (MaterialCardView) findViewById(R.id.sports);
        technology = (MaterialCardView) findViewById(R.id.technology);

        general.setChecked(true);
        selectCategory();

    }

    private void selectCategory() {

        general.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!general.isChecked()){
                    general.setChecked(true);
                }else {
                    general.setChecked(false);
                }
            }
        });

        entertainment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!entertainment.isChecked()){
                    entertainment.setChecked(true);
                }else {
                    entertainment.setChecked(false);
                }
            }
        });

        business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!business.isChecked()){
                    business.setChecked(true);
                }else {
                    business.setChecked(false);
                }
            }
        });

        health.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!health.isChecked()){
                    health.setChecked(true);
                }else {
                    health.setChecked(false);
                }
            }
        });

        science.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!science.isChecked()){
                    science.setChecked(true);
                }else {
                    science.setChecked(false);
                }
            }
        });

        sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!sports.isChecked()){
                    sports.setChecked(true);
                }else {
                    sports.setChecked(false);
                }
            }
        });

        technology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!technology.isChecked()){
                    technology.setChecked(true);
                }else {
                    technology.setChecked(false);
                }
            }
        });
    }

    public void saveTopics(View view) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving preferences...");
        progressDialog.setCancelable(false);

        getselectedCategory();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users/"+auth.getCurrentUser().getUid()+"/category");
        reference.setValue(category);
        progressDialog.dismiss();
        startActivity(new Intent(InterestArea.this, MainActivity.class));
        finish();

    }

    private void getselectedCategory() {
        if(general.isChecked())
            category += "general,";

        if(entertainment.isChecked())
            category += "entertainment,";

        if(business.isChecked())
            category += "business,";

        if(health.isChecked())
            category += "health,";

        if(science.isChecked())
            category += "science,";

        if(sports.isChecked())
            category += "sports,";

        if(technology.isChecked())
            category += "technology,";

    }
}