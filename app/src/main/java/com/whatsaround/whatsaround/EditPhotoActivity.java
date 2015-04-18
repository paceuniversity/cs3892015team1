package com.whatsaround.whatsaround;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class EditPhotoActivity extends Activity {

    private final String ACTIVITY = "EditPhotoActivity";
    private final String FILE_NAME = "WAData";
    private Uri selectedImage;
    private final int REQUEST_IMAGE_LOAD = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photo);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        Log.d(ACTIVITY, "Selecting photo");
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_LOAD);

        Button saveButton = (Button) findViewById(R.id.save_button);

        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                EditText text = (EditText) findViewById(R.id.word_association);
                String word = text.getText().toString();
                File dir = getExternalFilesDir(null);
                File file = new File(dir, FILE_NAME);

                try {
                    addPictureToFile(word, selectedImage, file);
                } catch (IOException e) {

                    Log.d(ACTIVITY, "There is no existing data, creating new file");

                    try {
                        createFile(word, selectedImage, file);
                    } catch (Exception e2) {
                        Log.d(ACTIVITY, "Cannot create file");
                        Toast.makeText(EditPhotoActivity.this, "Cannot create file", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Log.d(ACTIVITY, "Not a JSON object");
                }

                try {
                    JSONArray data2 = readFile(file, 1);

                    StringBuffer dataBuffer = new StringBuffer();
                    for (int i = 0; i < data2.length(); i++) {
                        String uried = data2.getJSONObject(i).getString("uri");
                        String worded = data2.getJSONObject(i).getString("word");
                        dataBuffer.append(worded + "    " + uried + "\n");
                    }

                    TextView tv = (TextView) findViewById(R.id.fileText);
                    tv.setText(dataBuffer.toString());
                } catch (Exception e) {
                    Log.d(ACTIVITY, "Error testing\n" + e.getMessage());
                }

                Toast.makeText(EditPhotoActivity.this, "Picture Saved", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_LOAD && resultCode == RESULT_OK) {
            selectedImage = data.getData();
            String text = data.getDataString();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePath, null, null, null);
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(filePath[0]);
            String picPath = cursor.getString(index);
            Log.d(ACTIVITY, "The location of the photo is: " + picPath);
            cursor.close();
            ImageView image = (ImageView) findViewById(R.id.picture);
            image.setImageBitmap(BitmapFactory.decodeFile(picPath));
        }
    }

    public void addPictureToFile(String word, Uri uri, File file) throws IOException, JSONException {
        Log.d(ACTIVITY, "Called add picture");

        JSONArray data = readFile(file);
        data = addPicture(data, word, uri);

        String text = data.toString();

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(text.getBytes());
        fos.close();
    }

    public void createFile(String word, Uri uri, File file) throws JSONException, IOException {
        if (!checkExternalStorage()) {
            return;
        }

        JSONObject picture = new JSONObject();

        picture.put("word", word);
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

    public JSONArray addPicture(JSONArray data, String word, Uri uri) throws JSONException {
        if (!checkExternalStorage()) {
            return data;
        }

        try {
            JSONObject picture = new JSONObject();
            picture.put("word", word);
            picture.put("uri", uri);
            data.put(picture);
        } catch (JSONException e) {
            Log.d(ACTIVITY, "Not a JSON object");
        }

        return data;
    }

    public boolean checkExternalStorage() {
        String state = Environment.getExternalStorageState();

        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else if (state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            Toast.makeText(this, "External Storage is read only", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "External Storage is not accessible", Toast.LENGTH_LONG).show();
        }
        return false;
    }
}

