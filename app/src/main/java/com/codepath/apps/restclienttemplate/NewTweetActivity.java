package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.databinding.ActivityNewTweetBinding;

public class NewTweetActivity extends AppCompatActivity {
    ActivityNewTweetBinding binding;
    public static final int MAX_TWEET_LENGTH = 280;
    int tweetLength;
    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNewTweetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Boolean isReply = getIntent().getBooleanExtra("is_reply", false);
        long replyTo = getIntent().getLongExtra("reply_to", -1);
        String username = getIntent().getStringExtra("reply_to_username");

        if (isReply){
            binding.tvReplyToDisplayName.setText("@" + username);
            binding.rlReplyTo.setVisibility(View.VISIBLE);
            binding.editTweet.setHint("Tweet your reply");
        }

        tweetLength = 0;

        binding.editTweet.requestFocus();
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

        binding.cancelTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                finish();
            }
        });

        RequestOptions requestOptions;
        requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop(), new RoundedCorners(100));

        Glide.with(this)
                .load(TimelineActivity.currentUser.getPublicImgUrl())
                .apply(requestOptions)
                .into(binding.userProfileImg);


        binding.tweetSize.setText(String.valueOf(MAX_TWEET_LENGTH));
        binding.editTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tweetLength = binding.editTweet.getText().length();
                binding.tweetSize.setText(String.valueOf(MAX_TWEET_LENGTH - tweetLength));

                if(tweetLength == 0){
                    binding.sendTweet.setBackgroundColor(
                            getResources().getColor(R.color.button_disabled));
                    binding.tweetSize.setTextColor(getResources().getColor(R.color.twitter_primary));
                } else if(tweetLength < 200) {
                    binding.sendTweet.setClickable(true);
                    binding.tweetSize.setTextColor(getResources().getColor(R.color.twitter_primary));
                    binding.sendTweet.setBackgroundColor(
                            getResources().getColor(R.color.twitter_primary));
                } else if (tweetLength <= 280) {
                    binding.sendTweet.setBackgroundColor(
                            getResources().getColor(R.color.twitter_primary));
                    binding.sendTweet.setClickable(true);
                    binding.tweetSize.setTextColor(getResources().getColor(R.color.orange));
                } else {
                    binding.sendTweet.setBackgroundColor(
                            getResources().getColor(R.color.button_disabled));
                    binding.tweetSize.setTextColor(getResources().getColor(R.color.red));
                    binding.sendTweet.setClickable(false);
                }
            }
        });

        binding.sendTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tweetLength > 0 && tweetLength <= MAX_TWEET_LENGTH){
                    Intent i = new Intent();
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    i.putExtra("tweet_body", binding.editTweet.getText().toString());
                    if(isReply){
                        i.putExtra("is_reply", true);
                        i.putExtra("reply_to", replyTo);
                    }
                    setResult(RESULT_OK, i);
                    finish();
                }
            }
        });

    }
}
