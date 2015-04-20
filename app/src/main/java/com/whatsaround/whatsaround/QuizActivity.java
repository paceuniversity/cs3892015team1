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
    Context context = this;
    String QUIZ_ACTIVITY = "QuizActivity";
    int current = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Drawable myDrawable = getResources().getDrawable(R.drawable.chair);
        Bitmap icon = ((BitmapDrawable) myDrawable).getBitmap();
        flashCard chair = new flashCard("Chair", icon);
        // This is an array that will hold all the questions we have
        final flashCard[] questions = {chair};

        ImageView picture = (ImageView)findViewById(R.id.picture);
        Button option1 = (Button)findViewById(R.id.option1), option2 = (Button)findViewById(R.id.option2),
                option3 = (Button)findViewById(R.id.option3), option4 = (Button)findViewById(R.id.option4);
        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button)v;
                Log.d(QUIZ_ACTIVITY, "We're in the click listener");
                if( questions[current].checkCorrect(b.getText().toString()) == true ) {
                    Toast.makeText(QuizActivity.this, "TRUE", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(QuizActivity.this, "FALSE", Toast.LENGTH_LONG).show();
                }
                Log.d(QUIZ_ACTIVITY, "" + questions[current].checkCorrect(b.getText().toString()));
            }
        });
        option2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                Log.d(QUIZ_ACTIVITY, "We're in the click listener");
                if (questions[current].checkCorrect(b.getText().toString()) == true) {
                    Toast.makeText(QuizActivity.this, "TRUE", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(QuizActivity.this, "FALSE", Toast.LENGTH_LONG).show();
                }
                Log.d(QUIZ_ACTIVITY, "" + questions[current].checkCorrect(b.getText().toString()));
            }
        });
        option3.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                Log.d(QUIZ_ACTIVITY, "We're in the click listener");
                if (questions[current].checkCorrect(b.getText().toString()) == true) {
                    Toast.makeText(QuizActivity.this, "TRUE", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(QuizActivity.this, "FALSE", Toast.LENGTH_LONG).show();
                }
                Log.d(QUIZ_ACTIVITY, "" + questions[current].checkCorrect(b.getText().toString()));
            }
        });
        option4.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                Log.d(QUIZ_ACTIVITY, "We're in the click listener");
                if (questions[current].checkCorrect(b.getText().toString()) == true) {
                    Toast.makeText(QuizActivity.this, "TRUE", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(QuizActivity.this, "FALSE", Toast.LENGTH_LONG).show();
                }
                Log.d(QUIZ_ACTIVITY, "" + questions[current].checkCorrect(b.getText().toString()));
            }
        });


        picture.setImageBitmap(questions[0].getPicture());
        option1.setText(questions[0].getWord());
        option2.setText("Char");
        option3.setText("Chir");
        option4.setText("Chur");
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
