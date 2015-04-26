package com.whatsaround.whatsaround.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.whatsaround.whatsaround.R;
import com.whatsaround.whatsaround.model.Question;

import java.util.List;


//Android will call its methods when creating the list in the Activity that reference this adapter
public class QuestionsAdapter extends BaseAdapter {


    private List<Question> questions;
    private LayoutInflater inflater;


    public QuestionsAdapter(Context context, List<Question> questions) {

        this.questions = questions;
        this.inflater = LayoutInflater.from(context);

    }


    @Override
    public int getCount() {
        return questions.size();
    }


    @Override
    public Object getItem(int position) {
        return questions.get(position);
    }


    @Override
    public long getItemId(int position) {

        return questions.get(position).getId();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        //Holds ID of Views for them to be recycled. More details bellow, in the if statement.
        ViewQuestionHolder viewQuestionHolder;


        Question question = questions.get(position);


        //If Android did not pass a View (through convertView) that can be recycled, create (inflate) a new one
        // and associate a Holder as a tag to this view, in order to it can be reused later
        if (view == null) {

            view = inflater.inflate(R.layout.activity_settings_list, parent, false);


            //Create a Holder to hold the Views that are being created in order to allow them to be used later.
            viewQuestionHolder = new ViewQuestionHolder();
            viewQuestionHolder.image = (ImageView) view.findViewById(R.id.image);
            viewQuestionHolder.textAnswer = (TextView) view.findViewById(R.id.txt_answer);


            //Associate the View received to the holder that has the IDs
            view.setTag(viewQuestionHolder);


        } else {

            //In the case the View passed can be recycled (!= null), take its tag, which is a Holder
            //This avoid calling the methods "inflate" and "findViewById", which are considered heavy
            viewQuestionHolder = (ViewQuestionHolder) view.getTag();
        }


        //Resize image
        //Bitmap imageInNormalSize = BitmapFactory.decodeFile(question.getImage());
        Bitmap imageResized = decodeSampledBitmapFromResource(question.getImage(), 100, 100);


        //Set text and image of the new Views
        if (imageResized != null) {
            viewQuestionHolder.image.setImageBitmap(imageResized);
        }
        viewQuestionHolder.textAnswer.setText(question.getAnswer());


        return view;
    }


    //It holds the view that was already created for this to be recycled
    private class ViewQuestionHolder {

        public ImageView image;
        public TextView textAnswer;
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
}
