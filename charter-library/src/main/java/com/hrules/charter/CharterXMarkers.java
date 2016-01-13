package com.hrules.charter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

public class CharterXMarkers extends CharterMarkersBase {
  private float widthCorrection;

  public CharterXMarkers(Context context) {
    this(context, null);
  }

  public CharterXMarkers(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CharterXMarkers(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public CharterXMarkers(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init(context, attrs);
  }

  private void init(final Context context, final AttributeSet attrs) {
    final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Charter);
    widthCorrection = typedArray.getDimension(R.styleable.Charter_c_widthCorrection,
        getResources().getDimension(R.dimen.default_markerWidthCorrection));
    typedArray.recycle();
  }

  public float getWidthCorrection() {
    return widthCorrection;
  }

  public void setWidthCorrection(float widthCorrection) {
    if (widthCorrection < 0) {
      widthCorrection = 0;
    }
    this.widthCorrection = widthCorrection;
    invalidate();
  }

  public void setWidthCorrectionFromCharterLine(@NonNull CharterLine charterLine) {
    setWidthCorrection((charterLine.getStrokeSize() + charterLine.getIndicatorSize()) / 2);
  }

  @Override public void draw(Canvas canvas) {
    super.draw(canvas);

    if (markersCount == 0) {
      return;
    }

    final float markerStrokeWidth = paintMarker.getStrokeWidth();

    final float height = getMeasuredHeight();
    final float width = getMeasuredWidth();

    final float gap =
        stickyEdges ? (width - (widthCorrection * 2)) / (markersCount - 1) : width / markersCount;

    int visibilityPatternPos = -1;
    float x;
    final float y = 0;

    for (int i = 0; i < markersCount; i++) {
      if (visibilityPatternPos + 1 >= visibilityPattern.length) {
        visibilityPatternPos = 0;
      } else {
        visibilityPatternPos++;
      }

      if (visibilityPattern[visibilityPatternPos]) {
        if (stickyEdges) {
          if (i == 0) {
            x = (markerStrokeWidth / 2) + widthCorrection;
          } else if (i == markersCount - 1) {
            x = width - (markerStrokeWidth / 2) - widthCorrection;
          } else {
            x = gap * (i - 1) + gap - (markerStrokeWidth / 4) + widthCorrection;
          }
        } else {
          x = gap * i + (gap / 2) - (markerStrokeWidth / 2);
        }
        canvas.drawLine(x, y, x, y + height, paintMarker);
      }
    }

    float separatorStrokeWidth = paintSeparator.getStrokeWidth();
    if (separatorStrokeWidth > 0) {
      canvas.drawLine(widthCorrection, separatorStrokeWidth / 2, width - widthCorrection,
          separatorStrokeWidth / 2, paintSeparator);
    }
  }
}
