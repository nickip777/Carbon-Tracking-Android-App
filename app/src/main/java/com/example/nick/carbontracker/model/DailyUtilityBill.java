package com.example.nick.carbontracker.model;

/**
 * daily bill class, which help use to save and get the daily gas consumed and daily electricity consumed
 */

public class DailyUtilityBill {
    private double numElectricity=0;
    private double numGas=0;
    private CustomDate date;

    public DailyUtilityBill(CustomDate date) {
        this.date = date;
    }

    public double getNumElectricity() {
        double value = Singleton.getInstance().getUsersPreferredUnits((numElectricity));
        return round(value);
    }

    public void addNumElectricity(double numElectricity) {
        this.numElectricity += numElectricity;
    }

    public double getNumGas() {
        double value = Singleton.getInstance().getUsersPreferredUnits(numGas);
        return round(value);
    }

    public void addNumGas(double numGas) {
        this.numGas += numGas;
    }

    public CustomDate getDate() {
        return date;
    }

    public void setDate(CustomDate date) {
        this.date = date;
    }

    private static double round (double value) {
        int scale = (int) Math.pow(10, 1);
        return (double) Math.round(value * scale) / scale;
    }
}
