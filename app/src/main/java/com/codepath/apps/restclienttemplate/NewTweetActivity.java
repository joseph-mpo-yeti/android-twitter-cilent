package com.codepath.apps.restclienttemplate;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.codepath.apps.restclienttemplate.databinding.ActivityNewTweetBinding;

public class NewTweetActivity extends AppCompatActivity {
    ActivityNewTweetBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNewTweetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}
