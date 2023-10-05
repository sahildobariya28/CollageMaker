package com.photo.collagemaker.activities;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.photo.collagemaker.R;
import com.photo.collagemaker.adapters.AspectAdapter;
import com.photo.collagemaker.adapters.BackgroundGridAdapter;
import com.photo.collagemaker.adapters.FilterAdapter;
import com.photo.collagemaker.adapters.GridAdapter;
import com.photo.collagemaker.adapters.GridItemToolsAdapter;
import com.photo.collagemaker.adapters.GridToolsAdapter;
import com.photo.collagemaker.adapters.StickerAdapter;
import com.photo.collagemaker.adapters.StickerTabAdapter;
import com.photo.collagemaker.assets.EffectCodeAsset;
import com.photo.collagemaker.assets.FilterCodeAsset;
import com.photo.collagemaker.assets.StickersAsset;
import com.photo.collagemaker.databinding.ActivityGridBinding;
import com.photo.collagemaker.event.AlignHorizontallyEvent;
import com.photo.collagemaker.event.DeleteIconEvent;
import com.photo.collagemaker.event.FlipHorizontallyEvent;
import com.photo.collagemaker.event.ZoomIconEvent;
import com.photo.collagemaker.fragment.BurnFragment;
import com.photo.collagemaker.fragment.CropFragment;
import com.photo.collagemaker.fragment.DivideFragment;
import com.photo.collagemaker.fragment.DodgeFragment;
import com.photo.collagemaker.fragment.FilterFragment;
import com.photo.collagemaker.fragment.RotateFragment;
import com.photo.collagemaker.grid.QueShotGrid;
import com.photo.collagemaker.grid.QueShotLayout;
import com.photo.collagemaker.grid.QueShotLayoutParser;
import com.photo.collagemaker.listener.FilterListener;
import com.photo.collagemaker.module.Module;
import com.photo.collagemaker.picker.PermissionsUtils;
import com.photo.collagemaker.queshot.QueShotStickerIcons;
import com.photo.collagemaker.queshot.QueShotStickerView;
import com.photo.collagemaker.queshot.QueShotTextView;
import com.photo.collagemaker.sticker.DrawableSticker;
import com.photo.collagemaker.sticker.Sticker;
import com.photo.collagemaker.utils.CollageUtils;
import com.photo.collagemaker.utils.FilterUtils;
import com.photo.collagemaker.utils.SaveFileUtils;
import com.photo.collagemaker.utils.SystemUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.steelkiwi.cropiwa.AspectRatio;

import org.jetbrains.annotations.NotNull;
import org.wysaid.nativePort.CGENativeLibrary;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@SuppressLint("StaticFieldLeak")
public class QueShotGridActivity extends QueShotBaseActivity implements GridToolsAdapter.OnItemSelected,
        AspectAdapter.OnNewSelectedListener, StickerAdapter.OnClickSplashListener,
        BackgroundGridAdapter.BackgroundGridListener, FilterListener, CropFragment.OnCropPhoto,
        RotateFragment.OnCropPhoto, FilterFragment.OnFilterSavePhoto, DodgeFragment.OnFilterSavePhoto,
        DivideFragment.OnFilterSavePhoto, BurnFragment.OnFilterSavePhoto, GridItemToolsAdapter.OnPieceFuncItemSelected,
        GridAdapter.OnItemClickListener {


    private static QueShotGridActivity QueShotGridActivityInstance;
    public static QueShotGridActivity QueShotGridActivityCollage;


    public AspectRatio aspectRatio;
    public BackgroundGridAdapter.SquareView currentBackgroundState;
    public QueShotLayout queShotLayout;
    public GridToolsAdapter gridToolsAdapter = new GridToolsAdapter(this, true);
    private GridItemToolsAdapter gridItemToolsAdapter = new GridItemToolsAdapter(this);

    public Module moduleToolsId;
    public float BorderRadius;
    public float Padding;
    private int deviceHeight = 0;
    public int deviceWidth = 0;

    // ArrayList
    public ArrayList listFilterAll = new ArrayList<>();
    public ArrayList listFilterBW = new ArrayList<>();
    public ArrayList listFilterVintage = new ArrayList<>();
    public ArrayList listFilterSmooth = new ArrayList<>();
    public ArrayList listFilterCold = new ArrayList<>();
    public ArrayList listFilterWarm = new ArrayList<>();
    public ArrayList listFilterLegacy = new ArrayList<>();
    public List<Drawable> drawableList = new ArrayList<>();
    public List<String> stringList;
    public List<Target> targets = new ArrayList();

    ActivityGridBinding binding;

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        binding = ActivityGridBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        if (Build.VERSION.SDK_INT < 30) {
            getWindow().setSoftInputMode(72);
        }
        deviceWidth = getResources().getDisplayMetrics().widthPixels;
        deviceHeight = getResources().getDisplayMetrics().heightPixels;

        binding.imageViewExit.setOnClickListener(view -> onBackPressed());

        binding.recyclerViewTools.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recyclerViewTools.setAdapter(gridToolsAdapter);
        binding.recyclerViewToolsCollage.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recyclerViewToolsCollage.setAdapter(gridItemToolsAdapter);

        binding.seekbarBorder.setOnSeekBarChangeListener(onSeekBarChangeListener);


        binding.seekbarRadius.setOnSeekBarChangeListener(onSeekBarChangeListener);
        stringList = getIntent().getStringArrayListExtra(MultipleImagePickerActivity.KEY_DATA_RESULT);

        binding.recyclerViewFilterBw.setVisibility(View.GONE);
        binding.recyclerViewFilterVintage.setVisibility(View.GONE);
        binding.recyclerViewFilterSmooth.setVisibility(View.GONE);
        binding.recyclerViewFilterCold.setVisibility(View.GONE);
        binding.recyclerViewFilterWarm.setVisibility(View.GONE);
        binding.recyclerViewFilterLegacy.setVisibility(View.GONE);


        queShotLayout = CollageUtils.getCollageLayouts(stringList.size()).get(0);
        binding.collageView.setQueShotLayout(queShotLayout);
        binding.collageView.setTouchEnable(true);
        binding.collageView.setNeedDrawLine(false);
        binding.collageView.setNeedDrawOuterLine(false);
        binding.collageView.setLineSize(4);
        binding.collageView.setCollagePadding(6.0f);
        binding.collageView.setCollageRadian(15.0f);
        binding.collageView.setLineColor(ContextCompat.getColor(this, R.color.itemColorBlack));
        binding.collageView.setSelectedLineColor(ContextCompat.getColor(this, R.color.mainColor));
        binding.collageView.setHandleBarColor(ContextCompat.getColor(this, R.color.mainColor));
        binding.collageView.setAnimateDuration(300);

        binding.collageView.setOnQueShotSelectedListener((collage, i) -> {
            binding.recyclerViewTools.setVisibility(View.GONE);
            binding.imageToolContainer.setVisibility(View.VISIBLE);
            slideUp(binding.recyclerViewToolsCollage);
            setGoneSave();
//            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) binding.recyclerViewToolsCollage.getLayoutParams();
//            layoutParams.bottomMargin = SystemUtil.dpToPx(getApplicationContext(), 10);
//            binding.recyclerViewToolsCollage.setLayoutParams(layoutParams);
            moduleToolsId = Module.COLLAGE;
        });
        binding.collageView.setOnQueShotUnSelectedListener(() -> {
            binding.imageToolContainer.setVisibility(View.GONE);
            binding.recyclerViewTools.setVisibility(View.VISIBLE);
            setVisibleSave();
//            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) binding.recyclerViewToolsCollage.getLayoutParams();
//            layoutParams.bottomMargin = 0;
//            binding.recyclerViewToolsCollage.setLayoutParams(layoutParams);
            moduleToolsId = Module.NONE;
        });

        binding.collageView.post(this::loadPhoto);
        binding.linearLayoutAll.setOnClickListener(view -> setAllFilter());
        binding.linearLayoutBW.setOnClickListener(view -> setBwFilter());
        binding.linearLayoutVintage.setOnClickListener(view -> setVintageFilter());
        binding.linearLayoutSmooth.setOnClickListener(view -> setSmoothFilter());
        binding.linearLayoutCold.setOnClickListener(view -> setColdFilter());
        binding.linearLayoutWarm.setOnClickListener(view -> setWarmFilter());
        binding.linearLayoutLegacy.setOnClickListener(view -> setLegacyFilter());

        binding.btnDownArrow.setOnClickListener(view -> {
            binding.imageToolContainer.setVisibility(View.GONE);
            binding.recyclerViewTools.setVisibility(View.VISIBLE);
        });

        binding.imageViewSaveLayer.setOnClickListener(view -> {
            setGuideLineTools();
            binding.constrantLayoutChangeLayout.setVisibility(View.GONE);
            binding.recyclerViewTools.setVisibility(View.VISIBLE);
            setVisibleSave();
            binding.imageToolContainer.setVisibility(View.GONE);
            queShotLayout = binding.collageView.getQueShotLayout();
            BorderRadius = binding.collageView.getCollageRadian();
            Padding = binding.collageView.getCollagePadding();
            binding.collageView.setLocked(true);
            binding.collageView.setTouchEnable(true);
            aspectRatio = binding.collageView.getAspectRatio();
            moduleToolsId = Module.NONE;

            binding.backgroundContainer.backgroundTools.setVisibility(View.VISIBLE);
            binding.backgroundContainer.recyclerViewColor.setVisibility(View.GONE);
            binding.backgroundContainer.recyclerViewBlur.setVisibility(View.GONE);
            binding.backgroundContainer.recyclerViewGradient.setVisibility(View.GONE);
            binding.backgroundContainer.constrantLayoutChangeBackground.setVisibility(View.GONE);
            if (binding.collageView.getBackgroundResourceMode() == 0) {
                currentBackgroundState.isColor = true;
                currentBackgroundState.isBitmap = false;
                currentBackgroundState.drawableId = ((ColorDrawable) binding.collageView.getBackground()).getColor();
                currentBackgroundState.drawable = null;
            } else if (binding.collageView.getBackgroundResourceMode() == 1) {
                currentBackgroundState.isColor = false;
                currentBackgroundState.isBitmap = false;
                currentBackgroundState.drawable = binding.collageView.getBackground();
            } else {
                currentBackgroundState.isColor = false;
                currentBackgroundState.isBitmap = true;
                currentBackgroundState.drawable = binding.collageView.getBackground();
            }
        });
        binding.imageViewCloseLayer.setOnClickListener(view -> {
            setVisibleSave();
            onBackPressed();
        });

        binding.imageViewCloseSticker.setOnClickListener(view -> {
            setVisibleSave();
            onBackPressed();
        });
        binding.imageViewSaveFilter.setOnClickListener(view -> {
            setGuideLineTools();
            binding.constraintLayoutFilterLayout.setVisibility(View.GONE);
            binding.constraintLayoutConfirmSaveFilter.setVisibility(View.GONE);
            binding.recyclerViewTools.setVisibility(View.VISIBLE);
            binding.imageToolContainer.setVisibility(View.GONE);
            moduleToolsId = Module.NONE;
        });
//        binding.backgroundContainer.imageViewSavebackground.setOnClickListener(view -> {
//            setGuideLineTools();
//            binding.backgroundContainer.constrantLayoutChangeBackground.setVisibility(View.GONE);
//            binding.recyclerViewTools.setVisibility(View.VISIBLE);
//            binding.imageToolContainer.setVisibility(View.GONE);
//            setVisibleSave();
//            binding.collageView.setLocked(true);
//            binding.collageView.setTouchEnable(true);
//            if (binding.collageView.getBackgroundResourceMode() == 0) {
//                currentBackgroundState.isColor = true;
//                currentBackgroundState.isBitmap = false;
//                currentBackgroundState.drawableId = ((ColorDrawable) binding.collageView.getBackground()).getColor();
//                currentBackgroundState.drawable = null;
//            } else if (binding.collageView.getBackgroundResourceMode() == 1) {
//                currentBackgroundState.isColor = false;
//                currentBackgroundState.isBitmap = false;
//                currentBackgroundState.drawable = binding.collageView.getBackground();
//            } else {
//                currentBackgroundState.isColor = false;
//                currentBackgroundState.isBitmap = true;
//                currentBackgroundState.drawable = binding.collageView.getBackground();
//            }
//            moduleToolsId = Module.NONE;
//        });
        binding.imageViewSaveSticker.setOnClickListener(view -> {
            setGuideLineTools();
            binding.collageView.setHandlingSticker(null);
            binding.relativeLayoutAddSticker.setVisibility(View.GONE);
            binding.constraintLayoutSticker.setVisibility(View.GONE);
            binding.linearLayoutWrapperStickerList.setVisibility(View.VISIBLE);
            binding.recyclerViewTools.setVisibility(View.VISIBLE);
            binding.imageToolContainer.setVisibility(View.GONE);

            setVisibleSave();
            binding.collageView.setLocked(true);
            binding.collageView.setTouchEnable(true);
            moduleToolsId = Module.NONE;

        });
        binding.imageViewCloseFilter.setOnClickListener(view -> {
            setVisibleSave();
            onBackPressed();
        });

        binding.linearLayoutCollage.setOnClickListener(view -> setLayer());
        binding.linearLayoutBorder.setOnClickListener(view -> setBorder());
        binding.linearLayoutRatio.setOnClickListener(view -> setRatio());
        binding.linearLayoutBackground.setOnClickListener(view -> setBackground());

        binding.backgroundContainer.btnColor.setOnClickListener(view -> setBackgroundColor());
        binding.backgroundContainer.btnGradient.setOnClickListener(view -> setBackgroundGradient());
        binding.backgroundContainer.btnBlur.setOnClickListener(view -> selectBackgroundBlur());
        binding.backgroundContainer.btnWhite.setOnClickListener(view -> selectBackgroundBlur());
        binding.backgroundContainer.btnBlack.setOnClickListener(view -> selectBackgroundBlur());


        GridAdapter collageAdapter = new GridAdapter();

        binding.recyclerViewCollage.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recyclerViewCollage.setAdapter(collageAdapter);
        collageAdapter.refreshData(CollageUtils.getCollageLayouts(stringList.size()), null);
        collageAdapter.setOnItemClickListener(this);
        AspectAdapter aspectRatioPreviewAdapter = new AspectAdapter(this);
        aspectRatioPreviewAdapter.setListener(this);

        binding.recyclerViewRatio.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recyclerViewRatio.setAdapter(aspectRatioPreviewAdapter);
        binding.textViewSave.setOnClickListener(view -> {
            if (PermissionsUtils.checkWriteStoragePermission(QueShotGridActivity.this)) {
                Bitmap createBitmap = SaveFileUtils.createBitmap(binding.collageView, 1920);
                Bitmap createBitmap2 = binding.collageView.createBitmap();
                new SaveCollageAsFile().execute(createBitmap, createBitmap2);
            }

        });

        binding.relativeLayoutAddSticker.setVisibility(View.GONE);
        binding.relativeLayoutAddSticker.setOnClickListener(view -> {
            binding.relativeLayoutAddSticker.setVisibility(View.GONE);
            binding.linearLayoutWrapperStickerList.setVisibility(View.VISIBLE);
        });
        QueShotStickerIcons quShotStickerIconClose = new QueShotStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_close), 0, QueShotStickerIcons.DELETE);
        quShotStickerIconClose.setIconEvent(new DeleteIconEvent());
        QueShotStickerIcons quShotStickerIconScale = new QueShotStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_scale), 3, QueShotStickerIcons.SCALE);
        quShotStickerIconScale.setIconEvent(new ZoomIconEvent());
        QueShotStickerIcons quShotStickerIconFlip = new QueShotStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_flip), 1, QueShotStickerIcons.FLIP);
        quShotStickerIconFlip.setIconEvent(new FlipHorizontallyEvent());
        QueShotStickerIcons quShotStickerIconCenter = new QueShotStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_center), 2, QueShotStickerIcons.ALIGN);
        quShotStickerIconCenter.setIconEvent(new AlignHorizontallyEvent());
        binding.collageView.setIcons(Arrays.asList(quShotStickerIconClose, quShotStickerIconScale, quShotStickerIconFlip, quShotStickerIconCenter));
        binding.collageView.setConstrained(true);
        binding.collageView.setOnStickerOperationListener(onStickerOperationListener);
        binding.stickerViewpaper.setAdapter(new PagerAdapter() {
            public int getCount() {
                return 3;
            }

            public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
                return view.equals(obj);
            }

            @Override
            public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
                (container).removeView((View) object);

            }

            @NonNull
            public Object instantiateItem(@NonNull ViewGroup viewGroup, int i) {
                View inflate = LayoutInflater.from(getBaseContext()).inflate(R.layout.list_women_beauty, null, false);
                RecyclerView recycler_view_sticker = inflate.findViewById(R.id.recyclerViewSticker);
                recycler_view_sticker.setHasFixedSize(true);
                recycler_view_sticker.setLayoutManager(new GridLayoutManager(getApplicationContext(), 7));
                switch (i) {
                    case 0:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), StickersAsset.mListEmojy(), i, QueShotGridActivity.this));
                        break;
                    case 1:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), StickersAsset.mListFlag(), i, QueShotGridActivity.this));
                        break;
                    case 2:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), StickersAsset.mListBoom(), i, QueShotGridActivity.this));
                        break;

                }

                viewGroup.addView(inflate);
                return inflate;
            }
        });
        binding.recyclerTabLayout.setUpWithAdapter(new StickerTabAdapter(binding.stickerViewpaper, getApplicationContext()));
        binding.recyclerTabLayout.setPositionThreshold(0.5f);
        binding.recyclerTabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.white));


        setLoading(false);

        currentBackgroundState = new BackgroundGridAdapter.SquareView(Color.parseColor("#ffffff"), "", true);

        binding.backgroundContainer.recyclerViewColor.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        binding.backgroundContainer.recyclerViewColor.setHasFixedSize(true);
        binding.backgroundContainer.recyclerViewColor.setAdapter(new BackgroundGridAdapter(getApplicationContext(), this));
        binding.backgroundContainer.recyclerViewGradient.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        binding.backgroundContainer.recyclerViewGradient.setHasFixedSize(true);
        binding.backgroundContainer.recyclerViewGradient.setAdapter(new BackgroundGridAdapter(getApplicationContext(), this, true));
        binding.backgroundContainer.recyclerViewBlur.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        binding.backgroundContainer.recyclerViewBlur.setHasFixedSize(true);
        binding.backgroundContainer.recyclerViewBlur.setAdapter(new BackgroundGridAdapter(getApplicationContext(), this, true));
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) binding.collageView.getLayoutParams();
        layoutParams.height = point.x;
        layoutParams.width = point.x;
        binding.collageView.setLayoutParams(layoutParams);
        aspectRatio = new AspectRatio(1, 1);
        binding.collageView.setAspectRatio(new AspectRatio(1, 1));
        QueShotGridActivityCollage = this;
        moduleToolsId = Module.NONE;
        CGENativeLibrary.setLoadImageCallback(loadImageCallback, null);
        QueShotGridActivityInstance = this;

        binding.recyclerViewToolsCollage.setAlpha(0.0f);
        binding.constrantLayoutChangeLayout.post(() -> slideDown(binding.recyclerViewToolsCollage));
        new Handler().postDelayed(() -> binding.recyclerViewToolsCollage.setAlpha(1.0f), 1000);

    }

    public void onDestroy() {
        super.onDestroy();
        try {
            binding.collageView.reset();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void onPause() {
        super.onPause();
    }

    public void onResume() {
        super.onResume();
    }

    public void slideDown(View view) {
        ObjectAnimator.ofFloat(view, "translationY", 0.0f, (float) view.getHeight()).start();
    }


    public void slideUp(View view) {
        ObjectAnimator.ofFloat(view, "translationY", new float[]{(float) view.getHeight(), 0.0f}).start();
    }


    public SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
        }

        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            int id = seekBar.getId();
            if (id == R.id.seekbar_border) {
                binding.collageView.setCollagePadding((float) i);
            } else if (id == R.id.seekbar_radius) {
                binding.collageView.setCollageRadian((float) i);
            }
            binding.collageView.invalidate();
        }
    };
    QueShotStickerView.OnStickerOperationListener onStickerOperationListener = new QueShotStickerView.OnStickerOperationListener() {
        public void onStickerDrag(@NonNull Sticker sticker) {
        }

        public void onStickerFlip(@NonNull Sticker sticker) {
        }

        public void onStickerTouchedDown(@NonNull Sticker sticker) {
        }

        public void onStickerZoom(@NonNull Sticker sticker) {
        }

        public void onTouchDownBeauty(float f, float f2) {
        }

        public void onTouchDragBeauty(float f, float f2) {
        }

        public void onTouchUpBeauty(float f, float f2) {
        }

        public void onAddSticker(@NonNull Sticker sticker) {

        }

        public void onStickerSelected(@NonNull Sticker sticker) {

        }

        public void onStickerDeleted(@NonNull Sticker sticker) {

        }

        public void onStickerTouchOutside() {

        }

        public void onStickerDoubleTap(@NonNull Sticker sticker) {
            if (sticker instanceof QueShotTextView) {
                sticker.setShow(false);
                binding.collageView.setHandlingSticker(null);
            }
        }
    };


    public static QueShotGridActivity getQueShotGridActivityInstance() {
        return QueShotGridActivityInstance;
    }

    public CGENativeLibrary.LoadImageCallback loadImageCallback = new CGENativeLibrary.LoadImageCallback() {
        public Bitmap loadImage(String string, Object object) {
            try {
                return BitmapFactory.decodeStream(getAssets().open(string));
            } catch (IOException ioException) {
                return null;
            }
        }

        public void loadImageOK(Bitmap bitmap, Object object) {
            bitmap.recycle();
        }
    };

    public void setAllFilter() {
        binding.recyclerViewFilterAll.setVisibility(View.VISIBLE);
        binding.recyclerViewFilterBw.setVisibility(View.GONE);
        binding.recyclerViewFilterVintage.setVisibility(View.GONE);
        binding.recyclerViewFilterSmooth.setVisibility(View.GONE);
        binding.recyclerViewFilterCold.setVisibility(View.GONE);
        binding.recyclerViewFilterWarm.setVisibility(View.GONE);
        binding.recyclerViewFilterLegacy.setVisibility(View.GONE);

        binding.viewAll.setVisibility(View.VISIBLE);
        binding.viewCold.setVisibility(View.INVISIBLE);
        binding.viewBw.setVisibility(View.INVISIBLE);
        binding.viewVintage.setVisibility(View.INVISIBLE);
        binding.viewSmooth.setVisibility(View.INVISIBLE);
        binding.viewWarm.setVisibility(View.INVISIBLE);
        binding.viewLegacy.setVisibility(View.INVISIBLE);
    }

    public void setBwFilter() {
        binding.recyclerViewFilterAll.setVisibility(View.GONE);
        binding.recyclerViewFilterBw.setVisibility(View.VISIBLE);
        binding.recyclerViewFilterVintage.setVisibility(View.GONE);
        binding.recyclerViewFilterSmooth.setVisibility(View.GONE);
        binding.recyclerViewFilterCold.setVisibility(View.GONE);
        binding.recyclerViewFilterWarm.setVisibility(View.GONE);
        binding.recyclerViewFilterLegacy.setVisibility(View.GONE);
        binding.viewAll.setVisibility(View.INVISIBLE);
        binding.viewCold.setVisibility(View.INVISIBLE);
        binding.viewBw.setVisibility(View.VISIBLE);
        binding.viewVintage.setVisibility(View.INVISIBLE);
        binding.viewSmooth.setVisibility(View.INVISIBLE);
        binding.viewWarm.setVisibility(View.INVISIBLE);
        binding.viewLegacy.setVisibility(View.INVISIBLE);
    }

    public void setVintageFilter() {
        binding.recyclerViewFilterAll.setVisibility(View.GONE);
        binding.recyclerViewFilterBw.setVisibility(View.GONE);
        binding.recyclerViewFilterVintage.setVisibility(View.VISIBLE);
        binding.recyclerViewFilterSmooth.setVisibility(View.GONE);
        binding.recyclerViewFilterCold.setVisibility(View.GONE);
        binding.recyclerViewFilterWarm.setVisibility(View.GONE);
        binding.recyclerViewFilterLegacy.setVisibility(View.GONE);
        binding.viewAll.setVisibility(View.INVISIBLE);
        binding.viewCold.setVisibility(View.INVISIBLE);
        binding.viewBw.setVisibility(View.INVISIBLE);
        binding.viewVintage.setVisibility(View.VISIBLE);
        binding.viewSmooth.setVisibility(View.INVISIBLE);
        binding.viewWarm.setVisibility(View.INVISIBLE);
        binding.viewLegacy.setVisibility(View.INVISIBLE);
    }

    public void setSmoothFilter() {
        binding.recyclerViewFilterAll.setVisibility(View.GONE);
        binding.recyclerViewFilterBw.setVisibility(View.GONE);
        binding.recyclerViewFilterVintage.setVisibility(View.GONE);
        binding.recyclerViewFilterSmooth.setVisibility(View.VISIBLE);
        binding.recyclerViewFilterCold.setVisibility(View.GONE);
        binding.recyclerViewFilterWarm.setVisibility(View.GONE);
        binding.recyclerViewFilterLegacy.setVisibility(View.GONE);
        binding.viewAll.setVisibility(View.INVISIBLE);
        binding.viewCold.setVisibility(View.INVISIBLE);
        binding.viewBw.setVisibility(View.INVISIBLE);
        binding.viewVintage.setVisibility(View.INVISIBLE);
        binding.viewSmooth.setVisibility(View.VISIBLE);
        binding.viewWarm.setVisibility(View.INVISIBLE);
        binding.viewLegacy.setVisibility(View.INVISIBLE);
    }

    public void setColdFilter() {
        binding.recyclerViewFilterAll.setVisibility(View.GONE);
        binding.recyclerViewFilterBw.setVisibility(View.GONE);
        binding.recyclerViewFilterVintage.setVisibility(View.GONE);
        binding.recyclerViewFilterSmooth.setVisibility(View.GONE);
        binding.recyclerViewFilterCold.setVisibility(View.VISIBLE);
        binding.recyclerViewFilterWarm.setVisibility(View.GONE);
        binding.recyclerViewFilterLegacy.setVisibility(View.GONE);
        binding.viewAll.setVisibility(View.INVISIBLE);
        binding.viewCold.setVisibility(View.VISIBLE);
        binding.viewBw.setVisibility(View.INVISIBLE);
        binding.viewVintage.setVisibility(View.INVISIBLE);
        binding.viewSmooth.setVisibility(View.INVISIBLE);
        binding.viewWarm.setVisibility(View.INVISIBLE);
        binding.viewLegacy.setVisibility(View.INVISIBLE);
    }

    public void setWarmFilter() {
        binding.recyclerViewFilterAll.setVisibility(View.GONE);
        binding.recyclerViewFilterBw.setVisibility(View.GONE);
        binding.recyclerViewFilterVintage.setVisibility(View.GONE);
        binding.recyclerViewFilterSmooth.setVisibility(View.GONE);
        binding.recyclerViewFilterCold.setVisibility(View.GONE);
        binding.recyclerViewFilterWarm.setVisibility(View.VISIBLE);
        binding.recyclerViewFilterLegacy.setVisibility(View.GONE);
        binding.viewAll.setVisibility(View.INVISIBLE);
        binding.viewCold.setVisibility(View.INVISIBLE);
        binding.viewBw.setVisibility(View.INVISIBLE);
        binding.viewVintage.setVisibility(View.INVISIBLE);
        binding.viewSmooth.setVisibility(View.INVISIBLE);
        binding.viewWarm.setVisibility(View.VISIBLE);
        binding.viewLegacy.setVisibility(View.INVISIBLE);
    }

    public void setLegacyFilter() {
        binding.recyclerViewFilterAll.setVisibility(View.GONE);
        binding.recyclerViewFilterBw.setVisibility(View.GONE);
        binding.recyclerViewFilterVintage.setVisibility(View.GONE);
        binding.recyclerViewFilterSmooth.setVisibility(View.GONE);
        binding.recyclerViewFilterCold.setVisibility(View.GONE);
        binding.recyclerViewFilterWarm.setVisibility(View.GONE);
        binding.recyclerViewFilterLegacy.setVisibility(View.VISIBLE);
        binding.viewAll.setVisibility(View.INVISIBLE);
        binding.viewCold.setVisibility(View.INVISIBLE);
        binding.viewBw.setVisibility(View.INVISIBLE);
        binding.viewVintage.setVisibility(View.INVISIBLE);
        binding.viewSmooth.setVisibility(View.INVISIBLE);
        binding.viewWarm.setVisibility(View.INVISIBLE);
        binding.viewLegacy.setVisibility(View.VISIBLE);
    }

    public void setBackgroundColor() {
        binding.backgroundContainer.backgroundTools.setVisibility(View.GONE);
        binding.backgroundContainer.recyclerViewColor.scrollToPosition(0);
        ((BackgroundGridAdapter) binding.backgroundContainer.recyclerViewColor.getAdapter()).setSelectedIndex(-1);
        binding.backgroundContainer.recyclerViewColor.getAdapter().notifyDataSetChanged();
        binding.backgroundContainer.recyclerViewColor.setVisibility(View.VISIBLE);
        binding.backgroundContainer.recyclerViewGradient.setVisibility(View.GONE);
        binding.backgroundContainer.recyclerViewBlur.setVisibility(View.GONE);
//        binding.backgroundContainer.viewColor.setVisibility(View.VISIBLE);
//        binding.backgroundContainer.viewGradient.setVisibility(View.INVISIBLE);
//        binding.backgroundContainer.viewBlur.setVisibility(View.INVISIBLE);
    }

    public void setBackgroundGradient() {
        binding.backgroundContainer.backgroundTools.setVisibility(View.GONE);

        binding.backgroundContainer.recyclerViewGradient.scrollToPosition(0);
        ((BackgroundGridAdapter) binding.backgroundContainer.recyclerViewGradient.getAdapter()).setSelectedIndex(-1);
        binding.backgroundContainer.recyclerViewGradient.getAdapter().notifyDataSetChanged();

        binding.backgroundContainer.recyclerViewColor.setVisibility(View.GONE);
        binding.backgroundContainer.recyclerViewGradient.setVisibility(View.VISIBLE);
        binding.backgroundContainer.recyclerViewBlur.setVisibility(View.GONE);
//        binding.backgroundContainer.viewColor.setVisibility(View.INVISIBLE);
//        binding.backgroundContainer.viewGradient.setVisibility(View.VISIBLE);
//        binding.backgroundContainer.viewBlur.setVisibility(View.INVISIBLE);
    }

    public void selectBackgroundBlur() {
        binding.backgroundContainer.backgroundTools.setVisibility(View.GONE);

        ArrayList arrayList = new ArrayList();
        for (QueShotGrid drawable : binding.collageView.getQueShotGrids()) {
            arrayList.add(drawable.getDrawable());
        }
        BackgroundGridAdapter backgroundGridAdapter = new BackgroundGridAdapter(getApplicationContext(), this, (List<Drawable>) arrayList);
        backgroundGridAdapter.setSelectedIndex(-1);
        binding.backgroundContainer.recyclerViewBlur.setAdapter(backgroundGridAdapter);
        binding.backgroundContainer.recyclerViewColor.setVisibility(View.GONE);
        binding.backgroundContainer.recyclerViewGradient.setVisibility(View.GONE);
        binding.backgroundContainer.recyclerViewBlur.setVisibility(View.VISIBLE);
//        binding.backgroundContainer.viewColor.setVisibility(View.INVISIBLE);
//        binding.backgroundContainer.viewGradient.setVisibility(View.INVISIBLE);
//        binding.backgroundContainer.viewBlur.setVisibility(View.VISIBLE);
    }


    public void setDown() {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(binding.constrantLayoutChangeLayout);
        //constraintSet.connect(binding.constraintLayoutWrapperCollageView.getId(), 3, adView.getId(), 4, 0);
        constraintSet.connect(binding.constraintLayoutWrapperCollageView.getId(), 1, binding.constrantLayoutChangeLayout.getId(), 1, 0);
        constraintSet.connect(binding.constraintLayoutWrapperCollageView.getId(), 4, binding.guidelineTool.getId(), 3, 0);
        constraintSet.connect(binding.constraintLayoutWrapperCollageView.getId(), 2, binding.constrantLayoutChangeLayout.getId(), 2, 0);
        constraintSet.applyTo(binding.constrantLayoutChangeLayout);
    }

    public void setLayer() {
        binding.recyclerViewCollage.setVisibility(View.VISIBLE);
        binding.recyclerViewRatio.setVisibility(View.GONE);
        binding.linearLayoutPadding.setVisibility(View.GONE);
        binding.backgroundContainer.constrantLayoutChangeBackground.setVisibility(View.GONE);
        binding.viewCollage.setVisibility(View.VISIBLE);
        binding.viewBorder.setVisibility(View.INVISIBLE);
        binding.viewRatio.setVisibility(View.INVISIBLE);
        binding.viewBackground.setVisibility(View.INVISIBLE);
    }

    public void setBorder() {
        binding.recyclerViewCollage.setVisibility(View.GONE);
        binding.recyclerViewRatio.setVisibility(View.GONE);
        binding.linearLayoutPadding.setVisibility(View.VISIBLE);
        binding.backgroundContainer.constrantLayoutChangeBackground.setVisibility(View.GONE);
        binding.viewCollage.setVisibility(View.INVISIBLE);
        binding.viewBorder.setVisibility(View.VISIBLE);
        binding.viewRatio.setVisibility(View.INVISIBLE);
        binding.viewBackground.setVisibility(View.INVISIBLE);
        binding.seekbarBorder.setProgress((int) binding.collageView.getCollagePadding());
        binding.seekbarRadius.setProgress((int) binding.collageView.getCollageRadian());
    }

    public void setRatio() {
        binding.recyclerViewCollage.setVisibility(View.GONE);
        binding.recyclerViewRatio.setVisibility(View.VISIBLE);
        binding.linearLayoutPadding.setVisibility(View.GONE);
        binding.backgroundContainer.constrantLayoutChangeBackground.setVisibility(View.GONE);
        binding.viewCollage.setVisibility(View.INVISIBLE);
        binding.viewBorder.setVisibility(View.INVISIBLE);
        binding.viewRatio.setVisibility(View.VISIBLE);
        binding.viewBackground.setVisibility(View.INVISIBLE);
    }

    public void setBackground() {
        binding.recyclerViewCollage.setVisibility(View.GONE);
        binding.recyclerViewRatio.setVisibility(View.GONE);
        binding.linearLayoutPadding.setVisibility(View.GONE);
        binding.backgroundContainer.constrantLayoutChangeBackground.setVisibility(View.VISIBLE);
        binding.backgroundContainer.backgroundTools.setVisibility(View.VISIBLE);
        binding.backgroundContainer.recyclerViewColor.setVisibility(View.GONE);
        binding.backgroundContainer.recyclerViewGradient.setVisibility(View.GONE);
        binding.backgroundContainer.recyclerViewBlur.setVisibility(View.GONE);
        binding.viewCollage.setVisibility(View.INVISIBLE);
        binding.viewBorder.setVisibility(View.INVISIBLE);
        binding.viewRatio.setVisibility(View.INVISIBLE);
        binding.viewBackground.setVisibility(View.VISIBLE);
    }

    public void setGuideLineTools() {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(binding.constrantLayoutChangeLayout);
        constraintSet.connect(binding.constraintLayoutWrapperCollageView.getId(), 1, binding.constrantLayoutChangeLayout.getId(), 1, 0);
        constraintSet.connect(binding.constraintLayoutWrapperCollageView.getId(), 4, binding.guidelineTool.getId(), 3, 0);
        constraintSet.connect(binding.constraintLayoutWrapperCollageView.getId(), 2, binding.constrantLayoutChangeLayout.getId(), 2, 0);
        constraintSet.applyTo(binding.constrantLayoutChangeLayout);
    }

    public void setGuideLine() {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(binding.constrantLayoutChangeLayout);
        constraintSet.connect(binding.constraintLayoutWrapperCollageView.getId(), 1, binding.constrantLayoutChangeLayout.getId(), 1, 0);
        constraintSet.connect(binding.constraintLayoutWrapperCollageView.getId(), 4, binding.guidelineLayout.getId(), 3, 0);
        constraintSet.connect(binding.constraintLayoutWrapperCollageView.getId(), 2, binding.constrantLayoutChangeLayout.getId(), 2, 0);
        constraintSet.applyTo(binding.constrantLayoutChangeLayout);
    }

    public void onToolSelected(Module module) {
        moduleToolsId = module;
        switch (module) {
            case LAYER:
                setLayer();
                setGuideLine();
                binding.constrantLayoutChangeLayout.setVisibility(View.VISIBLE);
                binding.recyclerViewTools.setVisibility(View.GONE);
                binding.imageToolContainer.setVisibility(View.GONE);
                queShotLayout = binding.collageView.getQueShotLayout();
                aspectRatio = binding.collageView.getAspectRatio();
                BorderRadius = binding.collageView.getCollageRadian();
                Padding = binding.collageView.getCollagePadding();
                binding.recyclerViewCollage.scrollToPosition(0);
                ((GridAdapter) binding.recyclerViewCollage.getAdapter()).setSelectedIndex(-1);
                binding.recyclerViewCollage.getAdapter().notifyDataSetChanged();
                binding.recyclerViewRatio.scrollToPosition(0);
                ((AspectAdapter) binding.recyclerViewRatio.getAdapter()).setLastSelectedView(-1);
                binding.recyclerViewRatio.getAdapter().notifyDataSetChanged();
                binding.collageView.setLocked(false);
                binding.collageView.setTouchEnable(false);
                setGoneSave();

            case PADDING:
                setBorder();
                setGuideLine();
                binding.constrantLayoutChangeLayout.setVisibility(View.VISIBLE);
                binding.recyclerViewTools.setVisibility(View.GONE);
                binding.imageToolContainer.setVisibility(View.GONE);
                queShotLayout = binding.collageView.getQueShotLayout();
                aspectRatio = binding.collageView.getAspectRatio();
                BorderRadius = binding.collageView.getCollageRadian();
                Padding = binding.collageView.getCollagePadding();
                binding.recyclerViewCollage.scrollToPosition(0);
                ((GridAdapter) binding.recyclerViewCollage.getAdapter()).setSelectedIndex(-1);
                binding.recyclerViewCollage.getAdapter().notifyDataSetChanged();
                binding.recyclerViewRatio.scrollToPosition(0);
                ((AspectAdapter) binding.recyclerViewRatio.getAdapter()).setLastSelectedView(-1);
                binding.recyclerViewRatio.getAdapter().notifyDataSetChanged();
                binding.collageView.setLocked(false);
                binding.collageView.setTouchEnable(false);
                setGoneSave();
                return;
            case RATIO:
                setRatio();
                setGuideLine();
                binding.constrantLayoutChangeLayout.setVisibility(View.VISIBLE);
                binding.recyclerViewTools.setVisibility(View.GONE);
                binding.imageToolContainer.setVisibility(View.GONE);
                queShotLayout = binding.collageView.getQueShotLayout();
                aspectRatio = binding.collageView.getAspectRatio();
                BorderRadius = binding.collageView.getCollageRadian();
                Padding = binding.collageView.getCollagePadding();
                binding.recyclerViewCollage.scrollToPosition(0);
                ((GridAdapter) binding.recyclerViewCollage.getAdapter()).setSelectedIndex(-1);
                binding.recyclerViewCollage.getAdapter().notifyDataSetChanged();
                binding.recyclerViewRatio.scrollToPosition(0);
                ((AspectAdapter) binding.recyclerViewRatio.getAdapter()).setLastSelectedView(-1);
                binding.recyclerViewRatio.getAdapter().notifyDataSetChanged();
                binding.collageView.setLocked(false);
                binding.collageView.setTouchEnable(false);
                setGoneSave();
                return;
            case FILTER:
                if (drawableList.isEmpty()) {
                    for (QueShotGrid drawable : binding.collageView.getQueShotGrids()) {
                        drawableList.add(drawable.getDrawable());
                    }
                }
                setAllFilter();
                new allFilters().execute();
                new bwFilters().execute();
                new vintageFilters().execute();
                new smoothFilters().execute();
                new coldFilters().execute();
                new warmFilters().execute();
                new legacyFilters().execute();
                setGoneSave();
                return;
            case STICKER:
                setGuideLine();
                binding.constrantLayoutChangeLayout.setVisibility(View.GONE);
                binding.constraintLayoutFilterLayout.setVisibility(View.GONE);
                binding.backgroundContainer.constrantLayoutChangeBackground.setVisibility(View.GONE);
                binding.recyclerViewTools.setVisibility(View.GONE);
                binding.imageToolContainer.setVisibility(View.GONE);
                binding.constraintLayoutSticker.setVisibility(View.VISIBLE);
                binding.collageView.updateLayout(queShotLayout);
                binding.collageView.setCollagePadding(Padding);
                binding.collageView.setCollageRadian(BorderRadius);
                getWindowManager().getDefaultDisplay().getSize(new Point());
                onNewAspectRatioSelected(aspectRatio);
                binding.collageView.setAspectRatio(aspectRatio);
                for (int i = 0; i < drawableList.size(); i++) {
                    binding.collageView.getQueShotGrids().get(i).setDrawable(drawableList.get(i));
                }
                binding.collageView.invalidate();
                if (currentBackgroundState.isColor) {
                    binding.collageView.setBackgroundResourceMode(0);
                    binding.collageView.setBackgroundColor(currentBackgroundState.drawableId);
                } else {
                    binding.collageView.setBackgroundResourceMode(1);
                    if (currentBackgroundState.drawable != null) {
                        binding.collageView.setBackground(currentBackgroundState.drawable);
                    } else {
                        binding.collageView.setBackgroundResource(currentBackgroundState.drawableId);
                    }
                }
                setGoneSave();
                binding.collageView.setLocked(false);
                binding.collageView.setTouchEnable(false);
                binding.linearLayoutWrapperStickerList.setVisibility(View.VISIBLE);
                return;
            case GRADIENT:
                setBackground();
                binding.constrantLayoutChangeLayout.setVisibility(View.VISIBLE);
                binding.recyclerViewTools.setVisibility(View.GONE);
                binding.imageToolContainer.setVisibility(View.GONE);
                queShotLayout = binding.collageView.getQueShotLayout();
                aspectRatio = binding.collageView.getAspectRatio();
                BorderRadius = binding.collageView.getCollageRadian();
                Padding = binding.collageView.getCollagePadding();
                binding.recyclerViewCollage.scrollToPosition(0);
                ((GridAdapter) binding.recyclerViewCollage.getAdapter()).setSelectedIndex(-1);
                binding.recyclerViewCollage.getAdapter().notifyDataSetChanged();
                binding.recyclerViewRatio.scrollToPosition(0);
                ((AspectAdapter) binding.recyclerViewRatio.getAdapter()).setLastSelectedView(-1);
                binding.recyclerViewRatio.getAdapter().notifyDataSetChanged();
                binding.collageView.setLocked(false);
                binding.collageView.setTouchEnable(false);
                setGoneSave();

//                setGuideLine();
//                binding.backgroundContainer.constrantLayoutChangeBackground.setVisibility(View.VISIBLE);
//                binding.recyclerViewTools.setVisibility(View.GONE);
//                binding.imageToolContainer.setVisibility(View.GONE);
//                binding.collageView.setLocked(false);
//                binding.collageView.setTouchEnable(false);
//                setGoneSave();
//                setBackgroundColor();
//                if (binding.collageView.getBackgroundResourceMode() == 0) {
//                    currentBackgroundState.isColor = true;
//                    currentBackgroundState.isBitmap = false;
//                    currentBackgroundState.drawableId = ((ColorDrawable) binding.collageView.getBackground()).getColor();
//                    return;
//                } else if (binding.collageView.getBackgroundResourceMode() == 2 || (binding.collageView.getBackground() instanceof ColorDrawable)) {
//                    currentBackgroundState.isBitmap = true;
//                    currentBackgroundState.isColor = false;
//                    currentBackgroundState.drawable = binding.collageView.getBackground();
//                    return;
//                } else if (binding.collageView.getBackground() instanceof GradientDrawable) {
//                    currentBackgroundState.isBitmap = false;
//                    currentBackgroundState.isColor = false;
//                    currentBackgroundState.drawable = binding.collageView.getBackground();
//                    return;
//                } else {
//                    return;
//                }
            default:
        }
    }


    public void loadPhoto() {
        final int i;
        final ArrayList arrayList = new ArrayList();
        if (stringList.size() > queShotLayout.getAreaCount()) {
            i = queShotLayout.getAreaCount();
        } else {
            i = stringList.size();
        }
        for (int i2 = 0; i2 < i; i2++) {
            Target r4 = new Target() {
                public void onBitmapFailed(Exception exc, Drawable drawable) {
                }

                public void onPrepareLoad(Drawable drawable) {
                }

                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                    int width = bitmap.getWidth();
                    float f = (float) width;
                    float height = (float) bitmap.getHeight();
                    float max = Math.max(f / f, height / f);
                    if (max > 1.0f) {
                        bitmap = Bitmap.createScaledBitmap(bitmap, (int) (f / max), (int) (height / max), false);
                    }
                    arrayList.add(bitmap);
                    if (arrayList.size() == i) {
                        if (stringList.size() < queShotLayout.getAreaCount()) {
                            for (int i = 0; i < queShotLayout.getAreaCount(); i++) {
                                binding.collageView.addQuShotCollage((Bitmap) arrayList.get(i % i));
                            }
                        } else {
                            binding.collageView.addPieces(arrayList);
                        }
                    }
                    targets.remove(this);
                }
            };
            Picasso picasso = Picasso.get();
            picasso.load("file:///" + stringList.get(i2)).resize(deviceWidth, deviceWidth).centerInside().config(Bitmap.Config.RGB_565).into((Target) r4);
            targets.add(r4);
        }
    }

    private void setOnBackPressDialog() {
        TextView textViewCancel, textViewDiscard;
        final Dialog dialogOnBackPressed = new Dialog(QueShotGridActivity.this, R.style.UploadDialog);
        dialogOnBackPressed.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogOnBackPressed.setContentView(R.layout.dialog_exit);
        dialogOnBackPressed.setCancelable(true);
        dialogOnBackPressed.show();
        textViewCancel = dialogOnBackPressed.findViewById(R.id.textViewCancel);
        textViewDiscard = dialogOnBackPressed.findViewById(R.id.textViewDiscard);

        textViewDiscard.setOnClickListener(view -> {
            dialogOnBackPressed.dismiss();
            moduleToolsId = null;
            finish();
        });

        textViewCancel.setOnClickListener(view -> {
            dialogOnBackPressed.dismiss();
        });
    }

    public void setGoneSave() {
        binding.constraintSaveControl.setVisibility(View.GONE);
    }

    public void setVisibleSave() {
        binding.constraintSaveControl.setVisibility(View.VISIBLE);
    }

    public void onBackPressed() {
        if (moduleToolsId == null) {
            super.onBackPressed();
            return;
        }
        try {
            switch (moduleToolsId) {
                case PADDING:
                case RATIO:
                case LAYER:
                    setGuideLineTools();
                    binding.recyclerViewTools.setVisibility(View.VISIBLE);
                    binding.constrantLayoutChangeLayout.setVisibility(View.GONE);
                    binding.imageToolContainer.setVisibility(View.GONE);
                    binding.collageView.updateLayout(queShotLayout);
                    binding.collageView.setCollagePadding(Padding);
                    binding.collageView.setCollageRadian(BorderRadius);
                    getWindowManager().getDefaultDisplay().getSize(new Point());
                    onNewAspectRatioSelected(aspectRatio);
                    binding.collageView.setAspectRatio(aspectRatio);
                    binding.collageView.setLocked(true);
                    binding.collageView.setTouchEnable(true);

                    binding.backgroundContainer.backgroundTools.setVisibility(View.VISIBLE);
                    binding.backgroundContainer.recyclerViewColor.setVisibility(View.GONE);
                    binding.backgroundContainer.recyclerViewBlur.setVisibility(View.GONE);
                    binding.backgroundContainer.recyclerViewGradient.setVisibility(View.GONE);
                    if (currentBackgroundState.isColor) {
                        binding.collageView.setBackgroundResourceMode(0);
                        binding.collageView.setBackgroundColor(currentBackgroundState.drawableId);
                    } else if (currentBackgroundState.isBitmap) {
                        binding.collageView.setBackgroundResourceMode(2);
                        binding.collageView.setBackground(currentBackgroundState.drawable);
                    } else {
                        binding.collageView.setBackgroundResourceMode(1);
                        if (currentBackgroundState.drawable != null) {
                            binding.collageView.setBackground(currentBackgroundState.drawable);
                        } else {
                            binding.collageView.setBackgroundResource(currentBackgroundState.drawableId);
                        }
                    }
                    setVisibleSave();
                    moduleToolsId = Module.NONE;
                    return;
                case COLLAGE:
                    binding.backgroundContainer.backgroundTools.setVisibility(View.VISIBLE);
                    binding.backgroundContainer.recyclerViewColor.setVisibility(View.GONE);
                    binding.backgroundContainer.recyclerViewBlur.setVisibility(View.GONE);
                    binding.backgroundContainer.recyclerViewGradient.setVisibility(View.GONE);

                    setVisibleSave();
                    setGuideLineTools();
                    binding.recyclerViewTools.setVisibility(View.VISIBLE);
                    binding.imageToolContainer.setVisibility(View.GONE);
                    moduleToolsId = Module.NONE;
                    binding.collageView.setQueShotGrid(null);
                    binding.collageView.setPrevHandlingQueShotGrid(null);
                    binding.collageView.invalidate();
                    moduleToolsId = Module.NONE;
                    return;

                case FILTER:
                    setGuideLineTools();
                    binding.recyclerViewTools.setVisibility(View.VISIBLE);
                    binding.constraintLayoutFilterLayout.setVisibility(View.GONE);
                    binding.imageToolContainer.setVisibility(View.GONE);
                    binding.collageView.setLocked(true);
                    binding.collageView.setTouchEnable(true);
                    for (int i = 0; i < drawableList.size(); i++) {
                        binding.collageView.getQueShotGrids().get(i).setDrawable(drawableList.get(i));
                    }
                    binding.collageView.invalidate();
                    setVisibleSave();
                    moduleToolsId = Module.NONE;
                    return;
                case STICKER:
                    setGuideLineTools();
                    if (binding.collageView.getStickers().size() <= 0) {
                        binding.linearLayoutWrapperStickerList.setVisibility(View.VISIBLE);
                        binding.relativeLayoutAddSticker.setVisibility(View.GONE);
                        binding.collageView.setHandlingSticker((Sticker) null);
                        binding.recyclerViewTools.setVisibility(View.VISIBLE);
                        binding.constraintLayoutSticker.setVisibility(View.GONE);
                        binding.imageToolContainer.setVisibility(View.GONE);
                        binding.collageView.setLocked(true);
                        moduleToolsId = Module.NONE;
                    } else if (binding.relativeLayoutAddSticker.getVisibility() == View.VISIBLE) {
                        binding.collageView.getStickers().clear();
                        binding.relativeLayoutAddSticker.setVisibility(View.GONE);
                        binding.collageView.setHandlingSticker(null);
                        binding.linearLayoutWrapperStickerList.setVisibility(View.VISIBLE);
                        binding.imageToolContainer.setVisibility(View.GONE);
                        binding.recyclerViewTools.setVisibility(View.VISIBLE);
                        binding.constraintLayoutSticker.setVisibility(View.GONE);
                        binding.collageView.setLocked(true);
                        binding.collageView.setTouchEnable(true);
                        moduleToolsId = Module.NONE;
                    } else {
                        binding.linearLayoutWrapperStickerList.setVisibility(View.GONE);
                        binding.relativeLayoutAddSticker.setVisibility(View.VISIBLE);
                        binding.imageToolContainer.setVisibility(View.GONE);
                        binding.recyclerViewTools.setVisibility(View.VISIBLE);
                    }
                    binding.linearLayoutWrapperStickerList.setVisibility(View.VISIBLE);
                    binding.recyclerViewTools.setVisibility(View.VISIBLE);
                    binding.constraintLayoutSticker.setVisibility(View.GONE);
                    setVisibleSave();
                    return;
                case GRADIENT:
                    setGuideLineTools();
                    binding.backgroundContainer.recyclerViewColor.setVisibility(View.GONE);
                    binding.backgroundContainer.recyclerViewBlur.setVisibility(View.GONE);
                    binding.backgroundContainer.recyclerViewGradient.setVisibility(View.GONE);
                    binding.recyclerViewTools.setVisibility(View.VISIBLE);
                    binding.imageToolContainer.setVisibility(View.GONE);
                    binding.collageView.setLocked(true);
                    binding.collageView.setTouchEnable(true);
                    if (currentBackgroundState.isColor) {
                        binding.collageView.setBackgroundResourceMode(0);
                        binding.collageView.setBackgroundColor(currentBackgroundState.drawableId);
                    } else if (currentBackgroundState.isBitmap) {
                        binding.collageView.setBackgroundResourceMode(2);
                        binding.collageView.setBackground(currentBackgroundState.drawable);
                    } else {
                        binding.collageView.setBackgroundResourceMode(1);
                        if (currentBackgroundState.drawable != null) {
                            binding.collageView.setBackground(currentBackgroundState.drawable);
                        } else {
                            binding.collageView.setBackgroundResource(currentBackgroundState.drawableId);
                        }
                    }
                    setVisibleSave();
                    moduleToolsId = Module.NONE;
                    return;

                case NONE:
                    setOnBackPressDialog();
                    return;
                default:
                    super.onBackPressed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onItemClick(QueShotLayout puzzleLayout2, int i) {
        QueShotLayout parse = QueShotLayoutParser.parse(puzzleLayout2.generateInfo());
        puzzleLayout2.setRadian(binding.collageView.getCollageRadian());
        puzzleLayout2.setPadding(binding.collageView.getCollagePadding());
        binding.collageView.updateLayout(parse);
    }

    public void onNewAspectRatioSelected(AspectRatio aspectRatio) {
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        int[] calculateWidthAndHeight = calculateWidthAndHeight(aspectRatio, point);
        binding.collageView.setLayoutParams(new ConstraintLayout.LayoutParams(calculateWidthAndHeight[0], calculateWidthAndHeight[1]));
        binding.collageView.setAspectRatio(aspectRatio);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(binding.constraintLayoutWrapperCollageView);
        constraintSet.connect(binding.collageView.getId(), 3, binding.constraintLayoutWrapperCollageView.getId(), 3, 0);
        constraintSet.connect(binding.collageView.getId(), 1, binding.constraintLayoutWrapperCollageView.getId(), 1, 0);
        constraintSet.connect(binding.collageView.getId(), 4, binding.constraintLayoutWrapperCollageView.getId(), 4, 0);
        constraintSet.connect(binding.collageView.getId(), 2, binding.constraintLayoutWrapperCollageView.getId(), 2, 0);
        constraintSet.applyTo(binding.constraintLayoutWrapperCollageView);
    }

    public void replaceCurrentPiece(String str) {
        new OnLoadBitmapFromUri().execute(str);
    }

    private int[] calculateWidthAndHeight(AspectRatio aspectRatio, Point point) {
        int height = binding.constraintLayoutWrapperCollageView.getHeight();
        if (aspectRatio.getHeight() > aspectRatio.getWidth()) {
            int ratio = (int) (aspectRatio.getRatio() * ((float) height));
            if (ratio < point.x) {
                return new int[]{ratio, height};
            }
            return new int[]{point.x, (int) (((float) point.x) / aspectRatio.getRatio())};
        }
        int ratio2 = (int) (((float) point.x) / aspectRatio.getRatio());
        if (ratio2 > height) {
            return new int[]{(int) (((float) height) * aspectRatio.getRatio()), height};
        }
        return new int[]{point.x, ratio2};
    }

    public void addSticker(Bitmap bitmap) {
        binding.collageView.addSticker(new DrawableSticker(new BitmapDrawable(getResources(), bitmap)));
        binding.linearLayoutWrapperStickerList.setVisibility(View.GONE);
        binding.relativeLayoutAddSticker.setVisibility(View.VISIBLE);
    }

    public void onBackgroundSelected(final BackgroundGridAdapter.SquareView squareView) {
        if (squareView.isColor) {
            binding.collageView.setBackgroundColor(squareView.drawableId);
            binding.collageView.setBackgroundResourceMode(0);
        } else if (squareView.drawable != null) {
            binding.collageView.setBackgroundResourceMode(2);

            AsyncTask<Void, Bitmap, Bitmap> asyncTask = new AsyncTask<Void, Bitmap, Bitmap>() {
                @Override
                protected Bitmap doInBackground(Void... voidArr) {
                    return FilterUtils.getBlurImageFromBitmap(((BitmapDrawable) squareView.drawable).getBitmap(), 5.0f);
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    binding.collageView.setBackground(new BitmapDrawable(getResources(), bitmap));
                }
            };

            asyncTask.execute();
        } else {
            binding.collageView.setBackgroundResource(squareView.drawableId);
            binding.collageView.setBackgroundResourceMode(1);
        }
    }

    public void onFilterSelected(String str) {

        new LoadBitmapWithFilter().execute(str);

    }

    public void finishCrop(Bitmap bitmap) {
        binding.collageView.replace(bitmap, "");
    }

    public void onSaveFilter(Bitmap bitmap) {
        binding.collageView.replace(bitmap, "");
    }

    @Override
    public void onPieceFuncSelected(Module toolType) {
        switch (toolType) {
            case REPLACE:
                Intent intent = new Intent(this, SingleImagePickerActivity.class);
                Bundle optionsBundle = new Bundle();
                optionsBundle.putInt("MAX_COUNT", 1);
                optionsBundle.putBoolean("PREVIEW_ENABLED", false);
                optionsBundle.putBoolean("SHOW_CAMERA", false);
                optionsBundle.putBoolean("MAIN_ACTIVITY", true);
                intent.putExtras(optionsBundle);
                startActivityForResult(intent, 233);
                return;
            case H_FLIP:
                binding.collageView.flipHorizontally();
                return;
            case V_FLIP:
                binding.collageView.flipVertically();
                return;
            case ROTATE:
                binding.collageView.rotate(90.0f);
                return;
            case CROP:
                CropFragment.show(this, this, ((BitmapDrawable) binding.collageView.getQueShotGrid().getDrawable()).getBitmap());
                return;
            case FILTER:
                new LoadFilterBitmapForCurrentPiece().execute();
                return;
            case DODGE:
                new LoadDodgeBitmapForCurrentPiece().execute();
                return;
            case DIVIDE:
                new LoadDivideBitmapForCurrentPiece().execute();
                return;
            case BURN:
                new LoadBurnBitmapForCurrentPiece().execute();
                return;
        }
    }

    class allFilters extends AsyncTask<Void, Void, Void> {
        allFilters() {
        }

        public void onPreExecute() {
            setLoading(true);
        }

        @SuppressLint("WrongThread")
        public Void doInBackground(Void... voidArr) {
            listFilterAll.clear();
            listFilterAll.addAll(FilterCodeAsset.getListBitmapFilterAll(ThumbnailUtils.extractThumbnail(((BitmapDrawable) binding.collageView.getQueShotGrids().get(0).getDrawable()).getBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voidR) {
            binding.recyclerViewFilterAll.setAdapter(new FilterAdapter(listFilterAll, QueShotGridActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.ALL_FILTERS)));
            binding.constraintLayoutFilterLayout.setVisibility(View.VISIBLE);
            binding.constraintLayoutConfirmSaveFilter.setVisibility(View.VISIBLE);
            binding.recyclerViewTools.setVisibility(View.GONE);
            binding.imageToolContainer.setVisibility(View.GONE);
            binding.collageView.setLocked(false);
            binding.collageView.setTouchEnable(false);
            setLoading(false);
            setGuideLine();
        }
    }

    class bwFilters extends AsyncTask<Void, Void, Void> {
        bwFilters() {
        }

        public void onPreExecute() {
            setLoading(true);
        }

        @SuppressLint("WrongThread")
        public Void doInBackground(Void... voidArr) {
            listFilterBW.clear();
            listFilterBW.addAll(FilterCodeAsset.getListBitmapFilterBW(ThumbnailUtils.extractThumbnail(((BitmapDrawable) binding.collageView.getQueShotGrids().get(0).getDrawable()).getBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voidR) {
            binding.recyclerViewFilterBw.setAdapter(new FilterAdapter(listFilterBW, QueShotGridActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.BW_FILTERS)));
            binding.collageView.setLocked(false);
            binding.collageView.setTouchEnable(false);
            setLoading(false);
        }
    }

    class vintageFilters extends AsyncTask<Void, Void, Void> {
        vintageFilters() {
        }

        public void onPreExecute() {
            setLoading(true);
        }

        @SuppressLint("WrongThread")
        public Void doInBackground(Void... voidArr) {
            listFilterVintage.clear();
            listFilterVintage.addAll(FilterCodeAsset.getListBitmapFilterVintage(ThumbnailUtils.extractThumbnail(((BitmapDrawable) binding.collageView.getQueShotGrids().get(0).getDrawable()).getBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voidR) {
            binding.recyclerViewFilterVintage.setAdapter(new FilterAdapter(listFilterVintage, QueShotGridActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.VINTAGE_FILTERS)));
            binding.collageView.setLocked(false);
            binding.collageView.setTouchEnable(false);
            setLoading(false);
        }
    }

    class smoothFilters extends AsyncTask<Void, Void, Void> {
        smoothFilters() {
        }

        public void onPreExecute() {
            setLoading(true);
        }

        @SuppressLint("WrongThread")
        public Void doInBackground(Void... voidArr) {
            listFilterSmooth.clear();
            listFilterSmooth.addAll(FilterCodeAsset.getListBitmapFilterSmooth(ThumbnailUtils.extractThumbnail(((BitmapDrawable) binding.collageView.getQueShotGrids().get(0).getDrawable()).getBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voidR) {
            binding.recyclerViewFilterSmooth.setAdapter(new FilterAdapter(listFilterSmooth, QueShotGridActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.SMOOTH_FILTERS)));
            binding.collageView.setLocked(false);
            binding.collageView.setTouchEnable(false);
            setLoading(false);
        }
    }

    class coldFilters extends AsyncTask<Void, Void, Void> {
        coldFilters() {
        }

        public void onPreExecute() {
            setLoading(true);
        }

        @SuppressLint("WrongThread")
        public Void doInBackground(Void... voidArr) {
            listFilterCold.clear();
            listFilterCold.addAll(FilterCodeAsset.getListBitmapFilterCold(ThumbnailUtils.extractThumbnail(((BitmapDrawable) binding.collageView.getQueShotGrids().get(0).getDrawable()).getBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voidR) {
            binding.recyclerViewFilterCold.setAdapter(new FilterAdapter(listFilterCold, QueShotGridActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.COLD_FILTERS)));
            binding.collageView.setLocked(false);
            binding.collageView.setTouchEnable(false);
            setLoading(false);
        }
    }

    class warmFilters extends AsyncTask<Void, Void, Void> {
        warmFilters() {
        }

        public void onPreExecute() {
            setLoading(true);
        }

        @SuppressLint("WrongThread")
        public Void doInBackground(Void... voidArr) {
            listFilterWarm.clear();
            listFilterWarm.addAll(FilterCodeAsset.getListBitmapFilterWarm(ThumbnailUtils.extractThumbnail(((BitmapDrawable) binding.collageView.getQueShotGrids().get(0).getDrawable()).getBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voidR) {
            binding.recyclerViewFilterWarm.setAdapter(new FilterAdapter(listFilterWarm, QueShotGridActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.WARM_FILTERS)));
            binding.collageView.setLocked(false);
            binding.collageView.setTouchEnable(false);
            setLoading(false);
        }
    }

    class legacyFilters extends AsyncTask<Void, Void, Void> {
        legacyFilters() {
        }

        public void onPreExecute() {
            setLoading(true);
        }

        @SuppressLint("WrongThread")
        public Void doInBackground(Void... voidArr) {
            listFilterLegacy.clear();
            listFilterLegacy.addAll(FilterCodeAsset.getListBitmapFilterLegacy(ThumbnailUtils.extractThumbnail(((BitmapDrawable) binding.collageView.getQueShotGrids().get(0).getDrawable()).getBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voidR) {
            binding.recyclerViewFilterLegacy.setAdapter(new FilterAdapter(listFilterLegacy, QueShotGridActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.LEGACY_FILTERS)));
            binding.collageView.setLocked(false);
            binding.collageView.setTouchEnable(false);
            setLoading(false);
        }
    }

    class LoadFilterBitmapForCurrentPiece extends AsyncTask<Void, List<Bitmap>, List<Bitmap>> {
        LoadFilterBitmapForCurrentPiece() {
        }

        public void onPreExecute() {
            setLoading(true);
        }

        @SuppressLint("WrongThread")
        public List<Bitmap> doInBackground(Void... voidArr) {
            return FilterCodeAsset.getListBitmapFilterAll(ThumbnailUtils.extractThumbnail(((BitmapDrawable) binding.collageView.getQueShotGrid().getDrawable()).getBitmap(), 100, 100));
        }

        public void onPostExecute(List<Bitmap> list) {
            setLoading(false);
            if (binding.collageView.getQueShotGrid() != null) {
                FilterFragment.show(QueShotGridActivity.this, QueShotGridActivity.this, ((BitmapDrawable) binding.collageView.getQueShotGrid().getDrawable()).getBitmap(), list);
            }
        }
    }

    class LoadDodgeBitmapForCurrentPiece extends AsyncTask<Void, List<Bitmap>, List<Bitmap>> {
        LoadDodgeBitmapForCurrentPiece() {
        }

        public void onPreExecute() {
            setLoading(true);
        }

        @SuppressLint("WrongThread")
        public List<Bitmap> doInBackground(Void... voidArr) {
            return EffectCodeAsset.getListBitmapDodgeEffect(ThumbnailUtils.extractThumbnail(((BitmapDrawable) binding.collageView.getQueShotGrid().getDrawable()).getBitmap(), 100, 100));
        }

        public void onPostExecute(List<Bitmap> list) {
            setLoading(false);
            if (binding.collageView.getQueShotGrid() != null) {
                DodgeFragment.show(QueShotGridActivity.this, QueShotGridActivity.this, ((BitmapDrawable) binding.collageView.getQueShotGrid().getDrawable()).getBitmap(), list);
            }
        }
    }

    class LoadDivideBitmapForCurrentPiece extends AsyncTask<Void, List<Bitmap>, List<Bitmap>> {
        LoadDivideBitmapForCurrentPiece() {
        }

        public void onPreExecute() {
            setLoading(true);
        }

        @SuppressLint("WrongThread")
        public List<Bitmap> doInBackground(Void... voidArr) {
            return EffectCodeAsset.getListBitmapDivideEffect(ThumbnailUtils.extractThumbnail(((BitmapDrawable) binding.collageView.getQueShotGrid().getDrawable()).getBitmap(), 100, 100));
        }

        public void onPostExecute(List<Bitmap> list) {
            setLoading(false);
            if (binding.collageView.getQueShotGrid() != null) {
                DivideFragment.show(QueShotGridActivity.this, QueShotGridActivity.this, ((BitmapDrawable) binding.collageView.getQueShotGrid().getDrawable()).getBitmap(), list);
            }
        }
    }

    class LoadBurnBitmapForCurrentPiece extends AsyncTask<Void, List<Bitmap>, List<Bitmap>> {
        LoadBurnBitmapForCurrentPiece() {
        }

        public void onPreExecute() {
            setLoading(true);
        }

        @SuppressLint("WrongThread")
        public List<Bitmap> doInBackground(Void... voidArr) {
            return EffectCodeAsset.getListBitmapColorEffect(ThumbnailUtils.extractThumbnail(((BitmapDrawable) binding.collageView.getQueShotGrid().getDrawable()).getBitmap(), 100, 100));
        }

        public void onPostExecute(List<Bitmap> list) {
            setLoading(false);
            if (binding.collageView.getQueShotGrid() != null) {
                BurnFragment.show(QueShotGridActivity.this, QueShotGridActivity.this, ((BitmapDrawable) binding.collageView.getQueShotGrid().getDrawable()).getBitmap(), list);
            }
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
                binding.collageView.getQueShotGrids().get(i).setDrawable(bitmapDrawable);
            }
            binding.collageView.invalidate();
            setLoading(false);
        }
    }

    class OnLoadBitmapFromUri extends AsyncTask<String, Bitmap, Bitmap> {
        OnLoadBitmapFromUri() {
        }

        public void onPreExecute() {
            setLoading(true);
        }

        public Bitmap doInBackground(String... strArr) {
            try {
                Uri fromFile = Uri.fromFile(new File(strArr[0]));

                Bitmap rotateBitmap = SystemUtil.rotateBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), fromFile), new ExifInterface(getContentResolver().openInputStream(fromFile)).getAttributeInt(ExifInterface.TAG_ORIENTATION, 1));

                float width = (float) rotateBitmap.getWidth();
                float height = (float) rotateBitmap.getHeight();
                float max = Math.max(width / 1280.0f, height / 1280.0f);
                return max > 1.0f ? Bitmap.createScaledBitmap(rotateBitmap, (int) (width / max), (int) (height / max), false) : rotateBitmap;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public void onPostExecute(Bitmap bitmap) {
            setLoading(false);
            binding.collageView.replace(bitmap, "");
        }
    }


    class SaveCollageAsFile extends AsyncTask<Bitmap, String, String> {
        SaveCollageAsFile() {
        }

        @Override
        public void onPreExecute() {
            setLoading(true);
        }

        @Override
        public String doInBackground(Bitmap... bitmapArr) {
            Bitmap bitmap = bitmapArr[0];
            Bitmap bitmap2 = bitmapArr[1];
            Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            Paint paint = null;
            canvas.drawBitmap(bitmap, null, new RectF(0.0f, 0.0f, (float) bitmap.getWidth(), (float) bitmap.getHeight()), paint);
            canvas.drawBitmap(bitmap2, null, new RectF(0.0f, 0.0f, (float) bitmap.getWidth(), (float) bitmap.getHeight()), paint);
            bitmap.recycle();
            bitmap2.recycle();
            try {
                File image = SaveFileUtils.saveBitmapFileCollage(QueShotGridActivity.this, createBitmap, new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date()));
                createBitmap.recycle();
                return image.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public void onPostExecute(String str) {
            setLoading(false);
            Intent intent = new Intent(QueShotGridActivity.this, PhotoShareActivity.class);
            intent.putExtra("path", str);
            startActivity(intent);
        }
    }

    public void setLoading(boolean z) {
        if (z) {
            getWindow().setFlags(16, 16);
            binding.relativeLayoutLoading.setVisibility(View.VISIBLE);
            return;
        }
        getWindow().clearFlags(16);
        binding.relativeLayoutLoading.setVisibility(View.GONE);
    }

}
