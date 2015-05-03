package com.whatsaround.whatsaround.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.whatsaround.whatsaround.R;
import com.whatsaround.whatsaround.adapter.QuestionsAdapter;
import com.whatsaround.whatsaround.data.QuestionDAO;
import com.whatsaround.whatsaround.dialog.DeletionDialog;
import com.whatsaround.whatsaround.dataType.Question;

import java.util.List;
import java.util.Set;


public class SettingsActivity extends ActionBarActivity implements AdapterView.OnItemClickListener, AbsListView.MultiChoiceModeListener, DeletionDialog.DeletionDialogListener {

    public static final String QUESTION_KEY = "question";
    private QuestionsAdapter adapter;
    private ListView questionsListView;
    //private List<Question> questionsSelected = new ArrayList<>();
    private List<Question> questionsListed;
    private QuestionDAO questionDAO;
    private TextView emptyListText;
    private int numberOfQuestionsSelected = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        questionDAO = QuestionDAO.getInstance(this);

        questionsListed = questionDAO.list();


        emptyListText = (TextView) findViewById(R.id.txt_empty_list);
        /* Take the listView (which is referenced inside the layout file of this Activity
        and pass the list of questions to it through the adapter.
        Also make it handle simple clicks*/
        questionsListView = (ListView) findViewById(R.id.activity_settings_list);
        updateList(questionsListed);
        questionsListView.setOnItemClickListener(this);


        //Configure the ListView to handle multiple selections of the items in the list - in order to delete
        questionsListView.setMultiChoiceModeListener(this);
        questionsListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);


    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        TextView answerView = (TextView) view.findViewById(R.id.txt_answer);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);

        //Create Intent and pass the values of the Views (answer and image) to the EditQuestionActivity
        Intent intent = new Intent(getApplicationContext(), EditQuestionActivity.class);

        // Question question = new Question((int) id, imageView.getText().toString(), answerView.getText().toString());

        intent.putExtra(QUESTION_KEY, String.valueOf(id));


        startActivity(intent);
    }


    //--------------------- Options Menu ----------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Make the main menu appear
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //If the menu button clicked was the "Add question button", call another Activity
        if (id == R.id.mnu_add_question) {
            Intent intent = new Intent(getApplicationContext(), EditQuestionActivity.class);

            startActivity(intent);
        }

        return false;
    }


    //--------------------- Multiple Selection of Items ----------------------------
    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {


        //When some item receives a long click, add it to the list of questions selected and make its background blue.
        //Otherwise, delete it from the list of questions selected and make its color be the same as the list's color.

        //Question questionSelected = (Question) adapter.getItem(position);
        //View question = questionsListView.getChildAt(position);

        if (checked) {

            numberOfQuestionsSelected++;
            adapter.setNewSelection(position, checked);
            // questionsSelected.add(questionSelected);
            //question.setBackgroundColor(Color.parseColor("#BBDEFB"));
        } else {
            numberOfQuestionsSelected--;
            adapter.removeSelection(position);
            //questionsSelected.remove(questionSelected);
            //question.setBackgroundColor(Color.TRANSPARENT);
        }

        mode.setTitle(numberOfQuestionsSelected + " selected");
    }


    //Create the menu when one item is holden
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        //Make the deletion menu appear
        numberOfQuestionsSelected = 0;
        getMenuInflater().inflate(R.menu.menu_settings_list_deletion, menu);

        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

        //If the button of deletion is clicked, take all the items selected and delete them.
        //Then, finish the "Deletion Menu"
        if (item.getItemId() == R.id.mnu_delete_question) {


            //Create Dialog for the user to confirm or not the deletion of the item(s)
            DeletionDialog deletionDialog = DeletionDialog.newInstance(adapter.getPositionsOfSelectedQuestions(),this);
            deletionDialog.show(getFragmentManager(), "Settings Activity");


            mode.finish();
            return true;
        }


        return false;


    }


    @Override
    public void onDestroyActionMode(ActionMode mode) {

        adapter.clearEntireSelection();


    }


    //--------------------- Dialog for Deletion ----------------------------

    /*
    * When "Yes" Button clicked, delete the items selected
    * */
    @Override
    public void onDialogPositiveClick(Set<Integer> questionsSelected) {

        int deletedRows = 0;

        //Delete selected Items
        for (Integer questionId : questionsSelected) {

            Question question = (Question) adapter.getItem(questionId);

            deletedRows = questionDAO.delete(question);
        }

        if (deletedRows > 0) {

            adapter.clearEntireSelection();

            updateList(questionDAO.list());

            Toast.makeText(getApplicationContext(), "Deleted Questions: " + numberOfQuestionsSelected, Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(getApplicationContext(), "Error deleting questions.", Toast.LENGTH_SHORT).show();
        }


    }


    //--------------------- Private Methods ----------------------------


    /*
    * Take the items to be relisted (not deleted ones) and make their background transparent.
    * Then make the list of selected questions empty.
    * At last, notify the changes to the adapter
    * */
//    private void clearQuestionsSelected() {
//
//
//        int count = questionsListView.getChildCount();
//
//
//        for (int i = 0; i < count; i++) {
//
//            View questionView = questionsListView.getChildAt(i);
//            questionView.setBackgroundColor(Color.TRANSPARENT);
//
//        }
//
//        questionsSelected.clear();
//
//    }


    private void updateList(List<Question> list) {


        if (list.isEmpty()) {
            questionsListView.setVisibility(View.GONE);
            emptyListText.setVisibility(View.VISIBLE);
        } else {
            emptyListText.setVisibility(View.GONE);
            questionsListView.setVisibility(View.VISIBLE);

            adapter = new QuestionsAdapter(this, list);
            questionsListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }


}
