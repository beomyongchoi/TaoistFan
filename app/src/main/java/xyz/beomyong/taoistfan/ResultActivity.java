package xyz.beomyong.taoistfan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import xyz.beomyong.taoistfan.common.BYConstants;
import xyz.beomyong.taoistfan.common.BaseFragmentActivity;

/**
 * Created by BeomyongChoi on 9/5/16
 */
public class ResultActivity extends BaseFragmentActivity {

    String mQuestionTitle;
    int mCount;
    String[] mChoicesArray;

    float[] mResultArray;

    LinearLayout mContainer;

    private PieChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView titleTextView = (TextView) toolbar.findViewById(R.id.toolbarTitle);
        titleTextView.setText(R.string.result_title);
        titleTextView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/GodoM.otf"));

        toolbar.findViewById(R.id.toolbarBackButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        mQuestionTitle = intent.getExtras().getString("questionTitle");
        mCount = intent.getExtras().getInt("count");
        mChoicesArray = intent.getExtras().getStringArray("choices");

        mResultArray = new float[mCount];
        Arrays.fill(mResultArray, 0);

        dosanim(mResultArray);

        mChart = (PieChart) findViewById(R.id.chart);
        mChart.setUsePercentValues(true);
        mChart.setDescription(mQuestionTitle);

        // enable hole and configure
        mChart.setDrawHoleEnabled(false);
//        mChart.setHoleRadius(7);
        mChart.setTransparentCircleRadius(10);

        // enable rotation of the chart by touch
        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);

        // set a chart value selected listener
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null)
                    return;

                Toast.makeText(ResultActivity.this,
                        mChoicesArray[(int) e.getX()] + " = " + e.getData() + "%", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        // add data
        addData();

        // customize legends
        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);

        mContainer = (LinearLayout) findViewById(R.id.container);

        for (int i = 0; i < mCount ; i++) {
            LayoutInflater layoutInflater =
                    (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View addView = layoutInflater.inflate(R.layout.question_results_row, null);
            TextView choicesTextView = (TextView) addView.findViewById(R.id.choicesTextView);
            TextView resultsTextView = (TextView) addView.findViewById(R.id.resultValueTextView);
            choicesTextView.setText(mChoicesArray[i]);
            resultsTextView.setText(mResultArray[i] + "");

            mContainer.addView(addView);
        }
    }

    public void dosanim(float array[]) {
        Random random = new Random(System.currentTimeMillis());
//        double multiplier;
        double desiredStandardDeviation, desiredMean;

        desiredStandardDeviation = 10000;
        desiredMean = 1000;
        for (int i = 0; i < mCount; i++) {
            array[i] += Math.abs(random.nextGaussian() * desiredStandardDeviation + desiredMean);
        }
    }

    private void addData() {
        List<PieEntry> yVals1 = new ArrayList<>();
        List<String> xVals = new ArrayList<>();

        for (int i = 0; i < mCount; i++) {
            yVals1.add(new PieEntry(mResultArray[i] * mResultArray[i], i));
            xVals.add(mChoicesArray[i]);
        }

        // create pie data set
        PieDataSet dataSet = new PieDataSet(yVals1, "그래");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        // add many colors
        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        // instantiate pie data object now
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.GRAY);

        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        // update pie chart
        mChart.invalidate();
    }
}
