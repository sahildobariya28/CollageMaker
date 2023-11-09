package com.photo.collagemaker.activities.multifit;

import static com.photo.collagemaker.activities.multifit.MultiFitAdapter.getBitmapFromView;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.photo.collagemaker.R;
import com.photo.collagemaker.activities.PhotoShareActivity;
import com.photo.collagemaker.activities.editor.collage_editor.adapter.BackgroundGridAdapter;
import com.photo.collagemaker.activities.picker.MultipleImagePickerActivity;
import com.photo.collagemaker.databinding.ActivityMultiFitBinding;
import com.photo.collagemaker.utils.FilterUtils;
import com.photo.collagemaker.utils.SaveFileUtils;
import com.skydoves.colorpickerview.listeners.ColorListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MultiFitActivity extends AppCompatActivity implements BackgroundGridAdapter.BackgroundGridListener {

    public int selectedPosition = 0;
    MultiFitAdapter multiFitAdapter;

    ActivityMultiFitBinding binding;
    MultiFitViewModel viewModel;

    int currentSavePosition = 0;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMultiFitBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this, new MultiFitViewModelFactory(this)).get(MultiFitViewModel.class);
        setContentView(binding.getRoot());

        viewModel.imageList = getIntent().getStringArrayListExtra(MultipleImagePickerActivity.KEY_DATA_RESULT);

        for (int i = 0; i < viewModel.imageList.size(); i++) {
            Bitmap bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
            bitmap.setPixel(0, 0, Color.RED);

            viewModel.imageModelsList.add(new BackgroundGridAdapter.SquareView(Color.parseColor("#ffffff"), "", true));
        }


        initMultiFitView();
        setLoading(false);

        binding.backgroundContainer.btnColor.setOnClickListener(view -> setBackgroundColor());
        binding.backgroundContainer.btnGradient.setOnClickListener(view -> setBackgroundGradient());
        binding.backgroundContainer.btnBlur.setOnClickListener(view -> selectBackgroundBlur());
        binding.backgroundContainer.btnSelect.setOnClickListener(view -> selectColorPicker());
        binding.backgroundContainer.btnWhite.setOnClickListener(view -> {
            viewModel.imageModelsList.get(selectedPosition).setDrawableId(Color.parseColor("#ffffff"));
            viewModel.imageModelsList.get(selectedPosition).setColor(true);
            multiFitAdapter.notifyItemChanged(selectedPosition);
        });
        binding.backgroundContainer.btnBlack.setOnClickListener(view -> {
            viewModel.imageModelsList.get(selectedPosition).setDrawableId(Color.parseColor("#000000"));
            viewModel.imageModelsList.get(selectedPosition).setColor(true);
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

            viewModel.imageModelsList.get(selectedPosition).setDrawableId(binding.colorPickerView.getColor());
            viewModel.imageModelsList.get(selectedPosition).setColor(true);
            multiFitAdapter.notifyItemChanged(selectedPosition);
        });

        binding.btnSave.setOnClickListener(view -> {

            dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_layout);
            Window window = dialog.getWindow();
            if (window != null) {
                WindowManager.LayoutParams params = window.getAttributes();
                params.width = binding.carouselView.getWidth();
                params.height = binding.carouselView.getHeight();
                window.setAttributes(params);
            }

//            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(false);

            ImageView collageView = dialog.findViewById(R.id.collageView);
//            ViewGroup.LayoutParams layoutParams = collageView.getLayoutParams();
//            layoutParams.width = binding.carouselView.getWidth();
//            layoutParams.height = binding.carouselView.getHeight();
//            collageView.setLayoutParams(layoutParams);

            showImage(collageView);

            dialog.show();

//            multiFitAdapter.isSave = true;
//            multiFitAdapter.notifyDataSetChanged();
        });

    }

    public void showImage(ImageView collageView) {
        BackgroundGridAdapter.SquareView squareView = viewModel.imageModelsList.get(currentSavePosition);

        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), viewModel.imageList.get(currentSavePosition));
        collageView.setImageDrawable(bitmapDrawable);


        if (viewModel.imageModelsList.get(currentSavePosition).isColor) {
            collageView.setBackgroundColor(squareView.drawableId);
            collageView.post(() -> {
                if (currentSavePosition == (viewModel.imageList.size() - 1)) {
                    saveBitmap(collageView, true);
                } else {
                    saveBitmap(collageView, false);
                }

            });

        } else if (viewModel.imageModelsList.get(currentSavePosition).drawable != null) {
            AsyncTask<Void, Bitmap, Bitmap> asyncTask = new AsyncTask<Void, Bitmap, Bitmap>() {
                @Override
                protected Bitmap doInBackground(Void... voidArr) {
                    return FilterUtils.getBlurImageFromBitmap(((BitmapDrawable) squareView.drawable).getBitmap(), 5.0f);
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    collageView.setBackground(new BitmapDrawable(viewModel.activity.getResources(), bitmap));
                    collageView.post(() -> {
                        if (currentSavePosition == (viewModel.imageList.size() - 1)) {
                            saveBitmap(collageView, true);
                        } else {
                            saveBitmap(collageView, false);
                        }
                    });

                }
            };
            asyncTask.execute();
        } else {
            collageView.setBackgroundResource(squareView.drawableId);
            collageView.post(() -> {
                if (currentSavePosition == (viewModel.imageList.size() - 1)) {
                    saveBitmap(collageView, true);
                } else {
                    saveBitmap(collageView, false);
                }
            });
        }
    }

    public void saveBitmap(ImageView imageView, boolean isLast) {

        try {
            Bitmap savedBitmap = getBitmapFromView(imageView);
            File image = SaveFileUtils.saveBitmapFileCollage(viewModel.activity, savedBitmap, new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date()));

            if (isLast) {
                Intent intent = new Intent(viewModel.activity, PhotoShareActivity.class);
                intent.putExtra("path", image.getAbsolutePath());
                viewModel.activity.startActivity(intent);
                currentSavePosition = 0;
                dialog.dismiss();
            }else {
                currentSavePosition++;
                showImage(imageView);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initMultiFitView() {
        multiFitAdapter = new MultiFitAdapter(viewModel);
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
        for (int i = 0; i < viewModel.imageList.size(); i++) {
            Drawable d = new BitmapDrawable(getResources(), viewModel.imageList.get(i));
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


        binding.colorPickerView.setPaletteDrawable(new BitmapDrawable(getResources(), viewModel.imageList.get(selectedPosition)));
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
    public void onBackgroundSelected(BackgroundGridAdapter.SquareView squareView, int position, boolean isBlur) {
        viewModel.imageModelsList.set(selectedPosition, squareView);
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