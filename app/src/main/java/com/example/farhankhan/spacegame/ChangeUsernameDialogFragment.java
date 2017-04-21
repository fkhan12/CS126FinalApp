package com.example.farhankhan.spacegame;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.EditText;

/**
 * called by onClick of the textView that displays the username in settings
 * see documentation: https://developer.android.com/guide/topics/ui/dialogs.html
 *
 * Created by FarhanKhan on 4/16/2017.
 */

public class ChangeUsernameDialogFragment extends DialogFragment{

    EditText editText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        //Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.username_dialog, null))
        //Add action buttons
            .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {

                    //User clicked on set new username button
                    //add code here for changing the username in shared preferences and updating the username in the database (if possible - first check if the username already exists (if it does then don't do anything except make a toast saying that this username is already taken), otherwise copy the value of the existing username in the database, then delete the key-value pair, then make a new key-value pair with the new username, then update the shared preferences)
                    SharedPreferences settings = getActivity().getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    //String prevUsernameStored = ;
                    //editor.putString("username", R.id.username.getText().toString());

                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    //user cancelled the dialog
                    ChangeUsernameDialogFragment.this.getDialog().cancel();
                }
            });

        //Chain together various setter methods to set the dialog characteristics
        builder.setTitle("Change Username");

        //Get the AlertDialog from create()
        AlertDialog dialog = builder.create();

        return dialog;
    }

}
