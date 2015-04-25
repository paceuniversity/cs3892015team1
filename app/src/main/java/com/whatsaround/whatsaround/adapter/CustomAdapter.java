package com.whatsaround.whatsaround.adapter;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.whatsaround.whatsaround.R;
import com.whatsaround.whatsaround.activity.ListPicturesActivity;
import com.whatsaround.whatsaround.activity.PictureWordActivity;

import java.io.IOException;
import java.util.ArrayList;

// The CustomAdapter class to populate the ListView on the ListPicturesActivity
// It takes it an ArrayList of URIs (image) and an ArrayList of words (result)
// It then returns an instance of the object_list.xml as that list element
// This is done in the getView method

public class CustomAdapter extends BaseAdapter{
    private final String ACTIVITY = "CustomAdapter";
    ArrayList<String> result;
    Context context;
    ArrayList<String> image;
    private static LayoutInflater inflater=null;
    public CustomAdapter(ListPicturesActivity Activity, ArrayList<String> wordList, ArrayList<String> pictureList) {
        // TODO Auto-generated constructor stub
        result = wordList;
        context = Activity;
        image = pictureList;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.object_list, null);
        holder.tv = (TextView) rowView.findViewById(R.id.word);
        holder.img = (ImageView) rowView.findViewById(R.id.picture);
        holder.tv.setText(result.get(position));
        Log.d(ACTIVITY, "Successfully set text for " + result.get(position));
        Log.d(ACTIVITY, "Trying to set picture for " + Uri.parse(image.get(position)));
        String path = getRealPathFromURI(Uri.parse(image.get(position)));
       //Bitmap bitmap = BitmapFactory.decodeFile(path);
        holder.img.setImageBitmap(decodeSampledBitmapFromResource(path, 100, 100));
        int orientation = getExifOrientation(path);
        Bitmap bitmap = decodeSampledBitmapFromResource(path, 100, 100);
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
            holder.img.setImageBitmap(bit);
        }
        else {
            holder.img.setImageBitmap(bitmap);
        }

        //holder.img.setImageResource(R.drawable.chair);
        Log.d(ACTIVITY, result.get(position) + " is set!");
        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(context, PictureWordActivity.class);
                intent.putExtra("word", result.get(position));
                intent.putExtra("uri", image.get(position));
                context.startActivity(intent);
            }
        });
        return rowView;
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(this.context, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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

    private static int getExifOrientation(String filepath) {// YOUR MEDIA PATH AS STRING
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

}