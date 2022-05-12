package com.adbu.newsaholic.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adbu.newsaholic.R;
import com.adbu.newsaholic.ViewArticle;

import com.adbu.newsaholic.ViewBookmarks;
import com.bumptech.glide.Glide;
import com.kwabenaberko.newsapilib.models.Article;


import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    List<Article> articles;
    Context context;
    boolean bookmark = false;

    public NewsAdapter(List<Article> articles, Context context) {
        this.articles = articles;
        this.context = context;
    }

    public NewsAdapter(List<Article> articles, Context context, boolean bookmark) {
        this.articles = articles;
        this.context = context;
        this.bookmark = bookmark;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.articles, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        Article article = articles.get(position);
        holder.title.setText(article.getTitle());
        holder.source.setText("Source: "+article.getSource().getName()+" • "+article.getPublishedAt().substring(0,10));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(bookmark)
                    intent = new Intent(context, ViewBookmarks.class);
                else
                    intent = new Intent(context, ViewArticle.class);
                intent.putExtra("source", ""+article.getSource().getName());
                intent.putExtra("author", ""+article.getAuthor());
                intent.putExtra("title", ""+article.getTitle());
                intent.putExtra("description", ""+article.getDescription());
                intent.putExtra("url", ""+article.getUrl());
                intent.putExtra("urlToImage", ""+article.getUrlToImage());
                intent.putExtra("publishedAt", ""+article.getPublishedAt());
                context.startActivity(intent);
            }
        });

        Glide.with(context)
                .load(article.getUrlToImage())
                .centerCrop()
                .placeholder(R.drawable.placeholder_news)
                .into(holder.image);

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, article.getTitle()+"\n\nRead more:-"+article.getUrl());
                sendIntent.setType("text/plain");
                context.startActivity(sendIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder{

        TextView title, source;
        ImageView image, share;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            source = (TextView) itemView.findViewById(R.id.source);
            image = (ImageView) itemView.findViewById(R.id.image);
            share = (ImageView) itemView.findViewById(R.id.share);
        }
    }
}
