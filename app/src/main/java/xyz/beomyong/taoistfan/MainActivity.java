package xyz.beomyong.taoistfan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import xyz.beomyong.taoistfan.common.BaseFragmentActivity;
import xyz.beomyong.taoistfan.common.ClearEditText;

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

        final ClearEditText titleEditText = (ClearEditText) dialogView.findViewById(R.id.questionTitleEditText);

        dialogBuilder.setTitle(R.string.title);
        dialogBuilder.setMessage(R.string.question_title);
        dialogBuilder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                mQuestionTitle = titleEditText.getText().toString();
                Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("questionTitle", mQuestionTitle);
                startActivity(intent);
            }
        });

        dialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Snackbar.make(view, R.string.see_you_later, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final AlertDialog alertDialog = dialogBuilder.create();

        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        alertDialog.show();

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
