package com.photo.collagemaker.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.BitmapShader;
import android.graphics.Color;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.photo.collagemaker.R;
import com.photo.collagemaker.activities.editor.collage_editor.adapter.BackgroundGridAdapter;
import com.photo.collagemaker.adapters.FontAdapter;
import com.photo.collagemaker.assets.FontAsset;
import com.photo.collagemaker.databinding.FragmentAddTextBinding;
import com.photo.collagemaker.picker.QuShotCarouselPicker;
import com.photo.collagemaker.preference.Preference;
import com.photo.collagemaker.custom_view.CustomText;
import com.photo.collagemaker.utils.SystemUtil;

import java.util.ArrayList;
import java.util.List;

public class TextFragment extends DialogFragment implements View.OnClickListener, FontAdapter.ItemClickListener {
    public static final String EXTRA_COLOR_CODE = "extra_color_code";
    public static final String EXTRA_INPUT_TEXT = "extra_input_text";
    public static final String TAG = "TextFragment";

    public List<QuShotCarouselPicker.PickerItem> colorItems;
    private FontAdapter fontAdapter;
    private InputMethodManager inputMethodManager;

    public CustomText quShotText;
    private TextEditor textEditor;
    private List<ImageView> textFunctions;

    public List<QuShotCarouselPicker.PickerItem> textTextureItems;


    public interface TextEditor {
        void onBackButton();

        void onDone(CustomText addTextProperties);
    }

    BackgroundGridAdapter textColorPickerAdapter, textBackgroundColorPickerAdapter;

    FragmentAddTextBinding binding;

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        binding = FragmentAddTextBinding.inflate(layoutInflater, viewGroup, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (quShotText == null) {
            quShotText = CustomText.getDefaultProperties();
        }
        binding.addTextEditText.setTextFragment(this);
        initAddTextLayout();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(new DisplayMetrics());
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        setDefaultStyleForEdittext();
        inputMethodManager.toggleSoftInput(2, 0);
        highlightFunction(binding.imageViewKeyboard);
        binding.recyclerViewFonts.setLayoutManager(new GridLayoutManager(getContext(), 5));
        fontAdapter = new FontAdapter(getContext(), FontAsset.getListFonts());
        fontAdapter.setClickListener(this);
        binding.recyclerViewFonts.setAdapter(fontAdapter);
        Log.d("dkfldsfkjsjnif", "onViewCreated: " + colorItems.size() + "    " + colorItems.get(1).getColor());

        textColorPickerAdapter = new BackgroundGridAdapter(getContext(), (squareView, position, isBlur) -> {
            if (squareView.isColor){
                binding.textViewPreviewEffect.setTextColor(squareView.drawableId);
                quShotText.setQuShotTextColorIndex(position);
                quShotText.setQuShotTextColor(squareView.drawableId);
                quShotText.setQuShotTextShader(null);
            }
        });
        binding.rvTextColor.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.rvTextColor.setHasFixedSize(true);
        binding.rvTextColor.setAdapter(textColorPickerAdapter);

        binding.textureCarouselPicker.setAdapter(new QuShotCarouselPicker.CarouselViewAdapter(getContext(), textTextureItems, 0));
        binding.textureCarouselPicker.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int i) {
            }

            public void onPageSelected(int i) {
            }

            public void onPageScrolled(int i, float f, int i2) {
                if (f > 0.0f) {
                    if (binding.imageViewTextTexture.getVisibility() == View.INVISIBLE) {
                        binding.imageViewTextTexture.setVisibility(View.VISIBLE);
                        binding.viewHighlightTexture.setVisibility(View.VISIBLE);

                    }
                    float f2 = ((float) i) + f;
                    BitmapShader bitmapShader = new BitmapShader((textTextureItems.get(Math.round(f2))).getBitmap(), Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
                    binding.textViewPreviewEffect.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                    binding.textViewPreviewEffect.getPaint().setShader(bitmapShader);
                    quShotText.setQuShotTextShader(bitmapShader);
                    quShotText.setQuShotTextShaderIndex(Math.round(f2));
                }
            }
        });
        binding.seekbarTextOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                int i2 = 255 - i;
                quShotText.setQuShotTextAlpha(i2);
                binding.textViewPreviewEffect.setTextColor(Color.argb(i2, Color.red(quShotText.getQuShotTextColor()), Color.green(quShotText.getQuShotTextColor()), Color.blue(quShotText.getQuShotTextColor())));
            }
        });
        binding.addTextEditText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                binding.textViewPreviewEffect.setText(charSequence.toString());
                quShotText.setQuShotTexts(charSequence.toString());
            }
        });
        binding.checkboxBackground.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (!z) {
                    quShotText.setQuShotShowBackground(false);
                    binding.textViewPreviewEffect.setBackgroundResource(0);
                    binding.textViewPreviewEffect.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
                } else if (binding.checkboxBackground.isPressed() || quShotText.isQuShotShowBackground()) {
                    quShotText.setQuShotShowBackground(true);
                    initPreviewText();
                } else {
                    binding.checkboxBackground.setChecked(false);
                    quShotText.setQuShotShowBackground(false);
                    initPreviewText();
                }
            }
        });

        textBackgroundColorPickerAdapter = new BackgroundGridAdapter(getContext(), (squareView, position, isBlur) -> {
            if (squareView.isColor){
                quShotText.setQuShotShowBackground(true);
                if (!binding.checkboxBackground.isChecked()) {
                    binding.checkboxBackground.setChecked(true);
                }
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setColor(Color.argb(quShotText.getQuShotBackgroundAlpha(), squareView.drawableId, squareView.drawableId, squareView.drawableId));
                gradientDrawable.setCornerRadius((float) SystemUtil.dpToPx(requireContext(), quShotText.getQuShotBackgroundBorder()));
                binding.textViewPreviewEffect.setBackground(gradientDrawable);
                quShotText.setQuShotBackgroundColor(squareView.drawableId);
                quShotText.setQuShotBackgroundColorIndex(position);
                binding.seekbarRadius.setEnabled(true);
            }
        });
        binding.rvTextBackgroundColor.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.rvTextBackgroundColor.setHasFixedSize(true);
        binding.rvTextBackgroundColor.setAdapter(textBackgroundColorPickerAdapter);

        binding.seekbarWidth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                binding.textViewPreviewEffect.setPadding(SystemUtil.dpToPx(requireContext(), i), binding.textViewPreviewEffect.getPaddingTop(), SystemUtil.dpToPx(getContext(), i), binding.textViewPreviewEffect.getPaddingBottom());
                quShotText.setPaddingWidth(i);
            }
        });
        binding.seekbarHeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                binding.textViewPreviewEffect.setPadding(binding.textViewPreviewEffect.getPaddingLeft(), SystemUtil.dpToPx(requireContext(), i), binding.textViewPreviewEffect.getPaddingRight(), SystemUtil.dpToPx(getContext(), i));
                quShotText.setPaddingHeight(i);
            }
        });

        binding.seekbarBackgroundOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                quShotText.setQuShotBackgroundAlpha(255 - i);
                if (quShotText.isQuShotShowBackground()) {
                    int red = Color.red(quShotText.getQuShotBackgroundColor());
                    int green = Color.green(quShotText.getQuShotBackgroundColor());
                    int blue = Color.blue(quShotText.getQuShotBackgroundColor());
                    GradientDrawable gradientDrawable = new GradientDrawable();
                    gradientDrawable.setColor(Color.argb(quShotText.getQuShotBackgroundAlpha(), red, green, blue));
                    gradientDrawable.setCornerRadius((float) SystemUtil.dpToPx(requireContext(), quShotText.getQuShotBackgroundBorder()));
                    binding.textViewPreviewEffect.setBackground(gradientDrawable);
                }
            }
        });
        binding.seekbarTextSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (i >= 15) {
                    binding.textViewPreviewEffect.setTextSize((float) i);
                    quShotText.setQuShotTextSize(i);
                }

            }
        });
        binding.seekbarRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                quShotText.setQuShotBackgroundBorder(i);
                if (quShotText.isQuShotShowBackground()) {
                    GradientDrawable gradientDrawable = new GradientDrawable();
                    gradientDrawable.setCornerRadius((float) SystemUtil.dpToPx(requireContext(), i));
                    gradientDrawable.setColor(Color.argb(quShotText.getQuShotBackgroundAlpha(), Color.red(quShotText.getQuShotBackgroundColor()), Color.green(quShotText.getQuShotBackgroundColor()), Color.blue(quShotText.getQuShotBackgroundColor())));
                    binding.textViewPreviewEffect.setBackground(gradientDrawable);
                }
            }
        });
        if (Preference.getKeyboard(requireContext()) > 0) {
            updateAddTextBottomToolbarHeight(Preference.getKeyboard(getContext()));
        }
        initPreviewText();
    }


    public void onItemClick(View view, int i) {
        FontAsset.setFontByName(requireContext(), binding.textViewPreviewEffect, FontAsset.getListFonts().get(i));
        quShotText.setQuShotFontName(FontAsset.getListFonts().get(i));
        quShotText.setQuShotFontIndex(i);
    }

    public static TextFragment show(@NonNull AppCompatActivity appCompatActivity, @NonNull String str, @ColorInt int i) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_INPUT_TEXT, str);
        bundle.putInt(EXTRA_COLOR_CODE, i);
        TextFragment addTextFragment = new TextFragment();
        addTextFragment.setArguments(bundle);
        addTextFragment.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return addTextFragment;
    }

    public static TextFragment show(@NonNull AppCompatActivity appCompatActivity, CustomText addTextProperties) {
        TextFragment addTextFragment = new TextFragment();
        addTextFragment.setQuShotText(addTextProperties);
        addTextFragment.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return addTextFragment;
    }

    public static TextFragment show(@NonNull AppCompatActivity appCompatActivity) {
        return show(appCompatActivity, "Test", ContextCompat.getColor(appCompatActivity, R.color.text_color_dark));
    }

    public void setQuShotText(CustomText addTextProperties2) {
        this.quShotText = addTextProperties2;
    }

    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
    }

    public void dismissAndShowSticker() {
        if (textEditor != null) {
            textEditor.onBackButton();
        }
        dismiss();
    }

    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
    }


    public void initPreviewText() {
        if (quShotText.isQuShotShowBackground()) {
            if (quShotText.getQuShotBackgroundColor() != 0) {
                binding.textViewPreviewEffect.setBackgroundColor(quShotText.getQuShotBackgroundColor());
            }
            if (quShotText.getQuShotBackgroundAlpha() < 255) {
                binding.textViewPreviewEffect.setBackgroundColor(Color.argb(quShotText.getQuShotBackgroundAlpha(), Color.red(quShotText.getQuShotBackgroundColor()), Color.green(quShotText.getQuShotBackgroundColor()), Color.blue(quShotText.getQuShotBackgroundColor())));
            }
            if (quShotText.getQuShotBackgroundBorder() > 0) {
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setCornerRadius((float) SystemUtil.dpToPx(requireContext(), quShotText.getQuShotBackgroundBorder()));
                gradientDrawable.setColor(Color.argb(quShotText.getQuShotBackgroundAlpha(), Color.red(quShotText.getQuShotBackgroundColor()), Color.green(quShotText.getQuShotBackgroundColor()), Color.blue(quShotText.getQuShotBackgroundColor())));
                binding.textViewPreviewEffect.setBackground(gradientDrawable);
            }
        }
        if (quShotText.getPaddingHeight() > 0) {
            binding.textViewPreviewEffect.setPadding(binding.textViewPreviewEffect.getPaddingLeft(), quShotText.getPaddingHeight(), binding.textViewPreviewEffect.getPaddingRight(), quShotText.getPaddingHeight());
            binding.seekbarHeight.setProgress(quShotText.getPaddingHeight());
        }
        if (quShotText.getPaddingWidth() > 0) {
            binding.textViewPreviewEffect.setPadding(quShotText.getPaddingWidth(), binding.textViewPreviewEffect.getPaddingTop(), quShotText.getPaddingWidth(), binding.textViewPreviewEffect.getPaddingBottom());
            binding.seekbarWidth.setProgress(quShotText.getPaddingWidth());
        }
        if (quShotText.getQuShotTexts() != null) {
            binding.textViewPreviewEffect.setText(quShotText.getQuShotTexts());
            binding.addTextEditText.setText(quShotText.getQuShotTexts());
        }
        if (quShotText.getQuShotTextShader() != null) {
            binding.textViewPreviewEffect.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            binding.textViewPreviewEffect.getPaint().setShader(quShotText.getQuShotTextShader());
        }
        if (quShotText.getQuShotTextAlign() == 4) {
            binding.imageViewAlign.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_center));
        } else if (quShotText.getQuShotTextAlign() == 3) {
            binding.imageViewAlign.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_right));
        } else if (quShotText.getQuShotTextAlign() == 2) {
            binding.imageViewAlign.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_left));
        }
        binding.textViewPreviewEffect.setPadding(SystemUtil.dpToPx(getContext(), quShotText.getPaddingWidth()), binding.textViewPreviewEffect.getPaddingTop(), SystemUtil.dpToPx(getContext(), quShotText.getPaddingWidth()), binding.textViewPreviewEffect.getPaddingBottom());
        binding.textViewPreviewEffect.setTextColor(quShotText.getQuShotTextColor());
        binding.textViewPreviewEffect.setTextAlignment(quShotText.getQuShotTextAlign());
        binding.textViewPreviewEffect.setTextSize((float) quShotText.getQuShotTextSize());
        FontAsset.setFontByName(getContext(), binding.textViewPreviewEffect, quShotText.getQuShotFontName());
        binding.textViewPreviewEffect.invalidate();
    }

    private void setDefaultStyleForEdittext() {
        binding.addTextEditText.requestFocus();
        binding.addTextEditText.setTextSize(20.0f);
        binding.addTextEditText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        binding.addTextEditText.setTextColor(getContext().getColor(R.color.text_color_dark));
    }

    private void initAddTextLayout() {
        textFunctions = getTextFunctions();
        binding.imageViewKeyboard.setOnClickListener(this);
        binding.imageViewFonts.setOnClickListener(this);
        binding.imageViewAdjust.setOnClickListener(this);
        binding.imageViewColor.setOnClickListener(this);
        binding.imageViewAlign.setOnClickListener(this);
        binding.imageViewSaveChange.setOnClickListener(this);
        binding.scrollViewChangeFontLayout.setVisibility(View.GONE);
        binding.scrollViewChangeColorAdjust.setVisibility(View.GONE);
        binding.scrollViewChangeColorLayout.setVisibility(View.GONE);
        binding.imageViewTextTexture.setVisibility(View.INVISIBLE);
        binding.viewHighlightTexture.setVisibility(View.GONE);
        binding.seekbarWidth.setProgress(quShotText.getPaddingWidth());
        colorItems = getColorItems();
        textTextureItems = getTextTextures();
    }

    public void onResume() {
        super.onResume();
        ViewCompat.setOnApplyWindowInsetsListener(getDialog().getWindow().getDecorView(), new OnApplyWindowInsetsListener() {
            public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                return ViewCompat.onApplyWindowInsets(
                        getDialog().getWindow().getDecorView(),
                        windowInsetsCompat.inset(windowInsetsCompat.getSystemWindowInsetLeft(), 0, windowInsetsCompat.getSystemWindowInsetRight(), windowInsetsCompat.getSystemWindowInsetBottom()));
            }
        });

    }

    public void updateAddTextBottomToolbarHeight(final int i) {
        new Handler().post(() -> {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) binding.linearLayoutEditTextTools.getLayoutParams();
//            layoutParams.bottomMargin = i;
            binding.linearLayoutEditTextTools.setLayoutParams(layoutParams);
            binding.linearLayoutEditTextTools.invalidate();
            binding.scrollViewChangeFontLayout.invalidate();
            binding.scrollViewChangeColorAdjust.invalidate();
            binding.scrollViewChangeColorLayout.invalidate();
        });
    }

    public void setOnTextEditorListener(TextEditor textEditor2) {
        textEditor = textEditor2;
    }

    private void highlightFunction(ImageView imageView) {
        for (ImageView next : textFunctions) {
            if (next == imageView) {
                imageView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.line));
            } else {
                next.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.line_fake));
            }
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == binding.imageViewAlign.getId()) {
            if (quShotText.getQuShotTextAlign() == 4) {
                quShotText.setQuShotTextAlign(3);
                binding.imageViewAlign.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_right));
            } else if (quShotText.getQuShotTextAlign() == 3) {
                quShotText.setQuShotTextAlign(2);
                binding.imageViewAlign.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_left));
            } else if (quShotText.getQuShotTextAlign() == 2) {
                quShotText.setQuShotTextAlign(4);
                binding.imageViewAlign.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_center));
            }
            binding.textViewPreviewEffect.setTextAlignment(quShotText.getQuShotTextAlign());
            TextView textView = binding.textViewPreviewEffect;
            textView.setText(binding.textViewPreviewEffect.getText().toString().trim() + " ");
            binding.textViewPreviewEffect.setText(binding.textViewPreviewEffect.getText().toString().trim());
        } else if (id == binding.imageViewAdjust.getId()) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            binding.scrollViewChangeColorLayout.setVisibility(View.GONE);
            binding.scrollViewChangeColorAdjust.setVisibility(View.VISIBLE);
            binding.scrollViewChangeFontLayout.setVisibility(View.GONE);
            binding.seekbarBackgroundOpacity.setProgress(255 - quShotText.getQuShotBackgroundAlpha());
            binding.seekbarTextSize.setProgress(quShotText.getQuShotTextSize());
            binding.seekbarRadius.setProgress(quShotText.getQuShotBackgroundBorder());
            binding.seekbarWidth.setProgress(quShotText.getPaddingWidth());
            binding.seekbarHeight.setProgress(quShotText.getPaddingHeight());
            binding.seekbarTextOpacity.setProgress(255 - quShotText.getQuShotTextAlpha());
            toggleTextEditEditable(false);
            highlightFunction(binding.imageViewAdjust);
        } else if (id == binding.imageViewColor.getId()) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            binding.scrollViewChangeColorLayout.setVisibility(View.VISIBLE);
            binding.scrollViewChangeColorAdjust.setVisibility(View.GONE);
            toggleTextEditEditable(false);
            highlightFunction(binding.imageViewColor);
            binding.scrollViewChangeFontLayout.setVisibility(View.GONE);
            binding.addTextEditText.setVisibility(View.GONE);
            textColorPickerAdapter.setSelectedIndex(quShotText.getQuShotTextColorIndex());
//            binding.colorCarouselPicker.setCurrentItem(quShotText.getQuShotTextColorIndex());
            binding.textureCarouselPicker.setCurrentItem(quShotText.getQuShotTextShaderIndex());
            binding.checkboxBackground.setChecked(quShotText.isQuShotShowBackground());
            textBackgroundColorPickerAdapter.setSelectedIndex(quShotText.getQuShotBackgroundColorIndex());
//            binding.backgroundCarouselPicker.setCurrentItem(quShotText.getQuShotBackgroundColorIndex());
            binding.checkboxBackground.setChecked(quShotText.isQuShotShowBackground());
            if (quShotText.getQuShotTextShader() != null && binding.imageViewTextTexture.getVisibility() == View.INVISIBLE) {
                binding.imageViewTextTexture.setVisibility(View.VISIBLE);
                binding.viewHighlightTexture.setVisibility(View.VISIBLE);
//                binding.imageViewColorDown.setVisibility(View.INVISIBLE);
//                binding.viewHighlightTextColor.setVisibility(View.GONE);
            }
        } else if (id == binding.imageViewFonts.getId()) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            binding.scrollViewChangeFontLayout.setVisibility(View.VISIBLE);
            binding.scrollViewChangeColorLayout.setVisibility(View.GONE);
            binding.scrollViewChangeColorAdjust.setVisibility(View.GONE);
            binding.addTextEditText.setVisibility(View.GONE);
            toggleTextEditEditable(false);
            highlightFunction(binding.imageViewFonts);
            fontAdapter.setSelectedItem(quShotText.getQuShotFontIndex());
        } else if (id == binding.imageViewSaveChange.getId()) {
            if (quShotText.getQuShotTexts() == null || quShotText.getQuShotTexts().length() == 0) {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                textEditor.onBackButton();
                dismiss();
                return;
            }
            quShotText.setQuShotTextWidth(binding.textViewPreviewEffect.getMeasuredWidth());
            quShotText.setQuShotTextHeight(binding.textViewPreviewEffect.getMeasuredHeight());
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            textEditor.onDone(quShotText);
            dismiss();
        } else if (id == binding.imageViewKeyboard.getId()) {
            toggleTextEditEditable(true);
            binding.addTextEditText.setVisibility(View.VISIBLE);
            binding.addTextEditText.requestFocus();
            highlightFunction(binding.imageViewKeyboard);
            binding.scrollViewChangeFontLayout.setVisibility(View.GONE);
            binding.scrollViewChangeColorLayout.setVisibility(View.GONE);
            binding.scrollViewChangeColorAdjust.setVisibility(View.GONE);
            binding.linearLayoutEditTextTools.invalidate();
            inputMethodManager.toggleSoftInput(2, 0);
        }
    }

    private void toggleTextEditEditable(boolean z) {
        binding.addTextEditText.setFocusable(z);
        binding.addTextEditText.setFocusableInTouchMode(z);
        binding.addTextEditText.setClickable(z);

    }

    private List<ImageView> getTextFunctions() {
        ArrayList<ImageView> arrayList = new ArrayList<>();
        arrayList.add(binding.imageViewKeyboard);
        arrayList.add(binding.imageViewFonts);
        arrayList.add(binding.imageViewColor);
        arrayList.add(binding.imageViewAdjust);
        arrayList.add(binding.imageViewAlign);
        arrayList.add(binding.imageViewSaveChange);
        return arrayList;
    }

    public List<QuShotCarouselPicker.PickerItem> getTextTextures() {
        ArrayList<QuShotCarouselPicker.PickerItem> arrayList = new ArrayList<>();
        for (int i = 0; i < 42; i++) {
            try {
                AssetManager assets = getContext().getAssets();
                arrayList.add(new QuShotCarouselPicker.DrawableItem(Drawable.createFromStream(assets.open("texture/texture_" + (i + 1) + ".jpg"), null)));
            } catch (Exception ignored) {
            }
        }
        return arrayList;
    }

    public List<QuShotCarouselPicker.PickerItem> getColorItems() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ffffff"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#000000"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ffebee"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ffcdd2"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ef9a9a"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#e57373"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ef5350"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#f44336"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#e53935"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#d32f2f"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#c62828"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#b71c1c"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ff8a80"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ff5252"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ff1744"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#d50000"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#fce4ec"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#f8bbd0"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#f48fb1"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#f06292"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ec407a"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#e91e63"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#d81b60"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#c2185b"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ad1457"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#880e4f"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ff80ab"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ff4081"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#f50057"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#c51162"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#f3e5f5"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#e1bee7"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ce93d8"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ba68c8"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ab47bc"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#9c27b0"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#8e24aa"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#7b1fa2"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#6a1b9a"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#4a148c"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ea80fc"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#e040fb"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#d500f9"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#aa00ff"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ede7f6"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#d1c4e9"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#b39ddb"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#9575cd"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#7e57c2"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#673ab7"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#5e35b1"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#512da8"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#4527a0"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#311b92"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#b388ff"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#7c4dff"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#651fff"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#6200ea"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#e8eaf6"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#c5cae9"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#9fa8da"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#7986cb"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#5c6bc0"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#3f51b5"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#3949ab"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#303f9f"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#283593"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#1a237e"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#8c9eff"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#536dfe"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#3d5afe"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#304ffe"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#e3f2fd"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#bbdefb"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#90caf9"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#64b5f6"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#42a5f5"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#2196f3"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#1e88e5"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#1976d2"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#1565c0"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#0d47a1"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#82b1ff"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#448aff"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#2979ff"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#2962ff"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#e1f5fe"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#b3e5fc"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#81d4fa"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#4fc3f7"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#29b6f6"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#03a9f4"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#039be5"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#0288d1"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#0277bd"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#01579b"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#80d8ff"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#40c4ff"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#00b0ff"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#0091ea"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#e0f7fa"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#b2ebf2"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#80deea"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#4dd0e1"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#26c6da"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#00bcd4"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#00acc1"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#0097a7"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#00838f"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#006064"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#84ffff"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#18ffff"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#00e5ff"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#00b8d4"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#e0f2f1"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#b2dfdb"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#80cbc4"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#4db6ac"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#26a69a"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#009688"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#00897b"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#00796b"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#00695c"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#004d40"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#a7ffeb"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#64ffda"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#1de9b6"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#00bfa5"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#e8f5e9"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#c8e6c9"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#a5d6a7"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#81c784"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#66bb6a"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#4caf50"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#43a047"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#388e3c"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#2e7d32"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#1b5e20"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#b9f6ca"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#69f0ae"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#00e676"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#00c853"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#f1f8e9"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#dcedc8"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#c5e1a5"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#aed581"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#9ccc65"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#8bc34a"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#7cb342"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#689f38"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#558b2f"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#33691e"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ccff90"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#b2ff59"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#76ff03"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#64dd17"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#f9fbe7"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#f0f4c3"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#e6ee9c"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#dce775"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#d4e157"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#cddc39"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#c0ca33"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#afb42b"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#9e9d24"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#827717"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#f4ff81"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#eeff41"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#c6ff00"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#aeea00"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#fffde7"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#fff9c4"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#fff59d"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#fff176"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ffee58"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ffeb3b"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#fdd835"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#fbc02d"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#f9a825"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#f57f17"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ffff8d"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ffff00"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ffea00"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ffd600"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#fff8e1"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ffecb3"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ffe082"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ffd54f"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ffca28"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ffc107"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ffb300"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ffa000"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ff8f00"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ff6f00"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ffe57f"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ffd740"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ffc400"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ffab00"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#fff3e0"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ffe0b2"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ffcc80"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ffb74d"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ffa726"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ff9800"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#fb8c00"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#f57c00"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ef6c00"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#e65100"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ffd180"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ffab40"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ff9100"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ff6d00"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#fbe9e7"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ffccbc"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ffab91"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ff8a65"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ff7043"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ff5722"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#f4511e"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#e64a19"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#d84315"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#bf360c"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ff9e80"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ff6e40"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#ff3d00"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#dd2c00"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#efebe9"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#d7ccc8"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#bcaaa4"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#a1887f"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#8d6e63"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#795548"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#6d4c41"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#5d4037"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#4e342e"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#3e2723"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#fafafa"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#f5f5f5"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#eeeeee"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#e0e0e0"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#bdbdbd"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#9e9e9e"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#757575"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#616161"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#424242"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#212121"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#eceff1"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#cfd8dc"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#b0bec5"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#90a4ae"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#78909c"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#607d8b"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#546e7a"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#455a64"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#37474f"));
        arrayList.add(new QuShotCarouselPicker.ColorItem("#263238"));
        return arrayList;
    }
}
