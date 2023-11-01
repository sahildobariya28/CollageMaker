package com.photo.collagemaker.activities.editor.single_editor;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.internal.view.SupportMenu;
import androidx.exifinterface.media.ExifInterface;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.photo.collagemaker.R;
import com.photo.collagemaker.activities.PhotoShareActivity;
import com.photo.collagemaker.activities.editor.single_editor.adapter.AdjustAdapter;
import com.photo.collagemaker.activities.editor.single_editor.adapter.HardmixAdapter;
import com.photo.collagemaker.activities.editor.single_editor.adapter.MenBeautyAdapter;
import com.photo.collagemaker.activities.editor.single_editor.adapter.QueShotToolsAdapter;
import com.photo.collagemaker.activities.editor.single_editor.adapter.WomenBeautyAdapter;
import com.photo.collagemaker.activities.editor.single_editor.adapter.ColorAdapter;
import com.photo.collagemaker.activities.editor.single_editor.adapter.QueShotDrawToolsAdapter;
import com.photo.collagemaker.activities.editor.single_editor.adapter.StickerAdapter;
import com.photo.collagemaker.activities.editor.single_editor.adapter.StickerTabAdapter;
import com.photo.collagemaker.assets.MenBeautyAssets;
import com.photo.collagemaker.assets.StickersAsset;
import com.photo.collagemaker.databinding.ActivityEditorBinding;
import com.photo.collagemaker.fragment.ColoredFragment;
import com.photo.collagemaker.fragment.MosaicFragment;
import com.photo.collagemaker.preference.KeyboardHeightObserver;
import com.photo.collagemaker.preference.KeyboardHeightProvider;
import com.photo.collagemaker.custom_view.CustomText;
import com.photo.collagemaker.assets.EffectCodeAsset;
import com.photo.collagemaker.assets.FilterCodeAsset;
import com.photo.collagemaker.assets.WomenBeautyAssets;
import com.photo.collagemaker.listener.AdjustListener;
import com.photo.collagemaker.listener.BrushColorListener;
import com.photo.collagemaker.listener.HardmixListener;
import com.photo.collagemaker.listener.FilterListener;
import com.photo.collagemaker.fragment.RotateFragment;
import com.photo.collagemaker.fragment.FrameFragment;
import com.photo.collagemaker.fragment.SplashBlurSquareFragment;
import com.photo.collagemaker.preference.Preference;
import com.photo.collagemaker.utils.FilterUtils;
import com.photo.collagemaker.activities.editor.collage_editor.adapter.FilterAdapter;
import com.photo.collagemaker.fragment.TextFragment;
import com.photo.collagemaker.fragment.ColorSplashFragment;
import com.photo.collagemaker.fragment.CropFragment;
import com.photo.collagemaker.fragment.RatioFragment;
import com.photo.collagemaker.fragment.SplashFragment;
import com.photo.collagemaker.listener.OnQuShotEditorListener;
import com.photo.collagemaker.custom_view.CustomEditor;
import com.photo.collagemaker.draw.Drawing;
import com.photo.collagemaker.picker.PermissionsUtils;
import com.photo.collagemaker.custom_view.StickerIcons;
import com.photo.collagemaker.sticker.DrawableSticker;
import com.photo.collagemaker.sticker.Sticker;
import com.photo.collagemaker.custom_view.StickerView;
import com.photo.collagemaker.custom_view.CustomTextView;
import com.photo.collagemaker.event.AlignHorizontallyEvent;
import com.photo.collagemaker.event.DeleteIconEvent;
import com.photo.collagemaker.event.EditTextIconEvent;
import com.photo.collagemaker.event.FlipHorizontallyEvent;
import com.photo.collagemaker.event.ZoomIconEvent;
import com.photo.collagemaker.module.Module;
import com.photo.collagemaker.utils.SaveFileUtils;
import com.photo.collagemaker.utils.SystemUtil;

import org.jetbrains.annotations.NotNull;
import org.wysaid.myUtils.MsgUtil;
import org.wysaid.nativePort.CGENativeLibrary;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@SuppressLint("StaticFieldLeak")
public class SingleEditorActivity extends AppCompatActivity implements OnQuShotEditorListener,
        View.OnClickListener, StickerAdapter.OnClickSplashListener, KeyboardHeightObserver,
        MenBeautyAdapter.OnClickBeautyItemListener, WomenBeautyAdapter.OnClickBeautyItemListener,
        CropFragment.OnCropPhoto, RotateFragment.OnCropPhoto, BrushColorListener,
        RatioFragment.RatioSaveListener, FrameFragment.RatioSaveListener,
        SplashFragment.SplashListener, SplashBlurSquareFragment.SplashDialogListener,
        MosaicFragment.MosaicListener, ColoredFragment.ColoredListener,
        QueShotToolsAdapter.OnQuShotItemSelected, QueShotDrawToolsAdapter.OnQuShotDrawItemSelected,
        FilterListener, AdjustListener, HardmixListener {

    private static final String TAG = "QuShotEditorActivity";
    // Tools
    public Module moduleToolsId = Module.NONE;
    // Keyboard
    private KeyboardHeightProvider keyboardProvider;

    // Adapter
    public AdjustAdapter adjustAdapter;
    public ColorAdapter colorAdapter;
    private final QueShotToolsAdapter mEditingToolsAdapter = new QueShotToolsAdapter(this);
    // QuShot
    public CustomEditor quShotCustomEditor;
    // BitmapStickerIcon
    StickerIcons quShotStickerIconClose;
    StickerIcons quShotStickerIconScale;
    StickerIcons quShotStickerIconFlip;
    StickerIcons quShotStickerIconRotate;
    StickerIcons quShotStickerIconEdit;
    StickerIcons quShotStickerIconAlign;

    public TextFragment.TextEditor textEditor;
    public TextFragment addTextFragment;

    public TextView textViewCancel;
    public TextView textViewDiscard;
    // ArrayList & List
    public ArrayList listDodge = new ArrayList<>();
    public ArrayList listHardmix = new ArrayList<>();
    public ArrayList listDivide = new ArrayList<>();
    public ArrayList listOverlay = new ArrayList<>();
    public ArrayList listBurn = new ArrayList<>();
    public ArrayList listAllFilter = new ArrayList<>();
    public ArrayList listBwFilter = new ArrayList<>();
    public ArrayList listVintageFilter = new ArrayList<>();
    public ArrayList listSmoothFilter = new ArrayList<>();
    public ArrayList listColdFilter = new ArrayList<>();
    public ArrayList listWarmFilter = new ArrayList<>();
    public ArrayList listLegacyFilter = new ArrayList<>();

    ActivityEditorBinding binding;
    SingleEditorViewModel viewModel;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = ActivityEditorBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this, new SingleEditorViewModelFactory(this, binding)).get(SingleEditorViewModel.class);
        setContentView(binding.getRoot());
        CGENativeLibrary.setLoadImageCallback(loadImageCallback, null);
        if (Build.VERSION.SDK_INT < 26) {
            getWindow().setSoftInputMode(48);
        }
        keyboardProvider = new KeyboardHeightProvider(this);
        new Handler().post(() -> keyboardProvider.start());
        initView();
        onClickListener();
        setView();
        setBottomToolbar(false);

        initDrawView();
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

        binding.btnCloseDraw.setOnClickListener(view -> {
            setVisibleSave();
            onBackPressed();
        });

        binding.btnSaveDraw.setOnClickListener(view -> {
            showLoading(true);
            quShotCustomEditor.setBrushDrawingMode(false);

            viewModel.rvPrimaryToolShow();

            binding.photoEditorView.setImageSource(quShotCustomEditor.getBrushDrawingView().getDrawBitmap(binding.photoEditorView.getCurrentBitmap()));
            quShotCustomEditor.clearBrushAllViews();
            showLoading(false);
            reloadingLayout();
            setVisibleSave();
            moduleToolsId = Module.NONE;
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
    public void onDestroy() {
        super.onDestroy();
        keyboardProvider.close();
    }

    public void onPause() {
        super.onPause();
        keyboardProvider.setKeyboardHeightObserver(null);
    }

    public void onResume() {
        super.onResume();
        keyboardProvider.setKeyboardHeightObserver(this);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {

        binding.relativeLayoutLoading.setVisibility(View.VISIBLE);
        binding.photoEditorView.setVisibility(View.INVISIBLE);


        binding.imageViewCompareAdjust.setOnTouchListener(onTouchListener);
        binding.imageViewCompareAdjust.setVisibility(View.GONE);
        binding.imageViewCompareFilter.setOnTouchListener(onTouchListener);
        binding.imageViewCompareFilter.setVisibility(View.GONE);
        binding.imageViewCompareEffect.setOnTouchListener(onTouchListener);
        binding.imageViewCompareEffect.setVisibility(View.GONE);

    }

    private void setOnBackPressDialog() {
        final Dialog dialogOnBackPressed = new Dialog(SingleEditorActivity.this, R.style.UploadDialog);
        dialogOnBackPressed.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogOnBackPressed.setContentView(R.layout.dialog_exit);
        dialogOnBackPressed.setCancelable(true);
        dialogOnBackPressed.show();
        textViewCancel = dialogOnBackPressed.findViewById(R.id.textViewCancel);
        textViewDiscard = dialogOnBackPressed.findViewById(R.id.textViewDiscard);
        textViewCancel.setOnClickListener(view -> {
            dialogOnBackPressed.dismiss();
        });

        textViewDiscard.setOnClickListener(view -> {
                dialogOnBackPressed.dismiss();
                moduleToolsId = null;
                finish();
        });
    }

    private void setView() {
        binding.recyclerViewTools.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recyclerViewTools.setAdapter(mEditingToolsAdapter);
        binding.recyclerViewTools.setHasFixedSize(true);
        binding.recyclerViewFilterAll.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recyclerViewFilterAll.setHasFixedSize(true);
        binding.recyclerViewFilterBW.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recyclerViewFilterBW.setHasFixedSize(true);
        binding.recyclerViewFilterVintage.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recyclerViewFilterVintage.setHasFixedSize(true);
        binding.recyclerViewFilterSmooth.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recyclerViewFilterSmooth.setHasFixedSize(true);
        binding.recyclerViewFilterCold.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recyclerViewFilterCold.setHasFixedSize(true);
        binding.recyclerViewFilterWarm.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recyclerViewFilterWarm.setHasFixedSize(true);
        binding.recyclerViewFilterLegacy.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recyclerViewFilterLegacy.setHasFixedSize(true);
        binding.recyclerViewDodge.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recyclerViewDodge.setHasFixedSize(true);
        binding.recyclerViewHardmix.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recyclerViewHardmix.setHasFixedSize(true);
        binding.recyclerViewOverlay.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recyclerViewOverlay.setHasFixedSize(true);
        binding.recyclerViewDivide.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recyclerViewDivide.setHasFixedSize(true);
        binding.recyclerViewBurn.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recyclerViewBurn.setHasFixedSize(true);
        new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        binding.recyclerViewAdjust.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recyclerViewAdjust.setHasFixedSize(true);
        adjustAdapter = new AdjustAdapter(getApplicationContext(), this);
        binding.recyclerViewAdjust.setAdapter(adjustAdapter);
        binding.recyclerViewColorPaint.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recyclerViewColorPaint.setHasFixedSize(true);
        binding.recyclerViewColorPaint.setAdapter(new ColorAdapter(getApplicationContext(), this));
        binding.recyclerViewColorNeon.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recyclerViewColorNeon.setHasFixedSize(true);
        binding.recyclerViewColorNeon.setAdapter(new ColorAdapter(getApplicationContext(), this));
        binding.stickerViewpager.setAdapter(new PagerAdapter() {
            public int getCount() {
                return 22;
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
                recycler_view_sticker.setLayoutManager(new GridLayoutManager(getApplicationContext(), 6));
                switch (i) {
                    case 0:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), StickersAsset.mListEmojy(), i, SingleEditorActivity.this));
                        break;
                    case 1:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), StickersAsset.mListFlag(), i, SingleEditorActivity.this));
                        break;
                    case 2:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), StickersAsset.mListBoom(), i, SingleEditorActivity.this));
                        break;
                    case 3:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), MenBeautyAssets.mListhair(), i, SingleEditorActivity.this));
                        break;
                    case 4:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), MenBeautyAssets.mListGlasses(), i, SingleEditorActivity.this));
                        break;
                    case 5:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), MenBeautyAssets.mListMostach(), i, SingleEditorActivity.this));
                        break;
                    case 6:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), MenBeautyAssets.mListLhya(), i, SingleEditorActivity.this));
                        break;
                    case 7:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), MenBeautyAssets.mListScarf(), i, SingleEditorActivity.this));
                        break;
                    case 8:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), MenBeautyAssets.mListTie(), i, SingleEditorActivity.this));
                        break;
                    case 9:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), MenBeautyAssets.mListTatoo(), i, SingleEditorActivity.this));
                        break;
                    case 10:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), MenBeautyAssets.mListChain(), i, SingleEditorActivity.this));
                        break;
                    case 11:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), WomenBeautyAssets.mListCrown(), i, SingleEditorActivity.this));
                        break;
                    case 12:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), WomenBeautyAssets.mListSnsla(), i, SingleEditorActivity.this));
                        break;
                    case 13:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), WomenBeautyAssets.mListHalat(), i, SingleEditorActivity.this));
                        break;
                    case 14:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), WomenBeautyAssets.mListFlower(), i, SingleEditorActivity.this));
                        break;
                    case 15:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), WomenBeautyAssets.mListGlass(), i, SingleEditorActivity.this));
                        break;
                    case 16:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), WomenBeautyAssets.mListChap(), i, SingleEditorActivity.this));
                        break;
                    case 17:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), WomenBeautyAssets.mListHairs(), i, SingleEditorActivity.this));
                        break;
                    case 18:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), WomenBeautyAssets.mListSmile(), i, SingleEditorActivity.this));
                        break;
                    case 19:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), WomenBeautyAssets.mListHjban(), i, SingleEditorActivity.this));
                        break;
                    case 20:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), WomenBeautyAssets.mListChfer(), i, SingleEditorActivity.this));
                        break;
                    case 21:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), WomenBeautyAssets.mListZwaq(), i, SingleEditorActivity.this));
                        break;

                }

                viewGroup.addView(inflate);
                return inflate;
            }
        });

        binding.recyclerTabLayout.setUpWithAdapter(new StickerTabAdapter(binding.stickerViewpager, getApplicationContext()));
        binding.recyclerTabLayout.setPositionThreshold(0.5f);
        binding.recyclerTabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.white));

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            new loadBitmapUri().execute(bundle.getString("SELECTED_PHOTOS"));
        }
        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        CGENativeLibrary.setLoadImageCallback(loadImageCallback, null);
        if (Build.VERSION.SDK_INT < 26) {
            getWindow().setSoftInputMode(48);
        }
        quShotCustomEditor = new CustomEditor.Builder(this, binding.photoEditorView).setPinchTextScalable(true).build();
        quShotCustomEditor.setOnPhotoEditorListener(this);

    }

    @Override
    public void onKeyboardHeightChanged(int height, int orientation) {
        Preference.setKeyboard(getApplicationContext(), 0);
        String orientationLabel = orientation == Configuration.ORIENTATION_PORTRAIT ? "portrait" : "landscape";
        Log.i(TAG, "onKeyboardHeightChanged in pixels: " + height + " " + orientationLabel);
        if (height <= 0) {
            Preference.setHeightOfNotch(getApplicationContext(), -height);
        } else if (addTextFragment != null) {
            addTextFragment.updateAddTextBottomToolbarHeight(Preference.getHeightOfNotch(getApplicationContext()) + height);
            Preference.setKeyboard(getApplicationContext(), height + Preference.getHeightOfNotch(getApplicationContext()));
        }
    }

    private void  onClickListener(){
        binding.textViewSave.setOnClickListener(view -> {
            if (PermissionsUtils.checkWriteStoragePermission(SingleEditorActivity.this)) {
                new SaveEditingBitmap().execute();
            }
        });
        binding.imageViewExit.setOnClickListener(view -> onBackPressed());
        binding.seekbarEraseSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                quShotCustomEditor.setBrushEraserSize((float) i);
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                quShotCustomEditor.brushEraser();
            }
        });
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
        binding.linearLayoutAll.setOnClickListener(view -> viewModel.allFilterShow());
        binding.linearLayoutSmooth.setOnClickListener(view -> viewModel.filterSmoothShow());
        binding.linearLayoutBW.setOnClickListener(view -> viewModel.filterBWShow());
        binding.linearLayoutVintage.setOnClickListener(view -> viewModel.filterVintageShow());
        binding.linearLayoutCold.setOnClickListener(view -> viewModel.filterColdShow());
        binding.linearLayoutWarm.setOnClickListener(view -> viewModel.filterWarmShow());
        binding.linearLayoutLegacy.setOnClickListener(view -> viewModel.filterLegacyShow());

        binding.linearLayoutOverlay.setOnClickListener(view -> viewModel.overlayEffectShow());
        binding.linearLayoutHardmix.setOnClickListener(view -> viewModel.overLayHardMixShow());
        binding.linearLayoutDodge.setOnClickListener(view -> viewModel.overLayDodgeShow());
        binding.linearLayoutDivide.setOnClickListener(view -> viewModel.overLayDivideShow());
        binding.linearLayoutBurn.setOnClickListener(view -> viewModel.overLayBurnShow());
        binding.seekbarStickerAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                Sticker currentSticker = binding.photoEditorView.getCurrentSticker();
                if (currentSticker != null) {
                    currentSticker.setAlpha(i);
                }
            }
        });

        binding.imageViewAddSticker.setVisibility(View.GONE);
        binding.imageViewAddSticker.setOnClickListener(view -> {
            binding.imageViewAddSticker.setVisibility(View.GONE);
            binding.linearLayoutWrapperStickerList.setVisibility(View.VISIBLE);
        });

        binding.relativeLayoutAddText.setOnClickListener(view -> {
            binding.photoEditorView.setHandlingSticker(null);
            textFragment();
        });
        binding.seekbarAdjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                adjustAdapter.getCurrentAdjustModel().setSeekBarIntensity(quShotCustomEditor, ((float) i) / ((float) seekBar.getMax()), true);
            }
        });
        quShotStickerIconClose = new StickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_close), 0, StickerIcons.DELETE);
        quShotStickerIconClose.setIconEvent(new DeleteIconEvent());
        quShotStickerIconScale = new StickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_scale), 3, StickerIcons.SCALE);
        quShotStickerIconScale.setIconEvent(new ZoomIconEvent());
        quShotStickerIconFlip = new StickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_flip), 1, StickerIcons.FLIP);
        quShotStickerIconFlip.setIconEvent(new FlipHorizontallyEvent());
        quShotStickerIconRotate = new StickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_rotate), 3, StickerIcons.ROTATE);
        quShotStickerIconRotate.setIconEvent(new ZoomIconEvent());
        quShotStickerIconEdit = new StickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_edit), 1, StickerIcons.EDIT);
        quShotStickerIconEdit.setIconEvent(new EditTextIconEvent());
        quShotStickerIconAlign = new StickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_center), 2, StickerIcons.ALIGN);
        quShotStickerIconAlign.setIconEvent(new AlignHorizontallyEvent());
        binding.photoEditorView.setIcons(Arrays.asList(quShotStickerIconClose, quShotStickerIconScale,
                quShotStickerIconFlip, quShotStickerIconEdit, quShotStickerIconRotate, quShotStickerIconAlign));
        binding.photoEditorView.setBackgroundColor(getColor(R.color.white));
        binding.photoEditorView.setLocked(false);
        binding.photoEditorView.setConstrained(true);
        binding.photoEditorView.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
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
                binding.seekbarStickerAlpha.setVisibility(View.VISIBLE);
                binding.seekbarStickerAlpha.setProgress(sticker.getAlpha());
            }

            @SuppressLint("RestrictedApi")
            public void onStickerSelected(Sticker sticker, int selectedStickerPosition) {
                if (sticker instanceof CustomTextView) {
                    ((CustomTextView) sticker).setTextColor(SupportMenu.CATEGORY_MASK);
                    binding.photoEditorView.replace(sticker);
                    binding.photoEditorView.invalidate();
                }
                binding.seekbarStickerAlpha.setVisibility(View.VISIBLE);
                binding.seekbarStickerAlpha.setProgress(sticker.getAlpha());
            }

            public void onStickerDeleted(@NonNull Sticker sticker) {
                binding.seekbarStickerAlpha.setVisibility(View.GONE);
            }

            public void onStickerTouchOutside() {
                binding.seekbarStickerAlpha.setVisibility(View.GONE);
            }

            public void onStickerDoubleTap(@NonNull Sticker sticker) {
                if (sticker instanceof CustomTextView) {
                    sticker.setShow(false);
                    binding.photoEditorView.setHandlingSticker(null);
                    addTextFragment = TextFragment.show(SingleEditorActivity.this, ((CustomTextView) sticker).getQuShotText());
                    textEditor = new TextFragment.TextEditor() {
                        public void onDone(CustomText addTextProperties) {
                            binding.photoEditorView.getStickers().remove(binding.photoEditorView.getLastHandlingSticker());
                            binding.photoEditorView.addSticker(new CustomTextView(SingleEditorActivity.this, addTextProperties));
                        }

                        public void onBackButton() {
                            binding.photoEditorView.showLastHandlingSticker();
                        }
                    };
                    addTextFragment.setOnTextEditorListener(textEditor);
                }
            }
        });
        binding.seekbarFilter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                binding.photoEditorView.setFilterIntensity(((float) i) / 100.0f);
            }
        });
        binding.seekbarEffect.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                binding.photoEditorView.setFilterIntensity(((float) i) / 100.0f);
            }
        });
      }

    private void setBottomToolbar(boolean z) {
        int mVisibility = !z ? View.GONE : View.VISIBLE;
        binding.imageViewUndo.setVisibility(mVisibility);
        binding.imageViewRedo.setVisibility(mVisibility);
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

    @SuppressLint("ClickableViewAccessibility")
    View.OnTouchListener onTouchListener = (view, motionEvent) -> {
        switch (motionEvent.getAction()) {
            case 0:
                binding.photoEditorView.getGLSurfaceView().setAlpha(0.0f);
                return true;
            case 1:
                binding.photoEditorView.getGLSurfaceView().setAlpha(1.0f);
                return false;
            default:
                return true;
        }
    };

    public void onRequestPermissionsResult(int i, @NonNull String[] string, @NonNull int[] i2) {
        super.onRequestPermissionsResult(i, string, i2);
    }

    public void onAddViewListener(Drawing viewType, int i) {
        Log.d(TAG, "onAddViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + i + "]");
    }

    public void onRemoveViewListener(int i) {
        Log.d(TAG, "onRemoveViewListener() called with: numberOfAddedViews = [" + i + "]");
    }

    public void onRemoveViewListener(Drawing viewType, int i) {
        Log.d(TAG, "onRemoveViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + i + "]");
    }

    public void onStartViewChangeListener(Drawing viewType) {
        Log.d(TAG, "onStartViewChangeListener() called with: viewType = [" + viewType + "]");
    }

    public void onStopViewChangeListener(Drawing viewType) {
        Log.d(TAG, "onStopViewChangeListener() called with: viewType = [" + viewType + "]");
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.imageViewCloseAdjust || id == R.id.imageViewCloseSticker || id == R.id.imageViewCloseText) {
            setVisibleSave();
            onBackPressed();
            
        } else if (id == R.id.imageViewSaveAdjust) {
            viewModel.rvPrimaryToolShow();
            new SaveFilter().execute();
            moduleToolsId = Module.NONE;
            
        } else if (id == binding.imageViewSaveFilter.getId()) {
            viewModel.rvPrimaryToolShow();
            new SaveFilter().execute();
            moduleToolsId = Module.NONE;
           
        } else if (id == binding.imageViewSaveHardmix.getId()) {
            viewModel.rvPrimaryToolShow();
            new SaveFilter().execute();
            moduleToolsId = Module.NONE;
        } else if (id == R.id.imageViewSaveSticker) {
            viewModel.rvPrimaryToolShow();

            binding.photoEditorView.setHandlingSticker(null);
            binding.photoEditorView.setLocked(true);
            if (!binding.photoEditorView.getStickers().isEmpty()) {
                new SaveSticker().execute();
            }
            moduleToolsId = Module.NONE;
            
        } else if (id == R.id.imageViewSaveText) {
            viewModel.rvPrimaryToolShow();

            binding.photoEditorView.setHandlingSticker(null);
            binding.photoEditorView.setLocked(true);
            if (!binding.photoEditorView.getStickers().isEmpty()) {
                new SaveSticker().execute();
            }
            moduleToolsId = Module.NONE;
        }
    }
    public void onFilterSelected(String string) {
        quShotCustomEditor.setFilterEffect(string);
        binding.seekbarFilter.setProgress(50);
        binding.seekbarEffect.setProgress(50);
        if (moduleToolsId == Module.FILTER) {
            binding.photoEditorView.getGLSurfaceView().setFilterIntensity(0.5f);
        }
        if (moduleToolsId == Module.OVERLAY) {
            binding.photoEditorView.getGLSurfaceView().setFilterIntensity(0.5f);
        }

    }

    public void textFragment() {
        addTextFragment = TextFragment.show(this);
        textEditor = new TextFragment.TextEditor() {
            public void onDone(CustomText addTextProperties) {
                binding.photoEditorView.addSticker(new CustomTextView(getApplicationContext(), addTextProperties));
            }

            public void onBackButton() {
                if (binding.photoEditorView.getStickers().isEmpty()) {
                    onBackPressed();
                }
            }
        };
        addTextFragment.setOnTextEditorListener(textEditor);
    }

    public void onQuShotToolSelected(Module module) {
        moduleToolsId = module;
        switch (module) {
            case TEXT:
                viewModel.textShow();
                binding.photoEditorView.setLocked(false);
                textFragment();


                binding.photoEditorView.setHandlingSticker(null);
                binding.photoEditorView.getStickers().clear();
                quShotCustomEditor.setFilterEffect("");
                quShotCustomEditor.clearBrushAllViews();
                quShotCustomEditor.setBrushDrawingMode(false);
                binding.photoEditorView.setHandlingSticker(null);
                binding.photoEditorView.getStickers().clear();
                quShotCustomEditor.setFilterEffect("");
                quShotCustomEditor.clearBrushAllViews();
                quShotCustomEditor.setBrushDrawingMode(false);
                break;
            case ADJUST:
                viewModel.adjustShow();

                if (!binding.photoEditorView.getStickers().isEmpty()) {
                    binding.photoEditorView.getStickers().clear();
                    binding.photoEditorView.setHandlingSticker(null);
                }

                quShotCustomEditor.clearBrushAllViews();
                quShotCustomEditor.setBrushDrawingMode(false);
                adjustAdapter = new AdjustAdapter(getApplicationContext(), this);
                binding.recyclerViewAdjust.setAdapter(adjustAdapter);
                adjustAdapter.setSelectedAdjust(0);
                quShotCustomEditor.setAdjustFilter(adjustAdapter.getFilterConfig());
                break;
            case FILTER:
                if (!binding.photoEditorView.getStickers().isEmpty()) {
                    binding.photoEditorView.getStickers().clear();
                    binding.photoEditorView.setHandlingSticker(null);
                }

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
                binding.photoEditorView.setLocked(false);

                quShotCustomEditor.setFilterEffect("");
                quShotCustomEditor.clearBrushAllViews();
                quShotCustomEditor.setBrushDrawingMode(false);
                if (!binding.photoEditorView.getStickers().isEmpty()) {
                    binding.photoEditorView.getStickers().clear();
                    binding.photoEditorView.setHandlingSticker(null);
                }

                break;
            case DRAW:
                viewModel.drawShow();
                break;
            case OVERLAY:

                new effectOvarlay().execute();
                new effectHardmix().execute();
                new effectDodge().execute();
                new effectBurn().execute();
                new effectDivide().execute();

                break;
            case RATIO:
                new openBlurFragment().execute();
                goneLayout();
                break;
            case BACKGROUND:
                new openFrameFragment().execute();
                goneLayout();
                break;
            case SPLASH:
                new openSplashBrushFragment(true).execute();
                break;
            case SQUARESPLASH:
                new openSplashSquareFragment(true).execute();
                break;
            case BLUR:
                new openSplashBrushFragment(false).execute();
                break;
            case SQUAEBLUR:
                new openSplashSquareFragment(false).execute();
                break;
            case COLOR:
                ColorSplashFragment.show(this, binding.photoEditorView.getCurrentBitmap());
                goneLayout();
                break;
            case CROP:
                CropFragment.show(this, this, binding.photoEditorView.getCurrentBitmap());
                goneLayout();
                break;
            case ROTATE:
                RotateFragment.show(this, this, binding.photoEditorView.getCurrentBitmap());
                goneLayout();
                break;
        }
        binding.photoEditorView.setHandlingSticker(null);
    }

    public void onQuShotDrawToolSelected(Module module) {
        moduleToolsId = module;
        switch (module) {
            case PAINT:
            case NEON:

                break;
            case COLORED:
                new openColoredFragment().execute();
                binding.constraintLayoutDraw.setVisibility(View.GONE);
                goneLayout();
                break;
            case MOSAIC:
                new openShapeFragment().execute();
                binding.constraintLayoutDraw.setVisibility(View.GONE);
                goneLayout();
                break;
        }
        binding.photoEditorView.setHandlingSticker(null);
    }

    private void goneLayout() {
        setVisibleSave();
    }



    public void setVisibleSave() {
        binding.constraintLayoutSaveEditing.setVisibility(View.VISIBLE);
    }

    public void onBackPressed() {
        if (moduleToolsId != null) {
            try {
                switch (moduleToolsId) {
                    case PAINT:
                    case NEON:
                    case DRAW:
                        quShotCustomEditor.setBrushDrawingMode(false);
                        viewModel.rvPrimaryToolShow();
                        moduleToolsId = Module.NONE;
                        break;
                    case TEXT:
                        viewModel.rvPrimaryToolShow();
                        if (!binding.photoEditorView.getStickers().isEmpty()) {
                            binding.photoEditorView.getStickers().clear();
                            binding.photoEditorView.setHandlingSticker(null);
                        }
                        binding.photoEditorView.setHandlingSticker(null);
                        binding.photoEditorView.setLocked(true);

                        setVisibleSave();
                        moduleToolsId = Module.NONE;
                        return;
                    case ADJUST:
                        viewModel.rvPrimaryToolShow();
                        quShotCustomEditor.setFilterEffect("");
                        setVisibleSave();
                        moduleToolsId = Module.NONE;
                        return;
                    case FILTER:
                        viewModel.rvPrimaryToolShow();

                        setVisibleSave();
                        quShotCustomEditor.setFilterEffect("");
                        listAllFilter.clear();
                        if (binding.recyclerViewFilterAll.getAdapter() != null) {
                            binding.recyclerViewFilterAll.getAdapter().notifyDataSetChanged();
                        }
                        moduleToolsId = Module.NONE;
                        return;
                    case STICKER:
                        if (binding.photoEditorView.getStickers().size() == 0) {
                            viewModel.rvPrimaryToolShow();
                            binding.photoEditorView.setHandlingSticker(null);
                            binding.photoEditorView.setLocked(true);
                            moduleToolsId = Module.NONE;
                        } else if (binding.imageViewAddSticker.getVisibility() == View.VISIBLE) {
                            viewModel.rvPrimaryToolShow();
                            binding.photoEditorView.getStickers().clear();
                            binding.photoEditorView.setHandlingSticker(null);
                            moduleToolsId = Module.NONE;
                        } else {
                            binding.linearLayoutWrapperStickerList.setVisibility(View.GONE);
                            binding.imageViewAddSticker.setVisibility(View.VISIBLE);
                        }
                        binding.linearLayoutWrapperStickerList.setVisibility(View.VISIBLE);
                        moduleToolsId = Module.NONE;
                        setVisibleSave();
                        return;
                    case OVERLAY:
                        viewModel.rvPrimaryToolShow();

                        quShotCustomEditor.setFilterEffect("");
                        listOverlay.clear();
                        if (binding.recyclerViewOverlay.getAdapter() != null) {
                            binding.recyclerViewOverlay.getAdapter().notifyDataSetChanged();
                        }
                        setVisibleSave();
                        moduleToolsId = Module.NONE;
                        return;
                    case SPLASH:
                    case BLUR:
                    case SQUARESPLASH:
                    case SQUAEBLUR:
                    case MOSAIC:
                    case COLORED:
                    case ROTATE:
                    case CROP:
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
    }

    public void onAdjustSelected(AdjustAdapter.AdjustModel adjustModel) {
        Log.d("XXXXXXXX", "onAdjustSelected " + adjustModel.seekbarIntensity + " " + binding.seekbarAdjust.getMax());
        binding.seekbarAdjust.setProgress((int) (adjustModel.seekbarIntensity * ((float) binding.seekbarAdjust.getMax())));
    }

    public void addSticker(Bitmap bitmap) {
        binding.photoEditorView.addSticker(new DrawableSticker(new BitmapDrawable(getResources(), bitmap)));
        binding.linearLayoutWrapperStickerList.setVisibility(View.GONE);
        binding.imageViewAddSticker.setVisibility(View.VISIBLE);
    }

    public void finishCrop(Bitmap bitmap) {
        binding.photoEditorView.setImageSource(bitmap);
        moduleToolsId = Module.NONE;
        reloadingLayout();
    }

    public void onColorChanged(String string) {
        quShotCustomEditor.setBrushColor(Color.parseColor(string));
    }

    public void ratioSavedBitmap(Bitmap bitmap) {
        binding.photoEditorView.setImageSource(bitmap);
        moduleToolsId = Module.NONE;
        reloadingLayout();
    }

    public void onSaveSplash(Bitmap bitmap) {
        binding.photoEditorView.setImageSource(bitmap);
        moduleToolsId = Module.NONE;
    }

    public void onSaveMosaic(Bitmap bitmap) {
        binding.photoEditorView.setImageSource(bitmap);
        moduleToolsId = Module.NONE;
    }

    class allFilters extends AsyncTask<Void, Void, Void> {
        public void onPreExecute() {
            showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            listAllFilter.clear();
            listAllFilter.addAll(FilterCodeAsset.getListBitmapFilterAll(ThumbnailUtils.extractThumbnail(binding.photoEditorView.getCurrentBitmap(), 100, 100)));
            Log.d("XXXXXXXX", "allFilters " + listAllFilter.size());
            return null;
        }

        public void onPostExecute(Void voids) {
            viewModel.filterShow();
            binding.recyclerViewFilterAll.setAdapter(new FilterAdapter(listAllFilter, SingleEditorActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.ALL_FILTERS)));
            binding.photoEditorView.setHandlingSticker(null);
            binding.photoEditorView.getStickers().clear();
            binding.seekbarFilter.setProgress(100);
            showLoading(false);
            quShotCustomEditor.setFilterEffect("");
            quShotCustomEditor.clearBrushAllViews();
            quShotCustomEditor.setBrushDrawingMode(false);

        }
    }

    class bwFilters extends AsyncTask<Void, Void, Void> {
        public void onPreExecute() {
            showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            listBwFilter.clear();
            listBwFilter.addAll(FilterCodeAsset.getListBitmapFilterBW(ThumbnailUtils.extractThumbnail(binding.photoEditorView.getCurrentBitmap(), 100, 100)));
            Log.d("XXXXXXXX", "bwFilters " + listBwFilter.size());
            return null;
        }

        public void onPostExecute(Void voids) {
            binding.recyclerViewFilterBW.setAdapter(new FilterAdapter(listBwFilter, SingleEditorActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.BW_FILTERS)));
            binding.imageViewCompareFilter.setVisibility(View.VISIBLE);
            binding.seekbarFilter.setProgress(100);
            showLoading(false);
        }
    }

    class vintageFilters extends AsyncTask<Void, Void, Void> {
        public void onPreExecute() {
            showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            listVintageFilter.clear();
            listVintageFilter.addAll(FilterCodeAsset.getListBitmapFilterVintage(ThumbnailUtils.extractThumbnail(binding.photoEditorView.getCurrentBitmap(), 100, 100)));
            Log.d("XXXXXXXX", "vintageFilters " + listVintageFilter.size());
            return null;
        }

        public void onPostExecute(Void voids) {
            binding.recyclerViewFilterVintage.setAdapter(new FilterAdapter(listVintageFilter, SingleEditorActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.VINTAGE_FILTERS)));
            binding.imageViewCompareFilter.setVisibility(View.VISIBLE);
            binding.seekbarFilter.setProgress(100);
            showLoading(false);
        }
    }

    class smoothFilters extends AsyncTask<Void, Void, Void> {
        public void onPreExecute() {
            showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            listSmoothFilter.clear();
            listSmoothFilter.addAll(FilterCodeAsset.getListBitmapFilterSmooth(ThumbnailUtils.extractThumbnail(binding.photoEditorView.getCurrentBitmap(), 100, 100)));
            Log.d("XXXXXXXX", "smoothFilters " + listSmoothFilter.size());
            return null;
        }

        public void onPostExecute(Void voids) {
            binding.recyclerViewFilterSmooth.setAdapter(new FilterAdapter(listSmoothFilter, SingleEditorActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.SMOOTH_FILTERS)));
            binding.imageViewCompareFilter.setVisibility(View.VISIBLE);
            binding.seekbarFilter.setProgress(100);
            showLoading(false);
        }
    }

    class coldFilters extends AsyncTask<Void, Void, Void> {
        public void onPreExecute() {
            showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            listColdFilter.clear();
            listColdFilter.addAll(FilterCodeAsset.getListBitmapFilterCold(ThumbnailUtils.extractThumbnail(binding.photoEditorView.getCurrentBitmap(), 100, 100)));
            Log.d("XXXXXXXX", "coldFilters " + listColdFilter.size());
            return null;
        }

        public void onPostExecute(Void voids) {
            binding.recyclerViewFilterCold.setAdapter(new FilterAdapter(listColdFilter, SingleEditorActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.COLD_FILTERS)));
            binding.imageViewCompareFilter.setVisibility(View.VISIBLE);
            binding.seekbarFilter.setProgress(100);
            showLoading(false);
        }
    }

    class warmFilters extends AsyncTask<Void, Void, Void> {

        public void onPreExecute() {
            showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            listWarmFilter.clear();
            listWarmFilter.addAll(FilterCodeAsset.getListBitmapFilterWarm(ThumbnailUtils.extractThumbnail(binding.photoEditorView.getCurrentBitmap(), 100, 100)));
            Log.d("XXXXXXXX", "warmFilters " + listWarmFilter.size());
            return null;
        }

        public void onPostExecute(Void voids) {
            binding.recyclerViewFilterWarm.setAdapter(new FilterAdapter(listWarmFilter, SingleEditorActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.WARM_FILTERS)));
            binding.imageViewCompareFilter.setVisibility(View.VISIBLE);
            binding.seekbarFilter.setProgress(100);
            showLoading(false);
        }
    }

    class legacyFilters extends AsyncTask<Void, Void, Void> {
        public void onPreExecute() {
            showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            listLegacyFilter.clear();
            listLegacyFilter.addAll(FilterCodeAsset.getListBitmapFilterLegacy(ThumbnailUtils.extractThumbnail(binding.photoEditorView.getCurrentBitmap(), 100, 100)));
            Log.d("XXXXXXXX", "legacyFilters " + listLegacyFilter.size());
            return null;
        }

        public void onPostExecute(Void voids) {
            binding.recyclerViewFilterLegacy.setAdapter(new FilterAdapter(listLegacyFilter, SingleEditorActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.LEGACY_FILTERS)));
            binding.imageViewCompareFilter.setVisibility(View.VISIBLE);
            binding.seekbarFilter.setProgress(100);
            showLoading(false);
        }
    }

    class openBlurFragment extends AsyncTask<Void, Bitmap, Bitmap> {

        public void onPreExecute() {
            showLoading(true);
        }

        public Bitmap doInBackground(Void... voids) {
            return FilterUtils.getBlurImageFromBitmap(binding.photoEditorView.getCurrentBitmap(), 5.0f);
        }

        public void onPostExecute(Bitmap bitmap) {
            showLoading(false);
            RatioFragment.show(SingleEditorActivity.this, SingleEditorActivity.this, binding.photoEditorView.getCurrentBitmap(), bitmap);
        }
    }

    class openFrameFragment extends AsyncTask<Void, Bitmap, Bitmap> {

        public void onPreExecute() {
            showLoading(true);
        }

        public Bitmap doInBackground(Void... voids) {
            return FilterUtils.getBlurImageFromBitmap(binding.photoEditorView.getCurrentBitmap(), 5.0f);
        }

        public void onPostExecute(Bitmap bitmap) {
            showLoading(false);
            FrameFragment.show(SingleEditorActivity.this, SingleEditorActivity.this, binding.photoEditorView.getCurrentBitmap(), bitmap);
        }
    }

    class openShapeFragment extends AsyncTask<Void, List<Bitmap>, List<Bitmap>> {

        public void onPreExecute() {
            showLoading(true);
        }

        public List<Bitmap> doInBackground(Void... voids) {
            List<Bitmap> arrayList = new ArrayList<>();
            arrayList.add(FilterUtils.cloneBitmap(binding.photoEditorView.getCurrentBitmap()));
            arrayList.add(FilterUtils.getBlurImageFromBitmap(binding.photoEditorView.getCurrentBitmap(), 8.0f));
            return arrayList;
        }

        public void onPostExecute(List<Bitmap> list) {
            showLoading(false);
            MosaicFragment.show(SingleEditorActivity.this, list.get(0), list.get(1), SingleEditorActivity.this);
        }
    }

    class openColoredFragment extends AsyncTask<Void, List<Bitmap>, List<Bitmap>> {

        public void onPreExecute() {
            showLoading(true);
        }

        public List<Bitmap> doInBackground(Void... voids) {
            List<Bitmap> arrayList = new ArrayList<>();
            arrayList.add(FilterUtils.cloneBitmap(binding.photoEditorView.getCurrentBitmap()));
            arrayList.add(FilterUtils.getBlurImageFromBitmap(binding.photoEditorView.getCurrentBitmap(), 8.0f));
            return arrayList;
        }

        public void onPostExecute(List<Bitmap> list) {
            showLoading(false);
            ColoredFragment.show(SingleEditorActivity.this, list.get(0), list.get(1), SingleEditorActivity.this);
        }
    }

    class effectDodge extends AsyncTask<Void, Void, Void> {

        public void onPreExecute() {
            showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            listDodge.clear();
            listDodge.addAll(EffectCodeAsset.getListBitmapDodgeEffect(ThumbnailUtils.extractThumbnail(binding.photoEditorView.getCurrentBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voids) {
            binding.recyclerViewDodge.setAdapter(new HardmixAdapter(listDodge, SingleEditorActivity.this, getApplicationContext(), Arrays.asList(EffectCodeAsset.DODGE_EFFECTS)));
            binding.imageViewCompareEffect.setVisibility(View.VISIBLE);
            binding.seekbarEffect.setProgress(100);
        }
    }

    class effectDivide extends AsyncTask<Void, Void, Void> {

        public void onPreExecute() {
            showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            listDivide.clear();
            listDivide.addAll(EffectCodeAsset.getListBitmapDivideEffect(ThumbnailUtils.extractThumbnail(binding.photoEditorView.getCurrentBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voids) {
            binding.recyclerViewDivide.setAdapter(new HardmixAdapter(listDivide, SingleEditorActivity.this, getApplicationContext(), Arrays.asList(EffectCodeAsset.DIVIDE_EFFECTS)));
            binding.imageViewCompareEffect.setVisibility(View.VISIBLE);
            binding.seekbarEffect.setProgress(100);
        }
    }

    class effectHardmix extends AsyncTask<Void, Void, Void> {

        public void onPreExecute() {
            showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            listHardmix.clear();
            listHardmix.addAll(EffectCodeAsset.getListBitmapHardmixEffect(ThumbnailUtils.extractThumbnail(binding.photoEditorView.getCurrentBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voids) {
            binding.recyclerViewHardmix.setAdapter(new HardmixAdapter(listHardmix, SingleEditorActivity.this, getApplicationContext(), Arrays.asList(EffectCodeAsset.HARDMIX_EFFECTS)));
            binding.imageViewCompareEffect.setVisibility(View.VISIBLE);
            binding.seekbarEffect.setProgress(100);
        }
    }

    class effectOvarlay extends AsyncTask<Void, Void, Void> {

        public void onPreExecute() {
            showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            listOverlay.clear();
            listOverlay.addAll(EffectCodeAsset.getListBitmapOverlayEffect(ThumbnailUtils.extractThumbnail(binding.photoEditorView.getCurrentBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voids) {
            binding.recyclerViewOverlay.setAdapter(new HardmixAdapter(listOverlay, SingleEditorActivity.this, getApplicationContext(), Arrays.asList(EffectCodeAsset.OVERLAY_EFFECTS)));

            viewModel.overLayShow();

            binding.seekbarEffect.setProgress(100);
            showLoading(false);
        }
    }

    class effectBurn extends AsyncTask<Void, Void, Void> {

        public void onPreExecute() {
            showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            listBurn.clear();
            listBurn.addAll(EffectCodeAsset.getListBitmapColorEffect(ThumbnailUtils.extractThumbnail(binding.photoEditorView.getCurrentBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voids) {
            binding.recyclerViewBurn.setAdapter(new HardmixAdapter(listBurn, SingleEditorActivity.this, getApplicationContext(), Arrays.asList(EffectCodeAsset.COLOR_EFFECTS)));
            binding.imageViewCompareEffect.setVisibility(View.VISIBLE);
            binding.seekbarEffect.setProgress(100);
        }
    }

    class openSplashBrushFragment extends AsyncTask<Void, List<Bitmap>, List<Bitmap>> {
        boolean isSplashBrush;
        public openSplashBrushFragment(boolean z) {
            isSplashBrush = z;
        }

        public void onPreExecute() {
            showLoading(true);
        }

        public List<Bitmap> doInBackground(Void... voids) {
            Bitmap currentBitmap = binding.photoEditorView.getCurrentBitmap();
            List<Bitmap> arrayList = new ArrayList<>();
            arrayList.add(currentBitmap);
            if (isSplashBrush) {
                arrayList.add(FilterUtils.getBlackAndWhiteImageFromBitmap(currentBitmap));
            } else {
                arrayList.add(FilterUtils.getBlurImageFromBitmap(currentBitmap, 3.0f));
            }
            return arrayList;
        }

        public void onPostExecute(List<Bitmap> list) {
            if (isSplashBrush) {
                SplashFragment.show(SingleEditorActivity.this, list.get(0), null, list.get(1), SingleEditorActivity.this, true);
            } else {
                SplashFragment.show(SingleEditorActivity.this, list.get(0), list.get(1), null, SingleEditorActivity.this, false);
            }
            showLoading(false);
        }
    }

    class openSplashSquareFragment extends AsyncTask<Void, List<Bitmap>, List<Bitmap>> {
        boolean isSplashSquared;
        public openSplashSquareFragment(boolean z) {
            isSplashSquared = z;
        }

        public void onPreExecute() {
            showLoading(true);
        }

        public List<Bitmap> doInBackground(Void... voids) {
            Bitmap currentBitmap = binding.photoEditorView.getCurrentBitmap();
            List<Bitmap> arrayList = new ArrayList<>();
            arrayList.add(currentBitmap);
            if (isSplashSquared) {
                arrayList.add(FilterUtils.getBlackAndWhiteImageFromBitmap(currentBitmap));
            } else {
                arrayList.add(FilterUtils.getBlurImageFromBitmap(currentBitmap, 3.0f));
            }
            return arrayList;
        }

        public void onPostExecute(List<Bitmap> list) {
            if (isSplashSquared) {
                SplashBlurSquareFragment.show(SingleEditorActivity.this, list.get(0), null, list.get(1), SingleEditorActivity.this, true);
            } else {
                SplashBlurSquareFragment.show(SingleEditorActivity.this, list.get(0), list.get(1), null, SingleEditorActivity.this, false);
            }
            showLoading(false);
        }
    }

    class SaveFilter extends AsyncTask<Void, Void, Bitmap> {

        public void onPreExecute() {
            showLoading(true);
        }

        public Bitmap doInBackground(Void... voids) {
            final Bitmap[] bitmaps = {null};
            binding.photoEditorView.saveGLSurfaceViewAsBitmap(bitmap -> bitmaps[0] = bitmap);
            while (bitmaps[0] == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return bitmaps[0];
        }

        public void onPostExecute(Bitmap bitmap) {
            binding.photoEditorView.setImageSource(bitmap);
            binding.photoEditorView.setFilterEffect("");
            showLoading(false);
        }
    }

    class SaveSticker extends AsyncTask<Void, Void, Bitmap> {
        public void onPreExecute() {
            binding.photoEditorView.getGLSurfaceView().setAlpha(0.0f);
            showLoading(true);
        }

        public Bitmap doInBackground(Void... voids) {
            final Bitmap[] bitmaps = {null};
            while (bitmaps[0] == null) {
                try {
                    quShotCustomEditor.saveStickerAsBitmap(bitmap -> bitmaps[0] = bitmap);
                    while (bitmaps[0] == null) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                }
            }
            return bitmaps[0];
        }

        public void onPostExecute(Bitmap bitmap) {
            binding.photoEditorView.setImageSource(bitmap);
            binding.photoEditorView.getStickers().clear();
            binding.photoEditorView.getGLSurfaceView().setAlpha(1.0f);
            showLoading(false);
            reloadingLayout();
        }
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 123) {
            if (i2 == -1) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(intent.getData());
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    float width = (float) bitmap.getWidth();
                    float height = (float) bitmap.getHeight();
                    float max = Math.max(width / 1280.0f, height / 1280.0f);
                    if (max > 1.0f) {
                        bitmap = Bitmap.createScaledBitmap(bitmap, (int) (width / max), (int) (height / max), false);
                    }
                    if (SystemUtil.rotateBitmap(bitmap, new ExifInterface(inputStream).getAttributeInt(ExifInterface.TAG_ORIENTATION, 1)) != bitmap) {
                        bitmap.recycle();
                        bitmap = null;
                    }
                    binding.photoEditorView.setImageSource(bitmap);
                    reloadingLayout();
                } catch (Exception e) {
                    e.printStackTrace();
                    MsgUtil.toastMsg(this, "Error: Can not open image");
                }
            } else {
                finish();
            }
        }
    }

    class loadBitmapUri extends AsyncTask<String, Bitmap, Bitmap> {
        public void onPreExecute() {
            showLoading(true);
        }

        public Bitmap doInBackground(String... string) {
            try {
                Uri fromFile = Uri.fromFile(new File(string[0]));
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(SingleEditorActivity.this.getContentResolver(), fromFile);
                float width = (float) bitmap.getWidth();
                float height = (float) bitmap.getHeight();
                float max = Math.max(width / 1280.0f, height / 1280.0f);
                if (max > 1.0f) {
                    bitmap = Bitmap.createScaledBitmap(bitmap, (int) (width / max), (int) (height / max), false);
                }
                Bitmap bitmap1 = SystemUtil.rotateBitmap(bitmap, new ExifInterface(SingleEditorActivity.this.getContentResolver().openInputStream(fromFile)).getAttributeInt(ExifInterface.TAG_ORIENTATION, 1));
                if (bitmap1 != bitmap) {
                    bitmap.recycle();
                }
                return bitmap1;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }


        public void onPostExecute(Bitmap bitmap) {
            binding.photoEditorView.setImageSource(bitmap);
            reloadingLayout();
        }
    }

    public void reloadingLayout() {
        binding.photoEditorView.postDelayed(() -> {
            try {
                Display display = getWindowManager().getDefaultDisplay();
                Point point = new Point();
                display.getSize(point);
                int i = point.x;
                int height = binding.relativeLayoutWrapperPhoto.getHeight();
                int i2 = binding.photoEditorView.getGLSurfaceView().getRenderViewport().width;
                float f = (float) binding.photoEditorView.getGLSurfaceView().getRenderViewport().height;
                float f2 = (float) i2;
                if (((int) ((((float) i) * f) / f2)) <= height) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -2);
                    params.addRule(13);
                    binding.photoEditorView.setLayoutParams(params);
                    binding.photoEditorView.setVisibility(View.VISIBLE);
                } else {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) ((((float) height) * f2) / f), -1);
                    params.addRule(13);
                    binding.photoEditorView.setLayoutParams(params);
                    binding.photoEditorView.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            showLoading(false);
        }, 300);
    }

    class SaveEditingBitmap extends AsyncTask<Void, String, String> {
        public void onPreExecute() {
            showLoading(true);
        }

        public String doInBackground(Void... voids) {
            try {
                return SaveFileUtils.saveBitmapFileEditor(SingleEditorActivity.this, binding.photoEditorView.getCurrentBitmap(), new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date())).getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        public void onPostExecute(String string) {
            showLoading(false);
            if (string == null) {
                Toast.makeText(getApplicationContext(), "Oop! Something went wrong", Toast.LENGTH_LONG).show();
                return;
            }
            Intent i = new Intent(SingleEditorActivity.this, PhotoShareActivity.class);
            i.putExtra("path", string);
            startActivity(i);
        }

    }

    public void showLoading(boolean isShowing) {
        if (isShowing) {
            binding.relativeLayoutLoading.setVisibility(View.VISIBLE);
            return;
        }
        binding.relativeLayoutLoading.setVisibility(View.GONE);
    }
}
