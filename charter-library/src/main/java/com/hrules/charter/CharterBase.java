package com.hrules.charter;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

class CharterBase extends View {
  float minY;
  float maxY;

  float height;
  float width;

  float[] values;
  float[] valuesTransition;

  boolean anim;
  private long animDuration;
  boolean animFinished;
  ValueAnimator animator;
  private Interpolator animInterpolator;

  Path path;
  private boolean showGridLinesX;
  private boolean showGridLinesY;
  private int gridLinesCount;
  private Paint paintGrid;

  private CharterAnimListener animListener;

  protected CharterBase(Context context) {
    this(context, null);
  }

  protected CharterBase(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  protected CharterBase(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  protected CharterBase(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init(context, attrs);
  }

  private void init(final Context context, final AttributeSet attrs) {
    if (isInEditMode()) {
      return;
    }

    final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Charter);
    Resources res = getResources();
    anim =
        typedArray.getBoolean(R.styleable.Charter_c_anim, res.getBoolean(R.bool.default_animState));
    animDuration = checkAnimDuration(typedArray.getInt(R.styleable.Charter_c_animDuration,
        res.getInteger(R.integer.default_animDuration)));
    showGridLinesX = typedArray.getBoolean(R.styleable.Charter_c_showGridLinesX,
        res.getBoolean(R.bool.default_showGridLinesX));
    showGridLinesY = typedArray.getBoolean(R.styleable.Charter_c_showGridLinesY,
        res.getBoolean(R.bool.default_showGridLinesY));
    gridLinesCount = typedArray.getInteger(R.styleable.Charter_c_gridLinesCount,
        res.getInteger(R.integer.default_gridLinesCount));
    int gridLinesColor = typedArray.getColor(R.styleable.Charter_c_gridLinesColor,
        res.getColor(R.color.default_gridLinesColor));
    float gridLinesStrokeSize = typedArray.getDimension(R.styleable.Charter_c_gridLinesStrokeSize,
        res.getDimension(R.dimen.default_gridLinesStrokeSize));

    setWillNotDraw(!typedArray.getBoolean(R.styleable.Charter_c_autoShow,
        res.getBoolean(R.bool.default_autoShow)));
    typedArray.recycle();

    paintGrid = new Paint();
    paintGrid.setColor(gridLinesColor);
    paintGrid.setStrokeWidth(gridLinesStrokeSize);
    paintGrid.setStyle(Paint.Style.STROKE);
    paintGrid.setPathEffect(new DashPathEffect(new float[] { 10f, 5f }, 0));

    path = new Path();

    animFinished = false;
    animator = ValueAnimator.ofFloat(0f, 1f);
    animInterpolator = new LinearInterpolator();
  }

  private long checkAnimDuration(long animDuration) {
    int minAnimDuration = getResources().getInteger(R.integer.default_minAnimDuration);
    if (animDuration < minAnimDuration) {
      animDuration = minAnimDuration;
    }
    return animDuration;
  }

  public void show() {
    setWillNotDraw(false);
    invalidate();
  }

  public float[] getValues() {
    return values;
  }

  public void setValues(@NonNull @Size(min = 1) float[] values) {
    this.values = values;
    getMaxMinValues(values);
    replayAnim();
  }

  public void resetValues() {
    if (values == null || values.length == 0) {
      return;
    }

    for (int i = 0; i < values.length; i++) {
      values[i] = minY;
    }

    setValues(values);
  }

  private void getMaxMinValues(float[] values) {
    if (values != null && values.length > 0) {
      maxY = values[0];
      minY = values[0];
      for (float y : values) {
        if (y > maxY) {
          maxY = y;
        }
        if (y < minY) {
          minY = y;
        }
      }
    }
  }

  private void initValuesTarget(float[] values) {
    this.valuesTransition = values.clone();
    for (int i = 0; i < valuesTransition.length; i++) {
      valuesTransition[i] = minY;
    }
  }

  public float getMaxY() {
    return maxY;
  }

  public void setMaxY(float maxY) {
    if (values == null) {
      throw new IllegalStateException("You must call setValues() first");
    }
    this.maxY = maxY;
    invalidate();
  }

  public float getMinY() {
    return minY;
  }

  public void setMinY(float minY) {
    if (values == null) {
      throw new IllegalStateException("You must call setValues() first");
    }
    this.minY = minY;
    invalidate();
  }

  private void calculateNextAnimStep(float animationProgress) {
    float step = (animationProgress * maxY);
    for (int i = 0; i < valuesTransition.length; i++) {
      valuesTransition[i] = (step >= values[i]) ? values[i] : step;
    }

    if (animListener != null) {
      animListener.onAnimProgress(animationProgress);
    }
  }

  public void replayAnim() {
    if (values == null || values.length == 0) {
      return;
    }

    initValuesTarget(values);
    animator.cancel();
    animFinished = false;
    invalidate();
  }

  public boolean isAnim() {
    return anim;
  }

  public void setAnim(boolean anim) {
    this.anim = anim;
    replayAnim();
  }

  public long getAnimDuration() {
    return animDuration;
  }

  public void setAnimDuration(long animDuration) {
    this.animDuration = checkAnimDuration(animDuration);
    replayAnim();
  }

  public void setAnimListener(CharterAnimListener animListener) {
    this.animListener = animListener;
  }

  public void setAnimInterpolator(Interpolator animInterpolator) {
    this.animInterpolator = animInterpolator;
  }

  void playAnimation() {
    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        calculateNextAnimStep((float) animation.getAnimatedValue());
        invalidate();
      }
    });
    animator.addListener(new Animator.AnimatorListener() {
      @Override public void onAnimationStart(Animator animation) {
        if (animListener != null) {
          animListener.onAnimStart();
        }
      }

      @Override public void onAnimationEnd(Animator animation) {
        animFinished = true;
        if (animListener != null) {
          animListener.onAnimFinish();
        }
      }

      @Override public void onAnimationCancel(Animator animation) {
        if (animListener != null) {
          animListener.onAnimCancel();
        }
      }

      @Override public void onAnimationRepeat(Animator animation) {

      }
    });
    animator.setDuration(animDuration);
    animator.setInterpolator(animInterpolator);
    animator.start();
  }

  public void setShowGridLines(boolean showGridLines) {
    this.showGridLinesX = showGridLines;
    this.showGridLinesY = showGridLines;
  }

  public boolean isShowGridLinesX() {
    return showGridLinesX;
  }

  public void setShowGridLinesX(boolean showGridLinesX) {
    this.showGridLinesX = showGridLinesX;
  }

  public boolean isShowGridLinesY() {
    return showGridLinesY;
  }

  public void setShowGridLinesY(boolean showGridLinesY) {
    this.showGridLinesY = showGridLinesY;
  }

  public int getGridLinesCount() {
    return gridLinesCount;
  }

  public void setGridLinesCount(int gridLinesCount) {
    if (gridLinesCount <= 1) {
      gridLinesCount = 1;
    }
    this.gridLinesCount = gridLinesCount;
  }

  public Paint getPaintGrid() {
    return paintGrid;
  }

  public void setPaintGrid(Paint paintGrid) {
    this.paintGrid = paintGrid;
  }

  public void setGridLinesColor(@ColorInt int gridLinesColor) {
    paintGrid.setColor(gridLinesColor);
    invalidate();
  }

  public int getGridLinesColor() {
    return paintGrid.getColor();
  }

  public float getGridLinesStrokeSize() {
    return paintGrid.getStrokeWidth();
  }

  public void setGridLinesStrokeSize(float strokeSize) {
    paintGrid.setStrokeWidth(strokeSize);
    invalidate();
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    height = getMeasuredHeight();
    width = getMeasuredWidth();

    final float stepX = height / (gridLinesCount + 1);
    final float stepY = width / (gridLinesCount + 1);

    path.reset();
    if (showGridLinesX || showGridLinesY) {
      for (int i = 1; i < gridLinesCount + 1; i++) {
        if (showGridLinesX) {
          path.moveTo(0, stepX * i);
          path.lineTo(width, stepX * i);
        }
        if (showGridLinesY) {
          path.moveTo(stepY * i, 0);
          path.lineTo(stepY * i, height);
        }
      }
      canvas.drawPath(path, paintGrid);
    }
  }
}