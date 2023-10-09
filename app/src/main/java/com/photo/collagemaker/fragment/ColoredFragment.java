package com.photo.collagemaker.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.photo.collagemaker.R;
import com.photo.collagemaker.assets.ColoredCodeAsset;
import com.photo.collagemaker.adapters.ColoredAdapter;
import com.photo.collagemaker.databinding.FragmentColoredBinding;

public class ColoredFragment extends DialogFragment implements ColoredAdapter.ColoredChangeListener {
    private static final String TAG = "ColoredFragment";
    public Bitmap adjustBitmap;
    public Bitmap bitmap;
    public ColoredListener coloredListener;


    public interface ColoredListener {
        void onSaveMosaic(Bitmap bitmap);
    }

    public void setBitmap(Bitmap bitmap2) {
        this.bitmap = bitmap2;
    }

    public static ColoredFragment show(@NonNull AppCompatActivity appCompatActivity, Bitmap bitmap2, Bitmap bitmap3, ColoredListener coloredListener) {
        ColoredFragment coloredFragment = new ColoredFragment();
        coloredFragment.setBitmap(bitmap2);
        coloredFragment.setAdjustBitmap(bitmap3);
        coloredFragment.setColoredListener(coloredListener);
        coloredFragment.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return coloredFragment;
    }

    public void setColoredListener(ColoredListener coloredListener) {
        this.coloredListener = coloredListener;
    }

    public void setAdjustBitmap(Bitmap bitmap2) {
        this.adjustBitmap = bitmap2;
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
    }

    FragmentColoredBinding binding;
    @SuppressLint("WrongConstant")
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        binding = FragmentColoredBinding.inflate(layoutInflater, viewGroup, false);

        binding.coloredView.setImageBitmap(bitmap);
        binding.coloredView.setColoredItems(new ColoredAdapter.ColoredItems(R.drawable.colored_1, 0, ColoredAdapter.COLORED.COLOR_1));
        adjustBitmap = ColoredCodeAsset.getColoredBitmap1(bitmap);
        binding.imageViewBackground.setImageBitmap(adjustBitmap);
        binding.relativeLayoutLoading.setVisibility(View.GONE);

        binding.recyclerViewColored.setLayoutManager(new LinearLayoutManager(getContext(), 0, false));
        binding.recyclerViewColored.setAdapter(new ColoredAdapter(getContext(), this));
        binding.imageViewSaveColored.setOnClickListener(view -> new SaveMosaicView().execute(new Void[0]));
        binding.imageViewCloseColored.setOnClickListener(view -> dismiss());
        binding.seekbarColored.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                binding.coloredView.setBrushBitmapSize(i + 25);
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                binding.coloredView.updateBrush();
            }
        });
        binding.imageViewUndo.setOnClickListener(view -> binding.coloredView.undo());
        binding.imageViewRedo.setOnClickListener(view -> binding.coloredView.redo());
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
    }

    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(-16777216));
        }
    }

    public void onDestroy() {
        super.onDestroy();
        bitmap.recycle();
        bitmap = null;
        adjustBitmap.recycle();
        adjustBitmap = null;
    }

    public void onStop() {
        super.onStop();
    }

    public void onSelected(ColoredAdapter.ColoredItems coloredItems) {
        if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_1) {
            adjustBitmap = ColoredCodeAsset.getColoredBitmap1(bitmap);
            binding.imageViewBackground.setImageBitmap(adjustBitmap);
        } else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_2) {
            adjustBitmap = ColoredCodeAsset.getColoredBitmap2(bitmap);
            binding.imageViewBackground.setImageBitmap(adjustBitmap);
        } else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_3) {
            adjustBitmap = ColoredCodeAsset.getColoredBitmap3(bitmap, -1.0f);
            binding.imageViewBackground.setImageBitmap(adjustBitmap);
        }else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_4) {
            adjustBitmap = ColoredCodeAsset.getColoredBitmap4(bitmap, 1.0f);
            binding.imageViewBackground.setImageBitmap(adjustBitmap);
        } else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_5) {
            adjustBitmap = ColoredCodeAsset.getColoredBitmap5(bitmap);
            binding.imageViewBackground.setImageBitmap(adjustBitmap);
        } else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_6) {
            adjustBitmap = ColoredCodeAsset.getColoredBitmap6(bitmap);
            binding.imageViewBackground.setImageBitmap(adjustBitmap);
        }else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_7) {
            adjustBitmap = ColoredCodeAsset.getColoredBitmap7(bitmap);
            binding.imageViewBackground.setImageBitmap(adjustBitmap);
        }else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_8) {
            adjustBitmap = ColoredCodeAsset.getColoredBitmap8(bitmap);
            binding.imageViewBackground.setImageBitmap(adjustBitmap);
        }else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_9) {
            adjustBitmap = ColoredCodeAsset.getColoredBitmap9(bitmap);
            binding.imageViewBackground.setImageBitmap(adjustBitmap);
        }else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_10) {
            adjustBitmap = ColoredCodeAsset.getColoredBitmap10(bitmap);
            binding.imageViewBackground.setImageBitmap(adjustBitmap);
        }else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_11) {
            adjustBitmap = ColoredCodeAsset.getColoredBitmap11(bitmap);
            binding.imageViewBackground.setImageBitmap(adjustBitmap);
        }else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_12) {
            adjustBitmap = ColoredCodeAsset.getColoredBitmap12(bitmap);
            binding.imageViewBackground.setImageBitmap(adjustBitmap);
        }else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_13) {
            adjustBitmap = ColoredCodeAsset.getColoredBitmap13(bitmap);
            binding.imageViewBackground.setImageBitmap(adjustBitmap);
        }else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_14) {
            adjustBitmap = ColoredCodeAsset.getColoredBitmap14(bitmap);
            binding.imageViewBackground.setImageBitmap(adjustBitmap);
        }else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_15) {
            adjustBitmap = ColoredCodeAsset.getColoredBitmap15(bitmap);
            binding.imageViewBackground.setImageBitmap(adjustBitmap);
        }
        binding.coloredView.setColoredItems(coloredItems);
    }

    class SaveMosaicView extends AsyncTask<Void, Bitmap, Bitmap> {
        SaveMosaicView() {
        }
        public void onPreExecute() {
            mLoading(true);
        }

        public Bitmap doInBackground(Void... voidArr) {
            return binding.coloredView.getBitmap(bitmap, adjustBitmap);
        }

        public void onPostExecute(Bitmap bitmap) {
            mLoading(false);
            coloredListener.onSaveMosaic(bitmap);
            dismiss();
        }
    }

    public void mLoading(boolean isLoading) {
        if (isLoading) {
            getActivity().getWindow().setFlags(16, 16);
            binding.relativeLayoutLoading.setVisibility(View.VISIBLE);
            return;
        }
        getActivity().getWindow().clearFlags(16);
        binding.relativeLayoutLoading.setVisibility(View.GONE);
    }

}
