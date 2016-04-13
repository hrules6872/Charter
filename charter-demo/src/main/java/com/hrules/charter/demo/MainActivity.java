package com.hrules.charter.demo;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.BounceInterpolator;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.hrules.charter.CharterBar;
import com.hrules.charter.CharterLine;
import com.hrules.charter.CharterXLabels;
import com.hrules.charter.CharterXMarkers;
import com.hrules.charter.CharterYLabels;
import com.hrules.charter.CharterYMarkers;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
  @Bind(R.id.charter_line) CharterLine charterLine;
  @Bind(R.id.charter_bar) CharterBar charterBar;
  @Bind(R.id.charter_bar_no_margin) CharterBar charterBarNoMargin;
  @Bind(R.id.charter_line_XLabel) CharterXLabels charterLineLabelX;
  @Bind(R.id.charter_line_with_XLabel) CharterLine charterLineWithLabel;
  @Bind(R.id.charter_bar_XLabel) CharterXLabels charterBarLabelX;
  @Bind(R.id.charter_bar_with_XLabel) CharterBar charterBarWithLabel;
  @Bind(R.id.charter_line_YLabel) CharterYLabels charterLineYLabel;
  @Bind(R.id.charter_bar_YLabel) CharterYLabels charterBarYLabel;
  @Bind(R.id.charter_bar_with_YLabel) CharterBar charterBarWithYLabel;
  @Bind(R.id.charter_line_with_XMarker) CharterLine charterLineWithXMarker;
  @Bind(R.id.charter_line_XMarker) CharterXMarkers charterLineXMarkers;
  @Bind(R.id.charter_bar_with_XMarker) CharterBar charterBarWithXMarker;
  @Bind(R.id.charter_bar_XMarker) CharterXMarkers charterBarXMarkers;
  @Bind(R.id.charter_line_with_YMarker) CharterLine charterLineWithYMarker;
  @Bind(R.id.charter_line_YMarker) CharterYMarkers charterLineYMarkers;
  @Bind(R.id.charter_bar_with_YMarker) CharterBar charterBarWithYMarker;
  @Bind(R.id.charter_bar_YMarker) CharterYMarkers charterBarYMarkers;

  private static final int DEFAULT_ITEMS_COUNT = 15;
  private static final int DEFAULT_RANDOM_VALUE_MIN = 10;
  private static final int DEFAULT_RANDOM_VALUE_MAX = 100;

  private float[] values;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    values =
        fillRandomValues(DEFAULT_ITEMS_COUNT, DEFAULT_RANDOM_VALUE_MAX, DEFAULT_RANDOM_VALUE_MIN);
    Resources res = getResources();
    int[] barColors = new int[] {
        res.getColor(R.color.lightBlue500), res.getColor(R.color.lightBlue400),
        res.getColor(R.color.lightBlue300)
    };

    // charter_line
    charterLine.setValues(values);
    charterLine.setAnimInterpolator(new BounceInterpolator());
    charterLine.setShowGridLines(true);
    charterLine.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        values = fillRandomValues(DEFAULT_ITEMS_COUNT, DEFAULT_RANDOM_VALUE_MAX,
            DEFAULT_RANDOM_VALUE_MIN);
        charterLine.setValues(values);
        charterLine.show();
      }
    });

    // charter_bar
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
    charterBarNoMargin.setValues(values);
    charterBarNoMargin.setBarMargin(0);
    charterBarNoMargin.setColors(barColors);
    charterBarNoMargin.setColorsBackground(new int[] { android.R.color.white });
    charterBarNoMargin.setShowGridLinesX(true);
    charterBarNoMargin.setGridLinesCount(5);
    charterBarNoMargin.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        values = fillRandomValues(DEFAULT_ITEMS_COUNT, DEFAULT_RANDOM_VALUE_MAX,
            DEFAULT_RANDOM_VALUE_MIN);
        charterBarNoMargin.setValues(values);
        charterBarNoMargin.show();
      }
    });

    // charter_line_XLabel
    charterLineLabelX.setStickyEdges(true);
    charterLineLabelX.setValues(values);

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
    charterBarLabelX.setStickyEdges(false);
    charterBarLabelX.setVisibilityPattern(new boolean[] { true, false });
    charterBarLabelX.setValues(values);

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
    charterBarYLabel.setVisibilityPattern(new boolean[] { true, false });
    charterBarYLabel.setValues(values, true);

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

    // charter_line_XMarker
    charterLineWithXMarker.setValues(values);
    charterLineWithXMarker.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        values = fillRandomValues(DEFAULT_ITEMS_COUNT, DEFAULT_RANDOM_VALUE_MAX,
            DEFAULT_RANDOM_VALUE_MIN);
        charterLineWithXMarker.setValues(values);
        charterLineWithXMarker.show();
      }
    });

    charterLineXMarkers.setSeparatorStrokeSize(2);
    charterLineXMarkers.setWidthCorrectionFromCharterLine(charterLineWithXMarker);
    charterLineXMarkers.setMarkersCount(DEFAULT_ITEMS_COUNT);

    // charter_bar_XMarker
    charterBarWithXMarker.setValues(values);
    charterBarWithXMarker.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        values = fillRandomValues(DEFAULT_ITEMS_COUNT, DEFAULT_RANDOM_VALUE_MAX,
            DEFAULT_RANDOM_VALUE_MIN);
        charterBarWithXMarker.setValues(values);
        charterBarWithXMarker.show();
      }
    });

    charterBarXMarkers.setStickyEdges(false);
    charterBarXMarkers.setSeparatorStrokeSize(2);
    charterBarXMarkers.setMarkersCount(DEFAULT_ITEMS_COUNT);

    // charter_line_YMarker
    charterLineWithYMarker.setValues(values);
    charterLineWithYMarker.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        values = fillRandomValues(DEFAULT_ITEMS_COUNT, DEFAULT_RANDOM_VALUE_MAX,
            DEFAULT_RANDOM_VALUE_MIN);
        charterLineWithYMarker.setValues(values);
        charterLineWithYMarker.show();
      }
    });

    charterLineYMarkers.setVisibilityPattern(new boolean[] { true, false });
    charterLineYMarkers.setHeightCorrectionFromCharterLine(charterLineWithYMarker);
    charterLineYMarkers.setMarkersCount(DEFAULT_ITEMS_COUNT);

    // charter_bar_YMarker
    charterBarWithYMarker.setValues(values);
    charterBarWithYMarker.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        values = fillRandomValues(DEFAULT_ITEMS_COUNT, DEFAULT_RANDOM_VALUE_MAX,
            DEFAULT_RANDOM_VALUE_MIN);
        charterBarWithYMarker.setValues(values);
        charterBarWithYMarker.show();
      }
    });

    charterBarYMarkers.setVisibilityPattern(new boolean[] { true, false });
    charterBarYMarkers.setMarkersCount(DEFAULT_ITEMS_COUNT);
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
