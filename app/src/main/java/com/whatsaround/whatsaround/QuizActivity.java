package com.whatsaround.whatsaround;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.whatsaround.whatsaround.com.whatsaround.whatsaround.dataType.flashCard;


public class QuizActivity extends Activity {
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Drawable myDrawable = getResources().getDrawable(R.drawable.chair);
        Bitmap icon = ((BitmapDrawable) myDrawable).getBitmap();
        flashCard chair = new flashCard("Chair", icon);
        // This is an array that will hold all the questions we have
        flashCard[] questions = {chair};

        ImageView picture = (ImageView)findViewById(R.id.picture);
        picture.setImageBitmap(questions[0].getPicture());
        TextView option1 = (TextView)findViewById(R.id.option1), option2 = (TextView)findViewById(R.id.option2),
                option3 = (TextView)findViewById(R.id.option3), option4 = (TextView)findViewById(R.id.option4);
        option1.setText(questions[0].getWord());
        option2.setText(questions[0].getWord());
        option3.setText(questions[0].getWord());
        option4.setText(questions[0].getWord());
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
