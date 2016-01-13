package com.hrules.charter;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

class CharterBase extends View {
  float minY;
  float maxY;

  float[] values;
  float[] valuesTransition;

  boolean anim;
  private long animDuration;
  boolean animFinished;
  ValueAnimator animator;

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
    setWillNotDraw(!typedArray.getBoolean(R.styleable.Charter_c_autoShow,
        res.getBoolean(R.bool.default_autoShow)));
    typedArray.recycle();

    animFinished = false;
    animator = ValueAnimator.ofFloat(0f, 1f);
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
    animator.setInterpolator(new LinearInterpolator());
    animator.start();
  }
}
