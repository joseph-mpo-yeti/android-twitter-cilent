package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.adapters.TweetsAdapter;
import com.codepath.apps.restclienttemplate.databinding.ActivityTimelineBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {

    public static final String TAG = "TimelineActivity";
    ActivityTimelineBinding tBD;
    TwitterClient client;
    TweetsAdapter adapter;
    List<Tweet> tweetList;
    EndlessScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tBD = ActivityTimelineBinding.inflate(getLayoutInflater());
        setContentView(tBD.getRoot());

        setSupportActionBar(tBD.timelineToolbar);

        tweetList = new ArrayList<>();
        adapter = new TweetsAdapter(tweetList, this);

        tBD.rvTimeline.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        tBD.rvTimeline.setLayoutManager(layoutManager);

        client = TwitterApplication.getRestClient(this);
        populateHomeTimeline();

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

    }


    public void newTweet(View v){
        Intent i = new Intent(getApplicationContext(), NewTweetActivity.class);
        startActivity(i);
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
                    Log.e(TAG, "onFailure: "+json.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "It failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!client.isAuthenticated()){
            setResult(RESULT_OK);
            finish();
        }
    }

    public void refresh(View view) {
        populateHomeTimeline();
    }
}