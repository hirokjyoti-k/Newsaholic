package com.adbu.newsaholic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.Source;

public class ViewBookmarks extends AppCompatActivity {

    private Article article;
    private TextView source, author, title, description;
    private ImageView image;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bookmarks);

        auth = FirebaseAuth.getInstance();

        source = (TextView) findViewById(R.id.source);
        author = (TextView) findViewById(R.id.author);
        title = (TextView) findViewById(R.id.title);
        description = (TextView) findViewById(R.id.description);
        image = (ImageView) findViewById(R.id.image);

        getArticleData();
    }

    private void getArticleData() {

        Source source = new Source();
        source.setName(getIntent().getStringExtra("source"));

        article = new Article();
        article.setSource(source);
        article.setAuthor(getIntent().getStringExtra("author"));
        article.setTitle(getIntent().getStringExtra("title"));
        article.setDescription(getIntent().getStringExtra("description"));
        article.setUrl(getIntent().getStringExtra("url"));
        article.setUrlToImage(getIntent().getStringExtra("urlToImage"));
        article.setPublishedAt(getIntent().getStringExtra("publishedAt"));
        loadData();
    }

    private void loadData() {

        source.setText("Source : "+getIntent().getStringExtra("source")+" • "+article.getPublishedAt().substring(0,10));
        title.setText(article.getTitle());
        description.setText(article.getDescription());
        author.setText("Author: "+article.getAuthor());
        Glide.with(this)
                .load(article.getUrlToImage())
                .centerCrop()
                .placeholder(R.drawable.placeholder_news)
                .into(image);
    }

    public void readMore(View view) { ;
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(getIntent().getStringExtra("url")));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu_3, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.removeBookmark:
                removeBookmark();
                break;
            case R.id.share:
                share();
                break;
            default:
                Toast.makeText(ViewBookmarks.this, "Invalid Option", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void share() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, article.getTitle()+"\n\nRead more:-"+article.getUrl());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void removeBookmark() {
        DatabaseReference bookmarkRef = FirebaseDatabase.getInstance().getReference("users/"+
                auth.getCurrentUser().getUid()+"/Bookmarks/"+article.getPublishedAt());
        bookmarkRef.removeValue();
        Toast.makeText(this, "Bookmark removed.", Toast.LENGTH_SHORT).show();
        finish();
    }
}