package com.photo.collagemaker.activities;

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
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.core.internal.view.SupportMenu;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.photo.collagemaker.R;
import com.photo.collagemaker.adapters.AdjustAdapter;
import com.photo.collagemaker.adapters.ColorAdapter;
import com.photo.collagemaker.adapters.HardmixAdapter;
import com.photo.collagemaker.adapters.MenBeautyAdapter;
import com.photo.collagemaker.adapters.MenTabAdapter;
import com.photo.collagemaker.adapters.QueShotDrawToolsAdapter;
import com.photo.collagemaker.adapters.QueShotStickersToolsAdapter;
import com.photo.collagemaker.adapters.RecyclerTabLayout;
import com.photo.collagemaker.adapters.StickerAdapter;
import com.photo.collagemaker.adapters.QueShotToolsAdapter;
import com.photo.collagemaker.adapters.StickerTabAdapter;
import com.photo.collagemaker.adapters.WomenBeautyAdapter;
import com.photo.collagemaker.adapters.WomenTabAdapter;
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
import com.photo.collagemaker.adapters.FilterAdapter;
import com.photo.collagemaker.fragment.TextFragment;
import com.photo.collagemaker.fragment.ColorSplashFragment;
import com.photo.collagemaker.fragment.CropFragment;
import com.photo.collagemaker.fragment.RatioFragment;
import com.photo.collagemaker.fragment.SplashFragment;
import com.photo.collagemaker.listener.OnQuShotEditorListener;
import com.photo.collagemaker.custom_view.CustomEditor;
import com.photo.collagemaker.custom_view.CustomCollageView;
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
public class EditorActivity extends BaseActivity implements OnQuShotEditorListener,
        View.OnClickListener, StickerAdapter.OnClickSplashListener, KeyboardHeightObserver,
        MenBeautyAdapter.OnClickBeautyItemListener, WomenBeautyAdapter.OnClickBeautyItemListener,
        CropFragment.OnCropPhoto, RotateFragment.OnCropPhoto, BrushColorListener,
        RatioFragment.RatioSaveListener, FrameFragment.RatioSaveListener,
        SplashFragment.SplashListener, SplashBlurSquareFragment.SplashDialogListener,
        MosaicFragment.MosaicListener, ColoredFragment.ColoredListener,
        QueShotToolsAdapter.OnQuShotItemSelected, QueShotDrawToolsAdapter.OnQuShotDrawItemSelected,
        QueShotStickersToolsAdapter.OnQuShotStickerItemSelected, FilterListener, AdjustListener, HardmixListener {

    private static final String TAG = "QuShotEditorActivity";
    // Tools
    public Module moduleToolsId = Module.NONE;
    // Keyboard
    private KeyboardHeightProvider keyboardProvider;

    // Adapter
    public AdjustAdapter adjustAdapter;
    public ColorAdapter colorAdapter;
    private final QueShotToolsAdapter mEditingToolsAdapter = new QueShotToolsAdapter(this);
    private final QueShotDrawToolsAdapter mEditingEffectToolsAdapter = new QueShotDrawToolsAdapter(this);
    private final QueShotStickersToolsAdapter mEditingStickersToolsAdapter = new QueShotStickersToolsAdapter(this);
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

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        binding = ActivityEditorBinding.inflate(getLayoutInflater());
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
        binding.recyclerViewFilterBw.setVisibility(View.GONE);
        binding.recyclerViewFilterVintage.setVisibility(View.GONE);
        binding.recyclerViewFilterSmooth.setVisibility(View.GONE);
        binding.recyclerViewFilterCold.setVisibility(View.GONE);
        binding.recyclerViewFilterWarm.setVisibility(View.GONE);
        binding.recyclerViewFilterLegacy.setVisibility(View.GONE);

        binding.viewBw.setVisibility(View.INVISIBLE);
        binding.viewCold.setVisibility(View.INVISIBLE);
        binding.viewVintage.setVisibility(View.INVISIBLE);
        binding.viewSmooth.setVisibility(View.INVISIBLE);
        binding.viewWarm.setVisibility(View.INVISIBLE);
        binding.viewLegacy.setVisibility(View.INVISIBLE);


        binding.recyclerViewDodge.setVisibility(View.GONE);
        binding.recyclerViewDivide.setVisibility(View.GONE);
        binding.recyclerViewHardmix.setVisibility(View.GONE);
        binding.recyclerViewBurn.setVisibility(View.GONE);


        binding.seekbarStickerMenAlpha.setVisibility(View.GONE);

        binding.seekbarStickerWomenAlpha.setVisibility(View.GONE);

        binding.seekbarStickerAlpha.setVisibility(View.GONE);





        binding.imageViewUndo.setVisibility(View.GONE);

        binding.imageViewRedo.setVisibility(View.GONE);

        binding.imageViewClean.setVisibility(View.GONE);

        binding.imageViewCleanNeon.setVisibility(View.GONE);
        binding.imageViewUndoNeon.setVisibility(View.GONE);
        binding.imageViewRedoNeon.setVisibility(View.GONE);


        binding.imageViewCompareAdjust.setOnTouchListener(onTouchListener);
        binding.imageViewCompareAdjust.setVisibility(View.GONE);
        binding.imageViewCompareFilter.setOnTouchListener(onTouchListener);
        binding.imageViewCompareFilter.setVisibility(View.GONE);
        binding.imageViewCompareEffect.setOnTouchListener(onTouchListener);
        binding.imageViewCompareEffect.setVisibility(View.GONE);
        binding.relativeLayoutAddText.setVisibility(View.GONE);
    }

    private void setOnBackPressDialog() {
        final Dialog dialogOnBackPressed = new Dialog(EditorActivity.this, R.style.UploadDialog);
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
                finish();
        });
    }

    private void setView() {
        binding.recyclerViewTools.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recyclerViewTools.setAdapter(mEditingToolsAdapter);
        binding.recyclerViewTools.setHasFixedSize(true);
        binding.recyclerViewDraw.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));
        binding.recyclerViewDraw.setAdapter(mEditingEffectToolsAdapter);
        binding.recyclerViewDraw.setHasFixedSize(true);
        binding.recyclerViewemoji.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        binding.recyclerViewemoji.setAdapter(mEditingStickersToolsAdapter);
        binding.recyclerViewemoji.setHasFixedSize(true);
        binding.recyclerViewFilterAll.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recyclerViewFilterAll.setHasFixedSize(true);
        binding.recyclerViewFilterBw.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recyclerViewFilterBw.setHasFixedSize(true);
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

        binding.stickerWomenViewpager.setAdapter(new PagerAdapter() {
            public int getCount() {
                return 11;
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
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), WomenBeautyAssets.mListCrown(), i, EditorActivity.this));
                        break;
                    case 1:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), WomenBeautyAssets.mListSnsla(), i, EditorActivity.this));
                        break;
                    case 2:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), WomenBeautyAssets.mListHalat(), i, EditorActivity.this));
                        break;
                    case 3:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), WomenBeautyAssets.mListFlower(), i, EditorActivity.this));
                        break;
                    case 4:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), WomenBeautyAssets.mListGlass(), i, EditorActivity.this));
                        break;
                    case 5:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), WomenBeautyAssets.mListChap(), i, EditorActivity.this));
                        break;
                    case 6:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), WomenBeautyAssets.mListHairs(), i, EditorActivity.this));
                        break;
                    case 7:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), WomenBeautyAssets.mListSmile(), i, EditorActivity.this));
                        break;
                    case 8:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), WomenBeautyAssets.mListHjban(), i, EditorActivity.this));
                        break;
                    case 9:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), WomenBeautyAssets.mListChfer(), i, EditorActivity.this));
                        break;
                    case 10:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), WomenBeautyAssets.mListZwaq(), i, EditorActivity.this));
                        break;

                }

                viewGroup.addView(inflate);
                return inflate;
            }
        });
        RecyclerTabLayout recycler_tab_layout_women = findViewById(R.id.recycler_tab_layout_women);
        recycler_tab_layout_women.setUpWithAdapter(new WomenTabAdapter(binding.stickerWomenViewpager, getApplicationContext()));
        recycler_tab_layout_women.setPositionThreshold(0.5f);
        recycler_tab_layout_women.setBackgroundColor(ContextCompat.getColor(this, R.color.TabColor));

        binding.stickerViewpager.setAdapter(new PagerAdapter() {
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
                recycler_view_sticker.setLayoutManager(new GridLayoutManager(getApplicationContext(), 6));
                switch (i) {
                    case 0:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), StickersAsset.mListEmojy(), i, EditorActivity.this));
                        break;
                    case 1:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), StickersAsset.mListFlag(), i, EditorActivity.this));
                        break;
                    case 2:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), StickersAsset.mListBoom(), i, EditorActivity.this));
                        break;

                }

                viewGroup.addView(inflate);
                return inflate;
            }
        });
        RecyclerTabLayout recycler_tab_layout = findViewById(R.id.recycler_tab_layout);
        recycler_tab_layout.setUpWithAdapter(new StickerTabAdapter(binding.stickerViewpager, getApplicationContext()));
        recycler_tab_layout.setPositionThreshold(0.5f);
        recycler_tab_layout.setBackgroundColor(ContextCompat.getColor(this, R.color.TabColor));

        binding.stickerMenViewpager.setAdapter(new PagerAdapter() {
            public int getCount() {
                return 8;
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
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), MenBeautyAssets.mListhair(), i, EditorActivity.this));
                        break;
                    case 1:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), MenBeautyAssets.mListGlasses(), i, EditorActivity.this));
                        break;
                    case 2:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), MenBeautyAssets.mListMostach(), i, EditorActivity.this));
                        break;
                    case 3:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), MenBeautyAssets.mListLhya(), i, EditorActivity.this));
                        break;
                    case 4:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), MenBeautyAssets.mListScarf(), i, EditorActivity.this));
                        break;
                    case 5:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), MenBeautyAssets.mListTie(), i, EditorActivity.this));
                        break;
                    case 6:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), MenBeautyAssets.mListTatoo(), i, EditorActivity.this));
                        break;
                    case 7:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), MenBeautyAssets.mListChain(), i, EditorActivity.this));
                        break;

                }

                viewGroup.addView(inflate);
                return inflate;
            }
        });
        RecyclerTabLayout recycler_tab_layout_men = findViewById(R.id.recycler_tab_layout_men);
        recycler_tab_layout_men.setUpWithAdapter(new MenTabAdapter(binding.stickerMenViewpager, getApplicationContext()));
        recycler_tab_layout_men.setPositionThreshold(0.5f);
        recycler_tab_layout_men.setBackgroundColor(ContextCompat.getColor(this, R.color.TabColor));


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
            if (PermissionsUtils.checkWriteStoragePermission(EditorActivity.this)) {
                new SaveEditingBitmap().execute();
            }
        });
        binding.imageViewExit.setOnClickListener(view -> onBackPressed());
        binding.imageViewErase.setOnClickListener(view -> setImageErasePaint());
        binding.imageViewBrush.setOnClickListener(view -> setColorPaint());
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
        binding.imageViewEraseNeon.setOnClickListener(view -> setImageEraseNeon());
        binding.imageViewNeon.setOnClickListener(view -> setColorNeon());
        binding.seekbarEraseSizeNeon.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                quShotCustomEditor.setBrushEraserSize((float) i);
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                quShotCustomEditor.brushEraser();
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
        binding.linearLayoutAll.setOnClickListener(view -> setAllFilter());
        binding.linearLayoutBW.setOnClickListener(view -> setBwFilter());
        binding.linearLayoutVintage.setOnClickListener(view -> setVintageFilter());
        binding.linearLayoutSmooth.setOnClickListener(view -> setSmoothFilter());
        binding.linearLayoutCold.setOnClickListener(view -> setColdFilter());
        binding.linearLayoutWarm.setOnClickListener(view -> setWarmFilter());
        binding.linearLayoutLegacy.setOnClickListener(view -> setLegacyFilter());
        binding.linearLayoutHardmix.setOnClickListener(view -> setHardmixEffect());
        binding.linearLayoutOverlay.setOnClickListener(view -> setOverlayEffect());
        binding.linearLayoutDodge.setOnClickListener(view -> setDodgeEffect());
        binding.linearLayoutDivide.setOnClickListener(view -> setDivideEffect());
        binding.linearLayoutBurn.setOnClickListener(view -> setBurnEffect());
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
        binding.seekbarStickerMenAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
        binding.seekbarStickerWomenAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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

        binding.imageViewAddStickerWomen.setVisibility(View.GONE);
        binding.imageViewAddStickerWomen.setOnClickListener(view -> {
            binding.imageViewAddStickerWomen.setVisibility(View.GONE);
            binding.linearLayoutWrapperStickerWomenList.setVisibility(View.VISIBLE);
        });


        binding.imageViewAddStickerMen.setOnClickListener(view -> {
            binding.imageViewAddStickerMen.setVisibility(View.GONE);
            binding.linearLayoutWrapperStickerMenList.setVisibility(View.VISIBLE);
            binding.imageViewAddStickerMen.setVisibility(View.GONE);
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
        binding.photoEditorView.setBackgroundColor(-16777216);
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
                binding.seekbarStickerMenAlpha.setVisibility(View.VISIBLE);
                binding.seekbarStickerMenAlpha.setProgress(sticker.getAlpha());
                binding.seekbarStickerWomenAlpha.setVisibility(View.VISIBLE);
                binding.seekbarStickerWomenAlpha.setProgress(sticker.getAlpha());
            }

            public void onStickerSelected(@NonNull Sticker sticker) {
                if (sticker instanceof CustomTextView) {
                    ((CustomTextView) sticker).setTextColor(SupportMenu.CATEGORY_MASK);
                    binding.photoEditorView.replace(sticker);
                    binding.photoEditorView.invalidate();
                }
                binding.seekbarStickerAlpha.setVisibility(View.VISIBLE);
                binding.seekbarStickerAlpha.setProgress(sticker.getAlpha());
                binding.seekbarStickerMenAlpha.setVisibility(View.VISIBLE);
                binding.seekbarStickerMenAlpha.setProgress(sticker.getAlpha());
                binding.seekbarStickerWomenAlpha.setVisibility(View.VISIBLE);
                binding.seekbarStickerWomenAlpha.setProgress(sticker.getAlpha());
            }

            public void onStickerDeleted(@NonNull Sticker sticker) {
                binding.seekbarStickerAlpha.setVisibility(View.GONE);
                binding.seekbarStickerMenAlpha.setVisibility(View.GONE);
                binding.seekbarStickerWomenAlpha.setVisibility(View.GONE);
            }

            public void onStickerTouchOutside() {
                binding.seekbarStickerAlpha.setVisibility(View.GONE);
                binding.seekbarStickerMenAlpha.setVisibility(View.GONE);
                binding.seekbarStickerWomenAlpha.setVisibility(View.GONE);
            }

            public void onStickerDoubleTap(@NonNull Sticker sticker) {
                if (sticker instanceof CustomTextView) {
                    sticker.setShow(false);
                    binding.photoEditorView.setHandlingSticker( (Sticker) null);
                    addTextFragment = TextFragment.show(EditorActivity.this, ((CustomTextView) sticker).getQuShotText());
                    textEditor = new TextFragment.TextEditor() {
                        public void onDone(CustomText addTextProperties) {
                            binding.photoEditorView.getStickers().remove(binding.photoEditorView.getLastHandlingSticker());
                            binding.photoEditorView.addSticker(new CustomTextView(EditorActivity.this, addTextProperties));
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

    public void setOverlayEffect() {
        binding.recyclerViewOverlay.setVisibility(View.VISIBLE);
        binding.recyclerViewHardmix.setVisibility(View.GONE);
        binding.recyclerViewDodge.setVisibility(View.GONE);
        binding.recyclerViewDivide.setVisibility(View.GONE);
        binding.recyclerViewBurn.setVisibility(View.GONE);
        binding.textViewListOverlay.setTextColor(ContextCompat.getColor(this, R.color.white));
        binding.textViewListHardmix.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListDodge.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListDivide.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListBurn.setTextColor(ContextCompat.getColor(this, R.color.grayText));
    }

    public void setHardmixEffect() {
        binding.recyclerViewHardmix.setVisibility(View.VISIBLE);
        binding.recyclerViewDodge.setVisibility(View.GONE);
        binding.recyclerViewOverlay.setVisibility(View.GONE);
        binding.recyclerViewDivide.setVisibility(View.GONE);
        binding.recyclerViewBurn.setVisibility(View.GONE);
        binding.textViewListOverlay.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListHardmix.setTextColor(ContextCompat.getColor(this, R.color.white));
        binding.textViewListDodge.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListDivide.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListBurn.setTextColor(ContextCompat.getColor(this, R.color.grayText));
    }

    public void setDodgeEffect() {
        binding.recyclerViewHardmix.setVisibility(View.GONE);
        binding.recyclerViewDodge.setVisibility(View.VISIBLE);
        binding.recyclerViewDivide.setVisibility(View.GONE);
        binding.recyclerViewOverlay.setVisibility(View.GONE);
        binding.recyclerViewBurn.setVisibility(View.GONE);
        binding.textViewListOverlay.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListHardmix.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListDodge.setTextColor(ContextCompat.getColor(this, R.color.white));
        binding.textViewListDivide.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListBurn.setTextColor(ContextCompat.getColor(this, R.color.grayText));
    }

    public void setDivideEffect() {
        binding.recyclerViewHardmix.setVisibility(View.GONE);
        binding.recyclerViewDodge.setVisibility(View.GONE);
        binding.recyclerViewOverlay.setVisibility(View.GONE);
        binding.recyclerViewDivide.setVisibility(View.VISIBLE);
        binding.recyclerViewBurn.setVisibility(View.GONE);
        binding.textViewListHardmix.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListDodge.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListDivide.setTextColor(ContextCompat.getColor(this, R.color.white));
        binding.textViewListBurn.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListOverlay.setTextColor(ContextCompat.getColor(this, R.color.grayText));
    }

    public void setBurnEffect() {
        binding.recyclerViewHardmix.setVisibility(View.GONE);
        binding.recyclerViewDodge.setVisibility(View.GONE);
        binding.recyclerViewOverlay.setVisibility(View.GONE);
        binding.recyclerViewDivide.setVisibility(View.GONE);
        binding.recyclerViewBurn.setVisibility(View.VISIBLE);
        binding.textViewListOverlay.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListHardmix.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListDodge.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListDivide.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListBurn.setTextColor(ContextCompat.getColor(this, R.color.white));
    }

    public void setAllFilter() {
        binding.recyclerViewFilterAll.setVisibility(View.VISIBLE);
        binding.recyclerViewFilterBw.setVisibility(View.GONE);
        binding.recyclerViewFilterVintage.setVisibility(View.GONE);
        binding.recyclerViewFilterSmooth.setVisibility(View.GONE);
        binding.recyclerViewFilterCold.setVisibility(View.GONE);
        binding.recyclerViewFilterWarm.setVisibility(View.GONE);
        binding.recyclerViewFilterLegacy.setVisibility(View.GONE);
        binding.textViewListAll.setTextColor(ContextCompat.getColor(this, R.color.white));
        binding.textViewListCold.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListBw.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListVintage.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListSmooth.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListWarm.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListLegacy.setTextColor(ContextCompat.getColor(this, R.color.grayText));
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
        binding.textViewListAll.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListCold.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListBw.setTextColor(ContextCompat.getColor(this, R.color.white));
        binding.textViewListVintage.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListSmooth.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListWarm.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListLegacy.setTextColor(ContextCompat.getColor(this, R.color.grayText));
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
        binding.textViewListAll.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListCold.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListBw.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListVintage.setTextColor(ContextCompat.getColor(this, R.color.white));
        binding.textViewListSmooth.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListWarm.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListLegacy.setTextColor(ContextCompat.getColor(this, R.color.grayText));
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
        binding.textViewListAll.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListCold.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListBw.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListVintage.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListSmooth.setTextColor(ContextCompat.getColor(this, R.color.white));
        binding.textViewListWarm.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListLegacy.setTextColor(ContextCompat.getColor(this, R.color.grayText));
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
        binding.textViewListAll.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListCold.setTextColor(ContextCompat.getColor(this, R.color.white));
        binding.textViewListBw.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListVintage.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListSmooth.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListWarm.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListLegacy.setTextColor(ContextCompat.getColor(this, R.color.grayText));
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
        binding.textViewListAll.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListCold.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListBw.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListVintage.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListSmooth.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListWarm.setTextColor(ContextCompat.getColor(this, R.color.white));
        binding.textViewListLegacy.setTextColor(ContextCompat.getColor(this, R.color.grayText));
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
        binding.textViewListAll.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListCold.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListBw.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListVintage.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListSmooth.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListWarm.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListLegacy.setTextColor(ContextCompat.getColor(this, R.color.white));
        binding.viewAll.setVisibility(View.INVISIBLE);
        binding.viewCold.setVisibility(View.INVISIBLE);
        binding.viewBw.setVisibility(View.INVISIBLE);
        binding.viewVintage.setVisibility(View.INVISIBLE);
        binding.viewSmooth.setVisibility(View.INVISIBLE);
        binding.viewWarm.setVisibility(View.INVISIBLE);
        binding.viewLegacy.setVisibility(View.VISIBLE);
    }

    private void setBottomToolbar(boolean z) {
        int mVisibility = !z ? View.GONE : View.VISIBLE;
        binding.imageViewBrush.setVisibility(mVisibility);
        binding.imageViewErase.setVisibility(mVisibility);
        binding.imageViewUndo.setVisibility(mVisibility);
        binding.imageViewRedo.setVisibility(mVisibility);
        binding.imageViewClean.setVisibility(mVisibility);
        binding.imageViewCleanNeon.setVisibility(mVisibility);
        binding.imageViewNeon.setVisibility(mVisibility);
        binding.imageViewEraseNeon.setVisibility(mVisibility);
        binding.imageViewUndoNeon.setVisibility(mVisibility);
        binding.imageViewRedoNeon.setVisibility(mVisibility);
    }

    public void setImageErasePaint() {
        binding.seekbarBrushSize.setVisibility(View.GONE);
        binding.recyclerViewColorPaint.setVisibility(View.GONE);
        binding.seekbarEraseSize.setVisibility(View.VISIBLE);
        binding.imageViewErase.setImageResource(R.drawable.ic_erase_selected);
        quShotCustomEditor.brushEraser();
        binding.seekbarEraseSize.setProgress(20);
    }

    public void setImageEraseNeon() {
        binding.seekbarBrushSizeNeon.setVisibility(View.GONE);
        binding.recyclerViewColorNeon.setVisibility(View.GONE);
        binding.seekbarEraseSizeNeon.setVisibility(View.VISIBLE);
        binding.imageViewEraseNeon.setImageResource(R.drawable.ic_erase_selected);
        quShotCustomEditor.brushEraser();
        binding.seekbarEraseSizeNeon.setProgress(20);
    }

    public void setColorNeon() {
        binding.seekbarBrushSizeNeon.setVisibility(View.VISIBLE);
        binding.recyclerViewColorNeon.setVisibility(View.VISIBLE);
        colorAdapter = (ColorAdapter) binding.recyclerViewColorNeon.getAdapter();
        if (colorAdapter != null) {
            colorAdapter.setSelectedColorIndex(0);
        }
        binding.recyclerViewColorNeon.scrollToPosition(0);
        if (colorAdapter != null) {
            colorAdapter.notifyDataSetChanged();
        }
        binding.seekbarEraseSizeNeon.setVisibility(View.GONE);
        binding.imageViewEraseNeon.setImageResource(R.drawable.ic_erase);
        quShotCustomEditor.setBrushMode(2);
        quShotCustomEditor.setBrushDrawingMode(true);
        binding.seekbarBrushSizeNeon.setProgress(20);
    }

    public void setColorPaint() {
        binding.seekbarBrushSize.setVisibility(View.VISIBLE);
        binding.recyclerViewColorPaint.setVisibility(View.VISIBLE);
        binding.recyclerViewColorPaint.scrollToPosition(0);
        colorAdapter = (ColorAdapter) binding.recyclerViewColorPaint.getAdapter();
        if (colorAdapter != null) {
            colorAdapter.setSelectedColorIndex(0);
        }
        if (colorAdapter != null) {
            colorAdapter.notifyDataSetChanged();
        }
        binding.seekbarEraseSize.setVisibility(View.GONE);
        binding.imageViewErase.setImageResource(R.drawable.ic_erase);
        quShotCustomEditor.setBrushMode(1);
        quShotCustomEditor.setBrushDrawingMode(true);
        binding.seekbarBrushSize.setProgress(20);
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
        if (id == R.id.imageViewCloseAdjust || id == R.id.image_view_close_paint || id == R.id.image_view_close_neon || id == R.id.image_view_close_sticker_men || id == R.id.image_view_close_sticker_women || id == R.id.image_view_close_sticker || id == R.id.imageViewCloseText || id == R.id.image_view_close_filter || id == R.id.image_view_close_hardmix) {
            setVisibleSave();
            onBackPressed();
            
        } else if (id == R.id.imageViewSaveAdjust) {
            new SaveFilter().execute();
            binding.imageViewCompareAdjust.setVisibility(View.GONE);
            binding.constraintLayoutAdjust.setVisibility(View.GONE);
            binding.recyclerViewTools.setVisibility(View.VISIBLE);
            binding.constraintLayoutSave.setVisibility(View.VISIBLE);
            ConstraintSet constraintsetAdjust = new ConstraintSet();
            constraintsetAdjust.clone(binding.constraintLayoutRootView);
            constraintsetAdjust.connect(binding.relativeLayoutWrapperPhoto.getId(), 1, binding.constraintLayoutRootView.getId(), 1, 0);
            constraintsetAdjust.connect(binding.relativeLayoutWrapperPhoto.getId(), 4, binding.guideline.getId(), 3, 0);
            constraintsetAdjust.connect(binding.relativeLayoutWrapperPhoto.getId(), 2, binding.constraintLayoutRootView.getId(), 2, 0);
            constraintsetAdjust.applyTo(binding.constraintLayoutRootView);
            setVisibleSave();
            moduleToolsId = Module.NONE;
            
        } else if (id == R.id.image_view_save_paint) {
            showLoading(true);
            runOnUiThread(() -> {
                quShotCustomEditor.setBrushDrawingMode(false);
                binding.imageViewUndo.setVisibility(View.GONE);
                binding.imageViewRedo.setVisibility(View.GONE);
                binding.imageViewClean.setVisibility(View.GONE);
                binding.imageViewErase.setVisibility(View.GONE);
                binding.recyclerViewTools.setVisibility(View.VISIBLE);
                binding.constraintLayoutSave.setVisibility(View.GONE);
                binding.constraintLayoutPaint.setVisibility(View.GONE);
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(binding.constraintLayoutRootView);
                constraintSet.connect(binding.relativeLayoutWrapperPhoto.getId(), 1, binding.constraintLayoutRootView.getId(), 1, 0);
                constraintSet.connect(binding.relativeLayoutWrapperPhoto.getId(), 4, binding.guideline.getId(), 3, 0);
                constraintSet.connect(binding.relativeLayoutWrapperPhoto.getId(), 2, binding.constraintLayoutRootView.getId(), 2, 0);
                constraintSet.applyTo(binding.constraintLayoutRootView);
                binding.photoEditorView.setImageSource(quShotCustomEditor.getBrushDrawingView().getDrawBitmap(binding.photoEditorView.getCurrentBitmap()));
                quShotCustomEditor.clearBrushAllViews();
                showLoading(false);
                reloadingLayout();
            });
            setVisibleSave();
            moduleToolsId = Module.NONE;
            
        } else if (id == R.id.image_view_save_neon) {
            showLoading(true);
            runOnUiThread(() -> {
                quShotCustomEditor.setBrushDrawingMode(false);
                binding.imageViewUndoNeon.setVisibility(View.GONE);
                binding.imageViewRedoNeon.setVisibility(View.GONE);
                binding.recyclerViewTools.setVisibility(View.VISIBLE);
                binding.constraintLayoutConfirmSaveNeon.setVisibility(View.GONE);
                binding.constraintLayoutNeon.setVisibility(View.GONE);
                binding.imageViewEraseNeon.setVisibility(View.GONE);
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(binding.constraintLayoutRootView);
                constraintSet.connect(binding.relativeLayoutWrapperPhoto.getId(), 1, binding.constraintLayoutRootView.getId(), 1, 0);
                constraintSet.connect(binding.relativeLayoutWrapperPhoto.getId(), 4, binding.guideline.getId(), 3, 0);
                constraintSet.connect(binding.relativeLayoutWrapperPhoto.getId(), 2, binding.constraintLayoutRootView.getId(), 2, 0);
                constraintSet.applyTo(binding.constraintLayoutRootView);
                binding.photoEditorView.setImageSource(quShotCustomEditor.getBrushDrawingView().getDrawBitmap(binding.photoEditorView.getCurrentBitmap()));
                quShotCustomEditor.clearBrushAllViews();
                showLoading(false);
                reloadingLayout();
            });
            setVisibleSave();
            moduleToolsId = Module.NONE;
            
        } else if (id == R.id.image_view_save_filter) {
            new SaveFilter().execute();
            binding.imageViewCompareFilter.setVisibility(View.GONE);
            binding.constraintLayoutFilter.setVisibility(View.GONE);
            binding.constraintLayoutConfirmSaveFilter.setVisibility(View.GONE);
            binding.recyclerViewTools.setVisibility(View.VISIBLE);
            setVisibleSave();
            ConstraintSet constraintsete = new ConstraintSet();
            constraintsete.clone(binding.constraintLayoutRootView);
            constraintsete.connect(binding.relativeLayoutWrapperPhoto.getId(), 1, binding.constraintLayoutRootView.getId(), 1, 0);
            constraintsete.connect(binding.relativeLayoutWrapperPhoto.getId(), 4, binding.guideline.getId(), 3, 0);
            constraintsete.connect(binding.relativeLayoutWrapperPhoto.getId(), 2, binding.constraintLayoutRootView.getId(), 2, 0);
            constraintsete.applyTo(binding.constraintLayoutRootView);
            moduleToolsId = Module.NONE;
           
        } else if (id == R.id.image_view_save_hardmix) {
            new SaveFilter().execute();
            binding.imageViewCompareEffect.setVisibility(View.GONE);
            binding.constraintLayoutEffect.setVisibility(View.GONE);
            binding.constraintLayoutConfirmSaveHardmix.setVisibility(View.GONE);
            binding.recyclerViewTools.setVisibility(View.VISIBLE);
            moduleToolsId = Module.NONE;
            ConstraintSet constraintsetEffect = new ConstraintSet();
            constraintsetEffect.clone(binding.constraintLayoutRootView);
            constraintsetEffect.connect(binding.relativeLayoutWrapperPhoto.getId(), 1, binding.constraintLayoutRootView.getId(), 1, 0);
            constraintsetEffect.connect(binding.relativeLayoutWrapperPhoto.getId(), 4, binding.guideline.getId(), 3, 0);
            constraintsetEffect.connect(binding.relativeLayoutWrapperPhoto.getId(), 2, binding.constraintLayoutRootView.getId(), 2, 0);
            constraintsetEffect.applyTo(binding.constraintLayoutRootView);
            setVisibleSave();
            moduleToolsId = Module.NONE;
        } else if (id == R.id.image_view_save_sticker) {
            binding.photoEditorView.setHandlingSticker(null);
            binding.photoEditorView.setLocked(true);
            binding.seekbarStickerAlpha.setVisibility(View.GONE);
            binding.imageViewAddSticker.setVisibility(View.GONE);
            binding.recyclerViewTools.setVisibility(View.VISIBLE);
            if (!binding.photoEditorView.getStickers().isEmpty()) {
                new SaveSticker().execute();
            }
            binding.linearLayoutWrapperStickerList.setVisibility(View.VISIBLE);
            binding.constraintLayoutSticker.setVisibility(View.GONE);
            setVisibleSave();
            moduleToolsId = Module.NONE;
            
        } else if (id == R.id.image_view_save_sticker_men) {
            binding.photoEditorView.setHandlingSticker(null);
            binding.photoEditorView.setLocked(true);
            binding.seekbarStickerMenAlpha.setVisibility(View.GONE);
            binding.recyclerViewTools.setVisibility(View.VISIBLE);
            binding.imageViewAddStickerMen.setVisibility(View.GONE);
            if (!binding.photoEditorView.getStickers().isEmpty()) {
                new SaveSticker().execute();
            }
            binding.linearLayoutWrapperStickerMenList.setVisibility(View.VISIBLE);
            binding.constraintLayoutStickerMen.setVisibility(View.GONE);
            setVisibleSave();
            moduleToolsId = Module.NONE;
            
        } else if (id == R.id.image_view_save_sticker_women) {
            binding.photoEditorView.setHandlingSticker(null);
            binding.photoEditorView.setLocked(true);
            binding.seekbarStickerWomenAlpha.setVisibility(View.GONE);
            binding.imageViewAddStickerWomen.setVisibility(View.GONE);
            binding.recyclerViewTools.setVisibility(View.VISIBLE);
            if (!binding.photoEditorView.getStickers().isEmpty()) {
                new SaveSticker().execute();
            }
            binding.linearLayoutWrapperStickerWomenList.setVisibility(View.VISIBLE);
            binding.constraintLayoutStickerWomen.setVisibility(View.GONE);
            setVisibleSave();
            moduleToolsId = Module.NONE;
            
        } else if (id == R.id.imageViewSaveText) {
            binding.photoEditorView.setHandlingSticker(null);
            binding.photoEditorView.setLocked(true);
            binding.constraintLayoutConfirmText.setVisibility(View.GONE);
            binding.relativeLayoutAddText.setVisibility(View.GONE);
            if (!binding.photoEditorView.getStickers().isEmpty()) {
                new SaveSticker().execute();
            }
            ConstraintSet constraintsetText = new ConstraintSet();
            constraintsetText.clone(binding.constraintLayoutRootView);
            constraintsetText.connect(binding.relativeLayoutWrapperPhoto.getId(), 1, binding.constraintLayoutRootView.getId(), 1, 0);
            constraintsetText.connect(binding.relativeLayoutWrapperPhoto.getId(), 4, binding.guideline.getId(), 3, 0);
            constraintsetText.connect(binding.relativeLayoutWrapperPhoto.getId(), 2, binding.constraintLayoutRootView.getId(), 2, 0);
            constraintsetText.applyTo(binding.constraintLayoutRootView);
            binding.recyclerViewTools.setVisibility(View.VISIBLE);
            setVisibleSave();
            moduleToolsId = Module.NONE;
        } else if (id == R.id.image_view_redo_neon || id == R.id.image_view_redo) {
            quShotCustomEditor.redoBrush();
        } else if (id == R.id.image_view_undo_neon || id == R.id.image_view_undo) {
            quShotCustomEditor.undoBrush();
        } else if (id == R.id.image_view_clean_neon || id == R.id.image_view_clean) {
            quShotCustomEditor.clearBrushAllViews();
        }
    }

    public void isPermissionGranted(boolean z, String string) {
        if (z) {
            new SaveEditingBitmap().execute();
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
        ConstraintSet constraintSet;
        switch (module) {
            case TEXT:
                setGoneSave();
                binding.photoEditorView.setLocked(false);
                textFragment();
                binding.recyclerViewTools.setVisibility(View.GONE);
                binding.constraintLayoutDraw.setVisibility(View.GONE);
                binding.constraintLayoutEmoji.setVisibility(View.GONE);
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
                ConstraintSet constraintsetEffect = new ConstraintSet();
                constraintsetEffect.clone(binding.constraintLayoutRootView);
                constraintsetEffect.connect(binding.relativeLayoutWrapperPhoto.getId(), 1, binding.constraintLayoutRootView.getId(), 1, 0);
                constraintsetEffect.connect(binding.relativeLayoutWrapperPhoto.getId(), 4, binding.guideline.getId(), 3, 0);
                constraintsetEffect.connect(binding.relativeLayoutWrapperPhoto.getId(), 2, binding.constraintLayoutRootView.getId(), 2, 0);
                constraintsetEffect.applyTo(binding.constraintLayoutRootView);
                binding.constraintLayoutConfirmText.setVisibility(View.VISIBLE);
                binding.relativeLayoutAddText.setVisibility(View.VISIBLE);
                break;
            case ADJUST:
                setGoneSave();
                binding.imageViewCompareAdjust.setVisibility(View.VISIBLE);
                binding.constraintLayoutDraw.setVisibility(View.GONE);
                binding.constraintLayoutEmoji.setVisibility(View.GONE);
                binding.constraintLayoutAdjust.setVisibility(View.VISIBLE);
                binding.constraintLayoutNeon.setVisibility(View.GONE);
                binding.constraintLayoutFilter.setVisibility(View.GONE);
                binding.constraintLayoutPaint.setVisibility(View.GONE);
                if (!binding.photoEditorView.getStickers().isEmpty()) {
                    binding.photoEditorView.getStickers().clear();
                    binding.photoEditorView.setHandlingSticker(null);
                }

                ConstraintSet constraintsetAdjust = new ConstraintSet();
                constraintsetAdjust.clone(binding.constraintLayoutRootView);
                constraintsetAdjust.connect(binding.relativeLayoutWrapperPhoto.getId(), 1, binding.constraintLayoutRootView.getId(), 1, 0);
                constraintsetAdjust.connect(binding.relativeLayoutWrapperPhoto.getId(), 4, binding.guidelinePaint.getId(), 3, 0);
                constraintsetAdjust.connect(binding.relativeLayoutWrapperPhoto.getId(), 2, binding.constraintLayoutRootView.getId(), 2, 0);
                constraintsetAdjust.applyTo(binding.constraintLayoutRootView);
                binding.recyclerViewTools.setVisibility(View.GONE);
                binding.constraintLayoutSave.setVisibility(View.GONE);
                binding.relativeLayoutAddText.setVisibility(View.GONE);
                binding.constraintLayoutConfirmText.setVisibility(View.GONE);
                quShotCustomEditor.clearBrushAllViews();
                quShotCustomEditor.setBrushDrawingMode(false);
                adjustAdapter = new AdjustAdapter(getApplicationContext(), this);
                binding.recyclerViewAdjust.setAdapter(adjustAdapter);
                adjustAdapter.setSelectedAdjust(0);
                quShotCustomEditor.setAdjustFilter(adjustAdapter.getFilterConfig());
                break;
            case FILTER:
                setGoneSave();
                if (!binding.photoEditorView.getStickers().isEmpty()) {
                    binding.photoEditorView.getStickers().clear();
                    binding.photoEditorView.setHandlingSticker(null);
                }
                binding.relativeLayoutAddText.setVisibility(View.GONE);
                binding.constraintLayoutConfirmText.setVisibility(View.GONE);
                binding.constraintLayoutDraw.setVisibility(View.GONE);
                binding.constraintLayoutEmoji.setVisibility(View.GONE);
                new allFilters().execute();
                new bwFilters().execute();
                new vintageFilters().execute();
                new smoothFilters().execute();
                new coldFilters().execute();
                new warmFilters().execute();
                new legacyFilters().execute();
                break;
            case EMOJI:
                binding.constraintLayoutEmoji.setVisibility(View.VISIBLE);
                binding.constraintLayoutDraw.setVisibility(View.GONE);
                break;
            case DRAW:
                binding.constraintLayoutDraw.setVisibility(View.VISIBLE);
                binding.constraintLayoutEmoji.setVisibility(View.GONE);
                break;
            case OVERLAY:
                setGoneSave();
                new effectOvarlay().execute();
                new effectHardmix().execute();
                new effectDodge().execute();
                new effectBurn().execute();
                new effectDivide().execute();
                binding.constraintLayoutDraw.setVisibility(View.GONE);
                binding.constraintLayoutEmoji.setVisibility(View.GONE);
                break;
            case RATIO:
                new openBlurFragment().execute();
                binding.constraintLayoutDraw.setVisibility(View.GONE);
                binding.constraintLayoutEmoji.setVisibility(View.GONE);
                goneLayout();
                break;
            case BACKGROUND:
                new openFrameFragment().execute();
                binding.constraintLayoutDraw.setVisibility(View.GONE);
                binding.constraintLayoutEmoji.setVisibility(View.GONE);
                goneLayout();
                break;
            case SPLASH:
                new openSplashBrushFragment(true).execute();
                binding.constraintLayoutDraw.setVisibility(View.GONE);
                binding.constraintLayoutEmoji.setVisibility(View.GONE);
                break;
            case SQUARESPLASH:
                new openSplashSquareFragment(true).execute();
                binding.constraintLayoutDraw.setVisibility(View.GONE);
                binding.constraintLayoutEmoji.setVisibility(View.GONE);
                break;
            case BLUR:
                new openSplashBrushFragment(false).execute();
                binding.constraintLayoutDraw.setVisibility(View.GONE);
                binding.constraintLayoutEmoji.setVisibility(View.GONE);
                break;
            case SQUAEBLUR:
                new openSplashSquareFragment(false).execute();
                binding.constraintLayoutDraw.setVisibility(View.GONE);
                binding.constraintLayoutEmoji.setVisibility(View.GONE);
                break;
            case COLOR:
                ColorSplashFragment.show(this, binding.photoEditorView.getCurrentBitmap());
                binding.constraintLayoutDraw.setVisibility(View.GONE);
                binding.constraintLayoutEmoji.setVisibility(View.GONE);
                goneLayout();
                break;
            case CROP:
                CropFragment.show(this, this, binding.photoEditorView.getCurrentBitmap());
                binding.constraintLayoutDraw.setVisibility(View.GONE);
                binding.constraintLayoutEmoji.setVisibility(View.GONE);
                goneLayout();
                break;
            case ROTATE:
                RotateFragment.show(this, this, binding.photoEditorView.getCurrentBitmap());
                binding.constraintLayoutDraw.setVisibility(View.GONE);
                binding.constraintLayoutEmoji.setVisibility(View.GONE);
                goneLayout();
                break;
        }
        binding.photoEditorView.setHandlingSticker(null);
    }

    public void onQuShotStickerToolSelected(Module module) {
        moduleToolsId = module;
        ConstraintSet constraintSet;
        switch (module) {
            case STICKER:
                setGoneSave();
                binding.photoEditorView.setLocked(false);
                binding.constraintLayoutSticker.setVisibility(View.VISIBLE);
                binding.constraintLayoutAdjust.setVisibility(View.GONE);
                binding.constraintLayoutEmoji.setVisibility(View.GONE);
                binding.constraintLayoutNeon.setVisibility(View.GONE);
                binding.constraintLayoutFilter.setVisibility(View.GONE);
                binding.linearLayoutWrapperStickerList.setVisibility(View.VISIBLE);
                quShotCustomEditor.setFilterEffect("");
                quShotCustomEditor.clearBrushAllViews();
                quShotCustomEditor.setBrushDrawingMode(false);
                if (!binding.photoEditorView.getStickers().isEmpty()) {
                    binding.photoEditorView.getStickers().clear();
                    binding.photoEditorView.setHandlingSticker(null);
                }
                binding.relativeLayoutAddText.setVisibility(View.GONE);
                binding.constraintLayoutConfirmText.setVisibility(View.GONE);
                break;
            case MACKUER:
                setGoneSave();
                binding.photoEditorView.setLocked(false);
                binding.constraintLayoutStickerMen.setVisibility(View.VISIBLE);
                binding.constraintLayoutAdjust.setVisibility(View.GONE);
                binding.constraintLayoutEmoji.setVisibility(View.GONE);
                binding.constraintLayoutNeon.setVisibility(View.GONE);
                binding.constraintLayoutFilter.setVisibility(View.GONE);
                binding.linearLayoutWrapperStickerMenList.setVisibility(View.VISIBLE);
                quShotCustomEditor.setFilterEffect("");
                quShotCustomEditor.clearBrushAllViews();
                quShotCustomEditor.setBrushDrawingMode(false);
                if (!binding.photoEditorView.getStickers().isEmpty()) {
                    binding.photoEditorView.getStickers().clear();
                    binding.photoEditorView.setHandlingSticker(null);
                }
                binding.relativeLayoutAddText.setVisibility(View.GONE);
                binding.constraintLayoutConfirmText.setVisibility(View.GONE);
                break;
            case BEAUTY:
                setGoneSave();
                binding.photoEditorView.setLocked(false);
                binding.constraintLayoutStickerWomen.setVisibility(View.VISIBLE);
                binding.linearLayoutWrapperStickerWomenList.setVisibility(View.VISIBLE);
                binding.constraintLayoutAdjust.setVisibility(View.GONE);
                binding.constraintLayoutEmoji.setVisibility(View.GONE);
                binding.constraintLayoutNeon.setVisibility(View.GONE);
                binding.constraintLayoutFilter.setVisibility(View.GONE);
                quShotCustomEditor.setFilterEffect("");
                quShotCustomEditor.clearBrushAllViews();
                quShotCustomEditor.setBrushDrawingMode(false);
                if (!binding.photoEditorView.getStickers().isEmpty()) {
                    binding.photoEditorView.getStickers().clear();
                    binding.photoEditorView.setHandlingSticker(null);
                }
                binding.relativeLayoutAddText.setVisibility(View.GONE);
                binding.constraintLayoutConfirmText.setVisibility(View.GONE);
                break;
        }
        binding.photoEditorView.setHandlingSticker(null);
    }

    public void onQuShotDrawToolSelected(Module module) {
        moduleToolsId = module;
        ConstraintSet constraintSet;
        switch (module) {
            case PAINT:
                setColorPaint();
                quShotCustomEditor.setBrushDrawingMode(true);
                binding.constraintLayoutPaint.setVisibility(View.VISIBLE);
                binding.recyclerViewTools.setVisibility(View.GONE);
                binding.constraintLayoutDraw.setVisibility(View.GONE);
                binding.constraintLayoutPaint.setVisibility(View.VISIBLE);
                binding.constraintLayoutAdjust.setVisibility(View.GONE);
                binding.constraintLayoutFilter.setVisibility(View.GONE);
                binding.constraintLayoutNeon.setVisibility(View.GONE);
                binding.constraintLayoutSave.setVisibility(View.VISIBLE);
                binding.relativeLayoutAddText.setVisibility(View.GONE);
                binding.constraintLayoutConfirmText.setVisibility(View.GONE);
                quShotCustomEditor.setFilterEffect("");
                quShotCustomEditor.clearBrushAllViews();
                quShotCustomEditor.setBrushDrawingMode(false);
                setGoneSave();
                setBottomToolbar(true);
                constraintSet = new ConstraintSet();
                constraintSet.clone(binding.constraintLayoutRootView);
                constraintSet.connect(binding.relativeLayoutWrapperPhoto.getId(), 1, binding.constraintLayoutRootView.getId(), 1, 0);
                constraintSet.connect(binding.relativeLayoutWrapperPhoto.getId(), 4, binding.guidelinePaint.getId(), 3, 0);
                constraintSet.connect(binding.relativeLayoutWrapperPhoto.getId(), 2, binding.constraintLayoutRootView.getId(), 2, 0);
                constraintSet.applyTo(binding.constraintLayoutRootView);
                quShotCustomEditor.setBrushMode(1);
                reloadingLayout();
                break;
            case COLORED:
                new openColoredFragment().execute();
                binding.constraintLayoutDraw.setVisibility(View.GONE);
                goneLayout();
                break;
            case NEON:
                setColorNeon();
                quShotCustomEditor.setBrushDrawingMode(true);
                binding.recyclerViewTools.setVisibility(View.GONE);
                binding.constraintLayoutConfirmSaveNeon.setVisibility(View.VISIBLE);
                binding.constraintLayoutDraw.setVisibility(View.GONE);
                binding.constraintLayoutNeon.setVisibility(View.VISIBLE);
                binding.constraintLayoutAdjust.setVisibility(View.GONE);
                binding.constraintLayoutFilter.setVisibility(View.GONE);
                binding.constraintLayoutPaint.setVisibility(View.GONE);
                binding.relativeLayoutAddText.setVisibility(View.GONE);
                binding.constraintLayoutConfirmText.setVisibility(View.GONE);
                quShotCustomEditor.setFilterEffect("");
                quShotCustomEditor.clearBrushAllViews();
                quShotCustomEditor.setBrushDrawingMode(false);
                setGoneSave();
                setBottomToolbar(true);
                constraintSet = new ConstraintSet();
                constraintSet.clone(binding.constraintLayoutRootView);
                constraintSet.connect(binding.relativeLayoutWrapperPhoto.getId(), 1, binding.constraintLayoutRootView.getId(), 1, 0);
                constraintSet.connect(binding.relativeLayoutWrapperPhoto.getId(), 4, binding.constraintLayoutNeon.getId(), 3, 0);
                constraintSet.connect(binding.relativeLayoutWrapperPhoto.getId(), 2, binding.constraintLayoutRootView.getId(), 2, 0);
                constraintSet.applyTo(binding.constraintLayoutRootView);
                quShotCustomEditor.setBrushMode(2);
                reloadingLayout();
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

    public void setGoneSave() {
        binding.constraintLayoutSaveEditing.setVisibility(View.GONE);
    }

    public void setVisibleSave() {
        binding.constraintLayoutSaveEditing.setVisibility(View.VISIBLE);
    }

    public void onBackPressed() {
        if (moduleToolsId != null) {
            try {
                switch (moduleToolsId) {
                    case PAINT:
                        binding.constraintLayoutPaint.setVisibility(View.GONE);
                        setVisibleSave();
                        binding.imageViewUndo.setVisibility(View.GONE);
                        binding.imageViewRedo.setVisibility(View.GONE);
                        binding.imageViewClean.setVisibility(View.GONE);
                        binding.imageViewErase.setVisibility(View.GONE);
                        binding.recyclerViewTools.setVisibility(View.VISIBLE);
                        binding.constraintLayoutSave.setVisibility(View.GONE);
                        quShotCustomEditor.setBrushDrawingMode(false);
                        ConstraintSet constraintSet = new ConstraintSet();
                        constraintSet.clone(binding.constraintLayoutRootView);
                        constraintSet.connect(binding.relativeLayoutWrapperPhoto.getId(), 1, binding.constraintLayoutRootView.getId(), 1, 0);
                        constraintSet.connect(binding.relativeLayoutWrapperPhoto.getId(), 4, binding.guideline.getId(), 3, 0);
                        constraintSet.connect(binding.relativeLayoutWrapperPhoto.getId(), 2, binding.constraintLayoutRootView.getId(), 2, 0);
                        constraintSet.applyTo(binding.constraintLayoutRootView);
                        quShotCustomEditor.clearBrushAllViews();
                        setVisibleSave();
                        moduleToolsId = Module.NONE;
                        reloadingLayout();
                        return;
                    case NEON:
                        setVisibleSave();
                        binding.imageViewUndoNeon.setVisibility(View.GONE);
                        binding.imageViewRedoNeon.setVisibility(View.GONE);
                        binding.imageViewEraseNeon.setVisibility(View.GONE);
                        binding.constraintLayoutNeon.setVisibility(View.GONE);
                        binding.recyclerViewTools.setVisibility(View.VISIBLE);
                        binding.constraintLayoutConfirmSaveNeon.setVisibility(View.GONE);
                        quShotCustomEditor.setBrushDrawingMode(false);
                        ConstraintSet constraintSet1 = new ConstraintSet();
                        constraintSet1.clone(binding.constraintLayoutRootView);
                        constraintSet1.connect(binding.relativeLayoutWrapperPhoto.getId(), 1, binding.constraintLayoutRootView.getId(), 1, 0);
                        constraintSet1.connect(binding.relativeLayoutWrapperPhoto.getId(), 4, binding.guideline.getId(), 3, 0);
                        constraintSet1.connect(binding.relativeLayoutWrapperPhoto.getId(), 2, binding.constraintLayoutRootView.getId(), 2, 0);
                        constraintSet1.applyTo(binding.constraintLayoutRootView);
                        quShotCustomEditor.clearBrushAllViews();
                        setVisibleSave();
                        moduleToolsId = Module.NONE;
                        reloadingLayout();
                        return;
                    case TEXT:
                        if (!binding.photoEditorView.getStickers().isEmpty()) {
                            binding.photoEditorView.getStickers().clear();
                            binding.photoEditorView.setHandlingSticker(null);
                        }
                        binding.recyclerViewTools.setVisibility(View.VISIBLE);
                        binding.relativeLayoutAddText.setVisibility(View.GONE);
                        binding.constraintLayoutConfirmText.setVisibility(View.GONE);
                        binding.photoEditorView.setHandlingSticker(null);
                        binding.photoEditorView.setLocked(true);
                        ConstraintSet constraintsetEffect = new ConstraintSet();
                        constraintsetEffect.clone(binding.constraintLayoutRootView);
                        constraintsetEffect.connect(binding.relativeLayoutWrapperPhoto.getId(), 1, binding.constraintLayoutRootView.getId(), 1, 0);
                        constraintsetEffect.connect(binding.relativeLayoutWrapperPhoto.getId(), 4, binding.guideline.getId(), 3, 0);
                        constraintsetEffect.connect(binding.relativeLayoutWrapperPhoto.getId(), 2, binding.constraintLayoutRootView.getId(), 2, 0);
                        constraintsetEffect.applyTo(binding.constraintLayoutRootView);
                        setVisibleSave();
                        moduleToolsId = Module.NONE;
                        return;
                    case ADJUST:
                        quShotCustomEditor.setFilterEffect("");
                        binding.imageViewCompareAdjust.setVisibility(View.GONE);
                        binding.constraintLayoutAdjust.setVisibility(View.GONE);
                        ConstraintSet constraintsetAdjust = new ConstraintSet();
                        constraintsetAdjust.clone(binding.constraintLayoutRootView);
                        constraintsetAdjust.connect(binding.relativeLayoutWrapperPhoto.getId(), 1, binding.constraintLayoutRootView.getId(), 1, 0);
                        constraintsetAdjust.connect(binding.relativeLayoutWrapperPhoto.getId(), 4, binding.guideline.getId(), 3, 0);
                        constraintsetAdjust.connect(binding.relativeLayoutWrapperPhoto.getId(), 2, binding.constraintLayoutRootView.getId(), 2, 0);
                        constraintsetAdjust.applyTo(binding.constraintLayoutRootView);
                        binding.recyclerViewTools.setVisibility(View.VISIBLE);
                        binding.constraintLayoutSave.setVisibility(View.VISIBLE);
                        setVisibleSave();
                        moduleToolsId = Module.NONE;
                        return;
                    case FILTER:
                        binding.constraintLayoutFilter.setVisibility(View.GONE);
                        binding.constraintLayoutConfirmSaveFilter.setVisibility(View.GONE);
                        binding.recyclerViewTools.setVisibility(View.VISIBLE);
                        ConstraintSet constraintsete = new ConstraintSet();
                        constraintsete.clone(binding.constraintLayoutRootView);
                        constraintsete.connect(binding.relativeLayoutWrapperPhoto.getId(), 1, binding.constraintLayoutRootView.getId(), 1, 0);
                        constraintsete.connect(binding.relativeLayoutWrapperPhoto.getId(), 4, binding.guideline.getId(), 3, 0);
                        constraintsete.connect(binding.relativeLayoutWrapperPhoto.getId(), 2, binding.constraintLayoutRootView.getId(), 2, 0);
                        constraintsete.applyTo(binding.constraintLayoutRootView);
                        setVisibleSave();
                        quShotCustomEditor.setFilterEffect("");
                        binding.imageViewCompareFilter.setVisibility(View.GONE);
                        listAllFilter.clear();
                        if (binding.recyclerViewFilterAll.getAdapter() != null) {
                            binding.recyclerViewFilterAll.getAdapter().notifyDataSetChanged();
                        }
                        moduleToolsId = Module.NONE;
                        return;
                    case STICKER:
                        if (binding.photoEditorView.getStickers().size() <= 0) {
                            binding.linearLayoutWrapperStickerList.setVisibility(View.VISIBLE);
                            binding.constraintLayoutSticker.setVisibility(View.GONE);
                            binding.imageViewAddSticker.setVisibility(View.GONE);
                            binding.photoEditorView.setHandlingSticker(null);
                            binding.recyclerViewTools.setVisibility(View.VISIBLE);
                            binding.photoEditorView.setLocked(true);
                            moduleToolsId = Module.NONE;
                        } else if (binding.imageViewAddSticker.getVisibility() == View.VISIBLE) {
                            binding.photoEditorView.getStickers().clear();
                            binding.imageViewAddSticker.setVisibility(View.GONE);
                            binding.photoEditorView.setHandlingSticker(null);
                            binding.linearLayoutWrapperStickerList.setVisibility(View.VISIBLE);
                            binding.constraintLayoutSticker.setVisibility(View.GONE);
                            binding.recyclerViewTools.setVisibility(View.VISIBLE);
                            moduleToolsId = Module.NONE;
                        } else {
                            binding.linearLayoutWrapperStickerList.setVisibility(View.GONE);
                            binding.imageViewAddSticker.setVisibility(View.VISIBLE);
                        }
                        binding.linearLayoutWrapperStickerList.setVisibility(View.VISIBLE);
                        moduleToolsId = Module.NONE;
                        setVisibleSave();
                        return;
                    case MACKUER:
                        binding.linearLayoutWrapperStickerMenList.setVisibility(View.VISIBLE);
                        if (binding.photoEditorView.getStickers().size() <= 0) {
                            binding.linearLayoutWrapperStickerMenList.setVisibility(View.VISIBLE);
                            binding.constraintLayoutStickerMen.setVisibility(View.GONE);
                            binding.photoEditorView.setHandlingSticker(null);
                            binding.imageViewAddStickerMen.setVisibility(View.GONE);
                            binding.recyclerViewTools.setVisibility(View.VISIBLE);
                            binding.photoEditorView.setLocked(true);
                            moduleToolsId = Module.NONE;
                        } else if (binding.imageViewAddStickerMen.getVisibility() == View.VISIBLE) {
                            binding.photoEditorView.getStickers().clear();
                            binding.photoEditorView.setHandlingSticker(null);
                            binding.imageViewAddStickerMen.setVisibility(View.GONE);
                            binding.linearLayoutWrapperStickerMenList.setVisibility(View.VISIBLE);
                            binding.constraintLayoutStickerMen.setVisibility(View.GONE);
                            binding.recyclerViewTools.setVisibility(View.VISIBLE);
                            moduleToolsId = Module.NONE;
                        } else {
                            binding.linearLayoutWrapperStickerMenList.setVisibility(View.GONE);
                        }
                        binding.imageViewAddStickerMen.setVisibility(View.VISIBLE);
                        moduleToolsId = Module.NONE;
                        setVisibleSave();
                        return;
                    case BEAUTY:
                        if (binding.photoEditorView.getStickers().size() <= 0) {
                            binding.linearLayoutWrapperStickerWomenList.setVisibility(View.VISIBLE);
                            binding.constraintLayoutStickerWomen.setVisibility(View.GONE);
                            binding.imageViewAddStickerWomen.setVisibility(View.GONE);
                            binding.photoEditorView.setHandlingSticker(null);
                            binding.recyclerViewTools.setVisibility(View.VISIBLE);
                            binding.photoEditorView.setLocked(true);
                            moduleToolsId = Module.NONE;
                        } else if (binding.imageViewAddStickerWomen.getVisibility() == View.VISIBLE) {
                            binding.photoEditorView.getStickers().clear();
                            binding.imageViewAddStickerWomen.setVisibility(View.GONE);
                            binding.photoEditorView.setHandlingSticker(null);
                            binding.linearLayoutWrapperStickerWomenList.setVisibility(View.VISIBLE);
                            binding.constraintLayoutStickerWomen.setVisibility(View.GONE);
                            binding.recyclerViewTools.setVisibility(View.VISIBLE);
                            moduleToolsId = Module.NONE;
                        } else {
                            binding.linearLayoutWrapperStickerWomenList.setVisibility(View.GONE);
                            binding.imageViewAddStickerWomen.setVisibility(View.VISIBLE);
                        }
                        binding.linearLayoutWrapperStickerWomenList.setVisibility(View.VISIBLE);
                        moduleToolsId = Module.NONE;
                        setVisibleSave();
                        return;
                    case OVERLAY:
                        quShotCustomEditor.setFilterEffect("");
                        binding.imageViewCompareEffect.setVisibility(View.GONE);
                        binding.constraintLayoutEffect.setVisibility(View.GONE);
                        listOverlay.clear();
                        if (binding.recyclerViewOverlay.getAdapter() != null) {
                            binding.recyclerViewOverlay.getAdapter().notifyDataSetChanged();
                        }
                        ConstraintSet constraintsetOverlay = new ConstraintSet();
                        constraintsetOverlay.clone(binding.constraintLayoutRootView);
                        constraintsetOverlay.connect(binding.relativeLayoutWrapperPhoto.getId(), 1, binding.constraintLayoutRootView.getId(), 1, 0);
                        constraintsetOverlay.connect(binding.relativeLayoutWrapperPhoto.getId(), 4, binding.guideline.getId(), 3, 0);
                        constraintsetOverlay.connect(binding.relativeLayoutWrapperPhoto.getId(), 2, binding.constraintLayoutRootView.getId(), 2, 0);
                        constraintsetOverlay.applyTo(binding.constraintLayoutRootView);
                        setVisibleSave();
                        binding.constraintLayoutConfirmSaveHardmix.setVisibility(View.GONE);
                        binding.recyclerViewTools.setVisibility(View.VISIBLE);
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
        binding.linearLayoutWrapperStickerMenList.setVisibility(View.GONE);
        binding.linearLayoutWrapperStickerWomenList.setVisibility(View.GONE);
        binding.imageViewAddSticker.setVisibility(View.VISIBLE);
        binding.imageViewAddStickerWomen.setVisibility(View.VISIBLE);
        binding.imageViewAddStickerMen.setVisibility(View.VISIBLE);
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
        allFilters() {}

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
            binding.recyclerViewFilterAll.setAdapter(new FilterAdapter(listAllFilter, EditorActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.ALL_FILTERS)));
            binding.imageViewCompareFilter.setVisibility(View.VISIBLE);
            binding.constraintLayoutFilter.setVisibility(View.VISIBLE);
            binding.constraintLayoutConfirmSaveFilter.setVisibility(View.VISIBLE);
            binding.recyclerViewTools.setVisibility(View.GONE);
            binding.photoEditorView.setHandlingSticker(null);
            binding.photoEditorView.getStickers().clear();
            binding.seekbarFilter.setProgress(100);
            showLoading(false);
            quShotCustomEditor.setFilterEffect("");
            quShotCustomEditor.clearBrushAllViews();
            quShotCustomEditor.setBrushDrawingMode(false);

            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(binding.constraintLayoutRootView);
            constraintSet.connect(binding.relativeLayoutWrapperPhoto.getId(), 1, binding.constraintLayoutRootView.getId(), 1, 0);
            constraintSet.connect(binding.relativeLayoutWrapperPhoto.getId(), 4, binding.guidelinePaint.getId(), 3, 0);
            constraintSet.connect(binding.relativeLayoutWrapperPhoto.getId(), 2, binding.constraintLayoutRootView.getId(), 2, 0);
            constraintSet.applyTo(binding.constraintLayoutRootView);
        }
    }

    class bwFilters extends AsyncTask<Void, Void, Void> {
        bwFilters() {}

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
            binding.recyclerViewFilterBw.setAdapter(new FilterAdapter(listBwFilter, EditorActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.BW_FILTERS)));
            binding.imageViewCompareFilter.setVisibility(View.VISIBLE);
            binding.seekbarFilter.setProgress(100);
            showLoading(false);
        }
    }

    class vintageFilters extends AsyncTask<Void, Void, Void> {
        vintageFilters() {}

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
            binding.recyclerViewFilterVintage.setAdapter(new FilterAdapter(listVintageFilter, EditorActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.VINTAGE_FILTERS)));
            binding.imageViewCompareFilter.setVisibility(View.VISIBLE);
            binding.seekbarFilter.setProgress(100);
            showLoading(false);
        }
    }

    class smoothFilters extends AsyncTask<Void, Void, Void> {
        smoothFilters() {}

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
            binding.recyclerViewFilterSmooth.setAdapter(new FilterAdapter(listSmoothFilter, EditorActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.SMOOTH_FILTERS)));
            binding.imageViewCompareFilter.setVisibility(View.VISIBLE);
            binding.seekbarFilter.setProgress(100);
            showLoading(false);
        }
    }

    class coldFilters extends AsyncTask<Void, Void, Void> {
        coldFilters() {}

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
            binding.recyclerViewFilterCold.setAdapter(new FilterAdapter(listColdFilter, EditorActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.COLD_FILTERS)));
            binding.imageViewCompareFilter.setVisibility(View.VISIBLE);
            binding.seekbarFilter.setProgress(100);
            showLoading(false);
        }
    }

    class warmFilters extends AsyncTask<Void, Void, Void> {
        warmFilters() {}

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
            binding.recyclerViewFilterWarm.setAdapter(new FilterAdapter(listWarmFilter, EditorActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.WARM_FILTERS)));
            binding.imageViewCompareFilter.setVisibility(View.VISIBLE);
            binding.seekbarFilter.setProgress(100);
            showLoading(false);
        }
    }

    class legacyFilters extends AsyncTask<Void, Void, Void> {
        legacyFilters() { }
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
            binding.recyclerViewFilterLegacy.setAdapter(new FilterAdapter(listLegacyFilter, EditorActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.LEGACY_FILTERS)));
            binding.imageViewCompareFilter.setVisibility(View.VISIBLE);
            binding.seekbarFilter.setProgress(100);
            showLoading(false);
        }
    }

    class openBlurFragment extends AsyncTask<Void, Bitmap, Bitmap> {
        openBlurFragment() {}

        public void onPreExecute() {
            showLoading(true);
        }

        public Bitmap doInBackground(Void... voids) {
            return FilterUtils.getBlurImageFromBitmap(binding.photoEditorView.getCurrentBitmap(), 5.0f);
        }

        public void onPostExecute(Bitmap bitmap) {
            showLoading(false);
            RatioFragment.show(EditorActivity.this, EditorActivity.this, binding.photoEditorView.getCurrentBitmap(), bitmap);
        }
    }

    class openFrameFragment extends AsyncTask<Void, Bitmap, Bitmap> {
        openFrameFragment() {}

        public void onPreExecute() {
            showLoading(true);
        }

        public Bitmap doInBackground(Void... voids) {
            return FilterUtils.getBlurImageFromBitmap(binding.photoEditorView.getCurrentBitmap(), 5.0f);
        }

        public void onPostExecute(Bitmap bitmap) {
            showLoading(false);
            FrameFragment.show(EditorActivity.this, EditorActivity.this, binding.photoEditorView.getCurrentBitmap(), bitmap);
        }
    }

    class openShapeFragment extends AsyncTask<Void, List<Bitmap>, List<Bitmap>> {
        openShapeFragment() {}

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
            MosaicFragment.show(EditorActivity.this, list.get(0), list.get(1), EditorActivity.this);
        }
    }

    class openColoredFragment extends AsyncTask<Void, List<Bitmap>, List<Bitmap>> {
        openColoredFragment() { }

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
            ColoredFragment.show(EditorActivity.this, list.get(0), list.get(1), EditorActivity.this);
        }
    }

    class effectDodge extends AsyncTask<Void, Void, Void> {
        effectDodge() { }

        public void onPreExecute() {
            showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            listDodge.clear();
            listDodge.addAll(EffectCodeAsset.getListBitmapDodgeEffect(ThumbnailUtils.extractThumbnail(binding.photoEditorView.getCurrentBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voids) {
            binding.recyclerViewDodge.setAdapter(new HardmixAdapter(listDodge, EditorActivity.this, getApplicationContext(), Arrays.asList(EffectCodeAsset.DODGE_EFFECTS)));
            binding.imageViewCompareEffect.setVisibility(View.VISIBLE);
            binding.seekbarEffect.setProgress(100);
        }
    }

    class effectDivide extends AsyncTask<Void, Void, Void> {
        effectDivide() { }

        public void onPreExecute() {
            showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            listDivide.clear();
            listDivide.addAll(EffectCodeAsset.getListBitmapDivideEffect(ThumbnailUtils.extractThumbnail(binding.photoEditorView.getCurrentBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voids) {
            binding.recyclerViewDivide.setAdapter(new HardmixAdapter(listDivide, EditorActivity.this, getApplicationContext(), Arrays.asList(EffectCodeAsset.DIVIDE_EFFECTS)));
            binding.imageViewCompareEffect.setVisibility(View.VISIBLE);
            binding.seekbarEffect.setProgress(100);
        }
    }

    class effectHardmix extends AsyncTask<Void, Void, Void> {
        effectHardmix() { }

        public void onPreExecute() {
            showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            listHardmix.clear();
            listHardmix.addAll(EffectCodeAsset.getListBitmapHardmixEffect(ThumbnailUtils.extractThumbnail(binding.photoEditorView.getCurrentBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voids) {
            binding.recyclerViewHardmix.setAdapter(new HardmixAdapter(listHardmix, EditorActivity.this, getApplicationContext(), Arrays.asList(EffectCodeAsset.HARDMIX_EFFECTS)));
            binding.imageViewCompareEffect.setVisibility(View.VISIBLE);
            binding.seekbarEffect.setProgress(100);
        }
    }

    class effectOvarlay extends AsyncTask<Void, Void, Void> {
        effectOvarlay() { }

        public void onPreExecute() {
            showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            listOverlay.clear();
            listOverlay.addAll(EffectCodeAsset.getListBitmapOverlayEffect(ThumbnailUtils.extractThumbnail(binding.photoEditorView.getCurrentBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voids) {
            binding.recyclerViewOverlay.setAdapter(new HardmixAdapter(listOverlay, EditorActivity.this, getApplicationContext(), Arrays.asList(EffectCodeAsset.OVERLAY_EFFECTS)));
            binding.constraintLayoutEffect.setVisibility(View.VISIBLE);
            binding.constraintLayoutConfirmSaveHardmix.setVisibility(View.VISIBLE);
            binding.recyclerViewTools.setVisibility(View.GONE);
            binding.imageViewCompareEffect.setVisibility(View.VISIBLE);
            binding.seekbarEffect.setProgress(100);
            showLoading(false);

            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(binding.constraintLayoutRootView);
            constraintSet.connect(binding.relativeLayoutWrapperPhoto.getId(), 1, binding.constraintLayoutRootView.getId(), 1, 0);
            constraintSet.connect(binding.relativeLayoutWrapperPhoto.getId(), 4, binding.guidelinePaint.getId(), 3, 0);
            constraintSet.connect(binding.relativeLayoutWrapperPhoto.getId(), 2, binding.constraintLayoutRootView.getId(), 2, 0);
            constraintSet.applyTo(binding.constraintLayoutRootView);
        }
    }

    class effectBurn extends AsyncTask<Void, Void, Void> {
        effectBurn() { }

        public void onPreExecute() {
            showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            listBurn.clear();
            listBurn.addAll(EffectCodeAsset.getListBitmapColorEffect(ThumbnailUtils.extractThumbnail(binding.photoEditorView.getCurrentBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voids) {
            binding.recyclerViewBurn.setAdapter(new HardmixAdapter(listBurn, EditorActivity.this, getApplicationContext(), Arrays.asList(EffectCodeAsset.COLOR_EFFECTS)));
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
                SplashFragment.show(EditorActivity.this, list.get(0), null, list.get(1), EditorActivity.this, true);
            } else {
                SplashFragment.show(EditorActivity.this, list.get(0), list.get(1), null, EditorActivity.this, false);
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
                SplashBlurSquareFragment.show(EditorActivity.this, list.get(0), null, list.get(1), EditorActivity.this, true);
            } else {
                SplashBlurSquareFragment.show(EditorActivity.this, list.get(0), list.get(1), null, EditorActivity.this, false);
            }
            showLoading(false);
        }
    }

    class SaveFilter extends AsyncTask<Void, Void, Bitmap> {
        SaveFilter() {}

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
        SaveSticker() {}

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
        loadBitmapUri() {}

        public void onPreExecute() {
            showLoading(true);
        }

        public Bitmap doInBackground(String... string) {
            try {
                Uri fromFile = Uri.fromFile(new File(string[0]));
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fromFile);
                
                Bitmap bitmap1 = SystemUtil.rotateBitmap(bitmap, new ExifInterface(getContentResolver().openInputStream(fromFile)).getAttributeInt(ExifInterface.TAG_ORIENTATION, 1));
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
        SaveEditingBitmap() {}

        public void onPreExecute() {
            showLoading(true);
        }

        public String doInBackground(Void... voids) {
            try {
                return SaveFileUtils.saveBitmapFileEditor(EditorActivity.this, binding.photoEditorView.getCurrentBitmap(), new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date())).getAbsolutePath();
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
            Intent i = new Intent(EditorActivity.this, PhotoShareActivity.class);
            i.putExtra("path", string);
            startActivity(i);
        }

    }

    public void showLoading(boolean z) {
        if (z) {
            getWindow().setFlags(16, 16);
            binding.relativeLayoutLoading.setVisibility(View.VISIBLE);
            return;
        }
        getWindow().clearFlags(16);
        binding.relativeLayoutLoading.setVisibility(View.GONE);
    }
}
