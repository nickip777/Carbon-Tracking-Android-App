package com.example.nick.carbontracker.UI.fourWeekGraph;

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
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * a graph that can show the 28 days carbon consume with a stacked bar chart
 */
public class FourWeeksGraph extends AppCompatActivity{
    private CombinedChart mChart;
    private Singleton singleton= Singleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four_weeks_graph);

        mChart = (CombinedChart) findViewById(R.id.chart1);

        mChart.getDescription().setEnabled(false);

        mChart.setMaxVisibleValueCount(40);

        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);

        mChart.setDrawValueAboveBar(false);
        mChart.setHighlightFullBarEnabled(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        mChart.getAxisRight().setEnabled(false);

        XAxis xLabels = mChart.getXAxis();
        xLabels.setPosition(XAxis.XAxisPosition.TOP);
        CombinedData data = new CombinedData();

        try {
            data.setData(generateLineData());

            data.setData(generateBarData());
            //data.setValueTypeface(mTfLight);

            //xAxis.setAxisMaximum(data.getXMax() + 0.25f);

            mChart.setData(data);
            mChart.invalidate();
            mChart.animateX(2500);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);

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
    private LineData generateLineData() {
        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<Entry>();

        for (int index = 0; index < 28; index++)
            entries.add(new Entry(index + 0.5f, 34));

        ArrayList<Entry> entries2 = new ArrayList<Entry>();

        for (int index = 0; index < 28; index++)
            entries2.add(new Entry(index + 0.5f, 43));
        LineDataSet set = new LineDataSet(entries, getString(R.string.target));
        set.setColor(Color.rgb(132,24,16));
        set.setLineWidth(5f);
        set.setCircleColor(Color.BLUE);
        set.setCircleRadius(2.5f);
        set.setFillColor(Color.rgb(132,24,16));
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.rgb(240, 238, 70));

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        LineDataSet set2 = new LineDataSet(entries2, getString(R.string.current));
        set2.setColor(Color.rgb(240, 238, 70));
        set2.setLineWidth(5f);
        set2.setCircleColor(Color.GRAY);
        set2.setCircleRadius(2.5f);
        set2.setFillColor(Color.rgb(240, 238, 70));
        set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set2.setDrawValues(true);
        set2.setValueTextSize(10f);
        set2.setValueTextColor(Color.rgb(240, 238, 70));

        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
        LineData data = new LineData(set, set2);



        return data;
    }

    public BarData generateBarData() throws ParseException {

        List<DailyUtilityBill> monthCost = singleton.getUsageDailyBills(singleton.returnDay(), 28);
        //monthCost.get(0).getNumElectricity();

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i < 28; i++) {
            float val1 = 0.001f;
            float val2 = 0.001f;
            float val3 = 0.001f;
            float val4 = 0.001f;
            float val5 = 0.001f;
            float val6 = 0.001f;
            if(monthCost.get(i).getNumElectricity()!= 0) {
                val1 = (float) (monthCost.get(i).getNumElectricity());
            }
            if(monthCost.get(i).getNumGas()!=0) {
                val2 = (float) (monthCost.get(i).getNumGas());
            }
            int startDate = CustomDate.toJulian(singleton.returnDay().getYear(),singleton.returnDay()
                    .getMonth(),singleton.returnDay().getDay())-28+i;
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

            yVals1.add(new BarEntry(
                    i,

                    new float[]{val1, val2, val3, val4, val5, val6
                    }));
            //, val3, val4, val5, val6
                    //getResources().getDrawable(R.drawable.)));
        }

        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, getString(R.string.carbon_consume_bill));
            set1.setColors(new int[] {Color.RED, Color.GREEN, Color.GRAY, Color.BLACK, Color.BLUE, Color.YELLOW});
            set1.setStackLabels(new String[]{getString(R.string.elec), getString(R.string.gas),
                    getString(R.string.bus), getString(R.string.pedestrian),
                    getString(R.string.skytrain), getString(R.string.car)});

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextColor(Color.WHITE);
            return data;

            //mChart.setData(data);
        }
        return null;
        //mChart.setFitBars(true);
        //mChart.invalidate();
    }

    /*private int[] getColors() {

        int stacksize = 6;

        int[] colors = new int[stacksize];

        for (int i = 0; i < colors.length; i++) {
            colors[i] = ColorTemplate.COLORFUL_COLORS[i];
        }

        return colors;
    }*/
}
