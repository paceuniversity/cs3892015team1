package com.whatsaround.whatsaround.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.whatsaround.whatsaround.R;
import com.whatsaround.whatsaround.data.QuestionDAO;
import com.whatsaround.whatsaround.dataType.Question;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class EditQuestionActivity extends ActionBarActivity {

    public static final String LOG_KEY = EditQuestionActivity.class.getName();

    private static final int GALLERY_REQUEST_CODE = 2;
    private static final int CAMERA_REQUEST_CODE = 1;

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


        //If this activity was called in Edition Mode, then an item on the list was clicked and redirected to here.
        //Therefore, take Views on this screen and assign the Image and Answer (given in the Intent) as their values.
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

    public void goToCamera(View view) {


        //If External storage is available, create a new file and pass it to the camera Intent, which will use this file
        //to save on it the picture taken.
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {

            //If the folder "WhatsAround" doesn't already exist on the public Picture folder, create it.
            File imagesFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "/WhatsAround");

            if (!imagesFolder.exists()) {


                boolean successOnCreatingFolder = imagesFolder.mkdirs();

                if (!successOnCreatingFolder) {
                    Toast.makeText(getApplicationContext(), "Error when creating the image file.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }


            //To create different file names, take the current time and add it to the name of the file
            String currentTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "WhatsAround_" + currentTime + "_";


            //Create a new file on the "WhatsAround" folder and tell our application
            //that this is the path for the image added in a new question.
            File imageFile = new File(imagesFolder, imageFileName + ".jpg");
            newImagePath = imageFile.getPath();


            //Create a Intent to call the camera.
            //Give the file created to a camera Intent to take a picture and save it on this file.
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri photoPathUri = Uri.fromFile(imageFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoPathUri);


            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);


        } else {

            Toast.makeText(getApplicationContext(), "Unable to proceed. SD Card not available.", Toast.LENGTH_SHORT).show();

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //If the Activity that returned the Intent was the gallery Activity,
        // take the picture passed and set to ImageView
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {


            Uri selectedImage = data.getData();

            String[] galleryFolderPath = {
                    MediaStore.Images.Media.DATA
            };


            //Make a query (through a cursor) to the gallery directory asking for the selectedImage
            Cursor cursor = getContentResolver().query(selectedImage, galleryFolderPath, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(galleryFolderPath[0]);


            //Set the newImagePath of the question as the path of the image retrieved from the gallery
            newImagePath = cursor.getString(columnIndex);


            cursor.close();


            //Set image taken from the gallery and put on imageView
            imageView.setImageBitmap(BitmapFactory.decodeFile(newImagePath));


        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {


            //Tell Android to update the gallery with the picture that the camera has taken.
            //If we don't do that and we open the gallery without turning off the phone,
            //the picture be shown there, besides being stored on a public directory.
            MediaScannerConnection.scanFile(getApplicationContext(), new String[]{newImagePath},
                    null, new MediaScannerConnection.OnScanCompletedListener() {

                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            //Don't need to do nothing after notifying android about changes.

                        }
                    });


            //Put image received from the camera in the imageView
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
            answerView.setError("Answer no defined");
            //Toast.makeText(getApplicationContext(), "Couldn't update question. Answer not defined.", Toast.LENGTH_SHORT).show();
            return;
        }


        //Save on the database and receive the number of rows affected. If it's larger than zero,
        //the question was saved. Then, give a success message and come back to the previous Activity.
        //Otherwise, give an error message.
        int updatedRows = questionDAO.update(question);

        if (updatedRows != 0) {
            Toast.makeText(this, "Question updated.", Toast.LENGTH_SHORT).show();

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
            answerView.setError("Answer no defined");
            //Toast.makeText(getApplicationContext(), "Couldn't save question. Answer not defined.", Toast.LENGTH_SHORT).show();
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
