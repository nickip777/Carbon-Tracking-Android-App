package com.example.nick.carbontracker.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nick.carbontracker.R;
import com.example.nick.carbontracker.model.Car;
import com.example.nick.carbontracker.model.Singleton;

/**
 * Activity for user to select car
 * has list view and button to launch dialog to add or edit car
 * launches route activity if car is selected
 */
public class TransportationModeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transportation_mode);

        populateTransportationList();
        carClickCallback();
        activateAddCarButton();
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
    //populate list view
    public void populateTransportationList() {
        Singleton singleton = Singleton.getInstance();
        singleton.saveUserData(getApplicationContext(), Singleton.LIST_OF_CARS);

        // Build Adapter
        ArrayAdapter<Car> adapter = new MyListAdapter();

        // Configure the list view
        ListView list = (ListView) findViewById(R.id.listOfUserCars);
        list.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter<Car>{
        public MyListAdapter() {
            super(TransportationModeActivity.this,R.layout.list_of_cars,Singleton.getInstance().getUserCars());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.list_of_cars,parent,false);
            }
            Car currentCar = Singleton.getInstance().getUserCars().get(position);

            ImageView imageView = (ImageView)itemView.findViewById(R.id.icon_item);
            imageView.setImageResource(currentCar.getIconID());

            TextView nickNameText = (TextView)itemView.findViewById(R.id.nick_name_text);
            nickNameText.setText(currentCar.getNickName());

            return itemView;
        }
    }

    //when user clicks list view
    public void carClickCallback() {
        ListView list = (ListView) findViewById(R.id.listOfUserCars);
        //on item click
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                if(position > 2) {
                    TextView textView = (TextView) findViewById(R.id.nick_name_text);
                    String message = getString(R.string.you_clicked) + position
                            + getString(R.string.which_car) + textView.getText().toString();
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                }
                onTransportationClicked(position);
            }
        });
        //on long click for edit
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View viewClicked, int position, long id) {

                // Pass Bundle to Dialog
                if(position > 2) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("Position", position);// set Fragment class Arguments
                    bundle.putBoolean("EditMode", Boolean.TRUE);

                    FragmentManager manager = getSupportFragmentManager();
                    AddCarDialog dialog = new AddCarDialog();
                    dialog.setArguments(bundle);
                    dialog.show(manager, "AddCarDialog");
                    return true;
                }
                return true;
            }
        });
    }

    //launch route activity
    private void onTransportationClicked(int position) {
        Intent journeyListIntent = getIntent();
        boolean bool = journeyListIntent.getBooleanExtra("editJourney",Boolean.FALSE);
        int journeyPosition = journeyListIntent.getIntExtra("journeyPosition", 0);
        Intent intent = new Intent(getApplicationContext(), SelectRouteActivity.class);
        intent.putExtra("carPosition", position);
        intent.putExtra("edit_journey",bool);
        intent.putExtra("journey_position",journeyPosition);
        startActivity(intent);
        finish();
    }

    //open dialog for add car
    private void activateAddCarButton() {
        Button btn = (Button) findViewById(R.id.addCarButton);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAddCarDialog();
            }
        });
    }

    // Call dialog to add Car
    private void callAddCarDialog() {

        Bundle bundle = new Bundle();
        bundle.putBoolean("EditMode", Boolean.FALSE);

        FragmentManager manager = getSupportFragmentManager();
        AddCarDialog dialog = new AddCarDialog();
        dialog.setArguments(bundle);
        dialog.show(manager, "AddCarDialog");
    }
}
