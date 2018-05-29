package com.example.nick.carbontracker.UI;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.example.nick.carbontracker.R;
import com.example.nick.carbontracker.model.Journey;
import com.example.nick.carbontracker.model.Singleton;

public class JourneyList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Singleton singleton = Singleton.getInstance();
        setContentView(R.layout.activity_journey_list);
        if(!singleton.getJourneys().isEmpty()) {
            singleton.createTips(getApplicationContext());
            if (!singleton.getTipsIsEmpty() && singleton.isRecentJourneyOnCar()) {
                FragmentManager manager = getSupportFragmentManager();
                tipsDialog dialog = new tipsDialog();
                dialog.show(manager, "tipsDialog");
            }
        }
        populateJourneyList();
        journeyListCallBack();
    }

    @Override
    protected void onResume() {
        populateJourneyList();
        super.onResume();
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
    private void journeyListCallBack() {
        ListView list = (ListView) findViewById(R.id.listOfJourney);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editJourney(Boolean.TRUE, position);

            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteJourneyDialog(position);
                return true;
            }
        });
    }

    private void editJourney(boolean bool, int position) {
        Intent intent = new Intent(JourneyList.this, TransportationModeActivity.class);
        intent.putExtra("editJourney",bool);
        intent.putExtra("journeyPosition",position);
        startActivity(intent);
        //finish();
    }

    private void deleteJourneyDialog(int position){
        Bundle bundle = new Bundle();
        bundle.putInt("deletePosition", position);
        FragmentManager manager = getSupportFragmentManager();
        DeleteJourneyDialog dialog = new DeleteJourneyDialog();
        dialog.setArguments(bundle);
        dialog.show(manager, "DeleteJourneyDialog");
    }

    public void populateJourneyList() {
        Singleton.getInstance().saveUserData(getApplicationContext(), Singleton.LIST_OF_JOURNEYS);

        // Build Adapter
        // Note: we are using the same layout file as we are for the cars,
        // Do not let the name confuse you

        ArrayAdapter<Journey> adapter = new MyListAdapter();

        // Configure the list view
        ListView list = (ListView) findViewById(R.id.listOfJourney);
        list.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter<Journey>{
        public MyListAdapter() {
            super(JourneyList.this,R.layout.list_of_journeys,Singleton.getInstance().getJourneys());
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.list_of_journeys,parent,false);
            }
            Journey currentJourney = Singleton.getInstance().getJourneys().get(position);

            ImageView imageView = (ImageView)itemView.findViewById(R.id.journey_car_icon_item);
            imageView.setImageResource(currentJourney.getTransportation().getCar().getIconID());

            TextView nickNameText = (TextView)itemView.findViewById(R.id.car_and_route_name_text);
            nickNameText.setText(currentJourney.getNickName() + " - " + currentJourney.getRouteName());

            return itemView;
        }
    }
}
