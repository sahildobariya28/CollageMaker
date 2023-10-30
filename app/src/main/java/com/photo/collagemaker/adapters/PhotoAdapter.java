package com.photo.collagemaker.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.photo.collagemaker.model.ImageModel;
import com.photo.collagemaker.interfac.OnPhoto;
import com.photo.collagemaker.R;

import java.util.ArrayList;

public class PhotoAdapter extends ArrayAdapter<ImageModel> {
    Context context;
    ArrayList<ImageModel> data;
    int layoutResourceId;
    OnPhoto onPhoto;
    int pHeightItem = 0;
    ArrayList<ImageModel> selectedPhotoList = new ArrayList<>();

    static class RecordHolder {
        ImageView click;
        ImageView imageItem;
        ConstraintLayout layoutRoot;
        TextView selectedCount;
        ConstraintLayout selectedContainer;

        RecordHolder() {
        }
    }

    public PhotoAdapter(Context context, int layoutResourceId, ArrayList<ImageModel> arrayList, OnPhoto onPhoto) {
        super(context, layoutResourceId, arrayList);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = arrayList;
        this.pHeightItem = getDisplayInfo((Activity) context).widthPixels / 3;
        this.onPhoto = onPhoto;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageModel imageModel = data.get(i);
        RecordHolder recordHolder;
        if (view == null) {
            view = ((Activity) context).getLayoutInflater().inflate(layoutResourceId, viewGroup, false);
            recordHolder = new RecordHolder();
            recordHolder.imageItem = view.findViewById(R.id.imageItem);
            recordHolder.click = view.findViewById(R.id.click);
            recordHolder.layoutRoot = view.findViewById(R.id.layoutRoot);
            recordHolder.selectedCount = view.findViewById(R.id.text_selection_count);
            recordHolder.selectedContainer = view.findViewById(R.id.selection_container);



            int selectedCount = 0;
            for (int j = 0; j < selectedPhotoList.size(); j++) {
//                Log.d("fdsfwewrwerw", "itemSelect: " + selectedPhotoList.get(j).getPathFile() + "  " + imageModel.getPathFile());
                if (selectedPhotoList.get(j).getPathFile().equals(imageModel.getPathFile())){
                    selectedCount++;
                }
            }
            Log.d("fdsfwewrwerw", "itemSelect: " + selectedCount);
            if (selectedCount > 0) {
                recordHolder.selectedContainer.setVisibility(View.VISIBLE);
                recordHolder.selectedCount.setText("" + selectedCount);
            } else {
                recordHolder.selectedContainer.setVisibility(View.GONE);
                recordHolder.selectedCount.setText("" + selectedCount);
            }


            recordHolder.layoutRoot.getLayoutParams().height = pHeightItem;
            recordHolder.imageItem.getLayoutParams().width = pHeightItem;
            recordHolder.imageItem.getLayoutParams().height = pHeightItem;
            recordHolder.click.getLayoutParams().width = pHeightItem;
            recordHolder.click.getLayoutParams().height = pHeightItem;
            view.setTag(recordHolder);
        } else {
            recordHolder = (RecordHolder) view.getTag();
        }

        ((RequestBuilder) Glide.with(context).load(imageModel.getPathFile()).placeholder((int) R.drawable.image_show)).into(recordHolder.imageItem);
        view.setOnClickListener(view1 -> {
            if (onPhoto != null) {
                onPhoto.OnItemListPhotoClick(imageModel);
                notifyDataSetChanged();

            }
        });
        return view;
    }


    public void setOnListAlbum(OnPhoto onPhoto2) {
        this.onPhoto = onPhoto2;
    }

    public void updateSelectionList(ArrayList<ImageModel> selectedPhotoList) {
        this.selectedPhotoList = selectedPhotoList;
        notifyDataSetChanged();
    }

    public static DisplayMetrics getDisplayInfo(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }
}
