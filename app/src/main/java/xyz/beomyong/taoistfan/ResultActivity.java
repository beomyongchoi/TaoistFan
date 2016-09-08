package xyz.beomyong.taoistfan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import xyz.beomyong.taoistfan.common.BYConstants;
import xyz.beomyong.taoistfan.common.BaseFragmentActivity;

/**
 * Created by BeomyongChoi on 9/5/16
 */
public class ResultActivity extends BaseFragmentActivity {

    String mQuestionTitle;
    int mCount;
    String[] mChoicesArray;

    int[] mResultArray;

    LinearLayout mContainer;

    private Handler mUiHandler = new Handler();

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

        mResultArray = new int[mCount];
        Arrays.fill(mResultArray, 0);

        addResultView();

        Thread myThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Random random = new Random(System.currentTimeMillis());
                int[] temp = new int[mCount];
                int maxDice, maxDiceIndex;

                RelativeLayout choicesRelativeLayout;
                TextView choicesTextView;

                while (true) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(5);
                        Log.d("TAG", "i am here");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Arrays.fill(temp, 0);
                    for (int j = 0; j < BYConstants.DICE; j++) temp[random.nextInt(mCount)] += 1;

                    maxDice = temp[0];
                    maxDiceIndex = 0;

                    for (int j = 1; j < mCount; j++) {
                        if (temp[j] >= maxDice) {
                            maxDice = temp[j];
                            maxDiceIndex = j;
                        }
                    }
                    mResultArray[maxDiceIndex] += 1;

                    choicesRelativeLayout = (RelativeLayout) mContainer.getChildAt(maxDiceIndex);
                    choicesTextView = (TextView) choicesRelativeLayout.getChildAt(1);

                    final int finalMaxDiceIndex = maxDiceIndex;
                    final TextView finalChoicesTextView = choicesTextView;

                    mUiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            finalChoicesTextView.setText(mResultArray[finalMaxDiceIndex] + "");
                        }
                    });
                    if (mResultArray[maxDiceIndex] == 10) break;
                }
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
                        Snackbar snackbar;
                        snackbar = Snackbar.make(coordinatorLayout, R.string.finish, Snackbar.LENGTH_SHORT);
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.obdefaultWhite));
                        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.obdefaultBlack));
                        snackbar.setDuration(2000).show();
                    }
                });
            }
        });
        myThread.start();

    }

    private void addResultView() {
        mContainer = (LinearLayout) findViewById(R.id.container);

        for (int i = 0; i < mCount ; i++) {
            LayoutInflater layoutInflater =
                    (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View addView = layoutInflater.inflate(R.layout.question_results_row, null);
            TextView choicesTextView = (TextView) addView.findViewById(R.id.choicesTextView);
            TextView resultsTextView = (TextView) addView.findViewById(R.id.resultValueTextView);
            choicesTextView.setText(mChoicesArray[i]);
            resultsTextView.setText(0 + "");
            mContainer.addView(addView);
        }
    }

}
