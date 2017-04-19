package com.example.farhankhan.spacegame;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.pm.ActivityInfo;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_settings);

        username = (TextView) findViewById(R.id.userNameView);
        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Open the change username dialog onClick
                DialogFragment dialogFrag = new ChangeUsernameDialogFragment();
                dialogFrag.show(getFragmentManager(), "changeUsername");

            }
        });

    }
}
