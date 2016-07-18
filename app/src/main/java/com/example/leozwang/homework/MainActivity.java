package com.example.leozwang.homework;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ProgressBar;

import org.w3c.dom.Text;

import java.util.Random;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final int TOTAL_QUESTIONS = 5;

    private TextView questionTextView;
    private EditText answerText;
    private Button nextButton;
    private TextView timerText;
    private TextView numberTextView;
    private ProgressBar progressBar;

    private int currentQuestionIndex = 0;
    private int totalQuestions = 0;
    private int totalCorrectQuestions = 0;
    private boolean answerIsWrong = false;

    private int a = 0;
    private int b = 0;
    private int x = 0;
    private int answer = 0;

    private Timer timer;
    private int count = 0;
    long startTime = 0;

    Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        questionTextView = (TextView) findViewById(R.id.questionTextView);
        questionTextView.setText("");

        answerText = (EditText) findViewById(R.id.answerText);
        answerText.setText("");

        nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setText("Start");

        timerText = (TextView) findViewById(R.id.timerTextView);
        timerText.setText("--:--");

        numberTextView = (TextView) findViewById(R.id.numberTextView);
        numberTextView.setText("");

        random = new Random();
        Date date = new Date();
        random.setSeed(date.getTime());

        timer = new Timer();

        progressBar = (ProgressBar) findViewById (R.id.progressBar);
        progressBar.setProgress(0);
        progressBar.setMax(TOTAL_QUESTIONS);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showNextQuestion(View view) {
        Log.d("???", "???");

        if (currentQuestionIndex == 0) {
            prepareNextQuestion();
            return;
        }

        int answered = toInt(answerText.getText().toString());
        int expected = answer;

        Log.d("???", "answered: " + answered + " expected: " + expected);

        if (answered == expected) {
            if (answerIsWrong) {
                answerIsWrong = false;
            } else {
                totalCorrectQuestions++;
            }
            Log.d("???", "right");
            prepareNextQuestion();

            answerText.setText("");
            answerText.setBackgroundColor(Color.WHITE);
        } else {
            answerIsWrong = true;
            answerText.setText("");
            answerText.setBackgroundColor(Color.RED);
            Log.d("???", "wrong");
        }
    }

    private String toString(int in) {
        return String.valueOf(in);
    }

    private int toInt(String in) {
        // Default is always 0
        if (TextUtils.isEmpty(in)) {
            return 0;
        }
        return Integer.parseInt(in);
    }

    private void prepareNextQuestion() {
        if (currentQuestionIndex >= TOTAL_QUESTIONS) {
            Log.d("???", "stop timer");
            currentQuestionIndex = 0;
            stopTimer();

            questionTextView.setText(String.format("Wrong: %d", TOTAL_QUESTIONS - totalCorrectQuestions));
            nextButton.setText("Excelletn!!! Restart?");
            return;
        }

        if (currentQuestionIndex == 0) {
            totalCorrectQuestions = 0;
            answerIsWrong = false;

            startTimer();
        }

        nextButton.setText("Next");
        numberTextView.setText(toString(currentQuestionIndex + 1));

        int operation = getRandomNumber(1, 4);
        if (operation == 1) {
            a = getRandomNumber(0, 50);
            b = getRandomNumber(1, 50);
            int sum = a + b;
            answer = sum;
            questionTextView.setText(String.format("%d + %d =", a, b));
        } else if (operation == 2) {
            a = getRandomNumber(0, 50);
            b = getRandomNumber(1, 100);
            int sum = a + b;
            answer = a;
            questionTextView.setText(String.format("%d - %d =", sum, b));
        } else if (operation == 3) {
            a = getRandomNumber(0, 45);
            b = getRandomNumber(1, 10);
            int result = a * b;
            answer = result;
            questionTextView.setText(String.format("%d X %d =", a, b));
        } else if (operation == 4) {
            a = getRandomNumber(1, 10);
            b = getRandomNumber(1, 10);
            int result = a * b;
            answer = b;
            questionTextView.setText(String.format("%d / %d =", result, a));
        }

        currentQuestionIndex++;
        progressBar.incrementProgressBy(1);
    }

    private int getRandomNumber(int from, int to) {
        return random.nextInt(to - from) + from;
    }

    /**
     * Start local timer.
     */
    private void startTimer() {
        if (timer == null) {
            timer = new Timer();
        }
        startTime = System.currentTimeMillis();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        long now = System.currentTimeMillis();
                        long seconds = (now - startTime) / 1000;
                        long mins = seconds / 60;
                        seconds = seconds % 60;
                        timerText.setText(String.format("%d\' : %d\" ", mins, seconds));
                    }
                });
            }
        }, 1000, 1000);
    }

    private void stopTimer() {
        timer.cancel();
        timer = null;
    }
}
