package xyz.beomyong.taoistfan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import xyz.beomyong.taoistfan.common.BYConstants;
import xyz.beomyong.taoistfan.common.BaseFragmentActivity;

/**
 * Created by BeomyongChoi on 9/5/16
 */
public class ResultActivity extends BaseFragmentActivity {

    ArrayList<String> mChoicesArrayList;
    int mCount;

    LinearLayout mContainer;

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
        mCount = intent.getExtras().getInt("count");
        mChoicesArrayList = (ArrayList<String>) intent.getSerializableExtra("choices");

        int resultArray[] =  new int[mCount];
        Arrays.fill(resultArray, 0);

        dosanim(resultArray);

        mContainer = (LinearLayout) findViewById(R.id.container);

        for (int i = 0; i <mCount ; i++) {
            LayoutInflater layoutInflater =
                    (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View addView = layoutInflater.inflate(R.layout.question_results_row, null);
            TextView choicesTextView = (TextView) addView.findViewById(R.id.choicesTextView);
            TextView resultsTextView = (TextView) addView.findViewById(R.id.resultValueTextView);
            choicesTextView.setText(mChoicesArrayList.get(i));
            resultsTextView.setText(resultArray[i] + "");

            mContainer.addView(addView);
        }
    }

    public void dosanim(int array[]) {
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < BYConstants.DICE; i++) {
            array[random.nextInt(mCount)] += 1;
        }
    }
}
