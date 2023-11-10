package com.photo.collagemaker.activities.multifit;

import static com.photo.collagemaker.activities.multifit.MultiFitAdapter.getBitmapFromView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
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
import com.photo.collagemaker.activities.editor.collage_editor.adapter.FilterAdapter;
import com.photo.collagemaker.activities.picker.MultipleImagePickerActivity;
import com.photo.collagemaker.assets.FilterCodeAsset;
import com.photo.collagemaker.databinding.ActivityMultiFitBinding;
import com.photo.collagemaker.listener.FilterListener;
import com.photo.collagemaker.utils.FilterUtils;
import com.photo.collagemaker.utils.SaveFileUtils;
import com.skydoves.colorpickerview.listeners.ColorListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MultiFitActivity extends AppCompatActivity implements BackgroundGridAdapter.BackgroundGridListener, FilterListener {

    public int firstVisibleItem = 0;
    MultiFitAdapter multiFitAdapter;

    ActivityMultiFitBinding binding;
    MultiFitViewModel viewModel;

    int currentSavePosition = 0;
    Dialog dialog;

    // ArrayList
    public ArrayList listFilterAll = new ArrayList<>();
    public ArrayList listFilterBW = new ArrayList<>();
    public ArrayList listFilterVintage = new ArrayList<>();
    public ArrayList listFilterSmooth = new ArrayList<>();
    public ArrayList listFilterCold = new ArrayList<>();
    public ArrayList listFilterWarm = new ArrayList<>();
    public ArrayList listFilterLegacy = new ArrayList<>();
    public List<Drawable> drawableList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMultiFitBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this, new MultiFitViewModelFactory(this, binding)).get(MultiFitViewModel.class);
        setContentView(binding.getRoot());

        viewModel.imageList = getIntent().getStringArrayListExtra(MultipleImagePickerActivity.KEY_DATA_RESULT);

        for (int i = 0; i < viewModel.imageList.size(); i++) {
            viewModel.imageDrawableList.add(new BitmapDrawable(getResources(), viewModel.imageList.get(i)));
        }

        for (int i = 0; i < viewModel.imageDrawableList.size(); i++) {
            Bitmap bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
            bitmap.setPixel(0, 0, Color.RED);

            viewModel.imageModelsList.add(new BackgroundGridAdapter.SquareView(Color.parseColor("#ffffff"), "", true));
        }

        binding.backgroundContainer.btnFilter.setVisibility(View.VISIBLE);

        initMultiFitView();
        initFilterView();
        setLoading(false);

        binding.backgroundContainer.btnFilter.setOnClickListener(view -> setFilter());
        binding.backgroundContainer.btnColor.setOnClickListener(view -> setBackgroundColor());
        binding.backgroundContainer.btnGradient.setOnClickListener(view -> setBackgroundGradient());
        binding.backgroundContainer.btnBlur.setOnClickListener(view -> selectBackgroundBlur());
        binding.backgroundContainer.btnSelect.setOnClickListener(view -> selectColorPicker());
        binding.backgroundContainer.btnWhite.setOnClickListener(view -> {
            for (int i = 0; i < viewModel.imageModelsList.size(); i++) {
                viewModel.imageModelsList.get(i).setDrawableId(Color.parseColor("#ffffff"));
                viewModel.imageModelsList.get(i).setColor(true);
                multiFitAdapter.notifyItemChanged(i);
            }

        });
        binding.backgroundContainer.btnBlack.setOnClickListener(view -> {
            for (int i = 0; i < viewModel.imageModelsList.size(); i++) {
                viewModel.imageModelsList.get(i).setDrawableId(Color.parseColor("#000000"));
                viewModel.imageModelsList.get(i).setColor(true);
                multiFitAdapter.notifyItemChanged(i);
            }

        });

        binding.btnDone.setOnClickListener(view -> {
            binding.backgroundContainer.recyclerViewColor.setVisibility(View.GONE);
            binding.backgroundContainer.recyclerViewGradient.setVisibility(View.GONE);
            binding.backgroundContainer.recyclerViewBlur.setVisibility(View.GONE);
            binding.backgroundContainer.backgroundTools.setVisibility(View.VISIBLE);
            binding.colorPickerView.setVisibility(View.GONE);
            binding.carouselView.setVisibility(View.VISIBLE);

            if (binding.colorPickerView.isSelected()){
                for (int i = 0; i < viewModel.imageModelsList.size(); i++) {
                    viewModel.imageModelsList.get(i).setDrawableId(binding.colorPickerView.getColor());
                    viewModel.imageModelsList.get(i).setColor(true);
                    multiFitAdapter.notifyItemChanged(i);
                }
            }
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

            dialog.setCancelable(false);

            ImageView collageView = dialog.findViewById(R.id.collageView);

            showImage(collageView);

            dialog.show();

        });

    }

    public void showImage(ImageView collageView) {
        BackgroundGridAdapter.SquareView squareView = viewModel.imageModelsList.get(currentSavePosition);
        collageView.setImageDrawable(viewModel.imageDrawableList.get(currentSavePosition));


        if (viewModel.imageModelsList.get(currentSavePosition).isColor) {
            collageView.setBackgroundColor(squareView.drawableId);
            collageView.post(() -> {
                if (currentSavePosition == (viewModel.imageDrawableList.size() - 1)) {
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
                        if (currentSavePosition == (viewModel.imageDrawableList.size() - 1)) {
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
                if (currentSavePosition == (viewModel.imageDrawableList.size() - 1)) {
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
            } else {
                currentSavePosition++;
                showImage(imageView);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initFilterView() {
        binding.imageViewSaveFilter.setOnClickListener(view -> {
            viewModel.rvPrimaryToolShow();
        });


        binding.recyclerViewFilterBW.setVisibility(View.GONE);
        binding.recyclerViewFilterVintage.setVisibility(View.GONE);
        binding.recyclerViewFilterSmooth.setVisibility(View.GONE);
        binding.recyclerViewFilterCold.setVisibility(View.GONE);
        binding.recyclerViewFilterWarm.setVisibility(View.GONE);
        binding.recyclerViewFilterLegacy.setVisibility(View.GONE);

        binding.linearLayoutAll.setOnClickListener(view -> viewModel.filterAllShow());
        binding.linearLayoutSmooth.setOnClickListener(view -> viewModel.filterSmoothShow());
        binding.linearLayoutBW.setOnClickListener(view -> viewModel.filterBWShow());
        binding.linearLayoutVintage.setOnClickListener(view -> viewModel.filterVintageShow());
        binding.linearLayoutCold.setOnClickListener(view -> viewModel.filterColdShow());
        binding.linearLayoutWarm.setOnClickListener(view -> viewModel.filterWarmShow());
        binding.linearLayoutLegacy.setOnClickListener(view -> viewModel.filterLegacyShow());
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
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
//                firstVisibleItem = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
            }
        });

    }

    public void setFilter() {

        drawableList.clear();
        if (viewModel.imageDrawableList.size() != 0){
            for (String imagePath : viewModel.imageList) {
                drawableList.add(new BitmapDrawable(getResources(), imagePath));
            }
        }

        viewModel.filterAllShow();
        new allFilters().execute();
        new bwFilters().execute();
        new vintageFilters().execute();
        new smoothFilters().execute();
        new coldFilters().execute();
        new warmFilters().execute();
        new legacyFilters().execute();

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


        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < viewModel.imageDrawableList.size(); i++) {
            Drawable d = viewModel.imageDrawableList.get(i);
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
        binding.carouselView.setVisibility(View.GONE);
        binding.colorPickerView.setVisibility(View.VISIBLE);

        int viewX = binding.colorPickerView.getLeft() + (binding.colorPickerView.getRight() - binding.colorPickerView.getLeft()) / 2;
        int viewY = binding.colorPickerView.getTop() + (binding.colorPickerView.getBottom() - binding.colorPickerView.getTop()) / 2;
        binding.colorPickerView.setSelectorPoint(viewX, viewY);


        binding.colorPickerView.setPaletteDrawable(viewModel.imageDrawableList.get(firstVisibleItem));
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
        for (int i = 0; i < viewModel.imageModelsList.size(); i++) {
            viewModel.imageModelsList.set(i, squareView);
            multiFitAdapter.notifyItemChanged(i);
        }

    }

    @Override
    public void onBackPressed() {

        if (binding.backgroundContainer.recyclerViewColor.getVisibility() == View.VISIBLE || binding.backgroundContainer.recyclerViewGradient.getVisibility() == View.VISIBLE || binding.backgroundContainer.recyclerViewBlur.getVisibility() == View.VISIBLE || binding.colorPickerView.getVisibility() == View.VISIBLE) {
            binding.backgroundContainer.recyclerViewColor.setVisibility(View.GONE);
            binding.backgroundContainer.recyclerViewGradient.setVisibility(View.GONE);
            binding.backgroundContainer.recyclerViewBlur.setVisibility(View.GONE);
            binding.colorPickerView.setVisibility(View.GONE);
            binding.backgroundContainer.backgroundTools.setVisibility(View.VISIBLE);
            binding.carouselView.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }

    }


    class allFilters extends AsyncTask<Void, Void, Void> {
        public void onPreExecute() {
            setLoading(true);
        }

        @SuppressLint("WrongThread")
        public Void doInBackground(Void... voidArr) {
            listFilterAll.clear();
            listFilterAll.addAll(FilterCodeAsset.getListBitmapFilterAll(ThumbnailUtils.extractThumbnail(new BitmapDrawable(getResources(), viewModel.imageList.get(0)).getBitmap(), 70, 70)));
            return null;
        }

        public void onPostExecute(Void voidR) {
            binding.recyclerViewFilterAll.setAdapter(new FilterAdapter(listFilterAll, MultiFitActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.ALL_FILTERS)));
            binding.constraintLayoutFilterLayout.setVisibility(View.VISIBLE);
            setLoading(false);
        }
    }

    class bwFilters extends AsyncTask<Void, Void, Void> {
        public void onPreExecute() {
            setLoading(true);
        }

        @SuppressLint("WrongThread")
        public Void doInBackground(Void... voidArr) {
            listFilterBW.clear();
            listFilterBW.addAll(FilterCodeAsset.getListBitmapFilterBW(ThumbnailUtils.extractThumbnail(new BitmapDrawable(getResources(), viewModel.imageList.get(0)).getBitmap(), 70, 70)));
            return null;
        }

        public void onPostExecute(Void voidR) {
            binding.recyclerViewFilterBW.setAdapter(new FilterAdapter(listFilterBW, MultiFitActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.BW_FILTERS)));
            setLoading(false);
        }
    }

    class vintageFilters extends AsyncTask<Void, Void, Void> {
        public void onPreExecute() {
            setLoading(true);
        }

        @SuppressLint("WrongThread")
        public Void doInBackground(Void... voidArr) {
            listFilterVintage.clear();
            listFilterVintage.addAll(FilterCodeAsset.getListBitmapFilterVintage(ThumbnailUtils.extractThumbnail(new BitmapDrawable(getResources(), viewModel.imageList.get(0)).getBitmap(), 70, 70)));
            return null;
        }

        public void onPostExecute(Void voidR) {
            binding.recyclerViewFilterVintage.setAdapter(new FilterAdapter(listFilterVintage, MultiFitActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.VINTAGE_FILTERS)));
            setLoading(false);
        }
    }

    class smoothFilters extends AsyncTask<Void, Void, Void> {
        public void onPreExecute() {
            setLoading(true);
        }

        @SuppressLint("WrongThread")
        public Void doInBackground(Void... voidArr) {
            listFilterSmooth.clear();
            listFilterSmooth.addAll(FilterCodeAsset.getListBitmapFilterSmooth(ThumbnailUtils.extractThumbnail(new BitmapDrawable(getResources(), viewModel.imageList.get(0)).getBitmap(), 70, 70)));
            return null;
        }

        public void onPostExecute(Void voidR) {
            binding.recyclerViewFilterSmooth.setAdapter(new FilterAdapter(listFilterSmooth, MultiFitActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.SMOOTH_FILTERS)));

            setLoading(false);
        }
    }

    class coldFilters extends AsyncTask<Void, Void, Void> {
        public void onPreExecute() {
            setLoading(true);
        }

        @SuppressLint("WrongThread")
        public Void doInBackground(Void... voidArr) {
            listFilterCold.clear();
            listFilterCold.addAll(FilterCodeAsset.getListBitmapFilterCold(ThumbnailUtils.extractThumbnail(new BitmapDrawable(getResources(), viewModel.imageList.get(0)).getBitmap(), 70, 70)));
            return null;
        }

        public void onPostExecute(Void voidR) {
            binding.recyclerViewFilterCold.setAdapter(new FilterAdapter(listFilterCold, MultiFitActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.COLD_FILTERS)));

            setLoading(false);
        }
    }

    class warmFilters extends AsyncTask<Void, Void, Void> {
        public void onPreExecute() {
            setLoading(true);
        }

        @SuppressLint("WrongThread")
        public Void doInBackground(Void... voidArr) {
            listFilterWarm.clear();
            listFilterWarm.addAll(FilterCodeAsset.getListBitmapFilterWarm(ThumbnailUtils.extractThumbnail(new BitmapDrawable(getResources(), viewModel.imageList.get(0)).getBitmap(), 70, 70)));
            return null;
        }

        public void onPostExecute(Void voidR) {
            binding.recyclerViewFilterWarm.setAdapter(new FilterAdapter(listFilterWarm, MultiFitActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.WARM_FILTERS)));

            setLoading(false);
        }
    }

    class legacyFilters extends AsyncTask<Void, Void, Void> {
        public void onPreExecute() {
            setLoading(true);
        }

        @SuppressLint("WrongThread")
        public Void doInBackground(Void... voidArr) {
            listFilterLegacy.clear();
            listFilterLegacy.addAll(FilterCodeAsset.getListBitmapFilterLegacy(ThumbnailUtils.extractThumbnail(new BitmapDrawable(getResources(), viewModel.imageList.get(0)).getBitmap(), 70, 70)));
            return null;
        }

        public void onPostExecute(Void voidR) {
            binding.recyclerViewFilterLegacy.setAdapter(new FilterAdapter(listFilterLegacy, MultiFitActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.LEGACY_FILTERS)));
            setLoading(false);
        }
    }

    class LoadBitmapWithFilter extends AsyncTask<String, List<Bitmap>, List<Bitmap>> {
        LoadBitmapWithFilter() {
        }

        public void onPreExecute() {
            setLoading(true);
        }

        public List<Bitmap> doInBackground(String... strArr) {
            ArrayList arrayList = new ArrayList();
            for (Drawable drawable : drawableList) {
                arrayList.add(FilterUtils.getBitmapWithFilter(((BitmapDrawable) drawable).getBitmap(), strArr[0]));
            }
            return arrayList;
        }


        public void onPostExecute(List<Bitmap> list) {
            for (int i = 0; i < list.size(); i++) {
                BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), list.get(i));
                bitmapDrawable.setAntiAlias(true);
                bitmapDrawable.setFilterBitmap(true);

                viewModel.imageDrawableList.set(i, bitmapDrawable);
                multiFitAdapter.notifyItemChanged(i);
            }
            setLoading(false);
        }
    }

    @Override
    public void onFilterSelected(String str) {
        new LoadBitmapWithFilter().execute(str);
    }

}