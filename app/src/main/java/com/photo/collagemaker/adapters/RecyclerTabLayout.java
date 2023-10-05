package com.photo.collagemaker.adapters;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.photo.collagemaker.R;

public class RecyclerTabLayout extends RecyclerView {
    protected static final float DEFAULT_POSITION_THRESHOLD = 0.6f;
    protected static final long DEFAULT_SCROLL_DURATION = 200;
    protected static final float POSITION_THRESHOLD_ALLOWABLE = 0.001f;
    protected Adapter<?> mAdapter;
    protected int mIndicatorGap;
    protected int mIndicatorHeight;
    protected Paint mIndicatorPaint;
    protected int mIndicatorPosition;
    protected int mIndicatorScroll;
    protected LinearLayoutManager mLinearLayoutManager;
    private int mOldPosition;
    protected float mOldPositionOffset;
    private int mOldScrollOffset;
    protected float mPositionThreshold;
    protected RecyclerOnScrollListener mRecyclerOnScrollListener;
    protected boolean mRequestScrollToTab;
    protected boolean mScrollEanbled;
    protected int mTabBackgroundResId;
    protected int mTabMaxWidth;
    protected int mTabMinWidth;
    protected int mTabOnScreenLimit;
    protected int mTabPaddingBottom;
    protected int mTabPaddingEnd;
    protected int mTabPaddingStart;
    protected int mTabPaddingTop;
    protected int mTabSelectedTextColor;
    protected boolean mTabSelectedTextColorSet;
    protected int mTabTextAppearance;
    protected ViewPager mViewPager;

    public RecyclerTabLayout(Context context) {
        this(context, null);
    }

    public RecyclerTabLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public RecyclerTabLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setWillNotDraw(false);
        mIndicatorPaint = new Paint();
        getAttributes(context, attributeSet, i);
        mLinearLayoutManager = new LinearLayoutManager(getContext()) {
            public boolean canScrollHorizontally() {
                return mScrollEanbled;
            }
        };
        mLinearLayoutManager.setOrientation(HORIZONTAL);
        setLayoutManager(mLinearLayoutManager);
        setItemAnimator(null);
        mPositionThreshold = DEFAULT_POSITION_THRESHOLD;
    }

    @SuppressLint("ResourceType")
    private void getAttributes(Context context, AttributeSet attributeSet, int i) {
        final int[] rtl_RecyclerTabLayout = {R.attr.mScrollEnabled, R.attr.mTabBackground, R.attr.mTabIndicatorColor, R.attr.mTabIndicatorHeight, R.attr.mTabMaxWidth, R.attr.mTabMinWidth, R.attr.mTabOnScreenLimit, R.attr.mTabPadding, R.attr.mTabPaddingBottom, R.attr.mTabPaddingEnd, R.attr.mTabPaddingStart, R.attr.mTabPaddingTop, R.attr.mTabSelectedTextColor, R.attr.mTabTextAppearance};

        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, rtl_RecyclerTabLayout, i, R.style.mRecyclerTabLayout);
        setIndicatorColor(obtainStyledAttributes.getColor(2, 0));
        setIndicatorHeight(obtainStyledAttributes.getDimensionPixelSize(3, 0));
        mTabTextAppearance = obtainStyledAttributes.getResourceId(13, R.style.mRecyclerTabLayout_Tab);
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(7, 0);
        mTabPaddingBottom = dimensionPixelSize;
        mTabPaddingEnd = dimensionPixelSize;
        mTabPaddingTop = dimensionPixelSize;
        mTabPaddingStart = dimensionPixelSize;
        mTabPaddingStart = obtainStyledAttributes.getDimensionPixelSize(10, mTabPaddingStart);
        mTabPaddingTop = obtainStyledAttributes.getDimensionPixelSize(11, mTabPaddingTop);
        mTabPaddingEnd = obtainStyledAttributes.getDimensionPixelSize(9, mTabPaddingEnd);
        mTabPaddingBottom = obtainStyledAttributes.getDimensionPixelSize(8, mTabPaddingBottom);
        if (obtainStyledAttributes.hasValue(12)) {
            mTabSelectedTextColor = obtainStyledAttributes.getColor(12, 0);
            mTabSelectedTextColorSet = true;
        }
        mTabOnScreenLimit = obtainStyledAttributes.getInteger(6, 0);
        if (mTabOnScreenLimit == 0) {
            mTabMinWidth = obtainStyledAttributes.getDimensionPixelSize(5, 0);
            mTabMaxWidth = obtainStyledAttributes.getDimensionPixelSize(4, 0);
        }
        mTabBackgroundResId = obtainStyledAttributes.getResourceId(1, 0);
        mScrollEanbled = obtainStyledAttributes.getBoolean(0, true);
        obtainStyledAttributes.recycle();
    }


    public void onDetachedFromWindow() {
        if (mRecyclerOnScrollListener != null) {
            removeOnScrollListener(mRecyclerOnScrollListener);
            mRecyclerOnScrollListener = null;
        }
        super.onDetachedFromWindow();
    }

    public void setIndicatorColor(int i) {
        mIndicatorPaint.setColor(i);
    }

    public void setIndicatorHeight(int i) {
        mIndicatorHeight = i;
    }

    public void setPositionThreshold(float f) {
        mPositionThreshold = f;
    }

    public void setUpWithAdapter(Adapter<?> adapter) {
        mAdapter = adapter;
        mViewPager = adapter.getViewPager();
        if (mViewPager.getAdapter() != null) {
            mViewPager.addOnPageChangeListener(new ViewPagerOnPageChangeListener(this));
            setAdapter(adapter);
            scrollToTab(mViewPager.getCurrentItem());
            return;
        }
        throw new IllegalArgumentException("ViewPager does not have a PagerAdapter set");
    }

    public void setCurrentItem(int i, boolean z) {
        if (mViewPager != null) {
            mViewPager.setCurrentItem(i, z);
            scrollToTab(mViewPager.getCurrentItem());
        } else if (!z || i == mIndicatorPosition) {
            scrollToTab(i);
        } else {
            startAnimation(i);
        }
    }


    public void startAnimation(final int i) {
        float f;
        ValueAnimator valueAnimator;
        View findViewByPosition = mLinearLayoutManager.findViewByPosition(i);
        if (findViewByPosition != null) {
            f = Math.abs((((float) getMeasuredWidth()) / 2.0f) - (findViewByPosition.getX() + (((float) findViewByPosition.getMeasuredWidth()) / 2.0f))) / ((float) findViewByPosition.getMeasuredWidth());
        } else {
            f = 1.0f;
        }
        if (i < mIndicatorPosition) {
            valueAnimator = ValueAnimator.ofFloat(new float[]{f, 0.0f});
        } else {
            valueAnimator = ValueAnimator.ofFloat(new float[]{-f, 0.0f});
        }
        valueAnimator.setDuration(DEFAULT_SCROLL_DURATION);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                scrollToTab(i, ((Float) valueAnimator.getAnimatedValue()).floatValue(), true);
            }
        });
        valueAnimator.start();
    }


    public void scrollToTab(int i) {
        scrollToTab(i, 0.0f, false);
        mAdapter.setCurrentIndicatorPosition(i);
        mAdapter.notifyDataSetChanged();
    }


    public void scrollToTab(int i, float f, boolean z) {
        int i2 = 0;
        int i3;
        float f2;
        View findViewByPosition = mLinearLayoutManager.findViewByPosition(i);
        View findViewByPosition2 = mLinearLayoutManager.findViewByPosition(i + 1);
        if (findViewByPosition != null) {
            int measuredWidth = getMeasuredWidth();
            if (i == 0) {
                f2 = 0.0f;
            } else {
                f2 = (((float) measuredWidth) / 2.0f) - (((float) findViewByPosition.getMeasuredWidth()) / 2.0f);
            }
            float measuredWidth2 = ((float) findViewByPosition.getMeasuredWidth()) + f2;
            if (findViewByPosition2 != null) {
                float measuredWidth3 = (measuredWidth2 - ((((float) measuredWidth) / 2.0f) - (((float) findViewByPosition2.getMeasuredWidth()) / 2.0f))) * f;
                i2 = (int) (f2 - measuredWidth3);
                if (i == 0) {
                    float measuredWidth4 = (float) ((findViewByPosition2.getMeasuredWidth() - findViewByPosition.getMeasuredWidth()) / 2);
                    mIndicatorGap = (int) (measuredWidth4 * f);
                    mIndicatorScroll = (int) ((((float) findViewByPosition.getMeasuredWidth()) + measuredWidth4) * f);
                } else {
                    mIndicatorGap = (int) (((float) ((findViewByPosition2.getMeasuredWidth() - findViewByPosition.getMeasuredWidth()) / 2)) * f);
                    mIndicatorScroll = (int) measuredWidth3;
                }
            } else {
                i2 = (int) f2;
                mIndicatorScroll = 0;
                mIndicatorGap = 0;
            }
            if (z) {
                mIndicatorScroll = 0;
                mIndicatorGap = 0;
            }
        } else {
            if (getMeasuredWidth() <= 0 || mTabMaxWidth <= 0 || mTabMinWidth != mTabMaxWidth) {
                i3 = 0;
            } else {
                int i4 = mTabMinWidth;
                i3 = ((int) (((float) (-i4)) * f)) + ((int) (((float) (getMeasuredWidth() - i4)) / 2.0f));
            }
            mRequestScrollToTab = true;
        }
        updateCurrentIndicatorPosition(i, f - mOldPositionOffset, f);
        mIndicatorPosition = i;
        stopScroll();
        if (!(i == mOldPosition && i2 == mOldScrollOffset)) {
            mLinearLayoutManager.scrollToPositionWithOffset(i, i2);
        }
        if (mIndicatorHeight > 0) {
            invalidate();
        }
        mOldPosition = i;
        mOldScrollOffset = i2;
        mOldPositionOffset = f;
    }


    public void updateCurrentIndicatorPosition(int i, float f, float f2) {
        if (mAdapter != null) {
            if (f > 0.0f && f2 >= mPositionThreshold - POSITION_THRESHOLD_ALLOWABLE) {
                i++;
            } else if (f >= 0.0f || f2 > (1.0f - mPositionThreshold) + POSITION_THRESHOLD_ALLOWABLE) {
                i = -1;
            }
            if (i >= 0 && i != mAdapter.getCurrentIndicatorPosition()) {
                mAdapter.setCurrentIndicatorPosition(i);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    public void onDraw(Canvas canvas) {
        int i;
        int i2;
        View findViewByPosition = mLinearLayoutManager.findViewByPosition(mIndicatorPosition);
        if (findViewByPosition != null) {
            mRequestScrollToTab = false;
            if (isLayoutRtl()) {
                i = (findViewByPosition.getLeft() - mIndicatorScroll) - mIndicatorGap;
                i2 = (findViewByPosition.getRight() - mIndicatorScroll) + mIndicatorGap;
            } else {
                i = (findViewByPosition.getLeft() + mIndicatorScroll) - mIndicatorGap;
                i2 = findViewByPosition.getRight() + mIndicatorScroll + mIndicatorGap;
            }
            Canvas canvas2 = canvas;
            canvas2.drawRect((float) i, (float) (getHeight() - mIndicatorHeight), (float) i2, (float) getHeight(), mIndicatorPaint);
        } else if (mRequestScrollToTab) {
            mRequestScrollToTab = false;
            scrollToTab(mViewPager.getCurrentItem());
        }
    }


    public boolean isLayoutRtl() {
        return ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL;
    }

    protected static class RecyclerOnScrollListener extends OnScrollListener {
        public int mDx;
        protected LinearLayoutManager mLinearLayoutManager;
        protected RecyclerTabLayout mRecyclerTabLayout;

        public RecyclerOnScrollListener(RecyclerTabLayout recyclerTabLayout, LinearLayoutManager linearLayoutManager) {
            mRecyclerTabLayout = recyclerTabLayout;
            mLinearLayoutManager = linearLayoutManager;
        }

        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            mDx += i;
        }

        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            if (i == 0) {
                if (mDx > 0) {
                    selectCenterTabForRightScroll();
                } else {
                    selectCenterTabForLeftScroll();
                }
                mDx = 0;
            }
        }


        public void selectCenterTabForRightScroll() {
            int findLastVisibleItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();
            int width = mRecyclerTabLayout.getWidth() / 2;
            for (int findFirstVisibleItemPosition = mLinearLayoutManager.findFirstVisibleItemPosition(); findFirstVisibleItemPosition <= findLastVisibleItemPosition; findFirstVisibleItemPosition++) {
                View findViewByPosition = mLinearLayoutManager.findViewByPosition(findFirstVisibleItemPosition);
                if (findViewByPosition.getLeft() + findViewByPosition.getWidth() >= width) {
                    mRecyclerTabLayout.setCurrentItem(findFirstVisibleItemPosition, false);
                    return;
                }
            }
        }


        public void selectCenterTabForLeftScroll() {
            int findFirstVisibleItemPosition = mLinearLayoutManager.findFirstVisibleItemPosition();
            int width = mRecyclerTabLayout.getWidth() / 2;
            for (int findLastVisibleItemPosition = mLinearLayoutManager.findLastVisibleItemPosition(); findLastVisibleItemPosition >= findFirstVisibleItemPosition; findLastVisibleItemPosition--) {
                if (mLinearLayoutManager.findViewByPosition(findLastVisibleItemPosition).getLeft() <= width) {
                    mRecyclerTabLayout.setCurrentItem(findLastVisibleItemPosition, false);
                    return;
                }
            }
        }
    }

    protected static class ViewPagerOnPageChangeListener implements ViewPager.OnPageChangeListener {
        private final RecyclerTabLayout mRecyclerTabLayout;
        private int mScrollState;

        public ViewPagerOnPageChangeListener(RecyclerTabLayout recyclerTabLayout) {
            mRecyclerTabLayout = recyclerTabLayout;
        }

        public void onPageScrolled(int i, float f, int i2) {
            mRecyclerTabLayout.scrollToTab(i, f, false);
        }

        public void onPageScrollStateChanged(int i) {
            mScrollState = i;
        }

        public void onPageSelected(int i) {
            if (mScrollState == 0 && mRecyclerTabLayout.mIndicatorPosition != i) {
                mRecyclerTabLayout.scrollToTab(i);
            }
        }
    }

    public static abstract class Adapter<T extends ViewHolder> extends RecyclerView.Adapter<T> {
        protected int mIndicatorPosition;
        protected ViewPager mViewPager;

        public Adapter(ViewPager viewPager) {
            mViewPager = viewPager;
        }

        public ViewPager getViewPager() {
            return mViewPager;
        }

        public void setCurrentIndicatorPosition(int i) {
            mIndicatorPosition = i;
        }

        public int getCurrentIndicatorPosition() {
            return mIndicatorPosition;
        }
    }

    public static class DefaultAdapter extends Adapter<DefaultAdapter.ViewHolder> {
        protected static final int MAX_TAB_TEXT_LINES = 2;
        private int mTabBackgroundResId;
        private int mTabMaxWidth;
        private int mTabMinWidth;
        private int mTabOnScreenLimit;
        protected int mTabPaddingBottom;
        protected int mTabPaddingEnd;
        protected int mTabPaddingStart;
        protected int mTabPaddingTop;
        protected int mTabSelectedTextColor;
        protected boolean mTabSelectedTextColorSet;
        protected int mTabTextAppearance;

        public DefaultAdapter(ViewPager viewPager) {
            super(viewPager);
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            TabTextView tabTextView = new TabTextView(viewGroup.getContext());
            if (mTabSelectedTextColorSet) {
                tabTextView.setTextColor(tabTextView.createColorStateList(tabTextView.getCurrentTextColor(), mTabSelectedTextColor));
            }
            ViewCompat.setPaddingRelative(tabTextView, mTabPaddingStart, mTabPaddingTop, mTabPaddingEnd, mTabPaddingBottom);
            tabTextView.setTextAppearance(viewGroup.getContext(), mTabTextAppearance);
            tabTextView.setGravity(17);
            tabTextView.setMaxLines(2);
            tabTextView.setEllipsize(TextUtils.TruncateAt.END);
            if (mTabOnScreenLimit > 0) {
                int measuredWidth = viewGroup.getMeasuredWidth() / mTabOnScreenLimit;
                tabTextView.setMaxWidth(measuredWidth);
                tabTextView.setMinWidth(measuredWidth);
            } else {
                if (mTabMaxWidth > 0) {
                    tabTextView.setMaxWidth(mTabMaxWidth);
                }
                tabTextView.setMinWidth(mTabMinWidth);
            }
            tabTextView.setTextAppearance(tabTextView.getContext(), mTabTextAppearance);
            if (mTabSelectedTextColorSet) {
                tabTextView.setTextColor(tabTextView.createColorStateList(tabTextView.getCurrentTextColor(), mTabSelectedTextColor));
            }
            if (mTabBackgroundResId != 0) {
                tabTextView.setBackgroundDrawable(AppCompatResources.getDrawable(tabTextView.getContext(), mTabBackgroundResId));
            }
            tabTextView.setLayoutParams(createLayoutParamsForTabs());
            return new ViewHolder(tabTextView);
        }

        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            viewHolder.title.setText(getViewPager().getAdapter().getPageTitle(i));
            viewHolder.title.setSelected(getCurrentIndicatorPosition() == i);
        }

        public int getItemCount() {
            return getViewPager().getAdapter().getCount();
        }

        public void setTabPadding(int i, int i2, int i3, int i4) {
            mTabPaddingStart = i;
            mTabPaddingTop = i2;
            mTabPaddingEnd = i3;
            mTabPaddingBottom = i4;
        }

        public void setTabTextAppearance(int i) {
            mTabTextAppearance = i;
        }

        public void setTabSelectedTextColor(boolean z, int i) {
            mTabSelectedTextColorSet = z;
            mTabSelectedTextColor = i;
        }

        public void setTabMaxWidth(int i) {
            mTabMaxWidth = i;
        }

        public void setTabMinWidth(int i) {
            mTabMinWidth = i;
        }

        public void setTabBackgroundResId(int i) {
            mTabBackgroundResId = i;
        }

        public void setTabOnScreenLimit(int i) {
            mTabOnScreenLimit = i;
        }


        public LayoutParams createLayoutParamsForTabs() {
            return new LayoutParams(-2, -1);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView title;

            public ViewHolder(View view) {
                super(view);
                title = (TextView) view;
                view.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        int adapterPosition = getAdapterPosition();
                        if (adapterPosition != -1) {
                            getViewPager().setCurrentItem(adapterPosition, true);
                        }
                    }
                });
            }
        }
    }

    public static class TabTextView extends AppCompatTextView {
        public TabTextView(Context context) {
            super(context);
        }

        public ColorStateList createColorStateList(int i, int i2) {
            return new ColorStateList(new int[][]{SELECTED_STATE_SET, EMPTY_STATE_SET}, new int[]{i2, i});
        }
    }
}
