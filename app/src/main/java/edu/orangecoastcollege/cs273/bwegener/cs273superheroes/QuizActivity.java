package edu.orangecoastcollege.cs273.bwegener.cs273superheroes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * This <code>QuizActivity</code> is where all the activities associated to the quiz
 * take place. The user gets to see a picture of a "Superhero" and then based on which
 * quiz they are taking answer the question.
 *
 * @author Brian Wegener
 * @version 1.0
 *          <p>
 *          Created by bwegener on 10/9/2017
 */
public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "Superhero Quiz";

    private static final int SUPERHEROES_IN_QUIZ = 10;

    private Button[] mButtons = new Button[4];
    private List<Superhero> mAllSuperheroesList;
    private List<Superhero> mQuizSuperheroesList;
    private List<String> mAllSuperheroNamesList;
    private List<String> mAllSuperheroPowersList;
    private List<String> mAllSuperheroThingsList;
    private List<String> mAllSuperheroTypeList;

    private Superhero mCorrectSuperhero;
    private String mCorrectAnswer;

    private int mTotalGuesses;
    private int mCorrectGuesses;

    private SecureRandom rng;
    private Handler handler; // used to delay loading next Superhero

    private TextView mQuestionNumberTextView; // shows the current question number
    private ImageView mSuperheroImageView; // displays a superhero
    private TextView mAnswerTextView; // displays the correct answer
    private TextView mGuessTextView;

    private String mQuizType; // Stores what superhero choice is selected (name, power, thing)

    private SharedPreferences.OnSharedPreferenceChangeListener mPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        /**
         * This is where the sharedPreferences are changed.
         */
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            mQuizType = sharedPreferences.getString(getString(R.string.pref_key), getString(R.string.pref_default));
            resetQuiz();

            Toast.makeText(QuizActivity.this, R.string.restarting_quiz, Toast.LENGTH_SHORT).show();
        }
    };


    /**
     * This is what happens when the app is first loaded.
     * It instantiates all of the views, loads the JSON,
     * and makes sure that everything is good to go.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mQuestionNumberTextView = (TextView) findViewById(R.id.questionNumberTextView);
        mSuperheroImageView = (ImageView) findViewById(R.id.superheroImageView);
        mGuessTextView = (TextView) findViewById(R.id.guessTextView);
        mAnswerTextView = (TextView) findViewById(R.id.answerTextView);

        mButtons[0] = (Button) findViewById(R.id.button);
        mButtons[1] = (Button) findViewById(R.id.button2);
        mButtons[2] = (Button) findViewById(R.id.button3);
        mButtons[3] = (Button) findViewById(R.id.button4);

        try {
            mAllSuperheroesList = JSONLoader.loadJSONFromAsset(this);
        } catch (IOException e) {
            Log.e(TAG, "Error loading from JSON", e);
        }

        mAllSuperheroNamesList = new ArrayList<>();
        mAllSuperheroPowersList = new ArrayList<>();
        mAllSuperheroThingsList = new ArrayList<>();

        for (Superhero s : mAllSuperheroesList) {
            mAllSuperheroNamesList.add(s.getName());
            mAllSuperheroPowersList.add(s.getPower());
            mAllSuperheroThingsList.add(s.getThing());
        }
        mQuizSuperheroesList = new ArrayList<>(SUPERHEROES_IN_QUIZ);

        rng = new SecureRandom();
        handler = new Handler();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(mPreferenceChangeListener);

        mQuizType = preferences.getString(getString(R.string.pref_key), getString(R.string.pref_default));

        resetQuiz();
    }


    private void resetQuiz() {
        mTotalGuesses = 0;
        mCorrectGuesses = 0;
        mQuizSuperheroesList.clear();

        mGuessTextView.setText(getString(R.string.guess, mQuizType));

        while (mQuizSuperheroesList.size() < SUPERHEROES_IN_QUIZ) {
            int randomPosition = rng.nextInt(mAllSuperheroesList.size());
            if (!mQuizSuperheroesList.contains(mAllSuperheroesList.get(randomPosition)))
                mQuizSuperheroesList.add(mAllSuperheroesList.get(randomPosition));
        }

        loadNextSuperhero();
    }


    private void loadNextSuperhero() {

        mCorrectSuperhero = mQuizSuperheroesList.remove(0);

        mAnswerTextView.setText("");

        int questionNumber = SUPERHEROES_IN_QUIZ - mQuizSuperheroesList.size();
        mQuestionNumberTextView.setText(getString(R.string.question, questionNumber, SUPERHEROES_IN_QUIZ));

        try {
            InputStream stream = getAssets().open(mCorrectSuperhero.getFileName());
            Drawable image = Drawable.createFromStream(stream, mCorrectSuperhero.getUserName());
            mSuperheroImageView.setImageDrawable(image);
        } catch (IOException e) {
            Log.e(TAG, "Error loading image: " + mCorrectSuperhero.getFileName(), e);
        }

        if (mQuizType.equals(getString(R.string.name_type)))
        {
            mAllSuperheroTypeList = new ArrayList<>(mAllSuperheroNamesList);
            mCorrectAnswer = mCorrectSuperhero.getName();
        } else if (mQuizType.equals(getString(R.string.power_type)))
        {
            mAllSuperheroTypeList = new ArrayList<>(mAllSuperheroPowersList);
            mCorrectAnswer = mCorrectSuperhero.getPower();
        } else if (mQuizType.equals(getString(R.string.one_thing_type)))
        {
            mAllSuperheroTypeList = new ArrayList<>(mAllSuperheroThingsList);
            mCorrectAnswer = mCorrectSuperhero.getThing();
        }

        do {
            Collections.shuffle(mAllSuperheroTypeList);
        }
        while (mAllSuperheroTypeList.subList(0, mButtons.length).contains(mCorrectAnswer));

        for (int i = 0; i < mButtons.length; i++) {
            mButtons[i].setEnabled(true);
            mButtons[i].setText(mAllSuperheroTypeList.get(i));
        }

        mButtons[rng.nextInt(mButtons.length)].setText(mCorrectAnswer);

    }

    /**
     * This is the view that happens when the user makes a guess.
     *
     * @param v
     */
    public void makeGuess(View v) {

        Button clickedButton = (Button) v;
        String guess = clickedButton.getText().toString();

        mTotalGuesses++;

        if (guess.equals(mCorrectAnswer)) {
            mCorrectGuesses++;
            for (Button b : mButtons)
                b.setEnabled(false);
            mAnswerTextView.setText(mCorrectAnswer);
            mAnswerTextView.setTextColor(ContextCompat.getColor(this, R.color.correct_answer));


            if (mCorrectGuesses < SUPERHEROES_IN_QUIZ) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextSuperhero();
                    }

                }, 2000);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.results, mTotalGuesses, 100.0 * mCorrectGuesses / mTotalGuesses));
                builder.setPositiveButton(getString(R.string.reset_quiz), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        resetQuiz();
                    }
                });
                builder.setCancelable(false);
                builder.create();
                builder.show();
            }
        } else {
            clickedButton.setEnabled(false);
            mAnswerTextView.setText(getString(R.string.incorrect_answer));
            mAnswerTextView.setTextColor(ContextCompat.getColor(this, R.color.incorrect_answer));
        }


    }

    /**
     * This is what happens to create the options menu.
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This is what item is selected in the options menu
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);

        return super.onOptionsItemSelected(item);
    }
}
