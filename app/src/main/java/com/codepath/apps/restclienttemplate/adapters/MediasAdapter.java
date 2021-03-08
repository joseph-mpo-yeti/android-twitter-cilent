package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.ViewImageActivity;
import com.codepath.apps.restclienttemplate.databinding.ItemPreviewImageBinding;
import com.codepath.apps.restclienttemplate.models.Media;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MediasAdapter extends RecyclerView.Adapter<MediasAdapter.ViewHolder> {

    List<Media> mediaList;
    Context context;

    public MediasAdapter(Context context, List<Media> mediaList) {
        this.mediaList = mediaList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_preview_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.bind(mediaList.get(position));
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ItemPreviewImageBinding iiBD;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            iiBD = ItemPreviewImageBinding.bind(itemView);
        }

        public void bind(Media media) {

            RequestOptions requestOptions;
            requestOptions = new RequestOptions();
            requestOptions = requestOptions.transform(new CenterCrop(), new RoundedCorners(16));

            iiBD.previewImg.setOnClickListener(v -> {
                Intent i = new Intent(context, ViewImageActivity.class);
                i.putExtra("url", media.getURL());
                context.startActivity(i);
            });

            Glide.with(context)
                    .load(media.getURL())
                    .apply(requestOptions)
                    .into(iiBD.previewImg);
        }
    }
}
