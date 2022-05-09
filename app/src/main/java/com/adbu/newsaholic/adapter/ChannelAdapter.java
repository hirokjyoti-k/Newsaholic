package com.adbu.newsaholic.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adbu.newsaholic.LiveTVPlayer;
import com.adbu.newsaholic.R;
import com.adbu.newsaholic.model.LiveChannel;
import com.bumptech.glide.Glide;

import java.util.List;

public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.ChannelViewHolder> {

    List<LiveChannel> liveChannels;
    Context context;

    public ChannelAdapter(List<LiveChannel> liveChannels, Context context) {
        this.liveChannels = liveChannels;
        this.context = context;
    }

    @NonNull
    @Override
    public ChannelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.channels, parent, false);
        return new ChannelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChannelViewHolder holder, int position) {
        LiveChannel channel = liveChannels.get(position);
        holder.newsName.setText(channel.getNewsName());
        holder.newsLang.setText(channel.getLanguage());
        Glide.with(context).load(channel.getNewsLogo()).into(holder.newsLogo);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LiveTVPlayer.class);
                intent.putExtra("newsUrl", "" + channel.getNewsUrl());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return liveChannels.size();
    }

    class ChannelViewHolder extends RecyclerView.ViewHolder {

        private ImageView newsLogo;
        private TextView newsName;
        private TextView newsLang;

        public ChannelViewHolder(@NonNull View itemView) {
            super(itemView);

            newsLogo = (ImageView) itemView.findViewById(R.id.NewsLogo);
            newsName = (TextView) itemView.findViewById(R.id.NewsName);
            newsLang = (TextView) itemView.findViewById(R.id.NewsLanguage);
        }
    }
}
