package com.example.nick.carbontracker.model;

import java.text.ParseException;

/**
 * Journey class that stores car and route
 */

public class Journey {
    private Transportation transportation;
    private Route route;
    private CustomDate dateFrom;
    private CustomDate dateTo;

    //constructor
    public Journey(Transportation transportation, Route route, CustomDate dateFrom, CustomDate dateTo) {
        this.transportation = transportation;
        this.route = route;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public double getGasUsedPerDay() throws ParseException {
        return getTotalGasUsed()/MonthlyUtilityBill.getDaysBetween(dateFrom,dateTo);
    }

    //function to calc gas
    public double getTotalGasUsed() {
        double cityKMPG = transportation.getCar().getCityMPG() * 1.69034;
        double highwayKMPG = transportation.getCar().getHighwayMPG() * 1.69034;

        return route.getCityDistance() / cityKMPG + route.getHighwayDistance() / highwayKMPG;
    }

    //SETTERS AND GETTERS*********************************************************

    public Transportation getTransportation() {
        return transportation;
    }

    public void setTransportation(Transportation transportation) {
        this.transportation = transportation;
    }

    public Car getCar() {
        return transportation.getCar();
    }

    public void setCar(Car car) { this.transportation.setCar(car); }

    public CustomDate getDateFrom(){ return dateFrom;}

    public CustomDate getDateTo(){ return dateTo;}

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public double getTotalDistance() {
        return route.getTotalDistance();
    }

    public double getCityDistance() {
        return route.getCityDistance();
    }

    public double getHighwayDistance() {
        return route.getHighwayDistance();
    }


    public String getModelName() {
        return transportation.getCar().getModelName();
    }

    public double getCityMPG() { return transportation.getCar().getCityMPG(); }

    public double getHighwayMPG() {
        return transportation.getCar().getHighwayMPG();
    }

    public String getFuelType() {
        return transportation.getCar().getFuelType();
    }

    public String getMake() {
        return transportation.getCar().getMake();
    }

    public String getNickName() {
        return transportation.getCar().getNickName();
    }

    public String getRouteName() {
        return route.getName();
    }

    public int getYearMade() {
        return transportation.getCar().getYearMade();
    }

    public String getTransmission() {
        return transportation.getCar().getTransmission();
    }

    public double getEngineDisplacement() { return transportation.getCar().getEngineDisplacement(); }
}
