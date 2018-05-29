package com.example.nick.carbontracker.UI;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.nick.carbontracker.R;
import com.example.nick.carbontracker.model.Singleton;

/**
 *
 * Splash screen for app start
 */
public class MainActivity extends AppCompatActivity {
    public static final int TIMER_RUNTIME = 4000;
    Thread splashTread;
    boolean clicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        //set background image and transparency
        Drawable background = getResources().getDrawable(R.drawable.background);
        background.setAlpha(80);
        Singleton singleton = Singleton.getInstance();
        singleton.loadUserData(getApplicationContext(), Singleton.LIST_OF_CARS);
        singleton.loadUserData(getApplicationContext(), Singleton.LIST_OF_ROUTES);
        singleton.loadUserData(getApplicationContext(), Singleton.LIST_OF_JOURNEYS);
        singleton.loadUserData(getApplicationContext(), Singleton.LIST_OF_MONTHLYBILLS);
        singleton.loadUserData(getApplicationContext(), Singleton.TIP_HISTORY);
        singleton.initializeMakeSpinner(getApplicationContext());
        singleton.initializeModelSpinner(getApplicationContext());
        singleton.initializeYearSpinner(getApplicationContext());

        //setup skip button
        Button btn = (Button) findViewById(R.id.skipBtn);
        btn.setBackgroundResource(R.drawable.play_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                clicked = true;
                startActivity(intent);
                finish();
            }
        });

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.CYAN, android.graphics.PorterDuff.Mode.SRC_IN);

        //set up splashscreen timeout
        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < TIMER_RUNTIME) {
                        sleep(100);
                        waited += 100;
                        // Update the progress bar
                    }
                    if (clicked != true) {
                        Intent intent = new Intent(MainActivity.this,
                                MenuActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        //start menu activity
                        startActivity(intent);
                    }
                    finish();
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    //end splash screen
                    finish();
                }
            }
        };
        splashTread.start();
    }

}
