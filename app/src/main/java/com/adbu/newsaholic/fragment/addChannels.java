package com.adbu.newsaholic.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.adbu.newsaholic.R;
import com.adbu.newsaholic.model.LiveChannel;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class addChannels extends Fragment {

    private EditText cId, name, url, logo, language;
    private MaterialButton addChannel;
    private LiveChannel channel;
    private String CID;
    private NavController navController;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_channels, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        cId = (EditText) view.findViewById(R.id.cId);
        name = (EditText) view.findViewById(R.id.name);
        url = (EditText) view.findViewById(R.id.url);
        logo = (EditText) view.findViewById(R.id.logo);
        language = (EditText) view.findViewById(R.id.language);

        channel = new LiveChannel();

        navController = Navigation.findNavController(view);
        
        addChannel = (MaterialButton) view.findViewById(R.id.addChannelBtn);
        addChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getChannelData();
            }
        });
    }

    private void getChannelData() {

        CID = cId.getText().toString().trim();
        channel.setNewsName(name.getText().toString().trim());
        channel.setNewsUrl(url.getText().toString().trim());
        channel.setLanguage(language.getText().toString().trim());
        channel.setNewsLogo(logo.getText().toString().trim());

        if(validateData()){
            uploadChannelData();
        }else {
            return;
        }
    }

    private boolean validateData() {

        if(CID.isEmpty()){
            cId.setError("Channel ID can't be empty");
            return false;
        }

        if(channel.getNewsName().isEmpty()){
            name.setError("Channel Name can't be empty");
            return false;
        }

        if(channel.getLanguage().isEmpty()){
            language.setError("Channel language can't be empty");
            return false;
        }

        if(channel.getNewsUrl().isEmpty()){
            url.setError("Streaming url can't be empty");
            return false;
        }

        if(channel.getNewsLogo().isEmpty()){
            logo.setError("Channel logo can't be empty");
            return false;
        }

        return true;
    }

    private void uploadChannelData() {
        DatabaseReference channelDB = FirebaseDatabase.getInstance().getReference("channels/"+CID);
        channelDB.setValue(channel);
        Toast.makeText(getContext(), "Chennel Added Successfully", Toast.LENGTH_SHORT).show();

        navController.navigate(R.id.action_addChannels_to_liveNews);
    }


}