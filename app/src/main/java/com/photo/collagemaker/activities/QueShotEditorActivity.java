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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.content.ContextCompat;
import androidx.core.internal.view.SupportMenu;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

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
import com.photo.collagemaker.queshot.QueShotText;
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
import com.photo.collagemaker.queshot.QueShotEditor;
import com.photo.collagemaker.queshot.QueShotView;
import com.photo.collagemaker.draw.Drawing;
import com.photo.collagemaker.picker.PermissionsUtils;
import com.photo.collagemaker.queshot.QueShotStickerIcons;
import com.photo.collagemaker.sticker.DrawableSticker;
import com.photo.collagemaker.sticker.Sticker;
import com.photo.collagemaker.queshot.QueShotStickerView;
import com.photo.collagemaker.queshot.QueShotTextView;
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
public class QueShotEditorActivity extends QueShotBaseActivity implements OnQuShotEditorListener,
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
    public QueShotEditor quShotEditor;
    public QueShotView quShotView;
    // BitmapStickerIcon
    QueShotStickerIcons quShotStickerIconClose;
    QueShotStickerIcons quShotStickerIconScale;
    QueShotStickerIcons quShotStickerIconFlip;
    QueShotStickerIcons quShotStickerIconRotate;
    QueShotStickerIcons quShotStickerIconEdit;
    QueShotStickerIcons quShotStickerIconAlign;

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

        recyclerViewSmoothFilter = findViewById(R.id.recycler_view_filter_smooth);
        recyclerViewLegacyFilter = findViewById(R.id.recycler_view_filter_legacy);
        binding.recyclerViewFilterBw.setVisibility(View.GONE);
        binding.recyclerViewFilterVintage.setVisibility(View.GONE);
        recyclerViewSmoothFilter.setVisibility(View.GONE);
        binding.recyclerViewFilterCold.setVisibility(View.GONE);
        binding.recyclerViewFilterWarm.setVisibility(View.GONE);
        recyclerViewLegacyFilter.setVisibility(View.GONE);
        viewAll = findViewById(R.id.view_all);
        viewCold = findViewById(R.id.view_cold);
        viewBW = findViewById(R.id.view_bw);
        viewVintage = findViewById(R.id.view_vintage);
        viewSmooth = findViewById(R.id.view_smooth);
        viewWarm = findViewById(R.id.view_warm);
        viewLegacy = findViewById(R.id.view_legacy);
        viewBW.setVisibility(View.INVISIBLE);
        viewCold.setVisibility(View.INVISIBLE);
        viewVintage.setVisibility(View.INVISIBLE);
        viewSmooth.setVisibility(View.INVISIBLE);
        viewWarm.setVisibility(View.INVISIBLE);
        viewLegacy.setVisibility(View.INVISIBLE);
        linearLayoutBurn = findViewById(R.id.linearLayoutBurn);
        linearLayoutDivide = findViewById(R.id.linearLayoutDivide);

        linearLayoutLegacy = findViewById(R.id.linearLayoutLegacy);
        textViewListAllFilter = findViewById(R.id.text_view_list_all);
        textViewListBwFilter = findViewById(R.id.text_view_list_bw);
        textViewListVintageFilter = findViewById(R.id.text_view_list_vintage);
        textViewListSmoothFilter = findViewById(R.id.text_view_list_smooth);
        textViewListColdFilter = findViewById(R.id.text_view_list_cold);
        textViewListWarmFilter = findViewById(R.id.text_view_list_warm);
        textViewListLegacyFilter = findViewById(R.id.text_view_list_legacy);
        recyclerViewDodge = findViewById(R.id.recycler_view_dodge);
        recyclerViewHardmix = findViewById(R.id.recycler_view_hardmix);
        recyclerViewDivide = findViewById(R.id.recycler_view_divide);
        recyclerViewDodge.setVisibility(View.GONE);
        recyclerViewDivide.setVisibility(View.GONE);
        recyclerViewHardmix.setVisibility(View.GONE);
        binding.recyclerViewBurn.setVisibility(View.GONE);
        recyclerViewAdjust = findViewById(R.id.recyclerViewAdjust);
        constraintLayoutAdjust = findViewById(R.id.constraintLayoutAdjust);
        constraintLayoutSticker = findViewById(R.id.constraint_layout_sticker);
        constraintLayoutStickerWomen = findViewById(R.id.constraint_layout_sticker_women);
        constraintLayoutStickerMen = findViewById(R.id.constraint_layout_sticker_men);
        viewPagerWomenSticker = findViewById(R.id.stickerWomenViewpaper);
        viewPagerStickers = findViewById(R.id.stickerViewpaper);
        seekbarStickerMen = findViewById(R.id.seekbarStickerMenAlpha);
        seekbarStickerMen.setVisibility(View.GONE);
        seekbarStickerWomen = findViewById(R.id.seekbarStickerWomenAlpha);
        seekbarStickerWomen.setVisibility(View.GONE);
        seekbarSticker = findViewById(R.id.seekbarStickerAlpha);
        seekbarSticker.setVisibility(View.GONE);
        constraintLayoutPaint = findViewById(R.id.constraintLayoutPaint);
        recyclerViewPaintListColor = findViewById(R.id.recyclerViewColorPaint);
        imageViewColor = findViewById(R.id.imageViewBrush);
        imageViewErasePaint = findViewById(R.id.image_view_erase);
        imageViewUndoPaint = findViewById(R.id.image_view_undo);
        imageViewUndoPaint.setVisibility(View.GONE);
        imageViewRedoPaint = findViewById(R.id.image_view_redo);
        imageViewRedoPaint.setVisibility(View.GONE);
        imageViewCleanPaint = findViewById(R.id.image_view_clean);
        imageViewCleanPaint.setVisibility(View.GONE);
        imageViewCleanNeon = findViewById(R.id.image_view_clean_neon);
        imageViewCleanNeon.setVisibility(View.GONE);
        binding.imageViewUndoNeon.setVisibility(View.GONE);
        binding.imageViewRedoNeon.setVisibility(View.GONE);
        seekbarBrushPaintSize = findViewById(R.id.seekbarBrushSize);
        seekbarEraseNeonSize = findViewById(R.id.seekbarEraseSize);
        imageViewNeon = findViewById(R.id.imageViewNeon);
        textViewSaveEditing = findViewById(R.id.text_view_save);
        image_view_exit = findViewById(R.id.image_view_exit);
        constraintLayoutSaveEditing = findViewById(R.id.constraintLayoutSaveEditing);
        constraintLayoutSave = findViewById(R.id.constraintLayoutSave);
        constraintLayoutSavePaint = findViewById(R.id.constraint_layout_confirm_save_paint);
        constraintLayoutDraw= findViewById(R.id.constraint_layout_draw);
        constraintLayoutEmoji= findViewById(R.id.constraint_layout_emoji);
        imageViewCompareAdjust = findViewById(R.id.imageViewCompareAdjust);
        imageViewCompareAdjust.setOnTouchListener(onTouchListener);
        imageViewCompareAdjust.setVisibility(View.GONE);
        binding.imageViewCompareFilter.setOnTouchListener(onTouchListener);
        binding.imageViewCompareFilter.setVisibility(View.GONE);
        binding.imageViewCompareEffect.setOnTouchListener(onTouchListener);
        binding.imageViewCompareEffect.setVisibility(View.GONE);
        binding.relativeLayoutAddText.setVisibility(View.GONE);
    }

    private void setOnBackPressDialog() {
        final Dialog dialogOnBackPressed = new Dialog(QueShotEditorActivity.this, R.style.UploadDialog);
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
        binding.recyclerViewEmoji.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        binding.recyclerViewEmoji.setAdapter(mEditingStickersToolsAdapter);
        binding.recyclerViewEmoji.setHasFixedSize(true);
        binding.recyclerViewFilterAll.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recyclerViewFilterAll.setHasFixedSize(true);
        binding.recyclerViewFilterBw.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recyclerViewFilterBw.setHasFixedSize(true);
        binding.recyclerViewFilterVintage.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recyclerViewFilterVintage.setHasFixedSize(true);
        recyclerViewSmoothFilter.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerViewSmoothFilter.setHasFixedSize(true);
        binding.recyclerViewFilterCold.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recyclerViewFilterCold.setHasFixedSize(true);
        binding.recyclerViewFilterWarm.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recyclerViewFilterWarm.setHasFixedSize(true);
        recyclerViewLegacyFilter.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerViewLegacyFilter.setHasFixedSize(true);
        recyclerViewDodge.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerViewDodge.setHasFixedSize(true);
        recyclerViewHardmix.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerViewHardmix.setHasFixedSize(true);
        binding.recyclerViewOverlay.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recyclerViewOverlay.setHasFixedSize(true);
        recyclerViewDivide.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerViewDivide.setHasFixedSize(true);
        binding.recyclerViewBurn.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recyclerViewBurn.setHasFixedSize(true);
        new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerViewAdjust.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerViewAdjust.setHasFixedSize(true);
        adjustAdapter = new AdjustAdapter(getApplicationContext(), this);
        recyclerViewAdjust.setAdapter(adjustAdapter);
        recyclerViewPaintListColor.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerViewPaintListColor.setHasFixedSize(true);
        recyclerViewPaintListColor.setAdapter(new ColorAdapter(getApplicationContext(), this));
        binding.recyclerViewColorNeon.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recyclerViewColorNeon.setHasFixedSize(true);
        binding.recyclerViewColorNeon.setAdapter(new ColorAdapter(getApplicationContext(), this));

        viewPagerWomenSticker.setAdapter(new PagerAdapter() {
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
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), WomenBeautyAssets.mListCrown(), i, QueShotEditorActivity.this));
                        break;
                    case 1:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), WomenBeautyAssets.mListSnsla(), i, QueShotEditorActivity.this));
                        break;
                    case 2:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), WomenBeautyAssets.mListHalat(), i, QueShotEditorActivity.this));
                        break;
                    case 3:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), WomenBeautyAssets.mListFlower(), i, QueShotEditorActivity.this));
                        break;
                    case 4:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), WomenBeautyAssets.mListGlass(), i, QueShotEditorActivity.this));
                        break;
                    case 5:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), WomenBeautyAssets.mListChap(), i, QueShotEditorActivity.this));
                        break;
                    case 6:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), WomenBeautyAssets.mListHairs(), i, QueShotEditorActivity.this));
                        break;
                    case 7:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), WomenBeautyAssets.mListSmile(), i, QueShotEditorActivity.this));
                        break;
                    case 8:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), WomenBeautyAssets.mListHjban(), i, QueShotEditorActivity.this));
                        break;
                    case 9:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), WomenBeautyAssets.mListChfer(), i, QueShotEditorActivity.this));
                        break;
                    case 10:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), WomenBeautyAssets.mListZwaq(), i, QueShotEditorActivity.this));
                        break;

                }

                viewGroup.addView(inflate);
                return inflate;
            }
        });
        RecyclerTabLayout recycler_tab_layout_women = findViewById(R.id.recycler_tab_layout_women);
        recycler_tab_layout_women.setUpWithAdapter(new WomenTabAdapter(viewPagerWomenSticker, getApplicationContext()));
        recycler_tab_layout_women.setPositionThreshold(0.5f);
        recycler_tab_layout_women.setBackgroundColor(ContextCompat.getColor(this, R.color.TabColor));

        viewPagerStickers.setAdapter(new PagerAdapter() {
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
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), StickersAsset.mListEmojy(), i, QueShotEditorActivity.this));
                        break;
                    case 1:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), StickersAsset.mListFlag(), i, QueShotEditorActivity.this));
                        break;
                    case 2:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), StickersAsset.mListBoom(), i, QueShotEditorActivity.this));
                        break;

                }

                viewGroup.addView(inflate);
                return inflate;
            }
        });
        RecyclerTabLayout recycler_tab_layout = findViewById(R.id.recycler_tab_layout);
        recycler_tab_layout.setUpWithAdapter(new StickerTabAdapter(viewPagerStickers, getApplicationContext()));
        recycler_tab_layout.setPositionThreshold(0.5f);
        recycler_tab_layout.setBackgroundColor(ContextCompat.getColor(this, R.color.TabColor));

        binding.stickerMenViewpaper.setAdapter(new PagerAdapter() {
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
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), MenBeautyAssets.mListhair(), i, QueShotEditorActivity.this));
                        break;
                    case 1:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), MenBeautyAssets.mListGlasses(), i, QueShotEditorActivity.this));
                        break;
                    case 2:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), MenBeautyAssets.mListMostach(), i, QueShotEditorActivity.this));
                        break;
                    case 3:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), MenBeautyAssets.mListLhya(), i, QueShotEditorActivity.this));
                        break;
                    case 4:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), MenBeautyAssets.mListScarf(), i, QueShotEditorActivity.this));
                        break;
                    case 5:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), MenBeautyAssets.mListTie(), i, QueShotEditorActivity.this));
                        break;
                    case 6:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), MenBeautyAssets.mListTatoo(), i, QueShotEditorActivity.this));
                        break;
                    case 7:
                        recycler_view_sticker.setAdapter(new StickerAdapter(getApplicationContext(), MenBeautyAssets.mListChain(), i, QueShotEditorActivity.this));
                        break;

                }

                viewGroup.addView(inflate);
                return inflate;
            }
        });
        RecyclerTabLayout recycler_tab_layout_men = findViewById(R.id.recycler_tab_layout_men);
        recycler_tab_layout_men.setUpWithAdapter(new MenTabAdapter(binding.stickerMenViewpaper, getApplicationContext()));
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
        quShotEditor = new QueShotEditor.Builder(this, quShotView).setPinchTextScalable(true).build();
        quShotEditor.setOnPhotoEditorListener(this);

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
        textViewSaveEditing.setOnClickListener(view -> {
            if (PermissionsUtils.checkWriteStoragePermission(QueShotEditorActivity.this)) {
                new SaveEditingBitmap().execute();
            }
        });
        image_view_exit.setOnClickListener(view -> onBackPressed());
        imageViewErasePaint.setOnClickListener(view -> setImageErasePaint());
        imageViewColor.setOnClickListener(view -> setColorPaint());
        seekbarEraseNeonSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                quShotEditor.setBrushEraserSize((float) i);
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                quShotEditor.brushEraser();
            }
        });
        seekbarBrushPaintSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                quShotEditor.setBrushSize((float) (i + 10));
            }
        });
        binding.imageViewEraseNeon.setOnClickListener(view -> setImageEraseNeon());
        imageViewNeon.setOnClickListener(view -> setColorNeon());
        binding.seekbarEraseSizeNeon.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                quShotEditor.setBrushEraserSize((float) i);
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                quShotEditor.brushEraser();
            }
        });
        binding.seekbarBrushSizeNeon.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                quShotEditor.setBrushSize((float) (i + 10));
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
        seekbarSticker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                Sticker currentSticker = quShotView.getCurrentSticker();
                if (currentSticker != null) {
                    currentSticker.setAlpha(i);
                }
            }
        });
        seekbarStickerMen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                Sticker currentSticker = quShotView.getCurrentSticker();
                if (currentSticker != null) {
                    currentSticker.setAlpha(i);
                }
            }
        });
        seekbarStickerWomen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                Sticker currentSticker = quShotView.getCurrentSticker();
                if (currentSticker != null) {
                    currentSticker.setAlpha(i);
                }
            }
        });

        binding.imageViewAddStickerWomen = findViewById(R.id.binding.imageViewAddStickerWomen);
        binding.imageViewAddStickerWomen.setVisibility(View.GONE);
        binding.imageViewAddStickerWomen.setOnClickListener(view -> {
            binding.imageViewAddStickerWomen.setVisibility(View.GONE);
            binding.linearLayoutWrapperStickerWomenList.setVisibility(View.VISIBLE);
        });


        binding.imageViewAddStickerMen.setOnClickListener(view -> {
            binding.imageViewAddStickerMen.setVisibility(View.GONE);
            binding.imageViewAddStickerMen = findViewById(R.id.binding.imageViewAddStickerMen);
            binding.linearLayoutWrapperStickerMenList.setVisibility(View.VISIBLE);
            binding.imageViewAddStickerMen.setVisibility(View.GONE);
        });
        imageViewAddSticker = findViewById(R.id.imageViewAddSticker);
        binding.imageViewAddSticker.setVisibility(View.GONE);
        binding.imageViewAddSticker.setOnClickListener(view -> {
            binding.imageViewAddSticker.setVisibility(View.GONE);
            binding.linearLayoutWrapperStickerList.setVisibility(View.VISIBLE);
        });

        binding.relativeLayoutAddText.setOnClickListener(view -> {
            quShotView.setHandlingSticker(null);
            textFragment();
        });
        binding.seekbarAdjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                adjustAdapter.getCurrentAdjustModel().setSeekBarIntensity(quShotEditor, ((float) i) / ((float) seekBar.getMax()), true);
            }
        });
        quShotStickerIconClose = new QueShotStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_close), 0, QueShotStickerIcons.DELETE);
        quShotStickerIconClose.setIconEvent(new DeleteIconEvent());
        quShotStickerIconScale = new QueShotStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_scale), 3, QueShotStickerIcons.SCALE);
        quShotStickerIconScale.setIconEvent(new ZoomIconEvent());
        quShotStickerIconFlip = new QueShotStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_flip), 1, QueShotStickerIcons.FLIP);
        quShotStickerIconFlip.setIconEvent(new FlipHorizontallyEvent());
        quShotStickerIconRotate = new QueShotStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_rotate), 3, QueShotStickerIcons.ROTATE);
        quShotStickerIconRotate.setIconEvent(new ZoomIconEvent());
        quShotStickerIconEdit = new QueShotStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_edit), 1, QueShotStickerIcons.EDIT);
        quShotStickerIconEdit.setIconEvent(new EditTextIconEvent());
        quShotStickerIconAlign = new QueShotStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_center), 2, QueShotStickerIcons.ALIGN);
        quShotStickerIconAlign.setIconEvent(new AlignHorizontallyEvent());
        quShotView.setIcons(Arrays.asList(quShotStickerIconClose, quShotStickerIconScale,
                quShotStickerIconFlip, quShotStickerIconEdit, quShotStickerIconRotate, quShotStickerIconAlign));
        quShotView.setBackgroundColor(-16777216);
        quShotView.setLocked(false);
        quShotView.setConstrained(true);
        quShotView.setOnStickerOperationListener(new QueShotStickerView.OnStickerOperationListener() {
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
                seekbarSticker.setVisibility(View.VISIBLE);
                seekbarSticker.setProgress(sticker.getAlpha());
                seekbarStickerMen.setVisibility(View.VISIBLE);
                seekbarStickerMen.setProgress(sticker.getAlpha());
                seekbarStickerWomen.setVisibility(View.VISIBLE);
                seekbarStickerWomen.setProgress(sticker.getAlpha());
            }

            public void onStickerSelected(@NonNull Sticker sticker) {
                if (sticker instanceof QueShotTextView) {
                    ((QueShotTextView) sticker).setTextColor(SupportMenu.CATEGORY_MASK);
                    quShotView.replace(sticker);
                    quShotView.invalidate();
                }
                seekbarSticker.setVisibility(View.VISIBLE);
                seekbarSticker.setProgress(sticker.getAlpha());
                seekbarStickerMen.setVisibility(View.VISIBLE);
                seekbarStickerMen.setProgress(sticker.getAlpha());
                seekbarStickerWomen.setVisibility(View.VISIBLE);
                seekbarStickerWomen.setProgress(sticker.getAlpha());
            }

            public void onStickerDeleted(@NonNull Sticker sticker) {
                seekbarSticker.setVisibility(View.GONE);
                seekbarStickerMen.setVisibility(View.GONE);
                seekbarStickerWomen.setVisibility(View.GONE);
            }

            public void onStickerTouchOutside() {
                seekbarSticker.setVisibility(View.GONE);
                seekbarStickerMen.setVisibility(View.GONE);
                seekbarStickerWomen.setVisibility(View.GONE);
            }

            public void onStickerDoubleTap(@NonNull Sticker sticker) {
                if (sticker instanceof QueShotTextView) {
                    sticker.setShow(false);
                    quShotView.setHandlingSticker( (Sticker) null);
                    addTextFragment = TextFragment.show(QueShotEditorActivity.this, ((QueShotTextView) sticker).getQuShotText());
                    textEditor = new TextFragment.TextEditor() {
                        public void onDone(QueShotText addTextProperties) {
                            quShotView.getStickers().remove(quShotView.getLastHandlingSticker());
                            quShotView.addSticker(new QueShotTextView(QueShotEditorActivity.this, addTextProperties));
                        }

                        public void onBackButton() {
                            quShotView.showLastHandlingSticker();
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
                quShotView.setFilterIntensity(((float) i) / 100.0f);
            }
        });
        binding.seekbarEffect.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                quShotView.setFilterIntensity(((float) i) / 100.0f);
            }
        });
      }

    public void setOverlayEffect() {
        binding.recyclerViewOverlay.setVisibility(View.VISIBLE);
        recyclerViewHardmix.setVisibility(View.GONE);
        recyclerViewDodge.setVisibility(View.GONE);
        recyclerViewDivide.setVisibility(View.GONE);
        binding.recyclerViewBurn.setVisibility(View.GONE);
        binding.textViewListOverlay.setTextColor(ContextCompat.getColor(this, R.color.white));
        binding.textViewListHardmix.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListDodge.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListDivide.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListBurn.setTextColor(ContextCompat.getColor(this, R.color.grayText));
    }

    public void setHardmixEffect() {
        recyclerViewHardmix.setVisibility(View.VISIBLE);
        recyclerViewDodge.setVisibility(View.GONE);
        binding.recyclerViewOverlay.setVisibility(View.GONE);
        recyclerViewDivide.setVisibility(View.GONE);
        binding.recyclerViewBurn.setVisibility(View.GONE);
        binding.textViewListOverlay.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListHardmix.setTextColor(ContextCompat.getColor(this, R.color.white));
        binding.textViewListDodge.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListDivide.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListBurn.setTextColor(ContextCompat.getColor(this, R.color.grayText));
    }

    public void setDodgeEffect() {
        recyclerViewHardmix.setVisibility(View.GONE);
        recyclerViewDodge.setVisibility(View.VISIBLE);
        recyclerViewDivide.setVisibility(View.GONE);
        binding.recyclerViewOverlay.setVisibility(View.GONE);
        binding.recyclerViewBurn.setVisibility(View.GONE);
        binding.textViewListOverlay.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListHardmix.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListDodge.setTextColor(ContextCompat.getColor(this, R.color.white));
        binding.textViewListDivide.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListBurn.setTextColor(ContextCompat.getColor(this, R.color.grayText));
    }

    public void setDivideEffect() {
        recyclerViewHardmix.setVisibility(View.GONE);
        recyclerViewDodge.setVisibility(View.GONE);
        binding.recyclerViewOverlay.setVisibility(View.GONE);
        recyclerViewDivide.setVisibility(View.VISIBLE);
        binding.recyclerViewBurn.setVisibility(View.GONE);
        binding.textViewListHardmix.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListDodge.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListDivide.setTextColor(ContextCompat.getColor(this, R.color.white));
        binding.textViewListBurn.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        binding.textViewListOverlay.setTextColor(ContextCompat.getColor(this, R.color.grayText));
    }

    public void setBurnEffect() {
        recyclerViewHardmix.setVisibility(View.GONE);
        recyclerViewDodge.setVisibility(View.GONE);
        binding.recyclerViewOverlay.setVisibility(View.GONE);
        recyclerViewDivide.setVisibility(View.GONE);
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
        recyclerViewSmoothFilter.setVisibility(View.GONE);
        binding.recyclerViewFilterCold.setVisibility(View.GONE);
        binding.recyclerViewFilterWarm.setVisibility(View.GONE);
        recyclerViewLegacyFilter.setVisibility(View.GONE);
        textViewListAllFilter.setTextColor(ContextCompat.getColor(this, R.color.white));
        textViewListColdFilter.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        textViewListBwFilter.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        textViewListVintageFilter.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        textViewListSmoothFilter.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        textViewListWarmFilter.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        textViewListLegacyFilter.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        viewAll.setVisibility(View.VISIBLE);
        viewCold.setVisibility(View.INVISIBLE);
        viewBW.setVisibility(View.INVISIBLE);
        viewVintage.setVisibility(View.INVISIBLE);
        viewSmooth.setVisibility(View.INVISIBLE);
        viewWarm.setVisibility(View.INVISIBLE);
        viewLegacy.setVisibility(View.INVISIBLE);
    }

    public void setBwFilter() {
        binding.recyclerViewFilterAll.setVisibility(View.GONE);
        binding.recyclerViewFilterBw.setVisibility(View.VISIBLE);
        binding.recyclerViewFilterVintage.setVisibility(View.GONE);
        recyclerViewSmoothFilter.setVisibility(View.GONE);
        binding.recyclerViewFilterCold.setVisibility(View.GONE);
        binding.recyclerViewFilterWarm.setVisibility(View.GONE);
        recyclerViewLegacyFilter.setVisibility(View.GONE);
        textViewListAllFilter.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        textViewListColdFilter.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        textViewListBwFilter.setTextColor(ContextCompat.getColor(this, R.color.white));
        textViewListVintageFilter.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        textViewListSmoothFilter.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        textViewListWarmFilter.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        textViewListLegacyFilter.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        viewAll.setVisibility(View.INVISIBLE);
        viewCold.setVisibility(View.INVISIBLE);
        viewBW.setVisibility(View.VISIBLE);
        viewVintage.setVisibility(View.INVISIBLE);
        viewSmooth.setVisibility(View.INVISIBLE);
        viewWarm.setVisibility(View.INVISIBLE);
        viewLegacy.setVisibility(View.INVISIBLE);
    }

    public void setVintageFilter() {
        binding.recyclerViewFilterAll.setVisibility(View.GONE);
        binding.recyclerViewFilterBw.setVisibility(View.GONE);
        binding.recyclerViewFilterVintage.setVisibility(View.VISIBLE);
        recyclerViewSmoothFilter.setVisibility(View.GONE);
        binding.recyclerViewFilterCold.setVisibility(View.GONE);
        binding.recyclerViewFilterWarm.setVisibility(View.GONE);
        recyclerViewLegacyFilter.setVisibility(View.GONE);
        textViewListAllFilter.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        textViewListColdFilter.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        textViewListBwFilter.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        textViewListVintageFilter.setTextColor(ContextCompat.getColor(this, R.color.white));
        textViewListSmoothFilter.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        textViewListWarmFilter.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        textViewListLegacyFilter.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        viewAll.setVisibility(View.INVISIBLE);
        viewCold.setVisibility(View.INVISIBLE);
        viewBW.setVisibility(View.INVISIBLE);
        viewVintage.setVisibility(View.VISIBLE);
        viewSmooth.setVisibility(View.INVISIBLE);
        viewWarm.setVisibility(View.INVISIBLE);
        viewLegacy.setVisibility(View.INVISIBLE);
    }

    public void setSmoothFilter() {
        binding.recyclerViewFilterAll.setVisibility(View.GONE);
        binding.recyclerViewFilterBw.setVisibility(View.GONE);
        binding.recyclerViewFilterVintage.setVisibility(View.GONE);
        recyclerViewSmoothFilter.setVisibility(View.VISIBLE);
        binding.recyclerViewFilterCold.setVisibility(View.GONE);
        binding.recyclerViewFilterWarm.setVisibility(View.GONE);
        recyclerViewLegacyFilter.setVisibility(View.GONE);
        textViewListAllFilter.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        textViewListColdFilter.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        textViewListBwFilter.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        textViewListVintageFilter.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        textViewListSmoothFilter.setTextColor(ContextCompat.getColor(this, R.color.white));
        textViewListWarmFilter.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        textViewListLegacyFilter.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        viewAll.setVisibility(View.INVISIBLE);
        viewCold.setVisibility(View.INVISIBLE);
        viewBW.setVisibility(View.INVISIBLE);
        viewVintage.setVisibility(View.INVISIBLE);
        viewSmooth.setVisibility(View.VISIBLE);
        viewWarm.setVisibility(View.INVISIBLE);
        viewLegacy.setVisibility(View.INVISIBLE);
    }

    public void setColdFilter() {
        binding.recyclerViewFilterAll.setVisibility(View.GONE);
        binding.recyclerViewFilterBw.setVisibility(View.GONE);
        binding.recyclerViewFilterVintage.setVisibility(View.GONE);
        recyclerViewSmoothFilter.setVisibility(View.GONE);
        binding.recyclerViewFilterCold.setVisibility(View.VISIBLE);
        binding.recyclerViewFilterWarm.setVisibility(View.GONE);
        recyclerViewLegacyFilter.setVisibility(View.GONE);
        textViewListAllFilter.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        textViewListColdFilter.setTextColor(ContextCompat.getColor(this, R.color.white));
        textViewListBwFilter.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        textViewListVintageFilter.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        textViewListSmoothFilter.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        textViewListWarmFilter.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        textViewListLegacyFilter.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        viewAll.setVisibility(View.INVISIBLE);
        viewCold.setVisibility(View.VISIBLE);
        viewBW.setVisibility(View.INVISIBLE);
        viewVintage.setVisibility(View.INVISIBLE);
        viewSmooth.setVisibility(View.INVISIBLE);
        viewWarm.setVisibility(View.INVISIBLE);
        viewLegacy.setVisibility(View.INVISIBLE);
    }

    public void setWarmFilter() {
        binding.recyclerViewFilterAll.setVisibility(View.GONE);
        binding.recyclerViewFilterBw.setVisibility(View.GONE);
        binding.recyclerViewFilterVintage.setVisibility(View.GONE);
        recyclerViewSmoothFilter.setVisibility(View.GONE);
        binding.recyclerViewFilterCold.setVisibility(View.GONE);
        binding.recyclerViewFilterWarm.setVisibility(View.VISIBLE);
        recyclerViewLegacyFilter.setVisibility(View.GONE);
        textViewListAllFilter.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        textViewListColdFilter.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        textViewListBwFilter.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        textViewListVintageFilter.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        textViewListSmoothFilter.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        textViewListWarmFilter.setTextColor(ContextCompat.getColor(this, R.color.white));
        textViewListLegacyFilter.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        viewAll.setVisibility(View.INVISIBLE);
        viewCold.setVisibility(View.INVISIBLE);
        viewBW.setVisibility(View.INVISIBLE);
        viewVintage.setVisibility(View.INVISIBLE);
        viewSmooth.setVisibility(View.INVISIBLE);
        viewWarm.setVisibility(View.VISIBLE);
        viewLegacy.setVisibility(View.INVISIBLE);
    }

    public void setLegacyFilter() {
        binding.recyclerViewFilterAll.setVisibility(View.GONE);
        binding.recyclerViewFilterBw.setVisibility(View.GONE);
        binding.recyclerViewFilterVintage.setVisibility(View.GONE);
        recyclerViewSmoothFilter.setVisibility(View.GONE);
        binding.recyclerViewFilterCold.setVisibility(View.GONE);
        binding.recyclerViewFilterWarm.setVisibility(View.GONE);
        recyclerViewLegacyFilter.setVisibility(View.VISIBLE);
        textViewListAllFilter.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        textViewListColdFilter.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        textViewListBwFilter.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        textViewListVintageFilter.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        textViewListSmoothFilter.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        textViewListWarmFilter.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        textViewListLegacyFilter.setTextColor(ContextCompat.getColor(this, R.color.white));
        viewAll.setVisibility(View.INVISIBLE);
        viewCold.setVisibility(View.INVISIBLE);
        viewBW.setVisibility(View.INVISIBLE);
        viewVintage.setVisibility(View.INVISIBLE);
        viewSmooth.setVisibility(View.INVISIBLE);
        viewWarm.setVisibility(View.INVISIBLE);
        viewLegacy.setVisibility(View.VISIBLE);
    }

    private void setBottomToolbar(boolean z) {
        int mVisibility = !z ? View.GONE : View.VISIBLE;
        imageViewColor.setVisibility(mVisibility);
        imageViewErasePaint.setVisibility(mVisibility);
        imageViewUndoPaint.setVisibility(mVisibility);
        imageViewRedoPaint.setVisibility(mVisibility);
        imageViewCleanPaint.setVisibility(mVisibility);
        imageViewCleanNeon.setVisibility(mVisibility);
        imageViewNeon.setVisibility(mVisibility);
        binding.imageViewEraseNeon.setVisibility(mVisibility);
        binding.imageViewUndoNeon.setVisibility(mVisibility);
        binding.imageViewRedoNeon.setVisibility(mVisibility);
    }

    public void setImageErasePaint() {
        seekbarBrushPaintSize.setVisibility(View.GONE);
        recyclerViewPaintListColor.setVisibility(View.GONE);
        seekbarEraseNeonSize.setVisibility(View.VISIBLE);
        imageViewErasePaint.setImageResource(R.drawable.ic_erase_selected);
        quShotEditor.brushEraser();
        seekbarEraseNeonSize.setProgress(20);
    }

    public void setImageEraseNeon() {
        binding.seekbarBrushSizeNeon.setVisibility(View.GONE);
        binding.recyclerViewColorNeon.setVisibility(View.GONE);
        binding.seekbarEraseSizeNeon.setVisibility(View.VISIBLE);
        binding.imageViewEraseNeon.setImageResource(R.drawable.ic_erase_selected);
        quShotEditor.brushEraser();
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
        quShotEditor.setBrushMode(2);
        quShotEditor.setBrushDrawingMode(true);
        binding.seekbarBrushSizeNeon.setProgress(20);
    }

    public void setColorPaint() {
        seekbarBrushPaintSize.setVisibility(View.VISIBLE);
        recyclerViewPaintListColor.setVisibility(View.VISIBLE);
        recyclerViewPaintListColor.scrollToPosition(0);
        colorAdapter = (ColorAdapter) recyclerViewPaintListColor.getAdapter();
        if (colorAdapter != null) {
            colorAdapter.setSelectedColorIndex(0);
        }
        if (colorAdapter != null) {
            colorAdapter.notifyDataSetChanged();
        }
        seekbarEraseNeonSize.setVisibility(View.GONE);
        imageViewErasePaint.setImageResource(R.drawable.ic_erase);
        quShotEditor.setBrushMode(1);
        quShotEditor.setBrushDrawingMode(true);
        seekbarBrushPaintSize.setProgress(20);
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
                quShotView.getGLSurfaceView().setAlpha(0.0f);
                return true;
            case 1:
                quShotView.getGLSurfaceView().setAlpha(1.0f);
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
            imageViewCompareAdjust.setVisibility(View.GONE);
            constraintLayoutAdjust.setVisibility(View.GONE);
            binding.recyclerViewTools.setVisibility(View.VISIBLE);
            constraintLayoutSave.setVisibility(View.VISIBLE);
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
                quShotEditor.setBrushDrawingMode(false);
                imageViewUndoPaint.setVisibility(View.GONE);
                imageViewRedoPaint.setVisibility(View.GONE);
                imageViewCleanPaint.setVisibility(View.GONE);
                imageViewErasePaint.setVisibility(View.GONE);
                binding.recyclerViewTools.setVisibility(View.VISIBLE);
                constraintLayoutSavePaint.setVisibility(View.GONE);
                constraintLayoutPaint.setVisibility(View.GONE);
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(binding.constraintLayoutRootView);
                constraintSet.connect(binding.relativeLayoutWrapperPhoto.getId(), 1, binding.constraintLayoutRootView.getId(), 1, 0);
                constraintSet.connect(binding.relativeLayoutWrapperPhoto.getId(), 4, binding.guideline.getId(), 3, 0);
                constraintSet.connect(binding.relativeLayoutWrapperPhoto.getId(), 2, binding.constraintLayoutRootView.getId(), 2, 0);
                constraintSet.applyTo(binding.constraintLayoutRootView);
                quShotView.setImageSource(quShotEditor.getBrushDrawingView().getDrawBitmap(quShotView.getCurrentBitmap()));
                quShotEditor.clearBrushAllViews();
                showLoading(false);
                reloadingLayout();
            });
            setVisibleSave();
            moduleToolsId = Module.NONE;
            
        } else if (id == R.id.image_view_save_neon) {
            showLoading(true);
            runOnUiThread(() -> {
                quShotEditor.setBrushDrawingMode(false);
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
                quShotView.setImageSource(quShotEditor.getBrushDrawingView().getDrawBitmap(quShotView.getCurrentBitmap()));
                quShotEditor.clearBrushAllViews();
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
            quShotView.setHandlingSticker(null);
            quShotView.setLocked(true);
            seekbarSticker.setVisibility(View.GONE);
            binding.imageViewAddSticker.setVisibility(View.GONE);
            binding.recyclerViewTools.setVisibility(View.VISIBLE);
            if (!quShotView.getStickers().isEmpty()) {
                new SaveSticker().execute();
            }
            binding.linearLayoutWrapperStickerList.setVisibility(View.VISIBLE);
            constraintLayoutSticker.setVisibility(View.GONE);
            setVisibleSave();
            moduleToolsId = Module.NONE;
            
        } else if (id == R.id.image_view_save_sticker_men) {
            quShotView.setHandlingSticker(null);
            quShotView.setLocked(true);
            seekbarStickerMen.setVisibility(View.GONE);
            binding.recyclerViewTools.setVisibility(View.VISIBLE);
            binding.imageViewAddStickerMen.setVisibility(View.GONE);
            if (!quShotView.getStickers().isEmpty()) {
                new SaveSticker().execute();
            }
            binding.linearLayoutWrapperStickerMenList.setVisibility(View.VISIBLE);
            constraintLayoutStickerMen.setVisibility(View.GONE);
            setVisibleSave();
            moduleToolsId = Module.NONE;
            
        } else if (id == R.id.image_view_save_sticker_women) {
            quShotView.setHandlingSticker(null);
            quShotView.setLocked(true);
            seekbarStickerWomen.setVisibility(View.GONE);
            binding.imageViewAddStickerWomen.setVisibility(View.GONE);
            binding.recyclerViewTools.setVisibility(View.VISIBLE);
            if (!quShotView.getStickers().isEmpty()) {
                new SaveSticker().execute();
            }
            binding.linearLayoutWrapperStickerWomenList.setVisibility(View.VISIBLE);
            constraintLayoutStickerWomen.setVisibility(View.GONE);
            setVisibleSave();
            moduleToolsId = Module.NONE;
            
        } else if (id == R.id.imageViewSaveText) {
            quShotView.setHandlingSticker(null);
            quShotView.setLocked(true);
            binding.constraintLayoutConfirmText.setVisibility(View.GONE);
            binding.relativeLayoutAddText.setVisibility(View.GONE);
            if (!quShotView.getStickers().isEmpty()) {
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
            quShotEditor.redoBrush();
        } else if (id == R.id.image_view_undo_neon || id == R.id.image_view_undo) {
            quShotEditor.undoBrush();
        } else if (id == R.id.image_view_clean_neon || id == R.id.image_view_clean) {
            quShotEditor.clearBrushAllViews();
        }
    }

    public void isPermissionGranted(boolean z, String string) {
        if (z) {
            new SaveEditingBitmap().execute();
        }
    }

    public void onFilterSelected(String string) {
        quShotEditor.setFilterEffect(string);
        binding.seekbarFilter.setProgress(50);
        binding.seekbarEffect.setProgress(50);
        if (moduleToolsId == Module.FILTER) {
            quShotView.getGLSurfaceView().setFilterIntensity(0.5f);
        }
        if (moduleToolsId == Module.OVERLAY) {
            quShotView.getGLSurfaceView().setFilterIntensity(0.5f);
        }

    }

    public void textFragment() {
        addTextFragment = TextFragment.show(this);
        textEditor = new TextFragment.TextEditor() {
            public void onDone(QueShotText addTextProperties) {
                quShotView.addSticker(new QueShotTextView(getApplicationContext(), addTextProperties));
            }

            public void onBackButton() {
                if (quShotView.getStickers().isEmpty()) {
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
                quShotView.setLocked(false);
                textFragment();
                binding.recyclerViewTools.setVisibility(View.GONE);
                constraintLayoutDraw.setVisibility(View.GONE);
                constraintLayoutEmoji.setVisibility(View.GONE);
                quShotView.setHandlingSticker(null);
                quShotView.getStickers().clear();
                quShotEditor.setFilterEffect("");
                quShotEditor.clearBrushAllViews();
                quShotEditor.setBrushDrawingMode(false);
                quShotView.setHandlingSticker(null);
                quShotView.getStickers().clear();
                quShotEditor.setFilterEffect("");
                quShotEditor.clearBrushAllViews();
                quShotEditor.setBrushDrawingMode(false);
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
                imageViewCompareAdjust.setVisibility(View.VISIBLE);
                constraintLayoutDraw.setVisibility(View.GONE);
                constraintLayoutEmoji.setVisibility(View.GONE);
                constraintLayoutAdjust.setVisibility(View.VISIBLE);
                binding.constraintLayoutNeon.setVisibility(View.GONE);
                binding.constraintLayoutFilter.setVisibility(View.GONE);
                constraintLayoutPaint.setVisibility(View.GONE);
                if (!quShotView.getStickers().isEmpty()) {
                    quShotView.getStickers().clear();
                    quShotView.setHandlingSticker(null);
                }

                ConstraintSet constraintsetAdjust = new ConstraintSet();
                constraintsetAdjust.clone(binding.constraintLayoutRootView);
                constraintsetAdjust.connect(binding.relativeLayoutWrapperPhoto.getId(), 1, binding.constraintLayoutRootView.getId(), 1, 0);
                constraintsetAdjust.connect(binding.relativeLayoutWrapperPhoto.getId(), 4, binding.guidelinePaint.getId(), 3, 0);
                constraintsetAdjust.connect(binding.relativeLayoutWrapperPhoto.getId(), 2, binding.constraintLayoutRootView.getId(), 2, 0);
                constraintsetAdjust.applyTo(binding.constraintLayoutRootView);
                binding.recyclerViewTools.setVisibility(View.GONE);
                constraintLayoutSave.setVisibility(View.GONE);
                binding.relativeLayoutAddText.setVisibility(View.GONE);
                binding.constraintLayoutConfirmText.setVisibility(View.GONE);
                quShotEditor.clearBrushAllViews();
                quShotEditor.setBrushDrawingMode(false);
                adjustAdapter = new AdjustAdapter(getApplicationContext(), this);
                recyclerViewAdjust.setAdapter(adjustAdapter);
                adjustAdapter.setSelectedAdjust(0);
                quShotEditor.setAdjustFilter(adjustAdapter.getFilterConfig());
                break;
            case FILTER:
                setGoneSave();
                if (!quShotView.getStickers().isEmpty()) {
                    quShotView.getStickers().clear();
                    quShotView.setHandlingSticker(null);
                }
                binding.relativeLayoutAddText.setVisibility(View.GONE);
                binding.constraintLayoutConfirmText.setVisibility(View.GONE);
                constraintLayoutDraw.setVisibility(View.GONE);
                constraintLayoutEmoji.setVisibility(View.GONE);
                new allFilters().execute();
                new bwFilters().execute();
                new vintageFilters().execute();
                new smoothFilters().execute();
                new coldFilters().execute();
                new warmFilters().execute();
                new legacyFilters().execute();
                break;
            case EMOJI:
                constraintLayoutEmoji.setVisibility(View.VISIBLE);
                constraintLayoutDraw.setVisibility(View.GONE);
                break;
            case DRAW:
                constraintLayoutDraw.setVisibility(View.VISIBLE);
                constraintLayoutEmoji.setVisibility(View.GONE);
                break;
            case OVERLAY:
                setGoneSave();
                new effectOvarlay().execute();
                new effectHardmix().execute();
                new effectDodge().execute();
                new effectBurn().execute();
                new effectDivide().execute();
                constraintLayoutDraw.setVisibility(View.GONE);
                constraintLayoutEmoji.setVisibility(View.GONE);
                break;
            case RATIO:
                new openBlurFragment().execute();
                constraintLayoutDraw.setVisibility(View.GONE);
                constraintLayoutEmoji.setVisibility(View.GONE);
                goneLayout();
                break;
            case BACKGROUND:
                new openFrameFragment().execute();
                constraintLayoutDraw.setVisibility(View.GONE);
                constraintLayoutEmoji.setVisibility(View.GONE);
                goneLayout();
                break;
            case SPLASH:
                new openSplashBrushFragment(true).execute();
                constraintLayoutDraw.setVisibility(View.GONE);
                constraintLayoutEmoji.setVisibility(View.GONE);
                break;
            case SQUARESPLASH:
                new openSplashSquareFragment(true).execute();
                constraintLayoutDraw.setVisibility(View.GONE);
                constraintLayoutEmoji.setVisibility(View.GONE);
                break;
            case BLUR:
                new openSplashBrushFragment(false).execute();
                constraintLayoutDraw.setVisibility(View.GONE);
                constraintLayoutEmoji.setVisibility(View.GONE);
                break;
            case SQUAEBLUR:
                new openSplashSquareFragment(false).execute();
                constraintLayoutDraw.setVisibility(View.GONE);
                constraintLayoutEmoji.setVisibility(View.GONE);
                break;
            case COLOR:
                ColorSplashFragment.show(this, quShotView.getCurrentBitmap());
                constraintLayoutDraw.setVisibility(View.GONE);
                constraintLayoutEmoji.setVisibility(View.GONE);
                goneLayout();
                break;
            case CROP:
                CropFragment.show(this, this, quShotView.getCurrentBitmap());
                constraintLayoutDraw.setVisibility(View.GONE);
                constraintLayoutEmoji.setVisibility(View.GONE);
                goneLayout();
                break;
            case ROTATE:
                RotateFragment.show(this, this, quShotView.getCurrentBitmap());
                constraintLayoutDraw.setVisibility(View.GONE);
                constraintLayoutEmoji.setVisibility(View.GONE);
                goneLayout();
                break;
        }
        quShotView.setHandlingSticker(null);
    }

    public void onQuShotStickerToolSelected(Module module) {
        moduleToolsId = module;
        ConstraintSet constraintSet;
        switch (module) {
            case STICKER:
                setGoneSave();
                quShotView.setLocked(false);
                constraintLayoutSticker.setVisibility(View.VISIBLE);
                constraintLayoutAdjust.setVisibility(View.GONE);
                constraintLayoutEmoji.setVisibility(View.GONE);
                binding.constraintLayoutNeon.setVisibility(View.GONE);
                binding.constraintLayoutFilter.setVisibility(View.GONE);
                binding.linearLayoutWrapperStickerList.setVisibility(View.VISIBLE);
                quShotEditor.setFilterEffect("");
                quShotEditor.clearBrushAllViews();
                quShotEditor.setBrushDrawingMode(false);
                if (!quShotView.getStickers().isEmpty()) {
                    quShotView.getStickers().clear();
                    quShotView.setHandlingSticker(null);
                }
                binding.relativeLayoutAddText.setVisibility(View.GONE);
                binding.constraintLayoutConfirmText.setVisibility(View.GONE);
                break;
            case MACKUER:
                setGoneSave();
                quShotView.setLocked(false);
                constraintLayoutStickerMen.setVisibility(View.VISIBLE);
                constraintLayoutAdjust.setVisibility(View.GONE);
                constraintLayoutEmoji.setVisibility(View.GONE);
                binding.constraintLayoutNeon.setVisibility(View.GONE);
                binding.constraintLayoutFilter.setVisibility(View.GONE);
                binding.linearLayoutWrapperStickerMenList.setVisibility(View.VISIBLE);
                quShotEditor.setFilterEffect("");
                quShotEditor.clearBrushAllViews();
                quShotEditor.setBrushDrawingMode(false);
                if (!quShotView.getStickers().isEmpty()) {
                    quShotView.getStickers().clear();
                    quShotView.setHandlingSticker(null);
                }
                binding.relativeLayoutAddText.setVisibility(View.GONE);
                binding.constraintLayoutConfirmText.setVisibility(View.GONE);
                break;
            case BEAUTY:
                setGoneSave();
                quShotView.setLocked(false);
                constraintLayoutStickerWomen.setVisibility(View.VISIBLE);
                binding.linearLayoutWrapperStickerWomenList.setVisibility(View.VISIBLE);
                constraintLayoutAdjust.setVisibility(View.GONE);
                constraintLayoutEmoji.setVisibility(View.GONE);
                binding.constraintLayoutNeon.setVisibility(View.GONE);
                binding.constraintLayoutFilter.setVisibility(View.GONE);
                quShotEditor.setFilterEffect("");
                quShotEditor.clearBrushAllViews();
                quShotEditor.setBrushDrawingMode(false);
                if (!quShotView.getStickers().isEmpty()) {
                    quShotView.getStickers().clear();
                    quShotView.setHandlingSticker(null);
                }
                binding.relativeLayoutAddText.setVisibility(View.GONE);
                binding.constraintLayoutConfirmText.setVisibility(View.GONE);
                break;
        }
        quShotView.setHandlingSticker(null);
    }

    public void onQuShotDrawToolSelected(Module module) {
        moduleToolsId = module;
        ConstraintSet constraintSet;
        switch (module) {
            case PAINT:
                setColorPaint();
                quShotEditor.setBrushDrawingMode(true);
                constraintLayoutPaint.setVisibility(View.VISIBLE);
                binding.recyclerViewTools.setVisibility(View.GONE);
                constraintLayoutDraw.setVisibility(View.GONE);
                constraintLayoutPaint.setVisibility(View.VISIBLE);
                constraintLayoutAdjust.setVisibility(View.GONE);
                binding.constraintLayoutFilter.setVisibility(View.GONE);
                binding.constraintLayoutNeon.setVisibility(View.GONE);
                constraintLayoutSavePaint.setVisibility(View.VISIBLE);
                binding.relativeLayoutAddText.setVisibility(View.GONE);
                binding.constraintLayoutConfirmText.setVisibility(View.GONE);
                quShotEditor.setFilterEffect("");
                quShotEditor.clearBrushAllViews();
                quShotEditor.setBrushDrawingMode(false);
                setGoneSave();
                setBottomToolbar(true);
                constraintSet = new ConstraintSet();
                constraintSet.clone(binding.constraintLayoutRootView);
                constraintSet.connect(binding.relativeLayoutWrapperPhoto.getId(), 1, binding.constraintLayoutRootView.getId(), 1, 0);
                constraintSet.connect(binding.relativeLayoutWrapperPhoto.getId(), 4, binding.guidelinePaint.getId(), 3, 0);
                constraintSet.connect(binding.relativeLayoutWrapperPhoto.getId(), 2, binding.constraintLayoutRootView.getId(), 2, 0);
                constraintSet.applyTo(binding.constraintLayoutRootView);
                quShotEditor.setBrushMode(1);
                reloadingLayout();
                break;
            case COLORED:
                new openColoredFragment().execute();
                constraintLayoutDraw.setVisibility(View.GONE);
                goneLayout();
                break;
            case NEON:
                setColorNeon();
                quShotEditor.setBrushDrawingMode(true);
                binding.recyclerViewTools.setVisibility(View.GONE);
                binding.constraintLayoutConfirmSaveNeon.setVisibility(View.VISIBLE);
                constraintLayoutDraw.setVisibility(View.GONE);
                binding.constraintLayoutNeon.setVisibility(View.VISIBLE);
                constraintLayoutAdjust.setVisibility(View.GONE);
                binding.constraintLayoutFilter.setVisibility(View.GONE);
                constraintLayoutPaint.setVisibility(View.GONE);
                binding.relativeLayoutAddText.setVisibility(View.GONE);
                binding.constraintLayoutConfirmText.setVisibility(View.GONE);
                quShotEditor.setFilterEffect("");
                quShotEditor.clearBrushAllViews();
                quShotEditor.setBrushDrawingMode(false);
                setGoneSave();
                setBottomToolbar(true);
                constraintSet = new ConstraintSet();
                constraintSet.clone(binding.constraintLayoutRootView);
                constraintSet.connect(binding.relativeLayoutWrapperPhoto.getId(), 1, binding.constraintLayoutRootView.getId(), 1, 0);
                constraintSet.connect(binding.relativeLayoutWrapperPhoto.getId(), 4, binding.constraintLayoutNeon.getId(), 3, 0);
                constraintSet.connect(binding.relativeLayoutWrapperPhoto.getId(), 2, binding.constraintLayoutRootView.getId(), 2, 0);
                constraintSet.applyTo(binding.constraintLayoutRootView);
                quShotEditor.setBrushMode(2);
                reloadingLayout();
                break;
            case MOSAIC:
                new openShapeFragment().execute();
                constraintLayoutDraw.setVisibility(View.GONE);
                goneLayout();
                break;
        }
        quShotView.setHandlingSticker(null);
    }

    private void goneLayout() {
        setVisibleSave();
    }

    public void setGoneSave() {
        constraintLayoutSaveEditing.setVisibility(View.GONE);
    }

    public void setVisibleSave() {
        constraintLayoutSaveEditing.setVisibility(View.VISIBLE);
    }

    public void onBackPressed() {
        if (moduleToolsId != null) {
            try {
                switch (moduleToolsId) {
                    case PAINT:
                        constraintLayoutPaint.setVisibility(View.GONE);
                        setVisibleSave();
                        imageViewUndoPaint.setVisibility(View.GONE);
                        imageViewRedoPaint.setVisibility(View.GONE);
                        imageViewCleanPaint.setVisibility(View.GONE);
                        imageViewErasePaint.setVisibility(View.GONE);
                        binding.recyclerViewTools.setVisibility(View.VISIBLE);
                        constraintLayoutSavePaint.setVisibility(View.GONE);
                        quShotEditor.setBrushDrawingMode(false);
                        ConstraintSet constraintSet = new ConstraintSet();
                        constraintSet.clone(binding.constraintLayoutRootView);
                        constraintSet.connect(binding.relativeLayoutWrapperPhoto.getId(), 1, binding.constraintLayoutRootView.getId(), 1, 0);
                        constraintSet.connect(binding.relativeLayoutWrapperPhoto.getId(), 4, binding.guideline.getId(), 3, 0);
                        constraintSet.connect(binding.relativeLayoutWrapperPhoto.getId(), 2, binding.constraintLayoutRootView.getId(), 2, 0);
                        constraintSet.applyTo(binding.constraintLayoutRootView);
                        quShotEditor.clearBrushAllViews();
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
                        quShotEditor.setBrushDrawingMode(false);
                        ConstraintSet constraintSet1 = new ConstraintSet();
                        constraintSet1.clone(binding.constraintLayoutRootView);
                        constraintSet1.connect(binding.relativeLayoutWrapperPhoto.getId(), 1, binding.constraintLayoutRootView.getId(), 1, 0);
                        constraintSet1.connect(binding.relativeLayoutWrapperPhoto.getId(), 4, binding.guideline.getId(), 3, 0);
                        constraintSet1.connect(binding.relativeLayoutWrapperPhoto.getId(), 2, binding.constraintLayoutRootView.getId(), 2, 0);
                        constraintSet1.applyTo(binding.constraintLayoutRootView);
                        quShotEditor.clearBrushAllViews();
                        setVisibleSave();
                        moduleToolsId = Module.NONE;
                        reloadingLayout();
                        return;
                    case TEXT:
                        if (!quShotView.getStickers().isEmpty()) {
                            quShotView.getStickers().clear();
                            quShotView.setHandlingSticker(null);
                        }
                        binding.recyclerViewTools.setVisibility(View.VISIBLE);
                        binding.relativeLayoutAddText.setVisibility(View.GONE);
                        binding.constraintLayoutConfirmText.setVisibility(View.GONE);
                        quShotView.setHandlingSticker(null);
                        quShotView.setLocked(true);
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
                        quShotEditor.setFilterEffect("");
                        imageViewCompareAdjust.setVisibility(View.GONE);
                        constraintLayoutAdjust.setVisibility(View.GONE);
                        ConstraintSet constraintsetAdjust = new ConstraintSet();
                        constraintsetAdjust.clone(binding.constraintLayoutRootView);
                        constraintsetAdjust.connect(binding.relativeLayoutWrapperPhoto.getId(), 1, binding.constraintLayoutRootView.getId(), 1, 0);
                        constraintsetAdjust.connect(binding.relativeLayoutWrapperPhoto.getId(), 4, binding.guideline.getId(), 3, 0);
                        constraintsetAdjust.connect(binding.relativeLayoutWrapperPhoto.getId(), 2, binding.constraintLayoutRootView.getId(), 2, 0);
                        constraintsetAdjust.applyTo(binding.constraintLayoutRootView);
                        binding.recyclerViewTools.setVisibility(View.VISIBLE);
                        constraintLayoutSave.setVisibility(View.VISIBLE);
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
                        quShotEditor.setFilterEffect("");
                        binding.imageViewCompareFilter.setVisibility(View.GONE);
                        listAllFilter.clear();
                        if (binding.recyclerViewFilterAll.getAdapter() != null) {
                            binding.recyclerViewFilterAll.getAdapter().notifyDataSetChanged();
                        }
                        moduleToolsId = Module.NONE;
                        return;
                    case STICKER:
                        if (quShotView.getStickers().size() <= 0) {
                            binding.linearLayoutWrapperStickerList.setVisibility(View.VISIBLE);
                            constraintLayoutSticker.setVisibility(View.GONE);
                            binding.imageViewAddSticker.setVisibility(View.GONE);
                            quShotView.setHandlingSticker(null);
                            binding.recyclerViewTools.setVisibility(View.VISIBLE);
                            quShotView.setLocked(true);
                            moduleToolsId = Module.NONE;
                        } else if (binding.imageViewAddSticker.getVisibility() == View.VISIBLE) {
                            quShotView.getStickers().clear();
                            binding.imageViewAddSticker.setVisibility(View.GONE);
                            quShotView.setHandlingSticker(null);
                            binding.linearLayoutWrapperStickerList.setVisibility(View.VISIBLE);
                            constraintLayoutSticker.setVisibility(View.GONE);
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
                        if (quShotView.getStickers().size() <= 0) {
                            binding.linearLayoutWrapperStickerMenList.setVisibility(View.VISIBLE);
                            constraintLayoutStickerMen.setVisibility(View.GONE);
                            quShotView.setHandlingSticker(null);
                            binding.imageViewAddStickerMen.setVisibility(View.GONE);
                            binding.recyclerViewTools.setVisibility(View.VISIBLE);
                            quShotView.setLocked(true);
                            moduleToolsId = Module.NONE;
                        } else if (binding.imageViewAddStickerMen.getVisibility() == View.VISIBLE) {
                            quShotView.getStickers().clear();
                            quShotView.setHandlingSticker(null);
                            binding.imageViewAddStickerMen.setVisibility(View.GONE);
                            binding.linearLayoutWrapperStickerMenList.setVisibility(View.VISIBLE);
                            constraintLayoutStickerMen.setVisibility(View.GONE);
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
                        if (quShotView.getStickers().size() <= 0) {
                            binding.linearLayoutWrapperStickerWomenList.setVisibility(View.VISIBLE);
                            constraintLayoutStickerWomen.setVisibility(View.GONE);
                            binding.imageViewAddStickerWomen.setVisibility(View.GONE);
                            quShotView.setHandlingSticker(null);
                            binding.recyclerViewTools.setVisibility(View.VISIBLE);
                            quShotView.setLocked(true);
                            moduleToolsId = Module.NONE;
                        } else if (binding.imageViewAddStickerWomen.getVisibility() == View.VISIBLE) {
                            quShotView.getStickers().clear();
                            binding.imageViewAddStickerWomen.setVisibility(View.GONE);
                            quShotView.setHandlingSticker(null);
                            binding.linearLayoutWrapperStickerWomenList.setVisibility(View.VISIBLE);
                            constraintLayoutStickerWomen.setVisibility(View.GONE);
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
                        quShotEditor.setFilterEffect("");
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
        quShotView.addSticker(new DrawableSticker(new BitmapDrawable(getResources(), bitmap)));
        binding.linearLayoutWrapperStickerList.setVisibility(View.GONE);
        binding.linearLayoutWrapperStickerMenList.setVisibility(View.GONE);
        binding.linearLayoutWrapperStickerWomenList.setVisibility(View.GONE);
        binding.imageViewAddSticker.setVisibility(View.VISIBLE);
        binding.imageViewAddStickerWomen.setVisibility(View.VISIBLE);
        binding.imageViewAddStickerMen.setVisibility(View.VISIBLE);
    }

    public void finishCrop(Bitmap bitmap) {
        quShotView.setImageSource(bitmap);
        moduleToolsId = Module.NONE;
        reloadingLayout();
    }

    public void onColorChanged(String string) {
        quShotEditor.setBrushColor(Color.parseColor(string));
    }

    public void ratioSavedBitmap(Bitmap bitmap) {
        quShotView.setImageSource(bitmap);
        moduleToolsId = Module.NONE;
        reloadingLayout();
    }

    public void onSaveSplash(Bitmap bitmap) {
        quShotView.setImageSource(bitmap);
        moduleToolsId = Module.NONE;
    }

    public void onSaveMosaic(Bitmap bitmap) {
        quShotView.setImageSource(bitmap);
        moduleToolsId = Module.NONE;
    }

    class allFilters extends AsyncTask<Void, Void, Void> {
        allFilters() {}

        public void onPreExecute() {
            showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            listAllFilter.clear();
            listAllFilter.addAll(FilterCodeAsset.getListBitmapFilterAll(ThumbnailUtils.extractThumbnail(quShotView.getCurrentBitmap(), 100, 100)));
            Log.d("XXXXXXXX", "allFilters " + listAllFilter.size());
            return null;
        }

        public void onPostExecute(Void voids) {
            binding.recyclerViewFilterAll.setAdapter(new FilterAdapter(listAllFilter, QueShotEditorActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.ALL_FILTERS)));
            binding.imageViewCompareFilter.setVisibility(View.VISIBLE);
            binding.constraintLayoutFilter.setVisibility(View.VISIBLE);
            binding.constraintLayoutConfirmSaveFilter.setVisibility(View.VISIBLE);
            binding.recyclerViewTools.setVisibility(View.GONE);
            quShotView.setHandlingSticker(null);
            quShotView.getStickers().clear();
            binding.seekbarFilter.setProgress(100);
            showLoading(false);
            quShotEditor.setFilterEffect("");
            quShotEditor.clearBrushAllViews();
            quShotEditor.setBrushDrawingMode(false);

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
            listBwFilter.addAll(FilterCodeAsset.getListBitmapFilterBW(ThumbnailUtils.extractThumbnail(quShotView.getCurrentBitmap(), 100, 100)));
            Log.d("XXXXXXXX", "bwFilters " + listBwFilter.size());
            return null;
        }

        public void onPostExecute(Void voids) {
            binding.recyclerViewFilterBw.setAdapter(new FilterAdapter(listBwFilter, QueShotEditorActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.BW_FILTERS)));
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
            listVintageFilter.addAll(FilterCodeAsset.getListBitmapFilterVintage(ThumbnailUtils.extractThumbnail(quShotView.getCurrentBitmap(), 100, 100)));
            Log.d("XXXXXXXX", "vintageFilters " + listVintageFilter.size());
            return null;
        }

        public void onPostExecute(Void voids) {
            binding.recyclerViewFilterVintage.setAdapter(new FilterAdapter(listVintageFilter, QueShotEditorActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.VINTAGE_FILTERS)));
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
            listSmoothFilter.addAll(FilterCodeAsset.getListBitmapFilterSmooth(ThumbnailUtils.extractThumbnail(quShotView.getCurrentBitmap(), 100, 100)));
            Log.d("XXXXXXXX", "smoothFilters " + listSmoothFilter.size());
            return null;
        }

        public void onPostExecute(Void voids) {
            binding.recyclerViewFilterSmooth.setAdapter(new FilterAdapter(listSmoothFilter, QueShotEditorActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.SMOOTH_FILTERS)));
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
            listColdFilter.addAll(FilterCodeAsset.getListBitmapFilterCold(ThumbnailUtils.extractThumbnail(quShotView.getCurrentBitmap(), 100, 100)));
            Log.d("XXXXXXXX", "coldFilters " + listColdFilter.size());
            return null;
        }

        public void onPostExecute(Void voids) {
            binding.recyclerViewFilterCold.setAdapter(new FilterAdapter(listColdFilter, QueShotEditorActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.COLD_FILTERS)));
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
            listWarmFilter.addAll(FilterCodeAsset.getListBitmapFilterWarm(ThumbnailUtils.extractThumbnail(quShotView.getCurrentBitmap(), 100, 100)));
            Log.d("XXXXXXXX", "warmFilters " + listWarmFilter.size());
            return null;
        }

        public void onPostExecute(Void voids) {
            binding.recyclerViewFilterWarm.setAdapter(new FilterAdapter(listWarmFilter, QueShotEditorActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.WARM_FILTERS)));
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
            listLegacyFilter.addAll(FilterCodeAsset.getListBitmapFilterLegacy(ThumbnailUtils.extractThumbnail(quShotView.getCurrentBitmap(), 100, 100)));
            Log.d("XXXXXXXX", "legacyFilters " + listLegacyFilter.size());
            return null;
        }

        public void onPostExecute(Void voids) {
            binding.recyclerViewFilterLegacy.setAdapter(new FilterAdapter(listLegacyFilter, QueShotEditorActivity.this, getApplicationContext(), Arrays.asList(FilterCodeAsset.LEGACY_FILTERS)));
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
            return FilterUtils.getBlurImageFromBitmap(quShotView.getCurrentBitmap(), 5.0f);
        }

        public void onPostExecute(Bitmap bitmap) {
            showLoading(false);
            RatioFragment.show(QueShotEditorActivity.this, QueShotEditorActivity.this, quShotView.getCurrentBitmap(), bitmap);
        }
    }

    class openFrameFragment extends AsyncTask<Void, Bitmap, Bitmap> {
        openFrameFragment() {}

        public void onPreExecute() {
            showLoading(true);
        }

        public Bitmap doInBackground(Void... voids) {
            return FilterUtils.getBlurImageFromBitmap(quShotView.getCurrentBitmap(), 5.0f);
        }

        public void onPostExecute(Bitmap bitmap) {
            showLoading(false);
            FrameFragment.show(QueShotEditorActivity.this, QueShotEditorActivity.this, quShotView.getCurrentBitmap(), bitmap);
        }
    }

    class openShapeFragment extends AsyncTask<Void, List<Bitmap>, List<Bitmap>> {
        openShapeFragment() {}

        public void onPreExecute() {
            showLoading(true);
        }

        public List<Bitmap> doInBackground(Void... voids) {
            List<Bitmap> arrayList = new ArrayList<>();
            arrayList.add(FilterUtils.cloneBitmap(quShotView.getCurrentBitmap()));
            arrayList.add(FilterUtils.getBlurImageFromBitmap(quShotView.getCurrentBitmap(), 8.0f));
            return arrayList;
        }

        public void onPostExecute(List<Bitmap> list) {
            showLoading(false);
            MosaicFragment.show(QueShotEditorActivity.this, list.get(0), list.get(1), QueShotEditorActivity.this);
        }
    }

    class openColoredFragment extends AsyncTask<Void, List<Bitmap>, List<Bitmap>> {
        openColoredFragment() { }

        public void onPreExecute() {
            showLoading(true);
        }

        public List<Bitmap> doInBackground(Void... voids) {
            List<Bitmap> arrayList = new ArrayList<>();
            arrayList.add(FilterUtils.cloneBitmap(quShotView.getCurrentBitmap()));
            arrayList.add(FilterUtils.getBlurImageFromBitmap(quShotView.getCurrentBitmap(), 8.0f));
            return arrayList;
        }

        public void onPostExecute(List<Bitmap> list) {
            showLoading(false);
            ColoredFragment.show(QueShotEditorActivity.this, list.get(0), list.get(1), QueShotEditorActivity.this);
        }
    }

    class effectDodge extends AsyncTask<Void, Void, Void> {
        effectDodge() { }

        public void onPreExecute() {
            showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            listDodge.clear();
            listDodge.addAll(EffectCodeAsset.getListBitmapDodgeEffect(ThumbnailUtils.extractThumbnail(quShotView.getCurrentBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voids) {
            recyclerViewDodge.setAdapter(new HardmixAdapter(listDodge, QueShotEditorActivity.this, getApplicationContext(), Arrays.asList(EffectCodeAsset.DODGE_EFFECTS)));
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
            listDivide.addAll(EffectCodeAsset.getListBitmapDivideEffect(ThumbnailUtils.extractThumbnail(quShotView.getCurrentBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voids) {
            recyclerViewDivide.setAdapter(new HardmixAdapter(listDivide, QueShotEditorActivity.this, getApplicationContext(), Arrays.asList(EffectCodeAsset.DIVIDE_EFFECTS)));
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
            listHardmix.addAll(EffectCodeAsset.getListBitmapHardmixEffect(ThumbnailUtils.extractThumbnail(quShotView.getCurrentBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voids) {
            recyclerViewHardmix.setAdapter(new HardmixAdapter(listHardmix, QueShotEditorActivity.this, getApplicationContext(), Arrays.asList(EffectCodeAsset.HARDMIX_EFFECTS)));
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
            listOverlay.addAll(EffectCodeAsset.getListBitmapOverlayEffect(ThumbnailUtils.extractThumbnail(quShotView.getCurrentBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voids) {
            binding.recyclerViewOverlay.setAdapter(new HardmixAdapter(listOverlay, QueShotEditorActivity.this, getApplicationContext(), Arrays.asList(EffectCodeAsset.OVERLAY_EFFECTS)));
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
            listBurn.addAll(EffectCodeAsset.getListBitmapColorEffect(ThumbnailUtils.extractThumbnail(quShotView.getCurrentBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voids) {
            binding.recyclerViewBurn.setAdapter(new HardmixAdapter(listBurn, QueShotEditorActivity.this, getApplicationContext(), Arrays.asList(EffectCodeAsset.COLOR_EFFECTS)));
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
            Bitmap currentBitmap = quShotView.getCurrentBitmap();
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
                SplashFragment.show(QueShotEditorActivity.this, list.get(0), null, list.get(1), QueShotEditorActivity.this, true);
            } else {
                SplashFragment.show(QueShotEditorActivity.this, list.get(0), list.get(1), null, QueShotEditorActivity.this, false);
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
            Bitmap currentBitmap = quShotView.getCurrentBitmap();
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
                SplashBlurSquareFragment.show(QueShotEditorActivity.this, list.get(0), null, list.get(1), QueShotEditorActivity.this, true);
            } else {
                SplashBlurSquareFragment.show(QueShotEditorActivity.this, list.get(0), list.get(1), null, QueShotEditorActivity.this, false);
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
            quShotView.saveGLSurfaceViewAsBitmap(bitmap -> bitmaps[0] = bitmap);
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
            quShotView.setImageSource(bitmap);
            quShotView.setFilterEffect("");
            showLoading(false);
        }
    }

    class SaveSticker extends AsyncTask<Void, Void, Bitmap> {
        SaveSticker() {}

        public void onPreExecute() {
            quShotView.getGLSurfaceView().setAlpha(0.0f);
            showLoading(true);
        }

        public Bitmap doInBackground(Void... voids) {
            final Bitmap[] bitmaps = {null};
            while (bitmaps[0] == null) {
                try {
                    quShotEditor.saveStickerAsBitmap(bitmap -> bitmaps[0] = bitmap);
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
            quShotView.setImageSource(bitmap);
            quShotView.getStickers().clear();
            quShotView.getGLSurfaceView().setAlpha(1.0f);
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
                    quShotView.setImageSource(bitmap);
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
                float width = (float) bitmap.getWidth();
                float height = (float) bitmap.getHeight();
                float max = Math.max(width / 1280.0f, height / 1280.0f);
                if (max > 1.0f) {
                    bitmap = Bitmap.createScaledBitmap(bitmap, (int) (width / max), (int) (height / max), false);
                }
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
            quShotView.setImageSource(bitmap);
            reloadingLayout();
        }
    }

    public void reloadingLayout() {
        quShotView.postDelayed(() -> {
            try {
                Display display = getWindowManager().getDefaultDisplay();
                Point point = new Point();
                display.getSize(point);
                int i = point.x;
                int height = binding.relativeLayoutWrapperPhoto.getHeight();
                int i2 = quShotView.getGLSurfaceView().getRenderViewport().width;
                float f = (float) quShotView.getGLSurfaceView().getRenderViewport().height;
                float f2 = (float) i2;
                if (((int) ((((float) i) * f) / f2)) <= height) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -2);
                    params.addRule(13);
                    quShotView.setLayoutParams(params);
                    quShotView.setVisibility(View.VISIBLE);
                } else {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) ((((float) height) * f2) / f), -1);
                    params.addRule(13);
                    quShotView.setLayoutParams(params);
                    quShotView.setVisibility(View.VISIBLE);
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
                return SaveFileUtils.saveBitmapFileEditor(QueShotEditorActivity.this, quShotView.getCurrentBitmap(), new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date())).getAbsolutePath();
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
            Intent i = new Intent(QueShotEditorActivity.this, PhotoShareActivity.class);
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
