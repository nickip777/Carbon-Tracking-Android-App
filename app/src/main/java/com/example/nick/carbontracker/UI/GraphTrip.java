package com.example.nick.carbontracker.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.nick.carbontracker.R;
import com.example.nick.carbontracker.model.Journey;
import com.example.nick.carbontracker.model.Singleton;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * graph for carbon footprint
 * shows pie chart
 */
public class GraphTrip extends AppCompatActivity {
    private Singleton singleton = Singleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_trip);
        setupChart();
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
    //setup pie chart
    private void setupChart() {
        List<PieEntry> pieEntries = new ArrayList<>();
        Journey journey;
        for (int i = 0; i < singleton.getJourneys().size(); i++) {
            double kgPerGallon;
            journey = singleton.getJourneys().get(i); // Journey being used
            //switch for type of gasoline

            //calc carbon consumed
            double carbonConsumed;
            carbonConsumed = singleton.getCarbonConsumed(journey);

            float floatNumber = (float) carbonConsumed;
            String routeName = singleton.getJourneys().get(i).getRouteName();
            pieEntries.add(new PieEntry(floatNumber, routeName));

        }

        //setup pie chart
        PieDataSet dataset = new PieDataSet(pieEntries, getString(R.string.routeCarbonEmit));
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        dataset.setValueTextSize(40f);
        PieData data = new PieData(dataset);

        //animate
        PieChart chart = (PieChart) findViewById(R.id.chart);
        chart.setData(data);
        chart.animateY(1000);
        chart.invalidate();
    }


}
