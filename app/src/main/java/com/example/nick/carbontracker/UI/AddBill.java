package com.example.nick.carbontracker.UI;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nick.carbontracker.R;
import com.example.nick.carbontracker.model.CustomDate;
import com.example.nick.carbontracker.model.MonthlyUtilityBill;
import com.example.nick.carbontracker.model.Singleton;

/**
 * add bill class, once we create a new monthlity utility, we use this add bill to add the consume.
 */
public class AddBill extends AppCompatActivity {
    private static final int DIALOG_ID = 0;
    public static final int DIALOG_ID1 = 1;
    private static boolean ifEdit = false;
    private Singleton singleton = Singleton.getInstance();

    private static int position;
    private static String nickName;
    private static String numGas;
    private static String numElec;
    private static String numUsers;

    private static CustomDate dateFrom;
    private static CustomDate dateTo;
    private CustomDate dateTemp;

    private static final int year = 2017;
    private static final int month = 2;
    private static final int day = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);
        if(ifEdit){
            populateFields();
            setupDeleteActivityButton();
        }
        setupEndActivityButton();
        showDialogDateFrom();
        showDialogDateTo();
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

    private void generateBill(){
        boolean isUserEntryValid = true;
        EditText editName = (EditText) findViewById(R.id.editTextBillName);
        //if input is string with length less than 1
        if (editName.getText().toString().length() == 0) {
            editName.setError(getString(R.string.at_least_1_character));
            Toast.makeText(AddBill.this, getString(R.string.error), Toast.LENGTH_SHORT).show();
            isUserEntryValid = false;
        }
        String userNickName = editName.getText().toString();

        EditText editGas = (EditText) findViewById(R.id.editTextGas);
        String userStringGas = editGas.getText().toString();
        double numGas = 0;
        //Check if num is valid in that is non negative
        try {
            numGas = Double.parseDouble(userStringGas);
            if (numGas < 0) {
                editGas.setError(getString(R.string.positive_prompt));
                Toast.makeText(AddBill.this, getString(R.string.error), Toast.LENGTH_SHORT).show();

                isUserEntryValid = false;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            editGas.setError(getString(R.string.invalid_number));
            Toast.makeText(AddBill.this, getString(R.string.error), Toast.LENGTH_SHORT).show();
            isUserEntryValid = false;
        }

        EditText editElectricity = (EditText) findViewById(R.id.editTextElectrcity);
        String userStringElec = editElectricity.getText().toString();
        double numElec = 0;
        //Check if num is valid in that is non negative
        try {
            numElec = Double.parseDouble(userStringElec);
            if (numElec < 0) {
                editElectricity.setError(getString(R.string.positive_prompt));
                Toast.makeText(AddBill.this, getString(R.string.error), Toast.LENGTH_SHORT).show();

                isUserEntryValid = false;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            editElectricity.setError(getString(R.string.invalid_number));
            Toast.makeText(AddBill.this, getString(R.string.error), Toast.LENGTH_SHORT).show();
            isUserEntryValid = false;
        }

        EditText editNumUsers = (EditText) findViewById(R.id.editTextUsers);
        String userStringUsers = editNumUsers.getText().toString();
        int numUsers = 0;
        //Check if num is valid in that is non negative
        try {
            numUsers = Integer.parseInt(userStringUsers);
            if (numUsers < 0) {
                editNumUsers.setError(getString(R.string.positive_prompt));
                Toast.makeText(AddBill.this, getString(R.string.error), Toast.LENGTH_SHORT).show();
                isUserEntryValid = false;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            editNumUsers.setError(getString(R.string.invalid_number));
            Toast.makeText(AddBill.this, getString(R.string.error), Toast.LENGTH_SHORT).show();
            isUserEntryValid = false;
        }
        TextView textDateFrom = (TextView) findViewById(R.id.textViewDateFrom);
        TextView textDateTo = (TextView) findViewById(R.id.textViewDateTo);
        if(dateFrom == null || dateTo == null){
            isUserEntryValid = false;
            textDateFrom.setError(getString(R.string.please_enter_date));
            textDateTo.setError(getString(R.string.please_enter_date));
            Toast.makeText(AddBill.this, getString(R.string.error), Toast.LENGTH_SHORT).show();
        }else if(dateFrom.compareGreater(dateTo)){
            textDateFrom.setError(getString(R.string.start_date_before_end_date));
            Toast.makeText(AddBill.this, getString(R.string.error), Toast.LENGTH_SHORT).show();
            isUserEntryValid = false;
        }
        MonthlyUtilityBill temp_bill = new MonthlyUtilityBill(userNickName, numUsers, numElec, numGas, dateFrom, dateTo);
        //if both name and weight is valid, return intent to main activity
        if (isUserEntryValid) {
            if(ifEdit)
                singleton.getMonthlyBill(position).setMonthlyBill(temp_bill);
            else
                singleton.addMonthlyBill(temp_bill);
        }
        finish();
    }

    //displaying current bill when editing
    private void populateFields() {
        MonthlyUtilityBill bill = singleton.getMonthlyBill(position);
        EditText editName = (EditText) findViewById(R.id.editTextBillName);
        EditText editElec = (EditText) findViewById(R.id.editTextElectrcity);
        EditText editGas = (EditText) findViewById(R.id.editTextGas);
        EditText editUsers = (EditText) findViewById(R.id.editTextUsers);
        TextView textDateFrom = (TextView) findViewById(R.id.textViewDateFrom);
        TextView textDateTo = (TextView) findViewById(R.id.textViewDateTo);
        textDateFrom.setText(bill.getDateFrom().toString());
        textDateTo.setText(bill.getDateTo().toString());
        editName.setText(bill.getName());
        editElec.setText(Double.toString(bill.getNumElectricity()));
        editGas.setText(Double.toString(bill.getNumGas()));
        editUsers.setText(Integer.toString(bill.getNumUsers()));
    }

    //Button for ending editing or adding
    private void setupEndActivityButton() {
        Button btn = (Button) findViewById(R.id.btnEnd);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateBill();
            }
        });
    }

    //BUTTON SETUP*******************************************************************************
    private void setupDeleteActivityButton() {
        //make delete button visible if editing
        Button btn = (Button) findViewById(R.id.btnDelete);
        btn.setVisibility(View.VISIBLE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.removeMonthlyBill(position);
                finish();
            }
        });
    }

    //make intent
    public static Intent makeIntent(Context context, int pos, boolean edit) {
        ifEdit = edit;
        position = pos;
        return new Intent(context, AddBill.class);
    }

    //METHODS FOR ACTION BAR******************************************************************
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_bill_menu, menu);
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

}
