package com.whatsaround.whatsaround;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.whatsaround.whatsaround.com.whatsaround.whatsaround.dataType.PictureWord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;


public class ListPicturesActivity extends Activity {

    private File file;
    private final String ACTIVITY = "ListPicturesActivity";
    private final String FILE_NAME = "WAData";

    // Declare two array lists to store our words and URIs
    ArrayList<String> wordList = new ArrayList<String>();
    ArrayList<String> pictureList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pictures);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        ListView lv = (ListView)findViewById(R.id.listView);

        File extDir = getExternalFilesDir(null);
        Log.d(ACTIVITY, "External Directory is " + extDir);
        if(extDir.list().length == 0){
            Log.d(ACTIVITY, "External Directory is empty");
            TextView tv = (TextView)findViewById(R.id.textView_no_pictures);
            tv.setText("You have no pictures yet :(\n\nGo back to start adding pictures");
        } else {
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
                    Log.d(ACTIVITY, word);
                    // image.setImageBitmap(BitmapFactory.decodeFile(uri));
                    // We make sure the JSONObject has a set "word" attribute, a set "uri" attribute, and is not something like "android.widget.EditText..."
                    // I had a few instances of the last case and it made things very odd
                    if  (!json.getJSONObject(i).isNull("word") && !json.getJSONObject(i).isNull("uri") &&
                            !json.getJSONObject(i).getString("word").matches("android.widget.EditText@(.*)") ) {
                        // Add both the word and uri to their corresponding ArrayLists
                        Log.d(ACTIVITY, "The word is: " + word);
                        wordList.add(word);
                        Log.d(ACTIVITY, "The uri is: " + uri);
                        pictureList.add(uri);
                    }
                }

                // Set the list view adapter to the custom adapter class
                lv.setAdapter(new CustomAdapter(this, wordList, pictureList));

                //listItems.add(String.valueOf(readFile(file,1)));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //adapter.notifyDataSetChanged();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_pictures, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
}
