package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.NewTweetActivity;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TimeFormatter;
import com.codepath.apps.restclienttemplate.TweetActivity;
import com.codepath.apps.restclienttemplate.databinding.ItemTweetBinding;
import com.codepath.apps.restclienttemplate.models.Media;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.google.android.material.timepicker.TimeFormat;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcel;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    public interface ReplyTweetOnClickListener {
        void onReplyClick(int pos);
    }
    
    public interface LikeTweetOnClickListener {
        void onLikeClick(int pos);
    }   
    
    public interface RetweetOnClickListener {
        void onRetweetClick(int pos);
    }

    public static final String TAG = "TweetsAdapter";

    ReplyTweetOnClickListener replyListener;
    LikeTweetOnClickListener likeListener;
    RetweetOnClickListener retweetListener;
    List<Tweet> tweets;
    Context context;

    public TweetsAdapter(List<Tweet> tweets,
                         Context context,
                         ReplyTweetOnClickListener replyListener,
                         LikeTweetOnClickListener likeListener,
                         RetweetOnClickListener retweetListener
    ) {
        this.tweets = tweets;
        this.context = context;
        this.replyListener = replyListener;
        this.likeListener = likeListener;
        this.retweetListener = retweetListener;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    public void clear() {
        this.tweets.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Tweet> tweetList) {
        this.tweets.addAll(tweetList);
        notifyDataSetChanged();
    }

    public void add(Tweet tweet) {
        this.tweets.add(0, tweet);
        notifyItemInserted(0);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemTweetBinding iBD;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            iBD = ItemTweetBinding.bind(itemView);
        }

        public void bind(int pos) {
            Tweet tweet = tweets.get(pos);

            RequestOptions requestOptions;
            requestOptions = new RequestOptions();
            requestOptions = requestOptions.transform(new CenterCrop(), new RoundedCorners(100));

            Tweet temp = tweet.getRetweetedStatus();

            if(temp != null && temp instanceof Tweet){
                iBD.retweetedText.setText("@"+tweet.getUser().getScreenName() + " Retweeted");
                iBD.retweetedBanner.setVisibility(View.VISIBLE);
                setUI(temp, requestOptions,  tweet.getMedias(), pos);
            } else {
                iBD.retweetedBanner.setVisibility(View.GONE);
                setUI(tweet, requestOptions, tweet.getMedias(), pos);
            }

        }

        private void setUI(Tweet tweet, RequestOptions requestOptions, List<Media> medias, int pos){

            Glide.with(context)
                    .load(tweet.getUser().getPublicImgUrl())
                    .fitCenter()
                    .centerInside()
                    .centerCrop()
                    .apply(requestOptions)
                    .into(iBD.ivProfileImg);
            iBD.tvBody.setText(tweet.getBody());
            iBD.tvHandle.setText(String.format(
                    "@%s • %s",
                    resizeName(tweet),
                    tweet.getFormattedTimestamp()
            ));
            iBD.tvScreenName.setText(tweet.getUser().getName());
            iBD.ivVerified.setVisibility(tweet.getUser().isVerified() ? View.VISIBLE : View.GONE);
            iBD.tvFavoritedCount.setText(displayCount(tweet.getFavoriteCount()));
            iBD.tvRetweetedCount.setText(displayCount(tweet.getRetweetCount()));
            iBD.rootElement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startTweetActivity(tweet);
                }
            });

            if(tweet.isFavorited()){
                Glide.with(context)
                        .load(R.drawable.ic_like_active)
                        .into(iBD.favoriteIcon);
                iBD.tvFavoritedCount.setTextColor(
                        context.getResources().getColor(R.color.red_pink)
                );
            } else {
                Glide.with(context)
                        .load(R.drawable.ic_like)
                        .into(iBD.favoriteIcon);
                iBD.tvFavoritedCount.setTextColor(
                        context.getResources().getColor(R.color.twitter_gray)
                );
            }

            if(tweet.isRetweeted()){
                Glide.with(context)
                        .load(R.drawable.ic_retweet_active)
                        .into(iBD.retweetIcon);
                iBD.tvRetweetedCount.setTextColor(
                        context.getResources().getColor(R.color.green)
                );
            } else {
                Glide.with(context)
                        .load(R.drawable.ic_retweet)
                        .into(iBD.retweetIcon);
                iBD.tvRetweetedCount.setTextColor(
                        context.getResources().getColor(R.color.twitter_gray)
                );
            }

            if(medias.size() > 0){
                MediasAdapter adapter = new MediasAdapter(itemView.getContext(), medias);
                iBD.rvMedias.setAdapter(adapter);
                iBD.rvMedias.setLayoutManager(new LinearLayoutManager(
                        itemView.getContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false));
                iBD.rvMedias.setVisibility(View.VISIBLE);
            } else {
                iBD.rvMedias.setVisibility(View.GONE);
            }

            iBD.actionShareTweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Share Tweet");
                    intent.putExtra(Intent.EXTRA_TEXT, "https://twitter.com/i/web/status/"+tweet.getId());
                    context.startActivity(Intent.createChooser(intent, "Share on"));
                }
            });

            iBD.actionMakeFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    likeListener.onLikeClick(pos);
                    tweet.setFavorited(!tweet.isFavorited());
                    if(tweet.isFavorited()){
                        tweet.setFavoriteCount(tweet.getFavoriteCount()+1);
                    } else {
                        tweet.setFavoriteCount(tweet.getFavoriteCount()-1);
                    }
                    notifyItemChanged(pos);
                }
            });

            iBD.actionRetweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    retweetListener.onRetweetClick(pos);
                    tweet.setRetweeted(!tweet.isRetweeted());
                    if(tweet.isRetweeted()){
                        tweet.setRetweetCount(tweet.getRetweetCount()+1);
                    } else {
                        tweet.setRetweetCount(tweet.getRetweetCount()-1);
                    }
                    notifyItemChanged(pos);
                }
            });

            iBD.actionReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    replyListener.onReplyClick(pos);
                }
            });
        }

        private String displayCount(int count) {
            if(count == 0)
                return "";

            if (count >= 1_000 && count < 1_000_000){
                return String.valueOf(count/1_000f).substring(0, 3) + "K";
            }

            if(count >= 1_000_000){
                return String.valueOf(count/1_000_000f).substring(0, 3) + "M";
            }

            return String.valueOf(count);
        }

        private void startTweetActivity(Tweet tweet) {
            Intent intent = new Intent(context, TweetActivity.class);
            intent.putExtra("tweet", Parcels.wrap(tweet));
            context.startActivity(intent);
        }

        private String resizeName(Tweet tweet) {
            String screenName = tweet.getUser().getScreenName();
            String name = tweet.getUser().getName();

            if(name.length() + screenName.length() > 33){
                int newSize = 27 - name.length() - 1;
                return screenName.substring(0, newSize > 0 ? newSize:0) + "...";
            }

            return screenName;
        }
    }
}
