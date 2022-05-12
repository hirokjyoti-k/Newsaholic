package com.adbu.newsaholic.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.adbu.newsaholic.R;
import com.adbu.newsaholic.adapter.NewsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kwabenaberko.newsapilib.models.Article;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bookmark extends Fragment {

    private List<Article> articles;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookmark, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth = FirebaseAuth.getInstance();
        articles = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        loadNews();
    }

    private void loadNews() {

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        DatabaseReference bookmarkRef = FirebaseDatabase.getInstance().getReference("users/"+
                auth.getCurrentUser().getUid()+"/Bookmarks");
        bookmarkRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Article article = dataSnapshot.getValue(Article.class);
                    articles.add(article);
                }
                //sort news by date and time
                progressDialog.dismiss();
                Collections.reverse(articles);
                recyclerView.setAdapter(new NewsAdapter(articles, getContext(), true));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "" + error, Toast.LENGTH_LONG).show();
            }
        });
    }
}