package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.databinding.ActivityTweetBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class TweetActivity extends AppCompatActivity {
    
    ActivityTweetBinding atB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        atB = ActivityTweetBinding.inflate(getLayoutInflater());
        setContentView(atB.getRoot());

        Tweet tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        
        atB.ivVerifiedView.setVisibility(tweet.getUser().isVerified() ? View.VISIBLE : View.GONE);

        RequestOptions requestOptions;
        requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop(), new RoundedCorners(100));

        Glide.with(this)
                .load(tweet.getUser().getPublicImgUrl())
                .centerCrop()
                .apply(requestOptions)
                .into(atB.ivProfileImgView);
        atB.tvBodyView.setText(tweet.getBody());
        atB.tvHandle.setText(String.format(
                "@%s • %s",
                tweet.getUser().getScreenName(),
                tweet.getFormattedTimestamp()
        ));
        atB.tvScreenNameView.setText(tweet.getUser().getName());

    }
}