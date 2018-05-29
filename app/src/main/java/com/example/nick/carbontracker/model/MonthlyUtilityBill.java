package com.example.nick.carbontracker.model;


import java.text.ParseException;

/**
 * daily bill class, which help use to save and get the daily gas consumed and daily electricity consumed
 */


public class MonthlyUtilityBill {
    private String name;
    private int numUsers;
    private double numElectricity;
    private double numGas;
    private CustomDate dateFrom;
    private CustomDate dateTo;
    private final double GWH_TO_CO2 = 9000;
    private final double GAS_TO_CO2 = 56.1;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MonthlyUtilityBill(String name, int numUsers, double numElectricity, double numGas, CustomDate dateFrom, CustomDate dateTo) {
        this.numUsers = numUsers;
        this.name = name;

        this.numElectricity = numElectricity;
        this.numGas = numGas;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public void setNumUsers(int numUsers) {
        this.numUsers = numUsers;
    }

    public void setNumElectricity(float numElectricity) {
        this.numElectricity = numElectricity;
    }

    public void setNumGas(float numGas) {
        this.numGas = numGas;
    }

    public int getNumUsers() {
        return numUsers;
    }

    public double getNumElectricity() {
        return numElectricity;
    }

    public double getNumGas() {
        return numGas;
    }

    public CustomDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(CustomDate date) {
        this.dateFrom = date;
    }

    public CustomDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(CustomDate date) {
        this.dateTo = date;
    }

    public double getElecUsePerDay() throws ParseException {
        return ((numElectricity * GWH_TO_CO2)/getDaysBetween(this.dateFrom, this.dateTo)) / this.numUsers;
    }

    public double getGasUsePerDay() throws ParseException {
        return ((numGas * GAS_TO_CO2)/getDaysBetween(this.dateFrom, this.dateTo)) / this.numUsers;
    }

    public static int getDaysBetween(CustomDate dateFrom, CustomDate dateTo) throws ParseException {
        int j = CustomDate.toJulian(dateFrom.getYear(),dateFrom.getMonth(), dateFrom.getDay());
        int j2 = CustomDate.toJulian(dateTo.getYear(),dateTo.getMonth(),dateTo.getDay());
        return Math.abs(j2-j+1);
    }


    public void setMonthlyBill(MonthlyUtilityBill bill){
        name = bill.name;
        numUsers = bill.numUsers;
        numElectricity = bill.numElectricity;
        numGas = bill.numGas;
        dateFrom = bill.dateFrom;
        dateTo = bill.dateTo;
    }
}

