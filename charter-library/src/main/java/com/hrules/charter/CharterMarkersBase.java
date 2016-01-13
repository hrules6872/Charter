package com.hrules.charter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.util.AttributeSet;
import android.view.View;

class CharterMarkersBase extends View {
  Paint paintMarker;
  Paint paintSeparator;
  boolean[] visibilityPattern;
  int markersCount;
  boolean stickyEdges;

  protected CharterMarkersBase(Context context) {
    this(context, null);
  }

  protected CharterMarkersBase(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  protected CharterMarkersBase(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  protected CharterMarkersBase(Context context, AttributeSet attrs, int defStyleAttr,
      int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    if (isInEditMode()) {
      return;
    }

    final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Charter);
    Resources res = getResources();
    stickyEdges = typedArray.getBoolean(R.styleable.Charter_c_stickyEdges,
        res.getBoolean(R.bool.default_stickyEdges));
    float paintMarkerStrokeSize = typedArray.getDimension(R.styleable.Charter_c_markerStrokeSize,
        getResources().getDimension(R.dimen.default_markerStrokeSize));
    int paintMarkerColor = typedArray.getColor(R.styleable.Charter_c_markerColor,
        res.getColor(R.color.default_markerColor));
    float paintSeparatorStrokeSize =
        typedArray.getDimension(R.styleable.Charter_c_separatorStrokeSize,
            getResources().getDimension(R.dimen.default_separatorStrokeSize));
    int paintSeparatorColor = typedArray.getColor(R.styleable.Charter_c_separatorColor,
        res.getColor(R.color.default_separatorColor));
    typedArray.recycle();

    paintMarker = new Paint();
    paintMarker.setAntiAlias(true);
    paintMarker.setColor(paintMarkerColor);
    paintMarker.setStrokeWidth(paintMarkerStrokeSize);
    paintMarker.setStyle(Paint.Style.STROKE);

    paintSeparator = new Paint();
    paintSeparator.setAntiAlias(true);
    paintSeparator.setColor(paintSeparatorColor);
    paintSeparator.setStrokeWidth(paintSeparatorStrokeSize);
    paintSeparator.setStyle(Paint.Style.STROKE);

    visibilityPattern = new boolean[] { true };
  }

  public Paint getPaintMarker() {
    return paintMarker;
  }

  public void setPaintMarker(@NonNull Paint paintMarker) {
    this.paintMarker = paintMarker;
  }

  public Paint getPaintSeparator() {
    return paintSeparator;
  }

  public void setPaintSeparator(@NonNull Paint paintSeparator) {
    this.paintSeparator = paintSeparator;
  }

  public boolean[] getVisibilityPattern() {
    return visibilityPattern;
  }

  public void setVisibilityPattern(@NonNull @Size(min = 1) boolean[] visibilityPattern) {
    this.visibilityPattern = visibilityPattern;
    invalidate();
  }

  public boolean isStickyEdges() {
    return stickyEdges;
  }

  public void setStickyEdges(boolean stickyEdges) {
    this.stickyEdges = stickyEdges;
    invalidate();
  }

  public int getMarkerColor() {
    return paintMarker.getColor();
  }

  public void setMarkerColor(@ColorInt int labelColor) {
    paintMarker.setColor(labelColor);
    invalidate();
  }

  public int getSeparatorColor() {
    return paintSeparator.getColor();
  }

  public void setSeparatorColor(@ColorInt int labelColor) {
    paintSeparator.setColor(labelColor);
    invalidate();
  }

  public float getMarkerStrokeSize() {
    return paintMarker.getStrokeWidth();
  }

  public void setMarkerStrokeSize(float strokeSize) {
    paintMarker.setStrokeWidth(strokeSize);
    invalidate();
  }

  public float getSeparatorStrokeSize() {
    return paintSeparator.getStrokeWidth();
  }

  public void setSeparatorStrokeSize(float strokeSize) {
    paintSeparator.setStrokeWidth(strokeSize);
    invalidate();
  }

  public int getMarkersCount() {
    return markersCount;
  }

  public void setMarkersCount(int markersCount) {
    if (markersCount < 0) {
      markersCount = 0;
    }
    this.markersCount = markersCount;
  }
}
