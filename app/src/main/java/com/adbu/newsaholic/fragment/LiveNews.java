package com.adbu.newsaholic.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.adbu.newsaholic.R;
import com.adbu.newsaholic.adapter.ChannelAdapter;
import com.adbu.newsaholic.drivers.FirebaseData;
import com.adbu.newsaholic.firebase.Firebase;
import com.adbu.newsaholic.model.ChannelSortByName;
import com.adbu.newsaholic.model.LiveChannel;
import com.adbu.newsaholic.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LiveNews extends Fragment {

    private List<LiveChannel> liveChannels;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private ProgressDialog progressDialog;
    private FirebaseData firebaseData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_live_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseData = new FirebaseData(getContext());

        liveChannels = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        firebaseData.readUser(new Firebase() {
            @Override
            public void user(User user) {
                if(user.isAdmin()){
                    NavController navController = Navigation.findNavController(view);
                    FloatingActionButton addChannels = (FloatingActionButton) view.findViewById(R.id.add_channel);
                    addChannels.setVisibility(View.VISIBLE);
                    addChannels.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            navController.navigate(R.id.action_liveNews_to_addChannels);
                        }
                    });
                }
            }
        });

        loadNewsUrl();
    }

    private void loadNewsUrl() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("channels");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                liveChannels.clear();
                progressDialog.dismiss();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    LiveChannel channel = snapshot.getValue(LiveChannel.class);
                    liveChannels.add(channel);
                }
                //liveChannels.sort(new ChannelSortByName());
                Collections.sort(liveChannels, new ChannelSortByName());
                recyclerView.setAdapter(new ChannelAdapter(liveChannels, getContext()));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "" + databaseError, Toast.LENGTH_LONG).show();
            }
        });
    }
}