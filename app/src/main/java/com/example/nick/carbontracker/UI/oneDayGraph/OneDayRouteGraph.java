package com.example.nick.carbontracker.UI.oneDayGraph;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.nick.carbontracker.R;
import com.example.nick.carbontracker.model.CustomDate;
import com.example.nick.carbontracker.model.DailyUtilityBill;
import com.example.nick.carbontracker.model.Journey;
import com.example.nick.carbontracker.model.Singleton;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * graph to show one day carbon emission route version
 */
public class OneDayRouteGraph extends AppCompatActivity {
    private Singleton singleton= Singleton.getInstance();
    private CustomDate daySelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_day_route_graph);
        daySelected = singleton.returnDay();
        try {
            setupChart();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
    //setup pie chart
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

    private void setupChart() throws ParseException {


        List<DailyUtilityBill> monthCost = singleton.getUsageDailyBills(singleton.returnDay(), 1);
        //monthCost.get(0).getNumElectricity();

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        float elecSum = 0f;
        float gasSum = 0f;
        for (int i = 0; i < 1; i++) {
            float val1 = 0.001f;
            float val2 = 0.001f;

            if (monthCost.get(i).getNumElectricity() != 0) {
                val1 = (float) (monthCost.get(i).getNumElectricity());
            }
            if (monthCost.get(i).getNumGas() != 0) {
                val2 = (float) (monthCost.get(i).getNumGas());
            }
            elecSum += val1;
            gasSum += val2;
        }


        List<Journey> journeyForPie = new ArrayList<>();
        Journey journey;
        for(int i = 0; i<singleton.getJourneys().size();i++){
            journey = singleton.getJourneys().get(i); // Journey being used
            if(journey.getDateFrom().equals(daySelected) || journey.getDateTo().equals(daySelected)){
                journeyForPie.add(journey);
            }
        }

        List<PieEntry> pieEntries = new ArrayList<>();

        pieEntries.add(new PieEntry(elecSum, getString(R.string.elec)));
        pieEntries.add(new PieEntry(gasSum, getString(R.string.gas)));
        for(int i=0; i<journeyForPie.size();i++){
            float val3 = (float)journeyForPie.get(i).getTotalGasUsed();
            pieEntries.add(new PieEntry(val3, journeyForPie.get(i).getNickName()));

        }






        PieDataSet dataset = new PieDataSet(pieEntries, getString(R.string.this_day_carbon_consume));
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataset);

        //animate
        PieChart chart = (PieChart) findViewById(R.id.oneDayRoute);
        chart.setData(data);
        chart.animateY(1000);
        chart.invalidate();
    }
}
