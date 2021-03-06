package com.adbu.newsaholic.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adbu.newsaholic.R;
import com.adbu.newsaholic.adapter.NewsAdapter;
import com.adbu.newsaholic.drivers.FirebaseData;
import com.adbu.newsaholic.firebase.Firebase;
import com.adbu.newsaholic.model.ArticleSortByDate;
import com.adbu.newsaholic.model.User;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Home extends Fragment {

    private NewsApiClient newsApiClient;
    private List<Article> articles;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseData firebaseData;
    private SwipeRefreshLayout swipeRefreshLayout;
    private User userObj;

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

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.homeRefresh);

        firebaseData.getUser(new Firebase() {
            @Override
            public void user(User user) {
                loadNews(user);
                userObj = user;
            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNews(userObj);
            }
        });
    }

    public void loadNews(User user) {

        String[] categories = user.getCategory().split(",");
        articles.clear();

        for(String categroy : categories){
            newsApiClient.getTopHeadlines(
                    new TopHeadlinesRequest.Builder()
                            .country(user.getCountry())
                            .category(categroy)
                            .build(),
                    new NewsApiClient.ArticlesResponseCallback() {
                        @Override
                        public void onSuccess(ArticleResponse response) {
//                            articles.clear();
                            for(Article article: response.getArticles()){
                                articles.add(article);
                            }
                            Collections.sort(articles, new ArticleSortByDate());
                            Collections.reverse(articles);
                            recyclerView.setAdapter(new NewsAdapter(articles, getContext()));
                            swipeRefreshLayout.setRefreshing(false);
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            System.out.println(throwable.getMessage());
                        }
                    }
            );
        }
    }
}