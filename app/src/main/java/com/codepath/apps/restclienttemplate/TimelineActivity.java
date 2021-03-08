package com.codepath.apps.restclienttemplate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.adapters.TweetsAdapter;
import com.codepath.apps.restclienttemplate.databinding.ActivityTimelineBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity
        implements TweetsAdapter.ReplyTweetOnClickListener, TweetsAdapter.RetweetOnClickListener, TweetsAdapter.LikeTweetOnClickListener {

    public static final String TAG = "TimelineActivity";
    public static final int REQUEST_CODE = 20;
    ActivityTimelineBinding tBD;
    TwitterClient client;
    TweetsAdapter adapter;
    List<Tweet> tweetList;
    Timer timer;
    EndlessScrollListener scrollListener;
    public static User currentUser;
    private static int backPressedCount;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tBD = ActivityTimelineBinding.inflate(getLayoutInflater());
        setContentView(tBD.getRoot());

        setSupportActionBar(tBD.toolbar);
        getSupportActionBar().setTitle("");

        backPressedCount = 0;
        timer = new Timer();

        tweetList = new ArrayList<>();
        adapter = new TweetsAdapter(tweetList, this, this, this, this);

        tBD.rvTimeline.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        tBD.rvTimeline.setLayoutManager(layoutManager);

        client = TwitterApplication.getRestClient(this);
        populateHomeTimeline();
        fetchUser();

        scrollListener = new EndlessScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadMoreData();
            }
        };

        tBD.rvTimeline.addOnScrollListener(scrollListener);
        tBD.swipeContainer.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );

        tBD.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateHomeTimeline();
            }
        });

        tBD.btnAddTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewTweet();
            }
        });

    }

    @Override
    public void onConfigurationChanged(@NonNull @NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void fetchUser() {
        client.getCurrentUser(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try {
                    currentUser = User.fromJSON(json.jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

            }
        });
    }

    private void loadMoreData() {
        client.getNextPageTweets(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try {
                    adapter.addAll(Tweet.fromJSONArray(json.jsonArray));
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "It failed " + e.getMessage() + " " + json.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    Log.e(TAG, "onFailure: "+json.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "It failed", Toast.LENGTH_LONG).show();
            }
        }, tweetList.get(tweetList.size()-1).getId());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timeline_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        backPressedCount++;
        if(backPressedCount < 2){
            Toast.makeText(getApplicationContext(), "Press back button again to exit the app", Toast.LENGTH_SHORT).show();
            startTimer();
        } else {
            finishAffinity();
        }
    }

    private Runnable timedTask = new Runnable() {
        @Override
        public void run() {
            backPressedCount = 0;
        }
    };

    private void logout(){
        client.clearAccessToken();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void startTimer() {
        handler.postDelayed(timedTask, 3000);
    }

    @Override
    protected void onDestroy() {
        stopTimer();
        super.onDestroy();
    }

    private void stopTimer() {
        handler.removeCallbacks(timedTask);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnLogout:
                logout();
                break;
            default:
                //
        }
        return true;
    }

    private void addNewTweet(boolean isReply, Tweet tweet) {
        Intent i = new Intent(this, NewTweetActivity.class);
        i.putExtra("is_reply", isReply);
        i.putExtra("reply_to", tweet.getId());
        i.putExtra("reply_to_username", tweet.getUser().getScreenName());
        startActivityForResult(i, REQUEST_CODE);
    }

    private void addNewTweet() {
        Intent i = new Intent(this, NewTweetActivity.class);
        startActivityForResult(i, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            String tweet = data.getStringExtra("tweet_body");
            boolean isReply = data.getBooleanExtra("is_reply", false);
            long originalTweetId = data.getLongExtra("reply_to", -1);
            sendTweet(tweet, isReply, originalTweetId);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void sendTweet(String tweetBody, boolean isReply, long originalId) {
        if(isReply){
            client.postReply(sendTweetHandler, tweetBody, originalId);
        } else {
            client.postTweet(sendTweetHandler, tweetBody);
        }
    }

    private JsonHttpResponseHandler sendTweetHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Headers headers, JSON json) {
            try {
                adapter.add(Tweet.fromJSON(json.jsonObject));
                Snackbar snackbar = Snackbar.make(tBD.getRoot(), "Your tweet was sent", Snackbar.LENGTH_SHORT);
                snackbar.show();
            } catch (JSONException e) {
                Snackbar snackbar = Snackbar.make(tBD.getRoot(), e.getMessage(), Snackbar.LENGTH_SHORT);
                snackbar.show();
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
            Snackbar snackbar = Snackbar.make(tBD.getRoot(), "There was a network failure", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    };

    private void populateHomeTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try {
                    adapter.clear();
                    adapter.addAll(Tweet.fromJSONArray(json.jsonArray));
                    tBD.swipeContainer.setRefreshing(false);
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "It failed " + e.getMessage() + " " + json.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "It failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onReplyClick(int pos) {
        addNewTweet(true, tweetList.get(pos));
    }

    @Override
    public void onLikeClick(int pos) {
        Tweet tweet = tweetList.get(pos);
        if(tweet.isFavorited()){
            client.dislikeTweet(responseHandler, tweet.getId());
        } else {
            client.likeTweet(responseHandler, tweet.getId());
        }
    }

    @Override
    public void onRetweetClick(int pos) {
        Tweet tweet = tweetList.get(pos);
        if(tweet.isRetweeted()){
            client.deleteRetweet(responseHandler, tweet.getId());
        } else {
            client.postRetweet(responseHandler, tweet.getId());
        }
    }

    private JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Headers headers, JSON json) {

        }

        @Override
        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
            Snackbar snackbar = Snackbar.make(tBD.getRoot(), "There was a network failure", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    };
}