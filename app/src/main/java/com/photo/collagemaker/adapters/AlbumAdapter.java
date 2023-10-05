package com.photo.collagemaker.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.photo.collagemaker.model.ImageModel;
import com.photo.collagemaker.interfac.OnAlbum;
import com.photo.collagemaker.R;

import java.io.File;
import java.util.ArrayList;

public class AlbumAdapter extends ArrayAdapter<ImageModel> {
    private Context context;
    private ArrayList<ImageModel> data;
    private int layoutResourceId;
    private OnAlbum onItem;

    public AlbumAdapter(Context context, OnAlbum onAlbum, int layoutResourceId, ArrayList<ImageModel> arrayList) {
        super(context, layoutResourceId, arrayList);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = arrayList;
        this.onItem = onAlbum;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = ((Activity) context).getLayoutInflater().inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.text_view_name_album = convertView.findViewById(R.id.text_view_name_album);
            holder.image_view_album = convertView.findViewById(R.id.image_view_album);
            holder.relative_layout_root = convertView.findViewById(R.id.relative_layout_root);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ImageModel imageModel = data.get(position);
        holder.text_view_name_album.setText(imageModel.getName());
        Glide.with(context)
                .load(new File(imageModel.getPathFile()))
                .placeholder(R.drawable.image_show)
                .into(holder.image_view_album);

        convertView.setOnClickListener(view -> {
            if (onItem != null) {
                onItem.OnItemAlbumClick(position);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        ImageView image_view_album;
        LinearLayout relative_layout_root;
        TextView text_view_name_album;
    }
}
