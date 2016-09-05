package xyz.beomyong.taoistfan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import xyz.beomyong.taoistfan.common.BaseFragmentActivity;
import xyz.beomyong.taoistfan.common.ClearEditText;

/**
 * Created by BeomyongChoi on 9/5/16
 */
public class QuestionActivity extends BaseFragmentActivity {

    String mQuestionTitle;
    ClearEditText mChoicesEditText;
    Button mButtonAdd;
    LinearLayout mContainer;

    ArrayList<String> mChoicesArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        mQuestionTitle = intent.getExtras().getString("questionTitle");

        TextView titleTextView = (TextView) toolbar.findViewById(R.id.toolbarTitle);
        titleTextView.setText(mQuestionTitle);
        titleTextView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/GodoM.otf"));

        toolbar.findViewById(R.id.toolbarBackButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mChoicesEditText = (ClearEditText) findViewById(R.id.choicesEditText);

        mButtonAdd = (Button) findViewById(R.id.add);
        mContainer = (LinearLayout) findViewById(R.id.container);

        mChoicesEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mButtonAdd.performClick();
                }
                return true;
            }
        });

        mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mChoicesEditText.getText().toString().length() > 0) {
                    LayoutInflater layoutInflater =
                            (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View addView = layoutInflater.inflate(R.layout.question_choices_row, null);
                    TextView choicesTextView = (TextView) addView.findViewById(R.id.choicesTextView);
                    choicesTextView.setText(mChoicesEditText.getText().toString());
                    mChoicesEditText.setText("");

                    Button buttonRemove = (Button) addView.findViewById(R.id.remove);

                    final View.OnClickListener thisListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ((LinearLayout) addView.getParent()).removeView(addView);
                        }
                    };

                    buttonRemove.setOnClickListener(thisListener);
                    mContainer.addView(addView);
                } else {
                    Snackbar.make(view, R.string.blank_edittext, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }

    public void fabOnClick(final View view) {
        int count;

        RelativeLayout choicesRelativeLayout;
        TextView choicesTextView;

        count = mContainer.getChildCount();

        mChoicesArrayList = new ArrayList<>();
        if (count > 1) {
            for (int i = 0; i < count; i++) {
                choicesRelativeLayout = (RelativeLayout) mContainer.getChildAt(i);
                choicesTextView = (TextView) choicesRelativeLayout.getChildAt(0);
                mChoicesArrayList.add(choicesTextView.getText().toString());
            }

            Intent intent = new Intent(QuestionActivity.this, ResultActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("count", count);
            intent.putExtra("choices", mChoicesArrayList);
            startActivity(intent);
        } else {
            Snackbar.make(view, R.string.one_more_choices, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }
}