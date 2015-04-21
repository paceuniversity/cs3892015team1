package com.whatsaround.whatsaround.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.whatsaround.whatsaround.R;
import com.whatsaround.whatsaround.model.Question;

import java.io.File;
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


        Question question = null;
        if(!questions.isEmpty()){
            question = questions.get(position);
            Log.e("EEEEEEE", String.valueOf(questions.size()));
            Log.e("EEEEEEE", question.getAnswer());
        }


        if(question==null){
            Log.e("EEEEEEE", String.valueOf(position));
        }else{
            Log.e("EEEEEEE", "question not null");
        }



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


        if (question != null) {

            //Resize image, if it exists

            if(new File(question.getImage()).exists()){

                Bitmap imageInNormalSize = BitmapFactory.decodeFile(question.getImage());
                Bitmap imageResized = Bitmap.createScaledBitmap(imageInNormalSize, 90, 90, false);

                //Set text and image of the new Views
                if (imageResized != null) {
                    viewQuestionHolder.image.setImageBitmap(imageResized);
                }
                viewQuestionHolder.textAnswer.setText(question.getAnswer());

            }else{

                Log.e("EEEEEEE", "Image not found");

                viewQuestionHolder.textAnswer.setText(question.getAnswer());

            }



        }


        return view;
    }


    //It holds the view that was already created for this to be recycled
    private class ViewQuestionHolder {

        public ImageView image;
        public TextView textAnswer;
    }
}
