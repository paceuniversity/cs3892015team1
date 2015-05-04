package com.whatsaround.whatsaround.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.whatsaround.whatsaround.R;
import com.whatsaround.whatsaround.data.QuestionDAO;
import com.whatsaround.whatsaround.dataType.Question;
import java.util.List;
import java.util.Random;


public class QuizActivity extends Activity {
    // Define some global variables that we're gonna work with
    private final String LOGTAG = "QuizActivity";

    int current = -1;
    int score = 0;
    TextView textScore;
    ImageView picture;
    Button option1, option2, option3, option4;

    // Declare two array lists to store our words and URIs
    List<Question> questionsListed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);


        questionsListed = QuestionDAO.getInstance(this).list();



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
        if (questionsListed.get(current).checkCorrect(b.getText().toString()) == true) {
            score += 100;
            textScore.setText("Score: " + score);
            // Plays a sound
            // This will have to be the 'correct' indication sound
            MediaPlayer mp = MediaPlayer.create(QuizActivity.this, R.raw.right_answer);
            mp.start();
            Toast toast = new Toast(this);
            toast.setGravity(Gravity.CENTER, 0, 0);
            ImageView result = new ImageView(this);
            result.setImageResource(R.drawable.checkmark);
            toast.setView(result);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        } else {
            score -= 50;

            if (score < 0)
                score = 0;
            textScore.setText("Score: " + score);

            MediaPlayer mp = MediaPlayer.create(QuizActivity.this, R.raw.wrong_answer);
            mp.start();

            Toast toast = new Toast(this);
            toast.setGravity(Gravity.CENTER, 0, 0);
            ImageView result = new ImageView(this);
            result.setImageResource(R.drawable.wrong);
            toast.setView(result);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
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
        if( current >= questionsListed.size())
            current = 0;
        picture.setImageBitmap(getRotatedBitmap(questionsListed.get(current).getImage()));
        // We're gonna create an array of positions to delete and initialize it with all -1s
        int[] deletions = {-1, -1, -1};
        // For each element in the deletions array, we want to generate a letter to delete
        for (int i = 0; i < deletions.length; i++) {
            // We'll generate a number once
            // If the position equals the position in either of the earlier elements, we'll generate a new number
            // We'll keep doing this until we get a new number
            do {
                deletions[i] = Math.abs( new Random().nextInt() ) % questionsListed.get(current).getAnswer().length();
            } while ( ( deletions[i] == deletions[0] && i > 0) || ( deletions[i] == deletions[1] && i > 1) );
        }
        //picture.setImageBitmap(decodeSampledBitmapFromResource(questionsListed.get(current).getImage(), 100, 100) );
        // Sets up an array containing the correct answer and three incorrect answers
        String[] options = {questionsListed.get(current).getAnswer(), removeLetter(questionsListed.get(current).getAnswer(), deletions[0]),
                removeLetter(questionsListed.get(current).getAnswer(), deletions[1]), removeLetter(questionsListed.get(current).getAnswer(), deletions[2])};
        // Each array element will be swapped with a random array element
        for(int i = 0; i < options.length; i++) {
            String temp = options[i];
            // We'll generate a random number and make sure it's positive (nextInt can return negatives)
            // We'll then mod it by the length of the array (4)
            // Then, we'll swap the element being worked on with the randomly generated element
            int swapped = Math.abs( new Random().nextInt() ) % options.length;
            options[i] = options[swapped];
            options[swapped] = temp;

        }
        // Each option is set to the corresponding array element
        option1.setText(options[0]);
        option2.setText(options[1]);
        option3.setText(options[2]);
        option4.setText(options[3]);
    }

    // Removes a random string from the first letter that's not the first letter
    // This may be improvable in the future to simulate really good child spelling errors
    private String removeLetter(String word, int letterPos) {
        // If the letter to be removed is in position 0, we'll append an e onto the end of the word
        // This keeps us from removing the first letter but for words that only have three letters, there won't be duplicate choices
        StringBuilder sb = new StringBuilder(word);
        if (letterPos == 0)
            sb.append('e');
        else
            sb.deleteCharAt( letterPos );
        return sb.toString();
    }

    // CREATE A CLASS FOR THE ROTATE/RESIZE METHODS SINCE LIKE THREE CLASSES DEFINE THEM


    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private static Bitmap decodeSampledBitmapFromResource(String uri, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(uri, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(uri, options);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private Bitmap getRotatedBitmap(String filePath){
        Bitmap bitmap = decodeSampledBitmapFromResource(filePath, 350, 350);

        ExifInterface exif = null;
        try{
            exif = new ExifInterface(filePath);
        }
        catch(Exception e){
            Toast.makeText(QuizActivity.this, "The image is not a jpeg", Toast.LENGTH_LONG);
        }

        if(exif != null){
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if(orientation != -1){
                Matrix matrix = new Matrix();
                switch(orientation) {
                    case (ExifInterface.ORIENTATION_ROTATE_90):
                        Log.d(LOGTAG, "Rotate 90");
                        matrix.postRotate(90);
                        return Bitmap.createBitmap(
                                bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    case (ExifInterface.ORIENTATION_ROTATE_180):
                        Log.d(LOGTAG, "Rotate 180");
                        matrix.postRotate(180);
                        return Bitmap.createBitmap(
                                bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    case (ExifInterface.ORIENTATION_ROTATE_270):
                        Log.d(LOGTAG, "Rotate 270");
                        matrix.postRotate(270);
                        return Bitmap.createBitmap(
                                bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    default:
                        Log.d(LOGTAG, "No rotation");
                        return bitmap;
                }
            }
            else{
                Toast.makeText(
                        QuizActivity.this, "The picture was never loaded", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(
                    QuizActivity.this, "The picture was never loaded", Toast.LENGTH_SHORT).show();
        }
        return bitmap;
    }
}
