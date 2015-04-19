package com.whatsaround.whatsaround.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.whatsaround.whatsaround.data.QuestionDAO;
import com.whatsaround.whatsaround.model.Question;
import com.whatsaround.whatsaround.R;


public class EditQuestionActivity extends ActionBarActivity {

    public static final String LOG_KEY = EditQuestionActivity.class.getName();

    private static final int RESULT_GALLERY_IMAGE = 2;

    private Question question;

    private boolean isEditionMode;

    private QuestionDAO questionDAO;

    private EditText answerView;

    private ImageView imageView;

    private String newImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_question);

        questionDAO = QuestionDAO.getInstance(this);


        //Get Image and answer from the item clicked on the list
        String id = getIntent().getStringExtra(SettingsActivity.QUESTION_KEY);

        if(id != null){
            question = questionDAO.getQuestonById(Integer.parseInt(id));
        }


        isEditionMode = question != null;

        answerView = (EditText) findViewById(R.id.txt_edit_answer);
        imageView = (ImageView) findViewById(R.id.img_edit_image);


        //If "question" is not null, the an item on the list was clicked and redirected to here.
        //Therefore, take Views on this screen and assign the given Image and Answer (given in the Intent) as their texts
        if (isEditionMode) {


            if ((answerView != null) && (imageView != null)) {
                answerView.setText(question.getAnswer());
                imageView.setImageBitmap(BitmapFactory.decodeFile(question.getImage()));
            } else {
                Log.i(LOG_KEY, "answerView is null.");
            }

        }


        //Otherwise, show the default Layout


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Makes the menu appear
        getMenuInflater().inflate(R.menu.menu_edit_question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.mnu_save_question) {


            if (isEditionMode) {

                editQuestion();


            } else {

                saveQuestion();

            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void goToGallery(View view) {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, RESULT_GALLERY_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //If the Activity that returned the Intent was the gallery Activity,
        // take the picture passed and set to ImageView
        if (requestCode == RESULT_GALLERY_IMAGE && resultCode == RESULT_OK && data != null) {

            Uri selectedImage = data.getData();

            String[] filePath = {
                    MediaStore.Images.Media.DATA
            };

            Cursor cursor = getContentResolver().query(selectedImage, filePath, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePath[0]);

            newImagePath = cursor.getString(columnIndex);


            cursor.close();

            Toast.makeText(getApplicationContext(), newImagePath, Toast.LENGTH_LONG).show();

            imageView.setImageBitmap(BitmapFactory.decodeFile(newImagePath));

        }
    }


    //---------------- Private Utility Methods -----------

    private void editQuestion() {

        Toast.makeText(getApplicationContext(), "Answer: " + answerView.getText().toString(), Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(), "Image: " + newImagePath, Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), "ID: " + question.getId(), Toast.LENGTH_LONG).show();

        question.setImage(question.getImage());

        if (newImagePath != null) {
            question.setImage(newImagePath);

        }


        question.setAnswer(answerView.getText().toString().trim());


        int updatedRows = questionDAO.update(question);

        if (updatedRows != 0) {
            Toast.makeText(this, "Question  updated.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Couldn't update question. Please, try again.", Toast.LENGTH_LONG).show();
        }
    }

    private void saveQuestion() {

        question = new Question();

        Toast.makeText(getApplicationContext(), "image: " + newImagePath, Toast.LENGTH_LONG).show();

        question.setImage(newImagePath);
        question.setAnswer(answerView.getText().toString().trim());

        int rowsAffected = questionDAO.save(question);

        if (rowsAffected != 0) {
            Toast.makeText(this, "Question saved.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Couldn't save question. Please, try again.", Toast.LENGTH_LONG).show();
        }
    }
}
