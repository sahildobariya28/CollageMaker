package com.photo.collagemaker.activities.editor.collage_editor;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
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
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.photo.collagemaker.R;
import com.photo.collagemaker.activities.picker.MultipleImagePickerActivity;
import com.photo.collagemaker.activities.PhotoShareActivity;
import com.photo.collagemaker.activities.picker.SingleImagePickerActivity;
import com.photo.collagemaker.activities.editor.collage_editor.adapter.AspectAdapter;
import com.photo.collagemaker.activities.editor.collage_editor.adapter.BackgroundGridAdapter;
import com.photo.collagemaker.activities.editor.collage_editor.adapter.FilterAdapter;
import com.photo.collagemaker.activities.editor.collage_editor.adapter.GridAdapter;
import com.photo.collagemaker.activities.editor.collage_editor.adapter.GridItemToolsAdapter;
import com.photo.collagemaker.activities.editor.collage_editor.adapter.GridToolsAdapter;
import com.photo.collagemaker.activities.editor.collage_editor.adapter.ColorAdapter;
import com.photo.collagemaker.activities.editor.collage_editor.adapter.StickerAdapter;
import com.photo.collagemaker.activities.editor.collage_editor.adapter.StickerTabAdapter;
import com.photo.collagemaker.assets.EffectCodeAsset;
import com.photo.collagemaker.assets.FilterCodeAsset;
import com.photo.collagemaker.assets.StickersAsset;
import com.photo.collagemaker.custom_view.CustomEditorForCollage;
import com.photo.collagemaker.custom_view.CustomText;
import com.photo.collagemaker.databinding.ActivityGridBinding;
import com.photo.collagemaker.draw.Drawing;
import com.photo.collagemaker.event.AlignHorizontallyEvent;
import com.photo.collagemaker.event.DeleteIconEvent;
import com.photo.collagemaker.event.EditTextIconEvent;
import com.photo.collagemaker.event.FlipHorizontallyEvent;
import com.photo.collagemaker.event.ZoomIconEvent;
import com.photo.collagemaker.fragment.BurnFragment;
import com.photo.collagemaker.fragment.ColoredFragment;
import com.photo.collagemaker.fragment.CropFragment;
import com.photo.collagemaker.fragment.DivideFragment;
import com.photo.collagemaker.fragment.DodgeFragment;
import com.photo.collagemaker.fragment.FilterFragment;
import com.photo.collagemaker.fragment.MosaicFragment;
import com.photo.collagemaker.fragment.RotateFragment;
import com.photo.collagemaker.fragment.TextFragment;
import com.photo.collagemaker.grid.QueShotGrid;
import com.photo.collagemaker.grid.QueShotLayout;
import com.photo.collagemaker.grid.QueShotLayoutParser;
import com.photo.collagemaker.listener.BrushColorListener;
import com.photo.collagemaker.listener.FilterListener;
import com.photo.collagemaker.listener.OnQuShotEditorListener;
import com.photo.collagemaker.module.Module;
import com.photo.collagemaker.picker.PermissionsUtils;
import com.photo.collagemaker.custom_view.StickerIcons;
import com.photo.collagemaker.custom_view.StickerView;
import com.photo.collagemaker.custom_view.CustomTextView;
import com.photo.collagemaker.sticker.DrawableSticker;
import com.photo.collagemaker.sticker.Sticker;
import com.photo.collagemaker.utils.CollageUtils;
import com.photo.collagemaker.utils.FilterUtils;
import com.photo.collagemaker.utils.SaveFileUtils;
import com.photo.collagemaker.utils.SystemUtil;
import com.skydoves.colorpickerview.listeners.ColorListener;
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
public class CollageEditorActivity extends AppCompatActivity implements GridToolsAdapter.OnItemSelected,
        AspectAdapter.OnNewSelectedListener, StickerAdapter.OnClickSplashListener,
        BackgroundGridAdapter.BackgroundGridListener, FilterListener, CropFragment.OnCropPhoto,
        RotateFragment.OnCropPhoto, FilterFragment.OnFilterSavePhoto, DodgeFragment.OnFilterSavePhoto,
        DivideFragment.OnFilterSavePhoto, BurnFragment.OnFilterSavePhoto, GridItemToolsAdapter.OnPieceFuncItemSelected,
        GridAdapter.OnItemClickListener, MosaicFragment.MosaicListener, ColoredFragment.ColoredListener, BrushColorListener {

    public AspectRatio aspectRatio;
    public BackgroundGridAdapter.SquareView currentBackgroundState;
    public QueShotLayout queShotLayout;
    public GridToolsAdapter gridToolsAdapter = new GridToolsAdapter(this);
    private GridItemToolsAdapter gridItemToolsAdapter = new GridItemToolsAdapter(this);

    public Module moduleToolsId;
    public float BorderRadius;
    public float Padding;

    public static CollageEditorActivity gridActivity;

    // ArrayList
    public ArrayList listFilterAll = new ArrayList<>();
    public ArrayList listFilterBW = new ArrayList<>();
    public ArrayList listFilterVintage = new ArrayList<>();
    public ArrayList listFilterSmooth = new ArrayList<>();
    public ArrayList listFilterCold = new ArrayList<>();
    public ArrayList listFilterWarm = new ArrayList<>();
    public ArrayList listFilterLegacy = new ArrayList<>();
    public List<Drawable> drawableList = new ArrayList<>();
    public List<String> imageList;
    public List<Target> targets = new ArrayList();

    public CustomEditorForCollage quShotCustomEditor;

    ActivityGridBinding binding;
    CollageEditorViewModel viewModel;

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        binding = ActivityGridBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this, new CollageEditorViewModelFactory(this, binding)).get(CollageEditorViewModel.class);
        setContentView(binding.getRoot());

        gridActivity = this;
        imageList = getIntent().getStringArrayListExtra(MultipleImagePickerActivity.KEY_DATA_RESULT);

        initDrawingView();
        initCollageView();
        initToolBar();
        initPrimaryView();
        initSecondaryView();

        setLoading(false);

        currentBackgroundState = new BackgroundGridAdapter.SquareView(getColor(R.color.white), "", true);


        CGENativeLibrary.setLoadImageCallback(loadImageCallback, null);

        binding.rvSecondaryTool.setAlpha(0.0f);
        binding.constrantLayoutChangeLayout.post(() -> slideDown(binding.rvSecondaryTool));
        new Handler().postDelayed(() -> binding.rvSecondaryTool.setAlpha(1.0f), 1000);

    }

    public void initCollageView() {
        queShotLayout = CollageUtils.getCollageLayouts(imageList.size()).get(0);
        binding.collageView.setQueShotLayout(queShotLayout);
        binding.collageView.setTouchEnable(true);
        binding.collageView.setNeedDrawLine(false);
        binding.collageView.setNeedDrawOuterLine(false);
        binding.collageView.setLineSize(4);
        binding.collageView.setCollagePadding(6.0f);
        binding.collageView.setCollageRadian(15.0f);
        binding.collageView.setLineColor(ContextCompat.getColor(this, R.color.theme_color_dark));
        binding.collageView.setSelectedLineColor(ContextCompat.getColor(this, R.color.theme_color));
        binding.collageView.setHandleBarColor(ContextCompat.getColor(this, R.color.theme_color));
        binding.collageView.setAnimateDuration(300);

        binding.collageView.setOnQueShotSelectedListener((collage, i) -> {
            viewModel.rvSecondaryToolShow();
            slideUp(binding.rvSecondaryTool);
            moduleToolsId = Module.COLLAGE;
        });
        binding.collageView.setOnQueShotUnSelectedListener(() -> {
            viewModel.rvPrimaryToolShow();
            moduleToolsId = Module.NONE;
        });
        binding.collageView.post(this::loadPhoto);

        //sticker editing view
        StickerIcons quShotStickerIconClose = new StickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_close), 0, StickerIcons.DELETE);
        quShotStickerIconClose.setIconEvent(new DeleteIconEvent());
        StickerIcons quShotStickerIconScale = new StickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_scale), 3, StickerIcons.SCALE);
        quShotStickerIconScale.setIconEvent(new ZoomIconEvent());
        StickerIcons quShotStickerIconFlip = new StickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_flip), 1, StickerIcons.FLIP);
        quShotStickerIconFlip.setIconEvent(new FlipHorizontallyEvent());
        StickerIcons quShotStickerIconRotate = new StickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_rotate), 3, StickerIcons.ROTATE);
        quShotStickerIconRotate.setIconEvent(new ZoomIconEvent());
        StickerIcons quShotStickerIconEdit = new StickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_edit), 1, StickerIcons.EDIT);
        quShotStickerIconEdit.setIconEvent(new EditTextIconEvent());
        StickerIcons quShotStickerIconCenter = new StickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_center), 2, StickerIcons.ALIGN);
        quShotStickerIconCenter.setIconEvent(new AlignHorizontallyEvent());
        binding.collageView.setIcons(Arrays.asList(quShotStickerIconClose, quShotStickerIconScale, quShotStickerIconFlip, quShotStickerIconEdit, quShotStickerIconRotate, quShotStickerIconCenter));
        binding.collageView.setConstrained(true);
        binding.collageView.setOnStickerOperationListener(onStickerOperationListener);
    }

    public void initDrawingView() {
        quShotCustomEditor = new CustomEditorForCollage.Builder(this, binding.collageView).setPinchTextScalable(true).build();
        quShotCustomEditor.setOnPhotoEditorListener(new OnQuShotEditorListener() {
            @Override
            public void onAddViewListener(Drawing viewType, int i) {

            }

            @Override
            public void onRemoveViewListener(int i) {

            }

            @Override
            public void onRemoveViewListener(Drawing viewType, int i) {

            }

            @Override
            public void onStartViewChangeListener(Drawing viewType) {

            }

            @Override
            public void onStopViewChangeListener(Drawing viewType) {

            }
        });
    }

    public void initToolBar() {
        binding.textTitle.setText("Collage Maker");

        binding.btnBack.setOnClickListener(view -> onBackPressed());

        binding.btnSave.setOnClickListener(view -> {
            if (PermissionsUtils.checkWriteStoragePermission(CollageEditorActivity.this)) {
                Bitmap createBitmap = SaveFileUtils.createBitmap(binding.collageView, 1920);
                Bitmap createBitmap2 = binding.collageView.createBitmap();
                new SaveCollageAsFile().execute(createBitmap, createBitmap2);
            }
        });
    }

    public void initPrimaryView() {
        initLayoutBorderRatioBgView();
        initTextEditView();
        initFilterView();
        initStickerView();
        initDrawView();
        initAddImageView();

        binding.rvPrimaryTool.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.rvPrimaryTool.setAdapter(gridToolsAdapter);
    }

    public void initSecondaryView() {
        binding.btnDownArrow.setOnClickListener(view -> {
            onBackPressed();
            viewModel.rvPrimaryToolShow();
        });

        binding.rvSecondaryTool.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.rvSecondaryTool.setAdapter(gridItemToolsAdapter);
    }

    public void initLayoutBorderRatioBgView() {


        GridAdapter collageAdapter = new GridAdapter();
        binding.recyclerViewCollage.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recyclerViewCollage.setAdapter(collageAdapter);
        collageAdapter.refreshData(CollageUtils.getCollageLayouts(imageList.size()), null);
        collageAdapter.setOnItemClickListener(this);

        AspectAdapter aspectRatioPreviewAdapter = new AspectAdapter(this);
        aspectRatioPreviewAdapter.setListener(this);
        binding.recyclerViewRatio.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recyclerViewRatio.setAdapter(aspectRatioPreviewAdapter);

        binding.backgroundContainer.recyclerViewColor.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        binding.backgroundContainer.recyclerViewColor.setHasFixedSize(true);
        binding.backgroundContainer.recyclerViewColor.setAdapter(new BackgroundGridAdapter(getApplicationContext(), this));

        binding.backgroundContainer.recyclerViewGradient.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        binding.backgroundContainer.recyclerViewGradient.setHasFixedSize(true);
        binding.backgroundContainer.recyclerViewGradient.setAdapter(new BackgroundGridAdapter(getApplicationContext(), this, true));

        binding.backgroundContainer.recyclerViewBlur.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        binding.backgroundContainer.recyclerViewBlur.setHasFixedSize(true);
        binding.backgroundContainer.recyclerViewBlur.setAdapter(new BackgroundGridAdapter(getApplicationContext(), this, true));


        binding.linearLayoutCollage.setOnClickListener(view -> viewModel.collageShow());
        binding.linearLayoutBorder.setOnClickListener(view -> viewModel.borderShow());
        binding.linearLayoutRatio.setOnClickListener(view -> viewModel.ratioShow());
        binding.linearLayoutBackground.setOnClickListener(view -> viewModel.bgShow());

        binding.backgroundContainer.btnColor.setOnClickListener(view -> setBackgroundColor());
        binding.backgroundContainer.btnGradient.setOnClickListener(view -> setBackgroundGradient());
        binding.backgroundContainer.btnBlur.setOnClickListener(view -> selectBackgroundBlur());
        binding.backgroundContainer.btnSelect.setOnClickListener(view -> selectColorPicker());
        binding.backgroundContainer.btnWhite.setOnClickListener(view -> binding.collageView.setBackgroundColor(ContextCompat.getColor(this, R.color.white)));
        binding.backgroundContainer.btnBlack.setOnClickListener(view -> binding.collageView.setBackgroundColor(ContextCompat.getColor(this, R.color.black)));

        binding.seekbarBorder.setOnSeekBarChangeListener(onSeekBarChangeListener);
        binding.seekbarRadius.setOnSeekBarChangeListener(onSeekBarChangeListener);

        binding.imageViewSaveLayer.setOnClickListener(view -> {
            viewModel.rvPrimaryToolShow();
            queShotLayout = binding.collageView.getQueShotLayout();
            BorderRadius = binding.collageView.getCollageRadian();
            Padding = binding.collageView.getCollagePadding();
            binding.collageView.setLocked(true);
            binding.collageView.setTouchEnable(true);
            aspectRatio = binding.collageView.getAspectRatio();
            moduleToolsId = Module.NONE;

            binding.collageView.setBackgroundColor(binding.colorPickerView.getColor());

            binding.backgroundContainer.backgroundTools.setVisibility(View.VISIBLE);
            binding.backgroundContainer.recyclerViewColor.setVisibility(View.GONE);
            binding.backgroundContainer.recyclerViewBlur.setVisibility(View.GONE);
            binding.backgroundContainer.recyclerViewGradient.setVisibility(View.GONE);
            binding.backgroundContainer.getRoot().setVisibility(View.GONE);
            binding.colorPickerView.setVisibility(View.GONE);
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

        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) binding.collageView.getLayoutParams();
        layoutParams.height = point.x;
        layoutParams.width = point.x;
        binding.collageView.setLayoutParams(layoutParams);
        aspectRatio = new AspectRatio(1, 1);
        binding.collageView.setAspectRatio(new AspectRatio(1, 1));
        moduleToolsId = Module.NONE;
    }

    public void initTextEditView() {
        binding.relativeLayoutAddText.setOnClickListener(view -> {
            binding.collageView.setHandlingSticker(null);
            textFragment();
        });

        binding.imageViewCloseText.setOnClickListener(view -> {
            binding.constraintLayoutConfirmText.setVisibility(View.GONE);
            binding.relativeLayoutAddText.setVisibility(View.GONE);
            binding.rvPrimaryTool.setVisibility(View.VISIBLE);
            onBackPressed();
        });
        binding.imageViewSaveText.setOnClickListener(view -> {
            binding.collageView.setHandlingSticker(null);
            binding.collageView.setLocked(true);
            if (!binding.collageView.getStickers().isEmpty()) {
//                new GridActivity.SaveSticker().execute();
            }

            binding.constraintLayoutConfirmText.setVisibility(View.GONE);
            binding.relativeLayoutAddText.setVisibility(View.GONE);
            binding.rvPrimaryTool.setVisibility(View.VISIBLE);
            moduleToolsId = Module.NONE;
        });
    }

    public void initFilterView() {
        binding.imageViewSaveFilter.setOnClickListener(view -> {

            binding.collageView.setLocked(true);
            binding.collageView.setTouchEnable(true);
            viewModel.rvPrimaryToolShow();
            moduleToolsId = Module.NONE;
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

    public void initStickerView() {

        binding.imageViewSaveSticker.setOnClickListener(view -> {

            binding.collageView.setHandlingSticker(null);
            viewModel.rvPrimaryToolShow();

            binding.collageView.setLocked(true);
            binding.collageView.setTouchEnable(true);
            moduleToolsId = Module.NONE;

        });

        binding.imageViewCloseSticker.setOnClickListener(view -> {
            viewModel.rvPrimaryToolShow();
            onBackPressed();
        });

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
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), StickersAsset.mListEmojy(), i, CollageEditorActivity.this));
                        break;
                    case 1:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), StickersAsset.mListFlag(), i, CollageEditorActivity.this));
                        break;
                    case 2:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), StickersAsset.mListBoom(), i, CollageEditorActivity.this));
                        break;

                }

                viewGroup.addView(inflate);
                return inflate;
            }
        });
        binding.recyclerTabLayout.setUpWithAdapter(new StickerTabAdapter(binding.stickerViewpaper, getApplicationContext()));
        binding.recyclerTabLayout.setPositionThreshold(0.5f);
        binding.recyclerTabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
    }

    public void initDrawView() {
        binding.imageViewRedo.setOnClickListener(view -> quShotCustomEditor.redoBrush());
        binding.imageViewUndo.setOnClickListener(view -> quShotCustomEditor.undoBrush());

        binding.seekbarBrushSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                quShotCustomEditor.setBrushSize((float) (i + 10));
            }
        });
        binding.seekbarBrushSizeNeon.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                quShotCustomEditor.setBrushSize((float) (i + 10));
            }
        });
        binding.seekbarEraseSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                quShotCustomEditor.setBrushEraserSize((float) (i + 10));
                quShotCustomEditor.brushEraser();
            }
        });

        binding.recyclerViewColorPaint.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recyclerViewColorPaint.setHasFixedSize(true);
        binding.recyclerViewColorPaint.setAdapter(new ColorAdapter(getApplicationContext(), this));
        binding.recyclerViewColorNeon.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recyclerViewColorNeon.setHasFixedSize(true);
        binding.recyclerViewColorNeon.setAdapter(new ColorAdapter(getApplicationContext(), this));

        binding.btnSaveDraw.setOnClickListener(view -> {
            viewModel.rvPrimaryToolShow();
            quShotCustomEditor.setBrushDrawingMode(false);
        });

        binding.btnNeon.setOnClickListener(view -> {
            binding.btnNeon.setColorFilter(this.getColor(R.color.icon_color_theme));
            binding.btnColor.setColorFilter(this.getColor(R.color.icon_color_dark));
            binding.btnEraser.setColorFilter(this.getColor(R.color.icon_color_dark));
            moduleToolsId = Module.NEON;

            setColorNeon();
            quShotCustomEditor.setBrushDrawingMode(true);

            quShotCustomEditor.setBrushDrawingMode(false);
            viewModel.neonShow();

            quShotCustomEditor.setBrushMode(2);
            reloadingLayout();
        });
        binding.btnColor.setOnClickListener(view -> {
            binding.btnNeon.setColorFilter(this.getColor(R.color.icon_color_dark));
            binding.btnColor.setColorFilter(this.getColor(R.color.icon_color_theme));
            binding.btnEraser.setColorFilter(this.getColor(R.color.icon_color_dark));
            moduleToolsId = Module.PAINT;
            setColorPaint();
            quShotCustomEditor.setBrushDrawingMode(true);
            viewModel.paintShow();
            quShotCustomEditor.setBrushDrawingMode(false);

            quShotCustomEditor.setBrushMode(1);
            reloadingLayout();
        });
        binding.btnEraser.setOnClickListener(view -> {
            binding.btnNeon.setColorFilter(this.getColor(R.color.icon_color_dark));
            binding.btnColor.setColorFilter(this.getColor(R.color.icon_color_dark));
            binding.btnEraser.setColorFilter(this.getColor(R.color.icon_color_theme));
            viewModel.eraserShow();
            quShotCustomEditor.brushEraser();
            binding.seekbarEraseSize.setProgress(20);
        });
    }

    public void initAddImageView() {
        binding.imageViewCloseAddImage.setOnClickListener(view -> {
            viewModel.rvPrimaryToolShow();
            binding.collageView.removeCurrentSticker();
            binding.collageView.setLocked(true);
            binding.collageView.setTouchEnable(true);
        });
        binding.imageViewSaveAddImage.setOnClickListener(view -> {
            viewModel.rvPrimaryToolShow();
            binding.collageView.setLocked(true);
            binding.collageView.setTouchEnable(true);
        });
    }

    public void reloadingLayout() {
        binding.collageView.postDelayed(() -> {
            try {
                binding.collageView.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                e.printStackTrace();
            }
            setLoading(false);
        }, 300);
    }

    public ColorAdapter colorAdapter;

    public void setColorNeon() {
        colorAdapter = (ColorAdapter) binding.recyclerViewColorNeon.getAdapter();
        if (colorAdapter != null) {
            colorAdapter.setSelectedColorIndex(0);
        }
        binding.recyclerViewColorNeon.scrollToPosition(0);
        if (colorAdapter != null) {
            colorAdapter.notifyDataSetChanged();
        }
        quShotCustomEditor.setBrushMode(2);
        quShotCustomEditor.setBrushDrawingMode(true);
        binding.seekbarBrushSizeNeon.setProgress(20);
    }

    public void setColorPaint() {
        binding.recyclerViewColorPaint.scrollToPosition(0);
        colorAdapter = (ColorAdapter) binding.recyclerViewColorPaint.getAdapter();
        if (colorAdapter != null) {
            colorAdapter.setSelectedColorIndex(0);
        }
        if (colorAdapter != null) {
            colorAdapter.notifyDataSetChanged();
        }
        quShotCustomEditor.setBrushMode(1);
        quShotCustomEditor.setBrushDrawingMode(true);
        binding.seekbarBrushSize.setProgress(20);
    }

    @Override
    public void onSaveMosaic(Bitmap bitmap) {
        moduleToolsId = Module.NONE;
    }

    @Override
    public void onColorChanged(String str) {
        quShotCustomEditor.setBrushColor(Color.parseColor(str));
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
            if (id == R.id.seekbarBorder) {
                binding.collageView.setCollagePadding((float) i);
            } else if (id == R.id.seekbarRadius) {
                binding.collageView.setCollageRadian((float) i);
            }
            binding.collageView.invalidate();
        }
    };
    StickerView.OnStickerOperationListener onStickerOperationListener = new StickerView.OnStickerOperationListener() {
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

        @Override
        public void onStickerSelected(Sticker sticker, int selectedStickerPosition) {

        }


        public void onStickerDeleted(@NonNull Sticker sticker) {

        }

        public void onStickerTouchOutside() {

        }

        public void onStickerDoubleTap(@NonNull Sticker sticker) {
            if (sticker instanceof CustomTextView) {
                sticker.setShow(false);
                binding.collageView.setHandlingSticker(null);
                addTextFragment = TextFragment.show(CollageEditorActivity.this, ((CustomTextView) sticker).getQuShotText());
                textEditor = new TextFragment.TextEditor() {
                    public void onDone(CustomText addTextProperties) {
                        binding.collageView.getStickers().remove(binding.collageView.getLastHandlingSticker());
                        binding.collageView.addSticker(new CustomTextView(CollageEditorActivity.this, addTextProperties));
                    }

                    public void onBackButton() {
                        binding.collageView.showLastHandlingSticker();
                    }
                };
                addTextFragment.setOnTextEditorListener(textEditor);
            }
        }
    };


    public static CollageEditorActivity getQueShotGridActivityInstance() {
        return gridActivity;
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


    public void setBackgroundColor() {
        binding.backgroundContainer.backgroundTools.setVisibility(View.GONE);
        binding.backgroundContainer.recyclerViewColor.scrollToPosition(0);
        ((BackgroundGridAdapter) binding.backgroundContainer.recyclerViewColor.getAdapter()).setSelectedIndex(-1);
        binding.backgroundContainer.recyclerViewColor.getAdapter().notifyDataSetChanged();
        binding.backgroundContainer.recyclerViewColor.setVisibility(View.VISIBLE);
        binding.backgroundContainer.recyclerViewGradient.setVisibility(View.GONE);
        binding.backgroundContainer.recyclerViewBlur.setVisibility(View.GONE);
    }

    public void setBackgroundGradient() {
        binding.backgroundContainer.backgroundTools.setVisibility(View.GONE);

        binding.backgroundContainer.recyclerViewGradient.scrollToPosition(0);
        ((BackgroundGridAdapter) binding.backgroundContainer.recyclerViewGradient.getAdapter()).setSelectedIndex(-1);
        binding.backgroundContainer.recyclerViewGradient.getAdapter().notifyDataSetChanged();

        binding.backgroundContainer.recyclerViewColor.setVisibility(View.GONE);
        binding.backgroundContainer.recyclerViewGradient.setVisibility(View.VISIBLE);
        binding.backgroundContainer.recyclerViewBlur.setVisibility(View.GONE);
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
    }

    public void selectColorPicker() {

        binding.colorPickerView.setVisibility(View.VISIBLE);

        int viewX = binding.colorPickerView.getLeft() + (binding.colorPickerView.getRight() - binding.colorPickerView.getLeft()) / 2;
        int viewY = binding.colorPickerView.getTop() + (binding.colorPickerView.getBottom() - binding.colorPickerView.getTop()) / 2;
        binding.colorPickerView.setSelectorPoint(viewX, viewY);


        binding.colorPickerView.setPaletteDrawable(new BitmapDrawable(getResources(), binding.collageView.createBitmap()));
        binding.colorPickerView.setColorListener((ColorListener) (color, fromUser) -> {
            binding.backgroundContainer.selectedColorPreview.setCardBackgroundColor(ColorStateList.valueOf(color));
            binding.backgroundContainer.selectedColorPreview.getBackground().setTint(color);
        });

    }


    public void onToolSelected(Module module) {
        moduleToolsId = module;
        switch (module) {
            case LAYER:
                viewModel.collageShow();
                queShotLayout = binding.collageView.getQueShotLayout();
                aspectRatio = binding.collageView.getAspectRatio();
                BorderRadius = binding.collageView.getCollageRadian();
                Padding = binding.collageView.getCollagePadding();
                binding.collageView.setLocked(false);
                binding.collageView.setTouchEnable(false);
                break;
            case PADDING:

                viewModel.borderShow();
                queShotLayout = binding.collageView.getQueShotLayout();
                aspectRatio = binding.collageView.getAspectRatio();
                BorderRadius = binding.collageView.getCollageRadian();
                Padding = binding.collageView.getCollagePadding();
                binding.collageView.setLocked(false);
                binding.collageView.setTouchEnable(false);
                break;
            case DRAW:
                viewModel.drawShow();
                break;
            case ADDIMAGE:
                binding.constraintSaveControl.setVisibility(View.GONE);
                binding.constraintLayoutConfirmSaveAddImage.setVisibility(View.VISIBLE);
                binding.rvPrimaryTool.setVisibility(View.GONE);
                binding.collageView.setLocked(false);
                binding.collageView.setTouchEnable(false);

                Intent intent = new Intent(this, SingleImagePickerActivity.class);
                Bundle optionsBundle = new Bundle();
                optionsBundle.putInt("MAX_COUNT", 1);
                optionsBundle.putBoolean("PREVIEW_ENABLED", false);
                optionsBundle.putBoolean("SHOW_CAMERA", false);
                optionsBundle.putBoolean("MAIN_ACTIVITY", true);
                optionsBundle.putBoolean("ADD_IMAGE", true);
                optionsBundle.putBoolean("FREE_STYLE_ADD_IMAGE", false);
                intent.putExtras(optionsBundle);
                startActivityForResult(intent, 266);
                break;
            case TEXT:
                viewModel.textShow();
                textFragment();
                binding.collageView.setLocked(false);
                binding.collageView.setTouchEnable(false);
                break;
            case FILTER:
                if (drawableList.isEmpty()) {
                    for (QueShotGrid drawable : binding.collageView.getQueShotGrids()) {
                        drawableList.add(drawable.getDrawable());
                    }
                }
                binding.collageView.setLocked(false);
                binding.collageView.setTouchEnable(false);
                viewModel.filterAllShow();
                new allFilters().execute();
                new bwFilters().execute();
                new vintageFilters().execute();
                new smoothFilters().execute();
                new coldFilters().execute();
                new warmFilters().execute();
                new legacyFilters().execute();
                break;
            case STICKER:
                viewModel.stickerShow();

                binding.collageView.updateLayout(queShotLayout);
                binding.collageView.setCollagePadding(Padding);
                binding.collageView.setCollageRadian(BorderRadius);
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

                binding.collageView.setLocked(false);
                binding.collageView.setTouchEnable(false);
                break;
            case GRADIENT:
                viewModel.bgShow();
                queShotLayout = binding.collageView.getQueShotLayout();
                aspectRatio = binding.collageView.getAspectRatio();
                BorderRadius = binding.collageView.getCollageRadian();
                Padding = binding.collageView.getCollagePadding();
                binding.collageView.setLocked(false);
                binding.collageView.setTouchEnable(false);

            default:
        }
    }

    public TextFragment addTextFragment;
    public TextFragment.TextEditor textEditor;

    public void textFragment() {
        addTextFragment = TextFragment.show(this);
        textEditor = new TextFragment.TextEditor() {
            public void onDone(CustomText addTextProperties) {
                binding.collageView.addSticker(new CustomTextView(getApplicationContext(), addTextProperties));
            }

            public void onBackButton() {
                if (binding.collageView.getStickers().isEmpty()) {
                    onBackPressed();
                }
            }
        };
        addTextFragment.setOnTextEditorListener(textEditor);
    }

    public void loadPhoto() {
        final int i;
        final ArrayList arrayList = new ArrayList();
        if (imageList.size() > queShotLayout.getAreaCount()) {
            i = queShotLayout.getAreaCount();
        } else {
            i = imageList.size();
        }
        for (int i2 = 0; i2 < i; i2++) {
            Target r4 = new Target() {
                public void onBitmapFailed(Exception exc, Drawable drawable) {
                }

                public void onPrepareLoad(Drawable drawable) {
                }

                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {

                    arrayList.add(bitmap);
                    if (arrayList.size() == i) {
                        if (imageList.size() < queShotLayout.getAreaCount()) {
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
            int deviceWidth = getResources().getDisplayMetrics().widthPixels;
            Picasso.get().load("file:///" + imageList.get(i2)).resize(deviceWidth, deviceWidth).centerInside().config(Bitmap.Config.RGB_565).into((Target) r4);
            targets.add(r4);
        }
    }

    private void setOnBackPressDialog() {
        TextView textViewCancel, textViewDiscard;
        final Dialog dialogOnBackPressed = new Dialog(CollageEditorActivity.this, R.style.UploadDialog);
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
                    viewModel.rvPrimaryToolShow();
                    binding.collageView.updateLayout(queShotLayout);
                    binding.collageView.setCollagePadding(Padding);
                    binding.collageView.setCollageRadian(BorderRadius);
                    onNewAspectRatioSelected(aspectRatio);
                    binding.collageView.setAspectRatio(aspectRatio);
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
                    moduleToolsId = Module.NONE;
                    return;
                case PAINT:
                case NEON:
                case DRAW:
                    quShotCustomEditor.setBrushDrawingMode(false);
                    viewModel.rvPrimaryToolShow();
                    moduleToolsId = Module.NONE;
                    break;
                case ADDIMAGE:
                    binding.collageView.removeCurrentSticker();
                    binding.collageView.setLocked(true);
                    binding.collageView.setTouchEnable(true);
                    binding.rvPrimaryTool.setVisibility(View.VISIBLE);
                    binding.constraintLayoutConfirmSaveAddImage.setVisibility(View.GONE);
                    moduleToolsId = Module.NONE;
                    break;
                case TEXT:
                    if (!binding.collageView.getStickers().isEmpty()) {
                        binding.collageView.removeCurrentSticker();
                        binding.collageView.setHandlingSticker(null);
                    }
                    viewModel.rvPrimaryToolShow();

                    binding.collageView.setHandlingSticker(null);
                    binding.collageView.setLocked(true);

                    moduleToolsId = Module.NONE;
                    break;
                case COLLAGE:
                    binding.rvPrimaryTool.setVisibility(View.VISIBLE);
                    binding.rvSecondaryToolContainer.setVisibility(View.GONE);
                    moduleToolsId = Module.NONE;
                    binding.collageView.setQueShotGrid(null);
                    binding.collageView.setPrevHandlingQueShotGrid(null);
                    binding.collageView.invalidate();
                    moduleToolsId = Module.NONE;
                    break;

                case FILTER:
                    viewModel.rvPrimaryToolShow();
                    binding.collageView.setLocked(true);
                    binding.collageView.setTouchEnable(true);
                    for (int i = 0; i < drawableList.size(); i++) {
                        binding.collageView.getQueShotGrids().get(i).setDrawable(drawableList.get(i));
                    }
                    binding.collageView.invalidate();
                    moduleToolsId = Module.NONE;
                    break;
                case STICKER:
                    binding.collageView.setHandlingSticker(null);
                    binding.collageView.setLocked(true);
                    moduleToolsId = Module.NONE;
                    viewModel.rvPrimaryToolShow();
                    return;
                case GRADIENT:
                    viewModel.rvPrimaryToolShow();

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
    }

    public void replaceCurrentPiece(String str) {
        new OnLoadBitmapFromUri().execute(str);
    }

    public BitmapDrawable resizeBitmapDrawable(BitmapDrawable originalDrawable, int newHeight, int newWidth) {
        int imageWidth = newHeight / 3;
        int imageHeight = imageWidth * originalDrawable.getBitmap().getHeight() / originalDrawable.getBitmap().getWidth();

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalDrawable.getBitmap(), imageWidth, imageHeight, true);

        BitmapDrawable resizedDrawable = new BitmapDrawable(getResources(), resizedBitmap);
        return resizedDrawable;
    }
    public void resultAddImage(String str) {

        if (!str.isEmpty()) {
            try {
                BitmapDrawable bitmapDrawable = resizeBitmapDrawable(new BitmapDrawable(getResources(), str), binding.collageView.getMeasuredHeight(), binding.collageView.getMeasuredWidth());
                Sticker sticker = new DrawableSticker(bitmapDrawable);
                binding.collageView.addSticker(sticker);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            binding.constraintSaveControl.setVisibility(View.VISIBLE);
            binding.collageView.removeCurrentSticker();
            binding.collageView.setLocked(true);
            binding.collageView.setTouchEnable(true);
            binding.rvPrimaryTool.setVisibility(View.VISIBLE);
            binding.constraintLayoutConfirmSaveAddImage.setVisibility(View.GONE);
        }

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

    @Override
    public void addSticker(Bitmap bitmap) {
        binding.collageView.addSticker(new DrawableSticker(new BitmapDrawable(getResources(), bitmap)));
    }

    @Override
    public void onBackgroundSelected(final BackgroundGridAdapter.SquareView squareView, int position) {
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
                optionsBundle.putBoolean("ADD_IMAGE", false);
                optionsBundle.putBoolean("FREE_STYLE_ADD_IMAGE", false);
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
        public void onPreExecute() {
            setLoading(true);
        }

        @SuppressLint("WrongThread")
        public Void doInBackground(Void... voidArr) {
            listFilterAll.clear();
            listFilterAll.addAll(FilterCodeAsset.getListBitmapFilterAll(ThumbnailUtils.extractThumbnail(((BitmapDrawable) binding.collageView.getQueShotGrids().get(0).getDrawable()).getBitmap(), 70, 70)));
            return null;
        }

        public void onPostExecute(Void voidR) {
            binding.recyclerViewFilterAll.setAdapter(new FilterAdapter(listFilterAll, CollageEditorActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.ALL_FILTERS)));
            binding.constraintLayoutFilterLayout.setVisibility(View.VISIBLE);
            binding.rvPrimaryTool.setVisibility(View.GONE);
            binding.rvSecondaryToolContainer.setVisibility(View.GONE);
            binding.collageView.setLocked(false);
            binding.collageView.setTouchEnable(false);
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
            listFilterBW.addAll(FilterCodeAsset.getListBitmapFilterBW(ThumbnailUtils.extractThumbnail(((BitmapDrawable) binding.collageView.getQueShotGrids().get(0).getDrawable()).getBitmap(), 70, 70)));
            return null;
        }

        public void onPostExecute(Void voidR) {
            binding.recyclerViewFilterBW.setAdapter(new FilterAdapter(listFilterBW, CollageEditorActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.BW_FILTERS)));
            binding.collageView.setLocked(false);
            binding.collageView.setTouchEnable(false);
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
            listFilterVintage.addAll(FilterCodeAsset.getListBitmapFilterVintage(ThumbnailUtils.extractThumbnail(((BitmapDrawable) binding.collageView.getQueShotGrids().get(0).getDrawable()).getBitmap(), 70, 70)));
            return null;
        }

        public void onPostExecute(Void voidR) {
            binding.recyclerViewFilterVintage.setAdapter(new FilterAdapter(listFilterVintage, CollageEditorActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.VINTAGE_FILTERS)));
            binding.collageView.setLocked(false);
            binding.collageView.setTouchEnable(false);
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
            listFilterSmooth.addAll(FilterCodeAsset.getListBitmapFilterSmooth(ThumbnailUtils.extractThumbnail(((BitmapDrawable) binding.collageView.getQueShotGrids().get(0).getDrawable()).getBitmap(), 70, 70)));
            return null;
        }

        public void onPostExecute(Void voidR) {
            binding.recyclerViewFilterSmooth.setAdapter(new FilterAdapter(listFilterSmooth, CollageEditorActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.SMOOTH_FILTERS)));
            binding.collageView.setLocked(false);
            binding.collageView.setTouchEnable(false);
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
            listFilterCold.addAll(FilterCodeAsset.getListBitmapFilterCold(ThumbnailUtils.extractThumbnail(((BitmapDrawable) binding.collageView.getQueShotGrids().get(0).getDrawable()).getBitmap(), 70, 70)));
            return null;
        }

        public void onPostExecute(Void voidR) {
            binding.recyclerViewFilterCold.setAdapter(new FilterAdapter(listFilterCold, CollageEditorActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.COLD_FILTERS)));
            binding.collageView.setLocked(false);
            binding.collageView.setTouchEnable(false);
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
            listFilterWarm.addAll(FilterCodeAsset.getListBitmapFilterWarm(ThumbnailUtils.extractThumbnail(((BitmapDrawable) binding.collageView.getQueShotGrids().get(0).getDrawable()).getBitmap(), 70, 70)));
            return null;
        }

        public void onPostExecute(Void voidR) {
            binding.recyclerViewFilterWarm.setAdapter(new FilterAdapter(listFilterWarm, CollageEditorActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.WARM_FILTERS)));
            binding.collageView.setLocked(false);
            binding.collageView.setTouchEnable(false);
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
            listFilterLegacy.addAll(FilterCodeAsset.getListBitmapFilterLegacy(ThumbnailUtils.extractThumbnail(((BitmapDrawable) binding.collageView.getQueShotGrids().get(0).getDrawable()).getBitmap(), 70, 70)));
            return null;
        }

        public void onPostExecute(Void voidR) {
            binding.recyclerViewFilterLegacy.setAdapter(new FilterAdapter(listFilterLegacy, CollageEditorActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.LEGACY_FILTERS)));
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
            return FilterCodeAsset.getListBitmapFilterAll(ThumbnailUtils.extractThumbnail(((BitmapDrawable) binding.collageView.getQueShotGrid().getDrawable()).getBitmap(), 70, 70));
        }

        public void onPostExecute(List<Bitmap> list) {
            setLoading(false);
            if (binding.collageView.getQueShotGrid() != null) {
                FilterFragment.show(CollageEditorActivity.this, CollageEditorActivity.this, ((BitmapDrawable) binding.collageView.getQueShotGrid().getDrawable()).getBitmap(), list);
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
            return EffectCodeAsset.getListBitmapDodgeEffect(ThumbnailUtils.extractThumbnail(((BitmapDrawable) binding.collageView.getQueShotGrid().getDrawable()).getBitmap(), 70, 70));
        }

        public void onPostExecute(List<Bitmap> list) {
            setLoading(false);
            if (binding.collageView.getQueShotGrid() != null) {
                DodgeFragment.show(CollageEditorActivity.this, CollageEditorActivity.this, ((BitmapDrawable) binding.collageView.getQueShotGrid().getDrawable()).getBitmap(), list);
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
            return EffectCodeAsset.getListBitmapDivideEffect(ThumbnailUtils.extractThumbnail(((BitmapDrawable) binding.collageView.getQueShotGrid().getDrawable()).getBitmap(), 70, 70));
        }

        public void onPostExecute(List<Bitmap> list) {
            setLoading(false);
            if (binding.collageView.getQueShotGrid() != null) {
                DivideFragment.show(CollageEditorActivity.this, CollageEditorActivity.this, ((BitmapDrawable) binding.collageView.getQueShotGrid().getDrawable()).getBitmap(), list);
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
            return EffectCodeAsset.getListBitmapColorEffect(ThumbnailUtils.extractThumbnail(((BitmapDrawable) binding.collageView.getQueShotGrid().getDrawable()).getBitmap(), 70, 70));
        }

        public void onPostExecute(List<Bitmap> list) {
            setLoading(false);
            if (binding.collageView.getQueShotGrid() != null) {
                BurnFragment.show(CollageEditorActivity.this, CollageEditorActivity.this, ((BitmapDrawable) binding.collageView.getQueShotGrid().getDrawable()).getBitmap(), list);
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
                File image = SaveFileUtils.saveBitmapFileCollage(CollageEditorActivity.this, createBitmap, new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date()));
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
            Intent intent = new Intent(CollageEditorActivity.this, PhotoShareActivity.class);
            intent.putExtra("path", str);
            startActivity(intent);
        }
    }

    public void setLoading(boolean isShowing) {
        if (isShowing) {
            binding.relativeLayoutLoading.setVisibility(View.VISIBLE);
            return;
        }
        binding.relativeLayoutLoading.setVisibility(View.GONE);
    }

}
