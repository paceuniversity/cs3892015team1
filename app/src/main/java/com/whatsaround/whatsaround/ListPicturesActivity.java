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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;


public class ListPicturesActivity extends Activity {

    private File file;
    private final String ACTIVITY = "ListPicturesActivity";
    private final String FILE_NAME = "WAData";

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> listItems=new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pictures);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        ListView lv = (ListView)findViewById(R.id.listView);

        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listItems);
        lv.setAdapter(adapter);

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
                listItems.add(String.valueOf(readFile(file,1)));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapter.notifyDataSetChanged();
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
