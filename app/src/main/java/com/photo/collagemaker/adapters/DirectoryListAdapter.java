package com.photo.collagemaker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.photo.collagemaker.R;
import com.photo.collagemaker.entity.PhotoDirectory;

import java.util.List;

public class DirectoryListAdapter extends BaseAdapter {
    private List<PhotoDirectory> directories;
    public RequestManager glide;

    public DirectoryListAdapter(RequestManager requestManager, List<PhotoDirectory> list) {
        this.directories = list;
        this.glide = requestManager;
    }

    public int getCount() {
        return directories.size();
    }

    public PhotoDirectory getItem(int i) {
        return directories.get(i);
    }

    public long getItemId(int i) {
        return directories.get(i).hashCode();
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_directory, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.bindData(directories.get(i));
        return view;
    }

    private class ViewHolder {
        public ImageView image_view_cover_album;
        public TextView text_view_count;
        public TextView text_view_name;

        public ViewHolder(View view) {
            image_view_cover_album = view.findViewById(R.id.image_view_cover_album);
            text_view_name = view.findViewById(R.id.text_view_name);
            text_view_count = view.findViewById(R.id.text_view_count);
        }

        public void bindData(PhotoDirectory photoDirectory) {
            RequestOptions requestOptions = new RequestOptions();
            ((requestOptions.dontAnimate()).dontTransform()).override(800, 800);
            glide.setDefaultRequestOptions(requestOptions).load(photoDirectory.getCoverPath()).thumbnail(0.1f).into(image_view_cover_album);
            text_view_name.setText(photoDirectory.getName());
            text_view_count.setText(photoDirectory.getPhotos().size() + "");
        }
    }
}
