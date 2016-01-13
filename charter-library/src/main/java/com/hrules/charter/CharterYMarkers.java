package com.hrules.charter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

public class CharterYMarkers extends CharterMarkersBase {
  private float heightCorrection;

  public CharterYMarkers(Context context) {
    this(context, null);
  }

  public CharterYMarkers(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CharterYMarkers(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public CharterYMarkers(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init(context, attrs);
  }

  private void init(final Context context, final AttributeSet attrs) {
    final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Charter);
    heightCorrection = typedArray.getDimension(R.styleable.Charter_c_heightCorrection,
        getResources().getDimension(R.dimen.default_markerWidthCorrection));
    typedArray.recycle();
  }

  public float getHeightCorrection() {
    return heightCorrection;
  }

  public void setHeightCorrection(float heightCorrection) {
    if (heightCorrection < 0) {
      heightCorrection = 0;
    }
    this.heightCorrection = heightCorrection;
    invalidate();
  }

  public void setHeightCorrectionFromCharterLine(@NonNull CharterLine charterLine) {
    setHeightCorrection((charterLine.getStrokeSize() + charterLine.getIndicatorSize()) / 2);
  }

  @Override public void draw(Canvas canvas) {
    super.draw(canvas);

    if (markersCount == 0) {
      return;
    }

    final float markerStrokeHeight = paintMarker.getStrokeWidth();

    final float height = getMeasuredHeight();
    final float width = getMeasuredWidth();

    final float gap = stickyEdges ? (height - (heightCorrection * 2)) / (markersCount - 1)
        : height / markersCount;

    int visibilityPatternPos = -1;
    final float x = 0;
    float y;

    for (int i = 0; i < markersCount; i++) {
      if (visibilityPatternPos + 1 >= visibilityPattern.length) {
        visibilityPatternPos = 0;
      } else {
        visibilityPatternPos++;
      }

      if (visibilityPattern[visibilityPatternPos]) {
        if (stickyEdges) {
          if (i == 0) {
            y = (markerStrokeHeight / 2) + heightCorrection;
          } else if (i == markersCount - 1) {
            y = height - (markerStrokeHeight / 2) - heightCorrection;
          } else {
            y = gap * (i - 1) + gap - (markerStrokeHeight / 4) + heightCorrection;
          }
        } else {
          y = gap * i + (gap / 2) - (markerStrokeHeight / 2);
        }
        canvas.drawLine(x, y, x + width, y, paintMarker);
      }
    }

    float separatorStrokeWidth = paintSeparator.getStrokeWidth();
    if (separatorStrokeWidth > 0) {
      canvas.drawLine(width - separatorStrokeWidth / 2, heightCorrection,
          width - separatorStrokeWidth / 2, height - heightCorrection, paintSeparator);
    }
  }
}
