package com.hrules.charter.demo;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.hrules.charter.CharterBar;
import com.hrules.charter.CharterLine;
import com.hrules.charter.CharterXLabels;
import com.hrules.charter.CharterYLabels;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
  private static final int DEFAULT_ITEMS_COUNT = 15;
  private static final int DEFAULT_RANDOM_VALUE_MIN = 10;
  private static final int DEFAULT_RANDOM_VALUE_MAX = 100;

  private float[] values;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    values =
        fillRandomValues(DEFAULT_ITEMS_COUNT, DEFAULT_RANDOM_VALUE_MAX, DEFAULT_RANDOM_VALUE_MIN);
    Resources res = getResources();
    int[] barColors = new int[] {
        res.getColor(R.color.lightblue500), res.getColor(R.color.lightblue400),
        res.getColor(R.color.lightblue300)
    };

    // charter_line
    final CharterLine charterLine = (CharterLine) findViewById(R.id.charter_line);
    charterLine.setValues(values);
    charterLine.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        values = fillRandomValues(DEFAULT_ITEMS_COUNT, DEFAULT_RANDOM_VALUE_MAX,
            DEFAULT_RANDOM_VALUE_MIN);
        charterLine.setValues(values);
        charterLine.show();
      }
    });

    // charter_bar
    final CharterBar charterBar = (CharterBar) findViewById(R.id.charter_bar);
    charterBar.setValues(values);
    charterBar.setColors(barColors);
    charterBar.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        values = fillRandomValues(DEFAULT_ITEMS_COUNT, DEFAULT_RANDOM_VALUE_MAX,
            DEFAULT_RANDOM_VALUE_MIN);
        charterBar.setValues(values);
        charterBar.show();
      }
    });

    // charter_bar_no_margin
    final CharterBar charterBarNoMargin = (CharterBar) findViewById(R.id.charter_bar_no_margin);
    charterBarNoMargin.setValues(values);
    charterBarNoMargin.setBarMargin(0);
    charterBarNoMargin.setColors(barColors);
    charterBarNoMargin.setColorsBackground(new int[] { android.R.color.white });
    charterBarNoMargin.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        values = fillRandomValues(DEFAULT_ITEMS_COUNT, DEFAULT_RANDOM_VALUE_MAX,
            DEFAULT_RANDOM_VALUE_MIN);
        charterBarNoMargin.setValues(values);
        charterBarNoMargin.show();
      }
    });

    // charter_line_XLabel
    final CharterXLabels charterLineLabelX =
        (CharterXLabels) findViewById(R.id.charter_line_XLabel);
    charterLineLabelX.setStickyEdges(true);
    charterLineLabelX.setValues(values);

    final CharterLine charterLineWithLabel =
        (CharterLine) findViewById(R.id.charter_line_with_XLabel);
    charterLineWithLabel.setValues(values);
    charterLineWithLabel.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        values = fillRandomValues(DEFAULT_ITEMS_COUNT, DEFAULT_RANDOM_VALUE_MAX,
            DEFAULT_RANDOM_VALUE_MIN);
        charterLineWithLabel.setValues(values);
        charterLineWithLabel.show();

        charterLineLabelX.setValues(values);
      }
    });

    // charter_bar_XLabel
    final CharterXLabels charterBarLabelX = (CharterXLabels) findViewById(R.id.charter_bar_XLabel);
    charterBarLabelX.setStickyEdges(false);
    charterBarLabelX.setVisibilityPattern(new boolean[] { true, false });
    charterBarLabelX.setValues(values);

    final CharterBar charterBarWithLabel = (CharterBar) findViewById(R.id.charter_bar_with_XLabel);
    charterBarWithLabel.setValues(values);
    charterBarWithLabel.setColors(barColors);
    charterBarWithLabel.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        values = fillRandomValues(DEFAULT_ITEMS_COUNT, DEFAULT_RANDOM_VALUE_MAX,
            DEFAULT_RANDOM_VALUE_MIN);
        charterBarWithLabel.setValues(values);
        charterBarWithLabel.show();

        charterBarLabelX.setValues(values);
      }
    });

    // charter_line_YLabel
    final CharterYLabels charterLineYLabel =
        (CharterYLabels) findViewById(R.id.charter_line_YLabel);
    charterLineYLabel.setValues(values, true);

    final CharterLine charterLineWithYLabel =
        (CharterLine) findViewById(R.id.charter_line_with_YLabel);
    charterLineWithYLabel.setValues(values);
    charterLineWithYLabel.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        values = fillRandomValues(DEFAULT_ITEMS_COUNT, DEFAULT_RANDOM_VALUE_MAX,
            DEFAULT_RANDOM_VALUE_MIN);
        charterLineWithYLabel.setValues(values);
        charterLineWithYLabel.show();

        charterLineYLabel.setValues(values, true);
      }
    });

    // charter_bar_YLabel
    final CharterYLabels charterBarYLabel = (CharterYLabels) findViewById(R.id.charter_bar_YLabel);
    charterBarYLabel.setVisibilityPattern(new boolean[] { true, false });
    charterBarYLabel.setValues(values, true);

    final CharterBar charterBarWithYLabel = (CharterBar) findViewById(R.id.charter_bar_with_YLabel);
    charterBarWithYLabel.setValues(values);
    charterBarWithYLabel.setColors(barColors);
    charterBarWithYLabel.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        values = fillRandomValues(DEFAULT_ITEMS_COUNT, DEFAULT_RANDOM_VALUE_MAX,
            DEFAULT_RANDOM_VALUE_MIN);
        charterBarWithYLabel.setValues(values);
        charterBarWithYLabel.show();

        charterBarYLabel.setValues(values, true);
      }
    });
  }

  private float[] fillRandomValues(int length, int max, int min) {
    Random random = new Random();

    float[] newRandomValues = new float[length];
    for (int i = 0; i < newRandomValues.length; i++) {
      newRandomValues[i] = random.nextInt(max - min + 1) - min;
    }
    return newRandomValues;
  }
}
