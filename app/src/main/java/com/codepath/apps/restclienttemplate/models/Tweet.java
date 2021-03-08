package com.codepath.apps.restclienttemplate.models;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

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

    public static String TAG = "TweetModel";
    Tweet retweetedStatus;
    String body, createdAt;
    User user;
    boolean favorited, retweeted, fromOtherTweet;
    int favoriteCount, retweetCount;
    long id;
    List<Media> medias;

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
        tweet.medias = new ArrayList<>();

        try {
            tweet.medias.addAll(Media.getMediafromJSON(ob.getJSONObject("extended_entities")));
            Log.i(TAG, "fromJSON: Entities were parsed: for "+ tweet.getId());
        } catch (Exception e){
            Log.e(TAG, "Error: Entities are not available for "+tweet.getId()+"\n"+e.getMessage());

            try {
                tweet.medias.addAll(Media.getMediafromJSON(ob.getJSONObject("entities")));
                Log.i(TAG, "fromJSON: Extended Entities were parsed: for "+ tweet.getId());
            } catch (Exception e2){
                Log.e(TAG, "Error: Extended Entities are not available for "+tweet.getId()+"\n"+e2.getMessage());
            }
        }


        try {
            tweet.retweetedStatus = Tweet.fromJSON(ob.getJSONObject("retweeted_status"));
            tweet.fromOtherTweet = true;
        } catch (Exception e){
            tweet.fromOtherTweet = false;
            tweet.retweetedStatus = null;
        }

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

    public List<Media> getMedias() {
        return medias;
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

    @Nullable
    public Tweet getRetweetedStatus() {
        if(fromOtherTweet){
            return retweetedStatus;
        }

        return null;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public void setRetweeted(boolean retweeted) {
        this.retweeted = retweeted;
    }

    public String getFormattedTimestamp(){
        return TimeFormatter.getTimeDifference(createdAt);
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }
}
