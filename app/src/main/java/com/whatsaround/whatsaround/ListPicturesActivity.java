package com.whatsaround.whatsaround;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Picture;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
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

            //Declares ArrayList of PictureWords
            final ArrayList<PictureWord> picWords = new ArrayList<PictureWord>();

            try{
                JSONArray data = readFile(file, 1);

                for (int i = 0; i < data.length(); i++) {
                    String word = data.getJSONObject(i).getString("word");
                    String uri = data.getJSONObject(i).getString("uri");

                    picWords.add(new PictureWord(word, uri));
                }
            }
            catch(Exception e){
                Log.d(ACTIVITY, "Error\n" + e.getMessage());
            }

            ArrayAdapter<PictureWord> adapter = new ArrayAdapter<PictureWord>(
                    this, android.R.layout.simple_list_item_1, picWords);
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(
                            ListPicturesActivity.this, PictureWordActivity.class);
                    intent.putExtra("word", picWords.get(position).word);
                    intent.putExtra("uri", picWords.get(position).picUri.toString());
                    startActivity(intent);
                    //Log.d(ACTIVITY, "OnItemClick: AdapterView, " + parent +" View, " + view + " position, " + position + " id, " + id);
                }
            });
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
