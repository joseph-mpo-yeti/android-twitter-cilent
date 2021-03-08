package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.databinding.ActivityViewImageBinding;

public class ViewImageActivity extends AppCompatActivity {
    ActivityViewImageBinding avB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        avB = ActivityViewImageBinding.inflate(getLayoutInflater());
        setContentView(avB.getRoot());

        String url = getIntent().getStringExtra("url");

        Glide.with(this)
                .load(url)
                .fitCenter()
                .into(avB.ivImagePreview);

    }
}