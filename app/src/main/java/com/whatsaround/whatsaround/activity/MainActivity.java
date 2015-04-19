package com.whatsaround.whatsaround.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.WindowManager;

import com.whatsaround.whatsaround.R;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Sets the main activity to full screen mode
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorView = getWindow().getDecorView();

        //Code above doesn't work on versions higher than 16. This code bellow correct this.
        if (Build.VERSION.SDK_INT > 16) {
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }


        // Sets the layout resource to MainActivity
        setContentView(R.layout.activity_main);
    }


    public void goToSettings(View view) {

        Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);

        startActivity(settingsIntent);
    }

    public void startPlaying(View view) {

        //Implement link to child perspective

        //Intent to QuizActivity (create this Activity)
    }
}
