package com.hrules.charter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;

public class CharterBar extends CharterBase {
    private static final boolean DEFAULT_PAINT_BAR_BACKGROUND = true;
    private static final float DEFAULT_BAR_MIN_CORRECTION = 2f;

    private boolean paintBarBackground;
    private int barBackgroundColor;
    private float barMargin;

    private Paint paintBar;
    private int[] colors;
    private int[] colorsBackground;

    public CharterBar(Context context) {
        this(context, null);
    }

    public CharterBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CharterBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CharterBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(final Context context, final AttributeSet attrs) {
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Charter);
        paintBarBackground = typedArray.getBoolean(R.styleable.Charter_c_paintBarBackground, DEFAULT_PAINT_BAR_BACKGROUND);
        int barColor = typedArray.getColor(R.styleable.Charter_c_barColor, getResources().getColor(R.color.default_barColor));
        int barBackgroundColor = typedArray.getColor(R.styleable.Charter_c_barBackgroundColor, getResources().getColor(R.color.default_barBackgroundColor));
        barMargin = typedArray.getDimension(R.styleable.Charter_c_barMargin, getResources().getDimension(R.dimen.default_barMargin));
        anim = typedArray.getBoolean(R.styleable.Charter_c_anim, DEFAULT_ANIM);
        animDuration = typedArray.getInt(R.styleable.Charter_c_animDuration, (int) DEFAULT_ANIM_DURATION);
        setWillNotDraw(!typedArray.getBoolean(R.styleable.Charter_c_autoShow, DEFAULT_AUTOSHOW));
        typedArray.recycle();

        paintBar = new Paint();
        paintBar.setAntiAlias(true);

        colors = new int[]{barColor};
        colorsBackground = new int[]{barBackgroundColor};
    }

    public Paint getPaintBar() {
        return paintBar;
    }

    public void setPaintBar(Paint paintBar) {
        this.paintBar = paintBar;
        invalidate();
    }

    public int[] getColors() {
        return colors;
    }

    public void setColors(@ColorInt int[] colors) {
        if (colors == null || colors.length == 0) {
            return;
        }

        this.colors = colors;
        invalidate();
    }

    public int[] getColorsBackground() {
        return colorsBackground;
    }

    public void setColorsBackground(@ColorInt int[] colorsBackground) {
        if (colorsBackground == null || colorsBackground.length == 0) {
            return;
        }

        this.colorsBackground = colorsBackground;
        invalidate();
    }

    public float getBarMargin() {
        return barMargin;
    }

    public void setBarMargin(float barMargin) {
        this.barMargin = barMargin;
        invalidate();
    }

    public boolean isPaintBarBackground() {
        return paintBarBackground;
    }

    public void setPaintBarBackground(boolean paintBarBackground) {
        this.paintBarBackground = paintBarBackground;
        invalidate();
    }

    public int getBarBackgroundColor() {
        return barBackgroundColor;
    }

    public void setBarBackgroundColor(@ColorInt int barBackgroundColor) {
        this.barBackgroundColor = barBackgroundColor;
        invalidate();
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (values == null || values.length == 0) {
            return;
        }

        if (anim) {
            calculateNextAnimStep();

        } else {
            valuesTransition = values.clone();
        }

        final int valuesLength = valuesTransition.length;

        final float height = getMeasuredHeight();
        final float width = getMeasuredWidth();

        final float barWidth = width / valuesLength;
        final float diff = maxY - minY;
        final float sliceHeight = height / diff;

        int colorsPos = 0;
        int colorsBackgroundPos = -1;

        for (int i = 0; i < valuesLength; i++) {
            RectF rectF = new RectF();
            rectF.left = (i * barWidth) + barMargin;
            rectF.top = height - (sliceHeight * (valuesTransition[i] - minY));
            rectF.top = rectF.top == height ? rectF.top - DEFAULT_BAR_MIN_CORRECTION : rectF.top;
            rectF.right = (i * barWidth) + barWidth - barMargin;
            rectF.bottom = height;

            // paint background
            if (paintBarBackground) {
                if (colorsBackgroundPos + 1 >= colorsBackground.length) {
                    colorsBackgroundPos = 0;
                } else {
                    colorsBackgroundPos++;
                }
                paintBar.setColor(colorsBackground[colorsBackgroundPos]);
                canvas.drawRect(rectF.left, 0, rectF.right, rectF.bottom, paintBar);
            }

            // paint bar
            if (colorsPos + 1 >= colors.length) {
                colorsPos = 0;
            } else {
                colorsPos++;
            }
            paintBar.setColor(colors[colorsPos]);

            canvas.drawRect(rectF.left, rectF.top, rectF.right, rectF.bottom, paintBar);
        }

        if (anim && !animFinished) {
            handlerAnim.postDelayed(doNextAnimStep, ANIM_DELAY_MILLIS);
        }
    }
}
