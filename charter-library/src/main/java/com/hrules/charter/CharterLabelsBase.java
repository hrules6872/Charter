package com.hrules.charter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.View;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class CharterLabelsBase extends View {
  /**
   * 垂直方向默认三种 上中下
   */
  public static final int VERTICAL_GRAVITY_TOP = 0;
  public static final int VERTICAL_GRAVITY_CENTER = 1;
  public static final int VERTICAL_GRAVITY_BOTTOM = 2;
  /**
   * 水平方向默认三种：左中右
   */
  public static final int HORIZONTAL_GRAVITY_LEFT = 0;
  public static final int HORIZONTAL_GRAVITY_CENTER = 1;
  public static final int HORIZONTAL_GRAVITY_RIGHT = 2;
  //垂直方向默认居下
  private static final int DEFAULT_VERTICAL_GRAVITY = VERTICAL_GRAVITY_BOTTOM;
  //水平方向默认居左
  private static final int DEFAULT_HORIZONTAL_GRAVITY = HORIZONTAL_GRAVITY_LEFT;
  private static final boolean DEFAULT_STICKY_EDGES = false;

  Paint paintLabel;//标签的画笔
  boolean[] visibilityPattern;//标签的显示模式
  int verticalGravity;//纵轴标签显示位置
  int horizontalGravity;//横轴标签的显示位置
  String[] values;//标签数值
  boolean stickyEdges;//是否跨边显示
  private int paintLabelColor;//标签的颜色
  private float paintLabelSize;//标签的大小

  protected CharterLabelsBase(Context context) {
    this(context, null);
  }

  protected CharterLabelsBase(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  protected CharterLabelsBase(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  protected CharterLabelsBase(Context context, AttributeSet attrs, int defStyleAttr,
      int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    /**
     * isInEditMode()是view类的方法，默认返回false
     */
    if (isInEditMode()) {
      return;
    }

    final TypedArray typedArray = context.obtainStyledAttributes(attrs,
            R.styleable.Charter);
    stickyEdges = typedArray.getBoolean(
            R.styleable.Charter_c_stickyEdges, DEFAULT_STICKY_EDGES);
    //垂直方向 默认居中
    verticalGravity =
        typedArray.getInt(R.styleable.Charter_c_verticalGravity,
                DEFAULT_VERTICAL_GRAVITY);
    //水平方向，默认居左
    horizontalGravity =
        typedArray.getInt(R.styleable.Charter_c_horizontalGravity,
                DEFAULT_HORIZONTAL_GRAVITY);
    //标签的颜色
    paintLabelColor = typedArray.getColor(R.styleable.Charter_c_labelColor,
        getResources().getColor(R.color.default_labelColor));
    //标签大小，默认10sp
    paintLabelSize = typedArray.getDimension(R.styleable.Charter_c_labelSize,
        getResources().getDimension(R.dimen.default_labelSize));
    typedArray.recycle();//回收
    //标签画笔
    paintLabel = new Paint();
    paintLabel.setAntiAlias(true);
    paintLabel.setColor(paintLabelColor);
    paintLabel.setTextSize(paintLabelSize);
    /**
     *  标签可见性模式 默认显示、显示、显示。。。。。
     *  当然也可以设置模式。
     */
    visibilityPattern = new boolean[] { true };
  }

  public boolean isStickyEdges() {
    return stickyEdges;
  }

  public void setStickyEdges(boolean stickyEdges) {
    this.stickyEdges = stickyEdges;
    invalidate();
  }

  public Paint getPaintLabel() {
    return paintLabel;
  }

  public void setPaintLabel(Paint paintLabel) {
    this.paintLabel = paintLabel;
    invalidate();
  }

  public boolean[] getVisibilityPattern() {
    return visibilityPattern;
  }

  public void setVisibilityPattern(boolean[] visibilityPattern) {
    this.visibilityPattern = visibilityPattern;
    invalidate();
  }

  public int getVerticalGravity() {
    return verticalGravity;
  }
  //使用注解 限制设置的值
  public void setVerticalGravity(@VerticalGravity int verticalGravity) {
    this.verticalGravity = verticalGravity;
    invalidate();
  }

  public int getHorizontalGravity() {
    return horizontalGravity;
  }

  public void setHorizontalGravity(@HorizontalGravity int horizontalGravity) {
    this.horizontalGravity = horizontalGravity;
    invalidate();
  }

  public int getLabelColor() {
    return paintLabelColor;
  }

  public void setLabelColor(@ColorInt int labelColor) {
    paintLabel.setColor(labelColor);
    paintLabelColor = labelColor;
    invalidate();
  }

  public float getLabelSize() {
    return paintLabelSize;
  }

  public void setLabelSize(float labelSize) {
    paintLabel.setTextSize(labelSize);
    paintLabelSize = labelSize;
    invalidate();
  }

  public void setLabelTypeface(Typeface typeface) {
    paintLabel.setTypeface(typeface);
    invalidate();
  }

  public String[] getValues() {
    return values;
  }

  public void setValues(float[] values) {
    setValues(floatArrayToStringArray(values));
  }

  public void setValues(String[] values) {
    if (values == null || values.length == 0) {
      return;
    }

    this.values = values;
    invalidate();
  }

  public void setValues(float[] values, boolean summarize) {
    if (summarize) {
      values = summarize(values);
    }
    //将值转化成字符串
    setValues(floatArrayToStringArray(values));
  }

  private String[] floatArrayToStringArray(float[] values) {
    if (values == null) {
      return new String[] {};
    }

    String[] stringArray = new String[values.length];
    for (int i = 0; i < stringArray.length; i++) {
      stringArray[i] = String.valueOf((int) values[i]);
    }
    return stringArray;
  }

  /**
   * 将值进行汇总
   * 汇总之后的值共有五个。最后显示的值也就五个值。
   * @param values
   * @return
   */
  private float[] summarize(float[] values) {
    if (values == null) {
      return new float[] {};
    }

    float max = values[0];
    float min = values[0];
    for (float value : values) {
      if (value > max) {
        max = value;
      }
      if (value < min) {
        min = value;
      }
    }
    float diff = max - min;

    return new float[] { min, diff / 5, diff / 2, (diff / 5) * 4, max };
  }

  /**
   * 定义注解
   */
  @Retention(RetentionPolicy.SOURCE)
  @IntDef({ VERTICAL_GRAVITY_TOP, VERTICAL_GRAVITY_CENTER,
          VERTICAL_GRAVITY_BOTTOM })
  public @interface VerticalGravity {
  }

  @Retention(RetentionPolicy.SOURCE)
  @IntDef({ HORIZONTAL_GRAVITY_LEFT, HORIZONTAL_GRAVITY_CENTER,
          HORIZONTAL_GRAVITY_RIGHT })
  public @interface HorizontalGravity {
  }
}
