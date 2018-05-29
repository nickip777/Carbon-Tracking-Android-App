package com.example.nick.carbontracker.UI.fourWeekGraph;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * a pie graph for four week route version
 */
public class FourWeekRouteGraph extends AppCompatActivity {
    private Singleton singleton= Singleton.getInstance();
    private CustomDate daySelected;
    private int userRouteSize;
    private float[] valueEachRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_four_week_route_graph);
        userRouteSize = singleton.getUserRoutes().size();
        valueEachRoute = new float[userRouteSize];
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


        List<DailyUtilityBill> monthCost = singleton.getUsageDailyBills(daySelected, 28);
        //monthCost.get(0).getNumElectricity();

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        float elecSum = 0f;
        float gasSum = 0f;
        for (int i = 0; i < 28; i++) {
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
        int startDate = CustomDate.toJulian(daySelected.getYear(),daySelected
                .getMonth(),daySelected.getDay())-28;
        int endDate = CustomDate.toJulian(daySelected.getYear(),daySelected
                .getMonth(),daySelected.getDay());
        float val3 = 0.001f;
        int start_date;
        int end_date;
        List<Journey> journeyForPie = new ArrayList<>();
        Journey journey;
        for(int i = 0 ; i<userRouteSize;i++){
            valueEachRoute[i] = 0;
        }
        for(int i = 0; i<singleton.getJourneys().size();i++){
            journey = singleton.getJourneys().get(i); // Journey being used
            int dateFrom = CustomDate.toJulian(journey.getDateFrom().getYear(),journey.
                    getDateFrom().getMonth(),journey.getDateFrom().getDay());
            int dateTo = CustomDate.toJulian(journey.getDateTo().getYear(),journey.
                    getDateTo().getMonth(),journey.getDateTo().getDay());
            if(!(startDate>dateTo) && !(dateFrom>endDate)){ //check if the journey date is between the range of dayselected or not
                if((startDate>dateFrom) && (endDate>dateTo) || (dateFrom>startDate) && (dateTo>endDate)) {
                    // when the range of journey date is intersecting the dayselected range
                    if (startDate>dateFrom)  // if the start of dayselected is higher, then use the start date of dayselected.
                        start_date = startDate;
                    else                        // else, use the start date from the journey
                        start_date = dateFrom;
                    if (endDate>dateTo)      // if the end of dayselected is higher, then use the end date of journey.
                        end_date = dateTo;
                    else                        // else, use the end date from the dayselected
                        end_date = endDate;
                }
                else if((startDate>=dateFrom) && (dateTo>=endDate)){ // when the range of the journey date is bigger than the dayselected
                    start_date = startDate;
                    end_date = endDate;
                }
                else{   // when the range of the dayselected is bigger than the journey date
                    start_date = dateFrom;
                    end_date = dateTo;
                }
                val3 = (float) (singleton.getCarbonConsumed(journey)/ MonthlyUtilityBill.
                        getDaysBetween(journey.getDateFrom(),journey.getDateTo())*(end_date-start_date+1));
                boolean routeFound = false;

                for(int j = 0; j<userRouteSize && !routeFound;j++) {
                    if(journey.getRouteName().equals(singleton.getUserRoutes().get(j).getName())) {
                        valueEachRoute[j] += val3;
                        routeFound = true;
                    }
                }
            }
        }

        List<PieEntry> pieEntries = new ArrayList<>();

        pieEntries.add(new PieEntry(elecSum, getString(R.string.elec)));
        pieEntries.add(new PieEntry(gasSum, getString(R.string.gas)));

        for(int i = 0; i < userRouteSize;i++) {
            pieEntries.add(new PieEntry(valueEachRoute[i], singleton.getUserRoutes().get(i).getName()));
        }

        PieDataSet dataset = new PieDataSet(pieEntries, getString(R.string.fourWeekCarbonConsume));
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        dataset.setValueTextSize(40f);
        PieData data = new PieData(dataset);

        //animate
        PieChart chart = (PieChart) findViewById(R.id.FourWeekRoutePie);
        chart.setData(data);
        chart.animateY(1000);
        chart.invalidate();
    }
}
