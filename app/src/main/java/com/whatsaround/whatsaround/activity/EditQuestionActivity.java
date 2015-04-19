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

    private static final int GALLERY_REQUEST_CODE = 2;

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


        //Try to get the "id" from the intent. If it is empty, it's not Edition Mode.
        //In Edition Mode, the "id" is not empty, which means this Activity was called
        //through clicking in one of the list items.
        String id = getIntent().getStringExtra(SettingsActivity.QUESTION_KEY);
        if (id != null) {
            question = questionDAO.getQuestonById(Integer.parseInt(id));
        }

        isEditionMode = question != null;


        //Take the references of the image and answer view from the xml layout
        answerView = (EditText) findViewById(R.id.txt_edit_answer);
        imageView = (ImageView) findViewById(R.id.img_edit_image);


        //If this activity was called in Edition Mode, hen an item on the list was clicked and redirected to here.
        //Therefore, take Views on this screen and assign the given Image and Answer (given in the Intent) as their values.
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

        int id = item.getItemId();


        //If the "save question" menu is clicked, verify: If the Activity is in Edition Mode, then edit question on the database.
        //Otherwise, create another one in the database with the values given.
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

        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //If the Activity that returned the Intent was the gallery Activity,
        // take the picture passed and set to ImageView
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            Uri selectedImage = data.getData();

            String[] filePath = {
                    MediaStore.Images.Media.DATA
            };

            Cursor cursor = getContentResolver().query(selectedImage, filePath, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePath[0]);

            newImagePath = cursor.getString(columnIndex);


            cursor.close();

            //Toast.makeText(getApplicationContext(), newImagePath, Toast.LENGTH_LONG).show();

            imageView.setImageBitmap(BitmapFactory.decodeFile(newImagePath));

        }
    }


    //---------------- Private Utility Methods -----------

    private void editQuestion() {

        //If a new image was selected, change the image path in the object that is going to be updated in the database.
        //Otherwise, let it the way it is.
        if (newImagePath != null) {
            question.setImage(newImagePath);
        }


        //If no text was entered, don't continue saving and give a warning.
        String answerToBeSaved = answerView.getText().toString().trim();

        if (!answerToBeSaved.matches("")) {
            question.setAnswer(answerView.getText().toString().trim());
        } else {
            Toast.makeText(getApplicationContext(), "Couldn't update question. Answer not defined.", Toast.LENGTH_SHORT).show();
            return;
        }


        //Save on the database and receive the number of rows affected. If it's larger than zero,
        //the question was saved. Then, give a success message and come back to the previous Activity.
        //Otherwise, give an error message.
        int updatedRows = questionDAO.update(question);

        if (updatedRows != 0) {
            Toast.makeText(this, "Question  updated.", Toast.LENGTH_SHORT).show();

            Intent parentActivityIntent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(parentActivityIntent);

        } else {
            Toast.makeText(this, "Couldn't update question. Please, try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveQuestion() {

        //Create a new question that is going to store all the new information.
        question = new Question();


        //If no image was chosen, don't continue saving and give a warning.
        if (newImagePath != null) {
            question.setImage(newImagePath);
        } else {
            Toast.makeText(getApplicationContext(), "Couldn't save question. Image not selected.", Toast.LENGTH_SHORT).show();
            return;
        }


        //If no text was entered, don't continue saving and give a warning.
        String answerToBeSaved = answerView.getText().toString().trim();

        if (!answerToBeSaved.matches("")) {
            question.setAnswer(answerView.getText().toString().trim());
        } else {
            Toast.makeText(getApplicationContext(), "Couldn't save question. Answer not defined.", Toast.LENGTH_SHORT).show();
            return;
        }


        //Save on the database and receive the number of rows affected. If it's larger than zero,
        //the question was saved. Then, give a success message and come back to the previous Activity.
        //Otherwise, give an error message.
        int rowsAffected = questionDAO.save(question);

        if (rowsAffected != 0) {
            Toast.makeText(this, "Question saved.", Toast.LENGTH_SHORT).show();

            Intent parentActivityIntent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(parentActivityIntent);

        } else {
            Toast.makeText(this, "Couldn't save question. Please, try again.", Toast.LENGTH_SHORT).show();
        }


    }
}
