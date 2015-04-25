package com.whatsaround.whatsaround.activity;

import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.whatsaround.whatsaround.R;
import com.whatsaround.whatsaround.dataType.flashCard;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class QuizActivity extends Activity {
    // Define some global variables that we're gonna work with
    Context context = this;
    private final String QUIZ_ACTIVITY = "QuizActivity";
    private final String FILE_NAME = "WAData";

    int current = -1;
    int score = 0;
    flashCard[] questions = new flashCard[5];
    TextView textScore;
    ImageView picture;
    Button option1, option2, option3, option4;

    // Declare two array lists to store our words and URIs
    ArrayList<String> wordList = new ArrayList<String>();
    ArrayList<String> pictureList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Copy pasted from the CustomAdapter
        // This will read the JSON input and add the word/uri to the appropriate ArrayList
        File dir = getExternalFilesDir(null);
        File file = new File(dir, FILE_NAME);

        try {
            JSONArray json = readFile(file,1);

            for (int i = 0; i < json.length(); i++)
            {
                // Declare and initialize so the compiler doesn't complain
                String word = "", uri = "";
                // We'll check to see if the current JSONObject has the "word" attribute
                // If it doesn't, we'll ignore any "uri" or "word" attribute
                if( json.getJSONObject(i).has("word") ) {
                    uri = json.getJSONObject(i).getString("uri");
                }
                if( json.getJSONObject(i).has("word") ) {
                    word = json.getJSONObject(i).getString("word");
                }
                Log.d(QUIZ_ACTIVITY, word);
                // image.setImageBitmap(BitmapFactory.decodeFile(uri));
                // We make sure the JSONObject has a set "word" attribute, a set "uri" attribute, and is not something like "android.widget.EditText..."
                // I had a few instances of the last case and it made things very odd
                if  (!json.getJSONObject(i).isNull("word") && !json.getJSONObject(i).isNull("uri") &&
                        !json.getJSONObject(i).getString("word").matches("android.widget.EditText@(.*)") ) {
                    // Add both the word and uri to their corresponding ArrayLists
                    Log.d(QUIZ_ACTIVITY, "The word is: " + word);
                    wordList.add(word);
                    Log.d(QUIZ_ACTIVITY, "The uri is: " + uri);
                    pictureList.add(uri);
                }
            }

            //listItems.add(String.valueOf(readFile(file,1)));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // This will set the picture and word for every item in the ArrayList
        for (int i = 0; i < pictureList.size(); i++) {
            String path = getRealPathFromURI(Uri.parse(pictureList.get(i)));
            //Bitmap bitmap = BitmapFactory.decodeFile(path);
            int orientation = getExifOrientation(path);
            Bitmap bitmap = decodeSampledBitmapFromResource(path, 100, 100);
            questions[i] = new flashCard(wordList.get(i), bitmap);
        }

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
        if( current >= pictureList.size())
            current = 0;
        picture.setImageBitmap(questions[current].getPicture());
        // Sets up an array containing the correct answer and three incorrect answers
        String[] options = {questions[current].getWord(), removeLetter(questions[current].getWord()),
                removeLetter(questions[current].getWord()), removeLetter(questions[current].getWord())};
        // Each array element will be swapped with a random array element
        for(int i = 0; i < options.length; i++) {
            String temp = options[i];
            int swapped = new Random().nextInt(options.length);
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
    private String removeLetter(String word) {
        StringBuilder sb = new StringBuilder(word);
        sb.deleteCharAt( new Random().nextInt(word.length()-1) + 1 );
        return sb.toString();
    }

    public JSONArray readFile(File file, int test) throws IOException, JSONException {
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        StringBuffer b = new StringBuffer();
        while (bis.available() != 0) {
            char c = (char) bis.read();
            b.append(c);
        }
        bis.close();
        fis.close();

        JSONArray tester = new JSONArray(b.toString());
        return tester;
    }

    // CREATE A CLASS FOR THE ROTATE/RESIZE METHODS SINCE LIKE THREE CLASSES DEFINE THEM

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(this.context, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private int getExifOrientation(String filepath) {// YOUR MEDIA PATH AS STRING
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }

            }
        }
        return degree;
    }

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
