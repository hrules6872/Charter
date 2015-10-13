package com.hrules.charter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

class CharterBase extends View {
    static final int ANIM_DELAY_MILLIS = 30;
    static final boolean DEFAULT_ANIM = true;
    static final long DEFAULT_ANIM_DURATION = 500;
    static final boolean DEFAULT_AUTOSHOW = true;

    final Runnable doNextAnimStep = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };

    float minY;
    float maxY;

    float[] values;
    float[] valuesTransition;

    boolean anim;
    long animDuration;
    boolean animFinished;
    Handler handlerAnim;

    protected CharterBase(Context context) {
        this(context, null);
    }

    protected CharterBase(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    protected CharterBase(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected CharterBase(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        if (isInEditMode()) {
            return;
        }

        animFinished = false;
        handlerAnim = new Handler();
    }

    public void show() {
        setWillNotDraw(false);
        invalidate();
    }

    public float[] getValues() {
        return values;
    }

    public void setValues(float[] values) {
        if (values == null || values.length == 0) {
            return;
        }

        this.values = values;
        getMaxMinValues(values);
        initValuesTarget(values);

        animFinished = false;
        invalidate();
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
        this.maxY = maxY;
        invalidate();
    }

    public float getMinY() {
        return minY;
    }

    public void setMinY(float minY) {
        this.minY = minY;
        invalidate();
    }

    void calculateNextAnimStep() {
        animFinished = true;
        for (int i = 0; i < valuesTransition.length; i++) {
            float diff = values[i] - minY;
            float step = (diff * ANIM_DELAY_MILLIS) / animDuration;

            if (valuesTransition[i] + step >= values[i]) {
                valuesTransition[i] = values[i];
            } else {
                valuesTransition[i] = valuesTransition[i] + step;
                animFinished = false;
            }
        }
    }

    public void replayAnim() {
        if (values == null || values.length == 0) {
            return;
        }

        initValuesTarget(values);
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
        this.animDuration = animDuration;
        replayAnim();
    }
}
