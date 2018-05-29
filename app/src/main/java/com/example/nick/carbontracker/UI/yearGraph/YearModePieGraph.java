package com.example.nick.carbontracker.UI.yearGraph;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
 * show a pie graph of year carbon emission, mode version.
 */
public class YearModePieGraph extends AppCompatActivity {
    private Singleton singleton= Singleton.getInstance();
    private CustomDate daySelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year_mode_pie_graph);
        daySelected = singleton.returnDay();
        try {
            setupChart();
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
    private void setupChart() throws ParseException {


        List<DailyUtilityBill> monthCost = singleton.getUsageDailyBills(singleton.returnDay(), 365);
        //monthCost.get(0).getNumElectricity();

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        float elecSum = 0f;
        float gasSum = 0f;
        for (int i = 0; i < 365; i++) {
            float val1 = 0.0001f;
            float val2 = 0.0001f;

            if (monthCost.get(i).getNumElectricity() != 0) {
                val1 = (float) (monthCost.get(i).getNumElectricity());
            }
            if (monthCost.get(i).getNumGas() != 0) {
                val2 = (float) (monthCost.get(i).getNumGas());
            }
            elecSum += val1;
            gasSum += val2;
        }

        float v3 =0.0001f;
        float v4 =0.0001f;
        float v5 =0.0001f;
        float v6 =0.0001f;

        for (int i = 0; i < 365; i++) {
            float val3 = 0.0001f;
            float val4 = 0.0001f;
            float val5 = 0.0001f;
            float val6 = 0.0001f;
            int startDate = CustomDate.toJulian(singleton.returnDay().getYear(),singleton.returnDay()
                    .getMonth(),singleton.returnDay().getDay())-365+i;
            int endDate = CustomDate.toJulian(singleton.returnDay().getYear(),singleton.returnDay()
                    .getMonth(),singleton.returnDay().getDay());
            for(Journey journey : singleton.getJourneys()){
                int dateFrom = CustomDate.toJulian(journey.getDateFrom().getYear(),journey.
                        getDateFrom().getMonth(),journey.getDateFrom().getDay());
                int dateTo = CustomDate.toJulian(journey.getDateTo().getYear(),journey.
                        getDateTo().getMonth(),journey.getDateTo().getDay());
                if(journey.getTransportation().isBus() && (startDate>=dateFrom && startDate<=dateTo)) {
                    val3 = val3 + (float) (singleton.getCarbonConsumed(journey) / MonthlyUtilityBill.
                            getDaysBetween(journey.getDateFrom(), journey.getDateTo()));
                }
                else if(journey.getTransportation().isPedestrian() && (startDate>=dateFrom
                        && startDate<=dateTo)) {
                    val4 = val4 + (float) (singleton.getCarbonConsumed(journey) / MonthlyUtilityBill.
                            getDaysBetween(journey.getDateFrom(), journey.getDateTo()));
                }
                else if(journey.getTransportation().isSkytrain()&& (startDate>=dateFrom
                        && startDate<=dateTo)) {
                    val5 = val5 + (float) (singleton.getCarbonConsumed(journey) / MonthlyUtilityBill.
                            getDaysBetween(journey.getDateFrom(), journey.getDateTo()));
                }
                else if(startDate>=dateFrom && startDate<=dateTo) {
                    val6 = val6 + (float) (singleton.getCarbonConsumed(journey) / MonthlyUtilityBill.
                            getDaysBetween(journey.getDateFrom(), journey.getDateTo()));
                }
                v3 += val3;
                v4 += val4;
                v5 += val5;
                v6 += val6;

            }
        }



        List<PieEntry> pieEntries = new ArrayList<>();

        pieEntries.add(new PieEntry(elecSum, getString(R.string.elec)));
        pieEntries.add(new PieEntry(gasSum, getString(R.string.gas)));
        pieEntries.add(new PieEntry(v3, getString(R.string.bus)));
        pieEntries.add(new PieEntry(v4, getString(R.string.pedestrian)));
        pieEntries.add(new PieEntry(v5, getString(R.string.skytrain)));
        pieEntries.add(new PieEntry(v6, getString(R.string.car)));






        PieDataSet dataset = new PieDataSet(pieEntries, getString(R.string.fourWeekCarbonConsume));
        dataset.setColors(new int[] {Color.RED, Color.GREEN, Color.GRAY, Color.BLACK, Color.BLUE, Color.YELLOW});
        PieData data = new PieData(dataset);

        //animate
        PieChart chart = (PieChart) findViewById(R.id.yearModePie);
        chart.setData(data);
        chart.animateY(1000);
        chart.invalidate();
    }
}

