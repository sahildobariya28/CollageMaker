package com.photo.collagemaker.activities.editor.scrapbook.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.photo.collagemaker.R;
import com.photo.collagemaker.activities.editor.scrapbook.ScrapBookFragment;

public class ScrapBGAdapter extends RecyclerView.Adapter<ScrapBGAdapter.ViewHolder> {
    Context context;
    String[] emojies;
    ScrapBookFragment fragment;

    public ScrapBGAdapter(Context context, String[] emojies, ScrapBookFragment fragment) {
        this.context = context;
        this.emojies = emojies;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.bg_row_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String sticker = emojies[position];
        Log.e("path", sticker);

        Glide.with(context).load(Uri.parse("file:///android_asset/backgrounds_image/" + sticker)).into(holder.emogies);
        holder.emoji_layout.setOnClickListener((View.OnClickListener) v -> fragment.setBackground(sticker));
    }

    @Override
    public int getItemCount() {
        return emojies.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout emoji_layout;
        ImageView emogies;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            emoji_layout = itemView.findViewById(R.id.emoji_layout);
            emogies = itemView.findViewById(R.id.emogies);
        }
    }
}