package com.whatsaround.whatsaround.dialog;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import java.util.Set;

public class DeletionDialog extends DialogFragment implements DialogInterface.OnClickListener {

    //Instance of the interface to deliver action events
    private DeletionDialogListener deletionDialogListener;

    private Set<Integer> selectedItems;

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * */
    public interface DeletionDialogListener {

        public void onDialogPositiveClick(Set<Integer> selectedItems);

        ;
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

    public static DeletionDialog newInstance(Set<Integer> selectedItems, DeletionDialogListener listener){

        DeletionDialog dialog = new DeletionDialog();
        dialog.selectedItems = selectedItems;
        dialog.deletionDialogListener = listener;

        return dialog;
    }

    /*
     *  Creates an AlertDialog and sets its message,and positive and negative buttons
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (selectedItems.size() == 1) {
            builder.setMessage("Are you sure you want to delete the selected item?");
        } else {
            builder.setMessage("Are you sure you want to delete the selected items?");
        }


        builder.setPositiveButton("Yes", this);
        builder.setNegativeButton("No", this);

        return builder.create();

    }

    /*
     * When one of the buttons is clicked, return the result for the class that called this Dialog.
     * (The class who called is a Listener).
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {

        if (which == DialogInterface.BUTTON_POSITIVE && deletionDialogListener != null) {

            deletionDialogListener.onDialogPositiveClick(selectedItems);
        }
    }
}