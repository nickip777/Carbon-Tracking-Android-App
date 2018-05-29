package com.example.nick.carbontracker.UI;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.nick.carbontracker.R;
import com.example.nick.carbontracker.model.Singleton;
import com.example.nick.carbontracker.UI.fourWeekGraph.SelectDayForFourWeeks;
import com.example.nick.carbontracker.UI.oneDayGraph.SelectDay;
import com.example.nick.carbontracker.UI.yearGraph.SelectDayForYear;
import com.example.nick.carbontracker.model.Transportation;

/**
 * a map to help user choose which kind of graph they want to see
 */
public class GraphMap extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_map);

        setOneDayButton();
        setFourWeeksButton();
        setOneYearButton();
        setChangeUnitsButton();
    }

    //METHODS FOR ACTION BAR******************************************************************
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    //when user clicks action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cancel_button:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void setFourWeeksButton() {
        Button btn = (Button) findViewById(R.id.fourWeekGraph);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SelectDayForFourWeeks.class);
                startActivity(intent);
            }
        });
    }

    private void setOneYearButton() {
        Button btn = (Button) findViewById(R.id.oneYearGraph);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SelectDayForYear.class);
                startActivity(intent);
            }
        });
    }


    private void goToGraphTrip() {


    }
    private void setOneDayButton() {
        Button btn = (Button) findViewById(R.id.oneDayGraph);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SelectDay.class);
                startActivity(intent);
            }
        });
    }
    private void setChangeUnitsButton() {
        Button btn = (Button) findViewById(R.id.changeUnitsButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeUnits();
                String message = getString(R.string.changeUnitsToast) + Singleton.getInstance().getUnit(GraphMap.this);
                Toast.makeText(GraphMap.this,message , Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeUnits() {
        if(Singleton.getInstance().getHumanRelatableUnits()){
                Singleton.getInstance().setHumanRelatableUnits(false);
        }
        else Singleton.getInstance().setHumanRelatableUnits(true);
    }

}
