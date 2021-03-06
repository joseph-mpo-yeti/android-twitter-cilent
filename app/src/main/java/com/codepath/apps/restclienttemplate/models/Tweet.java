package com.codepath.apps.restclienttemplate.models;

import com.codepath.apps.restclienttemplate.TimeFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;
import org.parceler.ParcelClass;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Tweet {

    String body, createdAt;
    User user;
    boolean favorited, retweeted;
    int favoriteCount, retweetCount;
    long id;

    public Tweet(){}

    public static Tweet fromJSON(JSONObject ob) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = ob.getString("text");
        tweet.createdAt = ob.getString("created_at");
        tweet.user = User.fromJSON(ob.getJSONObject("user"));
        tweet.favorited = ob.getBoolean("favorited");
        tweet.retweeted = ob.getBoolean("retweeted");
        tweet.favoriteCount = ob.getInt("favorite_count");
        tweet.retweetCount = ob.getInt("retweet_count");
        tweet.id = ob.getLong("id");
        return tweet;
    }

    public static List<Tweet> fromJSONArray(JSONArray arr) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();

        for (int i = 0; i < arr.length(); i++) {
            tweets.add(fromJSON(arr.getJSONObject(i)));
        }

        return tweets;
    }

    public String getBody() {
        return body;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public boolean isRetweeted() {
        return retweeted;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public long getId() {
        return id;
    }

    public String getFormattedTimestamp(){
        return TimeFormatter.getTimeDifference(createdAt);
    }
}
