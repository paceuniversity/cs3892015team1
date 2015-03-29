package com.whatsaround.whatsaround;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.whatsaround.whatsaround.com.whatsaround.whatsaround.dataType.PictureWord;

import java.io.File;
import java.util.ArrayList;


public class ListPicturesActivity extends Activity {

    private File file;
    private final String ACTIVITY = "ListPicturesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pictures);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        File extDir = getExternalFilesDir(null);
        Log.d(ACTIVITY, "External Directory is " + extDir);
        if(extDir.list().length == 0){
            Log.d(ACTIVITY, "External Directory is empty");
            TextView tv = (TextView)findViewById(R.id.textView_no_pictures);
            tv.setText("You have no pictures yet :(\n\nGo back to start adding pictures");
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
}
