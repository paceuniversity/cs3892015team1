package com.whatsaround.whatsaround;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        holder.tv=(TextView) rowView.findViewById(R.id.word);
        //holder.img=(ImageView) rowView.findViewById(R.id.picture);
        holder.tv.setText(result.get(position));
        Log.d(ACTIVITY, "Successfully set text for " + result.get(position));
        Log.d(ACTIVITY, "Trying to set picture for " + Uri.parse(image.get(position)));
        //holder.img.setImageResource(R.drawable.chair);
        Log.d(ACTIVITY, result.get(position) + " is set!");
        return rowView;
    }

}