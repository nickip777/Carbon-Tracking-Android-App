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
import com.example.nick.carbontracker.model.Journey;
import com.example.nick.carbontracker.model.Singleton;
import com.example.nick.carbontracker.model.Transportation;

/**
 * Select the route
 * has listview of routes and an add route button that launches dialog
 */

public class SelectRouteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_route);

        //populate route list
        populateRouteList();
        routeClickCallback();
        //launch dialog
        activateAddRouteButton();
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
    public void populateRouteList() {
        // Build Adapter
        // Note: we are using the same layout file as we are for the cars,
        // Do not let the name confuse you
        Singleton.getInstance().saveUserData(getApplicationContext(), Singleton.LIST_OF_ROUTES);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,                   // Context for the activity
                R.layout.list_of_routes, // Layout used to (create)
                Singleton.getInstance().getPrintableRouteList());  // Routes to be displayed

        // Configure the list view
        ListView list = (ListView) findViewById(R.id.listOfRoutes);
        list.setAdapter(adapter);

    }

    //when user clicks listview
    public void routeClickCallback() {
        ListView list = (ListView) findViewById(R.id.listOfRoutes);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {

                TextView textView = (TextView) viewClicked;
                String message = getString(R.string.you_clicked) + position
                        + getString(R.string.which_route) + textView.getText().toString();
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                //launch footprint activity
                callDateSelect(position);
            }
        });
        //edit on long click
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View viewClicked, int position, long id) {

                // Pass Bundle to Dialog
                Bundle bundle = new Bundle();
                bundle.putInt("Position", position);// set Fragmentclass Arguments
                bundle.putBoolean("EditMode", Boolean.TRUE);

                FragmentManager manager = getSupportFragmentManager();
                AddRouteDialog dialog = new AddRouteDialog();
                dialog.setArguments(bundle);
                dialog.show(manager, "AddRouteDialog");
                return true;
            }
        });

    }

    private void callDateSelect(int routePosition) {
        Intent carActivityIntent = getIntent();
        boolean editJourney = carActivityIntent.getBooleanExtra("edit_journey",Boolean.FALSE);
        int journeyPosition = carActivityIntent.getIntExtra("journey_position",0);
        Intent intent = new Intent(getApplicationContext(), SelectDatesForJourney.class);
        int carPosition = carActivityIntent.getIntExtra("carPosition", 0);
        intent.putExtra("carPosition", carPosition);
        intent.putExtra("routePosition", routePosition);
        intent.putExtra("journeysPosition", journeyPosition);
        intent.putExtra("editJourneys", editJourney);
        startActivity(intent);
        finish();
    }

    //button for launching dialog
    private void activateAddRouteButton() {
        Button btn = (Button) findViewById(R.id.addRouteButton);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAddRouteDialog();
            }
        });
    }

    // Call dialog to add Car
    private void callAddRouteDialog() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("EditMode", Boolean.FALSE);
        FragmentManager manager = getSupportFragmentManager();
        AddRouteDialog dialog = new AddRouteDialog();
        dialog.setArguments(bundle);
        dialog.show(manager, "AddRouteDialog");
    }

}
