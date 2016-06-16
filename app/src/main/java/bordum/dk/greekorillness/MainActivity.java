package bordum.dk.greekorillness;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String GREEK_OR_ILLNESS = "GREEK_OR_ILLNESS";
    private static final String AVAILABLE_GREEKS = "AVAILABLE_GREEKS";
    private static final String AVAILABLE_ILLNESSES = "AVAILABLE_ILLNESSES";
    private static final String CURRENT_QUESTION = "CURRENT_QUESTION";
    private static final String STREAK = "STREAK";
    private static final String BEST_STREAK = "BEST_STREAK";
    private static final int REQUEST_CODE = 1;

    private SharedPreferences sharedPreferences;
    private List<String> availableGreeks;
    private List<String> availableIllnesses;
    private Question currentQuestion;
    private int streak;
    private int bestStreak;

    TextView tvQuestion;
    TextView tvStreakCounter;
    TextView tvBestStreak;
    Button btnGreek;
    Button btnIllness;
    Typeface regularTypeface;
    Typeface boldTypeface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvQuestion = (TextView) findViewById(R.id.question);
        tvStreakCounter = (TextView) findViewById(R.id.streak_counter);
        tvBestStreak = (TextView) findViewById(R.id.best_streak_view);
        btnGreek = (Button) findViewById(R.id.greekButton);
        btnIllness = (Button) findViewById(R.id.illnessButton);
        regularTypeface = Typeface.createFromAsset(getAssets(), "fonts/Lithos-Pro-Regular.ttf");
        boldTypeface = Typeface.createFromAsset(getAssets(), "fonts/Lithos-Pro-Black.ttf");

        sharedPreferences = getSharedPreferences(GREEK_OR_ILLNESS, Context.MODE_PRIVATE);
        bestStreak = getBestStreak();

        populateLists();
        addFontToLayout();


        btnGreek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkResult(true);
            }
        });

        btnIllness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkResult(false);
            }
        });

    }


    @Override
    public void onPostCreate(Bundle savedInstanceState){

        super.onPostCreate(savedInstanceState);

        if (savedInstanceState == null) {
            streak = 0;
            changeCurrentQuestion();
        } else {
            tvQuestion.setText(currentQuestion.getQuestion());
            tvStreakCounter.setText(getString(R.string.x_suffix, streak));
        }

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putStringArrayList(AVAILABLE_GREEKS, new ArrayList<>(availableGreeks));
        outState.putStringArrayList(AVAILABLE_ILLNESSES, new ArrayList<>(availableIllnesses));
        outState.putString(CURRENT_QUESTION, currentQuestion.toString());
        outState.putInt(STREAK, streak);
        outState.putInt(BEST_STREAK, bestStreak);

    }


    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
        availableGreeks = savedInstanceState.getStringArrayList(AVAILABLE_GREEKS);
        availableIllnesses = savedInstanceState.getStringArrayList(AVAILABLE_ILLNESSES);
        currentQuestion = new Question(savedInstanceState.getString(CURRENT_QUESTION));
        streak = savedInstanceState.getInt(STREAK);
        bestStreak = savedInstanceState.getInt(BEST_STREAK);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                changeCurrentQuestion();
                tvStreakCounter.setText(getString(R.string.x_suffix, streak));
            }
            if(resultCode == Activity.RESULT_CANCELED){
                finish();
            }
        }

    }


    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setMessage(getResources().getString(R.string.warning_msg))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.no), null)
                .show();
    }


    public void checkResult(Boolean type) {

        if (type == currentQuestion.getIsItAGreek()) {
            streak++;
            if (streak > bestStreak) {
                setBestStreak();
                bestStreak = getBestStreak();
            }
        } else {
            streak = 0;
        }
        if (availableGreeks.size() < 1 && availableIllnesses.size() < 1) populateLists();

        //tvStreakCounter.setText(getString(R.string.x_suffix, streak));
        showInfoScreen();
        overridePendingTransition(0, 0);

    }


    public void changeCurrentQuestion() {

        int type = (Math.random() <= 0.5) ? 1 : 2;

        if (type == 1 && availableGreeks.size() > 0)
            currentQuestion = buildQuestion(availableGreeks, true);
        else if (availableIllnesses.size() > 0)
            currentQuestion = buildQuestion(availableIllnesses, false);
        else if (availableGreeks.size() > 0) currentQuestion = buildQuestion(availableGreeks, true);

        tvQuestion.setText(currentQuestion.getQuestion());

    }


    public Question buildQuestion(List<String> list, boolean isItAGreek) {

        int typeIndex = (int) (Math.random() * list.size());

        String type = list.get(typeIndex);
        list.remove(typeIndex);

        return new Question(type, isItAGreek);

    }


    public void setBestStreak() {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(BEST_STREAK, streak);
        editor.apply();

    }


    public int getBestStreak() {

        int tempBestStreak = sharedPreferences.getInt(BEST_STREAK, 0);
        tvBestStreak.setText(getString(R.string.x_suffix, tempBestStreak));
        return tempBestStreak;

    }


    public void populateLists() {

        availableGreeks = new LinkedList<>(Arrays.asList(getResources().getStringArray(R.array.greeks)));
        availableIllnesses = new LinkedList<>(Arrays.asList(getResources().getStringArray(R.array.illnesses)));

    }


    public void addFontToLayout() {

        tvQuestion.setTypeface(regularTypeface);
        tvBestStreak.setTypeface(boldTypeface);
        tvStreakCounter.setTypeface(boldTypeface);

        btnGreek.setTypeface(regularTypeface);
        btnIllness.setTypeface(regularTypeface);

    }


    public void showInfoScreen(){

        Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
        intent.putExtra(STREAK, streak);
        intent.putExtra(BEST_STREAK, bestStreak);
        intent.putExtra(CURRENT_QUESTION, currentQuestion.getQuestionWithDescription());

        startActivityForResult(intent, REQUEST_CODE);

    }


}