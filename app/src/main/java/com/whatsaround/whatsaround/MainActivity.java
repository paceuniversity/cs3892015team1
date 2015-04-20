package com.whatsaround.whatsaround;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
//import android.widget.Toast;

public class MainActivity extends Activity {

    private static final int REQUEST_IMAGE_CAPTURE  = 1;
    private static final int REQUEST_IMAGE_LOAD = 2;
    private final String MAIN_ACTIVITY = "MainActivity";
    private final String FILE_NAME = "WAData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addPhotoButton = (Button)findViewById(R.id.button_add_photo);
        addPhotoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d(MAIN_ACTIVITY, "onclick for addPhotoButton");
                Intent intent = new Intent(MainActivity.this, EditPhotoActivity.class);
                startActivity(intent);
            }
        });

        Button seeFilesButton = (Button)findViewById(R.id.button_files);
        seeFilesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(MAIN_ACTIVITY, "onclick for seeFilesButton");
                Intent intent = new Intent(MainActivity.this, ListPicturesActivity.class);
                startActivity(intent);
            }
        });

        Button takeQuizButton = (Button)findViewById(R.id.button_take_quiz);
        takeQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(MAIN_ACTIVITY, "onclick for seeFilesButton");
                Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                startActivity(intent);
            }
        });

//        createDirectory();
//        try {
//            createFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            readFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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


    //Call the camera app when the "+" button is clicked
    public void goToCamera(View view) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);

    }

//    protected void createDirectory() {
//        // If we want to store data on the user's external store, we would need something like this
//        // There is also a permission which needs to be set in the AndroidManifest.xml
//        File myDirectory = new File(Environment.getExternalStorageDirectory(), "WhatsAround");
//
//        if(!myDirectory.exists()) {
//            myDirectory.mkdirs();
//        }
//    }
//
//    protected void createFile() throws IOException {
//        // A simple method for creating a file in the device's internal storage and writing 'Hello world!' to it
//        // Here is where to find saving a file: http://developer.android.com/training/basics/data-storage/files.html
//        // We should modify this so that it would save objects (maybe using JSON or another object notation)
//        // The notation should include a reference to the picture and the corresponding word
//        String filename = "myfile.txt";
//        String string = "Hello world!";
//        FileOutputStream outputStream;
//
//        try {
//            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
//            outputStream.write(string.getBytes());
//            outputStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    protected void readFile() throws IOException {
//        // Do some cool file magic and read the file
//        // Then change the placeholder TextView's text on the main activity
//        // The text goes from the default 'New Text' to 'Hello world!'
//        // Here is where I found reading text: http://stackoverflow.com/questions/14768191/how-do-i-read-the-file-content-from-the-internal-storage-android-app
//        Context context = this;
//        FileInputStream fis = context.openFileInput("myfile.txt");
//        InputStreamReader isr = new InputStreamReader(fis);
//        BufferedReader bufferedReader = new BufferedReader(isr);
//        StringBuilder sb = new StringBuilder();
//        String line;
//        TextView text = (TextView)findViewById(R.id.fileText);
//        while ((line = bufferedReader.readLine()) != null) {
//            text.setText(line);
//        }
//    }

    //When the Context of the application come back from the camera app to this activity,
    //it shows a little message: "Picture Taken"
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent receivedIntent){
        super.onActivityResult(requestCode, resultCode, receivedIntent);

        Log.d(MAIN_ACTIVITY, "onActivityResult reached");

        if (requestCode == REQUEST_IMAGE_CAPTURE){
            if(resultCode == RESULT_OK && receivedIntent != null){
                String message = getString(R.string.toast_picture_taken);
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                Uri uri = receivedIntent.getData();
                Intent intent = new Intent(this, EditPhotoActivity.class);
                intent.putExtra("pictureUri", uri.toString());
                startActivity(intent);
            }
        }
    }
}
