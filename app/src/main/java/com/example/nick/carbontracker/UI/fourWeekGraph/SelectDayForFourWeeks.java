package com.example.nick.carbontracker.UI.fourWeekGraph;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.nick.carbontracker.R;
import com.example.nick.carbontracker.model.CustomDate;
import com.example.nick.carbontracker.model.Singleton;

/**
 * select the end day for 28 days carbon consume graph
 */
public class SelectDayForFourWeeks extends AppCompatActivity {
    private static CustomDate daySelected;
    private static final int DIALOG_ID = 0;
    private static final int year = 2017;
    private static int month = 2;
    private static int day = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_day_for_four_weeks);
        showDialogDate();


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
    private void showDialogDate(){
        TextView textView = (TextView) findViewById(R.id.textViewSelectDayForFourWeeks);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
            }
        });
    }
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID)
            return new DatePickerDialog(this, dateFromPickerListener, year, month, day);
        return null;
    }

    private DatePickerDialog.OnDateSetListener dateFromPickerListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day){
            month = month;
            daySelected = new CustomDate(year,month,day);

            populateDateFromField();
        }
    };
    private void populateDateFromField() {
        TextView textView = (TextView) findViewById(R.id.textViewSelectDayForFourWeeks);
        textView.setText(daySelected.toString());
        Singleton.getInstance().setDay(daySelected);
        Intent intent = new Intent(getApplicationContext(), SelectGraphModeFourWeek.class);
        startActivity(intent);
    }
}
