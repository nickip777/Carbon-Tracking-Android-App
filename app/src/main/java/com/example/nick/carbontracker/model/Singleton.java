package com.example.nick.carbontracker.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import com.example.nick.carbontracker.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.text.ParseException;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton for app
 * contains list of journeys, user selected cars and routes, and list of all cars
 */

public class Singleton {
    public static final int  LIST_OF_CARS=0;
    public static final int  LIST_OF_ROUTES=1;
    public static final int  LIST_OF_JOURNEYS=2;
    public static final int  LIST_OF_MONTHLYBILLS=3;
    public static final int  TIP_HISTORY=4;
    private String[] make;
    private String[][] model;
    private String[][][] year;
    public static final double CO2_KG_EMITTED_TREE_YEAR = 21.7724;
    private  List<Journey> journeys;
    private  List<Car> cars;
    private  List<Car> userCars;
    private  List<Car> carIconSpinner;
    private  List<Route> userRoutes;
    private  List<MonthlyUtilityBill> monthlybills;
    private  List<String> tipsHistory;
    private  CustomDate daySelected;
    public  void setDay(CustomDate day){
        daySelected = day;
    }
    public  CustomDate returnDay(){
        return daySelected;
    }
    private  Tips tips;
    private  int recursiveCount = 0;
    private  int lastIndex = -1;
    private Boolean humanRelatableUnits = Boolean.TRUE;
    private static volatile Singleton singleton = new Singleton();


    //constructor
    private Singleton() {
        journeys = new ArrayList<>();
        cars = new ArrayList<>();
        userCars = new ArrayList<>();
        userRoutes = new ArrayList<>();
        monthlybills = new ArrayList<>();
        tipsHistory = new ArrayList<>();
        carIconSpinner = new ArrayList<>();

        addCar(new Car("Empty","Empty","Pedestrian","Empty","Empty",0,0,0,0,R.drawable.pedestrian_icon));
        addCar(new Car("Empty","Empty","Bus","Empty","Empty",0,0,0,0,R.drawable.bus_icon));
        addCar(new Car("Empty","Empty","Skytrain","Empty","Empty",0,0,0,0,R.drawable.skytrain_icon));

        carIconSpinner.add(new Car("Empty","Empty","Red Car","Empty","Empty",0,0,0,0,R.drawable.car_red_icon));
        carIconSpinner.add(new Car("Empty","Empty","Blue Car","Empty","Empty",0,0,0,0,R.drawable.car_blue_icon));
        carIconSpinner.add(new Car("Empty","Empty","Orange Car","Empty","Empty",0,0,0,0,R.drawable.car_orange_icon));
        carIconSpinner.add(new Car("Empty","Empty","Purple Car","Empty","Empty",0,0,0,0,R.drawable.car_purple_icon));
        carIconSpinner.add(new Car("Empty","Empty","Racing Car","Empty","Empty",0,0,0,0,R.drawable.racing_car_icon));



        // Set up example list
        Route example1 = new Route("Route XX ", 11.11, 22.22);
        addRoute(example1);
    }

    public static Singleton getInstance() {
        return singleton;
    }

    public  void addJourney(Journey journey) {
        journeys.add(journey);
    }

    public void removeJourney(int position) { journeys.remove(position); }

    //function to add car to car list
    public  void checkCar(Car car, Context context) {
        InputStream is = context.getResources().openRawResource(R.raw.vehicles);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );
        cars.clear();   //clear the saved cars, if any

        String line = "";
        try {
            while ((line = reader.readLine()) != null) {  //input from CSV
                String[] tokens = line.split(",");
                try {
                    //check if valid car
                    if (tokens.length >= 8 && (tokens[0].length() != 0 && tokens[1].length() != 0 && tokens[2].length() != 0     //checking for empty string
                            && tokens[3].length() != 0 && tokens[4].length() != 0 && tokens[5].length() != 0
                            && tokens[6].length() != 0 && tokens[7].length() != 0)) {
                        if (car.getMake().equals(tokens[0]) && car.getModelName().equals(tokens[1])
                                && car.getYearMade() == Integer.parseInt(tokens[4])) {
                            Car temp_car = new Car(tokens[0], tokens[1], car.getNickName(), tokens[2], tokens[3],
                                    Integer.parseInt(tokens[4]), Double.parseDouble(tokens[5]),
                                    Double.parseDouble(tokens[6]), Double.parseDouble(tokens[7]),car.getIconID());
                            cars.add(temp_car);
                        }
                    }
                } catch (NumberFormatException ex) {
                }     //non-number is ignored
            }
        } catch (IOException e) {
            Toast.makeText(context, "Error detected!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void initializeMakeSpinner(Context context) {
        InputStream is = context.getResources().openRawResource(R.raw.vehicles);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );

        String line = "";
        List<String> stringArray = new ArrayList<>();
        try {
            //read each line as string array seperated by commas
            reader.readLine();
            while ((line = reader.readLine()) != null) {  //input from CSV
                String[] tokens = line.split(",");
                if (stringArray.size() == 0)
                    stringArray.add(tokens[0]);
                else {
                    int i = 0;
                    boolean equal = false;
                    //if make not added in list yet, add to array
                    while (i < stringArray.size() && !equal) {
                        if (stringArray.get(i).equals(tokens[0])) {
                            equal = true;
                        } else {
                            i++;
                            if (i == stringArray.size())
                                stringArray.add(tokens[0]);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //transfer list to array
        make = new String[stringArray.size() + 1];
        make[0] = "";
        for (int i = 1; i < stringArray.size() + 1; i++) {
            make[i] = stringArray.get(i - 1);
        }
        model = new String[make.length][];
        year = new String[make.length][][];
    }

    //initialize the model spinner
    public void initializeModelSpinner(Context context) {
        InputStream is = context.getResources().openRawResource(R.raw.vehicles);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );

        String line = "";
        List<String> stringArray = new ArrayList<>();
        int i = 1;
        try {
            //read in each line as string array
            reader.readLine();
            while ((line = reader.readLine()) != null) {  //input from CSV
                String[] tokens = line.split(",");
                //check if make matches
                if (!(make[i].equals(tokens[0]))) {
                    model[i] = new String[stringArray.size() + 1];
                    model[i][0] = " ";
                    for (int j = 1; j < stringArray.size() + 1; j++) {
                        model[i][j] = stringArray.get(j - 1);
                    }
                    i++;
                    stringArray.clear();
                }
                //add model to array
                if (stringArray.size() == 0)
                    stringArray.add(tokens[1]);
                    //check if model already exists
                else {
                    int j = 0;
                    boolean equal = false;
                    while (j < stringArray.size() && !equal) {
                        if (stringArray.get(j).equals(tokens[1])) {
                            equal = true;
                        } else {
                            j++;
                            if (j == stringArray.size())
                                stringArray.add(tokens[1]);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //transfer list to array
        model[i] = new String[stringArray.size() + 1];
        model[i][0] = " ";
        for (int j = 1; j < stringArray.size() + 1; j++) {
            model[i][j] = stringArray.get(j - 1);
        }
    }

    //populate year spinner
    public void initializeYearSpinner(Context context) {
        InputStream is = context.getResources().openRawResource(R.raw.vehicles);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );

        String line = "";
        List<String> stringArray = new ArrayList<>();
        int i = 1;
        int j = 1;
        year[i] = new String[model[i].length][];

        try {
            //read in each line as string array
            reader.readLine();
            while ((line = reader.readLine()) != null) {  //input from CSV
                String[] tokens = line.split(",");
                //check if make matches
                if (make[i].equals(tokens[0])) {
                    if (!(model[i][j].equals(tokens[1]))) {
                        year[i][j] = new String[stringArray.size() + 1];
                        year[i][j][0] = "";
                        for (int k = 1; k < stringArray.size() + 1; k++) {
                            year[i][j][k] = stringArray.get(k - 1);
                        }
                        j++;
                        stringArray.clear();
                    }
                }
                //create new array for years of model
                else {
                    year[i][j] = new String[stringArray.size() + 1];
                    year[i][j][0] = "";
                    for (int k = 1; k < stringArray.size() + 1; k++) {
                        year[i][j][k] = stringArray.get(k - 1);
                    }
                    i++;
                    j = 1;
                    year[i] = new String[model[i].length][];
                    stringArray.clear();
                }

                //add to array
                if (stringArray.size() == 0)
                    stringArray.add(tokens[4]);
                    //check if year already added
                else {
                    int k = 0;
                    boolean equal = false;
                    while (k < stringArray.size() && !equal) {
                        if (stringArray.get(k).equals(tokens[4])) {
                            equal = true;
                        } else {
                            k++;
                            if (k == stringArray.size())
                                stringArray.add(tokens[4]);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //transfer list to array
        year[i][j] = new String[stringArray.size() + 1];
        year[i][j][0] = "";
        for (int k = 1; k < stringArray.size() + 1; k++) {
            year[i][j][k] = stringArray.get(k - 1);
        }
    }

    public String[] getMakeSpinner() {return make;}
    public String[][] getModelSpinner() {return model;}
    public String[][][] getYearSpinner() {return year;}

    public  List<Car> getCarIconSpinner(){return carIconSpinner;}

    public  List<Car> getCars() {
        return cars;
    }

    public  List<Car> getUserCars() {
        return userCars;
    }

    public  void setUserCars(List<Car> userCars) {
        this.userCars = userCars;
    }

    public  List<Route> getUserRoutes() {
        return userRoutes;
    }

    public  void setUserRoutes(List<Route> userRoutes) {
        this.userRoutes = userRoutes;
    }

    public  void addCar(Car car) {
        this.userCars.add(car);
    }

    public  void addRoute(Route route) {
        this.userRoutes.add(route);
    }

    public  void editRoute(int pos, Route newRoute) {
        this.userRoutes.set(pos, newRoute);
    }

    public  void editCar(int pos, Car newCar) {
        this.userCars.set(pos, newCar);
    }

    //return list of routes to print in footprint
    public  String[] getPrintableRouteList() {
        int size = singleton.userRoutes.size();
        Route route;
        String[] listOfRoutes = new String[size];
        for (int i = 0; i < size; i++) {
            route = singleton.userRoutes.get(i);
            listOfRoutes[i] = route.getName() + "\nCity : " +
                    route.getHighwayDistance() + "km \nHighway : " +
                    route.getCityDistance() + "km";
        }
        return listOfRoutes;
    }

    //convert user car list into string array
    public String[] userCarListToString() {
        String[] list = new String[userCars.size()];
        for (int i = 0; i < userCars.size(); i++) {
            list[i] = userCars.get(i).getNickName();
        }
        return list;
    }

    public  List<Journey> getJourneys() {
        return journeys;
    }

    //update cars in journey
    public  void updateJourneysCars(Car newCar, int posOldCar) {
        if (journeys.size() == 0) return;

        for (int i = 0; i < journeys.size(); i++) {
            if (journeys.get(i).getCar().isEqual(userCars.get(posOldCar))) {
                journeys.get(i).setCar(newCar);
            }
        }
    }

    //update routes in journey
    public  void updateJourneysRoutes(Route newRoute, int posOldRoute) {
        if (journeys.size() == 0) return;

        for (int i = 0; i < journeys.size(); i++) {
            if (journeys.get(i).getRoute().isEqual(userRoutes.get(posOldRoute))) {
                journeys.get(i).setRoute(newRoute);
            }
        }
    }

    public  void clearJourneys() {
        journeys.clear();
    }

    public  void clearCars() {
        cars.clear();
    }

    public  void addMonthlyBill(MonthlyUtilityBill bill){
        monthlybills.add(bill);
    }

    public  void removeMonthlyBill(int index){
        monthlybills.remove(index);
    }

    public  MonthlyUtilityBill getMonthlyBill(int pos){
        return monthlybills.get(pos);
    }

    public  List<DailyUtilityBill> getUsageDailyBills(CustomDate dateTo, int numDays) throws ParseException {
        CustomDate dateFrom = dateTo.getLastNumDays(numDays);
        CustomDate dateTemp = dateFrom;
        List<DailyUtilityBill> dailyUsage = new ArrayList<>();
        for(int i = 0; i < numDays; i++){
            dailyUsage.add(new DailyUtilityBill(dateTemp));
            int j = dateTemp.toJulian(dateTemp.getYear(), dateTemp.getMonth(), dateTemp.getDay());
            j++;
            int[] jj = CustomDate.fromJulian(j);
            dateTemp = new CustomDate(jj[0], jj[1],jj[2]);
        }
        for(MonthlyUtilityBill bill: monthlybills){
            for(int i = 0; i < dailyUsage.size(); i++){
                if(dailyUsage.get(i).getDate().compareEqual(bill.getDateFrom()) || dailyUsage.get(i).getDate().compareEqual(bill.getDateTo())){
                    dailyUsage.get(i).addNumElectricity(bill.getElecUsePerDay());
                    dailyUsage.get(i).addNumGas(bill.getGasUsePerDay());
                }else if(dailyUsage.get(i).getDate().compareGreater(bill.getDateFrom())){
                    if(!dailyUsage.get(i).getDate().compareGreater(bill.getDateTo())){
                    dailyUsage.get(i).addNumElectricity(bill.getElecUsePerDay());
                    dailyUsage.get(i).addNumGas(bill.getGasUsePerDay());
                }
                }
            }

        }
        return dailyUsage;
    }

    public  DailyUtilityBill getSingleDayBill(CustomDate dateTo) throws ParseException {
        DailyUtilityBill billTemp = new DailyUtilityBill(dateTo);
        for(MonthlyUtilityBill bill: monthlybills){
            if(billTemp.getDate().compareEqual(bill.getDateFrom()) || billTemp.getDate().compareEqual(bill.getDateTo())){
                billTemp.addNumElectricity(bill.getElecUsePerDay());
                billTemp.addNumGas(bill.getGasUsePerDay());
            }else if(billTemp.getDate().compareGreater(bill.getDateFrom()) && !billTemp.getDate().compareGreater(bill.getDateTo())){
                billTemp.addNumElectricity(bill.getElecUsePerDay());
                billTemp.addNumGas(bill.getGasUsePerDay());
            }
        }
        return billTemp;
    }
    public  double getCarbonConsumed(Journey journey){
        double carbonConsumed;
        double kgPerGallon;
        switch (journey.getFuelType()) {
            case "Regular Gasoline":
            case "Premium Gasoline":
                kgPerGallon = 8.89;
                break;
            case "Diesel":
                kgPerGallon = 10.16;
                break;
            case "Electricity":
                kgPerGallon = 0;
                break;
            default:
                kgPerGallon = 0;
                break;
        }
        if(journey.getTransportation().isBus())
            carbonConsumed = (journey.getCityDistance()+journey.getHighwayDistance())*0.089;
        else if(journey.getTransportation().isSkytrain())
            carbonConsumed = (journey.getCityDistance()+journey.getHighwayDistance())*0.061632;
        else if(journey.getTransportation().isPedestrian())
            carbonConsumed = 0;
        else
            carbonConsumed = journey.getTotalGasUsed() * kgPerGallon;
        carbonConsumed = getUsersPreferredUnits(carbonConsumed);
        carbonConsumed = Math.floor(carbonConsumed * 100) / 100;
        return carbonConsumed;
    }

    public String[] userJourneyToString(){
        String[] list = new String[journeys.size()];
        for (int i = 0; i < journeys.size(); i++) {
            if(journeys.get(i).getTransportation().isBus())
                list[i] = "Bus - " + journeys.get(i).getRouteName();
            else if(journeys.get(i).getTransportation().isSkytrain())
                list[i] = "Skytrain - " + journeys.get(i).getRouteName();
            else if(journeys.get(i).getTransportation().isPedestrian())
                list[i] = "Pedestrian - " + journeys.get(i).getRouteName();
            else
                list[i] = journeys.get(i).getNickName() + " " + journeys.get(i).getRouteName();
        }
        return list;
    }

    public void createTips(Context context) {
        tips = new Tips(context);
    }

    public  String getTip(int index) {
        recursiveCount = 0;
        index = checkIndex(index);
        addTipToHistory(index);
        return tips.getTip(index);
    }

    public  String getTipPrevious(int index) {
        recursiveCount = 0;
        index = checkIndex(index);
        addTipToHistory(index);
        return tips.getTip(index);
    }


    private  int checkIndex(int index) {
        if(tipsHistory.size() == 0) return index;
        else    {
            recursiveCount++;
            if(recursiveCount > 7){
                lastIndex = (lastIndex +1)% tips.size();
                return lastIndex;
            }
            if(inHistory(tips.getTip(index))){
                index = (index+1) % tips.size();
                index = checkIndex(index);
            }
            return index;
        }
    }

    private  boolean inHistory(String text) {
        for (String tip : tipsHistory)  {
            if(text.equals(tip)) return true;
        }
        return false;
    }

    private  void addTipToHistory(int index) {
        if(tipsHistory.size() > Tips.MAX_TIPS_HISTORY)  {
            tipsHistory.remove(tipsHistory.size()-1);
        }
        if(!inHistory(tips.getTip(index))) {
            tipsHistory.add(0, tips.getTip((index)));
        }
    }

    public  Boolean getTipsIsEmpty() {
        return tips.isEmpty();
    }
    public  int getTipsSize() {
        return  tips.size();
    }

    public  List<MonthlyUtilityBill> getMonthlyBills() {
        return monthlybills;
    }

    //convert user bill list into string array
    public  String[] userBillListToString() {
        String[] list = new String[monthlybills.size()];
        for (int i = 0; i < monthlybills.size(); i++) {
            list[i] = monthlybills.get(i).getName();
        }
        return list;
    }

    public  Boolean isRecentJourneyOnCar(){
        if(!journeys.get(journeys.size()-1).getCar().getModelName().equals("Empty")){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public  int numCarTrips() {
        int count =0;
        for(int i=0; i <journeys.size(); i++){
            if (!journeys.get(i).getCar().getModelName().equals("Empty")) count++;
        }
        return count;
    }

    public  int numSameRoutes()   {
        int maxCount=0;
        int count=0;
        for(Route route : userRoutes){
            count=0;
            for(int j=0; j < journeys.size() ;j++)  {
                if (journeys.get(j).getCar().getModelName().equals("Empty") == Boolean.FALSE) {
                    if (route.equals(journeys.get(j).getRoute())) count++;
                }
            }
            if(count > maxCount) maxCount = count;
        }
        return maxCount;
    }

    public  double totalCarbonConsumedCar() {
        double carbonConsumed = 0;
        for(Journey journey : journeys){
            if (!journey.getModelName().equals("Empty")){
                carbonConsumed += getCarbonConsumed(journey);
            }
        }
        carbonConsumed = (Math.ceil(carbonConsumed*100)) / 100;
        return carbonConsumed;
    }

    public  double[] averageCO2ConsumedUtilities() {
        double carbonConsumedElec = 0;
        double carbonConsumedGas = 0;

        if(monthlybills.size() == 0) return new double[] {carbonConsumedElec,carbonConsumedGas};

        for(MonthlyUtilityBill bill: monthlybills){
            try {
                carbonConsumedElec += bill.getElecUsePerDay();
                carbonConsumedGas += bill.getGasUsePerDay();
            }
            catch (ParseException e) {
                Log.d("singleton", "Caught Parse Exception");
            }
        }

        double averageCO2Elec = getUsersPreferredUnits(carbonConsumedElec/monthlybills.size());
        double averageCO2Gas =  getUsersPreferredUnits(carbonConsumedGas/monthlybills.size());
        averageCO2Elec = (Math.ceil(averageCO2Elec*100)) / 100;
        averageCO2Gas = (Math.ceil(averageCO2Gas*100)) / 100;
        return new double[] {averageCO2Elec,averageCO2Gas};
    }

    public void saveUserData(Context context, int listNum)  {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json;
        switch(listNum) {
            case LIST_OF_CARS:
                json = gson.toJson(userCars);
                prefsEditor.putString("UserCars", json);
                break;
            case LIST_OF_ROUTES:
                json = gson.toJson(userRoutes);
                prefsEditor.putString("UserRoutes", json);
                break;
            case LIST_OF_JOURNEYS:
                json = gson.toJson(journeys);
                prefsEditor.putString("Journeys", json);
                break;
            case LIST_OF_MONTHLYBILLS:
                json = gson.toJson(monthlybills);
                prefsEditor.putString("MonthlyBills", json);
                break;
            case TIP_HISTORY:
                json = gson.toJson(tipsHistory);
                prefsEditor.putString("TipHistory", json);
                break;

            default:
                Log.d("Singleton", "Invalid key");
        }
        prefsEditor.apply();
    }

    public void loadUserData(Context context, int listNum)  {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Type listType; // For loading
        Gson gson = new Gson();
        String json;
        switch(listNum) {
            case LIST_OF_CARS:
                json = mPrefs.getString("UserCars", "");
                listType = new TypeToken<ArrayList<Car>>(){}.getType();
                if(!json.equals("")) {
                    userCars = gson.fromJson(json, listType);
                }
                break;
            case LIST_OF_ROUTES:
                json = mPrefs.getString("UserRoutes", "");
                listType = new TypeToken<ArrayList<Route>>(){}.getType();
                if(!json.equals("")) {
                    userRoutes = gson.fromJson(json, listType);
                }
                break;
            case LIST_OF_JOURNEYS:
                json = mPrefs.getString("Journeys", "");
                listType = new TypeToken<ArrayList<Journey>>(){}.getType();
                if(!json.equals("")) {
                    journeys = gson.fromJson(json, listType);
                }
                break;
            case LIST_OF_MONTHLYBILLS:
                json = mPrefs.getString("MonthlyBills", "");
                listType = new TypeToken<ArrayList<MonthlyUtilityBill>>(){}.getType();
                if(!json.equals("")) {
                    monthlybills = gson.fromJson(json, listType);
                }
                break;
            case TIP_HISTORY:
                json = mPrefs.getString("TipHistory", "");
                listType = new TypeToken<ArrayList<String>>(){}.getType();
                if(!json.equals("")) {
                    tipsHistory = gson.fromJson(json, listType);
                }
                break;

            default:
                Log.d("Singleton", "Invalid key");
        }
    }

    public double getUsersPreferredUnits(double result){
        if(!humanRelatableUnits) return result;
        else return result/ CO2_KG_EMITTED_TREE_YEAR;
    }

    public void setHumanRelatableUnits(Boolean humanRelatableUnits){
        this.humanRelatableUnits = humanRelatableUnits;
    }

    public Boolean getHumanRelatableUnits(){
        return this.humanRelatableUnits;
    }

    public String getUnit(Context context){
        if(humanRelatableUnits) return " " + context.getString(R.string.treeYearUnit);
        else return " " + context.getString(R.string.kgUnit);
    }

    public int numJourneysEnteredToday(){
        int result=0;
        CustomDate today = new CustomDate(0);

        for(Journey journey : journeys){
            if(journey.getDateTo().compareEqual(today)){
                result++;
            }
            Log.d("Singleton","Journey Date is " + journey.getDateTo().toString());
            Log.d("Singleton","Required Date is " + today.toString() );
        }
        Log.d("Singleton","Number of Journeys entered today is " + result);
        return result;
    }

    public Boolean utilityBillEnteredRecently(){
        CustomDate today = new CustomDate(0);
        CustomDate lastBillDate;
        int MAX_DIFF = 45;
        int diff = 99999;
        int daysBetween;

        for(MonthlyUtilityBill monthlyUtilityBill :monthlybills)    {
            lastBillDate = monthlyUtilityBill.getDateTo();
            try {
                daysBetween = MonthlyUtilityBill.getDaysBetween(lastBillDate, today);
                if (diff > daysBetween) diff = daysBetween;
                Log.d("Singleton","Days Between = " + daysBetween);
            }
            catch (ParseException e)    {
                Log.d("Singleton","Exception caught while getting days between");
            }
        }

        return diff <= 45;
    }
}
