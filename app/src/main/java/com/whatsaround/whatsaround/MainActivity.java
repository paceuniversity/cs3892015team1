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
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_IMAGE_LOAD);
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
            }
        }

        else if(requestCode == REQUEST_IMAGE_LOAD){

            Log.d(MAIN_ACTIVITY, "REQUESTED IMAGE LOAD REACHED");

            if(resultCode == RESULT_OK && receivedIntent != null){
                Uri selectedImage = receivedIntent.getData();
                String text = receivedIntent.getDataString();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePath, null, null, null);
                cursor.moveToFirst();
                int index = cursor.getColumnIndex(filePath[0]);
                String picPath = cursor.getString(index);
                cursor.close();
                ImageView image = (ImageView)findViewById(R.id.testPicture);
                image.setImageBitmap(BitmapFactory.decodeFile(picPath));
                File dir = getExternalFilesDir(null);
                File file = new File(dir, FILE_NAME);
                try {
                    addPictureToFile(selectedImage, file);
                }
                catch(IOException e){
                    Log.d(MAIN_ACTIVITY, "There is no existing data, creating new file");
                    try{
                        createFile(selectedImage, file);
                    }
                    catch (Exception e2){
                        Log.d(MAIN_ACTIVITY, "Cannot create file");
                        Toast.makeText(this, "Cannot create file", Toast.LENGTH_LONG).show();
                    }
                }
                catch(JSONException e){
                    Log.d(MAIN_ACTIVITY, "Not a JSON object");
                }

                try{
                    JSONArray data = readFile(file, 1);

                    StringBuffer dataBuffer = new StringBuffer();
                    for (int i = 0; i < data.length(); i++) {
                        String tour = data.getJSONObject(i).getString("uri");
                        dataBuffer.append(tour + "\n");
                    }

                    TextView tv = (TextView)findViewById(R.id.fileText);
                    tv.setText(dataBuffer.toString());
                }
                catch(Exception e){
                    Log.d(MAIN_ACTIVITY, "Error testing\n" + e.getMessage());
                }
            }
        }
    }

    public void addPictureToFile(Uri uri, File file) throws IOException, JSONException {
        Log.d(MAIN_ACTIVITY, "Called add picture");

        JSONArray data = readFile(file);
        data = addPicture(data, uri);

        String text = data.toString();

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(text.getBytes());
        fos.close();
    }

    public void createFile(Uri uri, File file) throws JSONException, IOException {
        if (!checkExternalStorage()){
            return;
        }

        JSONObject picture = new JSONObject();

        picture.put("uri", uri);

        JSONArray data = new JSONArray().put(picture);

        String text = data.toString();

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(text.getBytes());
        fos.close();
    }


    //OverRide is for testing purposes
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

    public JSONArray readFile(File file) throws IOException, JSONException {
       FileInputStream fis = new FileInputStream(file);
       BufferedInputStream bis = new BufferedInputStream(fis);
       StringBuffer b = new StringBuffer();
       while (bis.available() != 0) {
           char c = (char) bis.read();
           b.append(c);
       }
       bis.close();
       fis.close();

       return new JSONArray(b.toString());
    }

    public JSONArray addPicture(JSONArray data, Uri uri) throws JSONException {
        if (!checkExternalStorage()){
            return data;
        }

        try {
            JSONObject picture = new JSONObject();
            picture.put("uri", uri);
            data.put(picture);
        }
        catch(JSONException e){
            Log.d(MAIN_ACTIVITY, "Not a JSON object");
        }

       return data;
    }

    public boolean checkExternalStorage(){
        String state = Environment.getExternalStorageState();

        if(state.equals(Environment.MEDIA_MOUNTED)){
            return true;
        }
        else if(state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)){
            Toast.makeText(this, "External Storage is read only", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, "External Storage is not accessible", Toast.LENGTH_LONG).show();
        }
        return false;
    }
}
