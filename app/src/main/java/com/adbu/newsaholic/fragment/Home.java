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

public class Home extends Fragment {

    private NewsApiClient newsApiClient;
    private List<Article> articles;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseData firebaseData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
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

        firebaseData.readUser(new Firebase() {
            @Override
            public void user(User user) {
//                loadNews(user);
            }
        });

    }

    public void loadNews(User user) {

        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .country(user.getCountry())
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        articles.clear();
                        for(Article article: response.getArticles()){
                            articles.add(article);
                            System.out.println(response.getTotalResults());
                        }
                        recyclerView.setAdapter(new NewsAdapter(articles, getContext()));
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        System.out.println(throwable.getMessage());
                    }
                }
        );
    }
}