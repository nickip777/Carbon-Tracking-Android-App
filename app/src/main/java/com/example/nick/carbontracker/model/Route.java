package com.example.nick.carbontracker.model;

/**
 * Route class
 * stores nickname, city distance, highway distance, and total distance
 */

public class Route {

    private String Name;
    private double cityDistance;
    private double highwayDistance;
    private double totalDistance;

    //constructor
    public Route(String Name, double highwayDistance, double cityDistance) {
        this.Name = Name;
        this.highwayDistance = highwayDistance;
        this.cityDistance = cityDistance;
        this.totalDistance = this.cityDistance + this.highwayDistance;
    }

    //GETTERS AND SETTERS*****************************************************************************
    public double getTotalDistance() {
        return totalDistance;
    }

    public void updateTotalDistance() {
        this.totalDistance = this.highwayDistance + this.cityDistance;
    }

    public double getCityDistance() {
        return cityDistance;
    }

    public void setCityDistance(double cityDistance) {
        this.cityDistance = cityDistance;
    }

    public double getHighwayDistance() {
        return highwayDistance;
    }

    public void setHighwayDistance(double highwayDistance) {
        this.highwayDistance = highwayDistance;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public boolean isEqual(Route route) {
        double tol = 0.0000001;
        if( this.Name.equals(route.Name) &&
                (this.cityDistance - route.cityDistance) < tol &&
                (this.highwayDistance - route.highwayDistance) < tol)   {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
