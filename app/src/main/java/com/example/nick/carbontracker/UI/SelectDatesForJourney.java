package com.example.nick.carbontracker.UI;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nick.carbontracker.R;
import com.example.nick.carbontracker.model.CustomDate;
import com.example.nick.carbontracker.model.Journey;
import com.example.nick.carbontracker.model.Singleton;
import com.example.nick.carbontracker.model.Transportation;

/**
 * class to select the date for journey
 */

public class SelectDatesForJourney extends AppCompatActivity {
    private static final int DIALOG_ID = 0;
    public static final int DIALOG_ID1 = 1;
    private static final int year = 2017;
    private static final int month = 2;
    private static final int day = 20;
    private static CustomDate dateFrom;
    private static CustomDate dateTo;
    private Singleton singleton= Singleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_dates_for_journey);
        showDialogDateFrom();
        showDialogDateTo();
        setupEndActivityButton();
    }

    //Button for ending editing or adding
    private void setupEndActivityButton() {
        Button btn = (Button) findViewById(R.id.btnEnd);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateUserSelection();
            }
        });
    }

    private void validateUserSelection() {
        boolean isUserEntryValid = true;
        TextView textDateFrom = (TextView) findViewById(R.id.textViewDateFrom);
        TextView textDateTo = (TextView) findViewById(R.id.textViewDateTo);
        if(dateFrom == null || dateTo == null){
            isUserEntryValid = false;
            textDateFrom.setError(getString(R.string.please_enter_date));
            textDateTo.setError(getString(R.string.please_enter_date));
            Toast.makeText(SelectDatesForJourney.this, getString(R.string.error), Toast.LENGTH_SHORT).show();
        }else if(dateFrom.compareGreater(dateTo)){
            textDateFrom.setError(getString(R.string.start_date_before_end_date));
            Toast.makeText(SelectDatesForJourney.this, getString(R.string.error), Toast.LENGTH_SHORT).show();
            isUserEntryValid = false;
        }
        if(isUserEntryValid){
            Intent carActivityIntent = getIntent();
            boolean bool = carActivityIntent.getBooleanExtra("editJourneys",Boolean.FALSE);
            int journeyPosition = carActivityIntent.getIntExtra("journeysPosition",0);
            Transportation temp_transportation = new Transportation();
            int carPosition = carActivityIntent.getIntExtra("carPosition", 0);
            if(carPosition <= 2) {
                temp_transportation.setCar(singleton.getUserCars().get(carPosition));
                String transportation = singleton.getUserCars().get(carPosition).getNickName();
                if(transportation.equals("Pedestrian"))
                    temp_transportation.setPedestrian();
                else if(transportation.equals("Bus"))
                    temp_transportation.setBus();
                else
                    temp_transportation.setSkytrain();
            }
            else
                temp_transportation.setCar(singleton.getUserCars().get(carPosition));
            int routePosition = carActivityIntent.getIntExtra("routePosition", 0);
            Journey journey = new Journey(temp_transportation,
                    singleton.getUserRoutes().get(routePosition),dateFrom, dateTo);
            if(bool)
                singleton.getJourneys().set(journeyPosition,journey);
            else
                singleton.addJourney(journey);
            //Intent intent = new Intent(SelectDatesForJourney.this, JourneyList.class);
            //startActivity(intent);
            Singleton.getInstance().saveUserData(getApplicationContext(), Singleton.LIST_OF_JOURNEYS);
            finish();
        }

    }

    private void showDialogDateFrom(){
        TextView textView = (TextView) findViewById(R.id.textViewDateFrom);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
            }
        });
    }

    private void showDialogDateTo(){
        TextView textView = (TextView) findViewById(R.id.textViewDateTo);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID1);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID)
            return new DatePickerDialog(this, dateFromPickerListener, year, month, day);
        if (id == DIALOG_ID1)
            return new DatePickerDialog(this, dateToPickerListener, year, month, day);
        return null;
    }

    private DatePickerDialog.OnDateSetListener dateFromPickerListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day){
            dateFrom = new CustomDate(year,month,day);
            populateDateFromField();
        }
    };

    private DatePickerDialog.OnDateSetListener dateToPickerListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day){
            dateTo = new CustomDate(year,month,day);
            populateDateToField();
        }
    };

    private void populateDateFromField() {
        TextView textView = (TextView) findViewById(R.id.textViewDateFrom);
        textView.setText(dateFrom.toString());
    }

    private void populateDateToField() {
        TextView textView = (TextView) findViewById(R.id.textViewDateTo);
        textView.setText(dateTo.toString());
    }
}
