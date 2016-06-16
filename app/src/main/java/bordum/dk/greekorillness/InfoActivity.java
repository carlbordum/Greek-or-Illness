package bordum.dk.greekorillness;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {

    private static final String CURRENT_QUESTION = "CURRENT_QUESTION";
    private static final String STREAK = "STREAK";
    private static final String BEST_STREAK = "BEST_STREAK";

    Intent receiver;
    Intent sender;
    String question;
    int streak;
    int bestStreak;

    String correct;
    String incorrect;

    TextView tvStreakCounter;
    TextView tvBestStreak;
    TextView tvResult;
    TextView tvInfo;
    Button btnContinue;
    RelativeLayout layout;
    Typeface regularTypeface;
    Typeface boldTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        receiver = getIntent();
        streak = receiver.getIntExtra(STREAK, 999);
        if(streak == 0){
            setTheme(R.style.IncorrectTheme);
        }
        else{
            setTheme(R.style.CorrectTheme);
        }

        setContentView(R.layout.activity_info);

        tvStreakCounter = (TextView) findViewById(R.id.streak_counter);
        tvBestStreak = (TextView) findViewById(R.id.best_streak_view);
        tvResult = (TextView) findViewById(R.id.result);
        tvInfo = (TextView) findViewById(R.id.info);
        btnContinue = (Button) findViewById(R.id.continueButton);
        layout = (RelativeLayout) findViewById(R.id.info_layout);
        regularTypeface = Typeface.createFromAsset(getAssets(), "fonts/Lithos-Pro-Regular.ttf");
        boldTypeface = Typeface.createFromAsset(getAssets(), "fonts/Lithos-Pro-Black.ttf");

        question = receiver.getStringExtra(CURRENT_QUESTION);

        bestStreak = receiver.getIntExtra(BEST_STREAK, 999);
        correct = getResources().getString(R.string.correct);
        incorrect = getResources().getString(R.string.incorrect);

        if(streak == 0){
            tvResult.setText(incorrect);
            layout.setBackgroundColor(getResources().getColor(R.color.colorIncorrect));
            btnContinue.setBackgroundColor(getResources().getColor(R.color.colorIncorrectDark));
        }else{
            tvResult.setText(correct);
            layout.setBackgroundColor(getResources().getColor(R.color.colorCorrect));
            btnContinue.setBackgroundColor(getResources().getColor(R.color.colorCorrectDark));
        }

        tvStreakCounter.setText(getString(R.string.x_suffix, streak));
        tvBestStreak.setText(getString(R.string.x_suffix, bestStreak));
        tvInfo.setText(question);

        addFontToLayout();

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_OK, sender);
                finish();
                overridePendingTransition(0, 0);
            }
        });
    }


    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setMessage(getResources().getString(R.string.warning_msg))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        setResult(Activity.RESULT_CANCELED, sender);
                        InfoActivity.this.finish();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.no), null)
                .show();
    }


    public void addFontToLayout() {

        tvResult.setTypeface(boldTypeface);
        //tvInfo.setTypeface(regularTypeface);
        tvBestStreak.setTypeface(boldTypeface);
        tvStreakCounter.setTypeface(boldTypeface);

        btnContinue.setTypeface(regularTypeface);

    }
}
