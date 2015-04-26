package com.whatsaround.whatsaround.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.util.Log;

import com.whatsaround.whatsaround.model.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionDAO {

    public String ERROR_MESSAGE = "ERROR ON DAO";

    private SQLiteDatabase database;

    private static QuestionDAO questionDAOInstance;

    private String[] columns = {
            QuestionsContract.Columns.ID,
            QuestionsContract.Columns.ANSWER,
            QuestionsContract.Columns.IMAGE_PATH
    };

    /*
    * The Singleton Design Pattern is applied in this piece of code because
    * we just need one instance of QuestionDAO during the application.
    * */
    //-------------------------------------
    private QuestionDAO(Context context) {
        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(context);
        database = dataBaseHelper.getWritableDatabase();
    }

    public static QuestionDAO getInstance(Context context) {

        if (questionDAOInstance == null) {
            questionDAOInstance = new QuestionDAO(context.getApplicationContext());
        }

        return questionDAOInstance;
    }
    //---------------------------------------


    public int save(Question question) {

        /*
        * It takes the values of the Question object passed and insert on the database
        * Then, the id of this Question on the database is returned and associated to the Question object passed
        * */
        ContentValues contentValues = new ContentValues();
        contentValues.put(QuestionsContract.Columns.ANSWER, question.getAnswer());
        contentValues.put(QuestionsContract.Columns.IMAGE_PATH, question.getImage());

        long id = database.insert(QuestionsContract.TABLE_NAME, null, contentValues);

        //question.setId((int) id);

        return (int) id;
    }


    public int update(Question question) {


        /*
        * It takes the values of the Question object passed and update it on the database
        * */
        ContentValues contentValues = new ContentValues();
        contentValues.put(QuestionsContract.Columns.ANSWER, question.getAnswer());
        contentValues.put(QuestionsContract.Columns.IMAGE_PATH, question.getImage());

        //It creates an array with the IDs of the objects that are going to be changed in the table
        //In this case, it just contains the id of the object Question passed.
        String[] questionID = new String[]{
                String.valueOf(question.getId())
        };

        long updatedRows = database.update(QuestionsContract.TABLE_NAME, contentValues, QuestionsContract.Columns.ID + " = ?", questionID);

        return (int) updatedRows;
    }

    public void delete(Question question) {

        //It creates an array with the IDs of the objects that are going to be changed in the table
        //In this case, it just contains the id of the object Question passed.
        String[] questionID = new String[]{
                String.valueOf(question.getId())
        };

        database.delete(QuestionsContract.TABLE_NAME, QuestionsContract.Columns.ID + " = ?", questionID);

    }


    public Question getQuestonById(int id) {

        Question question = null;


        try {

            Cursor cursor = database.query(QuestionsContract.TABLE_NAME, columns,
                    QuestionsContract.Columns.ID + " = ? ", new String[]{String.valueOf(id)}, null, null, null);

            if (cursor.moveToFirst()) {

                do {
                    question = takeQuestionFromCursor(cursor);
                } while (cursor.moveToNext());
            }


        } catch (SQLiteDatabaseLockedException exception) {

            Log.e(ERROR_MESSAGE, exception.getMessage());

        }

        return question;
    }

    public List<Question> list() {


        List<Question> questions = new ArrayList<>();


        try {

            Cursor cursor = database.query(QuestionsContract.TABLE_NAME, columns, null, null, null, null,
                    QuestionsContract.Columns.ANSWER);


            if (cursor.moveToFirst()) {

                do {
                    Question question = takeQuestionFromCursor(cursor);
                    questions.add(question);

                } while (cursor.moveToNext());
            }


        } catch (SQLiteDatabaseLockedException exception) {

            Log.e(ERROR_MESSAGE, exception.getMessage());

        }

        return questions;

    }


    //------------------- Auxiliary Methods --------------------

    private Question takeQuestionFromCursor(Cursor cursor) {

        int id = cursor.getInt(cursor.getColumnIndex(QuestionsContract.Columns.ID));
        String answer = cursor.getString(cursor.getColumnIndex(QuestionsContract.Columns.ANSWER));
        String imagePath = cursor.getString(cursor.getColumnIndex(QuestionsContract.Columns.IMAGE_PATH));

        return new Question(id, imagePath, answer);

    }


}
