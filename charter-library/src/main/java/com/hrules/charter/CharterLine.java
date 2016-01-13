package com.hrules.charter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public class CharterLine extends CharterBase {
  private static final float DEFAULT_SMOOTHNESS = 0.2f;

  public static final int INDICATOR_TYPE_CIRCLE = 0;
  public static final int INDICATOR_TYPE_SQUARE = 1;
  public static final int INDICATOR_STYLE_FILL = 0;
  public static final int INDICATOR_STYLE_STROKE = 1;
  private static final int DEFAULT_INDICATOR_TYPE = INDICATOR_TYPE_CIRCLE;
  private static final int DEFAULT_INDICATOR_STYLE = INDICATOR_STYLE_STROKE;

  @Retention(RetentionPolicy.SOURCE) @IntDef({ INDICATOR_STYLE_FILL, INDICATOR_STYLE_STROKE })
  public @interface IndicatorStyle {
  }

  @Retention(RetentionPolicy.SOURCE) @IntDef({ INDICATOR_TYPE_CIRCLE, INDICATOR_TYPE_SQUARE })
  public @interface IndicatorType {
  }

  private Paint paintLine;
  private Paint paintFill;
  private Paint paintIndicator;
  private Path path;
  private int lineColor;
  private int defaultBackgroundColor;
  private int chartBackgroundColor;
  private float strokeSize;
  private float smoothness;

  private float indicatorSize;
  private boolean indicatorVisible;
  private int indicatorType;
  private int indicatorColor;
  private int indicatorStyle;
  private float indicatorStrokeSize;

  private boolean fullWidth;

  public CharterLine(Context context) {
    this(context, null, 0);
  }

  public CharterLine(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CharterLine(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context, attrs);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public CharterLine(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init(context, attrs);
  }

  private void init(final Context context, final AttributeSet attrs) {
    final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Charter);
    Resources res = getResources();
    fullWidth = typedArray.getBoolean(R.styleable.Charter_c_fullWidth,
        res.getBoolean(R.bool.default_fullWidth));
    lineColor = typedArray.getColor(R.styleable.Charter_c_lineColor,
        res.getColor(R.color.default_lineColor));
    int chartFillColor = typedArray.getColor(R.styleable.Charter_c_chartFillColor,
        res.getColor(R.color.default_chartFillColor));
    indicatorVisible = typedArray.getBoolean(R.styleable.Charter_c_indicatorVisible,
        res.getBoolean(R.bool.default_indicatorVisible));
    indicatorType = typedArray.getInt(R.styleable.Charter_c_indicatorType, DEFAULT_INDICATOR_TYPE);
    indicatorSize = typedArray.getDimension(R.styleable.Charter_c_indicatorSize,
        res.getDimension(R.dimen.default_indicatorSize));
    indicatorStrokeSize = typedArray.getDimension(R.styleable.Charter_c_indicatorStrokeSize,
        res.getDimension(R.dimen.default_indicatorStrokeSize));
    indicatorColor = typedArray.getColor(R.styleable.Charter_c_indicatorColor,
        res.getColor(R.color.default_indicatorColor));
    indicatorStyle =
        typedArray.getInt(R.styleable.Charter_c_indicatorStyle, DEFAULT_INDICATOR_STYLE);
    strokeSize = typedArray.getDimension(R.styleable.Charter_c_strokeSize,
        res.getDimension(R.dimen.default_strokeSize));
    smoothness = typedArray.getFloat(R.styleable.Charter_c_smoothness, DEFAULT_SMOOTHNESS);
    typedArray.recycle();

    paintLine = new Paint();
    paintLine.setAntiAlias(true);
    paintLine.setStrokeWidth(strokeSize);
    paintLine.setColor(lineColor);
    paintLine.setStyle(Paint.Style.STROKE);

    paintFill = new Paint();
    paintFill.setAntiAlias(true);
    paintFill.setColor(chartFillColor);
    paintFill.setStyle(Paint.Style.FILL);

    paintIndicator = new Paint();
    paintIndicator.setAntiAlias(true);
    paintIndicator.setStrokeWidth(indicatorStrokeSize);
    defaultBackgroundColor = res.getColor(R.color.default_chartBackgroundColor);
    chartBackgroundColor = defaultBackgroundColor;

    path = new Path();
  }

  public Paint getPaintLine() {
    return paintLine;
  }

  public void setPaintLine(@NonNull Paint paintLine) {
    this.paintLine = paintLine;
    invalidate();
  }

  public Paint getPaintFill() {
    return paintFill;
  }

  public void setPaintFill(@NonNull Paint paintFill) {
    this.paintFill = paintFill;
    invalidate();
  }

  public Paint getPaintIndicator() {
    return paintIndicator;
  }

  public void setPaintIndicator(@NonNull Paint paintIndicator) {
    this.paintIndicator = paintIndicator;
    invalidate();
  }

  public float getIndicatorStrokeSize() {
    return indicatorStrokeSize;
  }

  public void setIndicatorStrokeSize(float indicatorStrokeSize) {
    paintIndicator.setStrokeWidth(indicatorStrokeSize);
    this.indicatorStrokeSize = indicatorStrokeSize;
    invalidate();
  }

  public int getIndicatorStyle() {
    return indicatorStyle;
  }

  public void setIndicatorStyle(@IndicatorStyle int indicatorStyle) {
    this.indicatorStyle = indicatorStyle;
    invalidate();
  }

  public int getIndicatorColor() {
    return indicatorColor;
  }

  public void setIndicatorColor(@ColorInt int indicatorColor) {
    paintIndicator.setColor(indicatorColor);
    this.indicatorColor = indicatorColor;
    invalidate();
  }

  public int getIndicatorType() {
    return indicatorType;
  }

  public void setIndicatorType(@IndicatorType int indicatorType) {
    this.indicatorType = indicatorType;
    invalidate();
  }

  public int getLineColor() {
    return lineColor;
  }

  public void setLineColor(@ColorInt int color) {
    paintLine.setColor(lineColor);
    lineColor = color;
    invalidate();
  }

  public float getIndicatorSize() {
    return indicatorSize;
  }

  public void setIndicatorSize(float indicatorSize) {
    this.indicatorSize = indicatorSize;
    invalidate();
  }

  public float getStrokeSize() {
    return strokeSize;
  }

  public void setStrokeSize(float strokeSize) {
    paintLine.setStrokeWidth(strokeSize);
    this.strokeSize = strokeSize;
    invalidate();
  }

  public int getChartFillColor() {
    return paintFill.getColor();
  }

  public void setChartFillColor(@ColorInt int chartFillColor) {
    paintFill.setColor(chartFillColor);
    invalidate();
  }

  public boolean isIndicatorVisible() {
    return indicatorVisible;
  }

  public void setIndicatorVisible(boolean indicatorVisible) {
    this.indicatorVisible = indicatorVisible;
    invalidate();
  }

  public float getSmoothness() {
    return smoothness;
  }

  public void setSmoothness(@FloatRange(from = 0.0, to = 0.5) float smoothness) {
    this.smoothness = smoothness;
    invalidate();
  }

  public boolean isFullWidth() {
    return fullWidth;
  }

  public void setFullWidth(boolean fullWidth) {
    this.fullWidth = fullWidth;
    invalidate();
  }

  public void draw(Canvas canvas) {
    super.draw(canvas);

    if (values == null || values.length == 0) {
      return;
    }

    if (!anim) {
      valuesTransition = values.clone();
    }

    float fullWidthCorrectionX;

    final int valuesLength = valuesTransition.length;
    final float border = strokeSize + indicatorSize;

    final float height = getMeasuredHeight() - border;
    fullWidthCorrectionX = fullWidth ? 0 : border;
    final float width = getMeasuredWidth() - fullWidthCorrectionX;

    final float dX = valuesLength > 1 ? valuesLength - 1 : 2;
    final float dY = maxY - minY > 0 ? maxY - minY : 2;

    path.reset();

    // calculate point coordinates
    List<PointF> points = new ArrayList<>(valuesLength);
    fullWidthCorrectionX = fullWidth ? 0 : (border / 2);
    for (int i = 0; i < valuesLength; i++) {
      float x = fullWidthCorrectionX + i * width / dX;
      float pointBorder = !indicatorVisible && valuesTransition[i] == minY ? border : border / 2;
      float y = pointBorder + height - (valuesTransition[i] - minY) * height / dY;
      points.add(new PointF(x, y));
    }

    float lX = 0;
    float lY = 0;

    path.moveTo(points.get(0).x, points.get(0).y);
    for (int i = 1; i < valuesLength; i++) {
      PointF p = points.get(i);
      float pointSmoothness = valuesTransition[i] == minY ? 0 : smoothness;

      PointF firstPointF = points.get(i - 1);
      float x1 = firstPointF.x + lX;
      float y1 = firstPointF.y + lY;

      PointF secondPointF = points.get(i + 1 < valuesLength ? i + 1 : i);
      lX = (secondPointF.x - firstPointF.x) / 2 * pointSmoothness;
      lY = (secondPointF.y - firstPointF.y) / 2 * pointSmoothness;
      float x2 = p.x - lX;
      float y2 = p.y - lY;
      if (y1 == p.y) {
        y2 = y1;
      }

      path.cubicTo(x1, y1, x2, y2, p.x, p.y);
    }
    canvas.drawPath(path, paintLine);

    // fill area
    if (valuesLength > 0) {
      fullWidthCorrectionX = !fullWidth ? 0 : (border / 2);
      path.lineTo(points.get(valuesLength - 1).x + fullWidthCorrectionX, height + border);
      path.lineTo(points.get(0).x - fullWidthCorrectionX, height + border);
      path.close();
      canvas.drawPath(path, paintFill);
    }

    // draw indicator
    if (indicatorVisible) {
      for (int i = 0; i < points.size(); i++) {
        RectF rectF = new RectF();
        float x = points.get(i).x;
        float y = points.get(i).y;

        paintIndicator.setColor(indicatorColor);
        paintIndicator.setStyle(Paint.Style.FILL_AND_STROKE);
        if (indicatorType == INDICATOR_TYPE_CIRCLE) {
          canvas.drawCircle(x, y, indicatorSize / 2, paintIndicator);
        } else {
          rectF.left = x - (indicatorSize / 2);
          rectF.top = y - (indicatorSize / 2);
          rectF.right = x + (indicatorSize / 2);
          rectF.bottom = y + (indicatorSize / 2);
          canvas.drawRect(rectF.left, rectF.top, rectF.right, rectF.bottom, paintIndicator);
        }

        if (indicatorStyle == INDICATOR_STYLE_STROKE) {
          paintIndicator.setColor(chartBackgroundColor);
          paintIndicator.setStyle(Paint.Style.FILL);

          if (indicatorType == INDICATOR_TYPE_CIRCLE) {
            canvas.drawCircle(x, y, (indicatorSize - indicatorStrokeSize) / 2, paintIndicator);
          } else {
            rectF.left = x - (indicatorSize / 2) + indicatorStrokeSize;
            rectF.top = y - (indicatorSize / 2) + indicatorStrokeSize;
            rectF.right = x + (indicatorSize / 2) - indicatorStrokeSize;
            rectF.bottom = y + (indicatorSize / 2) - indicatorStrokeSize;
            canvas.drawRect(rectF.left, rectF.top, rectF.right, rectF.bottom, paintIndicator);
          }
        }
      }
    }

    if (anim && !animFinished && !animator.isRunning()) {
      playAnimation();
    }
  }

  @Override public void setBackgroundColor(@ColorInt int color) {
    super.setBackgroundColor(color);
    chartBackgroundColor = color;
  }

  @Override public void setBackground(Drawable background) {
    super.setBackground(background);
    chartBackgroundColor = defaultBackgroundColor;
    Drawable drawable = getBackground();
    if (drawable instanceof ColorDrawable) {
      chartBackgroundColor = ((ColorDrawable) drawable).getColor();
    }
  }
}
