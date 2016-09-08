package xyz.beomyong.taoistfan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import xyz.beomyong.taoistfan.common.BaseFragmentActivity;

/**
 * Created by BeomyongChoi on 9/5/16
 */
public class MainActivity extends BaseFragmentActivity {

    public String mQuestionTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView titleTextView = (TextView) toolbar.findViewById(R.id.toolbarTitle);
        titleTextView.setText(R.string.title);
        titleTextView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/GodoM.otf"));

        ImageView toolbarBackButtonImageView = (ImageView) toolbar.findViewById(R.id.toolbarBackButton);
        toolbarBackButtonImageView.setVisibility(View.INVISIBLE);
    }

    public void fabOnClick(final View view) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.question_title_dialog, null);
        dialogBuilder.setView(dialogView);

//        final ClearEditText titleEditText = (ClearEditText) dialogView.findViewById(R.id.questionTitleEditText);
        final EditText titleEditText = (EditText) dialogView.findViewById(R.id.questionTitleEditText);

        dialogBuilder.setTitle(R.string.title);
        dialogBuilder.setMessage(R.string.question_title);
        dialogBuilder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        dialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Snackbar snackbar;
                snackbar = Snackbar.make(view, R.string.see_you_later, Snackbar.LENGTH_SHORT);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.obdefaultWhite));
                TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorTitle));
                snackbar.setDuration(1000).show();
            }
        });

        final AlertDialog alertDialog = dialogBuilder.create();

        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mQuestionTitle = titleEditText.getText().toString();
                if (mQuestionTitle.length() == 0) {
                    Snackbar snackbar;
                    snackbar = Snackbar.make(v, R.string.empty_question, Snackbar.LENGTH_SHORT);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.obdefaultWhite));
                    TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorTitle));
                    snackbar.setDuration(500).show();
                } else if (mQuestionTitle.length() <= 20) {
                    Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("questionTitle", mQuestionTitle);
                    startActivity(intent);
                } else {
                    Snackbar snackbar;
                    snackbar = Snackbar.make(v, R.string.too_long_question, Snackbar.LENGTH_SHORT);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.obdefaultWhite));
                    TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorTitle));
                    snackbar.setDuration(500).show();
                }
            }
        });

        titleEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick();
                }
                return true;
            }
        });
    }

}
