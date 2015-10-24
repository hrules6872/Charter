package com.hrules.charter.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.hrules.charter.CharterLine;
import com.hrules.charter.CharterXLabels;
import com.hrules.charter.CharterYLabels;

import java.util.Random;

public class XYLineActivity extends AppCompatActivity {

    private CharterYLabels mYlableCharterYLabels;
    private CharterLine mLineCharterLine;
    private LinearLayout mLinearLinearLayout;
    private CharterXLabels mXlableCharterXLabels;
    private float[] valueX;
    private float[] valueY;
    private float[] valueLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xyline);
        mYlableCharterYLabels = (CharterYLabels) findViewById(R.id.ylable);
        mLineCharterLine = (CharterLine) findViewById(R.id.charter_line);
        mLinearLinearLayout = (LinearLayout) findViewById(R.id.linear);
        mXlableCharterXLabels = (CharterXLabels) findViewById(R.id.xlable);
        valueX = fillRandomValues(15,200,0);
        valueY = fillRandomValues(7,500,10);
        valueLine = fillRandomValues(15,500,10);

        mXlableCharterXLabels.setValues(valueX);
        mYlableCharterYLabels.setValues(valueY);
        mLineCharterLine.setValues(valueLine);
        mLineCharterLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valueX = fillRandomValues(15,200,0);
                valueY = fillRandomValues(7,500,10);
                valueLine = fillRandomValues(15,500,10);
                mXlableCharterXLabels.setValues(valueX);
                mYlableCharterYLabels.setValues(valueY);
                mLineCharterLine.setValues(valueLine);
                mLineCharterLine.show();
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
