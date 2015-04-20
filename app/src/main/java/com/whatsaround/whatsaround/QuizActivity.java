package com.whatsaround.whatsaround;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.whatsaround.whatsaround.com.whatsaround.whatsaround.dataType.flashCard;


public class QuizActivity extends Activity {
    // Define some global variables that we're gonna work with
    Context context = this;
    String QUIZ_ACTIVITY = "QuizActivity";
    int current = -1;
    int score = 0;
    flashCard[] questions = new flashCard[5];
    TextView textScore;
    ImageView picture;
    Button option1, option2, option3, option4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Since we're currently using placeholders from the drawable resources we have to do the bitmap stuff on it
        // This will pull questions from the JSON object/Database and then set array elements accordingly
        Drawable myDrawable = getResources().getDrawable(R.drawable.chair);
        Bitmap icon = ((BitmapDrawable) myDrawable).getBitmap();

        Drawable myDrawable2 = getResources().getDrawable(R.drawable.table);
        Bitmap icon2 = ((BitmapDrawable) myDrawable2).getBitmap();

        Drawable myDrawable3 = getResources().getDrawable(R.drawable.jupiter);
        Bitmap icon3 = ((BitmapDrawable) myDrawable3).getBitmap();
        // This is an array that will hold all the questions we have
        questions[0] = new flashCard("Chair", icon);
        questions[1] = new flashCard("Table", icon2);
        questions[2] = new flashCard("Jupiter", icon3);

        // Create variables for our views
        picture = (ImageView)findViewById(R.id.picture);
        option1 = (Button)findViewById(R.id.option1);
        option2 = (Button)findViewById(R.id.option2);
        option3 = (Button)findViewById(R.id.option3);
        option4 = (Button)findViewById(R.id.option4);
        textScore = (TextView)findViewById(R.id.score);

        // Give each button an onClickListener which calls checkAnswer
        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(v);
            }
        });
        option2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(v);
            }
        });
        option3.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(v);
            }
        });
        option4.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               checkAnswer(v);
            }
        });

        // Get the next question - since we start at -1 this will immediately get the first element of the array
        getNextQuestion();
    }

    public void checkAnswer(View v) {
        // Calls the current flashCard item's checkCorrect method
        // If it's correct, we add 100 points to the user score and make a toast (to be replaced by positive indicator)
        // Otherwise, we subtract 50 points and make a toast (to be replaced by negative indicator)
        Button b = (Button)v;
        if (questions[current].checkCorrect(b.getText().toString()) == true) {
            score += 100;
            Toast.makeText(QuizActivity.this, "Correct", Toast.LENGTH_LONG).show();
            textScore.setText("Score: " + score);
        } else {
            score -= 50;
            if (score < 0)
                score = 0;
            Toast.makeText(QuizActivity.this, "Incorrect", Toast.LENGTH_LONG).show();
            textScore.setText("Score: " + score);
        }
        // Then we get the next question
        getNextQuestion();
    }

    private void getNextQuestion() {
        /*
            This method will get a random flashCard from the array (providing it is not the current one and has not already been answered)
            It will then set the correct answer as well as incorrect answers
            When the user selects an answer, it will check correctness and also set an answered flag
            Then, we'll do it all again
            When all questions have been completed, we will either start again or do something to be determined
         */
        current++;
        // We'll set the current index back to the beginning if it is larger than the array
        // Currently, we're only working with two items so we want to set this to be lower
        if( current > 2)
            current = 0;
        // This is currently hardcoded for functionality purposes
        // When finished, this method will randomly select which position the correct answer will be in
        // It will also create three wrong choices to fill in the other positions
        // The three wrong answers will be random mis-spellings of the word
        // Hopefully those mis-spellings are plausible
        picture.setImageBitmap(questions[current].getPicture());
        option1.setText(questions[current].getWord());
        if( current == 0 ) {
            option2.setText("Char");
            option3.setText("Chir");
            option4.setText("Chur");
        }
        if ( current == 1 ) {
            option2.setText("Tayble");
            option3.setText("Tabel");
            option4.setText("Teybel");
        }
        if ( current == 2 ) {
            option2.setText("Joopiter");
            option3.setText("Jewpiter");
            option4.setText("Djupiter");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz, menu);
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
}
