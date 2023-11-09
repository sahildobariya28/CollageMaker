package com.photo.collagemaker.activities.freestyle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
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
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.TextView;

import com.photo.collagemaker.R;
import com.photo.collagemaker.activities.PhotoShareActivity;
import com.photo.collagemaker.activities.editor.collage_editor.adapter.AspectAdapter;
import com.photo.collagemaker.activities.editor.collage_editor.adapter.BackgroundGridAdapter;
import com.photo.collagemaker.activities.editor.collage_editor.adapter.ColorAdapter;
import com.photo.collagemaker.activities.editor.collage_editor.adapter.FilterAdapter;
import com.photo.collagemaker.activities.editor.collage_editor.adapter.GridToolsAdapter;
import com.photo.collagemaker.activities.editor.collage_editor.adapter.StickerAdapter;
import com.photo.collagemaker.activities.editor.collage_editor.adapter.StickerTabAdapter;
import com.photo.collagemaker.activities.picker.MultipleImagePickerActivity;
import com.photo.collagemaker.activities.picker.SingleImagePickerActivity;
import com.photo.collagemaker.assets.FilterCodeAsset;
import com.photo.collagemaker.assets.StickersAsset;
import com.photo.collagemaker.custom_view.CustomText;
import com.photo.collagemaker.custom_view.CustomTextView;
import com.photo.collagemaker.custom_view.StickerIcons;
import com.photo.collagemaker.custom_view.StickerView;
import com.photo.collagemaker.databinding.ActivityFreeStyleBinding;
import com.photo.collagemaker.draw.Drawing;
import com.photo.collagemaker.event.AlignHorizontallyEvent;
import com.photo.collagemaker.event.DeleteIconEvent;
import com.photo.collagemaker.event.EditTextIconEvent;
import com.photo.collagemaker.event.FlipHorizontallyEvent;
import com.photo.collagemaker.event.ZoomIconEvent;
import com.photo.collagemaker.fragment.TextFragment;
import com.photo.collagemaker.listener.BrushColorListener;
import com.photo.collagemaker.listener.FilterListener;
import com.photo.collagemaker.listener.OnQuShotEditorListener;
import com.photo.collagemaker.module.Module;
import com.photo.collagemaker.picker.PermissionsUtils;
import com.photo.collagemaker.sticker.DrawableSticker;
import com.photo.collagemaker.sticker.Sticker;
import com.photo.collagemaker.utils.FilterUtils;
import com.photo.collagemaker.utils.SaveFileUtils;
import com.photo.collagemaker.utils.SystemUtil;
import com.skydoves.colorpickerview.listeners.ColorListener;
import com.steelkiwi.cropiwa.AspectRatio;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class FreeStyleActivity extends AppCompatActivity implements FilterListener, GridToolsAdapter.OnItemSelected, StickerAdapter.OnClickSplashListener, AspectAdapter.OnNewSelectedListener, BackgroundGridAdapter.BackgroundGridListener, BrushColorListener, BorderColorAdapter.BorderColorListener {

    public int selectedStickerPosition = 0;
    public List<String> imageList;
    public Module moduleToolsId = Module.NONE;
    public AspectRatio aspectRatio;
    public BackgroundGridAdapter.SquareView currentBackgroundState;
    public CustomEditorForFreeStyle quShotCustomEditor;

    ActivityFreeStyleBinding binding;
    FreeStyleViewModel viewModel;
    public static FreeStyleActivity freeStyleInstance;


    //filter
    public ArrayList listFilterAll = new ArrayList<>();
    public ArrayList listFilterBW = new ArrayList<>();
    public ArrayList listFilterVintage = new ArrayList<>();
    public ArrayList listFilterSmooth = new ArrayList<>();
    public ArrayList listFilterCold = new ArrayList<>();
    public ArrayList listFilterWarm = new ArrayList<>();
    public ArrayList listFilterLegacy = new ArrayList<>();
    public List<Sticker> drawableList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        freeStyleInstance = this;
        binding = ActivityFreeStyleBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this, new FreeStyleViewModelFactory(this, binding)).get(FreeStyleViewModel.class);
        setContentView(binding.getRoot());

        imageList = getIntent().getStringArrayListExtra(MultipleImagePickerActivity.KEY_DATA_RESULT);
        binding.stickerView.post(() -> {
            initStickerView();
            loadImage();
            initBackgroundColor();
            initAddImage();
            initTextEditView();
            initDrawingView();
            binding.stickerView.requestLayout();
            initFilterView();
            setLoading(false);
        });

        binding.btnBack.setOnClickListener(view -> {
            onBackPressed();
        });
        initToolBar();
    }

    public void initFilterView() {
        binding.imageViewSaveFilter.setOnClickListener(view -> {
            viewModel.rvPrimaryToolShow();
            moduleToolsId = Module.NONE;
        });

        binding.imageViewCloseFilter.setOnClickListener(view -> {
            viewModel.rvPrimaryToolShow();
            onBackPressed();
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

    public void initTextEditView() {
        binding.relativeLayoutAddText.setOnClickListener(view -> {
            binding.stickerView.setHandlingSticker(null);
            textFragment();
        });

        binding.imageViewCloseText.setOnClickListener(view -> {
            binding.constraintLayoutConfirmText.setVisibility(View.GONE);
            binding.relativeLayoutAddText.setVisibility(View.GONE);
            binding.rvPrimaryTool.setVisibility(View.VISIBLE);
            onBackPressed();
        });
        binding.imageViewSaveText.setOnClickListener(view -> {
            binding.stickerView.setHandlingSticker(null);
            if (!binding.stickerView.getStickers().isEmpty()) {
//                new GridActivity.SaveSticker().execute();
            }
            binding.constraintLayoutConfirmText.setVisibility(View.GONE);
            binding.relativeLayoutAddText.setVisibility(View.GONE);
            binding.rvPrimaryTool.setVisibility(View.VISIBLE);
            moduleToolsId = Module.NONE;
        });
    }

    public TextFragment addTextFragment;
    public TextFragment.TextEditor textEditor;

    public void textFragment() {
        addTextFragment = TextFragment.show(this);
        textEditor = new TextFragment.TextEditor() {
            public void onDone(CustomText addTextProperties) {
                binding.stickerView.addSticker(new CustomTextView(getApplicationContext(), addTextProperties));
            }

            public void onBackButton() {
                if (binding.stickerView.getStickers().isEmpty()) {
                    onBackPressed();
                }
            }
        };
        addTextFragment.setOnTextEditorListener(textEditor);
    }

    public void initDrawingView() {
        quShotCustomEditor = new CustomEditorForFreeStyle.Builder(this, binding.stickerView).setPinchTextScalable(true).build();
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
            ConstraintSet constraintSet;
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
            ConstraintSet constraintSet;
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

    public void reloadingLayout() {
        binding.stickerView.postDelayed(() -> {
            try {
                binding.stickerView.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                e.printStackTrace();
            }
            setLoading(false);
        }, 300);
    }


    public void initAddImage() {

    }

    public void initToolBar() {
        GridToolsAdapter gridToolsAdapter = new GridToolsAdapter(this, true);
        binding.rvPrimaryTool.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.rvPrimaryTool.setAdapter(gridToolsAdapter);
    }

    public void initStickerView() {
        aspectRatio = new AspectRatio(1, 1);
        onNewAspectRatioSelected(aspectRatio);
        binding.stickerView.setLocked(false);

        StickerIcons quShotStickerIconClose = new StickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_close), 0, StickerIcons.DELETE);
        quShotStickerIconClose.setIconEvent(new DeleteIconEvent());
        quShotStickerIconClose.setShow(true);
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

        binding.btnShuffle.setOnClickListener(view -> {
            Random random = new Random();
            for (int i = 0; i < binding.stickerView.getStickers().size(); i++) {
                // Generate a random X and Y position
                float randomX = random.nextFloat() * binding.stickerView.getWidth(); // Random X within the view width
                float randomY = random.nextFloat() * binding.stickerView.getHeight(); // Random Y within the view height

                // Generate a random rotation angle in degrees
                float randomRotationDegrees;
                if (random.nextBoolean()) {
                    // Randomly choose between 270 to 360 or 0 to 90
                    randomRotationDegrees = random.nextFloat() * 45; // 0 to 90 degrees
                } else {
                    randomRotationDegrees = 315 + random.nextFloat() * 90; // 270 to 360 degrees
                }

                // Apply the translation (position) and rotation to the sticker
                Matrix matrix = binding.stickerView.getStickers().get(i).getMatrix();


                matrix.reset(); // Reset the matrix to an identity matrix
                matrix.postScale(0.4f, 0.4f);
                matrix.postTranslate(randomX, randomY); // Translate to the random X and Y position
                matrix.postRotate(randomRotationDegrees, binding.stickerView.getWidth() / 2, binding.stickerView.getHeight() / 2);
                binding.stickerView.getStickers().get(i).setMatrix(matrix);
            }
        });

        binding.stickerView.setIcons(Arrays.asList(quShotStickerIconClose, quShotStickerIconScale, quShotStickerIconFlip, quShotStickerIconEdit, quShotStickerIconRotate, quShotStickerIconCenter));
        binding.stickerView.setConstrained(true);
        binding.stickerView.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
            @Override
            public void onAddSticker(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerSelected(@NonNull Sticker sticker, int position) {
                selectedStickerPosition = position;
                Log.d("fdsfjskljfwe23", "onStickerSelected: " + selectedStickerPosition);
            }

            @Override
            public void onStickerDeleted(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerDoubleTap(@NonNull Sticker sticker) {
                if (sticker instanceof CustomTextView) {
                    sticker.setShow(false);
                    binding.stickerView.setHandlingSticker(null);
                    addTextFragment = TextFragment.show(FreeStyleActivity.this, ((CustomTextView) sticker).getQuShotText());
                    textEditor = new TextFragment.TextEditor() {
                        public void onDone(CustomText addTextProperties) {
                            binding.stickerView.getStickers().remove(binding.stickerView.getLastHandlingSticker());
                            binding.stickerView.addSticker(new CustomTextView(FreeStyleActivity.this, addTextProperties));
                        }

                        public void onBackButton() {
                            binding.stickerView.showLastHandlingSticker();
                        }
                    };
                    addTextFragment.setOnTextEditorListener(textEditor);
                }
            }

            @Override
            public void onStickerDrag(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerFlip(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerTouchOutside() {

            }

            @Override
            public void onStickerTouchedDown(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerZoom(@NonNull Sticker sticker) {

            }

            @Override
            public void onTouchDownBeauty(float f, float f2) {

            }

            @Override
            public void onTouchDragBeauty(float f, float f2) {

            }

            @Override
            public void onTouchUpBeauty(float f, float f2) {

            }
        });


        binding.relativeLayoutAddSticker.setVisibility(View.GONE);
        binding.relativeLayoutAddSticker.setOnClickListener(view -> {
            binding.relativeLayoutAddSticker.setVisibility(View.GONE);
            binding.linearLayoutWrapperStickerList.setVisibility(View.VISIBLE);
        });
        binding.btnDone.setOnClickListener(view -> {
            if (PermissionsUtils.checkWriteStoragePermission(FreeStyleActivity.this)) {
                Bitmap createBitmap = SaveFileUtils.createBitmap(binding.stickerView, 1920);
                Bitmap createBitmap2 = binding.stickerView.createBitmap();
                new SaveCollageAsFile().execute(createBitmap, createBitmap2);
            }
        });


        binding.imageViewSaveSticker.setOnClickListener(view -> {
            binding.stickerView.setHandlingSticker(null);
            binding.constraintLayoutSticker.setVisibility(View.GONE);
            binding.linearLayoutWrapperStickerList.setVisibility(View.GONE);
            binding.linearLayoutWrapperStickerList.setVisibility(View.VISIBLE);
            binding.rvPrimaryTool.setVisibility(View.VISIBLE);
            binding.constraintSaveControl.setVisibility(View.VISIBLE);

            moduleToolsId = Module.NONE;

        });
        binding.imageViewCloseSticker.setOnClickListener(view -> {

            binding.rvPrimaryTool.setVisibility(View.VISIBLE);
            binding.constraintSaveControl.setVisibility(View.VISIBLE);
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
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), StickersAsset.mListEmojy(), i, FreeStyleActivity.this));
                        break;
                    case 1:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), StickersAsset.mListFlag(), i, FreeStyleActivity.this));
                        break;
                    case 2:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), StickersAsset.mListBoom(), i, FreeStyleActivity.this));
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


    public void initBackgroundColor() {
        currentBackgroundState = new BackgroundGridAdapter.SquareView(getColor(R.color.white), "", true);

        AspectAdapter aspectRatioPreviewAdapter = new AspectAdapter(this);
        aspectRatioPreviewAdapter.setListener(this);
        binding.recyclerViewRatio.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recyclerViewRatio.setAdapter(aspectRatioPreviewAdapter);

        binding.recyclerBorderColor.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        binding.recyclerBorderColor.setHasFixedSize(true);
        binding.recyclerBorderColor.setAdapter(new BorderColorAdapter(getApplicationContext(), this));

        binding.backgroundContainer.recyclerViewColor.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        binding.backgroundContainer.recyclerViewColor.setHasFixedSize(true);
        binding.backgroundContainer.recyclerViewColor.setAdapter(new BackgroundGridAdapter(getApplicationContext(), this));
        this.onBackgroundSelected(new BackgroundGridAdapter.SquareView(Color.parseColor("#ffffff"), "", true), 0, false);

        binding.backgroundContainer.recyclerViewGradient.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        binding.backgroundContainer.recyclerViewGradient.setHasFixedSize(true);
        binding.backgroundContainer.recyclerViewGradient.setAdapter(new BackgroundGridAdapter(getApplicationContext(), this, true));

        BackgroundGridAdapter backgroundGridAdapter = new BackgroundGridAdapter(getApplicationContext(), this, true);
        binding.backgroundContainer.recyclerViewBlur.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        binding.backgroundContainer.recyclerViewBlur.setHasFixedSize(true);
        backgroundGridAdapter.setBlur(true);
        binding.backgroundContainer.recyclerViewBlur.setAdapter(backgroundGridAdapter);

        BackgroundGridAdapter backgroundCustomGridAdapter = new BackgroundGridAdapter(getApplicationContext(), this, true);
        binding.backgroundContainer.recyclerViewCustom.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        binding.backgroundContainer.recyclerViewCustom.setHasFixedSize(true);
        backgroundCustomGridAdapter.setBlur(false);
        binding.backgroundContainer.recyclerViewCustom.setAdapter(backgroundCustomGridAdapter);

        binding.linearLayoutBorder.setOnClickListener(view -> viewModel.borderShow());
        binding.linearLayoutRatio.setOnClickListener(view -> viewModel.ratioShow());
        binding.linearLayoutBackground.setOnClickListener(view -> viewModel.bgShow());

        binding.backgroundContainer.btnColor.setOnClickListener(view -> setBackgroundColor());
        binding.backgroundContainer.btnGradient.setOnClickListener(view -> setBackgroundGradient());
        binding.backgroundContainer.btnCustom.setOnClickListener(view -> selectBackgroundCustom());
        binding.backgroundContainer.btnBlur.setOnClickListener(view -> selectBackgroundBlur());
        binding.backgroundContainer.btnSelect.setOnClickListener(view -> selectColorPicker());
        binding.backgroundContainer.btnWhite.setOnClickListener(view -> binding.stickerView.setBackgroundColor(ContextCompat.getColor(this, R.color.white)));
        binding.backgroundContainer.btnBlack.setOnClickListener(view -> binding.stickerView.setBackgroundColor(ContextCompat.getColor(this, R.color.black)));

        binding.seekbarBorder.setOnSeekBarChangeListener(onSeekBarChangeListener);
        binding.seekbarRadius.setOnSeekBarChangeListener(onSeekBarChangeListener);

        binding.imageViewSaveLayer.setOnClickListener(view -> {


            aspectRatio = binding.stickerView.getAspectRatio();
            moduleToolsId = Module.NONE;
            if (binding.colorPickerView.isSelected()) {
                binding.stickerView.setBackgroundColor(binding.colorPickerView.getColor());
            }

            binding.rvPrimaryTool.setVisibility(View.VISIBLE);
            binding.constraintSaveControl.setVisibility(View.VISIBLE);
            binding.backgroundContainer.backgroundTools.setVisibility(View.GONE);
            binding.constrantLayoutChangeLayout.setVisibility(View.GONE);
            binding.backgroundContainer.recyclerViewColor.setVisibility(View.GONE);
            binding.backgroundContainer.recyclerViewBlur.setVisibility(View.GONE);
            binding.backgroundContainer.recyclerViewGradient.setVisibility(View.GONE);
            binding.backgroundContainer.getRoot().setVisibility(View.GONE);
            if (binding.stickerView.getBackground() != null) {
                if (binding.stickerView.getBackgroundResourceMode() == 0) {
                    currentBackgroundState.isColor = true;
                    currentBackgroundState.isBitmap = false;
                    currentBackgroundState.drawableId = ((ColorDrawable) binding.stickerView.getBackground()).getColor();
                    currentBackgroundState.drawable = null;
                } else if (binding.stickerView.getBackgroundResourceMode() == 1) {
                    currentBackgroundState.isColor = false;
                    currentBackgroundState.isBitmap = false;
                    currentBackgroundState.drawable = binding.stickerView.getBackground();
                } else {
                    currentBackgroundState.isColor = false;
                    currentBackgroundState.isBitmap = true;
                    currentBackgroundState.drawable = binding.stickerView.getBackground();
                }
            }

        });
    }

    public SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
        }

        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            int id = seekBar.getId();
            if (id == R.id.seekbarBorder) {
                for (int j = 0; j < binding.stickerView.getStickers().size(); j++) {
                    binding.stickerView.getStickers().get(j).setExtraBorderWidth((float) i);
                }

            } else if (id == R.id.seekbarRadius) {
                for (int j = 0; j < binding.stickerView.getStickers().size(); j++) {
                    binding.stickerView.getStickers().get(j).setCornerRadius((float) i);
                }
            }
            binding.stickerView.invalidate();
        }
    };

    public void selectBackgroundBlur() {
        binding.backgroundContainer.backgroundTools.setVisibility(View.GONE);
        binding.backgroundContainer.recyclerViewColor.setVisibility(View.GONE);
        binding.backgroundContainer.recyclerViewGradient.setVisibility(View.GONE);
        binding.backgroundContainer.recyclerViewCustom.setVisibility(View.GONE);
        binding.backgroundContainer.recyclerViewBlur.setVisibility(View.VISIBLE);


        ArrayList arrayList = new ArrayList();
        for (String drawable : imageList) {
            arrayList.add(new BitmapDrawable(getResources(), drawable));
        }
        BackgroundGridAdapter backgroundGridAdapter = new BackgroundGridAdapter(getApplicationContext(), this, (List<Drawable>) arrayList);
        backgroundGridAdapter.setSelectedIndex(-1);
        backgroundGridAdapter.setBlur(true);
        binding.backgroundContainer.recyclerViewBlur.setAdapter(backgroundGridAdapter);

    }

    public void selectBackgroundCustom() {
        binding.backgroundContainer.backgroundTools.setVisibility(View.GONE);
        binding.backgroundContainer.recyclerViewBlur.setVisibility(View.GONE);
        binding.backgroundContainer.recyclerViewColor.setVisibility(View.GONE);
        binding.backgroundContainer.recyclerViewGradient.setVisibility(View.GONE);
        binding.backgroundContainer.recyclerViewCustom.setVisibility(View.VISIBLE);


        ArrayList arrayList = new ArrayList();
        for (String drawable : imageList) {
            arrayList.add(new BitmapDrawable(getResources(), drawable));
        }
        BackgroundGridAdapter backgroundGridAdapter = new BackgroundGridAdapter(getApplicationContext(), this, (List<Drawable>) arrayList);
        backgroundGridAdapter.setSelectedIndex(-1);
        backgroundGridAdapter.setBlur(false);
        binding.backgroundContainer.recyclerViewCustom.setAdapter(backgroundGridAdapter);

    }

    public void selectColorPicker() {

        binding.colorPickerView.setVisibility(View.VISIBLE);

        int viewX = binding.colorPickerView.getLeft() + (binding.colorPickerView.getRight() - binding.colorPickerView.getLeft()) / 2;
        int viewY = binding.colorPickerView.getTop() + (binding.colorPickerView.getBottom() - binding.colorPickerView.getTop()) / 2;
        binding.colorPickerView.setSelectorPoint(viewX, viewY);


        binding.colorPickerView.setPaletteDrawable(new BitmapDrawable(getResources(), binding.stickerView.createBitmap()));
        binding.colorPickerView.setColorListener((ColorListener) (color, fromUser) -> {
            binding.backgroundContainer.selectedColorPreview.setCardBackgroundColor(ColorStateList.valueOf(color));
            binding.backgroundContainer.selectedColorPreview.getBackground().setTint(color);
        });

    }

    public void setBackgroundGradient() {
        binding.backgroundContainer.backgroundTools.setVisibility(View.GONE);
        binding.backgroundContainer.recyclerViewColor.setVisibility(View.GONE);
        binding.backgroundContainer.recyclerViewCustom.setVisibility(View.GONE);
        binding.backgroundContainer.recyclerViewGradient.setVisibility(View.VISIBLE);
        binding.backgroundContainer.recyclerViewBlur.setVisibility(View.GONE);

        binding.backgroundContainer.recyclerViewGradient.scrollToPosition(0);
        ((BackgroundGridAdapter) binding.backgroundContainer.recyclerViewGradient.getAdapter()).setSelectedIndex(-1);
        binding.backgroundContainer.recyclerViewGradient.getAdapter().notifyDataSetChanged();


    }


    public void setBackgroundColor() {
        binding.backgroundContainer.backgroundTools.setVisibility(View.GONE);
        binding.backgroundContainer.recyclerViewColor.setVisibility(View.VISIBLE);
        binding.backgroundContainer.recyclerViewGradient.setVisibility(View.GONE);
        binding.backgroundContainer.recyclerViewCustom.setVisibility(View.GONE);
        binding.backgroundContainer.recyclerViewBlur.setVisibility(View.GONE);

        binding.backgroundContainer.recyclerViewColor.scrollToPosition(0);
        ((BackgroundGridAdapter) binding.backgroundContainer.recyclerViewColor.getAdapter()).setSelectedIndex(-1);
        binding.backgroundContainer.recyclerViewColor.getAdapter().notifyDataSetChanged();

    }

    public BitmapDrawable resizeBitmapDrawable(BitmapDrawable originalDrawable, int newHeight, int newWidth) {
        int imageWidth = newHeight / 3;
        int imageHeight = imageWidth * originalDrawable.getBitmap().getHeight() / originalDrawable.getBitmap().getWidth();

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalDrawable.getBitmap(), imageWidth, imageHeight, true);

        BitmapDrawable resizedDrawable = new BitmapDrawable(getResources(), resizedBitmap);
        return resizedDrawable;
    }

    public void loadImage() {
        for (int i = 0; i < imageList.size(); i++) {

            BitmapDrawable bitmapDrawable = resizeBitmapDrawable(new BitmapDrawable(getResources(), imageList.get(i)), binding.stickerView.getMeasuredHeight(), binding.stickerView.getMeasuredWidth());

//            Sticker sticker = new DrawableSticker(viewModel.addWhiteBorder(getResources(), bitmapDrawable.getBitmap(), 10, Color.GREEN));
            Sticker sticker = new DrawableSticker(bitmapDrawable);
            drawableList.add(sticker);
            binding.stickerView.addSticker(sticker);
            binding.stickerView.setHandlingSticker(null);
        }
    }


    @Override
    public void onToolSelected(Module toolType) {
        onNewAspectRatioSelected(aspectRatio);
        switch (toolType) {
            case ADDIMAGE:
                moduleToolsId = Module.ADDIMAGE;
                Intent intent = new Intent(this, SingleImagePickerActivity.class);
                Bundle optionsBundle = new Bundle();
                optionsBundle.putInt("MAX_COUNT", 1);
                optionsBundle.putBoolean("PREVIEW_ENABLED", false);
                optionsBundle.putBoolean("SHOW_CAMERA", false);
                optionsBundle.putBoolean("MAIN_ACTIVITY", true);
                optionsBundle.putBoolean("ADD_IMAGE", false);
                optionsBundle.putBoolean("FREE_STYLE_ADD_IMAGE", true);
                intent.putExtras(optionsBundle);
                startActivityForResult(intent, 266);
                break;
            case PADDING:
                moduleToolsId = Module.PADDING;
                viewModel.borderShow();
                break;
            case GRADIENT:
                moduleToolsId = Module.GRADIENT;
                aspectRatio = binding.stickerView.getAspectRatio();
                viewModel.backgroundToolShow();
                break;
            case STICKER:
                moduleToolsId = Module.STICKER;
                binding.stickerView.setAspectRatio(aspectRatio);
                viewModel.stickerShow();
                break;
            case DRAW:
                moduleToolsId = Module.DRAW;
                viewModel.drawShow();
                break;
            case TEXT:
                moduleToolsId = Module.TEXT;
                viewModel.textShow();
                textFragment();
                break;
            case FILTER:
                moduleToolsId = Module.FILTER;
                if (drawableList.isEmpty()) {
                    for (int i = 0; i < drawableList.size(); i++) {
                        binding.stickerView.setSticker(drawableList.get(i), i);
                        binding.stickerView.invalidate();
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
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (moduleToolsId == null) {
            super.onBackPressed();
        }

        try {
            switch (moduleToolsId) {
                case FILTER:
                    viewModel.rvPrimaryToolShow();
                    for (int i = 0; i < drawableList.size(); i++) {
                        binding.stickerView.setSticker(drawableList.get(i), i);
                        binding.stickerView.invalidate();
                    }
                    binding.stickerView.invalidate();
                    moduleToolsId = Module.NONE;
                    return;
                case TEXT:
                    viewModel.rvPrimaryToolShow();

                    binding.stickerView.setHandlingSticker(null);

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
                    moduleToolsId = Module.NONE;
                    break;
                case GRADIENT:
                    viewModel.rvPrimaryToolShow();

                    if (currentBackgroundState.isColor) {
                        binding.stickerView.setBackgroundResourceMode(0);
                        binding.stickerView.setBackgroundColor(currentBackgroundState.drawableId);
                    } else if (currentBackgroundState.isBitmap) {
                        binding.stickerView.setBackgroundResourceMode(2);
                        binding.stickerView.setBackground(currentBackgroundState.drawable);
                    } else {
                        binding.stickerView.setBackgroundResourceMode(1);
                        if (currentBackgroundState.drawable != null) {
                            binding.stickerView.setBackground(currentBackgroundState.drawable);
                        } else {
                            binding.stickerView.setBackgroundResource(currentBackgroundState.drawableId);
                        }
                    }

                    moduleToolsId = Module.NONE;
                    break;
                case STICKER:
                    binding.stickerView.setHandlingSticker(null);
                    binding.stickerView.setLocked(true);
                    moduleToolsId = Module.NONE;
                    viewModel.rvPrimaryToolShow();
                    break;
                case NONE:
                    TextView textViewCancel, textViewDiscard;
                    final Dialog dialogOnBackPressed = new Dialog(FreeStyleActivity.this, R.style.UploadDialog);
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
                    break;
                default:
                    super.onBackPressed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFilterSelected(String str) {
        new LoadBitmapWithFilter().execute(str);
    }

    @Override
    public void onBorderColorSelected(BorderColorAdapter.SquareView squareView, int position) {
        for (int i = 0; i < binding.stickerView.getStickers().size(); i++) {
            binding.stickerView.getStickers().get(i).setExtraBorderColor(squareView.drawableId);
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
            for (Sticker sticker : drawableList) {
                arrayList.add(FilterUtils.getBitmapWithFilter(drawableToBitmap(sticker.getDrawable()), strArr[0]));
            }
            return arrayList;
        }


        public void onPostExecute(List<Bitmap> list) {
            for (int i = 0; i < list.size(); i++) {

//                BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), list.get(i));
//                bitmapDrawable.setAntiAlias(true);
//                bitmapDrawable.setFilterBitmap(true);
//                binding.collageView.getQueShotGrids().get(i).setDrawable(bitmapDrawable);

                BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), list.get(i));
                bitmapDrawable.setAntiAlias(true);
                bitmapDrawable.setFilterBitmap(true);

                Sticker sticker = new DrawableSticker(viewModel.addWhiteBorder(getResources(), bitmapDrawable.getBitmap(), 10, Color.GREEN));
                Matrix matrix = binding.stickerView.getStickers().get(i).getMatrix();
                binding.stickerView.setSticker(sticker, i);
                binding.stickerView.getStickers().get(i).setMatrix(matrix);
                binding.stickerView.setHandlingSticker(null);
            }
            binding.stickerView.invalidate();
            setLoading(false);
        }
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    class allFilters extends AsyncTask<Void, Void, Void> {
        public void onPreExecute() {
            setLoading(true);
        }

        @SuppressLint("WrongThread")
        public Void doInBackground(Void... voidArr) {
            listFilterAll.clear();
            listFilterAll.addAll(FilterCodeAsset.getListBitmapFilterAll(ThumbnailUtils.extractThumbnail(((BitmapDrawable) new BitmapDrawable(getResources(), imageList.get(0))).getBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voidR) {
            binding.recyclerViewFilterAll.setAdapter(new FilterAdapter(listFilterAll, FreeStyleActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.ALL_FILTERS)));
            binding.constraintLayoutFilterLayout.setVisibility(View.VISIBLE);
            binding.constraintLayoutConfirmSaveFilter.setVisibility(View.VISIBLE);
            binding.rvPrimaryTool.setVisibility(View.GONE);
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
            listFilterBW.addAll(FilterCodeAsset.getListBitmapFilterBW(ThumbnailUtils.extractThumbnail(((BitmapDrawable) new BitmapDrawable(getResources(), imageList.get(0))).getBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voidR) {
            binding.recyclerViewFilterBW.setAdapter(new FilterAdapter(listFilterBW, FreeStyleActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.BW_FILTERS)));
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
            listFilterVintage.addAll(FilterCodeAsset.getListBitmapFilterVintage(ThumbnailUtils.extractThumbnail(((BitmapDrawable) new BitmapDrawable(getResources(), imageList.get(0))).getBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voidR) {
            binding.recyclerViewFilterVintage.setAdapter(new FilterAdapter(listFilterVintage, FreeStyleActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.VINTAGE_FILTERS)));

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
            listFilterSmooth.addAll(FilterCodeAsset.getListBitmapFilterSmooth(ThumbnailUtils.extractThumbnail(((BitmapDrawable) new BitmapDrawable(getResources(), imageList.get(0))).getBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voidR) {
            binding.recyclerViewFilterSmooth.setAdapter(new FilterAdapter(listFilterSmooth, FreeStyleActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.SMOOTH_FILTERS)));
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
            listFilterCold.addAll(FilterCodeAsset.getListBitmapFilterCold(ThumbnailUtils.extractThumbnail(((BitmapDrawable) new BitmapDrawable(getResources(), imageList.get(0))).getBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voidR) {
            binding.recyclerViewFilterCold.setAdapter(new FilterAdapter(listFilterCold, FreeStyleActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.COLD_FILTERS)));
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
            listFilterWarm.addAll(FilterCodeAsset.getListBitmapFilterWarm(ThumbnailUtils.extractThumbnail(((BitmapDrawable) new BitmapDrawable(getResources(), imageList.get(0))).getBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voidR) {
            binding.recyclerViewFilterWarm.setAdapter(new FilterAdapter(listFilterWarm, FreeStyleActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.WARM_FILTERS)));
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
            listFilterLegacy.addAll(FilterCodeAsset.getListBitmapFilterLegacy(ThumbnailUtils.extractThumbnail(((BitmapDrawable) new BitmapDrawable(getResources(), imageList.get(0))).getBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voidR) {
            binding.recyclerViewFilterLegacy.setAdapter(new FilterAdapter(listFilterLegacy, FreeStyleActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.LEGACY_FILTERS)));
            setLoading(false);
        }
    }

    public static FreeStyleActivity getQueShotGridActivityInstance() {
        return freeStyleInstance;
    }

    public void resultAddImage(String str) {

        if (!str.isEmpty()) {
            try {
                Uri fromFile = Uri.fromFile(new File(str));
                Bitmap bitmap = SystemUtil.rotateBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), fromFile), new ExifInterface(getContentResolver().openInputStream(fromFile)).getAttributeInt(ExifInterface.TAG_ORIENTATION, 1));
                binding.stickerView.addSticker(new DrawableSticker(new BitmapDrawable(getResources(), bitmap)));
                moduleToolsId = Module.NONE;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            binding.stickerView.removeCurrentSticker();
        }

    }

    @Override
    public void addSticker(Bitmap bitmap) {
        binding.stickerView.addSticker(new DrawableSticker(new BitmapDrawable(getResources(), bitmap)));
        binding.linearLayoutWrapperStickerList.setVisibility(View.GONE);
        binding.relativeLayoutAddSticker.setVisibility(View.VISIBLE);
    }

    public void onNewAspectRatioSelected(AspectRatio aspectRatio) {
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        int[] calculateWidthAndHeight = calculateWidthAndHeight(aspectRatio, point);
        binding.stickerView.setLayoutParams(new ConstraintLayout.LayoutParams(calculateWidthAndHeight[0], calculateWidthAndHeight[1]));
        binding.stickerView.setAspectRatio(aspectRatio);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(binding.constraintLayoutWrapperCollageView);
        constraintSet.connect(binding.stickerView.getId(), 3, binding.constraintLayoutWrapperCollageView.getId(), 3, 0);
        constraintSet.connect(binding.stickerView.getId(), 1, binding.constraintLayoutWrapperCollageView.getId(), 1, 0);
        constraintSet.connect(binding.stickerView.getId(), 4, binding.constraintLayoutWrapperCollageView.getId(), 4, 0);
        constraintSet.connect(binding.stickerView.getId(), 2, binding.constraintLayoutWrapperCollageView.getId(), 2, 0);
        constraintSet.applyTo(binding.constraintLayoutWrapperCollageView);
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
    public void onBackgroundSelected(BackgroundGridAdapter.SquareView squareView, int position, boolean isBlur) {
        if (squareView.isColor) {
            binding.stickerView.setBackgroundColor(squareView.drawableId);
            binding.stickerView.setBackgroundResourceMode(0);
        } else if (squareView.drawable != null) {
            binding.stickerView.setBackgroundResourceMode(2);

            AsyncTask<Void, Bitmap, Bitmap> asyncTask = new AsyncTask<Void, Bitmap, Bitmap>() {
                @Override
                protected Bitmap doInBackground(Void... voidArr) {
                    if (isBlur) {
                        return FilterUtils.getBlurImageFromBitmap(((BitmapDrawable) squareView.drawable).getBitmap(), 5.0f);
                    } else {
                        return FilterUtils.getBlurImageFromBitmap(((BitmapDrawable) squareView.drawable).getBitmap(), 0f);
                    }
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    int targetWidth = binding.stickerView.getWidth();  // Replace with the desired x position
                    int targetHeight = binding.stickerView.getHeight();
                    Bitmap originalBitmap = bitmap;

                    if (originalBitmap != null) {
                        int originalWidth = originalBitmap.getWidth();
                        int originalHeight = originalBitmap.getHeight();

                        float scaleX = (float) targetWidth / originalWidth;
                        float scaleY = (float) targetHeight / originalHeight;
                        float scaleFactor = Math.max(scaleX, scaleY);

                        int newWidth = (int) (originalWidth * scaleFactor);
                        int newHeight = (int) (originalHeight * scaleFactor);

                        int left = (newWidth - targetWidth) / 2;
                        int top = (newHeight - targetHeight) / 2;

                        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
                        Bitmap croppedBitmap = Bitmap.createBitmap(scaledBitmap, left, top, targetWidth, targetHeight);
                        binding.stickerView.setBackground(new BitmapDrawable(getResources(), croppedBitmap));
                        originalBitmap.recycle(); // Release the original bitmap to free up memory
                    }
//                    binding.stickerView.setBackground(new BitmapDrawable(getResources(), bitmap));
                }
            };

            asyncTask.execute();
        } else {
            binding.stickerView.setBackgroundResource(squareView.drawableId);
            binding.stickerView.setBackgroundResourceMode(1);
        }
    }

    public void setLoading(boolean z) {
        if (z) {
            binding.relativeLayoutLoading.setVisibility(View.VISIBLE);
            return;
        }
        binding.relativeLayoutLoading.setVisibility(View.GONE);
    }

    @Override
    public void onColorChanged(String str) {
        quShotCustomEditor.setBrushColor(Color.parseColor(str));
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
                File image = SaveFileUtils.saveBitmapFileCollage(FreeStyleActivity.this, createBitmap, new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date()));
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
            Intent intent = new Intent(FreeStyleActivity.this, PhotoShareActivity.class);
            intent.putExtra("path", str);
            startActivity(intent);
        }
    }

}