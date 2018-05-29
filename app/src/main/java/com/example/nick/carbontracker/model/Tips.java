package com.example.nick.carbontracker.model;

import android.content.Context;

import com.example.nick.carbontracker.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Third iteration of Tips
 * String Array containing tips
 * Boolean Array to determine which tips are being showed
 * Other Booleans to determine which tips should be showed
 * List of Strings to show the tips, will be populated using boolean conditions
 * Some tips were used from the following websites
 * http://cotap.org/reduce-carbon-footprint/
 * https://carbonfund.org/reduce/
 * Created by ahad on 2017-03-17.
 */

public class Tips {

    ////////////////////////////////// CONSTANTS //////////////////////////////////////////////////
    private static final int MAX_NUM_CAR_TRIPS = 10;
    private static final int MAX_SAME_ROUTE_TRIPS = 6;
    private static final double MIN_DISTANCE = 1.5;
    private static final int MIN_CITY_MPG = 10;
    private static final int MIN_HIGHWAY_MPG = 15;
    private static final int LONG_ROUTE_DISTANCE =50;
    private static final int ELEC = 0;
    private static final int GAS = 1;
    private static final int NUM_TIPS = 15;
    private static final int JOURNEY_TIPS_LAST = 8;
    public static final int MAX_TIPS_HISTORY=7;
    private Context context;
    private Singleton singleton;

    ////////////////////////////////// DATA AND LIST //////////////////////////////////////////////
    private String[] TIPS_DATA= new String[NUM_TIPS];
    private List<String> tipsToDisplay;

    ////////////////////////////////// Booleans //////////////////////////////////////////////////
    private Boolean[] isDisplayed = new Boolean[15];
    private Boolean carUsedOnSmallRoutes = Boolean.FALSE;
    private Boolean carMPGLow = Boolean.FALSE;
    private Boolean routeDistanceLong = Boolean.FALSE;
    private Boolean isJourneysEmpty = Boolean.FALSE;
    private Boolean isMonthlyBillsEmpty = Boolean.FALSE;


    ////////////////////////////////// Constructor /////////////////////////////////////////////////
    Tips(Context context) {
        this.context = context;
        singleton = Singleton.getInstance();
        TIPS_DATA = context.getResources().getStringArray(R.array.TIPS);
        tipsToDisplay = new ArrayList<>();
        isJourneysEmpty = singleton.getJourneys().size() == 0;
        isMonthlyBillsEmpty = singleton.getMonthlyBills().size() == 0;

        double journeyCO2Emissions = singleton.totalCarbonConsumedCar();
        double[] CO2consumedUtilities = singleton.averageCO2ConsumedUtilities();

        String promptJourneys = promptJourneys(journeyCO2Emissions);
        String promptUtil = promptUtilities(CO2consumedUtilities);

        for(int i=0; i<NUM_TIPS; i++) {
            isDisplayed[i] = Boolean.FALSE;
        }
        checkIsJourneyCO2Greater(journeyCO2Emissions,CO2consumedUtilities,
                promptJourneys, promptUtil);
    }

    ////////////////////////////////// Methods for Constructor /////////////////////////////////////
    private String promptJourneys(double journeyCO2Emissions) {
        return context.getString(R.string.promptJourneys1) + " " + singleton.numCarTrips() + " " +
                context.getString(R.string.promptJourneys2) + " " +
                journeyCO2Emissions + singleton.getUnit(context) + context.getString(R.string.promptJourneys3) + " ";
    }

    private String promptUtilities(double[] CO2consumedUtilities)    {
        return context.getString(R.string.utilitiesPrompt1) + " " + (singleton.getMonthlyBills().size()*2)
                + " " + context.getString(R.string.utilitiesPrompt2) + " " + CO2consumedUtilities[ELEC]
                + " " +  context.getString(R.string.utilitiesPrompt4) + " " + CO2consumedUtilities[GAS] + singleton.getUnit(context) + " " +
                context.getString(R.string.utilitiesPrompt3) + " " +
                context.getString(R.string.utilitiesPrompt5) + " ";
    }

    private void checkIsJourneyCO2Greater(double journeyCO2, double[] utilitiesCO2,
                                          String journeys, String utilities)   {
        double totalUtilitiesCO2 = utilitiesCO2[ELEC] + utilitiesCO2[GAS];
        Boolean isJourneyCO2Greater = journeyCO2 >= totalUtilitiesCO2;

        if(isJourneyCO2Greater) {
            callJourneyMethods(journeys);
            if(!isMonthlyBillsEmpty) callUtilitiesMethods(utilities, utilitiesCO2);
        }
        else {
            callUtilitiesMethods(utilities, utilitiesCO2);
            if(!isJourneysEmpty) callJourneyMethods(journeys);
        }
    }

    private void callJourneyMethods(String promptJourneys){
        checkCarUsedOnSmallRoutes(promptJourneys);
        checkRouteDistanceLong(promptJourneys);
        setCarMPGLow(promptJourneys);
        checkExceededAverageJourneyCount(promptJourneys);
        checkExcessiveUseOfSameRoute(promptJourneys);

        for (int i = 0; i < JOURNEY_TIPS_LAST; i++) {
            addTip(i, promptJourneys);
        }
    }

    //////////////////////////// Methods for Journey Tips Logic ////////////////////////////////////

    private void callUtilitiesMethods(String promptUtil, double[] CO2consumedUtilities) {
        checkIsElecCO2Greater(CO2consumedUtilities[ELEC],CO2consumedUtilities[GAS], promptUtil);
        for(int i=JOURNEY_TIPS_LAST; i < NUM_TIPS; i++){
            addTip(i,promptUtil);
        }
    }

    // Checks if the most recent journey involves a small route and a car
    private void checkCarUsedOnSmallRoutes(String promptJourneys)  {
        int last = singleton.getJourneys().size()-1;
        if (singleton.isRecentJourneyOnCar()) {
            carUsedOnSmallRoutes =
                    singleton.getJourneys().get(last).getRoute().getTotalDistance() < MIN_DISTANCE;
        }
        if(carUsedOnSmallRoutes){
            String text = promptJourneys + context.getString(R.string.smallDistancePrompt);
            addTip(0,text);
        }
    }

    private void checkRouteDistanceLong(String promptJourneys) {
        int last = singleton.getJourneys().size()-1;
        if(singleton.isRecentJourneyOnCar())   {
            routeDistanceLong =
                    singleton.getJourneys().get(last).getTotalDistance() > LONG_ROUTE_DISTANCE;
        }
        if(routeDistanceLong){
            String text = promptJourneys + context.getString(R.string.longDistancePrompt);
            addTip(1,text);
            addTip(2,text);
            addTip(3,text);
            addTip(5,text);
        }
    }

    // Checks if the most recent journey involves a car with low MPG
    private void setCarMPGLow(String promptJourneys) {
        int last = singleton.getJourneys().size()-1;
        Car car = singleton.getJourneys().get(last).getCar();

        if(singleton.isRecentJourneyOnCar()) {
            carMPGLow = car.getCityMPG() < MIN_CITY_MPG || car.getHighwayMPG() < MIN_HIGHWAY_MPG;
        }
        if(carMPGLow){
            String text = promptJourneys + context.getString(R.string.MPGlowPrompt);
            addTip(6,text);
        }
    }

    private void checkExceededAverageJourneyCount(String promptJourneys)  {
        Boolean exceededAverageJourneyCount = singleton.numCarTrips() > MAX_NUM_CAR_TRIPS;
        if(exceededAverageJourneyCount){
            addTip(4,promptJourneys);
            addTip(7,promptJourneys);
        }
    }

    private void checkExcessiveUseOfSameRoute(String promptJourneys) {
        int numSameRoutes = singleton.numSameRoutes();
        Boolean excessiveUseOfSameRoute = numSameRoutes > MAX_SAME_ROUTE_TRIPS;

        if(excessiveUseOfSameRoute){
            String text = promptJourneys + context.getString(R.string.sameRoutePrompt1) + " " + singleton.numCarTrips() + " " +
                    context.getString(R.string.sameRoutePrompt2) + " " + numSameRoutes + " " +  context.getString(R.string.sameRoutePrompt3);
            addTip(4,text);
            addTip(7,text);
        }
    }

    //////////////////////////// Methods for Utilities Tips Logic ///////////////////////////////////

    private void checkIsElecCO2Greater(double electricityCO2, double gasCO2, String promptUtil)  {
        Boolean isElecCO2Greater = electricityCO2 > gasCO2;
        if(isElecCO2Greater)    {

            addTip(8,promptUtil);
            addTip(10,promptUtil);
            addTip(11,promptUtil);
            addTip(13,promptUtil);
        }
        else {
            addTip(9, promptUtil);
            addTip(12,promptUtil);
            addTip(14,promptUtil);
        }
    }

    private void addTip(int i, String text){
        if(!isDisplayed[i]) {
            tipsToDisplay.add(text+TIPS_DATA[i]);
            isDisplayed[i] = Boolean.TRUE;
        }
    }

    //////////////////////////// Other Methods for Tips ///////////////////////////////////////////

    public Boolean isEmpty(){
        return tipsToDisplay.size() == 0;
    }

    public String getTip(int index)   {
        return tipsToDisplay.get(index);
    }

    public int size()   {
        return tipsToDisplay.size();
    }

    public List<String> getTipsToDisplay() {
        return tipsToDisplay;
    }
}
