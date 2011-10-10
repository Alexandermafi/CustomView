package com.enea.training.customview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class CustomSlider extends View {

  private final Drawable               mIndicator;
  private final Drawable               mBackground;
  private float                        mMin;
  private float                        mMax;
  private float                        mPosition;
  private Rect                         mViewRect;
  private int                          mIndicatorOffset;
  private int                          mIndicatorMaxPos;
  private int                          mIndicatorMinPos;

  public CustomSlider(final Context context) {
    this(context, null, 0);
  }

  public CustomSlider(final Context context, final AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CustomSlider(final Context context, final AttributeSet attrs,
      final int defStyle) {
    super(context, attrs, defStyle);

    final Resources res = context.getResources();
    mIndicator = res.getDrawable(R.drawable.indicator_horizontal);
    mBackground = res.getDrawable(R.drawable.background_horizontal);

    mMin = -1.0f;
    mMax = 1.0f;
    mPosition = (mMax - mMin) / 2 + mMin;
  }

  @Override
  protected void onDraw(final Canvas canvas) {
    if (mViewRect == null) {
      mViewRect = new Rect();
      getDrawingRect(mViewRect);
      mIndicatorOffset = mIndicator.getIntrinsicWidth();
      mIndicatorMaxPos = mViewRect.right - mIndicatorOffset;
      mIndicatorMinPos = mViewRect.left + mIndicatorOffset;
      mBackground.setBounds(mViewRect.left, mViewRect.top, mViewRect.right,
          mViewRect.bottom);
    }

    final float pos;
    final int left;
    final int right;
    final int top;
    final int bottom;
    pos = mIndicatorMinPos
        + ((mIndicatorMaxPos - mIndicatorMinPos) / (mMax - mMin))
        * (mPosition - mMin);
    left = (int) pos - (mIndicator.getIntrinsicWidth() / 2);
    top = mViewRect.centerY() - (mIndicator.getIntrinsicHeight() / 2);
    right = left + mIndicator.getIntrinsicWidth();
    bottom = top + mIndicator.getIntrinsicHeight();
    mIndicator.setBounds(left, top, right, bottom);

    mBackground.draw(canvas);
    mIndicator.draw(canvas);
  }

  @Override
  protected void onMeasure(final int widthMeasureSpec,
      final int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    setMeasuredDimension(getMeasuredWidth(), mIndicator.getIntrinsicHeight());
  }

  public float getMin() {
    return mMin;
  }

  public float getMax() {
    return mMax;
  }

  public float getPosition() {
    return mPosition;
  }

  public void setPosition(float position) {
    position = within(position, mMin, mMax);
    if (position != mPosition) {
      mPosition = position;
      invalidate();
    }
  }

  private static float within(float position, final float min, final float max) {
    if (position < min) {
      position = min;
    }
    if (position > max) {
      position = max;
    }
    return position;
  }

  public void setMinMax(final float min, final float max) {
    if ((min != mMin) || (max != mMax)) {
      if (min > max) {
        throw new IllegalArgumentException(
            "setMinMax: min must be smaller than max.");
      }
      mMin = min;
      mMax = max;
      setPosition(mPosition);
      invalidate();
    }
  }

}
