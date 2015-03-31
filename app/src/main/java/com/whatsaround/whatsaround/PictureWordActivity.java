package com.whatsaround.whatsaround;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


public class PictureWordActivity extends Activity {

    private final String ACTIVITY = "PictureWordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_word);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String word = intent.getStringExtra("word");
        Uri uri = Uri.parse(intent.getStringExtra("uri"));

        String[] filePath = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, filePath, null, null, null);
        cursor.moveToFirst();
        int index = cursor.getColumnIndex(filePath[0]);
        String picPath = cursor.getString(index);
        Log.d(ACTIVITY, "The location of the photo is: " + picPath);
        cursor.close();
        Bitmap bitmap = BitmapFactory.decodeFile(picPath);
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap bit = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);


        TextView tv = (TextView)findViewById(R.id.label);
        tv.setText(word);

        ImageView image = (ImageView)findViewById(R.id.picture);
        image.setImageBitmap(bit);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_picture_word, menu);
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
