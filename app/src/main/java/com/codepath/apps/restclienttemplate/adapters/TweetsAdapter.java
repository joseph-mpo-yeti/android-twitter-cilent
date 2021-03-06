package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TimeFormatter;
import com.codepath.apps.restclienttemplate.TweetActivity;
import com.codepath.apps.restclienttemplate.databinding.ItemTweetBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.google.android.material.timepicker.TimeFormat;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcel;
import org.parceler.Parcels;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    interface TweetOnClickListener {
        void onClick(View v);
    }

    List<Tweet> tweets;
    Context context;

    public TweetsAdapter(List<Tweet> tweets, Context context) {
        this.tweets = tweets;
        this.context = context;
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
        holder.bind(tweets.get(position));
    }

    public void clear() {
        this.tweets.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Tweet> tweetList) {
        this.tweets.addAll(tweetList);
        notifyDataSetChanged();
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

        public void bind(Tweet tweet) {
            RequestOptions requestOptions;
            requestOptions = new RequestOptions();
            requestOptions = requestOptions.transform(new CenterCrop(), new RoundedCorners(100));

            Glide.with(context)
                    .load(tweet.getUser().getPublicImgUrl())
                    .centerCrop()
                    .apply(requestOptions)
                    .into(iBD.ivProfileImg);
            iBD.tvBody.setText(tweet.getBody());
            iBD.tvHandle.setText(String.format(
                    "@%s • %s",
                    tweet.getUser().getScreenName(),
                    tweet.getFormattedTimestamp()
            ));
            iBD.tvScreenName.setText(tweet.getUser().getName());
            iBD.ivVerified.setVisibility(tweet.getUser().isVerified() ? View.VISIBLE : View.GONE);
            iBD.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, TweetActivity.class);
                    intent.putExtra("tweet", Parcels.wrap(tweet));
                    context.startActivity(intent);
                }
            });
        }
    }
}
