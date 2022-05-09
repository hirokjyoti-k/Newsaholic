package com.adbu.newsaholic.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.adbu.newsaholic.R;
import com.adbu.newsaholic.adapter.NewsAdapter;
import com.adbu.newsaholic.drivers.FirebaseData;
import com.adbu.newsaholic.firebase.Firebase;

import com.adbu.newsaholic.model.User;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.ArrayList;
import java.util.List;

public class Category extends Fragment implements View.OnClickListener {

    private NewsApiClient newsApiClient;
    private List<Article> articles;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private Button general, entertainment, business, health, science, sports, technology;
    private String country;
    private FirebaseData firebaseData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        newsApiClient = new NewsApiClient("1b597cd6f2e542ba88bef759ca4c9714");
        firebaseData = new FirebaseData(getContext());
        articles = new ArrayList<>();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        general = (Button) view.findViewById(R.id.general);
        entertainment = (Button) view.findViewById(R.id.entertainment);
        business = (Button) view.findViewById(R.id.business);
        health = (Button) view.findViewById(R.id.health);
        science = (Button) view.findViewById(R.id.science);
        sports = (Button) view.findViewById(R.id.sports);
        technology = (Button) view.findViewById(R.id.technology);

        general.setOnClickListener(this);
        entertainment.setOnClickListener(this);
        business.setOnClickListener(this);
        health.setOnClickListener(this);
        science.setOnClickListener(this);
        sports.setOnClickListener(this);
        technology.setOnClickListener(this);

        firebaseData.readUser(new Firebase() {
            @Override
            public void user(User user) {
                country = user.getCountry();
                loadNewsByCategory("general");
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.general:
                loadNewsByCategory("general");
            case R.id.entertainment:
                loadNewsByCategory("entertainment");
                break;
            case R.id.business:
                loadNewsByCategory("business");
                break;
            case R.id.health:
                loadNewsByCategory("health");
                break;
            case R.id.science:
                loadNewsByCategory("science");
                break;
            case R.id.sports:
                loadNewsByCategory("sports");
                break;
            case R.id.technology:
                loadNewsByCategory("technology");
                break;
            default:
                Toast.makeText(getContext(), "Please select a category", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadNewsByCategory(String category) {

        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .country(country)
                        .category(category)
                        .pageSize(100)
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        articles.clear();
                        for(Article article: response.getArticles()){
                            articles.add(article);
                        }
                        recyclerView.setAdapter(new NewsAdapter(articles, getContext()));
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Toast.makeText(getContext(), ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}