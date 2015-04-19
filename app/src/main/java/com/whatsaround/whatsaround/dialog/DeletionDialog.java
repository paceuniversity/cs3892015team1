package com.whatsaround.whatsaround.dialog;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class DeletionDialog extends DialogFragment implements DialogInterface.OnClickListener {

    //Instance of the interface to deliver action events
    DeletionDialogListener deletionDialogListener;


    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it.
     * */
    public interface DeletionDialogListener {

        public void onDialogPositiveClick(DialogFragment dialog);

        public void onDialogNegativeClick(DialogFragment dialog);
    }


    //When this Dialog is attached to some Activity
    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);

        // Verify if the host activity implements the callback interface
        try {

            // Instantiate the NoticeDialogListener so we can send events to the host
            deletionDialogListener = (DeletionDialogListener) activity;

        } catch (ClassCastException e) {

            //The activity doesn't implement the interface, then throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");

        }
    }

    /*
     *  Creates an AlertDialog and sets its message,and positive and negative buttons
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to delete the selected item(s)?")
                .setPositiveButton("Yes", this)
                .setNegativeButton("No", this);

        return builder.create();

    }

    /*
     * When one of the buttons is clicked, return the result for the class that called this Dialog.
     * (The class who called is a Listener).
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {

        if (which == DialogInterface.BUTTON_POSITIVE) {

            deletionDialogListener.onDialogPositiveClick(DeletionDialog.this);

        } else {

            deletionDialogListener.onDialogNegativeClick(DeletionDialog.this);

        }
    }
}