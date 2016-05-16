package csteachingtips.csteachingtinder;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.andtinder.model.Orientations;
import com.andtinder.view.CardContainer;


public class CardPile extends CardContainer {

    private static final double DISORDERED_MAX_ROTATION_RADIANS = Math.PI / 64;

    public CardPile(Context context) {
        super(context);
        setOrientation(Orientations.Orientation.Ordered);
    }

    public CardPile(Context context, AttributeSet attrs) {

        super(context, attrs);
        setOrientation(Orientations.Orientation.Ordered);
    }


    public CardPile(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOrientation(Orientations.Orientation.Ordered);
    }


    //Set the size of the tip based on the size of the screen.
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int requestedWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int requestedHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        int childWidth, childHeight;
        if (getOrientation() == Orientations.Orientation.Disordered) {
            childWidth = (int) ((requestedWidth * Math.cos(DISORDERED_MAX_ROTATION_RADIANS) - requestedHeight * Math.sin(DISORDERED_MAX_ROTATION_RADIANS)) / Math.cos(2 * DISORDERED_MAX_ROTATION_RADIANS));
            childHeight = (int) ((requestedHeight * Math.cos(DISORDERED_MAX_ROTATION_RADIANS) - requestedWidth * Math.sin(DISORDERED_MAX_ROTATION_RADIANS)) / Math.cos(2 * DISORDERED_MAX_ROTATION_RADIANS));
        } else {
            childWidth = requestedWidth;
            childHeight = requestedHeight;
        }
        int childWidthMeasureSpec, childHeightMeasureSpec;
        childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.AT_MOST);
        childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.AT_MOST);

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            assert child != null;
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }
    }
}

