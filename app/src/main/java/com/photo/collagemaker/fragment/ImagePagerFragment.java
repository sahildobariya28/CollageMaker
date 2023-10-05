package com.photo.collagemaker.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.photo.collagemaker.R;
import com.photo.collagemaker.adapters.PhotoPagerAdapter;
import com.photo.collagemaker.databinding.FragmentImagePagerBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImagePagerFragment extends Fragment {
    public static final String ARG_CURRENT_ITEM = "ARG_CURRENT_ITEM";
    public static final String ARG_PATH = "PATHS";
    private int currentItem = 0;
    private PhotoPagerAdapter mPagerAdapter;
    private ArrayList<String> paths;

    FragmentImagePagerBinding binding;

    public static ImagePagerFragment newInstance(List<String> list, int i) {
        ImagePagerFragment imagePagerFragment = new ImagePagerFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArray(ARG_PATH, (String[]) list.toArray(new String[list.size()]));
        bundle.putInt(ARG_CURRENT_ITEM, i);
        imagePagerFragment.setArguments(bundle);
        return imagePagerFragment;
    }

    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = FragmentImagePagerBinding.inflate(layoutInflater, viewGroup, false);

        binding.viewPagerPhotos.setAdapter(mPagerAdapter);
        binding.viewPagerPhotos.setCurrentItem(currentItem);
        binding.viewPagerPhotos.setOffscreenPageLimit(5);

        return binding.getRoot();
    }

    public void onResume() {
        super.onResume();
    }

    public void setPhotos(List<String> list, int i) {
        paths.clear();
        paths.addAll(list);
        currentItem = i;
        binding.viewPagerPhotos.setCurrentItem(i);
        binding.viewPagerPhotos.getAdapter().notifyDataSetChanged();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        paths = new ArrayList<>();
        Bundle arguments = getArguments();
        if (arguments != null) {
            String[] stringArray = arguments.getStringArray(ARG_PATH);
            paths.clear();
            if (stringArray != null) {
                paths = new ArrayList<>(Arrays.asList(stringArray));
            }
            currentItem = arguments.getInt(ARG_CURRENT_ITEM);
        }
        mPagerAdapter = new PhotoPagerAdapter(Glide.with((Fragment) this), paths);
    }




    public ArrayList<String> getPaths() {
        return paths;
    }


    public void onDestroy() {
        super.onDestroy();
        paths.clear();
        paths = null;
        if (binding.viewPagerPhotos != null) {
            binding.viewPagerPhotos.setAdapter(null);
        }
    }
}
