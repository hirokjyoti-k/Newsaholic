package com.adbu.newsaholic.model;

import com.kwabenaberko.newsapilib.models.Article;

import java.util.Comparator;

public class ArticleSortByDate implements Comparator<Article> {
    @Override
    public int compare(Article o1, Article o2) {
        return (o1.getPublishedAt().compareTo(o2.getPublishedAt()));
    }
}
