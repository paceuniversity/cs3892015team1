package com.whatsaround.whatsaround;

import android.app.Activity;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.whatsaround.whatsaround.com.whatsaround.whatsaround.dataType.PictureWord;

import java.io.IOException;
import java.util.concurrent.ExecutionException;


public class PictureWordActivity extends Activity {

    private final String ACTIVITY = "PictureWordActivity";
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_word);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String word = intent.getStringExtra("word");
        uri = Uri.parse(intent.getStringExtra("uri"));

        TextView tv = (TextView)findViewById(R.id.label);
        tv.setText(word);

//        String path = getRealPathFromURI(uri);
//
//        LoadPicture task = new LoadPicture();
//        task.execute(uri);
//
//        Log.d(ACTIVITY, "The location of the photo is: " + path);
//        int orientation = getExifOrientation(path);
//        Bitmap bitmap = BitmapFactory.decodeFile(path);
//        int rotate;
//        switch(orientation){
//            case 90: rotate = 90;
//                     break;
//            case 180: rotate = 180;
//                     break;
//            case 270: rotate = 270;
//                     break;
//            default: rotate = 0;
//                     break;
//        }
//        if(rotate != 0){
//            Matrix matrix = new Matrix();
//            matrix.postRotate(90);
//            Bitmap bit = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//            ImageView image = (ImageView)findViewById(R.id.picture);
//            image.setImageBitmap(bit);
//        }
//        else {
//            ImageView image = (ImageView)findViewById(R.id.picture);
//            image.setImageBitmap(bitmap);
//        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_picture_word, menu);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        LoadPicture task = new LoadPicture();
        task.doInBackground(uri);
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

    public static int getExifOrientation(String filepath) {// YOUR MEDIA PATH AS STRING
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

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private class LoadPicture extends AsyncTask<Uri, Void, Bitmap>{
        @Override
        protected Bitmap doInBackground(Uri... uris) {
            if(uris.length > 1){
                Log.d(ACTIVITY, "More than one uri\n " + uris);
            }
            String path = getRealPathFromURI(uris[0]);

            Log.d(ACTIVITY, "The location of the photo is: " + path);
            int orientation = getExifOrientation(path);
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            int rotate;
            switch(orientation){
                case 90: rotate = 90;
                    break;
                case 180: rotate = 180;
                    break;
                case 270: rotate = 270;
                    break;
                default: rotate = 0;
                    break;
            }
            if(rotate != 0){
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap bit = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                bitmap = bit;
            }

            ImageView image = (ImageView)findViewById(R.id.picture);
            image.setImageBitmap(bitmap);

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ImageView image = (ImageView)findViewById(R.id.picture);
            image.setImageBitmap(bitmap);
        }
    }
}
