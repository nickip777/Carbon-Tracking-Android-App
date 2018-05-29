package com.example.nick.carbontracker.UI.oneDayGraph;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.nick.carbontracker.R;
import com.example.nick.carbontracker.model.CustomDate;
import com.example.nick.carbontracker.model.DailyUtilityBill;
import com.example.nick.carbontracker.model.Journey;
import com.example.nick.carbontracker.model.MonthlyUtilityBill;
import com.example.nick.carbontracker.model.Singleton;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * show one day carbon consume distribution with a pie graph
 */

public class OneDayGraph extends AppCompatActivity {
    private CustomDate daySelected;
    private Singleton singleton= Singleton.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_day_graph);
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

        float val1 = 0.001f;
        float val2 = 0.001f;
        float val3 = 0.001f;
        float val4 = 0.001f;
        float val5 = 0.001f;
        float val6 = 0.001f;
        if(monthCost.get(0).getNumElectricity()!= 0) {
            val1 = (float) (monthCost.get(0).getNumElectricity());
        }
        if(monthCost.get(0).getNumGas()!=0) {
            val2 = (float) (monthCost.get(0).getNumGas());
        }
        int startDate = CustomDate.toJulian(singleton.returnDay().getYear(),singleton.returnDay()
                .getMonth(),singleton.returnDay().getDay())+0;
        int endDate = CustomDate.toJulian(singleton.returnDay().getYear(),singleton.returnDay()
                .getMonth(),singleton.returnDay().getDay());
        for(Journey journey : singleton.getJourneys()){
            int dateFrom = CustomDate.toJulian(journey.getDateFrom().getYear(),journey.
                    getDateFrom().getMonth(),journey.getDateFrom().getDay());
            int dateTo = CustomDate.toJulian(journey.getDateTo().getYear(),journey.
                    getDateTo().getMonth(),journey.getDateTo().getDay());
            if(journey.getTransportation().isBus() && (startDate>=dateFrom && startDate<=dateTo))
                val3 = val3 + (float) (singleton.getCarbonConsumed(journey)/ MonthlyUtilityBill.
                        getDaysBetween(journey.getDateFrom(),journey.getDateTo()));
            else if(journey.getTransportation().isPedestrian() && (startDate>=dateFrom
                    && startDate<=dateTo))
                val4 = val4 + (float) (singleton.getCarbonConsumed(journey)/ MonthlyUtilityBill.
                        getDaysBetween(journey.getDateFrom(),journey.getDateTo()));
            else if(journey.getTransportation().isSkytrain()&& (startDate>=dateFrom
                    && startDate<=dateTo))
                val5 = val5 + (float) (singleton.getCarbonConsumed(journey)/ MonthlyUtilityBill.
                        getDaysBetween(journey.getDateFrom(),journey.getDateTo()));
            else if(startDate>=dateFrom && startDate<=dateTo)
                val6 = val6 + (float) (singleton.getCarbonConsumed(journey)/ MonthlyUtilityBill.
                        getDaysBetween(journey.getDateFrom(),journey.getDateTo()));
        }
        List<PieEntry> pieEntries = new ArrayList<>();

        pieEntries.add(new PieEntry(val1, getString(R.string.elec)));
        pieEntries.add(new PieEntry(val2, getString(R.string.gas)));
        pieEntries.add(new PieEntry(val3, getString(R.string.bus)));
        pieEntries.add(new PieEntry(val4, getString(R.string.pedestrian)));
        pieEntries.add(new PieEntry(val5, getString(R.string.skytrain)));
        pieEntries.add(new PieEntry(val6, getString(R.string.car)));





        //setup pie chart
        PieDataSet dataset = new PieDataSet(pieEntries, getString(R.string.this_day_carbon_consume));
        dataset.setColors(new int[] {Color.RED, Color.GREEN, Color.GRAY, Color.BLACK, Color.BLUE, Color.YELLOW});
        PieData data = new PieData(dataset);

        //animate
        PieChart chart = (PieChart) findViewById(R.id.oneDayChart);
        chart.setData(data);
        chart.animateY(1000);
        chart.invalidate();
    }
}
