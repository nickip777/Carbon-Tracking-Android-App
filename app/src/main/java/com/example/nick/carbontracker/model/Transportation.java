package com.example.nick.carbontracker.model;

/**
 * transportation class, save different transportation mode.
 */

public class Transportation {
    private Car car;
    private boolean pedestrian;
    private boolean bus;
    private boolean skytrain;

    public Transportation() {
        car = new Car("","","","","",0,0,0,0,0);
        pedestrian = false;
        bus = false;
        skytrain = false;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public boolean isPedestrian() {
        return pedestrian;
    }

    public void setPedestrian() {
        this.pedestrian = true;
    }

    public boolean isSkytrain() {
        return skytrain;
    }

    public void setSkytrain() {
        this.skytrain = true;
    }

    public boolean isBus() {
        return bus;
    }

    public void setBus() {
        this.bus = true;
    }
}
