package com.example.nick.carbontracker.UI;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nick.carbontracker.R;
import com.example.nick.carbontracker.model.Singleton;

/**
 * to display the monthly utilities of gas and electricity
 */
public class MonthlyUtilities extends AppCompatActivity {

    public static final int REQUEST_CODE = 1024;
    private Boolean showDialog = Boolean.FALSE;
    private Singleton singleton= Singleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_utilities);
        populateMonthlyUtilitiesList();
        activateAddBillButton();
        billClickCallback();
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

    //when user clicks list view
    public void billClickCallback() {
        ListView list = (ListView) findViewById(R.id.listOfUserBills);
        //on item click
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                TextView textView = (TextView) viewClicked;
                String message = getString(R.string.you_clicked) + position
                        + getString(R.string.which_bill) + textView.getText().toString();
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                //launch bill activity
                Intent intent = AddBill.makeIntent(getApplicationContext(), position, true);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
    }
    //populate list view
    public void populateMonthlyUtilitiesList() {
        singleton.saveUserData(getApplicationContext(), Singleton.LIST_OF_MONTHLYBILLS);
        String[] bills = singleton.userBillListToString();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_of_bills,bills);
        ListView list = (ListView) findViewById(R.id.listOfUserBills);
        list.setAdapter(adapter);
    }

    //result from activities
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            //when user clicks normal add pot
            case REQUEST_CODE:
                populateMonthlyUtilitiesList();
                showDialog = true;
                break;
        }
    }
    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

        // play with fragments here
        if (showDialog) {
            showDialog = false;
            singleton.createTips(getApplicationContext());
            if (!singleton.getTipsIsEmpty()) {
                FragmentManager manager = getSupportFragmentManager();
                tipsDialog dialog = new tipsDialog();
                dialog.show(manager, "tipsDialog");
            }
        }
    }



    //open dialog for add car
    private void activateAddBillButton() {
        Button btn = (Button) findViewById(R.id.buttonAddBill);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AddBill.makeIntent(MonthlyUtilities.this,0,false);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
    }

}
