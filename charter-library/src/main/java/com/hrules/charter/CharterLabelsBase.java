package com.hrules.charter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.View;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

class CharterLabelsBase extends View {
  public static final int VERTICAL_GRAVITY_TOP = 0;
  public static final int VERTICAL_GRAVITY_CENTER = 1;
  public static final int VERTICAL_GRAVITY_BOTTOM = 2;
  public static final int HORIZONTAL_GRAVITY_LEFT = 0;
  public static final int HORIZONTAL_GRAVITY_CENTER = 1;
  public static final int HORIZONTAL_GRAVITY_RIGHT = 2;
  private static final int DEFAULT_VERTICAL_GRAVITY = VERTICAL_GRAVITY_CENTER;
  private static final int DEFAULT_HORIZONTAL_GRAVITY = HORIZONTAL_GRAVITY_LEFT;
  private static final boolean DEFAULT_STICKY_EDGES = false;

  Paint paintLabel;
  boolean[] visibilityPattern;
  int verticalGravity;
  int horizontalGravity;
  String[] values;
  boolean stickyEdges;

  protected CharterLabelsBase(Context context) {
    this(context, null);
  }

  protected CharterLabelsBase(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  protected CharterLabelsBase(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  protected CharterLabelsBase(Context context, AttributeSet attrs, int defStyleAttr,
      int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    if (isInEditMode()) {
      return;
    }

    final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Charter);
    stickyEdges = typedArray.getBoolean(R.styleable.Charter_c_stickyEdges, DEFAULT_STICKY_EDGES);
    verticalGravity =
        typedArray.getInt(R.styleable.Charter_c_verticalGravity, DEFAULT_VERTICAL_GRAVITY);
    horizontalGravity =
        typedArray.getInt(R.styleable.Charter_c_horizontalGravity, DEFAULT_HORIZONTAL_GRAVITY);
    int paintLabelColor = typedArray.getColor(R.styleable.Charter_c_labelColor,
        getResources().getColor(R.color.default_labelColor));
    float paintLabelSize = typedArray.getDimension(R.styleable.Charter_c_labelSize,
        getResources().getDimension(R.dimen.default_labelSize));
    typedArray.recycle();

    paintLabel = new Paint();
    paintLabel.setAntiAlias(true);
    paintLabel.setColor(paintLabelColor);
    paintLabel.setTextSize(paintLabelSize);

    visibilityPattern = new boolean[] { true };
  }

  public boolean isStickyEdges() {
    return stickyEdges;
  }

  public void setStickyEdges(boolean stickyEdges) {
    this.stickyEdges = stickyEdges;
    invalidate();
  }

  public Paint getPaintLabel() {
    return paintLabel;
  }

  public void setPaintLabel(Paint paintLabel) {
    this.paintLabel = paintLabel;
    invalidate();
  }

  public boolean[] getVisibilityPattern() {
    return visibilityPattern;
  }

  public void setVisibilityPattern(boolean[] visibilityPattern) {
    this.visibilityPattern = visibilityPattern;
    invalidate();
  }

  public int getVerticalGravity() {
    return verticalGravity;
  }

  public void setVerticalGravity(@VerticalGravity int verticalGravity) {
    this.verticalGravity = verticalGravity;
    invalidate();
  }

  public int getHorizontalGravity() {
    return horizontalGravity;
  }

  public void setHorizontalGravity(@HorizontalGravity int horizontalGravity) {
    this.horizontalGravity = horizontalGravity;
    invalidate();
  }

  public int getLabelColor() {
    return paintLabel.getColor();
  }

  public void setLabelColor(@ColorInt int labelColor) {
    paintLabel.setColor(labelColor);
    invalidate();
  }

  public float getLabelSize() {
    return paintLabel.getTextSize();
  }

  public void setLabelSize(float labelSize) {
    paintLabel.setTextSize(labelSize);
    invalidate();
  }

  public void setLabelTypeface(Typeface typeface) {
    paintLabel.setTypeface(typeface);
    invalidate();
  }

  public String[] getValues() {
    return values;
  }

  public void setValues(float[] values) {
    setValues(floatArrayToStringArray(values));
  }

  public void setValues(String[] values) {
    if (values == null || values.length == 0) {
      return;
    }

    this.values = values;
    invalidate();
  }

  public void setValues(float[] values, boolean summarize) {
    if (summarize) {
      values = summarize(values);
    }
    setValues(floatArrayToStringArray(values));
  }

  private String[] floatArrayToStringArray(float[] values) {
    if (values == null) {
      return new String[] {};
    }

    String[] stringArray = new String[values.length];
    for (int i = 0; i < stringArray.length; i++) {
      stringArray[i] = String.valueOf((int) values[i]);
    }
    return stringArray;
  }

  private float[] summarize(float[] values) {
    if (values == null) {
      return new float[] {};
    }

    float max = values[0];
    float min = values[0];
    for (float value : values) {
      if (value > max) {
        max = value;
      }
      if (value < min) {
        min = value;
      }
    }
    float diff = max - min;

    return new float[] { min, diff / 5, diff / 2, (diff / 5) * 4, max };
  }

  @Retention(RetentionPolicy.SOURCE)
  @IntDef({ VERTICAL_GRAVITY_TOP, VERTICAL_GRAVITY_CENTER, VERTICAL_GRAVITY_BOTTOM })
  public @interface VerticalGravity {
  }

  @Retention(RetentionPolicy.SOURCE)
  @IntDef({ HORIZONTAL_GRAVITY_LEFT, HORIZONTAL_GRAVITY_CENTER, HORIZONTAL_GRAVITY_RIGHT })
  public @interface HorizontalGravity {
  }
}
