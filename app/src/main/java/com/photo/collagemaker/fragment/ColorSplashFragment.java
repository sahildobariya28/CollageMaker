package com.photo.collagemaker.fragment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.photo.collagemaker.R;
import com.photo.collagemaker.activities.editor.single_editor.adapter.ColorAdapter;
import com.photo.collagemaker.databinding.LayoutSplashBinding;
import com.photo.collagemaker.listener.BrushColorListener;

public class ColorSplashFragment extends DialogFragment implements BrushColorListener {
    private Bitmap bitmap;

    LayoutSplashBinding binding;

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        getDialog().getWindow().requestFeature(1);
        getDialog().getWindow().setFlags(1024, 1024);
        View inflate = layoutInflater.inflate(R.layout.layout_splash, viewGroup, false);
        binding = LayoutSplashBinding.inflate(layoutInflater, viewGroup, false);


        binding.imageViewPreview.setImageBitmap(bitmap);
        binding.recyclerViewColorBrush.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
//        recycler_view_color_brush.setHasFixedSize(true);
        binding.recyclerViewColorBrush.setAdapter(new ColorAdapter(getContext(), this));

        return binding.getRoot();
    }

    public void setBitmap(Bitmap bitmap2) {
        this.bitmap = bitmap2;
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
    }

    public static ColorSplashFragment show(@NonNull AppCompatActivity appCompatActivity, Bitmap bitmap2) {
        ColorSplashFragment colorSplashDialog = new ColorSplashFragment();
        colorSplashDialog.setBitmap(bitmap2);
        colorSplashDialog.show(appCompatActivity.getSupportFragmentManager(), "ColorSplashFragment");
        return colorSplashDialog;
    }

    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(-16777216));
        }
    }



    public void onColorChanged(String str) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0.0f);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        binding.imageViewPreview.setImageBitmap(createBitmap);
    }
}
