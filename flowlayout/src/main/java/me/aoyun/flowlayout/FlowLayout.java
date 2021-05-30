package me.aoyun.flowlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup {
    /**
     * Special value for the child view spacing.
     * SPACING_AUTO means that the actual spacing is calculated according to the size of the
     * container and the number of the child views, so that the child views are placed evenly in
     * the container.
     */
    public static final int SPACING_AUTO = -65536;

    /**
     * Special value for the horizontal spacing of the child views in the last row
     * SPACING_ALIGN means that the horizontal spacing of the child views in the last row keeps
     * the same with the spacing used in the row above. If there is only one row, this value is
     * ignored and the spacing will be calculated according to childSpacing.
     */
    public static final int SPACING_ALIGN = -65537;

    private static final int SPACING_UNDEFINED = -65538;

    private static final int UNSPECIFIED_GRAVITY = -1;

    private static final int ROW_VERTICAL_GRAVITY_AUTO = -65536;

    private static final boolean DEFAULT_FLOW = true;
    private static final int DEFAULT_CHILD_SPACING = 0;
    private static final int DEFAULT_CHILD_SPACING_FOR_LAST_ROW = SPACING_UNDEFINED;
    private static final float DEFAULT_ROW_SPACING = 0;
    private static final boolean DEFAULT_RTL = false;
    private static final int DEFAULT_MAX_ROWS = Integer.MAX_VALUE;

    private boolean mFlow = DEFAULT_FLOW;
    private int mChildSpacing = DEFAULT_CHILD_SPACING;
    private int mMinChildSpacing = DEFAULT_CHILD_SPACING;
    private int mChildSpacingForLastRow = DEFAULT_CHILD_SPACING_FOR_LAST_ROW;
    private float mRowSpacing = DEFAULT_ROW_SPACING;
    private float mAdjustedRowSpacing = DEFAULT_ROW_SPACING;
    private boolean mRtl = DEFAULT_RTL;
    private int mMaxRows = DEFAULT_MAX_ROWS;
    private int mGravity = UNSPECIFIED_GRAVITY;
    private int mRowVerticalGravity = ROW_VERTICAL_GRAVITY_AUTO;
    private int mExactMeasuredHeight;

    private List<Float> mHorizontalSpacingForRow = new ArrayList<>();
    private List<Integer> mHeightForRow = new ArrayList<>();
    private List<Integer> mWidthForRow = new ArrayList<>();
    private List<Integer> mChildForRow = new ArrayList<>();



    public FlowLayout(Context context) {
        super(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FlowLayout, 0, 0);

        mFlow = a.getBoolean(R.styleable.FlowLayout_flFlow, DEFAULT_FLOW);
        mChildSpacing = getDimensionOrInt(a, R.styleable.FlowLayout_flChildSpacing, (int) dpToPx(DEFAULT_CHILD_SPACING));
        mMinChildSpacing = getDimensionOrInt(a, R.styleable.FlowLayout_flMinChildSpacing, (int) dpToPx(DEFAULT_CHILD_SPACING));
        mChildSpacingForLastRow = getDimensionOrInt(a, R.styleable.FlowLayout_flChildSpacingForLastRow, SPACING_UNDEFINED);
        mRowSpacing = getDimensionOrInt(a, R.styleable.FlowLayout_flRowSpacing, (int) dpToPx(DEFAULT_ROW_SPACING));
        mMaxRows = a.getInt(R.styleable.FlowLayout_flMaxRows, DEFAULT_MAX_ROWS);
        mRtl = a.getBoolean(R.styleable.FlowLayout_flRtl, DEFAULT_RTL);
        mGravity = a.getInt(R.styleable.FlowLayout_android_gravity, UNSPECIFIED_GRAVITY);
        mRowVerticalGravity = a.getInt(R.styleable.FlowLayout_flRowVerticalGravity, ROW_VERTICAL_GRAVITY_AUTO);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int measuredHeight = 0;
        int measuredWidth = 0;
        int childCount = getChildCount();
        int rowWidth = 0;
        int maxChildHeightInRow = 0;
        int childNumInRow = 0;
        int rowSize = widthSize - getPaddingLeft() - getPaddingRight();
        int rowTotalChildWidth = 0;
        boolean allowFlow = widthMode != MeasureSpec.UNSPECIFIED && mFlow;
        int childSpacing = mChildSpacing == SPACING_AUTO && widthMode == MeasureSpec.UNSPECIFIED
                ? 0 : mChildSpacing;
        float tmpSpacing = childSpacing == SPACING_AUTO ? mMinChildSpacing : childSpacing;

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }

            LayoutParams childLayoutParams = child.getLayoutParams();
            int horizontalMargin = 0;
            int verticalMargin = 0;
            if (childLayoutParams instanceof MarginLayoutParams) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, measuredHeight);
                MarginLayoutParams marginLayoutParams = (MarginLayoutParams) childLayoutParams;
                horizontalMargin = marginLayoutParams.leftMargin + marginLayoutParams.rightMargin;
                verticalMargin = marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;

            } else {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
            }
        }
    }

    private int getDimensionOrInt(TypedArray a, int index, int defValue) {
        TypedValue typedValue = new TypedValue();
        a.getValue(index, typedValue);
        if (typedValue.type == TypedValue.TYPE_DIMENSION) {
            return a.getDimensionPixelSize(index, defValue);
        } else {
            return a.getInt(index, defValue);
        }

    }

    private float dpToPx(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
