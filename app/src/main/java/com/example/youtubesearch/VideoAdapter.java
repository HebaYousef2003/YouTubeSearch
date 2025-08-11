package com.example.youtubesearch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.youtubesearch.models.Item;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VH> {

    private Item[] items = new Item[0];
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String videoId);
    }

    public void setListener(OnItemClickListener l) { this.listener = l; }

    public void setItems(Item[] items) {
        this.items = items != null ? items : new Item[0];
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Item it = items[position];
        holder.title.setText(it.snippet.title);
        holder.desc.setText(it.snippet.description);
        holder.channel.setText(it.snippet.channelTitle);
        holder.date.setText(it.snippet.publishedAt);
        String thumb = null;
        if (it.snippet.thumbnails != null && it.snippet.thumbnails.medium != null) {
            thumb = it.snippet.thumbnails.medium.url;
        } else if (it.snippet.thumbnails != null && it.snippet.thumbnails.defaultThumbnail != null) {
            thumb = it.snippet.thumbnails.defaultThumbnail.url;
        }
        if (thumb != null) {
            Glide.with(holder.itemView.getContext()).load(thumb).into(holder.thumb);
        } else {
            holder.thumb.setImageDrawable(null);
        }

        String videoId = null;
        if (it.id != null) {
            // API sometimes returns object or videoId; in our simple model id is string
            videoId = it.id.videoId;
        } else if (it.snippet != null && it.snippet.channelId != null) {
            videoId = it.snippet.channelId;
        }

        final String finalVideoId = videoId;
        holder.itemView.setOnClickListener(v -> {
            if (listener != null && finalVideoId != null) listener.onItemClick(finalVideoId);
        });
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView title, desc, channel, date;
        ImageView thumb;
        VH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle);
            desc = itemView.findViewById(R.id.tvDesc);
            channel = itemView.findViewById(R.id.tvChannel);
            date = itemView.findViewById(R.id.tvDate);
            thumb = itemView.findViewById(R.id.ivThumb);
        }
    }
}
