package com.example.nick.carbontracker.UI;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.nick.carbontracker.R;

import java.util.Calendar;

/**
 * Menu activity
 * has 3 buttons for add jounrey, view carbon footprint and view graph
 */

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar().hide();
        // Initializing buttons to be activated
        Button addJourney = (Button) findViewById(R.id.addJourneyButton);
        Button viewFootprint = (Button) findViewById(R.id.viewFootprintButton);

        // Activate onCLickListener for Buttons

        setupNotification();
        activateButton(addJourney);
        activateButton(viewFootprint);
        aboutButton();
        setChartButton();
        setMoreGraphButton();
        monthlyUtilitiesBtn();
    }

    private void setupNotification() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        Intent intent = new Intent(getApplicationContext(),NotificationPublisher.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,
                intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,pendingIntent);
    }

    private void setMoreGraphButton() {

        Button btn = (Button) findViewById(R.id.moreGraphButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GraphMap.class);
                startActivity(intent);
            }
        });
    }

    private void aboutButton() {

        Button btn = (Button) findViewById(R.id.about_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(intent);
            }
        });
    }

    //setup monthly utilities button
    private void monthlyUtilitiesBtn() {
        Button btn = (Button) findViewById(R.id.monthlyUtilitiesButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MonthlyUtilities.class);
                startActivity(intent);
            }
        });
    }

    //button for chart activity
    private void setChartButton() {
        Button btn = (Button) findViewById(R.id.graphButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GraphTrip.class);
                startActivity(intent);
            }
        });
    }

    //buttons for footprint and transport activity
    private void activateButton(final Button btn) {
        // Distinguishes button by id and sets up appropriate method for it
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (btn.getId()) {
                    case R.id.addJourneyButton:
                        callTransportationActivity();
                        break;
                    case R.id.viewFootprintButton:
                        callFootprintActivity();
                        break;
                    default:
                        break;
                }

            }
        });

    }

    //start footprint activity
    private void callFootprintActivity() {
        // TODO: add Intent to call Footprint Activity
        Intent intent = new Intent(getApplicationContext(), JourneyList.class);
        startActivity(intent);
    }

    //start transport activity
    private void callTransportationActivity() {

        // Getting intent for the transportation mode activity and starting it
        Intent intent = new Intent(getApplicationContext(), TransportationModeActivity.class);
        startActivity(intent);
    }

}
