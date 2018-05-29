package com.example.nick.carbontracker.model;

/**
 * CAR CLASS
 * stores information about car
 * such as make, model name, nick name, fueltype, transmission, year, cityMPG, highwayMPG, engine displacement
 */

public class Car {
    private String make;
    private String modelName;
    private String nickName;
    private String fuelType;
    private String transmission;
    private int yearMade;
    private double cityMPG;
    private double highwayMPG;
    private double engineDisplacement;
    private int iconID;

    //constructor
    public Car(String make, String modelName, String nickName, String fuelType, String transmission,
               int yearMade, double cityMPG, double highwayMPG, double engineDisplacement,
               int iconID) {
        this.make = make;
        this.modelName = modelName;
        this.nickName = nickName;
        this.fuelType = fuelType;
        this.transmission = transmission;
        this.yearMade = yearMade;
        this.cityMPG = cityMPG;
        this.highwayMPG = highwayMPG;
        this.engineDisplacement = engineDisplacement;
        this.iconID = iconID;
    }

    //SETTERS AND GETTERS************************************************************************
    public int getIconID() {
        return iconID;
    }

    public void setIconID(int iconID) {
        this.iconID = iconID;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }


    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public double getCityMPG() {
        return cityMPG;
    }

    public void setCityMPG(double cityMPG) {
        this.cityMPG = cityMPG;
    }

    public double getHighwayMPG() {
        return highwayMPG;
    }

    public void setHighwayMPG(double highwayMPG) {
        this.highwayMPG = highwayMPG;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getYearMade() {
        return yearMade;
    }

    public void setYearMade(int yearMade) {
        this.yearMade = yearMade;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public double getEngineDisplacement() {
        return engineDisplacement;
    }

    public void setEngineDisplacement(double engineDisplacement) {
        this.engineDisplacement = engineDisplacement;
    }

    public boolean isEqual(Car car) {
        double tol = 0.0000001;
        if( this.make.equals(car.make) &&
                this.modelName.equals(car.modelName) &&
                this.nickName.equals(car.nickName) &&
                this.fuelType.equals(car.fuelType) &&
                this.transmission.equals(car.transmission) &&
                this.yearMade == car.yearMade &&
                (this.cityMPG - car.cityMPG) < tol &&
                (this.highwayMPG - car.highwayMPG) < tol  &&
                (this.engineDisplacement - car.engineDisplacement) < tol) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

}
