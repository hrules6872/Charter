package com.hrules.charter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;

public class CharterYLabels extends CharterLabelsBase {
  public CharterYLabels(Context context) {
    this(context, null);
  }

  public CharterYLabels(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CharterYLabels(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public CharterYLabels(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  @Override public void draw(Canvas canvas) {
    super.draw(canvas);

    if (values == null || values.length == 0) {
      return;
    }

    final int valuesLength = values.length;

    final float height = getMeasuredHeight();
    final float width = getMeasuredWidth();

    final float gap = height / (valuesLength - 1);

    int visibilityPatternPos = -1;

    for (int i = 0; i < valuesLength; i++) {
      if (visibilityPatternPos + 1 >= visibilityPattern.length) {
        visibilityPatternPos = 0;
      } else {
        visibilityPatternPos++;
      }

      if (visibilityPattern[visibilityPatternPos]) {
        Rect textBounds = new Rect();
        paintLabel.getTextBounds(values[i], 0, values[i].length(), textBounds);
        int textHeight = 2 * textBounds.bottom - textBounds.top;
        float textWidth = textBounds.right;

        float x;
        float y;

        switch (horizontalGravity) {
          default:
            // HORIZONTAL_GRAVITY_LEFT
            x = 0;
            break;

          case HORIZONTAL_GRAVITY_CENTER:
            x = (width - textWidth) / 2;
            break;

          case HORIZONTAL_GRAVITY_RIGHT:
            x = width - textWidth;
            break;
        }

        if (i == 0) {
          y = height;
        } else if (i == valuesLength - 1) {
          y = textHeight;
        } else {
          y = gap * i + (textHeight / 2);
        }

        canvas.drawText(values[i], x, y, paintLabel);
      }
    }
  }
}
