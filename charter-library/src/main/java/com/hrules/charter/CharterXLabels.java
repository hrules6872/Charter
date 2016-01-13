package com.hrules.charter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;

public class CharterXLabels extends CharterLabelsBase {
  public CharterXLabels(Context context) {
    this(context, null);
  }

  public CharterXLabels(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CharterXLabels(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public CharterXLabels(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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

    final float gap = stickyEdges ? width / (valuesLength - 1) : width / valuesLength;

    int visibilityPatternPos = -1;
    float x;
    float y;

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

        switch (verticalGravity) {
          case VERTICAL_GRAVITY_TOP:
            y = 0;
            break;

          case VERTICAL_GRAVITY_BOTTOM:
            y = height - textHeight;
            break;

          default:
            // VERTICAL_GRAVITY_CENTER
            y = (height + textHeight) / 2;
            break;
        }

        if (stickyEdges) {
          if (i == 0) {
            x = 0;
          } else if (i == valuesLength - 1) {
            x = width - textWidth;
          } else {
            x = gap * (i - 1) + gap - (textWidth / 2);
          }
        } else {
          x = gap * i + (gap / 2) - (textWidth / 2);
        }
        canvas.drawText(values[i], x, y, paintLabel);
      }
    }
  }
}
