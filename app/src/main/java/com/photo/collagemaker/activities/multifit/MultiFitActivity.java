package com.photo.collagemaker.activities.multifit;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.photo.collagemaker.activities.editor.collage_editor.CollageEditorActivity;
import com.photo.collagemaker.activities.editor.collage_editor.adapter.BackgroundGridAdapter;
import com.photo.collagemaker.activities.picker.MultipleImagePickerActivity;
import com.photo.collagemaker.databinding.ActivityMultiFitBinding;
import com.photo.collagemaker.grid.QueShotLayout;
import com.photo.collagemaker.picker.PermissionsUtils;
import com.photo.collagemaker.utils.SaveFileUtils;
import com.skydoves.colorpickerview.listeners.ColorListener;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class MultiFitActivity extends AppCompatActivity implements BackgroundGridAdapter.BackgroundGridListener {

    public ArrayList<String> imageList;
    public ArrayList<BackgroundGridAdapter.SquareView> imageModelsList = new ArrayList<>();
    public ArrayList<QueShotLayout> queShotLayout = new ArrayList<>();
    public List<Target> targets = new ArrayList();

    public int selectedPosition = 0;
    MultiFitAdapter multiFitAdapter;

    ActivityMultiFitBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMultiFitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imageList = getIntent().getStringArrayListExtra(MultipleImagePickerActivity.KEY_DATA_RESULT);

        for (int i = 0; i < imageList.size(); i++) {
            Bitmap bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
            bitmap.setPixel(0, 0, Color.RED);

            imageModelsList.add(new BackgroundGridAdapter.SquareView(Color.parseColor("#ffffff"), "", true));
        }


        initMultiFitView();
        setLoading(false);

        binding.backgroundContainer.btnColor.setOnClickListener(view -> setBackgroundColor());
        binding.backgroundContainer.btnGradient.setOnClickListener(view -> setBackgroundGradient());
        binding.backgroundContainer.btnBlur.setOnClickListener(view -> selectBackgroundBlur());
        binding.backgroundContainer.btnSelect.setOnClickListener(view -> selectColorPicker());
        binding.backgroundContainer.btnWhite.setOnClickListener(view -> {
            imageModelsList.get(selectedPosition).setDrawableId(Color.parseColor("#ffffff"));
            imageModelsList.get(selectedPosition).setColor(true);
            multiFitAdapter.notifyItemChanged(selectedPosition);
        });
        binding.backgroundContainer.btnBlack.setOnClickListener(view -> {
            imageModelsList.get(selectedPosition).setDrawableId(Color.parseColor("#000000"));
            imageModelsList.get(selectedPosition).setColor(true);
            multiFitAdapter.notifyItemChanged(selectedPosition);
        });

        binding.btnDone.setOnClickListener(view -> {
            binding.backgroundContainer.recyclerViewColor.setVisibility(View.GONE);
            binding.backgroundContainer.recyclerViewGradient.setVisibility(View.GONE);
            binding.backgroundContainer.recyclerViewBlur.setVisibility(View.GONE);
            binding.colorPickerView.setVisibility(View.GONE);
            binding.btnDone.setVisibility(View.GONE);
            binding.backgroundContainer.backgroundTools.setVisibility(View.VISIBLE);
            binding.carouselView.setVisibility(View.VISIBLE);

            imageModelsList.get(selectedPosition).setDrawableId(binding.colorPickerView.getColor());
            imageModelsList.get(selectedPosition).setColor(true);
            multiFitAdapter.notifyItemChanged(selectedPosition);
        });

        binding.btnSave.setOnClickListener(view -> {

        });

    }

    public void initMultiFitView() {
        multiFitAdapter = new MultiFitAdapter(this, imageList, imageModelsList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.carouselView.setLayoutManager(linearLayoutManager);
        binding.carouselView.setAdapter(multiFitAdapter);

        binding.carouselView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                selectedPosition = linearLayoutManager.findFirstVisibleItemPosition();
            }
        });

    }

    public void setBackgroundColor() {
        binding.backgroundContainer.recyclerViewColor.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        binding.backgroundContainer.recyclerViewColor.setHasFixedSize(true);
        binding.backgroundContainer.recyclerViewColor.setAdapter(new BackgroundGridAdapter(getApplicationContext(), this));

        binding.backgroundContainer.backgroundTools.setVisibility(View.GONE);
        binding.backgroundContainer.recyclerViewColor.scrollToPosition(0);
        ((BackgroundGridAdapter) binding.backgroundContainer.recyclerViewColor.getAdapter()).setSelectedIndex(-1);
        binding.backgroundContainer.recyclerViewColor.getAdapter().notifyDataSetChanged();
        binding.backgroundContainer.recyclerViewColor.setVisibility(View.VISIBLE);
        binding.backgroundContainer.recyclerViewGradient.setVisibility(View.GONE);
        binding.backgroundContainer.recyclerViewBlur.setVisibility(View.GONE);
    }

    public void setBackgroundGradient() {
        binding.backgroundContainer.recyclerViewGradient.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        binding.backgroundContainer.recyclerViewGradient.setHasFixedSize(true);
        binding.backgroundContainer.recyclerViewGradient.setAdapter(new BackgroundGridAdapter(this, this, true));

        binding.backgroundContainer.backgroundTools.setVisibility(View.GONE);

        binding.backgroundContainer.recyclerViewGradient.scrollToPosition(0);
        ((BackgroundGridAdapter) binding.backgroundContainer.recyclerViewGradient.getAdapter()).setSelectedIndex(-1);
        binding.backgroundContainer.recyclerViewGradient.getAdapter().notifyDataSetChanged();

        binding.backgroundContainer.recyclerViewColor.setVisibility(View.GONE);
        binding.backgroundContainer.recyclerViewGradient.setVisibility(View.VISIBLE);
        binding.backgroundContainer.recyclerViewBlur.setVisibility(View.GONE);
    }

    public void selectBackgroundBlur() {
        binding.backgroundContainer.recyclerViewBlur.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        binding.backgroundContainer.recyclerViewBlur.setHasFixedSize(true);
        binding.backgroundContainer.recyclerViewBlur.setAdapter(new BackgroundGridAdapter(getApplicationContext(), this, true));


        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < imageList.size(); i++) {
            Drawable d = new BitmapDrawable(getResources(), imageList.get(i));
            arrayList.add(d);
        }

        BackgroundGridAdapter backgroundGridAdapter = new BackgroundGridAdapter(getApplicationContext(), this, (List<Drawable>) arrayList);
        backgroundGridAdapter.setSelectedIndex(-1);
        binding.backgroundContainer.recyclerViewBlur.setAdapter(backgroundGridAdapter);
        binding.backgroundContainer.backgroundTools.setVisibility(View.GONE);
        binding.backgroundContainer.recyclerViewColor.setVisibility(View.GONE);
        binding.backgroundContainer.recyclerViewGradient.setVisibility(View.GONE);
        binding.backgroundContainer.recyclerViewBlur.setVisibility(View.VISIBLE);
    }

    public void selectColorPicker() {
        binding.btnDone.setVisibility(View.VISIBLE);
        binding.carouselView.setVisibility(View.GONE);
        binding.colorPickerView.setVisibility(View.VISIBLE);

        int viewX = binding.colorPickerView.getLeft() + (binding.colorPickerView.getRight() - binding.colorPickerView.getLeft()) / 2;
        int viewY = binding.colorPickerView.getTop() + (binding.colorPickerView.getBottom() - binding.colorPickerView.getTop()) / 2;
        binding.colorPickerView.setSelectorPoint(viewX, viewY);


        binding.colorPickerView.setPaletteDrawable(new BitmapDrawable(getResources(), imageList.get(selectedPosition)));
        binding.colorPickerView.setColorListener((ColorListener) (color, fromUser) -> {
            binding.backgroundContainer.selectedColorPreview.setCardBackgroundColor(ColorStateList.valueOf(color));
            binding.backgroundContainer.selectedColorPreview.getBackground().setTint(color);
        });

    }


    public void setLoading(boolean z) {
        if (z) {
            binding.relativeLayoutLoading.setVisibility(View.VISIBLE);
            return;
        }
        binding.relativeLayoutLoading.setVisibility(View.GONE);
    }

    @Override
    public void onBackgroundSelected(BackgroundGridAdapter.SquareView squareView, int position) {
        imageModelsList.set(selectedPosition, squareView);
        multiFitAdapter.notifyItemChanged(selectedPosition);
    }

    @Override
    public void onBackPressed() {

        if (binding.backgroundContainer.recyclerViewColor.getVisibility() == View.VISIBLE || binding.backgroundContainer.recyclerViewGradient.getVisibility() == View.VISIBLE || binding.backgroundContainer.recyclerViewBlur.getVisibility() == View.VISIBLE || binding.colorPickerView.getVisibility() == View.VISIBLE) {
            binding.backgroundContainer.recyclerViewColor.setVisibility(View.GONE);
            binding.backgroundContainer.recyclerViewGradient.setVisibility(View.GONE);
            binding.backgroundContainer.recyclerViewBlur.setVisibility(View.GONE);
            binding.colorPickerView.setVisibility(View.GONE);
            binding.btnDone.setVisibility(View.GONE);
            binding.backgroundContainer.backgroundTools.setVisibility(View.VISIBLE);
            binding.carouselView.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }

    }
}