package edu.orangecoastcollege.cs273.bwegener.cs273superheroes;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static edu.orangecoastcollege.cs273.bwegener.cs273superheroes.R.xml.preferences;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "Superhero Quiz";

    private static final int SUPERHEROES_IN_QUIZ = 10;

    private Button[] mButtons = new Button[8];
    private List<Superhero> mAllSuperheroesList;
    private List<Superhero> mQuizSuperheroesList;
    private List<Superhero> mFilteredSuperheroesList;
    private Superhero mCorrectSuperhero;
    private int mTotalGuesses;
    private int mCorrectGuesses;
    private SecureRandom rng;
    private Handler handler; // used to delay loading next Superhero
    private LinearLayout[] mLayouts = new LinearLayout[4];

    private TextView mQuestionNumberTextView; // shows the current question number
    private ImageView mSuperheroImageView; // displays a superhero
    private TextView mAnswerTextView; // displays the correct answer
    private TextView mGuessTextView;

    private int mChoices; // stores how many buttons are selected
    private String mSuperhero; // Stores what superhero choice is selected (name, power, thing)

    // Keys used in preferences.xml
    private static final String CHOICES = "pref_numberOfChoices";
    private static final String SUPERHEROES = "pref_powers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mQuizSuperheroesList = new ArrayList<>(SUPERHEROES_IN_QUIZ);
        rng = new SecureRandom();
        handler = new Handler();

        mQuestionNumberTextView = (TextView) findViewById(R.id.questionNumberTextView);
        mSuperheroImageView = (ImageView) findViewById(R.id.superheroImageView);
        mAnswerTextView = (TextView) findViewById(R.id.answerTextView);
        mGuessTextView = (TextView) findViewById(R.id.guessTextView);

        mButtons[0] = (Button) findViewById(R.id.button);
        mButtons[1] = (Button) findViewById(R.id.button2);
        mButtons[2] = (Button) findViewById(R.id.button3);
        mButtons[3] = (Button) findViewById(R.id.button4);
        mButtons[4] = (Button) findViewById(R.id.button5);
        mButtons[5] = (Button) findViewById(R.id.button6);
        mButtons[6] = (Button) findViewById(R.id.button7);
        mButtons[7] = (Button) findViewById(R.id.button8);

        mLayouts[0] = (LinearLayout) findViewById(R.id.row1LinearLayout);
        mLayouts[1] = (LinearLayout) findViewById(R.id.row2LinearLayout);
        mLayouts[2] = (LinearLayout) findViewById(R.id.row3LinearLayout);
        mLayouts[3] = (LinearLayout) findViewById(R.id.row4LinearLayout);


        mQuestionNumberTextView.setText(getString(R.string.question, 1, SUPERHEROES_IN_QUIZ));

        try {
            mAllSuperheroesList = JSONLoader.loadJSONFromAsset(this);
        } catch (IOException e) {
            Log.e("Superheroes Quiz", "Error loading from JSON", e);
        }

        mSuperhero = preferences.getString(SUPERHEROES, "Name");
        mChoices = Integer.parseInt(preferences.getString(CHOICES, "4"));

        updateChoices();
        updateSuperhero();
        resetQuiz();
    }


    private void resetQuiz() {
        mCorrectGuesses = 0;
        mTotalGuesses = 0;
        mQuizSuperheroesList.clear();

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
            Drawable image = Drawable.createFromStream(stream, mCorrectSuperhero.getName());
            mSuperheroImageView.setImageDrawable(image);
        } catch (IOException e) {
            Log.e(TAG, "Error loading image" + mCorrectSuperhero.getFileName(), e);
        }

        do {
            Collections.shuffle(mAllSuperheroesList);
        }
        while (mAllSuperheroesList.subList(0, mButtons.length).contains(mCorrectSuperhero));

        for (int i = 0; i < mButtons.length; i++) {
            mButtons[i].setEnabled(true);
            mButtons[i].setText(mAllSuperheroesList.get(i).getUserName());
        }

        mButtons[rng.nextInt(mButtons.length)].setText(mCorrectSuperhero.getUserName());

    }

    public void makeGuess(View v) {

        Button clickedButton = (Button) v;

        String guess = clickedButton.getText().toString();

        mTotalGuesses++;
        if (guess.equals(mCorrectSuperhero.getUserName())) {
            for (Button b : mButtons)
                b.setEnabled(false);
            mCorrectGuesses++;
            mAnswerTextView.setText(mCorrectSuperhero.getUserName());
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
                builder.setMessage(getString(R.string.results, mTotalGuesses, (double) mCorrectGuesses / mTotalGuesses * 100));
                builder.setPositiveButton(getString(R.string.reset_quiz), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_settings, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);

        return super.onOptionsItemSelected(item);
    }

    SharedPreferences.OnSharedPreferenceChangeListener mPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            switch (key) {
                case CHOICES:
                    mChoices = Integer.parseInt(sharedPreferences.getString(CHOICES, "4"));
                    updateChoices();
                    resetQuiz();
                    break;
                case SUPERHEROES:
                    mSuperhero = sharedPreferences.getString(SUPERHEROES, "Name");
                    updateSuperhero();
                    resetQuiz();
                    break;
            }

            Toast.makeText(QuizActivity.this, R.string.restarting_quiz, Toast.LENGTH_SHORT).show();
        }
    };

    private void updateChoices() {
        for (int i = 0; i < mLayouts.length; i++) {
            if (i < mChoices / 2) {
                mLayouts[i].setEnabled(true);
                mLayouts[i].setVisibility(View.VISIBLE);
            } else {
                mLayouts[i].setEnabled(false);
                mLayouts[i].setVisibility(View.GONE);
            }
        }
    }

    private void updateSuperhero() {
        if (mSuperhero.equals("Name"))
            mFilteredSuperheroesList = new ArrayList<>(mAllSuperheroesList);
        else {
            mFilteredSuperheroesList = new ArrayList<>();

            for (Superhero s : mAllSuperheroesList) {
                if (s.getName().equals(mSuperhero))
                    mFilteredSuperheroesList.add(s);
            }
        }
    }

}
